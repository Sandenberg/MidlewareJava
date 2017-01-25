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
@GenericController(classPath = "br.com.atsinformatica.erp.controller.CompProdController")
public class CompProdBean {
   private ProdutoERPBean produto;

    /**
     * @return the produto
     */
    public ProdutoERPBean getProduto() {
        return produto;
    }

    /**
     * @param produto the produto to set
     */
    public void setProduto(ProdutoERPBean produto) {
        this.produto = produto;
    }
        
}
