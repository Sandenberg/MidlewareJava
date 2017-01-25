/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

/**
 *
 * @author AlexsanderPimenta
 */
public enum FormaPagamentoEcom {
    PAGSEGURO("PagSeguro",1),
    PAYPAL("PayPal",2),
    CIELO("Cielo",3),
    TRANSFERENCIABANCARIA("Transferência bancária",3),
    PAGAMENTONAENTREGA("Pagamento na Entrega (PNA)",4);
    
    private final String nomePagamento;
    private int indicePagamento;
    
    private FormaPagamentoEcom(String nomePagamento, int indicePagamento) {
        this.nomePagamento = nomePagamento;
        this.indicePagamento = indicePagamento;
    }

    /**
     * @return the nomePagamento
     */
    public String getNomePagamento() {
        return nomePagamento;
    }

    /**
     * @return the indicePagamento
     */
    public int getIndicePagamento() {
        return indicePagamento;
    }
    
    /**
     * Retorna indice da forma de pagamento
     * @param nomePagamento Nome do pagamento
     * @return int índice do pagamento
     */
    public static int indiceOf(String nomePagamento) {
        for (FormaPagamentoEcom type : FormaPagamentoEcom.values()) {
            if (type.getNomePagamento().equals(nomePagamento)) {
                return type.getIndicePagamento();
            }
        }
        return 0;
    }
    
    /**
     * Retorna indice da forma de pagamento
     * @param nomePagamento Nome do pagamento
     * @return int índice do pagamento
     */
    public static String nomeOf(int indice) {
        for (FormaPagamentoEcom type : FormaPagamentoEcom.values()) {
            if (type.getIndicePagamento() == indice) {
                return type.getNomePagamento();
            }
        }
        return "";
    }
   
    
    
}
