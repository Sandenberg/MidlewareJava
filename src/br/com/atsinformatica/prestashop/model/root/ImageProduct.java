/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.root;

import br.com.atsinformatica.prestashop.model.node.Declination;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author AlexsanderPimenta
 */
@XmlType(propOrder = {
    "declination",
})
@XmlRootElement(name = "images")
public class ImageProduct extends Prestashop{
    
    public static String URLIMAGESPRODUCT = "images/products/";
    
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    private String id;
    @XmlElement(name = "declination")
    private List<Declination> declination;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the declinations
     */
    public List<Declination> getDeclination() {
        if(declination == null){
            declination = new ArrayList<>();
        }
        return declination;
    }

    /**
     * @param declinations the declinations to set
     */
    public void setDeclinations(List<Declination> declination) {
        this.declination = declination;
    }
    
    
}
