package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.dao.ParaStatusPedidoDAO;
import br.com.atsinformatica.erp.dao.PedidoCERPDAO;
import br.com.atsinformatica.erp.dao.PedidoTrocaERPDAO;
import br.com.atsinformatica.erp.entity.PedidoCBean;
import br.com.atsinformatica.erp.entity.PedidocTrocaERPBean;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.exception.GenericMiddleException;
import br.com.atsinformatica.prestashop.controller.CartController;
import br.com.atsinformatica.prestashop.controller.OrderController;
import br.com.atsinformatica.prestashop.model.root.Cart;
import br.com.atsinformatica.prestashop.model.root.Order;
import br.com.atsinformatica.utils.SingletonUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class PedidoTrocaController extends SincERPController<PedidocTrocaERPBean> {
    private final static Logger logger = Logger.getLogger(PedidoTrocaController.class);
    
    @Override
    public void post(PedidocTrocaERPBean obj) throws Exception {
        PedidoTrocaERPDAO pedidoTrocaDAO = SingletonUtil.get(PedidoTrocaERPDAO.class);
        OrderController orderController = SingletonUtil.get(OrderController.class);
        PedidoCERPDAO pedidoCDAO = SingletonUtil.get(PedidoCERPDAO.class);
        CartController cartController = SingletonUtil.get(CartController.class);
        
        try {
            //pedido original do erp
            PedidoCBean pOriginal = pedidoCDAO.abrirPedido(obj.getCodPedidoOriginal());
            if (pOriginal != null) {      
                //retornando pedido original da loja 
                Order order = orderController.getOrderById(pOriginal.getIdPedidoEcom());
                PedidoCBean pedidoVenda = pedidoCDAO.abrirPedido(obj.getCodPedidoVenda());               
                order.setId(null);
                order.setIdErp(Integer.parseInt(pedidoVenda.getCodPedido()));
                order.setTotal_paid_real(String.valueOf(pedidoVenda.getTotalPedido()));
               
                //status do pedido como pago
                pedidoVenda.setStatusPedidoEcom(StatusPedido.PAGAMENTO_ACEITO);                
                Integer idState = SingletonUtil.get(ParaStatusPedidoDAO.class).getIdPadrao(pedidoVenda.getStatusPedidoEcom());
                if (idState == null) {
                    throw new GenericMiddleException("Não foi encontrado um status correspondente "
                            + " para status \"" + pedidoVenda.getStatusPedidoEcom().getDescricao() 
                            + "\", verifique nas configurações os valores padrões.");
                }
                order.setCurrent_state(idState.toString());
                
                //retorna carrinho de compras
                Cart cart = cartController.getCart(Integer.parseInt(order.getIdCart()));
                cart.setId(null);
                order.setReference("");
                order.setId(null);
                Cart newCart = cartController.postCart(cart);
                order.setIdCart(newCart.getId());
              
                // Atualiza status de pedido anterior para troca
                orderController.updateStatusOrder(pOriginal.getIdPedidoEcom(), StatusPedido.TROCADO);
                
                /**
                 * Cadastra novo pedido na loja
                 *
                 * TODO: existe uma falha ao compactar HTML e JS no prestashop,
                 * caso esteja apresentando erro aqui, tente desabilitar
                 * "PARÂMETROS AVANÇADOS" > "DESEMPENHO", grupo "CCC
                 * (COMBINAÇÃO, COMPRESSÃO E CACHE)", parâmetros "Minimizar
                 * HTML" e "Comprimir JavaScript inline em HTML"
                 */
                Order newOrder = orderController.postOrder(order);
                
                pedidoVenda.setIdPedidoEcom(Integer.parseInt(newOrder.getId()));
                pedidoVenda.setCodPedidoEcom(newOrder.getReference());                
                pedidoVenda.setObservacao(pOriginal.getObservacao() 
                        + " Originado pela troca do pedido " + pOriginal.getCodPedidoEcom());
                pOriginal.setObservacao(pOriginal.getObservacao() 
                        + " Trocado pelo pedido " + pedidoVenda.getCodPedidoEcom());
                
                // Atualiza pedido final
                pedidoCDAO.atualizaPedido(pedidoVenda);
                // Atualiza status do pedido original
                pedidoCDAO.atualizaPedido(pOriginal);
            } else {
                throw new GenericMiddleException("Não foi possível identificar a origem do pedido.");
            }
        } catch (Exception e) {
            logger.error("Erro ao efetuar troca", e);
            throw new RuntimeException(e);
        }
    }
    
    
    
    
}
