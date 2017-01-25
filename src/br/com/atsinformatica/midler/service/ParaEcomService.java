package br.com.atsinformatica.midler.service;

import br.com.atsinformatica.erp.dao.ParaEcomDAO;
import br.com.atsinformatica.erp.dao.ParaUrlDAO;
import br.com.atsinformatica.erp.entity.ParaEcomBean;
import br.com.atsinformatica.erp.entity.ParaUrlWsdlBean;
import br.com.atsinformatica.utils.SingletonUtil;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Já que quando houver alterações nos parâmetros precisa reiniciar a aplicação,
 * não faz sentido ficar buscando esses dados a todo momento.
 * 
 * @author niwrodrigues
 */
public class ParaEcomService {
    private static final Logger logger = Logger.getLogger(ParaEcomService.class);
    
    private static ParaEcomBean bean;
    private static ParaUrlWsdlBean urlBean;
    
    public static ParaEcomBean getParams(){
        if (bean == null) {
            try {
                bean = SingletonUtil.get(ParaEcomDAO.class).buscaParametros();
            } catch (Exception e) {
                logger.error("Erro ao obter parametros.", e);
            }
            
            /** Força carregar os parametros de URL */
            ParaEcomService.getUrlBean();
        }
        
        return bean;
    }

    public static ParaUrlWsdlBean getUrlBean() {
        if (urlBean == null) {
            try {
                List<ParaUrlWsdlBean> list = SingletonUtil.get(ParaUrlDAO.class)
                        .listaTodos();
                if (list != null && !list.isEmpty()) {
                    urlBean = list.iterator().next();
                }
            } catch (Exception e) {
                logger.error("Erro ao obter parametros.", e);
            }
        }
        return urlBean;
    }

    /**
     * @return Default  10
     */
    public static int getQtdMantido(){
        int value = (ParaEcomService.getParams() == null) ? 
                10 : ParaEcomService.getParams().getQtdMantido();
        
        return Math.max(1, value);
    }

    /**
     * @return Default false
     */
    public static boolean getAtivaSincronizacao(){
        return (ParaEcomService.getParams() == null ) ? 
                false : Integer.valueOf(1).equals(ParaEcomService.getParams().getAtivaSincronizacao());
    }

    public static String getFilialSincronizacao(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getFilialSincronizacao();
    }

    public static String getCodEmpresaEcom(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getCodEmpresaEcom();
    }
    
    public static String getCodVendendEcom() {
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getCodVendendEcom();
    }

    public static String getProdPaiFilho(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getProdPaiFilho();
    }

    public static String getCodOperDentroEstado(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getCodOperDentroEstado();
    }

    public static String getCodOperForaEstado(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getCodOperForaEstado();
    }

    public static String getCaminhoRavePedido(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getCaminhoRavePedido();
    }

    public static String getGrupoProdNSincronizar(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getGrupoProdNSincronizar();
    }

    public static String getCodBanco(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getCodBanco();
    }

    public static String getConta(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getConta();
    }

    public static String getAgencia(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getAgencia();
    }

    public static String getCodPrazo(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getCodPrazo();
    }

    public static String getCodTVenda(){
        return (ParaEcomService.getParams() == null) ? 
                null : ParaEcomService.getParams().getCodTVenda();
    }
    
    public static String getDiretorioImagens() {
        if (ParaEcomService.getParams() == null || ParaEcomService.getParams().getDiretorioImagens() == null) {
            return null;
        }
        
        String dir = ParaEcomService.getParams().getDiretorioImagens().trim();
        if (!dir.endsWith("\\")) {
            dir += "\\";
        }
        
        return dir;
    }
}
