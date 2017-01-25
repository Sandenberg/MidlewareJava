/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.node;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Nó para parodutos agregados
 * @author AlexsanderPimenta
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "product",
})
@XmlRootElement(name="accessories")
public class Accessories {
    
    @XmlAttribute(name = "node_type")
    protected String nodeType = "product";
    
    @XmlElement(name = "product")
    private List<ProductNode> product;

    /**
     * @return the product
     */
    public List<ProductNode> getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(List<ProductNode> product) {
        this.product = product;
    }
    
}
