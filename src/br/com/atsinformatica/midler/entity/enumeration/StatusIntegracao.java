package br.com.atsinformatica.midler.entity.enumeration;

import br.com.atsinformatica.midler.entity.EnumDescritivel;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public enum StatusIntegracao implements EnumDescritivel{
    PENDENTE("Aguardando integração"),
    SINCRONIZADO("Integrado com sucesso"),
    ERRO("Erro durante a integração"),
    EM_ANDAMENTO("Em andamento");
    
    private static final Logger logger = Logger.getLogger(StatusIntegracao.class);
    private final String descricao;

    private StatusIntegracao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String getDescricao() {
        return descricao;
    }
    
    public static StatusIntegracao converteValor(String value){
        try {
            return value != null ? StatusIntegracao.valueOf(value.toUpperCase()) : null;
        } catch (Exception e) {
            // Não deve propagar a Exception
            logger.error("Falha ao converter StatusIntegracao:" + value);
            return null;
        }
    }
}