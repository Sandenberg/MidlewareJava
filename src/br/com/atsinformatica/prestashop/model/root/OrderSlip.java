/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.root;

import br.com.atsinformatica.prestashop.model.node.AssociationsOrderSlipDetail;
import java.util.Date;
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
@XmlType(name = "order_slip", propOrder = {
    "id",
    "idCustomer",
    "idOrder",
    "conversionRate",
    "amount",
    "shippingCost",
    "shippingCostAmount",
    "partial",
    "dateAdd",
    "dateUpd",
    "associations",
})
@XmlRootElement(name = "order_slip")
public class OrderSlip {
    public static final String URLORDERSLIP = "/order_slip"; 
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "id_order")
    private int idOrder;
    @XmlElement(name = "id_customer")
    private int idCustomer;
    @XmlElement(name = "conversion_rate")
    private Double conversionRate;
    @XmlElement(name = "amount")
    private Double amount;
    @XmlElement(name = "shipping_cost")
    private Double shippingCost;
    @XmlElement(name = "shipping_cost_amount")
    private Double shippingCostAmount;
    @XmlElement(name = "partial")
    private int partial;
    @XmlElement(name = "date_add")
    private Date dateAdd;
    @XmlElement(name = "date_upd")
    private Date dateUpd;
    @XmlElement(name = "associations")
    private AssociationsOrderSlipDetail associations;

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
     * @return the idOrder
     */
    public int getIdOrder() {
        return idOrder;
    }

    /**
     * @param idOrder the idOrder to set
     */
    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    /**
     * @return the idCustomer
     */
    public int getIdCustomer() {
        return idCustomer;
    }

    /**
     * @param idCustomer the idCustomer to set
     */
    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    /**
     * @return the conversionRate
     */
    public Double getConversionRate() {
        return conversionRate;
    }

    /**
     * @param conversionRate the conversionRate to set
     */
    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @return the shippingCost
     */
    public Double getShippingCost() {
        return shippingCost;
    }

    /**
     * @param shippingCost the shippingCost to set
     */
    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    /**
     * @return the shippingCostAmount
     */
    public Double getShippingCostAmount() {
        return shippingCostAmount;
    }

    /**
     * @param shippingCostAmount the shippingCostAmount to set
     */
    public void setShippingCostAmount(Double shippingCostAmount) {
        this.shippingCostAmount = shippingCostAmount;
    }

    /**
     * @return the partial
     */
    public int getPartial() {
        return partial;
    }

    /**
     * @param partial the partial to set
     */
    public void setPartial(int partial) {
        this.partial = partial;
    }

    /**
     * @return the dateAdd
     */
    public Date getDateAdd() {
        return dateAdd;
    }

    /**
     * @param dateAdd the dateAdd to set
     */
    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    /**
     * @return the dateUpd
     */
    public Date getDateUpd() {
        return dateUpd;
    }

    /**
     * @param dateUpd the dateUpd to set
     */
    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }

    /**
     * @return the associations
     */
    public AssociationsOrderSlipDetail getAssociations() {
        return associations;
    }

    /**
     * @param associations the associations to set
     */
    public void setAssociations(AssociationsOrderSlipDetail associations) {
        this.associations = associations;
    }
    
}
