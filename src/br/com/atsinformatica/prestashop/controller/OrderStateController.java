package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.prestashop.clientDAO.OrderStatePrestashopDAO;
import br.com.atsinformatica.prestashop.model.list.OrderStates;
import br.com.atsinformatica.prestashop.model.list.prestashop.WSItens;
import br.com.atsinformatica.prestashop.model.root.OrderState;
import br.com.atsinformatica.utils.SingletonUtil;

/**
 *
 * @author niwrodrigues
 */
public class OrderStateController {
   
    public OrderState get(String id) {
        OrderStatePrestashopDAO dao = SingletonUtil.get(OrderStatePrestashopDAO.class);
        OrderState os = dao.getId(OrderState.URLORDERSTATE, Integer.parseInt(id));
        return os;
    }
    
    public OrderStates listItens(){
        OrderStatePrestashopDAO dao = SingletonUtil.get(OrderStatePrestashopDAO.class);
        WSItens wsItens = dao.getAllItens();
        return wsItens.getOrderStates();
    }
}
