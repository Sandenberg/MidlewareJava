/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.prestashop.clientDAO.OrderSlipDAO;
import br.com.atsinformatica.prestashop.model.root.OrderSlip;

/**
 *
 * @author AlexsanderPimenta
 */
public class OrderSlipController {
    OrderSlipDAO orderSlipDAO;
        
    public OrderSlipController(){
        orderSlipDAO = new OrderSlipDAO();
    }
    
    public OrderSlip postOrderSlip(OrderSlip orderSlip){
        return orderSlipDAO.postOrderSlip(OrderSlip.URLORDERSLIP, orderSlip);
    }
    
    
    
    
    
    
    
    
}
