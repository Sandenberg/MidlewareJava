/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

/**
 *
 * @author AlexsanderPimenta
 */
public class ParacadBean {
    
    private String habilitaEcommerce;
    private int precoEcommerce;
    private String codTVenda;

    /**
     * @return the habilitaEcommerce
     */
    public String getHabilitaEcommerce() {
        return habilitaEcommerce;
    }

    /**
     * @param habilitaEcommerce the habilitaEcommerce to set
     */
    public void setHabilitaEcommerce(String habilitaEcommerce) {
        this.habilitaEcommerce = habilitaEcommerce;
    }

    /**
     * @return the precoEcommerce
     */
    public int getPrecoEcommerce() {
        return precoEcommerce;
    }

    /**
     * @param precoEcommerce the precoEcommerce to set
     */
    public void setPrecoEcommerce(int precoEcommerce) {
        this.precoEcommerce = precoEcommerce;
    }

    /**
     * @return the codTVenda
     */
    public String getCodTVenda() {
        return codTVenda;
    }

    /**
     * @param codTVenda the codTVenda to set
     */
    public void setCodTVenda(String codTVenda) {
        this.codTVenda = codTVenda;
    }
    
}
