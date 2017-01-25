/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

/**
 *
 * @author AlexsanderPimenta
 */
public enum TipoInstalacao {

    COMPRADO("COM"),
    LOCADO("LOC"),
    DEMONSTRACAO("DEM"),
    INSNULO("");
    private final String sigla;

    private TipoInstalacao(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }

    public static final TipoInstalacao porSigla(String sigla) {
        if (sigla == null) {
            return INSNULO;
        }

        for (TipoInstalacao tipo : TipoInstalacao.values()) {
            if (tipo.getSigla().equals(sigla)) {
                return tipo;
            }
        }

        return INSNULO;
    }
}
