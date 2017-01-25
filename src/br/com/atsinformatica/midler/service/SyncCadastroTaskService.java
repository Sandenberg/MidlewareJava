package br.com.atsinformatica.midler.service;

import br.com.atsinformatica.erp.controller.SincERPController;
import br.com.atsinformatica.erp.dao.HistoricoIntegraDAO;
import br.com.atsinformatica.erp.entity.HistoricoIntegraERPBean;
import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.exception.TipoOperacaoException;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.firebirdsql.jca.FBXAException;

/**
 *
 * @author niwrodrigues
 */
public class SyncCadastroTaskService{
    private final Logger logger = Logger.getLogger(SyncCadastroTaskService.class);
    
    private static SyncCadastroTaskService instance;
    private HistoricoIntegraDAO historicoDAO;
    private SincERPController syncController;
    
    private static SyncCadastroTaskService getInstance() {
        if (instance == null) {
            instance = new SyncCadastroTaskService();
        }
        return instance;
    }
    
    public static void start(){
        SyncCadastroTaskService.getInstance().synchronize();
    }
    
    private void synchronize() {
        try {
            /** 
             * Esse tratamento evita que o middle trabalhe em longos periodos.
             * Caso haja muitos registros para integrar ele irá percorrer somente
             * MAX_SYNC, os demais ficarão para a proxima chamada do loop
             */
            int qtdSync = 0;
            HistoricoIntegraERPBean bean = null;
            
            do {
                bean = this.getHistoricoDAO().proximoItemPendente();
                
                if (bean != null) {                    
                    // Precisa verificar conexão com WS a cada item sincronizado
                    SyncService.verificaConexaoWS();

                    // Inicia o processo de integração    
                    Connection connection = ConexaoATS.getConnection();                                        
                    try {
                        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                        connection.setAutoCommit(false);

                        bean.setStatus(StatusIntegracao.EM_ANDAMENTO);
                        getHistoricoDAO().alterar(bean);
                        connection.commit(); // Comita o histórico

                        connection.setAutoCommit(false);
                        this.getSyncController().setHistInteg(bean);
                        switch (bean.getTipoOperacao()){
                            case INSERT:{
                                this.getSyncController().post(bean.getObjectSinc());
                                break;
                            }
                            case UPDATE:{
                                this.getSyncController().update(bean.getObjectSinc());
                                break;
                            }
                            case DELETE:{
                                this.getSyncController().delete(bean.getCodEntidade());
                                break;
                            }
                            default: {
                                throw new TipoOperacaoException("Tipo de operação não conhecido.");
                            }
                        }
                        connection.commit();
                    } catch (FBXAException e){
                        ConexaoATS.fechaConexao();

                        connection = ConexaoATS.getConnection();      
                        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                        connection.setAutoCommit(false);
                        bean.setStatus(StatusIntegracao.PENDENTE);
                        getHistoricoDAO().alterar(bean);
                        connection.commit(); // Comita o histórico
                    } catch (Exception e) {
                        try {
                            connection.rollback();
                        } catch (SQLException ex) {
                            logger.debug("Falha ao aplicar rollback.", ex);
                        }
                        logger.error("Falha durante a integração.", e);
                    }
                }
                
                qtdSync++;
            } while (bean != null && qtdSync < SyncService.MAX_SYNC);
        } catch (Exception e) {
            logger.error("Falha ao buscar itens para integrar.", e);
        } 
    }

    private HistoricoIntegraDAO getHistoricoDAO() {
        if( this.historicoDAO == null){
            this.historicoDAO = new HistoricoIntegraDAO();
        }
        return historicoDAO;
    }

    public SincERPController getSyncController() {
        if (this.syncController == null) {
            this.syncController = new SincERPController();
        }
        return syncController;
    }
}
