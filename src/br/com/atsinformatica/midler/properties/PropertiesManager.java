/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;


/**
 *
 * Classe responsável por realizar a gerência do arquivo de configurações
 *
 * @author AlexsanderPimenta
 */
public class PropertiesManager {
    private static Logger logger = Logger.getLogger(PropertiesManager.class);

    private static Properties config;

    private static File getFile(){
        return new File("config.ini");
    }

    public static boolean isFileExists(){
        return getFile().exists();
    }
    
    public static void setConfig(Properties properties){
        if (properties != null) {
            FileOutputStream stream = null;
            try {
                File file = getFile();
                if (file.exists()) {
                    file.createNewFile();
                }
                
                stream = new FileOutputStream(file);
                properties.store(stream, "Arquivo de configurações do Midler");
                
                config = properties;
            } catch (Exception e) {
                throw new RuntimeException("Falha ao gravar configurações.");
            } finally {
                try {
                    stream.close();
                } catch (Exception e2) {}
            }
        }
    }
    
    /**
     * Retorna arquivo de configuração
     *
     * @return arquivo de configuração
     */
    public static Properties getConfig() {
        if (config == null) {
            config = new Properties();
            InputStream stream = null;
            try {
                File file = getFile();
                if (file.exists()) {
                    stream = new FileInputStream(file);
                    config.load(stream);
                }
            } catch (Exception e) {
                throw new RuntimeException("Falha ao ler configurações.");
            } finally {
                try {
                    stream.close();
                } catch (Exception e2) {}
            }
        }
        return config;
    }

}
