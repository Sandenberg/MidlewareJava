package br.com.atsinformatica.midler.service;

import br.com.atsinformatica.erp.entity.ParaUrlWsdlBean;
import br.com.atsinformatica.midler.exception.TipoOperacaoException;
import br.com.atsinformatica.prestashop.clientDAO.GenericPrestashopDAO;
import br.com.atsinformatica.utils.DateUtil;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public class SyncService{
    private final static Logger logger = Logger.getLogger(SyncService.class);
    public final static int MAX_SYNC = 10;
    
    private static SyncService instance;
    
    private Timer timer;
    private boolean inProcess;
    private boolean inStandby;
    
    private Thread tread = null;

    private static SyncService getInstance() {
        if (instance == null) {
            instance = new SyncService();
        }
        return instance;
    }
    
    /**
     * Inicia timer se necessário
     */
    public static void initialize() {
        SyncService.getInstance().instanceInitialize();
    }
    private void instanceInitialize() {
        if (timer == null) {
            long period = TimeUnit.SECONDS.toMillis(60);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SyncService.this.execute();
                }
            }, period, period);
        }
    }
    
    /**
     * Força a execução da sincronização
     */
    public static void forceRun(){
        SyncService.getInstance().instanceForceRun();
    }
    private void instanceForceRun(){
        new Thread(){
            @Override
            public void run() {
                SyncService.this.execute();
            }
        }.start();
    }
        
    /**
     * Realiza o processo de sincronização
     */
    private void execute(){
        String name = SyncService.this.getClass().getSimpleName();
        if (!ParaEcomService.getAtivaSincronizacao()) {
            Thread.currentThread().setName(name + " - Desativado no parâmetro");
            logger.debug("Sincronizaçao desabilitada.");
            return;
        }
        
        Thread.currentThread().setName(name + " - Em andamento desde " + DateUtil.dataHoraParaString(new Date()));

        if(SyncService.this.inProcess){
            SyncService.this.inStandby = true;
            return;
        }

        SyncService.this.inProcess = true;
        SyncService.this.inStandby = true;

        try {
            while (SyncService.this.inStandby) {
                SyncService.this.inStandby = false;
                try {
                    SyncCadastroTaskService.start();
                    SyncPedidoTaskService.start();
                } catch (Throwable e) {
                    logger.error("Erro durante a execução da Thread", e);
                    throw new RuntimeException(e);
                }
            }
        } finally {
            SyncService.this.inProcess = false;
            SyncService.this.inStandby = false;
            Thread.currentThread().setName(name + " - Paralizada desde " + 
                    DateUtil.dataHoraParaString(new Date()));
        }
    }

    public static boolean isInProcess() {
        return SyncService.getInstance().inProcess;
    }
    
    /**
     * Verifica conexão com webservice
     * @return 
     */
    public static void verificaConexaoWS() {
        boolean retorno = false;
        
        ParaUrlWsdlBean paraUrl = ParaEcomService.getUrlBean();
        if (paraUrl != null) {
            GenericPrestashopDAO prestashop = new GenericPrestashopDAO();
            // Testa conexão
            if ((paraUrl.getUrlWSDL() != null) && (paraUrl.getUrlKey() != null)) {
                if (prestashop.getWebService(paraUrl)) {                    
                    retorno = true;
                }
            }
        }      
        
        // Retorna erro, se houver
        if (!retorno) {
            throw new TipoOperacaoException("Conexão com web service inválida.");
        }
    }
}
