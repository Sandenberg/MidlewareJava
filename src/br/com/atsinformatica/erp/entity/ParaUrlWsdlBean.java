/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import com.towel.el.annotation.Resolvable;
import java.util.Objects;

/**
 * Bean de configuração de lista de url/wsdl
 * @author AlexsanderPimenta
 */
public class ParaUrlWsdlBean {
    
    @Resolvable(colName = "COD")
    private String codParaUrlWsdl;
    @Resolvable(colName = "Url/WSDL")
    private String urlWSDL;
    @Resolvable(colName = "Chave")
    private String urlKey;

    /**
     * @return the codParaUrlWsdl
     */
    public String getCodParaUrlWsdl() {
        return codParaUrlWsdl;
    }

    /**
     * @param codParaUrlWsdl the codParaUrlWsdl to set
     */
    public void setCodParaUrlWsdl(String codParaUrlWsdl) {
        this.codParaUrlWsdl = codParaUrlWsdl;
    }

    /**
     * @return the urlWSDL
     */
    public String getUrlWSDL() {
        return urlWSDL;
    }

    /**
     * @param urlWSDL the urlWSDL to set
     */
    public void setUrlWSDL(String urlWSDL) {
        this.urlWSDL = urlWSDL;
    }

    /**
     * @return the urlKey
     */
    public String getUrlKey() {
        return urlKey;
    }

    /**
     * @param urlKey the urlKey to set
     */
    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParaUrlWsdlBean other = (ParaUrlWsdlBean) obj;
        if (!Objects.equals(this.codParaUrlWsdl, other.codParaUrlWsdl)) {
            return false;
        }
        return true;
    }

    
    
    
    
}
