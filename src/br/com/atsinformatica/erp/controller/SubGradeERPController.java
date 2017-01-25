/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.entity.SubGradeERPBean;
import br.com.atsinformatica.prestashop.controller.ProductOptionValueController;

/**
 *
 * @author AlexsanderPimenta
 */
public class SubGradeERPController extends SincERPController<SubGradeERPBean> {
    private ProductOptionValueController prodOptionController = new ProductOptionValueController();
    
    @Override
    public int update(SubGradeERPBean sub){
        return prodOptionController.put(sub);        
    }
    
    @Override
    public int delete(String id){
        return prodOptionController.delete(id);
    }
    
}
