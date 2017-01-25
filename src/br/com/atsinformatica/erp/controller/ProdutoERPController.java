package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.controller.trigger.TriggerController;
import br.com.atsinformatica.erp.dao.CategoriaEcomDAO;
import br.com.atsinformatica.erp.dao.ProdImgDAO;
import br.com.atsinformatica.erp.dao.ProdutoCategoriaERPDAO;
import br.com.atsinformatica.erp.dao.ProdutoDAO;
import br.com.atsinformatica.erp.entity.ProdutoCategoriaERPBean;
import br.com.atsinformatica.erp.entity.ProdutoERPBean;
import br.com.atsinformatica.midler.exception.ErroSyncException;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.ui.PanelHistorico;
import br.com.atsinformatica.prestashop.clientDAO.ProductPrestashopDAO;
import br.com.atsinformatica.prestashop.controller.ProductController;
import br.com.atsinformatica.prestashop.model.root.Product;
import br.com.atsinformatica.prestashop.controller.ImageController;
import br.com.atsinformatica.prestashop.model.node.CategoryNode;
import br.com.atsinformatica.utils.SingletonUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Controladora de sincronização do produto do ERP
 *
 * @author AlexsanderPimenta
 */
public class ProdutoERPController extends SincERPController<ProdutoERPBean> {
    //controladora do produto no prestashop

    private ProductController prodController;
    private ProdGradeERPController prodErpController = new ProdGradeERPController();

    public ProdutoERPController() {
        prodController = new ProductController();
    }

    @Override
    public void post(ProdutoERPBean obj) throws Exception {
        try {
            Product p = prodController.createProductPrestashop(obj);
            if (p != null) {
                obj.setIdProdutoEcom(Integer.parseInt(p.getId().getContent()));
                SingletonUtil.get(ProdutoDAO.class).alterar(obj);
                
                prodErpController.atualizaEstoque(obj);
                this.uploadImagesProduct(obj);
            }
            Logger.getLogger(ProdutoERPController.class).info("Produto cadastrado com sucesso na loja virtual.");
        } catch (Exception e) {                
            Logger.getLogger(ProdutoERPController.class).info("Erro ao cadastrar produto na loja virtual: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(ProdutoERPBean obj) throws Exception {
        int codStatus = prodController.updateProduto(obj);
        switch (codStatus) {
            case 404:
                throw new ErroSyncException(codStatus, MessageFormat.format("Não foi encontrado nenhum "
                        + "produto com código {0} no e-commerce.", obj.getIdProdutoEcom()));
            case 500:
                throw new ErroSyncException(codStatus, MessageFormat.format("Erro 500 no servidor"
                        + " - produto com código {0} no e-commerce.", obj.getIdProdutoEcom()));    
        }
        
        prodErpController.atualizaEstoque(obj);
        this.uploadImagesProduct(obj);
        return codStatus;
    }

    @Override
    public int delete(String id) throws Exception {
        return prodController.deleteProduct(id);
    }

    /**
     * Retorna produto da loja virtual pelo id do produto na loja virtual
     *
     * @param id id do produto na loja virtual
     * @return Product
     */
    public Product get(int id) {
        return prodController.getProductById(id);
    }

    private void uploadImagesProduct(ProdutoERPBean prod) {
        ImageController imgService = new ImageController();
        imgService.prepareUpload(prod);
    }

    /**
     * Inicia processo de sincronização de produtos da loja virtual para o ERP
     */
    public void cadastraProdutos() throws SQLException, InstantiationException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ProductPrestashopDAO prodPSDAO = new ProductPrestashopDAO();
        TriggerController triggerController = new TriggerController();
        try {
            //lista de produtos cadastrados na loja virtual
            List<Product> listaProdWs = prodPSDAO.get("products/");
            //List<ProdutoERPBean> produtos = new ArrayList<>();
            //desativa triggers enquanto estiver sincronizando produtos
            triggerController.setActive("PRODUTO_ECOMM_AI", "INACTIVE");
            triggerController.setActive("PRODUTO_ECOMM_AU", "INACTIVE");
            if (!listaProdWs.isEmpty()) {
                for (Product product : listaProdWs) {
                    if (!produtoDAO.produtoExiste(Integer.parseInt(product.getId().getContent()))) {
                        ProdutoERPBean prodBean = produtoDAO.abrirPorRefFabric(product.getSku());
                        if (prodBean != null) {
                            Connection conn = ConexaoATS.getConnection();
                            conn.setAutoCommit(false);
                            prodBean.setDescricaoCompleta(product.getDescription().getTextDescription());
                            prodBean.setDescricaoBreve(product.getDescriptionShort().getTextDescriptionShort());
                            prodBean.setNomeProd(product.getName().getTextName());
                            prodBean.setPrecoCheio(Double.valueOf(product.getPrice().getContent()));
                            prodBean.setEmOferta("N");
                            prodBean.setCondicao(0);
                            prodBean.setAltura(Double.valueOf(product.getHeight()));
                            prodBean.setLargura(Double.valueOf(product.getWidth()));
                            prodBean.setProfundidade(Double.valueOf(product.getWeight()));
                            prodBean.setMostraNaLoja("S");
                            prodBean.setImportaProdEcom(1);
                            prodBean.setIdProdutoEcom(Integer.valueOf(product.getId().getContent()));
                            ConexaoATS.getConnection().setAutoCommit(false);
                            PanelHistorico.textArea.append("Gravando dados complementares do produto " + prodBean.getNomeProd() + "\n");
                            //grava dados adicionais do produto
                            produtoDAO.gravar(prodBean);
                            //grava categorias do produto
                            cadastraCategorias(prodBean.getCodProd(), product.getAssociations().getCategories().getCategory());                            
                            //grava id da loja virtual no produto e seta como importado
                            produtoDAO.alterar(prodBean);
                            //commita operação
                            conn.commit();
                            conn.setAutoCommit(true);
                            //fecha conexão
                            PanelHistorico.textArea.append("Dados complementares do produto  " + prodBean.getNomeProd() + " gravado com sucesso " + "\n");
                        }
                    }
                }
            }
            triggerController.setActive("PRODUTO_ECOMM_AI", "ACTIVE");
            triggerController.setActive("PRODUTO_ECOMM_AU", "ACTIVE");
        } catch (Exception e) {
            Logger.getLogger(ProdutoERPController.class).error("Erro ao gravar produto: " + e);
            PanelHistorico.textArea.append("Erro ao gravar dados complementares do produto :" + e + "\n");
            PanelHistorico.progressBar.setIndeterminate(false);
            ConexaoATS.getConnection().rollback();
        }
    }

    public void cadastraProduto(Product prod) throws SQLException, InstantiationException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();
            //  if (!produtoDAO.produtoExiste(Integer.parseInt(prod.getId().getContent()))) {
            ProdutoERPBean prodBean = produtoDAO.abrirPorRefFabric(prod.getSku());
            if (prodBean != null) {
                conn.setAutoCommit(false);
                prodBean.setDescricaoCompleta(prod.getDescription().getTextDescription());
                prodBean.setDescricaoBreve(prod.getDescriptionShort().getTextDescriptionShort());
                prodBean.setNomeProd(prod.getName().getTextName());
                prodBean.setPrecoCheio(Double.valueOf(prod.getPrice().getContent()));
                prodBean.setEmOferta("N");
                prodBean.setCondicao(0);
                prodBean.setAltura(Double.valueOf(prod.getHeight()));
                prodBean.setLargura(Double.valueOf(prod.getWidth()));
                prodBean.setProfundidade(Double.valueOf(prod.getWeight()));
                prodBean.setMostraNaLoja("S");
                prodBean.setImportaProdEcom(1);
                prodBean.setIdProdutoEcom(Integer.valueOf(prod.getId().getContent()));
                //grava dados adicionais do produto
                produtoDAO.gravar(prodBean);
                //grava categorias do produto
                cadastraCategorias(prodBean.getCodProd(), prod.getAssociations().getCategories().getCategory());                
                //grava id da loja virtual no produto e seta como importado
                produtoDAO.alterar(prodBean);                
                
                //commita operação
                conn.commit();
                //  conn.setAutoCommit(true);
                //    }
            }
        } catch (Exception e) {
            Logger.getLogger(ProdutoERPController.class).error("Erro ao gravar dados complementares do produto: " + e);
            conn.rollback();
        }
    }

    /**
     * Cadastra categorias do produto
     *
     * @param codProd código do produto
     * @param categorias lista de categorias do produto
     */
    private void cadastraCategorias(String codProd, List<CategoryNode> categorias) {
        ProdutoCategoriaERPDAO prodCategoriaDAO = new ProdutoCategoriaERPDAO();
        CategoriaEcomDAO categoriaDAO = new CategoriaEcomDAO();
        try {
            if (!categorias.isEmpty()) {
                for (CategoryNode nod : categorias) {
                    ProdutoCategoriaERPBean prodCat = new ProdutoCategoriaERPBean();
                    prodCat.setCodProd(codProd);
                    prodCat.setCodCategoria(categoriaDAO.retornaCodCategoria(nod.getId()));
                    prodCategoriaDAO.gravar(prodCat);
                    // = categoriaDAO.retornaCodCategoria(nod.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
