package br.com.atsinformatica.midler.entity.filter;

import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.entity.enumeration.TipoOperacao;
import br.com.atsinformatica.midler.entity.enumeration.TipoPeriodo;
import java.util.Date;

/**
 *
 * @author niwrodrigues
 */
public class HistoricoFilter {

    private TipoEntidade entidade;
    private TipoOperacao tipoOperacao;
    private TipoPeriodo tipoPeriodo;
    private StatusIntegracao status;
    private Date dataInicio;
    private Date dataFinal;

    public TipoEntidade getEntidade() {
        return entidade;
    }

    public void setEntidade(TipoEntidade entidade) {
        this.entidade = entidade;
    }

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public TipoPeriodo getTipoPeriodo() {
        return tipoPeriodo;
    }

    public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    public StatusIntegracao getStatus() {
        return status;
    }

    public void setStatus(StatusIntegracao status) {
        this.status = status;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

}
