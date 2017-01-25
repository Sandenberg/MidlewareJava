/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.root;

import br.com.atsinformatica.prestashop.model.node.Delay;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author kennedimalheiros
 */
@XmlRootElement(name = "carrier")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carrier", propOrder = {
    "idEcom",
    "name",// Nome transportadora    
    "active",
    "delay",
    "idERP",
    "deleted",
    "idReference"})
public class Carrier {

    public static String URLCARRIER = "carriers/";

    @XmlElement(name = "id", required = true)
    private String idEcom;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "active")
    private int active;
    @XmlElement(name = "delay")
    private Delay delay;
    @XmlElement(name = "id_erp")
    private String idERP;
    @XmlElement(name = "deleted")
    private String deleted;
    @XmlElement(name = "id_reference")
    private String idReference;

    public String getIdEcom() {
        return idEcom;
    }

    public void setIdEcom(String idEcom) {
        this.idEcom = idEcom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Delay getDelay() {
        return delay;
    }

    public void setDelay(Delay delay) {
        this.delay = delay;
    }

    public String getIdERP() {
        return idERP;
    }

    public void setIdERP(String idERP) {
        this.idERP = idERP;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getIdReference() {
        return idReference;
    }

    public void setIdReference(String idReference) {
        this.idReference = idReference;
    }

}
