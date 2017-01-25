package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.CategoriaEcomErpBean;
import br.com.atsinformatica.midler.annotation.GenericType;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.utils.Funcoes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
@GenericType(typeClass = TipoEntidade.CATEGORIA)
public class CategoriaEcomDAO extends GenericDAO<CategoriaEcomErpBean>  {
    private Connection conn;
    private static Logger logger = Logger.getLogger(CategoriaEcomDAO.class);

    @Override
    public Class<CategoriaEcomErpBean> getClasseBean() {
        return CategoriaEcomErpBean.class;
    }
    
    @Override
    public void gravar(CategoriaEcomErpBean object) throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String querie = "INSERT INTO CATEGORIASECOM (CODCATEGORIA, DESCRICAO, PRINCIPAL,                 "
                          + "CODCATEGORIASUPERIOR, DESCRICAODETALHADA, DESCRICAOCOMPLETA,                    "
                          + "MOSTRANALOJA, ORDEM, IDCATEGORIAECOM)                                           "
                          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);                                             ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, ultimoRegistro());
            pstmt.setString(2, object.getDescricao());
            pstmt.setString(3, object.getPrincipal());
            pstmt.setString(4, object.getCodCategoriaSuperior());
            pstmt.setString(5, object.getDescricaoDetalhada());
            pstmt.setString(6, object.getDescricaoCompleta());
            pstmt.setString(7, object.getMostranaLoja());
            pstmt.setString(8, object.getOrdem());
            pstmt.setInt(9, object.getIdCategoriaEcom());
            pstmt.executeUpdate();
            logger.info("Categoria cadastrada com sucesso.");
        }catch(Exception e){
            logger.error("Erro ao cadastrar categoria: "+e);            
        }finally{
            pstmt.close();
        }
       
    }

    @Override
    public void alterar(CategoriaEcomErpBean object) throws SQLException {
         PreparedStatement pstmt = null;        
        try{
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE CATEGORIASECOM " +
                            "SET DESCRICAO = ?, " +
                            "    PRINCIPAL = ?, " +
                            "    CODCATEGORIASUPERIOR = ?, " +
                            "    DESCRICAODETALHADA = ?, " +
                            "    DESCRICAOCOMPLETA = ?, " +
                            "    IDCATEGORIAECOM = ? " +
                            "WHERE (CODCATEGORIA = ?) ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getDescricao());
            pstmt.setString(2, object.getPrincipal());
            pstmt.setString(3, object.getCodCategoriaSuperior());
            pstmt.setString(4, object.getDescricaoDetalhada());
            pstmt.setString(5, object.getDescricaoCompleta());
            pstmt.setInt(6, object.getIdCategoriaEcom());
            pstmt.setString(7, object.getCodCategoria());
            pstmt.executeUpdate();
            logger.info("Categoria alterada com sucesso.");
        }catch(Exception e){
            logger.error("Erro ao altera categoria: "+e);        
        }finally{
            pstmt.close();
        }
    }
     
    public void alteraIdEcom(CategoriaEcomErpBean object) {
         PreparedStatement pstmt = null;        
        try{
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE CATEGORIASECOM " +
                            "SET IDCATEGORIAECOM = ? " +
                            "WHERE (CODCATEGORIA = ?) ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, object.getIdCategoriaEcom());
            pstmt.setString(2, object.getCodCategoria());
            pstmt.executeUpdate();
            logger.info("Id da categoria da loja virtual gravado com sucesso.");
        }catch(Exception e){
            logger.error("Erro ao gravar id da categoria da loja virtual: "+e);
            throw new RuntimeException(e);
        }finally{
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Retorna uma categoria através do codigo da mesma
     * @param id
     * @return categoria
     * @throws SQLException 
     */
    @Override
    public CategoriaEcomErpBean abrir(String id) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM CATEGORIASECOM "
                    + "WHERE CODCATEGORIA = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            CategoriaEcomErpBean categoria = null;
            while(rs.next()){
                categoria = new CategoriaEcomErpBean();
                categoria.setCodCategoria(rs.getString("CODCATEGORIA").trim());
                categoria.setCodCategoriaSuperior(rs.getString("CODCATEGORIASUPERIOR"));
                categoria.setDescricao(rs.getString("DESCRICAO"));
                categoria.setPrincipal(rs.getString("PRINCIPAL"));
                categoria.setDescricaoDetalhada(rs.getString("DESCRICAODETALHADA"));
                categoria.setDescricaoCompleta(rs.getString("DESCRICAOCOMPLETA"));
                categoria.setIdCategoriaEcom(rs.getInt("IDCATEGORIAECOM")); 
                categoria.setMostranaLoja(rs.getString("MOSTRANALOJA"));
            }
            logger.info("Categoria retornada com sucesso.");
            return categoria;
        }catch(Exception e){
            logger.error("Erro ao retornar categoria: "+e);
            return null;            
        }finally{
            //rs.close();
           // pstmt.close();
        }
    }
    
    /**
     * Retorna uma categoria através do codigo da mesma
     * @param id
     * @return categoria
     * @throws SQLException 
     */
    public List<CategoriaEcomErpBean> retornaCatSemSuperior() throws SQLException {
        ResultSet rs = null;
        Statement stmt = null;
        List<CategoriaEcomErpBean> listaCategorias = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM CATEGORIASECOM   "
                       + "WHERE CODCATEGORIASUPERIOR = 0 ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                CategoriaEcomErpBean categoria = new CategoriaEcomErpBean();
                categoria.setCodCategoria(rs.getString("CODCATEGORIA").trim());
                categoria.setCodCategoriaSuperior(rs.getString("CODCATEGORIASUPERIOR"));
                categoria.setDescricao(rs.getString("DESCRICAO"));
                categoria.setPrincipal(rs.getString("PRINCIPAL"));
                categoria.setDescricaoDetalhada(rs.getString("DESCRICAODETALHADA"));
                categoria.setDescricaoCompleta(rs.getString("DESCRICAOCOMPLETA"));
                categoria.setIdCategoriaEcom(rs.getInt("IDCATEGORIAECOM")); 
                categoria.setMostranaLoja(rs.getString("MOSTRANALOJA"));
                listaCategorias.add(categoria);
            }
            logger.info("Categoria retornada com sucesso.");
            return listaCategorias;
        }catch(Exception e){
            logger.error("Erro ao retornar categoria: "+e);
            return null;            
        }finally{
            rs.close();
            stmt.close();
        }
    }

    /**
     * Lista todas categorias cadastrados
     * @return lista de categorias
     * @throws SQLException 
     */
    @Override
    public List<CategoriaEcomErpBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select * from categoriasecom";
            List<CategoriaEcomErpBean> listaCategorias = new ArrayList<>();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY,
                                ResultSet.HOLD_CURSORS_OVER_COMMIT);
            rs = stmt.executeQuery(sql);
            CategoriaEcomErpBean categoria = null;
            while(rs.next()){
                categoria = new CategoriaEcomErpBean();
                categoria.setCodCategoria(rs.getString("CODCATEGORIA"));
                categoria.setCodCategoriaSuperior(rs.getString("CODCATEGORIASUPERIOR"));
                categoria.setDescricao(rs.getString("DESCRICAO"));
                categoria.setPrincipal(rs.getString("PRINCIPAL"));
                categoria.setDescricaoDetalhada(rs.getString("DESCRICAODETALHADA"));
                categoria.setDescricaoCompleta(rs.getString("DESCRICAOCOMPLETA"));
                categoria.setIdCategoriaEcom(rs.getInt("IDCATEGORIAECOM"));
                categoria.setMostranaLoja(rs.getString("MOSTRANALOJA"));                
                listaCategorias.add(categoria);                
            }
            logger.info("Categorias retornadas com sucesso.");
            return listaCategorias;
        }catch(Exception e){
            logger.error("Erro ao retornar categorias: "+e);
            return null;            
        }finally{
            rs.close();
            stmt.close();
        }
    }
    
    /**
     * Retorna todas categorias pai de uma categoria
     * @param cod
     * @return List<CategoriaEcomErpBean>
     */
    public List<CategoriaEcomErpBean> retornaCategoriasPai(String cod){
        List<CategoriaEcomErpBean> lista = new ArrayList<>();
        CategoriaEcomErpBean catEcom = null;
        try{            
            catEcom = this.abrir(cod);
            if(!catEcom.getCodCategoriaSuperior().equals("0")){
                lista.addAll(retornaCategoriasPai(catEcom.getCodCategoriaSuperior()));
            }
            lista.add(catEcom);           
            return lista;
        }catch(Exception e){
            return null;
            
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "select max(CODCATEGORIA) cod from CATEGORIASECOM";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            int cod = 0;
            while (rs.next()) {
                if (rs.getString("cod") != null) {
                    cod = Integer.valueOf(rs.getString("cod")) + 1;
                }
            }
            if(cod==0){
                cod = cod+1;
            }
            return Funcoes.preencheCom(Integer.toString(cod), "0", 4, Funcoes.LEFT);
        } catch (Exception e) {
            logger.error("ERRO ao Consultar ID ERP do ultimo cadastrada Cadastrado: " + e);
            return null;
        } finally {
            stmt.close();
            rs.close();
        }
    }
    
    public String retornaCodCategoria(int idCatEcom){
        String codCategoriaSuperior = "0";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODCATEGORIA             "
                       + "FROM CATEGORIASECOM             "
                       + "WHERE IDCATEGORIAECOM = ?       ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idCatEcom);
            rs = pstmt.executeQuery();
            if(rs.next()){
                codCategoriaSuperior = rs.getString("CODCATEGORIA");
            }
            logger.info("Código da categoria retornado com sucesso.");
            return codCategoriaSuperior;            
        }catch(Exception e){
            logger.error("Erro ao retornar código da categoria: "+e);
            return null;           
        }finally{
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException | NullPointerException ex) {
                java.util.logging.Logger.getLogger(CategoriaEcomDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
    }
    
    public boolean categoriaExiste(int idCatEcom){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean existe = false;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODCATEGORIA             "
                       + "FROM CATEGORIASECOM             "
                       + "WHERE IDCATEGORIAECOM = ?       ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idCatEcom);
            rs = pstmt.executeQuery();
            existe = rs.next();
            logger.info("Código da categoria retornado com sucesso.");
            return existe;            
        }catch(Exception e){
            logger.error("Erro ao retornar código da categoria: "+e);
            return false;           
        }finally{
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException | NullPointerException ex) {
                java.util.logging.Logger.getLogger(CategoriaEcomDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
    }
    
}
