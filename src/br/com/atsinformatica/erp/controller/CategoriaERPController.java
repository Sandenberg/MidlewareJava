/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.dao.CategoriaEcomDAO;
import br.com.atsinformatica.erp.entity.CategoriaEcomErpBean;
import br.com.atsinformatica.midler.ui.PanelHistorico;
import br.com.atsinformatica.prestashop.clientDAO.CategoryPrestashopDAO;
import br.com.atsinformatica.prestashop.controller.CategoriaController;
import br.com.atsinformatica.prestashop.model.node.AssociationsNode;
import br.com.atsinformatica.prestashop.model.node.CategoryNode;
import br.com.atsinformatica.prestashop.model.root.Category;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class CategoriaERPController extends SincERPController<CategoriaEcomErpBean> {

    private CategoriaController catController;
    

    public CategoriaERPController() {
        catController = new CategoriaController();
    }

    @Override
    public void post(CategoriaEcomErpBean obj) {
        if (obj != null && obj.getIdCategoriaEcom() > 0 && obj.getIdCategoriaEcom() <= 2) {
            throw new RuntimeException("Não é permitido realizar nenhuma "
                    + "operação com categorias ROOT");
        }
        
        try {
            //faz o post das categorias pendentes de sincronização
            catController = new CategoriaController();
            int codCatEcom = catController.createCategoryPrestashop(obj);
            if (codCatEcom != 0) {
                obj.setIdCategoriaEcom(codCatEcom);
                CategoriaEcomDAO dao = new CategoriaEcomDAO();
                //salvando código da categoria cadastrada 
                dao.alteraIdEcom(obj);
            }
            Logger.getLogger(CategoriaERPController.class).info("Categoria sincronizada na loja virtual com sucesso.");
        } catch (Exception e) {
            Logger.getLogger(CategoriaERPController.class).error("Erro ao sincronizar categoria na loja virtual: " + e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public int update(CategoriaEcomErpBean obj) {
        if (obj != null && obj.getIdCategoriaEcom() <= 2) {
            throw new RuntimeException("Não é permitido realizar nenhuma "
                    + "operação com categorias ROOT");
        }
        
        return catController.update(obj);
    }

    @Override
    public int delete(String id) {
        if (id != null && !id.isEmpty() && Integer.parseInt(id) <= 2) {
            throw new RuntimeException("Não é permitido realizar nenhuma "
                    + "operação com categorias ROOT");
        }
        
        return catController.deleteCategoryPrestashop(id);
        
    }
    
     /**
     * Cadastra categorias da loja virtual no ERP
     * @param categorias 
     */
    public void cadastraCategorias(List<Category> categorias) throws SQLException {
        CategoriaEcomDAO catEcomDAO = new CategoriaEcomDAO();
        String principal = "N";
        String codCatSuperior;
        for (Category categoria : categorias) {
            if (!catEcomDAO.categoriaExiste(Integer.valueOf(categoria.getId()))) {
                CategoriaEcomErpBean catEcom = new CategoriaEcomErpBean();
                catEcom.setDescricao(categoria.getName().getTextName());
                catEcom.setDescricaoCompleta(categoria.getName().getTextName());
                catEcom.setDescricaoDetalhada(categoria.getName().getTextName());
                catEcom.setMostranaLoja("S");
                if (categoria.getIdParent() == 0) principal = "S";                
                catEcom.setPrincipal(principal);
                catEcom.setIdCategoriaEcom(Integer.valueOf(categoria.getId()));
                codCatSuperior = catEcomDAO.retornaCodCategoria(Integer.valueOf(categoria.getIdParent()));                 
                try {                    
                    PanelHistorico.textArea.append("Cadastrando categoria " + categoria.getName().getTextName() + "\n");
                    catEcom.setCodCategoriaSuperior(codCatSuperior);
                    catEcomDAO.gravar(catEcom);
                    PanelHistorico.textArea.append("Categoria " + categoria.getName().getTextName() + " cadastrada com sucesso! \n");                    
                } catch (SQLException ex) {
                    PanelHistorico.textArea.append("Erro ao cadastrar categoria "+categoria.getName().getTextName() + " no ERP");
                }
                List<Category> sub = this.retornaCategorias(categoria.getAssociations());
                if (!sub.isEmpty()) {
                    this.cadastraCategorias(sub);
                }
            }
        }
    }
    
    /**
     * Retorna categorias através de um nó de categorias
     *
     * @param node nó de categorias
     * @return lista de categorias
     */
    private List<Category> retornaCategorias(AssociationsNode node) {
        List<Category> categorias = new ArrayList<>();
        CategoryPrestashopDAO dao = new CategoryPrestashopDAO();
        for (CategoryNode catNode : node.getCategories().getCategory()) {
            Category cat = dao.getId("categories/", catNode.getId());
            categorias.add(cat);
        }
        return categorias;
    }
    
    /**
     * Atualiza código de categoria superior caso não tenha sido preenchido
     */
    private void atualizaCodCategoriaSuperior(){
       CategoriaEcomDAO dao = new CategoriaEcomDAO();
       CategoryPrestashopDAO catPSDAO = new CategoryPrestashopDAO();
       try{
           //lista de categorias pendentes
           List<CategoriaEcomErpBean> catPendentes = dao.retornaCatSemSuperior();
           //se lista de categorias pendentes não esta nula ou vazia
           if(catPendentes !=null && !catPendentes.isEmpty()){
              for(CategoriaEcomErpBean cat : catPendentes){
                  //recebe categoria cadastrada na loja virtual
                  Category catPs = catPSDAO.getId("categories/", cat.getIdCategoriaEcom());                  
                  if(catPs!=null){
                      //se categoria pendente possui categoria superior
                      if(catPs.getIdParent()!=0){
                         //altera código da categoria superior 
                         cat.setCodCategoriaSuperior(dao.retornaCodCategoria(catPs.getIdParent()));
                         //altera dados da categoria superior
                         dao.alterar(cat);
                      }
                      
                  }               
              }    
           }           
       }catch(Exception e){
            
       }
    }
    
    
}
