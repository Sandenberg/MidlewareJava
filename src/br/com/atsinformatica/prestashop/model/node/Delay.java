package br.com.atsinformatica.prestashop.model.node;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kennedimalheiros
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "language"
})
@XmlRootElement(name = "delay")
public class Delay {

    public Delay() {
    }

    @XmlElement(required = true)
    protected List<Language> language;

    public List<Language> getLanguage() {
        if (language == null) {
            language = new ArrayList<Language>();
        }
        return this.language;
    }

}
