/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.prestashop.clientDAO.StockAvailableDAO;
import br.com.atsinformatica.prestashop.model.root.StockAvailable;

/**
 * Controladora de estoque
 * @author AlexsanderPimenta
 */
public class StockAvailableController {
    
    public int createStockAvailable(StockAvailable stock){
        return new StockAvailableDAO().postStockAvailable(StockAvailable.URLSTOCKAVAILABLE, stock);
    }
    
    public int updateStockAvailable(StockAvailable stock){
        return new StockAvailableDAO().put(StockAvailable.URLSTOCKAVAILABLE, stock);
    }
    
    public StockAvailable getStock(int idStock){
        return new StockAvailableDAO().getId(StockAvailable.URLSTOCKAVAILABLE, idStock);
    }
    
}
