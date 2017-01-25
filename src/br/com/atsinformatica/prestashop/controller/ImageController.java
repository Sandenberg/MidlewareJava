package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.dao.ParaUrlDAO;
import br.com.atsinformatica.erp.dao.ProdImgDAO;
import br.com.atsinformatica.erp.entity.ImgProdBean;
import br.com.atsinformatica.erp.entity.ParaUrlWsdlBean;
import br.com.atsinformatica.erp.entity.ProdutoERPBean;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.prestashop.clientDAO.GenericPrestashopDAO;
import br.com.atsinformatica.prestashop.clientDAO.ProductPrestashopDAO;
import br.com.atsinformatica.prestashop.model.list.Images;
import br.com.atsinformatica.prestashop.model.node.ImageNode;
import br.com.atsinformatica.prestashop.model.root.ImageProduct;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import br.com.atsinformatica.prestashop.model.root.Product;
import br.com.atsinformatica.utils.SingletonUtil;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Serviço de upload de imagens do produto na loja virtual
 *
 * @author AlexsanderPimenta
 */
public class ImageController extends GenericPrestashopDAO<ImageNode> {
    private final Logger logger = Logger.getLogger(ImageController.class);

    /**
     * Prepara upload de imagens do produto
     *
     * @param idProduct
     */
    public void prepareUpload(ProdutoERPBean prod) {
        if (ParaEcomService.getDiretorioImagens().isEmpty()) {
            logger.error("Não foi configurado nos parâmetros do middle, o diretório das imagens.");
            return;
        }
        
        ProdImgDAO prodImgDao = SingletonUtil.get(ProdImgDAO.class);
        try {
            ParaUrlWsdlBean urlWsdl = ParaEcomService.getUrlBean();
            
            //chave e url do web service
            String urlWS = urlWsdl.getUrlWSDL();
            String urlKey = urlWsdl.getUrlKey();
            
            //lista de imagens para o produto
            List<ImgProdBean> listaImgs = prodImgDao.listaImgCodProd(prod.getCodProd());
            this.deleteImagesProduct(prod.getIdProdutoEcom());
            
            for (ImgProdBean imgProdBean : listaImgs) {
                File file = new File(ParaEcomService.getDiretorioImagens() + imgProdBean.getUrlImagem());
                if (file.exists() && !file.isDirectory()) {
                    uploadLocalImage(urlKey, urlWS, prod.getIdProdutoEcom(), file);
                } else {
                    throw new RuntimeException(MessageFormat.format("Imagem \"{0}\" não encontrada para o produto {1}", 
                            file.getName(), imgProdBean.getCodProd()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna lista de imagens para um determinado produto
     *
     * @param idProdEcom id do produto no e-commerce
     * @return lista de urls
     */
    public ImageProduct retornaImagens(String idProdEcom) {
        Prestashop p = getWebResource().path(ImageProduct.URLIMAGESPRODUCT)
                .path(String.valueOf(idProdEcom)).type(MediaType.APPLICATION_XML).get(Prestashop.class);
        return p.getImage();
    }

    /**
     * Deleta imagens do produto
     *
     * @param codProdEcom
     */
    private void deleteImagesProduct(int codProdEcom) {
        ProductPrestashopDAO prodDao = new ProductPrestashopDAO();
        try {
            Product p = prodDao.getId(Product.URLPRODUCTS, codProdEcom);
            if (p.getAssociations().getImages() != null) {
                if (p.getAssociations().getImages().getImage() != null) {
                    for (ImageNode img : p.getAssociations().getImages().getImage()) {
                        getWebResource().path("images/products").path(String.valueOf(codProdEcom))
                                .path(img.getId()).type(MediaType.APPLICATION_XML).delete();
                    }
                }
            }
            Logger.getLogger(ImageController.class).info("Imagens do produto deletadas com sucesso.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Realiza upload de imagem local
     *
     * @param key chave do webservice
     * @param urlService url do webservice
     * @param id id do produto
     * @param urlImage url da imagem
     * @throws IOException
     */
    public void uploadLocalImage(String key, String urlService, int id, File image) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        try {            
            String service = urlService.replaceAll("^(https|http)://", "");
            String urlPost = "http://" + key + "@" + service + "/images/products/" + id;
            
            String contentType = URLConnection.guessContentTypeFromName(image.getName());
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addBinaryBody("image", image, ContentType.create(contentType), image.getName())
                .build();
            
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost(urlPost);
            httppost.setEntity(entity);    
            
            HttpResponse response = httpclient.execute(httppost);
            
            if(logger.isDebugEnabled()){
                logger.debug("Parametros POST: " + httppost.getEntity()); 
                logger.debug("Executando requisição: " + httppost.getRequestLine());
                logger.debug("Codigo de retorno: " + response.getStatusLine().getStatusCode());
                logger.debug("Retorno: \n" + EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
            
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("Upload da imagem " + image.getAbsolutePath() + " realizado com sucesso!");
            } else {
                StringBuilder sb = new StringBuilder("Erro ao realizar upload da imagem ")
                        .append(image.getAbsolutePath())
                        .append("\n\tCode erro: ").append(response.getStatusLine().getStatusCode())
                        .append("\n\tReturn erro:\n")
                        .append(EntityUtils.toString(response.getEntity()).replaceAll("\n", "\n\t\t"))
                        .append("\n\n");
                
                throw new RuntimeException(sb.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception e) {
                // Não faz nada
            }
        }
    }
    
    /**
     * Retorna todos os produtos que possuiem imagem
     * 
     * @return 
     */
    public Images retornaProdutosImages() {
        Prestashop prestashop = getWebResource().path(ImageProduct.URLIMAGESPRODUCT).type(MediaType.APPLICATION_XML).get(Prestashop.class);
        return prestashop != null ? prestashop.getImages() : null;
    }
}
