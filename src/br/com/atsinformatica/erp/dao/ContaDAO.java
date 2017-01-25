package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ContaBean;
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
public class ContaDAO implements IGenericDAO<ContaBean> {
    private Connection conn;
    private static Logger logger = Logger.getLogger(ContaDAO.class);

    @Override
    public void gravar(ContaBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(ContaBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ContaBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "Select codbanco, codagen, conta "
                       + "from contas                     "
                       + "where conta = ?                 ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            ContaBean conta = null;
            while(rs.next()){
                conta = new ContaBean();
                conta.setCodAgencia(rs.getString("codagen"));
                conta.setCodBanco(rs.getString("codbanco"));
                conta.setConta(rs.getString("conta"));
            }
            logger.info("Conta retornada com sucesso.");
            return conta;
        }catch(Exception e){
            logger.error("Erro ao retornar conta: "+e);
            return null;            
        }finally{
            rs.close();
            pstmt.close();
        }
    }

    @Override
    public List<ContaBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<ContaBean> listaContas = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select contas.codbanco, contas.codagen,  " +
                         "contas.conta  " +
                         "from contas";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                ContaBean contaBean = new ContaBean();
                contaBean.setCodBanco(rs.getString("codbanco"));
                contaBean.setCodAgencia(rs.getString("codagen"));
                contaBean.setConta(rs.getString("conta"));
                listaContas.add(contaBean);
            }
            
            logger.info("Contas retornadas com sucesso.");
            return listaContas;
        }catch(Exception e){
            logger.error("Erro ao retornar contas: "+e);
            return null;            
        }finally{
            stmt.close();
            rs.close();
        }
    }
    
    /**
     * Retorna conta baseado em banco e agencia
     * @param codBanco
     * @param codAgencia
     * @return
     * @throws SQLException 
     */
    public List<ContaBean> listaPorBancoAgencia(String codBanco, String codAgencia) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ContaBean> listaContas = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select codbanco, codagen,         " +
                         "conta                             " +
                         "from contas                       "
                       + "where codbanco = ? and codagen = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codBanco);
            pstmt.setString(2, codAgencia);
            rs = pstmt.executeQuery();
            while(rs.next()){
                ContaBean contaBean = new ContaBean();
                contaBean.setCodBanco(rs.getString("codbanco"));
                contaBean.setCodAgencia(rs.getString("codagen"));
                contaBean.setConta(rs.getString("conta"));
                listaContas.add(contaBean);
            }      
            logger.info("Contas retornadas com sucesso.");
            return listaContas;
        }catch(Exception e){
            logger.error("Erro ao retornar contas: "+e);
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
