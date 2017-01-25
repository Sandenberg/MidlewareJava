/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ParaEcomBean;
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
public class ParaEcomDAO implements IGenericDAO<ParaEcomBean> {   
    private static Logger logger = Logger.getLogger(ParaEcomDAO.class);
    private Connection conn;

    @Override
    public void gravar(ParaEcomBean object){
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "INSERT INTO PARAECOM (CODPARAECOM, QTDEMANTIDOS, "
                    + " ATIVASINCRONIZACAO, CODEMPRESAECOM, CODVENDENDECOM, CONTA, CODBANCO, AGENCIA, CODPRAZO, CODTVENDA, "
                    + "DIRETORIOIMAGENS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, ultimoRegistro());
            pstmt.setInt(2, object.getQtdMantido());
            pstmt.setInt(3, object.getAtivaSincronizacao());
            pstmt.setString(4, object.getCodEmpresaEcom());
            pstmt.setString(5, object.getCodVendendEcom());
            pstmt.setString(6, object.getConta());
            pstmt.setString(7, object.getCodBanco());
            pstmt.setString(8, object.getAgencia());
            pstmt.setString(9, object.getCodPrazo());
            pstmt.setString(10, object.getCodTVenda());
            pstmt.setString(11, object.getDiretorioImagens());
            pstmt.executeUpdate();
            logger.info("Parametros salvos com sucesso!");
        } catch (Exception e) {            
            logger.error("Erro ao salvar parametros: ", e);
            throw new RuntimeException("Falha ao gravar parametros.");
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException se) {
                logger.error("Erro ao fechar coneção: ", se);
            }
        }
    }

    @Override
    public void alterar(ParaEcomBean object){
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String querie = "Update PARAECOM set                     "
                    + " QTDEMANTIDOS = ?,                            "
                    + " ATIVASINCRONIZACAO = ?,                      "
                    + " CODEMPRESAECOM = ?,                          "
                    + " CODVENDENDECOM = ?,                          "
                    + " CONTA = ?,                                   "
                    + " CODBANCO = ?,                                "
                    + " AGENCIA = ?,                                 "
                    + " CODPRAZO = ?,                                "
                    + " CODTVENDA = ?,                               "
                    + " DIRETORIOIMAGENS = ?                         "
                    + " WHERE CODPARAECOM = ?                        ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, object.getQtdMantido());
            pstmt.setInt(2, object.getAtivaSincronizacao());
            pstmt.setString(3, object.getCodEmpresaEcom());
            pstmt.setString(4, object.getCodVendendEcom());
            pstmt.setString(5, object.getConta());
            pstmt.setString(6, object.getCodBanco());
            pstmt.setString(7, object.getAgencia());
            pstmt.setString(8,object.getCodPrazo());
            pstmt.setString(9, object.getCodTVenda());    
            pstmt.setString(10, object.getDiretorioImagens());   
            pstmt.setString(11, object.getCodparaecom());                 
            pstmt.executeUpdate();
            logger.info("Parametros alterados com sucesso!");
        }catch(Exception e){
            try {
                conn.rollback();
            } catch (SQLException se) {
                logger.error("Erro ao executar rollback: ", se);
            }
            
            logger.error("Erro ao atualizar parametros: ", e);
            throw new RuntimeException("Falha ao gravar parametros.");
        }finally{
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException se) {
                logger.error("Erro ao fechar coneção: ", se);
            }
        }
    }

    @Override
    public void deletar(String id) throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            String querie = "DELETE FROM PARAECOM where CODPARAECOM = ?";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, id);
            conn.commit();
            logger.info("Parametro deletado com sucesso!");
        }catch(Exception e){
            logger.error("Erro ao deletar parametro: "+e);
            conn.rollback();
        }finally{
            pstmt.close();
        }
    }

    @Override
    public ParaEcomBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM PARAECOM WHERE CODPARAECOM = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            ParaEcomBean  bean = new ParaEcomBean();
            if(rs.next()){
                bean = this.convertToBean(rs);
            }
            logger.info("Parametro retornado com sucesso!");
            return bean;
        }catch(Exception e){
            logger.error("Erro ao rettorna parametro: "+e);
            return null;
        }finally{
            pstmt.close();
            rs.close();
        }
    }
    
    public ParaEcomBean buscaParametros(){
        Statement stmt = null; ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            if (conn != null) {
                String sql = "SELECT FIRST 1 * FROM PARAECOM";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    return this.convertToBean(rs);
                }
            } else {
                throw new RuntimeException("Falha ao obter coneçao.");
            }
        }catch(Exception e){
            logger.error("Erro ao buscar parametros", e);
            throw new RuntimeException("Erro ao buscar parametros.");
        }finally{
            try {
                rs.close();
                stmt.close();
            } catch (SQLException | NullPointerException se) {
                logger.error("Erro ao fechar coneção", se);
            }
        }
        
        return null;
    }

    @Override
    public List<ParaEcomBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try{
            List<ParaEcomBean> listaParaEcom = new ArrayList<>();            
            conn = ConexaoATS.getConnection();
            if (conn != null) {
                String sql = "Select * from PARAECOM";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                   listaParaEcom.add(this.convertToBean(rs));
                }
                logger.info("Lista de parametros, retornada com sucesso!");
                return listaParaEcom;
            }
            else {
                return null;
            }
        }catch(Exception e){
            throw new RuntimeException("Erro ao retornar lista de parametros: ", e);
        }finally{
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select max(CODPARAECOM) cod from PARAECOM";
            conn = ConexaoATS.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            int cod = 1;
            if(rs.next()){
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
    
    private ParaEcomBean convertToBean(ResultSet rs) throws SQLException{
        ParaEcomBean para = new ParaEcomBean();
        para.setCodparaecom(rs.getString("CODPARAECOM"));
        para.setQtdMantido(rs.getInt("QTDEMANTIDOS"));
        para.setAtivaSincronizacao(rs.getInt("ATIVASINCRONIZACAO"));
        para.setCodEmpresaEcom(rs.getString("CODEMPRESAECOM"));
        para.setCodVendendEcom(rs.getString("CODVENDENDECOM"));
        para.setConta(rs.getString("CONTA"));
        para.setCodBanco(rs.getString("CODBANCO"));
        para.setAgencia(rs.getString("AGENCIA"));
        para.setCodPrazo(rs.getString("CODPRAZO"));
        para.setCodTVenda(rs.getString("CODTVENDA"));
        para.setDiretorioImagens(rs.getString("DIRETORIOIMAGENS"));
        
        return para;
    }
}
