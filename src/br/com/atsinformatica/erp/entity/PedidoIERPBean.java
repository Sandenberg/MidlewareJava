/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.atsinformatica.erp.entity;

/**
 *
 * @author kennedimalheiros
 */
public class PedidoIERPBean {
    private String codEmpresa;  
    private String codPedido;   
    private String codProdERP;  
    private String codGradERP;
    private double quantidade;
    private double precoUnit;
    private double totalItem;   //(Quantidade x Preço Unitário)
    private double peso;        // peso unitario
    private String unidadeSaida; 
    private String codClienteERP;
    private Double aliqIcm;
    private Double baseIcm;
    private Double aliqIpi;
    private String codTribut;
    private String uf;

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }


    public String getCodProdERP() {
        return codProdERP;
    }

    public void setCodProdERP(String codProdERP) {
        this.codProdERP = codProdERP;
    }

    public String getCodGradERP() {
        return codGradERP;
    }

    public void setCodGradERP(String codGradERP) {
        this.codGradERP = codGradERP;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnit() {
        return precoUnit;
    }

    public void setPrecoUnit(double precoUnit) {
        this.precoUnit = precoUnit;
    }

    public double getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(double totalItem) {
        this.totalItem = totalItem;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getUnidadeSaida() {
        return unidadeSaida;
    }

    public void setUnidadeSaida(String unidadeSaida) {
        this.unidadeSaida = unidadeSaida;
    }

    public String getCodClienteERP() {
        return codClienteERP;
    }

    public void setCodClienteERP(String codClienteERP) {
        this.codClienteERP = codClienteERP;
    }

    /**
     * @return the aliqIcm
     */
    public Double getAliqIcm() {
        return aliqIcm;
    }

    /**
     * @param aliqIcm the aliqIcm to set
     */
    public void setAliqIcm(Double aliqIcm) {
        this.aliqIcm = aliqIcm;
    }

    /**
     * @return the baseIcm
     */
    public Double getBaseIcm() {
        return baseIcm;
    }

    /**
     * @param baseIcm the baseIcm to set
     */
    public void setBaseIcm(Double baseIcm) {
        this.baseIcm = baseIcm;
    }

    /**
     * @return the aliqIpi
     */
    public Double getAliqIpi() {
        return aliqIpi;
    }

    /**
     * @param aliqIpi the aliqIpi to set
     */
    public void setAliqIpi(Double aliqIpi) {
        this.aliqIpi = aliqIpi;
    }

    /**
     * @return the codTribut
     */
    public String getCodTribut() {
        return codTribut;
    }

    /**
     * @param codTribut the codTribut to set
     */
    public void setCodTribut(String codTribut) {
        this.codTribut = codTribut;
    }

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @param uf the uf to set
     */
    public void setUf(String uf) {
        this.uf = uf;
    }
    
    
    
}
