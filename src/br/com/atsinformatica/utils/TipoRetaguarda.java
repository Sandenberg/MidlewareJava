/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

/**
 *
 * @author AlexsanderPimenta
 */
public enum TipoRetaguarda {

    WINDOWS("WIN"),
    JAVA("JAV"),
    DOS("DOS"),
    RETNULO("");
    private final String sigla;

    private TipoRetaguarda(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }

    public static final TipoRetaguarda porSigla(String sigla) {
        // TODO: Otimizar isto usando um Map
        if (sigla == null) {
            return RETNULO;
        }

        for (TipoRetaguarda tipo : TipoRetaguarda.values()) {
            if (tipo.getSigla().equals(sigla)) {
                return tipo;
            }
        }

        return RETNULO;
    }
}
