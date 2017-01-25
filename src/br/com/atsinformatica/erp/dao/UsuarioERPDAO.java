/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;
import br.com.atsinformatica.erp.entity.UsuarioERPBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class UsuarioERPDAO implements IGenericDAO<UsuarioERPBean> {
    private static final Logger logger = Logger.getLogger(UsuarioERPDAO.class);
    public static final String ADMIN_USUARIO = "ADMININTEGRADOR";
    public static final String ADMIN_SENHA = "ats#2015";
    
    private Connection conn;

    @Override
    public void gravar(UsuarioERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(UsuarioERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UsuarioERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UsuarioERPBean> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean validarUsuario(UsuarioERPBean usuarioERPBean){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            if (conn == null) 
                return false;
            
            String sql = " SELECT U.USUARIO, U.SENHA FROM CADUSUA U "
                    + "   WHERE U.USUARIO = ? "
                    + "     AND U.SENHA = ? ";
            pstmt = conn.prepareStatement(sql);            
            
            if (usuarioERPBean.getSenha().length() <= 24) {
                pstmt.setString(1, usuarioERPBean.getUsuario());
                pstmt.setString(2, usuarioERPBean.getSenha());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                logger.error("Erro na validação de login do usuario: Senha não pode ser maior que 8 caracteres");
                return false;
            }            
        } catch (Exception e) {
            logger.error("Erro na validação de login do usuario: " + e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception ex) {} // Não precisa fazer nada
            try {
                rs.close();
            } catch (Exception e) {}
        }
    }
    
    /**
     * Valida se usuário que esta logando, tem permissão de gerente
     * @param usuario
     * @return
     * @throws SQLException 
     */
    public boolean validaPermissaoUsuario(String usuario) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean temPermissao = false;
        try {
            
            conn = ConexaoATS.getConnection();
            String sql = " SELECT GERENTE FROM CADUSUA "
                    + "    WHERE USUARIO = ? AND ATIVO = 'S' ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario);                        
            rs = pstmt.executeQuery();
            while(rs.next()){
               temPermissao = rs.getString("GERENTE").equals("S");
            }
            return temPermissao;
        } catch (SQLException e) {
            logger.error("Erro na validação da permissão do usuario: " + e);
            return false;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

}
