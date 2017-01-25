/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ReservaCERP;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class ReservaCERPDAO implements IGenericDAO<ReservaCERP> {

    private static Logger logger = Logger.getLogger(ReservaCERPDAO.class);
    private Connection conn;

    @Override
    public void gravar(ReservaCERP object) throws SQLException {
    }

    public String gravarReserva(ReservaCERP object){
        PreparedStatement pstmt = null;
        try {
            String codReserva = ultimoRegistro();
            
            conn = ConexaoATS.getConnection();
            String sql = " INSERT INTO RESERVASC "
                    + " (CODRESERVA, DATARESERVA, CODEMPRESA, CODVENDEDOR, CODCLIENTE, USUARIO, CODPEDIDO, CODORC) "
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codReserva);
            pstmt.setDate(2, new Date(object.getDtReserva().getTime()));
            pstmt.setString(3, object.getCodEmpresa());
            pstmt.setString(4, object.getCodVendedor());
            pstmt.setString(5, object.getCodCliente());
            pstmt.setString(6, object.getUsuario());
            pstmt.setString(7, object.getCodPedido());
            pstmt.setString(8, object.getCodOrc());

            pstmt.executeUpdate();
            return codReserva;
        } catch (Exception e) {
            logger.error("ERRO ao Gravar Reservar", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }

    }

    @Override
    public void alterar(ReservaCERP object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReservaCERP abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ReservaCERP> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select max(CODRESERVA) cod from RESERVASC";
            conn = ConexaoATS.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next() && rs.getString("cod") != null) {
                return "" + (Integer.valueOf(rs.getString("cod")) + 1);
            }
            return "1";
        } catch (Exception e) {
            logger.error("ERRO ao Consultar ID ERP do ultimo CODRESERVA Cadastrado: ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
        }

    }

}
