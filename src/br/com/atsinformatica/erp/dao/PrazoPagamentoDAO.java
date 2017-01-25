/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.PrazoPagamentoERPBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class PrazoPagamentoDAO implements IGenericDAO<PrazoPagamentoERPBean> {
    private static final Logger logger = Logger.getLogger(PrazoPagamentoDAO.class);
    private Connection conn;

    @Override
    public void gravar(PrazoPagamentoERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(PrazoPagamentoERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PrazoPagamentoERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PrazoPagamentoERPBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<PrazoPagamentoERPBean> listaPrazos = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select  codprazo, prazo1, prazo2,           "
                       + "prazo3, prazo4, prazo5, prazo6,             "
                       + "prazo7, numparcelas, descricao              "
                       + "from condpag";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                PrazoPagamentoERPBean prazoPagamentoERPBean = new PrazoPagamentoERPBean();
                prazoPagamentoERPBean.setCodPrazo(rs.getString("codprazo"));
                prazoPagamentoERPBean.setDescricao(rs.getString("descricao"));
                prazoPagamentoERPBean.setPrazo1(rs.getDouble("prazo1"));
                prazoPagamentoERPBean.setPrazo2(rs.getDouble("prazo2"));
                prazoPagamentoERPBean.setPrazo3(rs.getDouble("prazo3"));
                prazoPagamentoERPBean.setPrazo4(rs.getDouble("prazo4"));
                prazoPagamentoERPBean.setPrazo5(rs.getDouble("prazo5"));
                prazoPagamentoERPBean.setPrazo6(rs.getDouble("prazo6"));
                prazoPagamentoERPBean.setPrazo7(rs.getDouble("prazo7"));
                prazoPagamentoERPBean.setNumparcelas(rs.getInt("numparcelas"));
                
                listaPrazos.add(prazoPagamentoERPBean);
            } 
            logger.info("Prazos retornados com sucesso!");
            return listaPrazos;
        }catch(Exception e){
            logger.error("Erro ao retornar prazos: "+e);
            return null;            
        }finally{
            stmt.close();
            rs.close();
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
