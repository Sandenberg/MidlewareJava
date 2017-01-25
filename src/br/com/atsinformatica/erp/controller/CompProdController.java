/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.entity.CompProdBean;

/**
 *
 * @author AlexsanderPimenta
 */
public class CompProdController extends SincERPController<CompProdBean> {
    private ProdGradeERPController prodErpController = new ProdGradeERPController();
    
    @Override
    public int update(CompProdBean obj) throws Exception {        
        return prodErpController.atualizaEstoque(obj.getProduto());
        
    }
    
    
}
