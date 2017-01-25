package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.CadFabricERPBean;
import br.com.atsinformatica.midler.annotation.GenericType;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
@GenericType(typeClass = TipoEntidade.FABRICANTE)
public class CadFabrERPDAO extends GenericDAO<CadFabricERPBean> {
    private Connection conn;

    @Override
    public Class<CadFabricERPBean> getClasseBean() {        
        return CadFabricERPBean.class;
    }

    @Override
    public void gravar(CadFabricERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Altera fabricante na loja virtual
     * @param object dados do fabricante
     * @throws SQLException 
     */
    @Override
    public void alterar(CadFabricERPBean object) throws SQLException {
        PreparedStatement pstmt = null;        
        try{
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE CADFABR                             " +
                            "SET DESCRICAO = ?,                         " +
                            "    CGCCPF = ?,                            " +
                            "    IDFABRICANTEECOM = ?                   " +
                            "WHERE (CODFABRIC = ?)                      " ;
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getDescricao());
            pstmt.setString(2, object.getCgCpf());
            pstmt.setInt(3, object.getIdFabricanteEcom());
            pstmt.setString(4, object.getCodFabric());
            pstmt.executeUpdate();
            Logger.getLogger(CadFabricERPBean.class).info("Fabricante alterado com sucesso.");
        }catch(Exception e){
            Logger.getLogger(CadFabricERPBean.class).error("Erro ao alterar fabricante: "+e);            
        }finally{
            pstmt.close();
        }
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Retorna fabricante na loja virtual
     * @param id id do fabricante
     * @return fabricante na loja virtual
     * @throws SQLException 
     */
    @Override
    public CadFabricERPBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM CADFABR             "
                        +"WHERE CODFABRIC = ?               ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            CadFabricERPBean cadFabric = null;
            if(rs.next()){
                cadFabric = new CadFabricERPBean();
                cadFabric.setCodFabric(rs.getString("CODFABRIC"));
                cadFabric.setDescricao(rs.getString("DESCRICAO"));
                cadFabric.setIdFabricanteEcom(rs.getInt("IDFABRICANTEECOM"));                
            }
            Logger.getLogger(CadFabrERPDAO.class).info("Fabricante retornado com sucesso");
            return cadFabric;
        }catch(Exception e){
            Logger.getLogger(CadFabrERPDAO.class).error("Erro ao retornar fabricante: "+e);
            return null;            
        }finally{
           // pstmt.close();
           // rs.close();
        }
    }

    @Override
    public List<CadFabricERPBean> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
