package br.com.atsinformatica.midler.entity.enumeration;

import br.com.atsinformatica.midler.entity.EnumDescritivel;

/**
 *
 * @author niwrodrigues
 */
public enum TipoPeriodo implements EnumDescritivel{
    ENTRADA("Data de entrada"),
    INTEGRACAO("Data da integração");
    
    private final String descricao;

    private TipoPeriodo(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String getDescricao() {
        return descricao;
    }
}