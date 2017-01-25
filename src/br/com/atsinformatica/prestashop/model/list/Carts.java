/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.list;

import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttribute;
import java.util.ArrayList;
import java.util.List;
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
@XmlType(name = "", propOrder = { "cart" })
@XmlRootElement(name = "carts")
public class Carts {
    
    @XmlElement(name = "cart")
    private List<AccessXMLAttribute> cart;

    /**
     * @return the cart
     */
    public List<AccessXMLAttribute> getCart() {
        if(cart == null)
            cart = new ArrayList<AccessXMLAttribute>();
        return cart;
    }

    /**
     * @param cart the cart to set
     */
    public void setCart(List<AccessXMLAttribute> cart) {
        this.cart = cart;
    }
    
}
