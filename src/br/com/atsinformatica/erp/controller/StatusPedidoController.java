/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.dao.ParaStatusPedidoDAO;
import br.com.atsinformatica.erp.dao.PedidoCERPDAO;
import br.com.atsinformatica.erp.entity.StatusPedidoERP;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.exception.GenericMiddleException;
import br.com.atsinformatica.prestashop.clientDAO.OrderHistoryPestashopDAO;
import br.com.atsinformatica.prestashop.controller.OrderController;
import br.com.atsinformatica.prestashop.model.root.OrderHistory;
import br.com.atsinformatica.utils.SingletonUtil;

/**
 *
 * @author AlexsanderPimenta
 */
public class StatusPedidoController extends SincERPController<StatusPedidoERP> {
        
    @Override
    public int update(StatusPedidoERP obj) throws Exception {        
        int idPedidoEcom = obj.getIdPedidoEcom();
        StatusPedido status = obj.getStatus();
        
        Integer id = SingletonUtil.get(ParaStatusPedidoDAO.class).getIdPadrao(status);
        if (id == null) {
            throw new GenericMiddleException("Não foi encontrado um status correspondente "
                    + " para status \"" + status.getDescricao() + "\", verifique nas configurações os valores padrões.");
        }
        
        //int codOper = orderController.updateStatusOrder(idPedidoEcom, codStatus);
        OrderHistoryPestashopDAO dao = new OrderHistoryPestashopDAO();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setIdEmployee("999999");
        orderHistory.setIdOrder(String.valueOf(idPedidoEcom));
        orderHistory.setIdOrderState(String.valueOf(id));
        dao.post(OrderHistory.URLORDERHISTORY, orderHistory);
        
        // Envia codigo de rastreio
        if (status == StatusPedido.ENVIADO) {
            String ratreio = SingletonUtil.get(PedidoCERPDAO.class).getCodigoRatreio(obj.getIdPedidoEcom());
            if (ratreio != null) {
                SingletonUtil.get(OrderController.class).updateCodRastreio(obj.getIdPedidoEcom(), ratreio);
            }
        }
        //se status diferente de status de controle entrega
        if (status != StatusPedido.ENVIADO && status != StatusPedido.ENTREGUE && status != StatusPedido.DEVOLVIDO) {
            SingletonUtil.get(PedidoCERPDAO.class).atualizaStatusPedidoEcom(idPedidoEcom, status);
        }
        return 200;
    }
    
    @Override
    public void post(StatusPedidoERP obj) throws Exception {        
        int idPedidoEcom = obj.getIdPedidoEcom();
        StatusPedido status = obj.getStatus();
               
        Integer id = SingletonUtil.get(ParaStatusPedidoDAO.class).getIdPadrao(status);
        if (id == null) {
            throw new GenericMiddleException("Não foi encontrado um status correspondente "
                    + " para status \"" + status.getDescricao() + "\", verifique nas configurações os valores padrões.");
        }
        
        OrderHistoryPestashopDAO dao = new OrderHistoryPestashopDAO();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setIdEmployee("999999");
        orderHistory.setIdOrder(String.valueOf(idPedidoEcom));
        orderHistory.setIdOrderState(id.toString());
        dao.post(OrderHistory.URLORDERHISTORY, orderHistory);
        
        SingletonUtil.get(PedidoCERPDAO.class).atualizaStatusPedidoEcom(idPedidoEcom, status);                
    }
    
    
    public void delete(StatusPedidoERP obj) throws Exception {        
        int idPedidoEcom = obj.getIdPedidoEcom();
        SingletonUtil.get(OrderController.class).updateStatusOrder(idPedidoEcom, obj.getStatus());                
    }
    
}
