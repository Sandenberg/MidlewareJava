/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.entity.GradeERPBean;
import br.com.atsinformatica.erp.entity.SubGradeERPBean;
import br.com.atsinformatica.prestashop.clientDAO.ProductOptionValueDAO;
import br.com.atsinformatica.prestashop.model.node.Language;
import br.com.atsinformatica.prestashop.model.node.Name;
import br.com.atsinformatica.prestashop.model.root.ProductOptionValue;

/**
 *
 * @author AlexsanderPimenta
 */
public class ProductOptionValueController {
    
    private ProductOptionValueDAO prodOptionDAO = new ProductOptionValueDAO();

    public int createProductOptionValuePrestashop(Object obj) {
        return prodOptionDAO.postProductOptionValue(ProductOptionValue.URLPRODUCTOPTIONVALUE, createProductOptionValue(obj));
    }
    
    public int put(Object obj){
        return prodOptionDAO.put(ProductOptionValue.URLPRODUCTOPTIONVALUE, createProductOptionValue(obj));
    }
    
    public int delete(String id){
        return prodOptionDAO.delete(ProductOptionValue.URLPRODUCTOPTIONVALUE, id);
    }
    private ProductOptionValue createProductOptionValue(Object obj) {
        ProductOptionValue prodOptionValue = new ProductOptionValue();
        Name name = null;
        if (obj.getClass().equals(GradeERPBean.class)) {
            GradeERPBean grade = (GradeERPBean) obj;
            name = new Name();
            prodOptionValue.setIdAttributeGroup(grade.getIdAtributo());
            name.getLanguage().add(new Language(grade.getDescricaoGrade()));
            prodOptionValue.setName(name);
            //se ja esta cadastrado na loja
            if(grade.getIdGradeEcom()!=0){
                prodOptionValue.setId(String.valueOf(grade.getIdGradeEcom()));
                //retorna id do atributo atrelado a grade
                prodOptionValue.setIdAttributeGroup(prodOptionDAO.getId(ProductOptionValue.URLPRODUCTOPTIONVALUE, grade.getIdGradeEcom()).getIdAttributeGroup());
            }
            
        }
        if(obj.getClass().equals(SubGradeERPBean.class)){
            SubGradeERPBean subGrade = (SubGradeERPBean) obj;
            name = new Name();
            prodOptionValue.setIdAttributeGroup(subGrade.getIdAtributo());
            name.getLanguage().add(new Language(subGrade.getDescSubGrade()));
            prodOptionValue.setName(name);
            if(subGrade.getIdSubgradeEcom()!=0){
                prodOptionValue.setId(String.valueOf(subGrade.getIdSubgradeEcom()));
                prodOptionValue.setIdAttributeGroup(prodOptionDAO.getId(ProductOptionValue.URLPRODUCTOPTIONVALUE, subGrade.getIdSubgradeEcom()).getIdAttributeGroup());
            }
        }
        return prodOptionValue;
    }
}
