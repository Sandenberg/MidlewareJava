/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AlexsanderPimenta
 */
public enum TipoSistema {

    LITE("LIT"),
    FATURAMENTO("FAT"),
    RECEBER("REC"),
    PAGAR("PAG"),
    BANCOS("BAN"),
    CAIXA("CXA"),
    CONTABILIDADE("CTB"),
    MATERIAIS("MAT"),
    COMPRAS("COM"),
    LIVROS("LIV"),
    POSTO("PST"),
    ECF("ECF"),
    OS("ORS"),
    CALLCENTER("SCC"),
    PDVWEB("VWB"),
    VENDAS("VD2"),
    PEDIDO("PED"),
    MALADIRETA("MLD"),
    PALM("PLM"),
    HOTEL("HTL"),
    CUSTOS("CTO"),
    FOLHA("FLH"),
    IMOVEIS("IMV"),
    LOCACAO("LOC"),
    PATRIMONIO("PAT"),
    SERPOL("SPL"),
    TELEMARK("TLM"),
    VEICULOS("VEI"),
    SINTEGRA("STG"),
    MINILITE("MLT"),
    LITECONTROLPRINT("CPL"),
    NFE("NFE"),
    SPE("SPD"),
    ENTREGAS("ETG"),
    INTEGRACAO("ITD"),
    CONCILIACAO_CARTAO_CREDITO("CCT"),
    // Java
    JAVA_ERP("JER"),
    JAVA_BUSINESS("JBU"),
    JAVA_LITE("JLT", JAVA_BUSINESS),
    JAVA_START("JST"),
    JAVA_TELEFONIA("JTF"),
    JAVA_PAINEL_TELEFONIA("PTF"),
    JAVA_OS("JOS"),
    JAVA_CONTABILIDADE("JCB"),
    JAVA_REPRESENTACAO("JLR");
    private final String sigla;
    private TipoSistema sinonimo;

    private TipoSistema(String sigla) {
        this(sigla, null);
    }

    private TipoSistema(String sigla, TipoSistema sinonimo) {
        this.sigla = sigla;
        this.sinonimo = sinonimo;

        // Se esse sistema é o sinónimo de outro, e o outro não tem sinônimo, este fica sento o sinônimo do outro. 
        if (sinonimo != null && sinonimo.sinonimo == null) {
            sinonimo.sinonimo = this;
        }
    }

    public String getSigla() {
        return sigla;
    }

    public TipoSistema getSinonimo() {
        return sinonimo;
    }
    private static Map<String, TipoSistema> porSigla = null;

    public static final TipoSistema porSigla(String sigla) {
        if (porSigla == null) {
            synchronized (TipoSistema.class) {
                HashMap<String, TipoSistema> mapa = new HashMap<String, TipoSistema>();
                for (TipoSistema tipo : TipoSistema.values()) {
                    mapa.put(tipo.getSigla(), tipo);
                }
                porSigla = mapa;
            }
        }
        return porSigla.get(sigla);
    }
}
