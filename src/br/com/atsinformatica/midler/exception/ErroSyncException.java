package br.com.atsinformatica.midler.exception;

/**
 * Exception usada para interceptar erros customizaveis no SincERPController
 * @author niwrodrigues
 */
public class ErroSyncException extends RuntimeException{
 
    private final Integer status;

    public ErroSyncException(int status) {
        this(status, null);
    }

    public ErroSyncException(int status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public ErroSyncException(String message) {
        super(message);
        this.status = null;
    }

    public int getStatus() {
        return status;
    }
}
