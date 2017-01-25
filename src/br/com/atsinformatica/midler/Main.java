package br.com.atsinformatica.midler;

import br.com.atsinformatica.erp.controller.trigger.TriggerController;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.properties.PropertiesManager;
import br.com.atsinformatica.midler.ui.FrameLogin;
import br.com.atsinformatica.midler.ui.util.AtsKeyboardFocusManager;
import br.com.atsinformatica.midler.ui.util.LayoutUtil;
import br.com.atsinformatica.midler.ui.PanelPrincipal;
import br.com.atsinformatica.midler.ui.util.PasswordUtil;
import br.com.atsinformatica.utils.InstanceManager;
import com.jtattoo.plaf.aero.AeroLookAndFeel;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author AlexsanderPimenta
 */
public class Main {
    private static Socket s;
    
    public static final boolean MODE_DEVELOPER = false;
    public static final String VERSION = "2.08de";
    public static boolean MODE_CONFIG = false;
    
    public static void main(String args[]) {
        //instala navegação pelo teclado
        AtsKeyboardFocusManager.install();
        Properties props = new Properties();
        props.put("logoString", "");
        AeroLookAndFeel.setCurrentTheme(props);
        //configura look and feel
        LayoutUtil.configuraLookAndFeel(new AeroLookAndFeel());
        
        if (!InstanceManager.registerInstance()) {
            // Já existe uma instancia aberta.              
            JOptionPane.showMessageDialog(null, "Já existe outra instância aberta, portando não será possível abrir o modulo sincronizador.");
            System.exit(0);
        }
        
        if (!PropertiesManager.isFileExists()){
            MODE_CONFIG = true;
        } else {
            try {
                if (ConexaoATS.getConnection() == null) {
                    MODE_CONFIG = true;
                }
            } catch (Exception ex) {
                MODE_CONFIG = true;
            }
        }
        
        if (MODE_CONFIG) {
            JOptionPane.showMessageDialog(null, "Antes de utilizar o módulo sincronizador é necessário configurá-lo! Acesse a rotina de configurações\n"
                    + "e certifique-se de que todos os campos obrigatórios foram devidamente preenchidos.");            
            PanelPrincipal.getInstance().showFrame();
        } else {
            if (MODE_DEVELOPER) {
                FrameLogin.loginSuperUser();
            } else {
                FrameLogin frameLogin = new FrameLogin();
                frameLogin.setVisible(true);
                frameLogin.toFront();  
            }
            runTrigger();
        }
    }
    
    private static void runTrigger(){
        Properties config = PropertiesManager.getConfig();
        String oldVersion = config.getProperty("erp.version", "");
        String actVersion = PasswordUtil.encrypt(VERSION);
        
        if (!actVersion.equals(PasswordUtil.encrypt(oldVersion))) {
            Connection conn = null;
            try {
                conn = ConexaoATS.getConnection();
                conn.setAutoCommit(false);
            
                new TriggerController().createTriggers();
                conn.commit();
                
                config.setProperty("erp.version", PasswordUtil.encrypt(VERSION));
                PropertiesManager.setConfig(config);
            } catch (Exception ex) {
                try {
                    conn.rollback();
                } catch (SQLException ex1) {}       
            }
        }
    }
}
