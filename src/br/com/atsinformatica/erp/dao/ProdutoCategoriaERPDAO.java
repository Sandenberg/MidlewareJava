package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.controller.ConnectionFactory;
import br.com.atsinformatica.erp.entity.ProdutoCategoriaERPBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
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
public class ProdutoCategoriaERPDAO extends ConnectionFactory implements IGenericDAO<ProdutoCategoriaERPBean> {
    private static final Logger logger = Logger.getLogger(ProdGradeERPDAO.class);
   

    @Override
    public void gravar(ProdutoCategoriaERPBean object) throws SQLException {
        PreparedStatement pstmt = null;        
        try{
            conn = ConexaoATS.getConnection();
            String querie = "INSERT INTO CATEGORIASPRODUTOECOM (CODPROD, CODCATEGORIA) VALUES (?, ?)";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getCodProd());
            pstmt.setString(2, object.getCodCategoria());
            pstmt.executeUpdate();
            logger.info("Categorias do produto salvas com sucesso.");          
        }catch(Exception e){
            logger.error("Erro ao salvar categorias do produto: "+e);            
        }finally{
            pstmt.close();
        }
    }

    @Override
    public void alterar(ProdutoCategoriaERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProdutoCategoriaERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ProdutoCategoriaERPDAO() {
        super();
    }

    @Override
    public List<ProdutoCategoriaERPBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<ProdutoCategoriaERPBean> listaProdutoCategorias = new ArrayList<>();
        try{
            String sql = " select codprod, codcategoria from categoriasprodutoecom ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ProdutoCategoriaERPBean produtoCategoria = null;
            while(rs.next()){
                produtoCategoria = new ProdutoCategoriaERPBean();
                produtoCategoria.setCodProd(rs.getString("codprod"));
                produtoCategoria.setCodCategoria(rs.getString("codcategoria"));
                listaProdutoCategorias.add(produtoCategoria);                
            }            
            logger.info("Lista de categorias relacionadas ao produto, retornada com sucesso!");
            return listaProdutoCategorias;
        }catch(Exception e){
            logger.error("Erro ao retornar lista de categorias relacionadas ao produto: "+e);
            return null;
        }finally{
            stmt.close();
            rs.close();
        }
    }
    
    /**
     * Lista categorias por produto
     * @param codProd Código do produto
     * @return List<ProdutoCategoriaERPBean> 
     * @throws SQLException 
     */
    public List<ProdutoCategoriaERPBean> listaCategoriasPorProduto(String codProd) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ProdutoCategoriaERPBean> listaProdutoCategorias = new ArrayList<>();
        try{
            String sql = " select codprod, codcategoria from categoriasprodutoecom "
                       + " where codprod = ?                                       ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codProd);
            rs = pstmt.executeQuery();
            ProdutoCategoriaERPBean produtoCategoria = null;
            while(rs.next()){
                produtoCategoria = new ProdutoCategoriaERPBean();
                produtoCategoria.setCodProd(rs.getString("codprod"));
                produtoCategoria.setCodCategoria(rs.getString("codcategoria"));
                listaProdutoCategorias.add(produtoCategoria);                
            }            
            logger.info("Lista de categorias por produto, retornada com sucesso!");
            return listaProdutoCategorias;
        }catch(Exception e){
            logger.error("Erro ao retornar lista de categorias relacionadas por produto: "+e);
            return null;
        }finally{
            pstmt.close();
            rs.close();
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
