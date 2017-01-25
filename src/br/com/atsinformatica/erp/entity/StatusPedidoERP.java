/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import br.com.atsinformatica.midler.annotation.GenericController;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;

/**
 * Bean de status do pedido
 * @author AlexsanderPimenta
 */
@GenericController(classPath = "br.com.atsinformatica.erp.controller.StatusPedidoController")
public class StatusPedidoERP {
    
    private int idPedidoEcom;
    private String idPedido;
    private StatusPedido status;

    /**
     * @return the idPedidoEcom
     */
    public int getIdPedidoEcom() {
        return idPedidoEcom;
    }

    /**
     * @param idPedidoEcom the idPedidoEcom to set
     */
    public void setIdPedidoEcom(int idPedidoEcom) {
        this.idPedidoEcom = idPedidoEcom;
    }

    /**
     * @return the idPedido
     */
    public String getIdPedido() {
        return idPedido;
    }

    /**
     * @param idPedido the idPedido to set
     */
    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }
}
