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

@XmlRootElement(name = "price2")
public class Price2 extends Price {
    
    public Price2(String content){
        super(content);
    }
    
    public Price2(){
        super();
    }
    
}
