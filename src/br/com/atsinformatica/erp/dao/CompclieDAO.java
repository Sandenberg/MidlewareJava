package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.CompclieBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class CompclieDAO implements IGenericDAO<CompclieBean> {
    private static Logger logger = Logger.getLogger(CompclieDAO.class);
    private Connection conn;

    @Override
    public void gravar(CompclieBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Insere ou atualiza endereço de nota fiscal eletronica
     * @param object dados endereço nota fiscal eletronica
     * @throws SQLException 
     */
    @Override
    public void alterar(CompclieBean object) {
        PreparedStatement pstmt = null;
        try {
            String querie = "UPDATE OR INSERT INTO compclie(codcliente, endereconfe, numeronfe, complementonfe)"
                    + " VALUES(?,?,?,?)";
            conn = ConexaoATS.getConnection();
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getCodCliente());
            pstmt.setString(2, object.getEnderecoNFE());
            pstmt.setString(3, object.getNumeroNFE());
            pstmt.setString(4, object.getComplementoNFE());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar endereço de nota fiscal eletrônica", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CompclieBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CompclieBean> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
