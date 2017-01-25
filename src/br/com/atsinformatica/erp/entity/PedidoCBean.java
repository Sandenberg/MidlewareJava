/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author AlexsanderPimenta
 */
public class PedidoCBean {
    
    private String codPedido;
    private String codEmpresa;
    private String faturado;
    private String tipoPedido;
    private Integer idPedidoEcom;
    private Double descontoVLR;
    private Date datPedido;
    private Date dtFinalizaPedidoEcom;
    private Double totalPedido;
    private StatusPedido statusPedidoEcom;
    private String codPedidoEcom;
    private Double frete;
    private String codVendedor;
    private String observacao;
    private String codOper;
    private String codPedidoVenda;
    private String codPedidoOriginal;
    private String codcliente;
    private List<PedidoIERPBean> itensPedido;
    private String codTVenda;
    

    public PedidoCBean(ResultSet rs) throws SQLException {
        if(rs!=null){
            this.codPedido = rs.getString("codpedido");
            this.codEmpresa = rs.getString("codempresa");
            this.codcliente = rs.getString("codcliente");
            this.faturado = rs.getString("faturado");
            this.tipoPedido = rs.getString("tipopedido");
            this.idPedidoEcom = rs.getInt("idpedidoecom");
            this.descontoVLR = rs.getDouble("descontovlr");
            this.datPedido = rs.getDate("datapedido");
            this.dtFinalizaPedidoEcom = rs.getDate("datafinalizapedidoecom");
            this.totalPedido = rs.getDouble("totalpedido");
            this.statusPedidoEcom = StatusPedido.converteValor(rs.getString("STATUSPEDIDOECOM"));
            this.codPedidoEcom = rs.getString("codpedidoecom");
            this.frete = rs.getDouble("frete");
            this.codVendedor = rs.getString("codvendedor");
            this.observacao = rs.getString("observacao");
            this.codOper    = rs.getString("codOper");
            this.codPedidoVenda = rs.getString("codpedidovenda");
            this.codPedidoOriginal = rs.getString("codpedidooriginal");
            this.totalPedido = rs.getDouble("TOTALPEDIDO");
        }
        
    }
    
    public PedidoCBean(){
        super();
    }
    
    /**
     * @return the codPedido
     */
    public String getCodPedido() {
        return codPedido;
    }

    /**
     * @param codPedido the codPedido to set
     */
    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
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
     * @return the faturado
     */
    public String getFaturado() {
        return faturado;
    }

    /**
     * @param faturado the faturado to set
     */
    public void setFaturado(String faturado) {
        this.faturado = faturado;
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
     * @return the idPedidoEcom
     */
    public Integer getIdPedidoEcom() {
        return idPedidoEcom;
    }

    /**
     * @param idPedidoEcom the idPedidoEcom to set
     */
    public void setIdPedidoEcom(int idPedidoEcom) {
        this.idPedidoEcom = idPedidoEcom;
    }

    /**
     * @return the descontoVLR
     */
    public Double getDescontoVLR() {
        return descontoVLR;
    }

    /**
     * @param descontoVLR the descontoVLR to set
     */
    public void setDescontoVLR(Double descontoVLR) {
        this.descontoVLR = descontoVLR;
    }

    /**
     * @return the datPedido
     */
    public Date getDatPedido() {
        return datPedido;
    }

    /**
     * @param datPedido the datPedido to set
     */
    public void setDatPedido(Date datPedido) {
        this.datPedido = datPedido;
    }

    /**
     * @return the dtFinalizaPedidoEcom
     */
    public Date getDtFinalizaPedidoEcom() {
        return dtFinalizaPedidoEcom;
    }

    /**
     * @param dtFinalizaPedidoEcom the dtFinalizaPedidoEcom to set
     */
    public void setDtFinalizaPedidoEcom(Date dtFinalizaPedidoEcom) {
        this.dtFinalizaPedidoEcom = dtFinalizaPedidoEcom;
    }

    /**
     * @return the totalPedido
     */
    public Double getTotalPedido() {
        return totalPedido;
    }

    /**
     * @param totalPedido the totalPedido to set
     */
    public void setTotalPedido(Double totalPedido) {
        this.totalPedido = totalPedido;
    }

    /**
     * @return the statusPedidoEcom
     */
    public StatusPedido getStatusPedidoEcom() {
        return statusPedidoEcom;
    }

    /**
     * @param statusPedidoEcom the statusPedidoEcom to set
     */
    public void setStatusPedidoEcom(StatusPedido statusPedidoEcom) {
        this.statusPedidoEcom = statusPedidoEcom;
    }

    /**
     * @return the codPedidoEcom
     */
    public String getCodPedidoEcom() {
        return codPedidoEcom;
    }

    /**
     * @param codPedidoEcom the codPedidoEcom to set
     */
    public void setCodPedidoEcom(String codPedidoEcom) {
        this.codPedidoEcom = codPedidoEcom;
    }

    /**
     * @return the frete
     */
    public Double getFrete() {
        return frete;
    }

    /**
     * @param frete the frete to set
     */
    public void setFrete(Double frete) {
        this.frete = frete;
    }

    /**
     * @return the codVendedor
     */
    public String getCodVendedor() {
        return codVendedor;
    }

    /**
     * @param codVendedor the codVendedor to set
     */
    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return the codOper
     */
    public String getCodOper() {
        return codOper;
    }

    /**
     * @param codOper the codOper to set
     */
    public void setCodOper(String codOper) {
        this.codOper = codOper;
    }

    /**
     * @return the codPedidoVenda
     */
    public String getCodPedidoVenda() {
        return codPedidoVenda;
    }

    /**
     * @param codPedidoVenda the codPedidoVenda to set
     */
    public void setCodPedidoVenda(String codPedidoVenda) {
        this.codPedidoVenda = codPedidoVenda;
    }

    /**
     * @return the codPedidoOriginal
     */
    public String getCodPedidoOriginal() {
        return codPedidoOriginal;
    }

    /**
     * @param codPedidoOriginal the codPedidoOriginal to set
     */
    public void setCodPedidoOriginal(String codPedidoOriginal) {
        this.codPedidoOriginal = codPedidoOriginal;
    }

    /**
     * @return the codcliente
     */
    public String getCodcliente() {
        return codcliente;
    }

    /**
     * @param codcliente the codcliente to set
     */
    public void setCodcliente(String codcliente) {
        this.codcliente = codcliente;
    }

    /**
     * @return the itensPedido
     */
    public List<PedidoIERPBean> getItensPedido() {
        return itensPedido;
    }

    /**
     * @param itensPedido the itensPedido to set
     */
    public void setItensPedido(List<PedidoIERPBean> itensPedido) {
        this.itensPedido = itensPedido;
    }

    /**
     * @return the codTVenda
     */
    public String getCodTVenda() {
        return codTVenda;
    }

    /**
     * @param codTVenda the codTVenda to set
     */
    public void setCodTVenda(String codTVenda) {
        this.codTVenda = codTVenda;
    }
    
}
