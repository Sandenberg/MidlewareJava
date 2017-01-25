/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.BancoERPBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
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
public class BancoERPDAO implements IGenericDAO<BancoERPBean> {
    private Connection conn;
    private static final Logger logger = Logger.getLogger(BancoERPDAO.class);
    

    @Override
    public void gravar(BancoERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(BancoERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BancoERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BancoERPBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<BancoERPBean> listaBancos = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select codbanco, nomebanco "
                       + "from bancos                ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                BancoERPBean banco = new BancoERPBean();
                banco.setCodBanco(rs.getString("codbanco"));
                banco.setNomeBanco(rs.getString("nomebanco"));
                listaBancos.add(banco);
            }
            logger.info("Bancos retornados com sucesso.");
            return listaBancos;
        }catch(Exception e){
            logger.error("Erro ao retornar bancos: "+e);
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
