/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.entity.enumeration.TipoOperacao;
import java.util.Date;

/**
 *
 * @author AlexsanderPimenta
 */
public class HistoricoIntegraERPBean {

    private int id;
    private TipoEntidade entidade;
    private String codEntidade;
    private Date dataEnt;
    private Date dataInteg;
    private TipoOperacao tipoOperacao;
    private StatusIntegracao status;
    private Object objectSinc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoEntidade getEntidade() {
        return entidade;
    }

    public void setEntidade(TipoEntidade entidade) {
        this.entidade = entidade;
    }

    public String getCodEntidade() {
        return codEntidade;
    }

    public void setCodEntidade(String codEntidade) {
        this.codEntidade = codEntidade;
    }

    public Date getDataEnt() {
        return dataEnt;
    }

    public void setDataEnt(Date dataEnt) {
        this.dataEnt = dataEnt;
    }

    public Date getDataInteg() {
        return dataInteg;
    }

    public void setDataInteg(Date dataInteg) {
        this.dataInteg = dataInteg;
    }

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public StatusIntegracao getStatus() {
        return status;
    }

    public void setStatus(StatusIntegracao status) {
        this.status = status;
    }

    public Object getObjectSinc() {
        return objectSinc;
    }

    public void setObjectSinc(Object objectSinc) {
        this.objectSinc = objectSinc;
    }

}
