package br.com.atsinformatica.midler.entity.enumeration;

import br.com.atsinformatica.midler.entity.EnumDescritivel;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public enum TipoOperacao implements EnumDescritivel{
    INSERT("Inclusão"),
    UPDATE("Alteração"),
    DELETE("Exclusão");
    
    private static final Logger logger = Logger.getLogger(TipoOperacao.class);
    private final String descricao;

    private TipoOperacao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String getDescricao() {
        return descricao;
    }
    
    public static TipoOperacao converteValor(String value){
        try {
            return value != null ? TipoOperacao.valueOf(value.toUpperCase()) : null;
        } catch (Exception e) {
            // Não deve propagar a Exception
            logger.error("Falha ao converter TipoOperacao:" + value);
            return null;
        }
    }
}