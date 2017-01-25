/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.dao.PedidoCERPDAO;
import br.com.atsinformatica.erp.dao.ProdutoDAO;
import br.com.atsinformatica.erp.entity.PedidoCBean;
import br.com.atsinformatica.erp.entity.PedidoDevERPBean;
import br.com.atsinformatica.erp.entity.PedidoIERPBean;
import br.com.atsinformatica.erp.entity.ProdutoERPBean;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.prestashop.controller.OrderController;
import br.com.atsinformatica.prestashop.controller.OrderSlipController;
import br.com.atsinformatica.prestashop.model.list.OrderSlipDetails;
import br.com.atsinformatica.prestashop.model.node.AssociationsOrderSlipDetail;
import br.com.atsinformatica.prestashop.model.node.OrderRowNode;
import br.com.atsinformatica.prestashop.model.node.OrderSlipDetail;
import br.com.atsinformatica.prestashop.model.root.Order;
import br.com.atsinformatica.prestashop.model.root.OrderSlip;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class PedidoDevController extends SincERPController<PedidoDevERPBean>{
    
    
    @Override
    public void post(PedidoDevERPBean p){
        PedidoCERPDAO pedidoCERPDAO = new PedidoCERPDAO();
        OrderController orderController = new OrderController();
        OrderSlipController orderSlipController = new OrderSlipController();
        try {
            PedidoCBean pedidoDev = pedidoCERPDAO.abrirPedido(p.getCodPedidoDevolucao());
            PedidoCBean pedidoNovo = pedidoCERPDAO.abrirPedido(p.getCodPedido());
            Order order = orderController.getOrderById(pedidoDev.getIdPedidoEcom());
            if(order!=null){
                OrderSlip orderSlip = new OrderSlip();
                orderSlip.setIdCustomer(Integer.parseInt(order.getId_customer()));
                orderSlip.setIdOrder(Integer.parseInt(order.getId()));
                orderSlip.setAmount(this.returnTotalAmount(pedidoNovo.getItensPedido()));
                orderSlip.setShippingCost(Double.valueOf(order.getTotalShippingTaxExcl()));
                orderSlip.setShippingCostAmount(Double.valueOf(order.getTotalShippingTaxIncl()));               
                AssociationsOrderSlipDetail associations = new AssociationsOrderSlipDetail();
                associations.setOrderSlipDetails(this.returnOrderSlipDetail(order, pedidoNovo));
                orderSlip.setAssociations(associations);
                //cria credito na loja
                orderSlipController.postOrderSlip(orderSlip);
                //se todos itens do pedido foram devolvidos
                if(pedidoDev.getItensPedido().size() == pedidoNovo.getItensPedido().size()){
                   //seta status do pedido para devolvido na loja
                   orderController.updateStatusOrder(Integer.parseInt(order.getId()), StatusPedido.DEVOLVIDO);
                   //seta status do pedido para devolvido no ERP
                   pedidoCERPDAO.atualizaStatusPedidoEcom(pedidoDev.getIdPedidoEcom(), StatusPedido.DEVOLVIDO);
                }                
            }
        } catch (SQLException ex) {
            Logger.getLogger(PedidoDevController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    /**
     * Retorna total de produtos
     * @param listaPedidoI
     * @return 
     */
    private Double returnTotalAmount(List<PedidoIERPBean> listaPedidoI){
        Double totalAmount = 0.00;
        if(!listaPedidoI.isEmpty()){
            for(PedidoIERPBean pedidoI : listaPedidoI){
                totalAmount += pedidoI.getPrecoUnit();
            }            
        }
        return totalAmount;
    }
    
    /**
     * 
     * @param o
     * @param pedidoC
     * @return 
     */
    private OrderSlipDetails returnOrderSlipDetail(Order o, PedidoCBean pedidoC){        
        ProdutoDAO prodDao = new ProdutoDAO();
        OrderSlipDetail orderSlipDetail = null;
        OrderSlipDetails orderSlipDetails = new OrderSlipDetails();
        List<OrderSlipDetail> listaOrderSlip = new ArrayList<>();
        for(OrderRowNode orderRow : o.getAssociations().getOrderRowsNode().getOrderRow()){
            orderSlipDetail = new OrderSlipDetail();
            orderSlipDetail.setIdOrderDetail(orderRow.getId());
            for(PedidoIERPBean pedidoI : pedidoC.getItensPedido()){
                try {
                    ProdutoERPBean produto = prodDao.abrir(pedidoI.getCodProdERP());
                    if(orderRow.getProductId() == produto.getIdProdutoEcom()){
                       orderSlipDetail.setAmountTaxExcl(produto.getPrecoCheio());
                       orderSlipDetail.setAmountTaxIncl(produto.getPrecoFinal());
                       orderSlipDetail.setProductQuantity(Integer.valueOf(String.valueOf(orderRow.getProductQuantity())));
                       listaOrderSlip.add(orderSlipDetail);
                    }
                    orderSlipDetails.setOrderSlipDetail(listaOrderSlip);
                    return orderSlipDetails;
                } catch (Exception ex) {
                    Logger.getLogger(PedidoDevController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
        }
        return orderSlipDetails;
        
    }
    
    
    
    
    
    
}
