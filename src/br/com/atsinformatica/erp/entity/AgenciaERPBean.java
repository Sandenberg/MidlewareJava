/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

/**
 *
 * @author AlexsanderPimenta
 */
public class AgenciaERPBean {
    private String codBanco;
    private String codAgencia;
    private String nomeAgencia;

    /**
     * @return the codBanco
     */
    public String getCodBanco() {
        return codBanco;
    }

    /**
     * @param codBanco the codBanco to set
     */
    public void setCodBanco(String codBanco) {
        this.codBanco = codBanco;
    }

    /**
     * @return the codAgencia
     */
    public String getCodAgencia() {
        return codAgencia;
    }

    /**
     * @param codAgencia the codAgencia to set
     */
    public void setCodAgencia(String codAgencia) {
        this.codAgencia = codAgencia;
    }

    /**
     * @return the nomeAgencia
     */
    public String getNomeAgencia() {
        return nomeAgencia;
    }

    @Override
    public String toString() {
        return codAgencia + " " + nomeAgencia;
    }

    /**
     * @param nomeAgencia the nomeAgencia to set
     */
    public void setNomeAgencia(String nomeAgencia) {
        this.nomeAgencia = nomeAgencia;
    }
    
}
