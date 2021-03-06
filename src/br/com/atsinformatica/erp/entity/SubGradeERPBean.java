/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import br.com.atsinformatica.midler.annotation.GenericController;

/**
 *
 * @author AlexsanderPimenta
 */
@GenericController(classPath = "br.com.atsinformatica.erp.controller.SubGradeERPController")
public class SubGradeERPBean {
    private String codSubgrade;
    private String descSubGrade;
    private int tipoDivisao;
    private int idSubgradeEcom;
    private int idAtributo;

    /**
     * @return the codSubgrade
     */
    public String getCodSubgrade() {
        return codSubgrade;
    }

    /**
     * @param codSubgrade the codSubgrade to set
     */
    public void setCodSubgrade(String codSubgrade) {
        this.codSubgrade = codSubgrade;
    }

    /**
     * @return the descSubGrade
     */
    public String getDescSubGrade() {
        return descSubGrade;
    }

    /**
     * @param descSubGrade the descSubGrade to set
     */
    public void setDescSubGrade(String descSubGrade) {
        this.descSubGrade = descSubGrade;
    }

    /**
     * @return the idSubgradeEcom
     */
    public int getIdSubgradeEcom() {
        return idSubgradeEcom;
    }

    /**
     * @param idSubgradeEcom the idSubgradeEcom to set
     */
    public void setIdSubgradeEcom(int idSubgradeEcom) {
        this.idSubgradeEcom = idSubgradeEcom;
    }

    /**
     * @return the tipoDivisao
     */
    public int getTipoDivisao() {
        return tipoDivisao;
    }

    /**
     * @param tipoDivisao the tipoDivisao to set
     */
    public void setTipoDivisao(int tipoDivisao) {
        this.tipoDivisao = tipoDivisao;
    }

    /**
     * @return the idAtributo
     */
    public int getIdAtributo() {
        return idAtributo;
    }

    /**
     * @param idAtributo the idAtributo to set
     */
    public void setIdAtributo(int idAtributo01) {
        this.idAtributo = idAtributo01;
    }

}
