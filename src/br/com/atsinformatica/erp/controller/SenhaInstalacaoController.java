/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.dao.ParaEcomDAO;
import br.com.atsinformatica.erp.dao.SenhaInstalacaoDAO;
import br.com.atsinformatica.erp.entity.ParaEcomBean;
import br.com.atsinformatica.erp.entity.SenhaInstalacaoERPBean;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class SenhaInstalacaoController {
    private static final Logger logger = Logger.getLogger(SenhaInstalacaoController.class);
    
    
    /**
     * Retorna senha de instalação
     * @return 
     */
    public List<SenhaInstalacaoERPBean> retornaSenhas(){
        SenhaInstalacaoDAO dao = new SenhaInstalacaoDAO();
        try{
            logger.info("Lista de senhas de instalação retornada com sucesso!");
            return dao.listaTodos();
        }catch(Exception e){
            logger.error("Erro ao retornar senhas de instalação: "+e);
            return null;
        }
    }
    
    /**
     * Retorna senha de instalação
     * @param codEmpresa codigo da empresa
     * @return SenhaInstalacaoERPBean
     */
    public SenhaInstalacaoERPBean retornaSenha(){
        ParaEcomDAO paraEcomDAO = new ParaEcomDAO();
        SenhaInstalacaoDAO senhaDAO = new SenhaInstalacaoDAO();        
        try{
            ParaEcomBean paraEcomBean = paraEcomDAO.listaTodos().get(0);
            SenhaInstalacaoERPBean senhaInstalacaoERPBean = senhaDAO.abrir(paraEcomBean.getCodEmpresaEcom());            
            logger.info("Senha de instalação retornada com sucesso.");
            return senhaInstalacaoERPBean;
        }catch(Exception e){
            logger.error("Erro ao retornar senha de instalação: "+e);
            return null;
            
        }
    }
    
}
