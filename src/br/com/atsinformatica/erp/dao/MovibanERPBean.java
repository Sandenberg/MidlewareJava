package br.com.atsinformatica.erp.dao;

import java.util.Date;

/**
 *
 * @author AlexsanderPimenta
 */
public class MovibanERPBean {
    
    private String codEmpresa;
    private String codBanco;
    private String codAgen;
    private String conta;
    private Date dataMov;
    private String numoRd;

    /**
     * @return the codEmpresa
     */
    public String getCodEmpresa() {
        return codEmpresa;
    }

    /**
     * @param codEmpresa the codEmpresa to set
     */
    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
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

    /**
     * @return the codAgen
     */
    public String getCodAgen() {
        return codAgen;
    }

    /**
     * @param codAgen the codAgen to set
     */
    public void setCodAgen(String codAgen) {
        this.codAgen = codAgen;
    }

    /**
     * @return the dataMov
     */
    public Date getDataMov() {
        return dataMov;
    }

    /**
     * @param dataMov the dataMov to set
     */
    public void setDataMov(Date dataMov) {
        this.dataMov = dataMov;
    }

    /**
     * @return the numoRd
     */
    public String getNumoRd() {
        return numoRd;
    }

    /**
     * @param numoRd the numoRd to set
     */
    public void setNumoRd(String numoRd) {
        this.numoRd = numoRd;
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
    
}
