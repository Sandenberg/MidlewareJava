package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.dao.ParaStatusPedidoDAO;
import br.com.atsinformatica.erp.entity.ParaStatusPedidoBean;
import br.com.atsinformatica.erp.entity.PedidoCERPBean;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.exception.ErroSyncException;
import br.com.atsinformatica.midler.exception.GenericMiddleException;
import br.com.atsinformatica.prestashop.clientDAO.OrderPrestashopDAO;
import br.com.atsinformatica.prestashop.model.root.Order;
import br.com.atsinformatica.utils.SingletonUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class OrderController {
    private static final Logger logger = Logger.getLogger(OrderController.class);
    private OrderPrestashopDAO orderPrestashopDAO;

    public PedidoCERPBean syncOrderControllerPrestashop(int cod) {
        OrderPrestashopDAO dao = new OrderPrestashopDAO();

        Order order = dao.getId(Order.URLORDER, cod);
        PedidoCERPBean bean = new PedidoCERPBean();

        bean.setId_ecom(order.getId());
        bean.setId_address_delivery(order.getId_address_delivery());
        bean.setId_address_invoice(order.getId_address_invoice());
        bean.setId_customer(order.getId_customer());
        bean.setCurrent_state(StatusPedido.thisValueOf(order.getCurrent_state()));
        bean.setInvoice_date(order.getInvoice_date());
        bean.setDelivery_date(order.getDelivery_date());
        bean.setPayment(order.getPayment());
        bean.setTotal_discounts(order.getTotal_discounts());
        bean.setTotal_paid(order.getTotal_paid());
        bean.setTotal_paid_real(order.getTotal_paid_real());
        bean.setTotal_products(order.getTotal_products());
        bean.setReference(order.getReference());

        return bean;
    }
    /**
     * Cadastra pedido na loja virtual
     * @param order
     * @return 
     */
    public Order postOrder(Order order){
        OrderPrestashopDAO orderDAO = new OrderPrestashopDAO();
        return orderDAO.postOrder(Order.URLORDER, order);
    }

    public List<PedidoCERPBean> buscaListaPendentes(){
        List<PedidoCERPBean> lista = new ArrayList<>();
        try {
            List<Order> listOrder = this.getOrderPrestashopDAO().get(Order.URLORDERPENDING);            
            
            ParaStatusPedidoDAO dao = SingletonUtil.get(ParaStatusPedidoDAO.class);
            for (Order order : listOrder) {
                try {
                    if (order == null || order.getId() == null) {
                        throw new ErroSyncException("Falha de integridade ao ler informações que vieram do WS.");
                    }

                    PedidoCERPBean bean = new PedidoCERPBean();
                    bean.setId_ecom(order.getId());
                    bean.setId_address_delivery(order.getId_address_delivery());
                    bean.setId_address_invoice(order.getId_address_invoice());
                    bean.setId_customer(order.getId_customer());
                    bean.setId_carrier(order.getId_carrier());
                    bean.setModule(order.getModule());
                    bean.setInvoice_number(order.getInvoice_number());
                    bean.setInvoice_date(order.getInvoice_date());
                    bean.setDelivery_date(order.getDelivery_date());       
                    
                    ParaStatusPedidoBean status = dao.abrir(order.getCurrent_state());
                    if (status == null || status.getStatus() == null) {
                        throw new GenericMiddleException("Não foi encontrado o status correspondente para o ID \"" +
                                order.getCurrent_state() + "\", configure os status correspondentes antes de continuar.");
                    } else {
                        bean.setCurrent_state(status.getStatus());
                    }
                    
                    Date dateAdd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(order.getDate_add());
                    bean.setDate_add(dateAdd);
                    bean.setHora(new SimpleDateFormat("HH:mm:ss").format(dateAdd));
                    
                    bean.setPayment(order.getPayment());
                    bean.setTotal_discounts(order.getTotal_discounts());
                    bean.setTotal_paid(order.getTotal_paid());
                    bean.setTotal_paid_real(order.getTotal_paid_real());
                    bean.setTotal_products(order.getTotal_products());
                    bean.setTotal_shipping(order.getTotal_shipping());
                    bean.setReference(order.getReference());
                    bean.setInstallmentCounts(order.getInstallmentCounts());
                    bean.setIdTransaction(order.getIdTransaction());
                    bean.setDateUpd(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(order.getDateUpd()));
                    bean.setListItensPedido(order.getAssociations().getOrderRowsNode().getOrderRow());
                    
                    lista.add(bean);
                } catch (Exception e) {
                    logger.error("Erro ao processar pedido, o processo continuará.", e);
                }
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar pedidos da loja prestashop", e);
            throw new RuntimeException(e);
        }
    }

    public int updateStatusOrder(int orderId, StatusPedido status) {
        try {
            OrderPrestashopDAO dao = new OrderPrestashopDAO();
            Order order = dao.getId(Order.URLORDER, orderId);
            
            Integer idState = SingletonUtil.get(ParaStatusPedidoDAO.class).getIdPadrao(status);
            if (idState == null) {
                throw new GenericMiddleException("Não foi encontrado um status correspondente "
                        + " para status \"" + status.getDescricao() 
                        + "\", verifique nas configurações os valores padrões.");
            }
            order.setCurrent_state(idState.toString());
            return dao.put(Order.URLORDER, order);
        } catch (Exception e) {
            logger.error("Erro ao atualizar Status Cod:(" + status + ") do PedidosEcon Cod:(" + orderId + ") na loja Prestashop: " + e);
            throw new RuntimeException(e);
        }
    }
    
    public int updateCodRastreio(int orderId, String codRastreamento) {
        try {
            if (codRastreamento == null || codRastreamento.isEmpty()) {
                 throw new GenericMiddleException("Não foi informado o código de rastreio.");
            }
            
            OrderPrestashopDAO dao = new OrderPrestashopDAO();
            Order order = dao.getId(Order.URLORDER, orderId);
            order.setShippingNumber(codRastreamento);
            return dao.put(Order.URLORDER, order);
        } catch (Exception e) {
            logger.error("Erro ao atualizar código de rastreio");
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Retorna pedido pelo id do pedido
     * @param orderID id do pedido
     * @return Order
     */
    public Order getOrderById(int orderID){
        OrderPrestashopDAO dao = new OrderPrestashopDAO();
        return dao.getId(Order.URLORDER, orderID);     
    }
    
    /**
     * Atualiza do pedido do ERP na loja virtual
     * @param orderId id do pedido na loja virtual
     * @param orderERPId id do erp 
     */
    public void updateIdErp(int orderId, int orderERPId) {
        try {
            OrderPrestashopDAO dao = new OrderPrestashopDAO();
            Order order = dao.getId(Order.URLORDER, orderId);
            order.setIdErp(orderERPId);
            int status = dao.put(Order.URLORDER, order);
            
            if (status != 200) {
                throw new ErroSyncException("Falha ao atualizar referência do ERP no pedido do e-commerce.");
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar id do ERP Cod:(" + orderERPId + ") na loja Prestashop: ", e);
            throw new RuntimeException(e);
        }

    }
    
    /* ******* */
    public OrderPrestashopDAO getOrderPrestashopDAO() {
        if( this.orderPrestashopDAO == null){
            this.orderPrestashopDAO = new OrderPrestashopDAO();
        }
        return orderPrestashopDAO;
    }
}
