/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.list;

import br.com.atsinformatica.prestashop.model.node.CartRow;
import br.com.atsinformatica.prestashop.model.node.CategoryNode;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author AlexsanderPimenta
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "cartRow",
})
@XmlRootElement(name = "cart_rows")
public class CartRows {
    @XmlAttribute(name = "node_type")
    protected String nodeType = "cart_rows";    
    @XmlElement(name = "cart_row")
    private List<CartRow> cartRow;

    /**
     * @return the cartRow
     */
    public List<CartRow> getCartRow() {
        return cartRow;
    }

    /**
     * @param cartRow the cartRow to set
     */
    public void setCartRow(List<CartRow> cartRow) {
        this.cartRow = cartRow;
    }
    
    
    
}
