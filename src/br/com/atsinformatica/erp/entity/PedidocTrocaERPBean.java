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
@GenericController(classPath = "br.com.atsinformatica.erp.controller.PedidoTrocaController")
public class PedidocTrocaERPBean {
    
    private String codPedidoVenda;  
    private String codPedidoTroca;
    private String codPedidoOriginal;

    /**
     * @return the codPedidoVenda
     */
    public String getCodPedidoVenda() {
        return codPedidoVenda;
    }

    /**
     * @param codPedidoVenda the codPedidoVenda to set
     */
    public void setCodPedidoVenda(String codPedidoVenda) {
        this.codPedidoVenda = codPedidoVenda;
    }

    /**
     * @return the codPedidoTroca
     */
    public String getCodPedidoTroca() {
        return codPedidoTroca;
    }

    /**
     * @param codPedidoTroca the codPedidoTroca to set
     */
    public void setCodPedidoTroca(String codPedidoTroca) {
        this.codPedidoTroca = codPedidoTroca;
    }

    /**
     * @return the codPedidoOriginal
     */
    public String getCodPedidoOriginal() {
        return codPedidoOriginal;
    }

    /**
     * @param codPedidoOriginal the codPedidoOriginal to set
     */
    public void setCodPedidoOriginal(String codPedidoOriginal) {
        this.codPedidoOriginal = codPedidoOriginal;
    }
    
}
