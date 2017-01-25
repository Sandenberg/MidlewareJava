package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ParaFatBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class ParaFATDAO implements IGenericDAO<ParaFatBean> {
    private Connection conn;
    private Logger logger = Logger.getLogger(ParaFATDAO.class);

    @Override
    public void gravar(ParaFatBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(ParaFatBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Seleciona paramatro tipo de operação da parafat
     * @param id
     * @return bean de parametros do resulth
     * @throws SQLException 
     */
    @Override
    public ParaFatBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ParaFatBean abrir() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        ParaFatBean paraFat = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT tpoppad FROM parafat";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                paraFat = new ParaFatBean();
                paraFat.setTpopPad(rs.getString("tpoppad"));
            }
            logger.info("Parametro de fatura do erp, retornado com sucesso.");
            return paraFat;
        }catch(Exception e){
            logger.error("Erro ao retornar parametro de fatura do erp: "+e);
            return null;
        }finally{
            //stmt.close();
            //rs.close();         
        }
    }
    
    /**
     * Retorna tipos de região
     * @return bean contendo tipos de região
     * @throws SQLException 
     */
    public ParaFatBean retornaTipoRegiao(){
        Statement stmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select regiao00, regiao01, regiao02, regiao03 from parafat";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            ParaFatBean paraFat = null;
            if(rs.next()){
                paraFat = new ParaFatBean();
                paraFat.setRegiao00(rs.getString("regiao00"));
                paraFat.setRegiao01(rs.getString("regiao01"));
                paraFat.setRegiao02(rs.getString("regiao02"));
                paraFat.setRegiao03(rs.getString("regiao03"));
            }
            return paraFat;
        }catch(Exception e){
            logger.error("Erro ao retornar tipo de região", e);
            throw new RuntimeException(e);
        }finally{
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                stmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    @Override
    public List<ParaFatBean> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String retornaInfTVenda(){
        Statement stmt = null;
        String inftVenda = "";
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select inftvenda from parafat";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                inftVenda = rs.getString("inftvenda");
            }
            logger.info("Informação do tipo de venda retornado com sucesso.");
            return inftVenda;
        }catch(Exception e){
            logger.error("Erro ao retornar informação do tipo de venda: "+e);
            return null;                     
        }
    }
    
}
