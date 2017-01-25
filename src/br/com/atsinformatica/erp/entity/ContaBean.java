/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

/**
 *
 * @author AlexsanderPimenta
 */
public class ContaBean {
    private String codAgencia;
    private String conta;
    private String codBanco;

    @Override
    public String toString() {
        return conta;
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
     * @return the conta
     */
    public String getConta() {
        return conta;
    }

    /**
     * @param conta the conta to set
     */
    public void setConta(String conta) {
        this.conta = conta;
    }

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
    
}
