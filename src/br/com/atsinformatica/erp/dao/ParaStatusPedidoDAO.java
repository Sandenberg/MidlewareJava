/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ParaStatusPedidoBean;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
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
public class ParaStatusPedidoDAO implements IGenericDAO<ParaStatusPedidoBean> {

    private static Logger logger = Logger.getLogger(ParaStatusPedidoDAO.class);

    private Connection conn;

    @Override
    public void gravar(ParaStatusPedidoBean object) {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE OR INSERT INTO PARAECOM_STATUSPEDIDO(ID, NOME, MODULO, STATUS, PRINCIPAL) VALUES (?,?,?,?,?)";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, object.getId());
            pstmt.setString(2, object.getNome());
            pstmt.setString(3, object.getModulo());
            pstmt.setString(4, object.getStatus() != null ? object.getStatus().toString() : null);
            pstmt.setBoolean(5, object.isPrincipal());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar bean: ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException ex) {
            }
        }
    }

    @Override
    public void alterar(ParaStatusPedidoBean object) throws SQLException {
        this.gravar(object);
    }

    @Override
    public ParaStatusPedidoBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Integer idStatus = Integer.parseInt(id.replaceAll("[^0-9]", ""));
            
            conn = ConexaoATS.getConnection();
            String querie = "SELECT * FROM PARAECOM_STATUSPEDIDO WHERE id = ?";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, idStatus);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return this.parseBean(rs);
            }
            return null;
        } catch (Exception e) {
            logger.error("Erro ao retornar status do pedido", e);
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {
            }
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {
            }
        }
    }

    @Override
    public List<ParaStatusPedidoBean> listaTodos() {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM PARAECOM_STATUSPEDIDO ORDER BY id";
            conn = ConexaoATS.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<ParaStatusPedidoBean> list = new ArrayList<>();
            while (rs.next()) {
                list.add(this.parseBean(rs));
            }
            logger.info("Lista de url retornada com sucesso.");
            return list;
        } catch (Exception e) {
                logger.error("Erro ao retornar lista de url", e);
            throw new RuntimeException(e);
        } finally {
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
    
    public void deletaDiferentesDe(List<ParaStatusPedidoBean> beans){
        if (beans == null || beans.isEmpty()) {
            return;
        }
        
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            // NOT IN Não funcionou
            StringBuilder sql = new StringBuilder("DELETE FROM PARAECOM_STATUSPEDIDO WHERE 1 = 1");
            for (ParaStatusPedidoBean be : beans) {
                if (be.getId() != null) {
                    sql.append(" AND id <> ").append(be.getId());
                }
            }
        
            pstmt =  conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao retornar bean: ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {
            }
        }
    }
    
    public Integer getIdPadrao(StatusPedido status) {        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "SELECT id FROM PARAECOM_STATUSPEDIDO WHERE status = ? AND principal = ?";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, status.toString());
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("ID");
            }
            return null;
        } catch (Exception e) {
            logger.error("Erro ao retornar bean: ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {
            }
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {
            }
        }
    }

    private ParaStatusPedidoBean parseBean(ResultSet rs) throws SQLException{
        ParaStatusPedidoBean bean = new ParaStatusPedidoBean();
        bean.setId(rs.getInt("ID"));
        bean.setNome(rs.getString("NOME"));
        bean.setModulo(rs.getString("MODULO"));
        bean.setStatus(StatusPedido.converteValor(rs.getString("STATUS")));
        bean.setPrincipal(rs.getBoolean("PRINCIPAL"));
        
        return bean;
    }
    
    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
