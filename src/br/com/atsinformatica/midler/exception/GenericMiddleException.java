package br.com.atsinformatica.midler.exception;

/**
 * @author niwrodrigues
 */
public class GenericMiddleException extends RuntimeException {

    public GenericMiddleException() {
    }

    public GenericMiddleException(String message) {
        super(message);
    }

    public GenericMiddleException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
