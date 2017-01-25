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
    "id",
    "idOrderDetail",
    "productQuantity",
    "amountTaxExcl",
    "amountTaxIncl",
})
@XmlRootElement(name = "order_slip_detail")
public class OrderSlipDetail {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "id_order_detail")
    private int idOrderDetail;
    @XmlElement(name = "product_quantity")
    private int productQuantity;
    @XmlElement(name = "amount_tax_excl")
    private Double amountTaxExcl;
    @XmlElement(name = "amount_tax_incl")
    private Double amountTaxIncl;

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
     * @return the idOrderDetail
     */
    public int getIdOrderDetail() {
        return idOrderDetail;
    }

    /**
     * @param idOrderDetail the idOrderDetail to set
     */
    public void setIdOrderDetail(int idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    /**
     * @return the productQuantity
     */
    public int getProductQuantity() {
        return productQuantity;
    }

    /**
     * @param productQuantity the productQuantity to set
     */
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    /**
     * @return the amountTaxExcl
     */
    public Double getAmountTaxExcl() {
        return amountTaxExcl;
    }

    /**
     * @param amountTaxExcl the amountTaxExcl to set
     */
    public void setAmountTaxExcl(Double amountTaxExcl) {
        this.amountTaxExcl = amountTaxExcl;
    }

    /**
     * @return the amountTaxIncl
     */
    public Double getAmountTaxIncl() {
        return amountTaxIncl;
    }

    /**
     * @param amountTaxIncl the amountTaxIncl to set
     */
    public void setAmountTaxIncl(Double amountTaxIncl) {
        this.amountTaxIncl = amountTaxIncl;
    }
    
}
