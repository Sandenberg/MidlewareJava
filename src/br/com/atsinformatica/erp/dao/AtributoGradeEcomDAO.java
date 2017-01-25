package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.AtributoGradeEcom;
import br.com.atsinformatica.midler.annotation.GenericType;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
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
@GenericType(typeClass = TipoEntidade.ATRIBUTOGRADE)
public class AtributoGradeEcomDAO extends GenericDAO<AtributoGradeEcom> {

    private Connection conn;

    @Override
    public Class<AtributoGradeEcom> getClasseBean() {
        return AtributoGradeEcom.class;
    }
    
    @Override
    public void gravar(AtributoGradeEcom object) throws SQLException {
    }

    @Override
    public void alterar(AtributoGradeEcom object) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE ATRIBUTOGRADEECOM               "
                    + "SET DESCRICAO = ?,                           "
                    + "    IDATRIBUTOECOM = ?                       "
                    + "WHERE CODATRIBUTO = ?;                       ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getDescricao());
            pstmt.setInt(2, object.getIdAtributoEcom());
            pstmt.setString(3, object.getCodAtributo());
            pstmt.executeUpdate();
            Logger.getLogger(AtributoGradeEcomDAO.class).info("Atributo grade alterado com sucesso.");
        } catch (Exception e) {
            Logger.getLogger(AtributoGradeEcomDAO.class).error("Erro ao alterar atributo da grade: " + e);
        }
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Retorna atributo da grade através do codigo do atributo
     *
     * @param cod01
     * @return AtributoGradeEcom
     */
    @Override
    public AtributoGradeEcom abrir(String cod) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM ATRIBUTOGRADEECOM   "
                    + "WHERE CODATRIBUTO =?";
            pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY, 
				ResultSet.HOLD_CURSORS_OVER_COMMIT);
            pstmt.setString(1, cod);
            rs = pstmt.executeQuery();
            AtributoGradeEcom atributoGrade = null;
            while (rs.next()) {
                atributoGrade = new AtributoGradeEcom();
                atributoGrade.setCodAtributo(rs.getString("codatributo"));
                atributoGrade.setDescricao(rs.getString("descricao"));
                atributoGrade.setIdAtributoEcom(rs.getInt("idatributoecom"));
            }
            Logger.getLogger(AtributoGradeEcomDAO.class).info("Atributo grade retornado com sucesso.");
            return atributoGrade;
        } catch (Exception e) {
            Logger.getLogger(AtributoGradeEcomDAO.class).error("Erro ao retornar atributo grade: " + e);
            return null;
        } finally {
           // rs.close();
           // pstmt.close();
        }
    }

    @Override
    public List<AtributoGradeEcom> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<AtributoGradeEcom> listaAtributoGrade = new ArrayList<>();
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM ATRIBUTOGRADEECOM";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            AtributoGradeEcom atributoGrade = null;
            while (rs.next()) {
                atributoGrade = new AtributoGradeEcom();
                atributoGrade.setCodAtributo(rs.getString("codatributo"));
                atributoGrade.setDescricao(rs.getString("descricao"));
                atributoGrade.setIdAtributoEcom(rs.getInt("idatributoecom"));
                listaAtributoGrade.add(atributoGrade);
            }
            Logger.getLogger(AtributoGradeEcomDAO.class).info("Lista de atributo de grade retornada com sucesso.");
            return listaAtributoGrade;
        } catch (Exception e) {
            Logger.getLogger(AtributoGradeEcomDAO.class).error("Erro ao retornar lista de atributos da grade: " + e);
            return null;
        } finally {
            stmt.close();
            rs.close();
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
