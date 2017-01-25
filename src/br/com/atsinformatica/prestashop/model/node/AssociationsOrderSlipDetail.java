package br.com.atsinformatica.prestashop.model.node;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import br.com.atsinformatica.prestashop.model.list.OrderSlipDetails;
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
    "orderSlipDetails", 
})
@XmlRootElement(name = "order_slip_details")
public class AssociationsOrderSlipDetail {
    
    @XmlElement(name = "order_slip_details")
    private OrderSlipDetails orderSlipDetails;

    /**
     * @return the orderSlipDetails
     */
    public OrderSlipDetails getOrderSlipDetails() {
        return orderSlipDetails;
    }

    /**
     * @param orderSlipDetails the orderSlipDetails to set
     */
    public void setOrderSlipDetails(OrderSlipDetails orderSlipDetails) {
        this.orderSlipDetails = orderSlipDetails;
    }
    
    
}
