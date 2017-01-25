/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.entity.SugestVendasERPBean;
import br.com.atsinformatica.prestashop.controller.AccessoriesController;

/**
 *
 * @author AlexsanderPimenta
 */
public class SugestVendasController extends SincERPController<SugestVendasERPBean> {
    
    private AccessoriesController accessoriesController;
    
    public SugestVendasController(){
        accessoriesController = new AccessoriesController();
    }
    
    @Override
    public int delete(String id) throws Exception {
        return accessoriesController.delete(id);
    }
    
    @Override
    public void post(SugestVendasERPBean obj) throws Exception {
        accessoriesController.insert(obj);
    }
    
    
    
    
    
    
}
