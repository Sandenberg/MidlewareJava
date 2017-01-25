/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author AlexsanderPimenta
 */
public class InstanceManager {
     private static ServerSocket socket;  
    private static InstanceListener subListener;  
      
    /** Porta aleatória, porém statica e de numero alto */  
    private static int PORT = 44121;  
    /** Constante, que indica até que porta no máximo será permitida a conexão*/  
    private static final int MAX_PORT = 44140;  
    /**Endereço do LocalHost*/  
    private static InetAddress localHost;  
    /** Mensagem para notificar nova instancia. PRECISA terminar com \n */  
    private static final String MENSAGEM = "Existe instancia?\n";  
    /** Resposta enviada quando detectar nova instancia. PRECISA terminar com \n */  
    private static final String RESPOSTA = "Sim!\n";  
    /** Conectado como servidor?*/  
    private static boolean started;  
    /** Conectado como cliente?*/  
    private static boolean connected;  
    /** Abrir a aplicação em caso de erro na rede?*/  
    private final static boolean ERRO = false;  
  
    /**  
     * Tenta conectar ao socket como servidor, 
     * caso a porta já estiver aberta, tenta conectar como cliente. 
     *  
     * Essa operação é repetida até conseguir conectar como servidor 
     * ou receber uma resposta correta de outra instancia do servidor. 
     *  
     * O numero da porta é incrementado até atingir o numero inicial da porta + 20 
     * nessa caso, ele para de tentar fazer conexões, e retorna o valor em caso de erro. 
     *  
     * retorna true se conseguir abrir o servidor ou false caso contrário. 
     * retorna o valor de erro caso ocorra algum problema. 
     */  
    public static boolean registerInstance() {  
  
        try {  
            localHost = InetAddress.getByAddress("Localhost", new byte[]{127, 0, 0, 1});    
        } catch (UnknownHostException e) {  
            e.printStackTrace(System.err);  
            return ERRO;  
        }  
  
        while (!started && !connected) {  
            startServer();  
  
            if (started) {  
                return true;  
            }  
  
            startClient();  
  
            if (connected) {  
                return false;  
            }  
  
            if (PORT > MAX_PORT) {  
                System.err.println("Nenhuma porta disponível! (44121~44140)");  
               break;  
            }  
        }  
  
        return ERRO;  
  
  
    }  
  
    /** 
     * Abre um socket como servidor. 
     * Se conseguir, inicia uma Thread para receber conexões de novas instancias 
     */  
    private static void startServer() {  
        try {  
            System.err.println("Abrindo novo Servidor em "+PORT+"...");  
              
            socket = new ServerSocket(PORT, 20, localHost);  
              
            System.err.println("Conectado, escutando novas instâncias em: " + localHost + ":" + PORT);  
  
            new InstanceThread().start();  
            started = true;  
        } catch (IOException ex) {  
            //ex.printStackTrace(System.err);  
            started = false;  
        }  
  
    }  
  
    /** 
     * Tenta conectar a um servidor que já está aberto. 
     * Envia a MENSAGEM e espera pela resposta. 
     *  
     * Se a resposta for igual a RESPOSTA esperada,  
     * significa que outra instancia já está em execução, neste caso, apenas termine  
     * a execução da instancia atual. 
     *  
     * Se a resposta for diferente (ou null) significa que outra aplicação está em execução 
     * nesta porta. Então, incrementa a porta para tentar conectar como sevidor novamente. 
     */  
    private static void startClient() {  
        System.err.println("Porta já está aberta, notificando a primeira instância...");  
        try (Socket clientSocket = new Socket(localHost, PORT);  
                OutputStream out = clientSocket.getOutputStream();  
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {  
            out.write(MENSAGEM.getBytes());  
            String resposta = in.readLine();  
            System.err.println("Primeira instância notificada");  
            connected = !(resposta == null || !RESPOSTA.trim().equals(resposta.trim()));  
  
            if (connected) {  
                System.err.println("Resposta Correta: \"" + resposta + "\"\nAplicação encerrada.");  
            } else {  
                System.err.println("Resposta Incorreta: \"" + resposta + "\"");  
                PORT++;  
            }  
        } catch (IOException ex) {  
            ex.printStackTrace();  
            connected = false;  
        }  
    }  
  
    /** 
     * Seta um listener para receber as notificações de nova instância. 
     *  
     */  
    public static void setListener(InstanceListener listener) {  
        subListener = listener;  
    }  
  
    /** 
     * Notifica o listener que uma nova instancia foi detectada. 
     */  
    private static void fireNewInstance() {  
        if (subListener != null) {  
            subListener.newInstanceCreated();  
        }  
    }  
  
    /** 
     * Thread para aceitar conexões e receber mensagens pelo socket 
     *  
     * Ela deve ser iniciada quando a conexão do tipo servidor for aberta 
     * e ficará rodando enquando o programa estiver em execução. 
     *  
     * Quando receber uma nova conexão, primeiro verifica se a mensagem recebida 
     * corresponde com a MENSAGEM esperada. Caso seja igual, significa que é  
     * uma nova instancia da mesma aplicação, portanto, envia a RESPOSTA que o cliente 
     * está esperando para garantir que é a mesma aplição. Depois notifica o listener 
     * que uma nova instancia foi aberta. 
     *  
     * Caso a mensagem seja diferente, apenas fecha a conexão desconhecida. 
     */  
    static class InstanceThread extends Thread {  
  
        public InstanceThread() {  
            super("Socket Listener");  
             
            /** 
             * Se esta for a única Thread ainda em execução na aplicação, 
             * não faz sentido continuar. 
             *  
             * Por isso, colocando daemon = true, 
             * impede que a aplicação continue aberta quando esta for 
             * a ultima Thread em execução 
             * (o que iria causar o programa a rodar eternamente) 
             */  
            setDaemon(true);  
        }  
  
        @Override  
        public void run() {  
            while (!socket.isClosed()) {  
  
                try (Socket client = socket.accept();  
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));  
                        OutputStream out = client.getOutputStream()) {  
                    String message = in.readLine();  
                    if (MENSAGEM.trim().equals(message.trim())) {  
                        System.err.println("Nova instância do programa detectada: \"" + message + "\"\nResposta enviada.");  
                        out.write(RESPOSTA.getBytes());  
                        fireNewInstance();  
                    } else {  
                        System.err.println("Conexão desconhecida detectada: \"" + message + "\"");  
                        out.write("Rain, rain, go away...".getBytes());  
                    }  
                } catch (IOException ex) {  
                    ex.printStackTrace(System.err);  
                }  
            }  
        }  
    }  
}  

    

