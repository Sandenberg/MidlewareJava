package br.com.atsinformatica.midler.entity.enumeration;

import br.com.atsinformatica.midler.entity.EnumDescritivel;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public enum TipoEntidade implements EnumDescritivel{
    PEDIDO("Pedido"),
    PRODUTO("Produto"),
    CATEGORIA("Categoria"),
    ATRIBUTOGRADE("Atributo de grade"),
    FABRICANTE("Fabricante"),
    TRANSPORTADORA("Transportadora"),
    GRADE("Grade"),
    SUBGRADE("Sub grade"),
    COMPPROD("Movimentação de estoque ou preço"),
    STATUSPEDIDOECOM("Status do pedido"),
    PRODAGREGADO("Produto agregado"),
    PEDIDOTROCA("Troca do pedido"),
    STATUSCONTROLEENTREGA("Status do controle de entrega"),
    STATUSNFSAIDECOM("Status NF saida");
    
    private static final Logger logger = Logger.getLogger(TipoEntidade.class);
    private final String descricao;

    private TipoEntidade(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String getDescricao() {
        return descricao;
    }
    
    public static TipoEntidade converteValor(String value){
        try {
            return value != null ? TipoEntidade.valueOf(value.toUpperCase()) : null;
        } catch (Exception e) {
            // Não deve propagar a Exception
            logger.error("Falha ao converter TipoEntidade:" + value);
            return null;
        }
    }
}
