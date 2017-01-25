/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.node;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author AlexsanderPimenta
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "categories",  
  ///  "stockAvailables",
})
@XmlRootElement(name="associations")
public class AssociationNodeBrinkel {
    
    @XmlElement(name = "categories")    
    private CategoriesNode categories;
    
//    @XmlElement(name = "stock_availables")
//    private StockAvailablesNode stockAvailables;

    /**
     * @return the categories
     */
    public CategoriesNode getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(CategoriesNode categories) {
        this.categories = categories;
    }

//    /**
//     * @return the stockAvailables
//     */
//    public StockAvailablesNode getStockAvailables() {
//        return stockAvailables;
//    }
//
//    /**
//     * @param stockAvailables the stockAvailables to set
//     */
//    public void setStockAvailables(StockAvailablesNode stockAvailables) {
//        this.stockAvailables = stockAvailables;
//    }
    
}
