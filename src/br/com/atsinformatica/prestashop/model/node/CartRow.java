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
    "idProduct",
    "idProductAttribute",
    "idAddressDelivery",
    "quantity",
})
@XmlRootElement(name = "cart_row")
public class CartRow {
    
    @XmlElement(name = "id_product")
    private String idProduct;
    @XmlElement(name = "id_product_attribute")
    private String idProductAttribute;
    @XmlElement(name = "id_address_delivery")
    private String idAddressDelivery;
    @XmlElement(name = "quantity")
    private int quantity;

    /**
     * @return the idProduct
     */
    public String getIdProduct() {
        return idProduct;
    }

    /**
     * @param idProduct the idProduct to set
     */
    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    /**
     * @return the idProductAttribute
     */
    public String getIdProductAttribute() {
        return idProductAttribute;
    }

    /**
     * @param idProductAttribute the idProductAttribute to set
     */
    public void setIdProductAttribute(String idProductAttribute) {
        this.idProductAttribute = idProductAttribute;
    }

    /**
     * @return the idAddressDelivery
     */
    public String getIdAddressDelivery() {
        return idAddressDelivery;
    }

    /**
     * @param idAddressDelivery the idAddressDelivery to set
     */
    public void setIdAddressDelivery(String idAddressDelivery) {
        this.idAddressDelivery = idAddressDelivery;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
}
