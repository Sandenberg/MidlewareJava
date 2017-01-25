/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.dao.GenericDAO;
import br.com.atsinformatica.erp.dao.HistoricoIntegraDAO;
import br.com.atsinformatica.erp.dao.MappedClassesDAO;
import br.com.atsinformatica.erp.entity.HistoricoIntegraERPBean;
import br.com.atsinformatica.midler.annotation.GenericController;
import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.exception.ErroSyncException;
import br.com.atsinformatica.utils.TradutorErro;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Superclasse generica responsável por gerenciar a sincronização na loja
 * virtual
 * @author AlexsanderPimenta
 */
public class SincERPController<T> implements ISincController<T> {
    private final Logger logger = Logger.getLogger(SincERPController.class);
    private HistoricoIntegraDAO historicoDAO;
    
    //Anotação do tipo de controladora
    private GenericController anotacao;
    //Classe da controladora
    private Class<?> classZ;
    //instancia da classe
    private Object instance;
    //metodo a ser executado
    Method action = null;
    private HistoricoIntegraERPBean histInteg;

    /**
     * Construtor padrão
     */
    public SincERPController() {
        
    }

    /**
     * Construtor que recebe como parametro o objeto a ser sincronizado
     * @param histInteg objeto de integração
     */
    public SincERPController(HistoricoIntegraERPBean histInteg) {

    }

    @Override
    public void post(T obj) throws Exception {
        try {
            if (obj == null) {
                throw new ErroSyncException("Não foi possível localizar o registro com esse código.");
            }
            
            action = classZ.getDeclaredMethod("post", new Class[]{obj.getClass()});
            action.invoke(instance, obj);
            
            this.getHistInteg().setStatus(StatusIntegracao.SINCRONIZADO);
            this.getHistInteg().setDataInteg(new Date());
            this.getHistoricoDAO().alterar(this.getHistInteg());
        } catch (Exception e) {
            this.trataException(e);
        }
    }

    @Override
    public int update(T obj) throws Exception {
        try {
            if (obj == null) {
                throw new ErroSyncException("Não foi possível localizar o registro com esse código.");
            }
            
            action = classZ.getDeclaredMethod("update", new Class[]{obj.getClass()});

            Integer responseStatus = (Integer) action.invoke(instance, obj);
            if (responseStatus != 200) {
                logger.info("Update " + obj.getClass() + " - ");
                throw new ErroSyncException(responseStatus, TradutorErro.descricaoOf(responseStatus));
            }

            this.getHistInteg().setDataInteg(new Date());
            this.getHistInteg().setStatus(StatusIntegracao.SINCRONIZADO);
            this.getHistoricoDAO().alterar(this.getHistInteg());
                     
            logger.info("Atualizado na loja virtual com sucesso.");
            return responseStatus;
        } catch (Exception e) {
            this.trataException(e);
            return 0;
        }
    }

    @Override
    public int delete(String id) throws Exception {
        try {
            action = classZ.getDeclaredMethod("delete", new Class[]{String.class});

            Integer responseStatus = (Integer) action.invoke(instance, id);
            if (responseStatus != 200) {
                throw new ErroSyncException(responseStatus, TradutorErro.descricaoOf(responseStatus));
            }

            this.getHistInteg().setDataInteg(new Date());
            this.getHistInteg().setStatus(StatusIntegracao.SINCRONIZADO);
            this.getHistoricoDAO().alterar(this.getHistInteg());
                     
            logger.info("Deletado na loja virtual com sucesso.");
            return responseStatus;
        } catch (Exception e) {
            this.trataException(e);
            return 0;
        }
    }

    /**
     * @return the histInteg
     */
    public HistoricoIntegraERPBean getHistInteg() {
        return histInteg;
    }

    /**
     * @param histInteg the histInteg to set
     */
    public void setHistInteg(HistoricoIntegraERPBean histInteg) {
        try {
            this.histInteg = histInteg;
            
            // Tenta descobir o controller
            if (histInteg.getObjectSinc() != null) {
                anotacao = histInteg.getObjectSinc().getClass().getAnnotation(GenericController.class);        
            } else if (histInteg.getEntidade() != null) {
                GenericDAO dao = MappedClassesDAO.getInstancia(histInteg.getEntidade());
                if (dao != null && dao.getClasseBean() != null) {
                    anotacao = (GenericController) dao.getClasseBean().getAnnotation(GenericController.class);        
                }
            }
            
            if (anotacao == null || anotacao.classPath() == null || anotacao.classPath().isEmpty()) {
                throw new RuntimeException("Não foi concontrado o controlle para essa entidade.");
            }
            
            classZ = Class.forName(anotacao.classPath());
            instance = classZ.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.error("Erro de instancia da classe", e);
            throw new RuntimeException(e);
        }
    }
    
    private void trataException(Throwable e){
        String mensage = null;
        if (e.getCause() instanceof ErroSyncException) {
            mensage = ((ErroSyncException) e.getCause()).getMessage();
        }
        if (e instanceof InvocationTargetException) {
            mensage = e.getCause().getMessage();
        }
        if (mensage == null || mensage.isEmpty()) {
            mensage = e.getMessage();
        }
        if (mensage == null || mensage.isEmpty()) {
            mensage = e.getClass().getSimpleName();
        }

        try {
            this.getHistInteg().setStatus(StatusIntegracao.ERRO);
            this.getHistInteg().setDataInteg(new Date());
            this.getHistoricoDAO().alterar(this.getHistInteg());
        } catch (Exception ex) {
            logger.error("Erro ao atulizar registro de integração", ex);
        }

        try {
            LogEcomErroController.geraErroLog(getHistInteg().getId(), "Erro ao efetuar " + histInteg.getTipoOperacao() + ": " + mensage);
        } catch (Exception ex) {
            logger.error("Erro ao registrar exception", ex);
        }
        logger.error("Erro ao efetuar " + histInteg.getTipoOperacao() + " " + histInteg.getEntidade() + " .", e);
    }
    
    /* *************** */
    
    public HistoricoIntegraDAO getHistoricoDAO() {
        if(this.historicoDAO == null){
            this.historicoDAO = new HistoricoIntegraDAO();
        }
        return historicoDAO;
   }
}
