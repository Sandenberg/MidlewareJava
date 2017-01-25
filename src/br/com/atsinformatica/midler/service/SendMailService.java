package br.com.atsinformatica.midler.service;

import br.com.atsinformatica.erp.dao.MappedClassesDAO;
import br.com.atsinformatica.erp.entity.ParaUrlWsdlBean;
import br.com.atsinformatica.midler.Main;
import br.com.atsinformatica.utils.DateUtil;
import com.towel.collections.CollectionsUtil;
import com.towel.collections.filter.Filter;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public class SendMailService {
    private static final Logger logger = Logger.getLogger(SendMailService.class);
    
    /** Constantes */
    private final int MAX_FILES = 24;
    private final String MAIL_USER = "sgchd@atsinformatica.com.br";
    private final String MAIL_PASS = "atsbh1234";
    private final String MAIL_SMTP = "smtp.atsinformatica.com.br";
    private final String MAIL_PORT = "25";
    
    private final Pattern LOG_FILE = Pattern
            .compile("middle.log.(\\d{8})-(\\d{2})(A|E).log");
    private final Pattern LOG_FILE_SEND = Pattern
            .compile("middle.log.(\\d{4})(\\d{2})(\\d{2})-(\\d{2})A.log");
    
    private static SendMailService instance;
    
    private Timer timer;
    private boolean inProcess;
    private boolean inStandby;
            
    private static SendMailService getInstance() {
        if (instance == null) {
            instance = new SendMailService();
        }
        return instance;
    }
    
    /**
     * Inicia timer se necessário
     */
    public static void initialize() {
        SendMailService.getInstance().instanceInitialize();
    }
    private void instanceInitialize() {
        if (timer == null) {
            long period = TimeUnit.HOURS.toMillis(1);
            long delay = TimeUnit.SECONDS.toMillis(10);
            
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SendMailService.this.execute();
                }
            }, delay, period);
        }
    }
    
    private void execute() {
        String name = "Enviando logs por e-mail";
        Thread.currentThread().setName(name + " - Em andamento desde " + DateUtil.dataHoraParaString(new Date()));
                
        if(SendMailService.this.inProcess){
            SendMailService.this.inStandby = true;
            return;
        }
        
        SendMailService.this.inProcess = true;
        SendMailService.this.inStandby = true;
        
        try {
            while (SendMailService.this.inStandby) {
                SendMailService.this.inStandby = false;
                this.prepareSend();
            }
        } catch (Exception e){
            logger.error("Falha ao enviar e-mail", e);
        } finally {
            SendMailService.this.inProcess = false;
            SendMailService.this.inStandby = false;
            Thread.currentThread().setName(name + " - Paralizada desde " + DateUtil.dataHoraParaString(new Date()));
        }
    }
    
    private void prepareSend(){
        File path = getPathLogs();
        if (path == null || !path.isDirectory()) {
            throw new RuntimeException("Não foi encontrado a pasta de logs.");
        }

        /* Recupera arquivos da pasta */
        List<File> array = Arrays.asList(path.listFiles());
        if (array == null) {
            return;
        }

        /* Remove arquivos que não são necessários */
        List<File> files = CollectionsUtil.filter(array, new Filter<File>() {
                @Override
                public boolean accept(File file) {
                    return LOG_FILE.matcher(file.getName()).find();
                }
            });

        /* Ordena em order decrescente de data */
        Collections.sort(files, new Comparator<File>(){
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName()) * -1;
                }
            });

        if (!Main.MODE_DEVELOPER) {
            /* Envia arquivos - Verifica quais são os arquivos que presa ser enviado */
            this.sendFiles(files);
        }

        /* Exclui os desnecessários */
        for(int count = MAX_FILES ; count < Math.max(files.size(), MAX_FILES) ; count++){
            try {
                File file = files.get(count);
                file.delete();
            } catch (Exception e) {
                logger.error("Falha ao excluir arquivo.", e);
            }
        }
    }

    private void sendFiles(List<File> files){
        ParaUrlWsdlBean urlBean = ParaEcomService.getUrlBean();
        if (urlBean == null || urlBean.getUrlWSDL() == null 
                || urlBean.getUrlWSDL().isEmpty()) {
            
            throw new RuntimeException("Não foi possível identidicar o dominio do cliente.");
        }
        
        Matcher m = Pattern.compile("^https?://([-a-zA-Z0-9+.]*[-a-zA-Z0-9+])")
                .matcher(urlBean.getUrlWSDL());
        if (!m.find()) {   
            throw new RuntimeException("Não foi possível identidicar o dominio do cliente.");
        }
        
        /** Somente o dominio do cliente */
        String url = m.group(1);
        
        for (File file : files) {
            Matcher matcher = LOG_FILE_SEND.matcher(file.getName());
            if (!matcher.find()) {
                continue;
            }

            StringBuilder subject = new StringBuilder("LOG ECOM ")
                    .append(matcher.group(3)).append("/")
                    .append(matcher.group(2)).append("/")
                    .append(matcher.group(1)).append(" - ")
                    .append(matcher.group(4)).append(" ")
                    .append(url.toUpperCase());

            StringBuilder body = new StringBuilder()
                    .append("Enviado pelo middle na data: ")
                    .append(DateUtil.dataHoraParaString(new Date()))
                    .append("\nNome original: ").append(file.getName());

            try {
                sendEmail(subject.toString(), body.toString(), file);

                File newFile = new File(file.getAbsolutePath().replaceFirst("A.log$", "E.log"));
                if (!file.renameTo(newFile)) {
                    logger.error("Falha ao renomear arquivo");
                }
            } catch (Exception e) {
                logger.error("Falha ao enviar e-mail.", e);
            }
        }
    }
    
    private void sendEmail(String subject, String body, File attachment) throws Exception {
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", MAIL_SMTP);
        prop.put("mail.smtp.port", MAIL_PORT);
        
        /** E-mail da ATS não requer
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        * */

        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_USER, MAIL_PASS);
            }
        };

        Session session = Session.getDefaultInstance(prop, authenticator);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(MAIL_USER));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(MAIL_USER));
        message.setSubject(subject);

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();

        DataSource source = new FileDataSource(attachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(attachment.getName());

        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        
        Transport.send(message);
    }
  
    private File getPathLogs(){
        String path = null;
        
        /* Percorre os itens mapeados no log4j */
        Enumeration e = Logger.getRootLogger().getAllAppenders();
        while (e.hasMoreElements()) {
            Appender app = (Appender) e.nextElement();
            if (app instanceof DailyRollingFileAppender) {
                path = ((DailyRollingFileAppender) app).getFile();
            }
        }
        
        /* Verifica se encontrou o caminho */
        if (path != null){
            File file = new File(path);
            if (file.exists()) {
                return file.getParentFile();
            }
        }
                
        return null;
    }

}
