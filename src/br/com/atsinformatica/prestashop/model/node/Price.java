package br.com.atsinformatica.prestashop.model.node;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder = {
    "content"
})
@XmlRootElement(name = "price")
public class Price {

    @XmlValue
    private String content;
    
    
    public Price(String content){
        this.content = content;
    }
    
    public Price(){
        
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String value) {
        this.content = value;
    }

}
