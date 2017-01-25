/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.dao.ParaStatusPedidoDAO;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.exception.GenericMiddleException;
import br.com.atsinformatica.prestashop.clientDAO.OrderHistoryPestashopDAO;
import br.com.atsinformatica.prestashop.model.root.OrderHistory;
import br.com.atsinformatica.utils.SingletonUtil;

/**
 *
 * @author kennedimalheiros
 */
public class OrderHistoryController {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OrderHistoryController.class);

    public void insertOrderHistory(int idOrder, StatusPedido status) {
        try {
            Integer id = SingletonUtil.get(ParaStatusPedidoDAO.class).getIdPadrao(status);
            if (id == null) {
                throw new GenericMiddleException("Não foi encontrado um status correspondente "
                        + " para status \"" + status.getDescricao() + "\", verifique nas configurações os valores padrões.");
            }
            
            OrderHistoryPestashopDAO dao = new OrderHistoryPestashopDAO();
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setIdEmployee("999999");
            orderHistory.setIdOrder(String.valueOf(idOrder));
            orderHistory.setIdOrderState(id.toString());
            dao.post(OrderHistory.URLORDERHISTORY, orderHistory);
        } catch (Exception e) {
            logger.error("Erro ao inserir Historio do pedido para Status Cod:(" + status + ") do PedidosEcon Cod:(" + idOrder + ") na loja Prestashop: " + e);
            throw new RuntimeException(e);
        }
    }
}
