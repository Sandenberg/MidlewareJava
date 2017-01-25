package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.dao.AtributoGradeEcomDAO;
import br.com.atsinformatica.erp.dao.CadFabrERPDAO;
import br.com.atsinformatica.erp.dao.CategoriaEcomDAO;
import br.com.atsinformatica.erp.dao.GradeERPDAO;
import br.com.atsinformatica.erp.dao.ParaUrlDAO;
import br.com.atsinformatica.erp.dao.ProdGradeERPDAO;
import br.com.atsinformatica.erp.dao.ProdutoCategoriaERPDAO;
import br.com.atsinformatica.erp.dao.SubGradeERPDAO;
import br.com.atsinformatica.erp.entity.CadFabricERPBean;
import br.com.atsinformatica.erp.entity.CategoriaEcomErpBean;
import br.com.atsinformatica.erp.entity.GradeERPBean;
import br.com.atsinformatica.erp.entity.ProdGradeERPBean;
import br.com.atsinformatica.erp.entity.ProdutoCategoriaERPBean;
import br.com.atsinformatica.prestashop.model.node.Name;
import br.com.atsinformatica.prestashop.model.node.Language;
import br.com.atsinformatica.erp.entity.ProdutoERPBean;
import br.com.atsinformatica.erp.entity.SubGradeERPBean;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.prestashop.clientDAO.ProductPrestashopDAO;
import br.com.atsinformatica.prestashop.model.list.prestashop.WSItens;
import br.com.atsinformatica.prestashop.model.node.*;
import br.com.atsinformatica.prestashop.model.root.Product;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;

/**
 *
 * @author ricardosilva
 */
public class ProductController {

    List<ProdutoERPBean> listProduct;
    private ProductPrestashopDAO prodPsDAO = new ProductPrestashopDAO();
    private CadFabrERPDAO fabrERPDao;
    private CadFabricERPBean cadFabric;

    /**
     * Adiciona um produto no prestashop e caso possua categoria ele salva em
     * uma existente ou cria uma e associa.
     *
     * @param listProdutoERP
     * @return
     */
    public Product createProductPrestashop(ProdutoERPBean produtoErp) throws SQLException {
        
        return new ProductPrestashopDAO().postProduct(Product.URLPRODUCTS, createProduct(produtoErp));
    }

    public int updateProduto(ProdutoERPBean produtoERPBean) throws SQLException {
        
        return new ProductPrestashopDAO().put(Product.URLPRODUCTS, updateProduct(produtoERPBean));
    }

    public int deleteProduct(String id) throws SQLException {
        return new ProductPrestashopDAO().delete(Product.URLPRODUCTS, id);
    }

    public Product getProductById(int id) {
        return new ProductPrestashopDAO().getId(Product.URLPRODUCTS, id);
    }
    
    public Product getProductByAttribute(String id) {        
        return prodPsDAO.getProductByAttributeId(Product.URLPRODUCTS, id);
    }

    /**
     * Faz o processo de conversão do produto do ERP para loja virtual
     *
     * @param produtoERP
     * @return Product
     */
    private Product createProduct(ProdutoERPBean produtoERP) {
        Product p = new Product();
        Name name = new Name();
        AssociationsNode associations = new AssociationsNode();
        Description description = new Description();
        DescriptionShort descriptionShort = new DescriptionShort();
        MetaDescription metaDesc = new MetaDescription();
        MetaKeyWord metaKey = new MetaKeyWord();
        Price price = new Price();
        LinkRewrite linkRewrite = new LinkRewrite();
        try {
            if (produtoERP.getIdProdutoEcom() != 0) {
                Id id = new Id();
                id.setContent(String.valueOf(produtoERP.getIdProdutoEcom()));
                p.setId(id);
                associations.setCombinations(this.createCombinations(produtoERP));
            }
            if (produtoERP.getNomeProd() != null) 
                name.getLanguage().add(new Language(produtoERP.getNomeProd()));
            else
                name.getLanguage().add(new Language(produtoERP.getDescricao()));
            if (produtoERP.getMetaDescricao() != null) 
                metaDesc.getLanguage().add(new Language(produtoERP.getMetaDescricao()));
            if (produtoERP.getPalavrasChave() != null) 
                metaKey.getLanguage().add(new Language(produtoERP.getPalavrasChave()));
            if (produtoERP.getDescricaoCompleta() != null) 
                description.getLanguage().add(new Language(produtoERP.getDescricaoCompleta()));
            else
                description.getLanguage().add(new Language(produtoERP.getDescricao()));
            descriptionShort.getLanguage().add(new Language(produtoERP.getDescricaoBreve()));
            p.setPrice(new Price(String.valueOf(produtoERP.getPrecoCheio())));
            p.setPrice1(new Price1(String.valueOf(produtoERP.getPreco())));
            p.setPrice2(new Price2(String.valueOf(produtoERP.getPreco2())));
            p.setPrice3(new Price3(String.valueOf(produtoERP.getPreco3())));
            p.setPrice4(new Price4(String.valueOf(produtoERP.getPreco4())));
            //price.setContent(String.valueOf(produtoERP.getPrecoCheio()));           
            // p.setPrice(new Price().setContent(String.valueOf(produtoERP.getPrecoCheio())));
            if(produtoERP.getNomeProd()!=null)
               linkRewrite.getLanguage().add(new Language(produtoERP.getNomeProd()));
            else
               linkRewrite.getLanguage().add(new Language(produtoERP.getDescricao()));            
            associations.setCategories(createCategoriesByProd(produtoERP.getCodProd()));
            // associations.setCategories(createCategories(produtoERP.getCodProd()));
            associations.setProductOptionValues(this.createProductOptionValues(produtoERP));
            //associations.setCombinations(createCombinations(produtoERP));
            p.setAssociations(associations);
            p.setEan13(produtoERP.getCodBarras());
            p.setDepth(String.valueOf(produtoERP.getProfundidade()));
            p.setWeight(String.valueOf(produtoERP.getPeso()));
            p.setWidth(String.valueOf(produtoERP.getLargura()));
            p.setHeight(String.valueOf(produtoERP.getAltura()));
            if(produtoERP.getMostraNaLoja()!=null)
               if(produtoERP.getMostraNaLoja().equals("S"))p.setActive(1);
            if(produtoERP.getEmOferta()!=null)
               if(produtoERP.getEmOferta().equals("S"))p.setOnSale(1);
            p.setMetaDescription(metaDesc);
            p.setMetaKeyWord(metaKey);
            p.setDescription(description);
            p.setDescriptionShort(descriptionShort);
            p.setCondition(getCondition(produtoERP.getCondicao()));
            p.setIdErp(Integer.parseInt(produtoERP.getCodProd()));
            p.setName(name);
            p.setLinkRewrite(linkRewrite);
            p.setSku(produtoERP.getRefFabricante());
            p.setReference(produtoERP.getCodProd());
            
            // Fabricante
            if (produtoERP.getCodFabricante() != null) {
                fabrERPDao = new CadFabrERPDAO();
                cadFabric = fabrERPDao.abrir(produtoERP.getCodFabricante());
                if (cadFabric != null)
                    p.setIdManufacturer(cadFabric.getIdFabricanteEcom());
            }
                        
            return p;
        } catch (Exception e) {
            return null;
        }
    }
    
    private Product updateProduct(ProdutoERPBean produtoERP) {

        Product p = new Product();
        Name name = new Name();
        AssociationsNode associations = new AssociationsNode();
        Description description = new Description();
        DescriptionShort descriptionShort = new DescriptionShort();
        MetaDescription metaDesc = new MetaDescription();
        MetaKeyWord metaKey = new MetaKeyWord();
        Price price = new Price();
        LinkRewrite linkRewrite = new LinkRewrite();
        try {
            
            Product ProductWS = new ProductPrestashopDAO().getById(Product.URLPRODUCTS, produtoERP.getIdProdutoEcom());
            
            if (produtoERP.getIdProdutoEcom() != 0) {
                Id id = new Id();
                id.setContent(String.valueOf(produtoERP.getIdProdutoEcom()));
                p.setId(id);
                associations.setCombinations(this.createCombinations(produtoERP));
            }
            if (produtoERP.getNomeProd() != null) 
                name.getLanguage().add(new Language(produtoERP.getNomeProd()));
            else
                name.getLanguage().add(new Language(produtoERP.getDescricao()));
            if (produtoERP.getMetaDescricao() != null) 
                metaDesc.getLanguage().add(new Language(produtoERP.getMetaDescricao()));
            if (produtoERP.getPalavrasChave() != null) 
                metaKey.getLanguage().add(new Language(produtoERP.getPalavrasChave()));
//            if (produtoERP.getDescricaoCompleta() != null) 
//                description.getLanguage().add(new Language(produtoERP.getDescricaoCompleta()));
//            else
//                description.getLanguage().add(new Language(produtoERP.getDescricao()));
//            descriptionShort.getLanguage().add(new Language(produtoERP.getDescricaoBreve()));

            if (ProductWS.getDescription() != null) 
                description.getLanguage().add(new Language(ProductWS.getDescription().getTextDescription()));
            else
                description.getLanguage().add(new Language(produtoERP.getDescricaoCompleta()));
            if(ProductWS.getDescriptionShort() != null)
                descriptionShort.getLanguage().add(new Language(ProductWS.getDescriptionShort().getTextDescriptionShort()));
            else
                descriptionShort.getLanguage().add(new Language(produtoERP.getDescricaoBreve()));

            p.setPrice(new Price(String.valueOf(produtoERP.getPrecoCheio())));
            p.setPrice1(new Price1(String.valueOf(produtoERP.getPreco())));
            p.setPrice2(new Price2(String.valueOf(produtoERP.getPreco2())));
            p.setPrice3(new Price3(String.valueOf(produtoERP.getPreco3())));
            p.setPrice4(new Price4(String.valueOf(produtoERP.getPreco4())));
            //price.setContent(String.valueOf(produtoERP.getPrecoCheio()));           
            // p.setPrice(new Price().setContent(String.valueOf(produtoERP.getPrecoCheio())));
            if(produtoERP.getNomeProd()!=null)
               linkRewrite.getLanguage().add(new Language(produtoERP.getNomeProd()));
            else
               linkRewrite.getLanguage().add(new Language(produtoERP.getDescricao()));            
            associations.setCategories(createCategoriesByProd(produtoERP.getCodProd()));
            // associations.setCategories(createCategories(produtoERP.getCodProd()));
            associations.setProductOptionValues(this.createProductOptionValues(produtoERP));
            //associations.setCombinations(createCombinations(produtoERP));
            p.setAssociations(associations);
            p.setEan13(produtoERP.getCodBarras());
            p.setDepth(String.valueOf(produtoERP.getProfundidade()));
            p.setWeight(String.valueOf(produtoERP.getPeso()));
            p.setWidth(String.valueOf(produtoERP.getLargura()));
            p.setHeight(String.valueOf(produtoERP.getAltura()));
            if(produtoERP.getMostraNaLoja()!=null)
               if(produtoERP.getMostraNaLoja().equals("S"))p.setActive(1);
            if(produtoERP.getEmOferta()!=null)
               if(produtoERP.getEmOferta().equals("S"))p.setOnSale(1);
            p.setMetaDescription(metaDesc);
            p.setMetaKeyWord(metaKey);
            p.setDescription(description);
            p.setDescriptionShort(descriptionShort);
            p.setCondition(getCondition(produtoERP.getCondicao()));
            p.setIdErp(Integer.parseInt(produtoERP.getCodProd()));
            p.setName(name);
            p.setLinkRewrite(linkRewrite);
            p.setSku(produtoERP.getRefFabricante());
            p.setReference(produtoERP.getCodProd());
            
            // Fabricante
            if (produtoERP.getCodFabricante() != null) {
                fabrERPDao = new CadFabrERPDAO();
                cadFabric = fabrERPDao.abrir(produtoERP.getCodFabricante());
                if (cadFabric != null)
                    p.setIdManufacturer(cadFabric.getIdFabricanteEcom());
            }
                        
            return p;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Cria associação de categorias
     * @param codCategory
     * @return
     */
    private CategoriesNode createCategories(String codCategory) {
        try {
            CategoriesNode categories = new CategoriesNode();
            List<CategoriaEcomErpBean> categoriasEcomErp = new CategoriaEcomDAO().retornaCategoriasPai(codCategory);
            String url = ParaEcomService.getUrlBean().getUrlWSDL() + "/";
            List<CategoryNode> listCategory = new ArrayList<>();            
            //categoria home
            CategoryNode homeCategory = new CategoryNode(2, url + 2);
            listCategory.add(homeCategory);
            for (CategoriaEcomErpBean catEcom : categoriasEcomErp) {
                CategoryNode categorieNode = new CategoryNode(catEcom.getIdCategoriaEcom(), url + catEcom.getIdCategoriaEcom());
                listCategory.add(categorieNode);
            }
            categories.setCategory(listCategory);
            return categories;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Cria associação de categorias pelo produto
     * @param codCategory 
     * @return CategoriesNode
     */
    private CategoriesNode createCategoriesByProd(String codProd) {
        ProdutoCategoriaERPDAO prodCatDao = new ProdutoCategoriaERPDAO();
        CategoriaEcomDAO catEcomDao = new CategoriaEcomDAO();
        try {
            CategoriesNode categories = new CategoriesNode();
            List<ProdutoCategoriaERPBean> listaProdutoCategoria = prodCatDao.listaCategoriasPorProduto(codProd);
            String url = ParaEcomService.getUrlBean().getUrlWSDL() + "/";
            List<CategoryNode> listCategory = new ArrayList<>();            
            //categoria home
            CategoryNode homeCategory = new CategoryNode(2, url + 2);
            listCategory.add(homeCategory);
            for (ProdutoCategoriaERPBean prodCat : listaProdutoCategoria) {
                CategoriaEcomErpBean catEcom = catEcomDao.abrir(prodCat.getCodCategoria());
                CategoryNode categorieNode = new CategoryNode(catEcom.getIdCategoriaEcom(), url + catEcom.getIdCategoriaEcom());
                listCategory.add(categorieNode);
            }
            categories.setCategory(listCategory);
            return categories;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Cria valores de grade
     *
     * @param prod
     * @return ProductOptionValuesNode
     *
     */
    //inserir na controladora de grade
    private ProductOptionValuesNode createProductOptionValues(ProdutoERPBean prod) {
        ProductOptionValuesNode prodOptionValues = new ProductOptionValuesNode();
        AtributoGradeEcomDAO atributoDao = new AtributoGradeEcomDAO();
        GradeERPDAO gradeDao = new GradeERPDAO();
        SubGradeERPDAO subGradeDAO = new SubGradeERPDAO();
        ProdGradeERPDAO prodGradeDao = new ProdGradeERPDAO();
        try {
            List<ProductOptionsValuesNode> prodOptionsValues = new ArrayList<>();
            //grade simples ou especial
            if (prod.getGrade() == 1 || prod.getGrade() == 2) {
                List<ProdGradeERPBean> listaProdGrade = prodGradeDao.searchGradeComumByCodProd(prod.getCodProd());
                if (!listaProdGrade.isEmpty()) {
                    ProductOptionsValuesNode prodOptions = null;
                    for (ProdGradeERPBean prodGrade : listaProdGrade) {
                        prodOptions = new ProductOptionsValuesNode();
                        GradeERPBean grade = gradeDao.abrir(prodGrade.getCodGrade());
                        grade.setIdAtributo(atributoDao.abrir(prod.getCodAtributo1()).getIdAtributoEcom());                        
                        prodOptions.setId(this.getIdGradeEcom(grade));
                        prodOptionsValues.add(prodOptions);
                    }
                }
            }
            //grade composta
            if (prod.getGrade() == 3) {
                List<ProdGradeERPBean> listaProdGrade = prodGradeDao.searchGradeCompostaByCodProd(prod.getCodProd());
                if (!listaProdGrade.isEmpty()) {
                    //ProductOptionsValuesNode prodOptions = null;
                    for (ProdGradeERPBean prodGrade : listaProdGrade) {
                        //valor para grade
                        ProductOptionsValuesNode gradeOptions = new ProductOptionsValuesNode();
                        //valor para subgrade
                        ProductOptionsValuesNode subGradeOptions = new ProductOptionsValuesNode();
                        //bean de grade
                        SubGradeERPBean grade = subGradeDAO.abrir(prodGrade.getCodGrade().substring(0, 2));
                        grade.setIdAtributo(atributoDao.abrir(prod.getCodAtributo1()).getIdAtributoEcom());
                        //bean de subgrade
                        SubGradeERPBean subGrade = subGradeDAO.abrir(prodGrade.getCodGrade().substring(2, 4));
                        subGrade.setIdAtributo(atributoDao.abrir(prod.getCodAtributo2()).getIdAtributoEcom());
                        gradeOptions.setId(this.getIdSubGradeEcom(grade));
                        subGradeOptions.setId(this.getIdSubGradeEcom(subGrade));
                        prodOptionsValues.add(gradeOptions);
                        prodOptionsValues.add(subGradeOptions);
                    }
                }
            }
            prodOptionValues.setProdOptionsValues(prodOptionsValues);
            Logger.getLogger(ProductController.class).info("Valor de atributo do produto criado ou atualizado com sucesso.");
            return prodOptionValues;
        } catch (Exception e) {
            Logger.getLogger(ProductController.class).info("Erro ao criar ou atualizar valor do atributo do produto: " + e);
            return null;
        }
    }

    /**
     * Retorna o id da grade na loja virtual, cadastra caso não exista
     *
     * @param grade
     * @return
     */
    private int getIdGradeEcom(GradeERPBean grade) {
        GradeERPDAO gradeDao = new GradeERPDAO();
        ProductOptionValueController prodValueController = new ProductOptionValueController();
        try {
            //se grade ainda nao foi integrada
            if (grade.getIdGradeEcom() == 0) {
                prodValueController = new ProductOptionValueController();
                int idProdOptionValue = prodValueController.createProductOptionValuePrestashop(grade);
                if (idProdOptionValue != 0) {
                    grade.setIdGradeEcom(idProdOptionValue);
                    gradeDao.alterar(grade);
                }
            }
            Logger.getLogger(ProductController.class).info("Id da grade na loja virtual retornado com sucesso.");
            return grade.getIdGradeEcom();
        } catch (Exception e) {
            Logger.getLogger(ProductController.class).error("Erro ao retornar id da grade na loja virtual");
            return 0;
        }
    }

    /**
     * Retorna o id da grade na loja virtual, cadastra caso não exista
     *
     * @param grade
     * @return
     */
    private int getIdSubGradeEcom(SubGradeERPBean grade) {
        SubGradeERPDAO gradeDao = new SubGradeERPDAO();
        ProductOptionValueController prodValueController = new ProductOptionValueController();
        try {
            //se grade ainda nao foi integrada
            if (grade.getIdSubgradeEcom() == 0) {
                prodValueController = new ProductOptionValueController();
                int idProdOptionValue = prodValueController.createProductOptionValuePrestashop(grade);
                if (idProdOptionValue != 0) {
                    grade.setIdSubgradeEcom(idProdOptionValue);
                    gradeDao.alterar(grade);
                }
            }
            Logger.getLogger(ProductController.class).info("Id da subgrade na loja virtual, retornado com sucesso.");
            return grade.getIdSubgradeEcom();
        } catch (Exception e) {
            Logger.getLogger(ProductController.class).info("Erro ao retornar id da subgrade na loja virtual: " + e);
            return 0;
        }
    }

    /**
     * Retorna a condição do produto, novo, usado e etc
     *
     * @param conditionErp
     * @return
     */
    private String getCondition(int conditionErp) {
        String condition = "";
        switch (conditionErp) {
            case 0:
                condition = "new";
                break;
            case 1:
                condition = "used";
                break;
            case 2:
                condition = "refurbished";
                break;
        }
        return condition;       
    }

    /**
     * Cria nó de combinação de grades
     *
     * @param produtoERP
     * @return
     */
    public CombinationsNode createCombinations(ProdutoERPBean produtoERP) {
        CombinationsNode combinations = new CombinationsNode();
        CombinationController combinationsController = new CombinationController();
        ProdGradeERPDAO prodGradeDAO = new ProdGradeERPDAO();
        List<ProdGradeERPBean> listProdGrade = null;
        try {
            List<CombinationsChild> listCombinationsChilds = new ArrayList<>();
            if (produtoERP.getGrade() != 0) {
                if (produtoERP.getGrade() == 1 || produtoERP.getGrade() == 2) {
                    listProdGrade = prodGradeDAO.searchGradeComumByCodProd(produtoERP.getCodProd());
                }
                if (produtoERP.getGrade() == 3) {
                    listProdGrade = prodGradeDAO.searchGradeCompostaByCodProd(produtoERP.getCodProd());
                }
                for (ProdGradeERPBean prodGradeERP : listProdGrade) {
                    prodGradeERP.setTipoGrade(produtoERP.getGrade());
                    CombinationsChild child = new CombinationsChild();
                    if (prodGradeERP.getIdProdGradeEcom() == 0) {
                        int idCombination = combinationsController.createCombination(prodGradeERP);
                        prodGradeERP.setIdProdGradeEcom(idCombination);
                        prodGradeDAO.alterar(prodGradeERP);
                    }
                    child.setId(prodGradeERP.getIdProdGradeEcom());
                    listCombinationsChilds.add(child);
                }
            }
            combinations.setCombinations(listCombinationsChilds);
            Logger.getLogger(ProductController.class).info("Combinação de grade criada com sucesso.");
            return combinations;
        } catch (Exception e) {
            Logger.getLogger(ProductController.class).info("Erro ao criar combinação de grade: " + e);
            return null;
        }
    }
    
    public WSItens listItens(){
        ProductPrestashopDAO prodPsDAO = new ProductPrestashopDAO();
        return prodPsDAO.getProductItem("products/");
    }
    
    
}
