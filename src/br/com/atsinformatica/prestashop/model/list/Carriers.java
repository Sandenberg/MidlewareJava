/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author kennedimalheiros
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"carrier"})
@XmlRootElement(name = "carriers")

public class Carriers {

    @XmlElement(name = "carrier")
    protected List<AccessXMLAttribute> carrier;

    public List<AccessXMLAttribute> getCarrier() {
        if (carrier == null) {
            carrier = new ArrayList<AccessXMLAttribute>();
        }
        return this.carrier;
    }

    public void setCarrier(List<AccessXMLAttribute> listCarrier) {
        this.carrier = listCarrier;
    }

}
