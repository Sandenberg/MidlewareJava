/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.TipoVendaBean;
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
public class TipoVendaDAO implements IGenericDAO<TipoVendaBean> {
    private Connection conn;
    private Logger logger = Logger.getLogger(TipoVendaDAO.class);

    @Override
    public void gravar(TipoVendaBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(TipoVendaBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TipoVendaBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Lista todos tipos de venda cadastrados
     * @return
     * @throws SQLException 
     */
    @Override
    public List<TipoVendaBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<TipoVendaBean> listaTipoVendas = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODTVENDA, DESCRICAO FROM TIPOVENDA";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                TipoVendaBean tipoVenda = new TipoVendaBean();
                tipoVenda.setCodTVenda(rs.getString("CODTVENDA"));
                tipoVenda.setDescricao(rs.getString("DESCRICAO"));
                listaTipoVendas.add(tipoVenda);
            }
            logger.info("Tipos de venda retornado com sucesso.");
            return listaTipoVendas;
        }catch(Exception e){
            logger.error("Erro ao retornar tipo de vendas: "+e);            
            return null;
        }finally{
           // stmt.close();
           // rs.close();
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
