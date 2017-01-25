/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

/**
 *
 * @author AlexsanderPimenta
 */
public class InformacaoSistema {

    private String sigla;
    private TipoSistema tipoSistema;
    private int quantidadeLinks;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public TipoSistema getTipoSistema() {
        return tipoSistema;
    }

    public void setTipoSistema(TipoSistema tipoSistema) {
        this.tipoSistema = tipoSistema;
    }

    public int getQuantidadeLinks() {
        return quantidadeLinks;
    }

    public void setQuantidadeLinks(int quantidadeLinks) {
        this.quantidadeLinks = quantidadeLinks;
    }
}
