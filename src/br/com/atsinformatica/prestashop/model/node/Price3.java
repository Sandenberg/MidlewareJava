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

@XmlRootElement(name = "price3")
public class Price3 extends Price {
    
    public Price3(String content){
       super(content);
    }
    
    public Price3(){
       super();
    }
}
