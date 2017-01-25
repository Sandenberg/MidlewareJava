/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.controller.ConnectionFactory;
import br.com.atsinformatica.erp.entity.SenhaInstalacaoERPBean;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AlexsanderPimenta
 */
public class SenhaInstalacaoDAO extends ConnectionFactory implements IGenericDAO<SenhaInstalacaoERPBean> {
    
    public SenhaInstalacaoDAO(){
        super();
    }
   
    @Override
    public void gravar(SenhaInstalacaoERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(SenhaInstalacaoERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SenhaInstalacaoERPBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            String sql = "Select codempresa, senha, codcliente from senhainstalacao "
                       + "where codempresa = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            SenhaInstalacaoERPBean senhaInstalacao = null;
            while(rs.next()){
                senhaInstalacao = new SenhaInstalacaoERPBean();
                senhaInstalacao.setCodCliente(rs.getString("codcliente"));
                byte[] bsenha = rs.getBlob("senha").getBytes(1, (int)rs.getBlob("senha").length());
                String senha = new String(bsenha);
                senhaInstalacao.setSenha(senha);
                senhaInstalacao.setCodEmpresa(rs.getString("codempresa"));
            }
            return senhaInstalacao;
        }catch(Exception e){
            return null;
        }finally{
            pstmt.close();
            rs.close();           
        }
    }

    @Override
    public List<SenhaInstalacaoERPBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<SenhaInstalacaoERPBean> listaSenhas = new ArrayList<>();        
        try{
            String sql = "Select codempresa, senha, codcliente from senhainstalacao";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                SenhaInstalacaoERPBean bean = new SenhaInstalacaoERPBean();
                bean.setCodEmpresa(rs.getString("codempresa"));                
                byte[] bsenha = rs.getBlob("senha").getBytes(1, (int)rs.getBlob("senha").length());
                String senha = new String(bsenha);
                bean.setSenha(senha);
                bean.setCodCliente(rs.getString("codcliente"));
                listaSenhas.add(bean);
            }
            return listaSenhas;
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
