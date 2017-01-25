package br.com.atsinformatica.midler.ui.util;

import br.com.atsinformatica.utils.Function;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author João Sizílio
 */
public class InternetUtil {
    
    private static InternetUtil instance = null;
    private static boolean conectado = false;
    private static Function<Boolean> callback;
    
    private static InternetUtil getInstance() {
        if (instance == null) {
            instance = new InternetUtil();
            instance.configuraVerificacaoInternet();
        }
        return instance;
    }
    
    public static void iniciaVerificacao() {
        iniciaVerificacao(null);
    }
    
    public static void iniciaVerificacao(Function<Boolean> callback) {
        InternetUtil.callback = callback;
        getInstance(); // Força o inicio das verificações
    }
    
    private void configuraVerificacaoInternet() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {                    
                conectado = verificaConexaoInternet();
                if (callback != null) {
                    callback.run(conectado);
                }
                
                Thread.currentThread().setName("Teste de conexão");
            }
        }, 0, 1000);
    }
    
    /**
     * Método responsável por verificar se há conexão com a internet.
     * @return 
     */
    public boolean verificaConexaoInternet() {
        try {
            InetAddress.getByName("www.google.com");        
            return true;
        } catch (UnknownHostException ex) {
            return false;
        }
        
    }

    public static boolean isConectado() {
        return conectado;
    }
    
}
