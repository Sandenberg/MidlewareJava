/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

/**
 * Classe tradutora de erros 
 * @author AlexsanderPimenta
 */
public enum TradutorErro {
    
    ERRO_400("Erro de requisição inválida, verifique se todos dados foram preenchidos corretamente.", 400),
    ERRO_401("Erro de acesso não autorizado.", 401),
    ERRO_500("Erro interno do servidor.",500);
    
    
    
    //Descrição do erro
    private final String descricao;
    //Código do erro
    private final int codigo;

    private TradutorErro(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    /**
     * Retorna descrição do erro
     * @param codigo código do erro
     * @return String
     */
    public static String descricaoOf(int codigo) {
        for (TradutorErro type : TradutorErro.values()) {
            if (type.getCodigo() == codigo) {
                return type.getDescricao();
            }
        }
        return "";
    }
    
    
}

    
