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
@GenericController(classPath = "br.com.atsinformatica.erp.controller.PedidoDevController")
public class PedidoDevERPBean {
    
    private String codPedido;
    private String codPedidoDevolucao;

    /**
     * @return the codPedido
     */
    public String getCodPedido() {
        return codPedido;
    }

    /**
     * @param codPedido the codPedido to set
     */
    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    /**
     * @return the codPedidoDevolucao
     */
    public String getCodPedidoDevolucao() {
        return codPedidoDevolucao;
    }

    /**
     * @param codPedidoDevolucao the codPedidoDevolucao to set
     */
    public void setCodPedidoDevolucao(String codPedidoDevolucao) {
        this.codPedidoDevolucao = codPedidoDevolucao;
    }
    
}
