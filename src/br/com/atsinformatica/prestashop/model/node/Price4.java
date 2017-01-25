/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.node;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AlexsanderPimenta
 */
@XmlRootElement(name = "price4")
public class Price4 extends Price{
    
    public Price4(String content){
        super(content);
    }
    
    public Price4(){
        super();
    }
    
}
