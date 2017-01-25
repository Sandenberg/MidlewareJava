/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.list;

import br.com.atsinformatica.prestashop.model.node.OrderSlipDetail;
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
    "orderSlipDetail",
})
@XmlRootElement(name = "order_slip_details")
public class OrderSlipDetails {
    
    @XmlAttribute(name = "node_type")
    protected String nodeType = "order_slip_detail";    
    @XmlElement(name = "order_slip_detail")
    private List<OrderSlipDetail> orderSlipDetail;

    /**
     * @return the orderSlipDetail
     */
    public List<OrderSlipDetail> getOrderSlipDetail() {
        return orderSlipDetail;
    }

    /**
     * @param orderSlipDetail the orderSlipDetail to set
     */
    public void setOrderSlipDetail(List<OrderSlipDetail> orderSlipDetail) {
        this.orderSlipDetail = orderSlipDetail;
    }
    
}
