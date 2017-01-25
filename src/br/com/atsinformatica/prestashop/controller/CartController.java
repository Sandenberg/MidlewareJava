/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.prestashop.clientDAO.CartPrestashopDAO;
import br.com.atsinformatica.prestashop.model.root.Cart;

/**
 *
 * @author AlexsanderPimenta
 */
public class CartController {
    CartPrestashopDAO cartDAO;
    
    public CartController(){
        cartDAO = new CartPrestashopDAO();
    }
    /**
     * Cria novo carrinho de compras
     * @param t
     * @return 
     */
    public Cart postCart(Cart t){
        return cartDAO.postCart(Cart.URLCART, t);
    }
    
    /**
     * Retorna carrinho de compras 
     * @param id
     * @return 
     */
    public Cart getCart(int id){
        return cartDAO.getId(Cart.URLCART, id);
    }
    
    
    
}
