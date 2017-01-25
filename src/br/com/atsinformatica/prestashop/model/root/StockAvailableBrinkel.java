/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.root;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author AlexsanderPimenta
 */
/**
 *
 * @author AlexsanderPimenta
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "id",
    "idProduct",
    "idProductAttribute",
    "quantity",
})

@XmlRootElement(name = "stock_available")
public class StockAvailableBrinkel {
    public static String URLSTOCK = "stock_availables";
    
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "id_product")
    private int idProduct;
    @XmlElement(name = "id_product_attribute")
    private int idProductAttribute;
    @XmlElement(name = "quantity")
    private int quantity;    

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the idProduct
     */
    public int getIdProduct() {
        return idProduct;
    }

    /**
     * @param idProduct the idProduct to set
     */
    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    /**
     * @return the idProductAttribute
     */
    public int getIdProductAttribute() {
        return idProductAttribute;
    }

    /**
     * @param idProductAttribute the idProductAttribute to set
     */
    public void setIdProductAttribute(int idProductAttribute) {
        this.idProductAttribute = idProductAttribute;
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
