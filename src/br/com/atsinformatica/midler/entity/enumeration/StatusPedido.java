package br.com.atsinformatica.midler.entity.enumeration;

import br.com.atsinformatica.midler.entity.EnumDescritivel;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public enum StatusPedido implements EnumDescritivel{
    AGUARDANDO_PAGAMENTO("Aguardando pagamento"), 
    PAGAMENTO_ACEITO("Pagamento aceito"), 
    PAGAMENTO_RECUSADO("Pagamento recusado"),
    SEPARACAO_ESTOQUE("Separação de estoque"), 
    ENVIADO("Pedido enviado"), 
    ENTREGUE("Pedido entregue"), 
    CANCELADO("Pedido cancelado"), 
    ESTORNADO("Pagamento estornado"), 
    DEVOLVIDO("Pedido devolvido"), 
    TROCADO("Pedido trocado"),
    NF_EMITIDA("Nota Fiscal emitida"),
    NONE("Status desconhecido");
    
    private static final Logger logger = Logger.getLogger(StatusPedido.class);
    private final String descricao;

    private StatusPedido(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String getDescricao() {
        return descricao;
    }
    
    public static StatusPedido thisValueOf(String value){
        return StatusPedido.valueOf(value.toUpperCase().trim());
    }
        
    public static StatusPedido converteValor(String value){
        try {
            return value != null ? thisValueOf(value) : null;
        } catch (Exception e) {
            // Não deve propagar a Exception
            logger.error("Falha ao converter TipoEntidade:" + value);
            return null;
        }
    }
}