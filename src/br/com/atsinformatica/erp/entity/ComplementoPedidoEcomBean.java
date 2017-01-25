/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import com.towel.el.annotation.Resolvable;
import java.util.Date;

/**
 *
 * @author AlexsanderPimenta
 */
public class ComplementoPedidoEcomBean {
    
    @Resolvable(colName = "Referência Pedido")
    private String codReferencia;
    @Resolvable(colName = "Cód. Pedido ERP")
    private String codErp;
    @Resolvable(colName = "Data do Pagamento")
    private Date dataPagamento;
    @Resolvable(colName = "Forma de Pagamento")
    private int formaPagamento;
    @Resolvable(colName = "ID Transação")
    private String codTransacao;
    @Resolvable(colName = "Valor Pago")
    private Double valorPago;
    @Resolvable(colName = "Id Pedido Ecom")
    private int idPedidoEcom;
    private String codBanco;
    private String agencia;
    private String conta;
    private String codPrazo;
    private String codConvenio;
    private String codEmpresa;
    private String tipoPedido;
    private int qtdeParcelaEcom;
    private String pagoNaLoja;

    /**
     * @return the codReferencia
     */
    public String getCodReferencia() {
        return codReferencia;
    }

    /**
     * @param codReferencia the codReferencia to set
     */
    public void setCodReferencia(String codReferencia) {
        this.codReferencia = codReferencia;
    }

    /**
     * @return the codErp
     */
    public String getCodErp() {
        return codErp;
    }

    /**
     * @param codErp the codErp to set
     */
    public void setCodErp(String codErp) {
        this.codErp = codErp;
    }

    /**
     * @return the dataPagamento
     */
    public Date getDataPagamento() {
        return dataPagamento;
    }

    /**
     * @param dataPagamento the dataPagamento to set
     */
    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    /**
     * @return the formaPagamento
     */
    public int getFormaPagamento() {
        return formaPagamento;
    }

    /**
     * @param formaPagamento the formaPagamento to set
     */
    public void setFormaPagamento(int formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    /**
     * @return the codTransacao
     */
    public String getCodTransacao() {
        return codTransacao;
    }

    /**
     * @param codTransacao the codTransacao to set
     */
    public void setCodTransacao(String codTransacao) {
        this.codTransacao = codTransacao;
    }

    /**
     * @return the valorPago
     */
    public Double getValorPago() {
        return valorPago;
    }

    /**
     * @param valorPago the valorPago to set
     */
    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    /**
     * @return the idPedidoEcom
     */
    public int getIdPedidoEcom() {
        return idPedidoEcom;
    }

    /**
     * @param idPedidoEcom the idPedidoEcom to set
     */
    public void setIdPedidoEcom(int idPedidoEcom) {
        this.idPedidoEcom = idPedidoEcom;
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
     * @return the agencia
     */
    public String getAgencia() {
        return agencia;
    }

    /**
     * @param agencia the agencia to set
     */
    public void setAgencia(String agencia) {
        this.agencia = agencia;
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
     * @return the codConvenio
     */
    public String getCodConvenio() {
        return codConvenio;
    }

    /**
     * @param codConvenio the codConvenio to set
     */
    public void setCodConvenio(String codConvenio) {
        this.codConvenio = codConvenio;
    }

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
     * @return the tipoPedido
     */
    public String getTipoPedido() {
        return tipoPedido;
    }

    /**
     * @param tipoPedido the tipoPedido to set
     */
    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    /**
     * @return the qtdeParcelaEcom
     */
    public int getQtdeParcelaEcom() {
        return qtdeParcelaEcom;
    }

    /**
     * @param qtdeParcelaEcom the qtdeParcelaEcom to set
     */
    public void setQtdeParcelaEcom(int qtdeParcelaEcom) {
        this.qtdeParcelaEcom = qtdeParcelaEcom;
    }

    /**
     * @return the pagoNaLoja
     */
    public String getPagoNaLoja() {
        return pagoNaLoja;
    }

    /**
     * @param pagoNaLoja the pagoNaLoja to set
     */
    public void setPagoNaLoja(String pagoNaLoja) {
        this.pagoNaLoja = pagoNaLoja;
    }
    
    
}
