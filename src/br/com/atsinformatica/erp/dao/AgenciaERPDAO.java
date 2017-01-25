package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.AgenciaERPBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class AgenciaERPDAO implements IGenericDAO<AgenciaERPBean> {
    private static final Logger logger = Logger.getLogger(AgenciaERPDAO.class);
    private Connection conn;

    @Override
    public void gravar(AgenciaERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(AgenciaERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AgenciaERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AgenciaERPBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<AgenciaERPBean> listaAgencias = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select codbanco, codagen, nomeagen "
                       + "from agencia";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);            
            while(rs.next()){
                AgenciaERPBean agencia = new AgenciaERPBean();
                agencia.setCodAgencia(rs.getString("codbanco"));
                agencia.setNomeAgencia(rs.getString("codagen"));
                agencia.setNomeAgencia(rs.getString("nomeagen"));
                listaAgencias.add(agencia);
            }
            logger.info("Agências retornadas com sucesso.");
            return listaAgencias;
        }catch(Exception e){
            logger.error("Erro ao retornar agências: "+e);
            return null;            
        }finally{
            stmt.close();
            rs.close();
        }
    }
    
    /**
     * Retorna agencia pelo código do banco
     * @param codBanco código do ban
     * @return
     * @throws SQLException 
     */
    public List<AgenciaERPBean> listByCodBanco(String codBanco) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AgenciaERPBean> listaAgencias = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select codbanco, codagen, nomeagen "
                       + "from agencia                       "
                       + "where codbanco = ?                 ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codBanco);
            rs = pstmt.executeQuery();            
            while(rs.next()){
                AgenciaERPBean agencia = new AgenciaERPBean();
                agencia.setCodBanco(rs.getString("codbanco"));
                agencia.setCodAgencia(rs.getString("codagen"));
                agencia.setNomeAgencia(rs.getString("nomeagen"));
                listaAgencias.add(agencia);
            }
            logger.info("Agências retornadas com sucesso.");
            return listaAgencias;
        }catch(Exception e){
            logger.error("Erro ao retornar agências: "+e);
            return null;            
        }finally{
            pstmt.close();
            rs.close();
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
