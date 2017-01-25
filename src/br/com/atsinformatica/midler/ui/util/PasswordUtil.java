package br.com.atsinformatica.midler.ui.util;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author niwrodrigues
 */
public class PasswordUtil {
    private static final String SEMENTE_SENHA = "senha001";
    private static BasicTextEncryptor encryptor;

    private static BasicTextEncryptor getEncryptor() {
        if (encryptor == null) {
            encryptor = new BasicTextEncryptor();
            encryptor.setPassword(SEMENTE_SENHA);
        }
        return encryptor;
    }
    
    public static String decrypt(String password){
        return getEncryptor().decrypt(password);
    }
    
    public static String encrypt(String password){
        return getEncryptor().encrypt(password);
    }
}
