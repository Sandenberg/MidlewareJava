package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.TransptERPBean;
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
 * @author kennedimalheiros
 */
@GenericType(typeClass = TipoEntidade.TRANSPORTADORA)
public class TransptERPDAO extends GenericDAO<TransptERPBean>{

    private Logger logger = Logger.getLogger(TransptERPDAO.class);
    private Connection conn;

    @Override
    public Class<TransptERPBean> getClasseBean() {
        return TransptERPBean.class;
    }
    
    @Override
    public void gravar(TransptERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(TransptERPBean object) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE TRANSPT "
                          + "SET IDTRANSPTECOM = ? "
                          + "WHERE CODTRANSPT  = ?  ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, Integer.valueOf(object.getIdEcom()));
            pstmt.setString(2, object.getIdERP());
            pstmt.executeUpdate();
            logger.info("Transportadora Cod ERP: " + object.getIdERP() + " alterada com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao altera Transportadora Cod ERP: " + object.getIdERP() + ": " + e);

        } finally {
            pstmt.close();
        }
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TransptERPBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = " SELECT * FROM TRANSPT "
                    + " WHERE CODTRANSPT = ?  ";
            pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY, 
				ResultSet.HOLD_CURSORS_OVER_COMMIT);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            TransptERPBean transptERPBean = null;
            while (rs.next()) {
                transptERPBean = new TransptERPBean();
                transptERPBean.setIdERP(rs.getString("CODTRANSPT"));
                transptERPBean.setNome(rs.getString("NOME"));
                transptERPBean.setIdEcom(rs.getString("IDTRANSPTECOM"));
            }
            logger.info("Transportadora retornado com sucesso ");
            return transptERPBean;
        } catch (Exception e) {
            logger.error("Erro ao retorna Transportadoras: " + e);
            return null;
        } finally {
           // pstmt.close();
           // rs.close();
        }

    }

    @Override
    public List<TransptERPBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<TransptERPBean> listaTransp = new ArrayList<>();
        try{            
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODTRANSPT, NOME           "
                       + "FROM TRANSPT                      " ;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            TransptERPBean transPt = null;
            while(rs.next()){
                transPt = new TransptERPBean();
                transPt.setIdERP(rs.getString("CODTRANSPT"));
                transPt.setNome(rs.getString("NOME"));
                listaTransp.add(transPt);
            }
            logger.info("Transportadoras, retornada com sucesso.");
            return listaTransp;           
        }catch(Exception e){
            logger.error("Erro ao retornar transportadoras: "+e);
            return null;
        }finally{
            rs.close();
            stmt.close();
            
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
