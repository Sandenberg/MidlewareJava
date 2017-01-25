package br.com.atsinformatica.midler.jdbc;

import br.com.atsinformatica.midler.properties.PropertiesManager;
import br.com.atsinformatica.midler.ui.util.PasswordUtil;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public class ConexaoATS {

    private final static Logger logger = Logger.getLogger(ConexaoATS.class);
    private final static Pattern EXCEPTION_GDS = Pattern.compile("^GDS Exception.\\s([0-9]+)(.+)?");

    private static Properties mapErros;
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null) {
                ConexaoATS.fechaConexao(); // Fecha a conexão se existe
                connection = ConexaoATS.criaNovaConexao();
            } else if (connection.isClosed()){
                throw new UnsupportedOperationException("A conexão foi fechada.");
            }
        } catch (Exception e) {
            logger.error("Falha ao recuperar conexão", e);
        }
        try {
            // Por padrão usa o auto commit false
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("Falha ao recuperar conexão", e);
        }
        
        return connection;
    }

    public static Connection criaNovaConexao() {
        String diretorio = PasswordUtil.decrypt(PropertiesManager.getConfig().getProperty("erp.diretorio"));
        String usuario = PasswordUtil.decrypt(PropertiesManager.getConfig().getProperty("erp.usuario"));
        String senha = PasswordUtil.decrypt(PropertiesManager.getConfig().getProperty("erp.senha"));
        
        return ConexaoATS.criaNovaConexao(diretorio, usuario, senha);
    }

    /**
     * Esse método deve ser evitado, atualmente ele é usado somente na tela de
     * configuração
     */
    public static Connection criaNovaConexao(String diretorio, String usuario, String senha) {
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            logger.info("Abrindo nova conexão...");            
            return DriverManager.getConnection("jdbc:firebirdsql:" + diretorio + "?encoding=ISO8859_1", usuario, senha);
        } catch (Exception e) {
            logger.error("Erro ao estabelecer conexão.", e);
            throw new RuntimeException(traduzErro(e));
        }
    }

    public static void fechaConexao() {
        try {
            logger.info("Fechando a conexão.");
            connection.close();
            connection = null;
            logger.info("Conexão fechada com sucesso.");
        } catch (Exception e) {
            logger.debug("Erro ao fechar conexão.", e);
        }
    }

    private static String traduzErro(Throwable t) {
        Matcher matEx = EXCEPTION_GDS.matcher(t.getMessage());
        if (matEx.find()) {
            String codigoErro = String.valueOf(matEx.group(1));
            String mensagem = getMapErros().getProperty(codigoErro);
            if (mensagem != null && !mensagem.isEmpty()) {
                return mensagem;
            }
        }

        // Processar outros tipos de erro
        
        return "Erro desconhecido ao tentar estabelecer conexão. Favor, contactar o suporte.";
    }

    private static Properties getMapErros() {
        if (mapErros == null) {
            mapErros = new Properties();
            try {
                URL url = ConexaoATS.class.getResource("traducaoErro.properties"); 
                InputStream stream = url.openStream(); 
                mapErros.load(stream);
            } catch (Exception e) {
                logger.error("Não foi possível ler arquivo de traduções, a aplicação não irá traduzir mensagens.", e);
            }
        }
        return mapErros;
    }
}
