/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import br.com.atsinformatica.midler.annotation.GenericController;

/**
 *
 * @author AlexsanderPimenta
 */
@GenericController(classPath = "br.com.atsinformatica.erp.controller.SugestVendasController")
public class SugestVendasERPBean {
    
    private int idProdEcom1;
    private int idProdEcom2;

    /**
     * @return the idProdEcom1
     */
    public int getIdProdEcom1() {
        return idProdEcom1;
    }

    /**
     * @param idProdEcom1 the idProdEcom1 to set
     */
    public void setIdProdEcom1(int idProdEcom1) {
        this.idProdEcom1 = idProdEcom1;
    }

    /**
     * @return the idProdEcom2
     */
    public int getIdProdEcom2() {
        return idProdEcom2;
    }

    /**
     * @param idProdEcom2 the idProdEcom2 to set
     */
    public void setIdProdEcom2(int idProdEcom2) {
        this.idProdEcom2 = idProdEcom2;
    }
    
    
}
