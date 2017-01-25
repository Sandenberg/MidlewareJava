/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

/**
 *
 * @author AlexsanderPimenta
 */
public class TipoVendaBean {
    
    private String codTVenda;
    private String descricao;

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

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return  codTVenda +" "+ descricao ;
    }
    
}
