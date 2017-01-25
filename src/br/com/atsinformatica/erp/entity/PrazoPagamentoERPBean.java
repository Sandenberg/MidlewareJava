/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

/**
 *
 * @author AlexsanderPimenta
 */
public class PrazoPagamentoERPBean {
    private String codPrazo;
    private String descricao;
    private Double prazo1;
    private Double prazo2;
    private Double prazo3;
    private Double prazo4;
    private Double prazo5;
    private Double prazo6;
    private Double prazo7;
    private int    numparcelas;

    /**
     * @return the codPrazo
     */
    public String getCodPrazo() {
        return codPrazo;
    }

    /**
     * @param codPrazo the codPrazo to set
     */
    public void setCodPrazo(String codPrazo) {
        this.codPrazo = codPrazo;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return codPrazo + " " + descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the numparcelas
     */
    public int getNumparcelas() {
        return numparcelas;
    }

    /**
     * @param numparcelas the numparcelas to set
     */
    public void setNumparcelas(int numparcelas) {
        this.numparcelas = numparcelas;
    }

    /**
     * @return the prazo1
     */
    public Double getPrazo1() {
        return prazo1;
    }

    /**
     * @param prazo1 the prazo1 to set
     */
    public void setPrazo1(Double prazo1) {
        this.prazo1 = prazo1;
    }

    /**
     * @return the prazo2
     */
    public Double getPrazo2() {
        return prazo2;
    }

    /**
     * @param prazo2 the prazo2 to set
     */
    public void setPrazo2(Double prazo2) {
        this.prazo2 = prazo2;
    }

    /**
     * @return the prazo3
     */
    public Double getPrazo3() {
        return prazo3;
    }

    /**
     * @param prazo3 the prazo3 to set
     */
    public void setPrazo3(Double prazo3) {
        this.prazo3 = prazo3;
    }

    /**
     * @return the prazo4
     */
    public Double getPrazo4() {
        return prazo4;
    }

    /**
     * @param prazo4 the prazo4 to set
     */
    public void setPrazo4(Double prazo4) {
        this.prazo4 = prazo4;
    }

    /**
     * @return the prazo5
     */
    public Double getPrazo5() {
        return prazo5;
    }

    /**
     * @param prazo5 the prazo5 to set
     */
    public void setPrazo5(Double prazo5) {
        this.prazo5 = prazo5;
    }

    /**
     * @return the prazo6
     */
    public Double getPrazo6() {
        return prazo6;
    }

    /**
     * @param prazo6 the prazo6 to set
     */
    public void setPrazo6(Double prazo6) {
        this.prazo6 = prazo6;
    }

    /**
     * @return the prazo7
     */
    public Double getPrazo7() {
        return prazo7;
    }

    /**
     * @param prazo7 the prazo7 to set
     */
    public void setPrazo7(Double prazo7) {
        this.prazo7 = prazo7;
    }
    
}
