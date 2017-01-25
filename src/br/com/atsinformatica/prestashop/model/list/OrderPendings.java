/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.list;

import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttributeOrder;
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
@XmlType(name = "", propOrder = {"order"})
@XmlRootElement(name = "order_pending")
public class OrderPendings {
    
    @XmlElement(name = "order")
    protected List<AccessXMLAttributeOrder> order;

    public List<AccessXMLAttributeOrder> getOrder() {
        if (order == null) {
            order = new ArrayList<AccessXMLAttributeOrder>();
        }
        return this.order;
    }

    public void setOrder(List<AccessXMLAttributeOrder> listOrder) {
        this.order = listOrder;
    }
    
}
