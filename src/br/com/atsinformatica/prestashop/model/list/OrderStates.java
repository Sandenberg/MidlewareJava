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
@XmlType(name = "", propOrder = {"listOrderState"})
@XmlRootElement(name = "order_states")
public class OrderStates {

    @XmlElement(name = "order_state")
    protected List<AccessXMLAttribute> listOrderState;

    public List<AccessXMLAttribute> getOrderStates() {
        if (listOrderState == null) {
            listOrderState = new ArrayList<AccessXMLAttribute>();
        }
        return this.listOrderState;
    }

    public void setOrderStates(List<AccessXMLAttribute> listOrderState) {
        this.listOrderState = listOrderState;
    }
}
