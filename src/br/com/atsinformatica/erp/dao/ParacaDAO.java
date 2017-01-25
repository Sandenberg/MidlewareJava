package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ParacadBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AlexsanderPimenta
 */
public class ParacaDAO implements IGenericDAO<ParacadBean> {
    
    private Connection conn;

    @Override
    public void gravar(ParacadBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(ParacadBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ParacadBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Retorna parametros do erp
     * @return lista de parametros
     * @throws SQLException 
     */
    @Override
    public List<ParacadBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<ParacadBean> parametros = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select habilitaecommerce, precoecommerce from paracad";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                ParacadBean paraCad = new ParacadBean();
                paraCad.setHabilitaEcommerce(rs.getString("habilitaecommerce"));
                paraCad.setPrecoEcommerce(rs.getInt("precoecommerce"));
                parametros.add(paraCad);
            }
           return parametros;
        }catch(Exception e){
           return null;            
        }finally{
            stmt.close();
            rs.close();            
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
