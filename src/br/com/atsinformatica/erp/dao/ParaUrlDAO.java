/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ParaUrlWsdlBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.utils.Funcoes;
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
public class ParaUrlDAO implements IGenericDAO<ParaUrlWsdlBean> {
    private static Logger logger = Logger.getLogger(ParaUrlDAO.class);
    
    private Connection conn;

    @Override
    public void gravar(ParaUrlWsdlBean object) {        
        PreparedStatement pstmt = null;      
        try {
            conn = ConexaoATS.getConnection();            
            ///conn.setAutoCommit(false);
            String querie = "UPDATE OR INSERT INTO PARAURL (CODPARAURL, URL, URLKEY) VALUES (?,?,?)";
            if(object.getCodParaUrlWsdl()==null)object.setCodParaUrlWsdl(ultimoRegistro());
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getCodParaUrlWsdl());
            pstmt.setString(2, object.getUrlWSDL());
            pstmt.setString(3, object.getUrlKey());
            pstmt.executeUpdate();
            logger.info("Url/wsdl salvo com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao salvar url/wsdl: ", e);  
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close(); 
            } catch (SQLException | NullPointerException ex) {}
        }
    }

    @Override
    public void alterar(ParaUrlWsdlBean object)  throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE PARAURL set URL = ?, URLKEY = ?  "
                          + " WHERE CODPARAURL = ? ";
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getUrlWSDL());
            pstmt.setString(2, object.getUrlKey());
            pstmt.setString(3, object.getCodParaUrlWsdl());            
            pstmt.executeUpdate();
            conn.commit();
            logger.info("Url/WSDL alterado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao alterar Url/WSDL: "+e);
            conn.rollback();
        } finally {
            pstmt.close();
            
        }
    }

    @Override
    public void deletar(String id){
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "DELETE FROM PARAURL WHERE CODPARAURL = ?";
            pstmt = conn.prepareStatement(querie);
            //conn.setAutoCommit(false);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            logger.info("URL/WSDL deletado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao deletar URL/WSDL: ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    @Override
    public ParaUrlWsdlBean abrir(String  id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "Select * from PARAURL where CODPARAURL = ?";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            ParaUrlWsdlBean paraUrlBean = new ParaUrlWsdlBean();
            while(rs.next()){
                paraUrlBean.setCodParaUrlWsdl(rs.getString("CODPARAURL"));
                paraUrlBean.setUrlWSDL(rs.getString("URL"));
                paraUrlBean.setUrlKey(rs.getString("URLKEY"));
            }         
            logger.info("Url/WSDl retornado com sucesso!");
            return paraUrlBean;
        } catch (Exception e) {
            logger.error("Erro ao retornar url/wsdl: "+e);
            return null;
        } finally {
            rs.close();
            pstmt.close();       
        }
        
    }

    @Override
    public List<ParaUrlWsdlBean> listaTodos(){
        Statement stmt = null;
        ResultSet rs = null;    
        try{
            List<ParaUrlWsdlBean> listaParaUrl = new ArrayList<>();
            String sql  = "SELECT * from PARAURL ";
            conn = ConexaoATS.getConnection();
            
            // #VS Testando: Erro "Cursor is not open"
            stmt = conn.createStatement();
            /*
            stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
		ResultSet.CONCUR_READ_ONLY,
                ResultSet.HOLD_CURSORS_OVER_COMMIT);
            //*/
            
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                ParaUrlWsdlBean paraUrl = new ParaUrlWsdlBean();
                paraUrl.setCodParaUrlWsdl(rs.getString("CODPARAURL"));
                paraUrl.setUrlWSDL(rs.getString("URL"));
                paraUrl.setUrlKey(rs.getString("URLKEY"));
                listaParaUrl.add(paraUrl);                      
            }           
            logger.info("Lista de url retornada com sucesso.");
            return listaParaUrl;
        }catch(Exception e){
            logger.error("Erro ao retornar lista de url", e);
            throw new RuntimeException(e);
        }finally{
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {
            }
            try {
                stmt.close();
            } catch (SQLException | NullPointerException e) {
            }
        }
       
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select max(CODPARAURL) cod from PARAURL";
            conn = ConexaoATS.getConnection();
            stmt = conn.createStatement();
            //stmt = ConexaoATS.conectaERP().createStatement();
            rs = stmt.executeQuery(sql);
            int cod = 0;
            while (rs.next()) {
                if (rs.getString("cod") != null) {
                    cod = Integer.valueOf(rs.getString("cod")) + 1;
                }
            }
            return Funcoes.preencheCom(Integer.toString(cod), "0", 6, Funcoes.LEFT);
        } catch (Exception e) {
            return null;
        } finally {
            stmt.close();
            rs.close();
        }
    }
}
