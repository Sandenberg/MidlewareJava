/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.entity;

import br.com.atsinformatica.utils.InformacaoSistema;
import br.com.atsinformatica.utils.TipoInstalacao;
import br.com.atsinformatica.utils.TipoRetaguarda;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author AlexsanderPimenta
 */
public class SenhaSistema {
    
    private static final long serialVersionUID = 1L;
    private Map<String, InformacaoSistema> sistemas = new LinkedHashMap<String, InformacaoSistema>();
    private String codigoCliente;
    private String empresa;
    private Date dataValidade;
    private boolean compartilhado;
    private int quantidadeNotas;
    private int versao;
    private int versoesAdicionais;
    private String cpfCnpj;
    private String senha;
    private TipoRetaguarda retaguarda;
    private TipoInstalacao tipoInstalacao;

    /**
     * @return the codEmpresa
     */
    public String getEmpresa() {
        return empresa;
    }

    /**
     * @param codEmpresa the codEmpresa to set
     */
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the dataValidade
     */
    public Date getDataValidade() {
        return dataValidade;
    }

    /**
     * @param dataValidade the dataValidade to set
     */
    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    /**
     * @return the compartilhado
     */
    public boolean isCompartilhado() {
        return compartilhado;
    }

    /**
     * @param compartilhado the compartilhado to set
     */
    public void setCompartilhado(boolean compartilhado) {
        this.compartilhado = compartilhado;
    }

    /**
     * @return the quantidadeNotas
     */
    public int getQuantidadeNotas() {
        return quantidadeNotas;
    }

    /**
     * @param quantidadeNotas the quantidadeNotas to set
     */
    public void setQuantidadeNotas(int quantidadeNotas) {
        this.quantidadeNotas = quantidadeNotas;
    }

    /**
     * @return the versao
     */
    public int getVersao() {
        return versao;
    }

    /**
     * @param versao the versao to set
     */
    public void setVersao(int versao) {
        this.versao = versao;
    }

    /**
     * @return the versoesAdicionais
     */
    public int getVersoesAdicionais() {
        return versoesAdicionais;
    }

    /**
     * @param versoesAdicionais the versoesAdicionais to set
     */
    public void setVersoesAdicionais(int versoesAdicionais) {
        this.versoesAdicionais = versoesAdicionais;
    }

    /**
     * @return the cpfCnpj
     */
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * @param cpfCnpj the cpfCnpj to set
     */
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the codigoCliente
     */
    public String getCodigoCliente() {
        return codigoCliente;
    }

    /**
     * @param codigoCliente the codigoCliente to set
     */
    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    /**
     * @return the retaguarda
     */
    public TipoRetaguarda getRetaguarda() {
        return retaguarda;
    }

    /**
     * @param retaguarda the retaguarda to set
     */
    public void setRetaguarda(TipoRetaguarda retaguarda) {
        this.retaguarda = retaguarda;
    }

    /**
     * @return the tipoInstalacao
     */
    public TipoInstalacao getTipoInstalacao() {
        return tipoInstalacao;
    }

    /**
     * @param tipoInstalacao the tipoInstalacao to set
     */
    public void setTipoInstalacao(TipoInstalacao tipoInstalacao) {
        this.tipoInstalacao = tipoInstalacao;
    }

    /**
     * @return the sistemas
     */
    public Map<String, InformacaoSistema> getSistemas() {
        return sistemas;
    }

    /**
     * @param sistemas the sistemas to set
     */
    public void setSistemas(Map<String, InformacaoSistema> sistemas) {
        this.sistemas = sistemas;
    }

    

    
}
