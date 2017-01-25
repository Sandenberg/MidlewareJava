/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.entity.GradeERPBean;
import br.com.atsinformatica.prestashop.controller.ProductOptionValueController;

/**
 *
 * @author AlexsanderPimenta
 */
public class GradeERPController extends SincERPController<GradeERPBean> {
    private ProductOptionValueController prodOptionValue = new ProductOptionValueController();
    
    @Override
    public int update(GradeERPBean obj){
        return prodOptionValue.put(obj);
    }
    
    @Override
    public int delete(String id){
        return prodOptionValue.delete(id);
    }
    
}
