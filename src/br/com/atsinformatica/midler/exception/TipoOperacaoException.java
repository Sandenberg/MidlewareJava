package br.com.atsinformatica.midler.exception;

/**
 *
 * @author niwrodrigues
 */
public class TipoOperacaoException extends RuntimeException {

    public TipoOperacaoException() {
    }

    public TipoOperacaoException(String message) {
        super(message);
    }

    public TipoOperacaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TipoOperacaoException(Throwable cause) {
        super(cause);
    }

    public TipoOperacaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
