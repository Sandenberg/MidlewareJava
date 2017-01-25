/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ReservaIERP;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class ReservaIERPDAO implements IGenericDAO<ReservaIERP> {

    private static Logger logger = Logger.getLogger(ReservaIERPDAO.class);
    private Connection conn;

    @Override
    public void gravar(ReservaIERP object) {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();            
            String sql = " INSERT INTO RESERVASI "
                    + "   (CODRESERVA, CODPROD, QUANTIDADE, PRECO, BAIXADO, CODGRADE) "
                    + "     VALUES (?, ?, ?, ?, ?, ?);";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, object.getCodReservar());
            pstmt.setString(2, object.getCodProd());
            pstmt.setDouble(3, object.getQuantidade());
            pstmt.setDouble(4, object.getPreco());
            pstmt.setString(5, object.getBaixado());
            pstmt.setString(6, object.getCodGrade());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("ERRO ao Gravar Reservar dos Itens da Reserva", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    @Override
    public void alterar(ReservaIERP object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReservaIERP abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ReservaIERP> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
