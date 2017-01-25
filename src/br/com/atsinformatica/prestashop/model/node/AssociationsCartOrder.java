/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.node;

import br.com.atsinformatica.prestashop.model.list.CartRows;
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
    "cartRows", 
})
@XmlRootElement(name = "associations")
public class AssociationsCartOrder {
    
    @XmlElement(name = "cart_rows")
    private CartRows cartRows;

    /**
     * @return the cartRows
     */
    public CartRows getCartRows() {
        return cartRows;
    }

    /**
     * @param cartRows the cartRows to set
     */
    public void setCartRows(CartRows cartRows) {
        this.cartRows = cartRows;
    }
    
}
