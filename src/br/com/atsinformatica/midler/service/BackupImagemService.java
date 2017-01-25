package br.com.atsinformatica.midler.service;

import br.com.atsinformatica.erp.dao.ParaUrlDAO;
import br.com.atsinformatica.erp.dao.ProdImgDAO;
import br.com.atsinformatica.erp.dao.ProdutoDAO;
import br.com.atsinformatica.erp.entity.ParaUrlWsdlBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.prestashop.controller.ImageController;
import br.com.atsinformatica.prestashop.controller.ProductController;
import br.com.atsinformatica.prestashop.model.list.Images;
import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttribute;
import br.com.atsinformatica.prestashop.model.node.Declination;
import br.com.atsinformatica.prestashop.model.root.ImageProduct;
import br.com.atsinformatica.utils.Function;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public class BackupImagemService {
    private static final Logger logger = Logger.getLogger(BackupImagemService.class);
    private static BackupImagemService instance;
    
    private ThreadLocal<ParaUrlWsdlBean> urlWsdl;
    private ProductController productController;
    private ImageController imageController;
    private ProdutoDAO produtoDAO;
    private ProdImgDAO imageDAO;
    private boolean inProcess;

    private static BackupImagemService getInstance() {
        if (instance == null) {
            instance = new BackupImagemService();
        }
        return instance;
    }

    public static void realizaBackup() {
        realizaBackup(null);
    }
    
    public static void realizaBackup(Function<String> registraLog) {
        Connection connection = null;
        try {
            connection = ConexaoATS.getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            
            Images products = getInstance().getImageController().retornaProdutosImages();
            if (products.getImage() != null || products.getImage().isEmpty()) {
                for(AccessXMLAttribute attribute : products.getImage()){
                    String codigo = getInstance().getProdutoDAO().retornaCodProdutoERP(attribute.getId());
                    if (codigo == null || codigo.isEmpty()) {
                        if (registraLog != null) {
                            registraLog.run("Produto não encontrado na base local, ID: " + attribute.getId());
                        }
                        continue;
                    }

                    ImageProduct images = getInstance().getImageController().retornaImagens(attribute.getId());
                    int contador = 1;
                    for(Declination declination : images.getDeclination()){
                        String nome = getInstance().downloadImg(codigo, declination);
                        getInstance().getImageDAO().gravar(codigo, nome, contador);                        

                        if (registraLog != null) {
                            registraLog.run(MessageFormat.format("Backup realizado com sucesso da imagem {0} "
                                    + "para o produto {1}. URL da imagem no e-commerce: {2}", 
                                    nome, codigo, declination.href));
                        }
                        contador++;
                    }
                }
            } else {
                registraLog.run("Não existe nenhum produto com imagem.");
            }
            
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException | NullPointerException ex) {}
            
            logger.error("Erro ao fazer download", e);
            throw new RuntimeException(e);
        }
    }
    
    private String getNomeImagem(String codProduto){
        String nome = null;
        do{
            String hash = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));
            hash = hash.substring(0, 5).toUpperCase();
            
            nome = codProduto + "-" + hash  + ".jpg";
        } while (new File(ParaEcomService.getDiretorioImagens() + nome).exists());
        
        return nome;
    }
    
    private String downloadImg(String codigo, Declination declination) {
        try {    
            String nome = this.getNomeImagem(codigo);
            String url = declination.href.replaceAll("^(http|https)://", ""); 
            
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://" + getUrlWsdl().getUrlKey() + "@" + url);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedInputStream bis = new BufferedInputStream(instream);
                File file = new File(ParaEcomService.getDiretorioImagens() + nome);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                int inByte;
                while ((inByte = bis.read()) != -1) {
                    bos.write(inByte);
                }
                bis.close();
                bos.close();
            }
            
            return nome;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* ********* */
    
    public ParaUrlWsdlBean getUrlWsdl() {
        if (urlWsdl == null || urlWsdl.get() == null) {
            ParaUrlWsdlBean bean = ParaEcomService.getUrlBean();
            
            urlWsdl = new ThreadLocal<>();
            urlWsdl.set(bean);
        }
        return urlWsdl.get();
    }
        
    public ImageController getImageController() {
        if (imageController == null) {
            imageController = new ImageController();
        }
        return imageController;
    }

    public ProductController getProductController() {
        if (productController == null) {
            productController = new ProductController();
        }
        return productController;
    }

    public ProdutoDAO getProdutoDAO() {
        if (produtoDAO == null) {
            produtoDAO = new ProdutoDAO();
        }
        return produtoDAO;
    }
    
    public ProdImgDAO getImageDAO() {
        if (imageDAO == null) {
            imageDAO = new ProdImgDAO();
        }
        return imageDAO;
    }
}
