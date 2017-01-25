/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ParacadBean;
import br.com.atsinformatica.erp.entity.ProdutoERPBean;
import br.com.atsinformatica.midler.annotation.GenericType;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.utils.Funcoes;
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
@GenericType(typeClass = TipoEntidade.PRODUTO)
public class ProdutoDAO extends GenericDAO<ProdutoERPBean> {

    private Logger logger = Logger.getLogger(ProdutoERPBean.class);
    private Connection conn;

    @Override
    public Class<ProdutoERPBean> getClasseBean() {
        return ProdutoERPBean.class;
    }

    @Override
    public void gravar(ProdutoERPBean object) throws SQLException {
        PreparedStatement pstmt = null;
        String codProd = Funcoes.preencheCom(object.getCodProd(), "0", 6, Funcoes.LEFT);
        try{
            //verifica se ja tem codigo do produto, se não, busca
            if(object.getCodProd().equals(""))
                codProd = retornaCodProdRefFabr(object.getRefFabricante());
            conn = ConexaoATS.getConnection();
           // conn.setAutoCommit(false);
            String querie = "INSERT INTO DADOSADICECOM (CODPROD, NOMEPROD, PRECO, EMOFERTA,                 "
                          + "CODCATEGORIA, CONDICAO, CODBARRAS, ALTURA, LARGURA, PROFUNDIDADE,              "
                          + "CODATRIBUTO1, CODATRIBUTO2, PALAVRASCHAVE, METADESCRICAO, DESCRICAOCOMPLETA,   "
                          + "DESCRICAOBREVE, MOSTRANALOJA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)          ";
            pstmt = conn.prepareStatement(querie);           
            pstmt.setString(1, codProd);
            pstmt.setString(2, object.getNomeProd());
            pstmt.setDouble(3, object.getPrecoCheio());
            pstmt.setString(4, object.getEmOferta());
            pstmt.setString(5, object.getCodCategoria());
            pstmt.setInt(6, object.getCondicao());
            pstmt.setString(7, object.getCodBarras());
            pstmt.setDouble(8, object.getAltura());
            pstmt.setDouble(9, object.getLargura());
            pstmt.setDouble(10,object.getProfundidade());
            pstmt.setString(11, object.getCodAtributo1());
            pstmt.setString(12, object.getCodAtributo2());
            pstmt.setString(13, object.getPalavrasChave());
            pstmt.setString(14, object.getMetaDescricao());
            pstmt.setString(15, object.getDescricaoCompleta());
            pstmt.setString(16, object.getDescricaoBreve());
            pstmt.setString(17, object.getMostraNaLoja());
            pstmt.executeUpdate();
            logger.info("Dados adicionais do produto "+ object.getNomeProd() + " gravado com sucesso.");
            //conn.commit();           
        }catch(Exception e){
             logger.error("Erro ao gravar dados adicionais do produto "+ object.getNomeProd());            
        }
    }
    
    
    public int getIdEcom(String codProd){
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       try{
           conn = ConexaoATS.getConnection();
           String sql = "SELECT IDPRODUTOECOM FROM PRODUTO WHERE CODPROD = ?";
           pstmt = conn.prepareCall(sql);
           pstmt.setString(1, sql);
           rs = pstmt.executeQuery();
           int idEcom = 0;
           if(rs.next()){
               idEcom = rs.getInt("IDPRODUTOECOM");
           }
           logger.info("Id do produto no e-commerce retornado com sucesso.");
           return idEcom;
       }catch(Exception e){
           logger.error("Erro ao retornar id do produto no e-commerce: "+e);
           return 0;
           
       } 
    }

    @Override
    public void alterar(ProdutoERPBean object) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
           // conn.setAutoCommit(false);
            String querie = "UPDATE produto set idprodutoecom = ?,"
                          + " importaprodecom = ?                "
                          + "where codprod = ?                    ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, object.getIdProdutoEcom());
            pstmt.setInt(2, object.getImportaProdEcom());
            pstmt.setString(3, object.getCodProd());
            pstmt.executeUpdate();
            //conn.commit();
            logger.info("Id do alterado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao alterar produto id do produto: " + e);
            //conn.rollback();
        }finally{
            pstmt.close();
        }
    }
    
    /**
     * Retorno código do produto no ERP pelo código de referência do fabricante
     * @param codRefFab código de referência do fabricante
     * @return String
     * @throws SQLException 
     */
    public String retornaCodProdRefFabr(String codRefFab) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String codProd = "";
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODPROD FROM PRODUTO                          "
                       + "WHERE REFFABRICANTE = ?                              ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codRefFab);
            rs = pstmt.executeQuery();
            if(rs.next())
                codProd = rs.getString("CODPROD");
            return codProd;
        }catch(Exception e){
            return null;            
        }finally{
            pstmt.close();
            rs.close();
        }
    }
    
    /**
     * Retorna id do ecommerce pelo código do fabricante
     * @param codRefFabricante código ref fabricante
     * @return int
     * @throws SQLException 
     */
    public int getIdEcomByRefFabricante(String codRefFabricante) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int idProdEcom = 0;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT IDPRODUTOECOM FROM PRODUTO                    "
                       + "WHERE REFFABRICANTE = ?                              ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codRefFabricante);
            rs = pstmt.executeQuery();
            if(rs.next())
                idProdEcom = rs.getInt("IDPRODUTOECOM");
            return idProdEcom;
        }catch(Exception e){
            return 0;            
        }finally{
            pstmt.close();
            rs.close();
        }
    }
    
    

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProdutoERPBean abrir(String id){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String codEcompresa = ParaEcomService.getCodEmpresaEcom();
            ParacadBean paracadBean = new ParacaDAO().listaTodos().get(0);
            
            conn = ConexaoATS.getConnection();
            String sql = 
                      "SELECT                                                                 "
                    + "    produto.codprod,                                                   "
                    + "    (compprod.estoque - compprod.quantbloqueada) AS estoquedisponivel, "
                    + "    produto.descricao,                                                 "
                    + "    dadosadicecom.nomeprod,                                            "
                    + "    dadosadicecom.preco,                                               "
                    + "    dadosadicecom.codcategoria,                                        "
                    + "    dadosadicecom.condicao,                                            "
                    + "    dadosadicecom.codbarras,                                           "
                    + "    dadosadicecom.altura,                                              "
                    + "    dadosadicecom.largura,                                             "
                    + "    dadosadicecom.profundidade,                                        "
                    + "    dadosadicecom.codatributo1,                                        "
                    + "    dadosadicecom.codatributo2,                                        "
                    + "    dadosadicecom.palavraschave,                                       "
                    + "    dadosadicecom.metadescricao,                                       "
                    + "    dadosadicecom.descricaocompleta,                                   "
                    + "    dadosadicecom.descricaobreve,                                      "
                    + "    dadosadicecom.mostranaloja,                                        "
                    + "    dadosadicecom.emoferta,                                            "
                    + "    produto.grade,                                                     "
                    + "    produto.codfabric,                                                 "
                    + "    produto.idprodutoecom,                                             "
                    + "    produto.peso,                                                      "
                    + "    produto.ativo,                                                     "
                    + "    produto.ALIQICMSREG00,                                             "
                    + "    produto.ALIQICMSREG01,                                             "
                    + "    produto.ALIQICMSREG02,                                             "
                    + "    produto.ALIQICMSREG03,                                             "
                    + "    produto.BASEICMSREG00,                                             "
                    + "    produto.BASEICMSREG01,                                             "
                    + "    produto.BASEICMSREG02,                                             "
                    + "    produto.BASEICMSREG03,                                             "
                    + "    produto.CODTRIBUT00,                                               "
                    + "    produto.CODTRIBUT01,                                               "
                    + "    produto.CODTRIBUT02,                                               "
                    + "    produto.CODTRIBUT03,                                               "
                    + "    produto.preco preco1,                                              "
                    + "    produto.preco2,                                                    "
                    + "    produto.preco3,                                                    "
                    + "    produto.preco4,                                                    "
                    + "    produto.ALIQIPI,                                                   "
                    + "    produto.reffabricante,                                             "
                    + "    produto.importaprodecom                                            "
                    + "FROM                                                                   "
                    + "    produto                                                            "
                    + "    LEFT JOIN dadosadicecom                                            "
                    + "      ON dadosadicecom.codprod = produto.codprod                       "
                    + "    JOIN compprod                                                      "
                    + "      ON produto.codprod = compprod.codprod                            "
                    + "WHERE                                                                  "
                    + "    produto.codprod = ?                                                "
                    + "    AND compprod.codempresa = ?                                        ";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, codEcompresa);
            rs = pstmt.executeQuery();
            
            ProdutoERPBean prodBean = null;
            while (rs.next()) {
                prodBean = new ProdutoERPBean(rs);                
                if(paracadBean.getPrecoEcommerce() > 0){                         
                    //retorna preço setado                    
                    if(paracadBean.getPrecoEcommerce()==1)
                        prodBean.setPrecoCheio(rs.getDouble("preco1"));
                    else
                        prodBean.setPrecoCheio(rs.getDouble("preco"+paracadBean.getPrecoEcommerce()));                    
                }
            }
            return prodBean;
        } catch (Exception e) {
            logger.error("Erro ao retornar produto", e);
            throw new RuntimeException(e);
        } 
    }
    
    /**
     * Retorna produto pelo código de referência do fabricante
     * @param id
     * @return
     * @throws SQLException 
     */
    public ProdutoERPBean abrirPorRefFabric(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ParaEcomDAO paraEcom = new ParaEcomDAO();
        try {
            String codEcompresa = paraEcom.listaTodos().get(0).getCodEmpresaEcom();
            conn = ConexaoATS.getConnection();
            String sql = "SELECT produto.codprod, produto.grade, (compprod.estoque - compprod.quantbloqueada) AS estoquedisponivel,     "
                    + "produto.codfabric, produto.idprodutoecom, produto.peso, produto.ativo,                                           "
                    + "produto.ALIQICMSREG00,                                                                                           "
                    + "produto.ALIQICMSREG01, produto.ALIQICMSREG02, produto.ALIQICMSREG03,                                             "
                    + "produto.BASEICMSREG00, produto.BASEICMSREG01, produto.BASEICMSREG02,produto.BASEICMSREG03,                       "
                    + "produto.CODTRIBUT00, produto.CODTRIBUT01, produto.CODTRIBUT02, produto.CODTRIBUT03,                              "
                    + "produto.ALIQIPI, produto.reffabricante, produto.importaprodecom                                                  "                   
                    + "FROM produto                                                                                                     "
                    + " JOIN compprod ON produto.codprod = compprod.codprod                                                             "
                    + "WHERE produto.reffabricante = ?                                                                                  "
                    + "AND CODEMPRESA = ?                                                                                               ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, codEcompresa);
            rs = pstmt.executeQuery();
            ProdutoERPBean prodBean = null;
            while (rs.next()) {
                prodBean = new ProdutoERPBean();
                prodBean.setCodProd(rs.getString("codprod"));
                prodBean.setIdProdutoEcom(rs.getInt("idprodutoecom"));
                prodBean.setGrade(rs.getInt("grade"));        
                prodBean.setPeso(rs.getDouble("peso"));
                prodBean.setAliqICM00(rs.getDouble("ALIQICMSREG00"));
                prodBean.setAliqICM01(rs.getDouble("ALIQICMSREG01"));
                prodBean.setAliqICM02(rs.getDouble("ALIQICMSREG02"));
                prodBean.setAliqICM03(rs.getDouble("ALIQICMSREG03"));
                prodBean.setBaseICM00(rs.getDouble("BASEICMSREG00"));
                prodBean.setBaseICM01(rs.getDouble("BASEICMSREG01"));
                prodBean.setBaseICM02(rs.getDouble("BASEICMSREG02"));
                prodBean.setBaseICM03(rs.getDouble("BASEICMSREG03"));
                prodBean.setCodTribut00(rs.getString("CODTRIBUT00"));
                prodBean.setCodTribut00(rs.getString("CODTRIBUT01"));
                prodBean.setCodTribut00(rs.getString("CODTRIBUT02"));
                prodBean.setCodTribut00(rs.getString("CODTRIBUT03"));
                prodBean.setAliqIPI(rs.getDouble("ALIQIPI"));
                prodBean.setRefFabricante(rs.getString("reffabricante"));
                prodBean.setImportaProdEcom(rs.getInt("importaprodecom"));
                prodBean.setEstoqueDisponivel(rs.getDouble("estoquedisponivel"));
            }
            logger.info("Produto retornado com sucesso.");
            return prodBean;
        } catch (Exception e) {
            logger.error("Erro ao retornar produto: " + e);
            return null;
        } finally {
          //  rs.close();
           // pstmt.close();
        }
    }

    public double retornaEstoqueProd(String codProd) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ParaEcomDAO paraEcom = new ParaEcomDAO();
        try {
            String codEcompresa = paraEcom.listaTodos().get(0).getCodEmpresaEcom();
            conn = ConexaoATS.getConnection();
            String sql = "SELECT (compprod.estoque - compprod.quantbloqueada) AS estoquedisponivel  "
                    + "FROM compprod                                                             "
                    + "WHERE compprod.codprod = ? AND compprod.codempresa = ?                    ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codProd);
            pstmt.setString(2, codEcompresa);
            rs = pstmt.executeQuery();
            double estoque = 0;
            while (rs.next()) {
                estoque = rs.getDouble("estoquedisponivel");
            }
            return estoque;
        } catch (Exception e) {
            logger.error("Erro ao retornar estoque do produto: " + e);
            return 0;
        }finally{
            rs.close();
            pstmt.close();
        }
    }
    
    
     public boolean produtoExiste(int idProdEcom){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean existe = false;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT IDPRODUTOECOM            "
                       + "FROM PRODUTO                    "
                       + "WHERE IDPRODUTOECOM = ?         ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProdEcom);
            rs = pstmt.executeQuery();
            existe = rs.next();
            logger.info("Código do produto ecom retornado com sucesso.");
            return existe;            
        }catch(Exception e){
            logger.error("Erro ao retornar código produto ecom: "+e);
            return false;           
        }
    }

    @Override
    public List<ProdutoERPBean> listaTodos() throws SQLException {
        List<ProdutoERPBean> listaProdutos = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //ParaEcomBean paraBean = new ParaEcomDAO().listaTodos().get(0);
            conn = ConexaoATS.getConnection();
            String sql = " SELECT  produto.codprod, (compprod.estoque - compprod.quantbloqueada) AS estoquedisponivel, produto.descricao, "
                    + " produto.descricao2, produto.descricao3, sub.descgrupo, sub.descsub, "
                    + " produto.referencia, produto.reffabricante, "
                    + "        produto.unidadeent, produto.unidadesaida, produto.preco, produto.preco2, produto.importaprodecom, produto.idprodutoecom, "
                    + "        produto.preco3, produto.preco4, compprod.precocusto, produto.grade,compprod.codgrade "
                    + " FROM produto, "
                    + " ( "
                    + "    select gruprod.codgrupo codgrupo, subgrup.codsubgrupo codsubgrupo,  gruprod.descricao descgrupo, "
                    + "    subgrup.descricao descsub from subgrup "
                    + "    join gruprod on gruprod.codgrupo = subgrup.codgrupo "
                    + " ) sub "
                    + " JOIN compprod ON produto.codprod = compprod.codprod "
                    + " WHERE  produto.codgrupo = sub.codgrupo and produto.codsubgrupo = sub.codsubgrupo ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProdutoERPBean bean = new ProdutoERPBean();
                bean.setCodProd(rs.getString("codprod"));
                bean.setIdProdutoEcom(rs.getInt("idprodutoecom"));
                bean.setGrade(rs.getInt("grade"));        
                bean.setRefFabricante(rs.getString("reffabricante"));
                bean.setImportaProdEcom(rs.getInt("importaprodecom"));
                bean.setCodGrade(rs.getString("codgrade"));               
                listaProdutos.add(bean);
            }
            logger.info("Lista de produtos a serem sincronizados, retornada com sucesso!");
            return listaProdutos;
        } catch (Exception e) {
            logger.error("Erro ao retornar lista de produtos a serem sincronizados: " + e);
            return null;
        } finally {
            rs.close();
            stmt.close();
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Atualiza status de importação do produto
     *
     * @param codProd código do produto
     * @param codStatus código do status do produto
     */
    public void atualizaStatusImportacao(String codProd, int codStatus) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            String querie = "update produto set importaproduto =? where codprod = ?";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, codStatus);
            pstmt.setString(2, codProd);
            pstmt.executeUpdate();
            conn.commit();
            logger.info("Status de importação do produto, atualizado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao atualizar status de importação do produto: " + e);
        } finally {
            pstmt.close();
        }

    }

    public String retornaCodProdutoERP(String codProdutoEcom){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT P.CODPROD FROM PRODUTO P WHERE P.IDPRODUTOECOM = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codProdutoEcom);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getString("CODPROD") != null) {
                return rs.getString("CODPROD");
            }
            return null;
        } catch (Exception e) {
            logger.error("Erro ao retornar código do produto pelo id do ecom: ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    public String retornaUnidadeSaidaProdutoERP(String codProdutoERP) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT P.UNIDADESAIDA FROM PRODUTO P WHERE P.CODPROD = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codProdutoERP);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getString("UNIDADESAIDA") != null) {
                return rs.getString("UNIDADESAIDA");
            }
            return null;
        } catch (Exception e) {
            logger.error("Erro ao retornaUnidadeSaidaProdutoERP do produto", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }
    
    /**
     * Atualiza status de operação de integração
     *
     * @param codProd código do produto
     * @param codStatus código do status de integração
     */
    public void atualizaStatusSincEcom(String codProd, int codStatus) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            //conn.setAutoCommit(false);
            String querie = "update produto set statussincecom =? where codprod = ?";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, codStatus);
            pstmt.setString(2, codProd);
            pstmt.executeUpdate();
            //conn.commit();
            logger.info("Status de integração, atualizado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao atualizar status integração do produto: " + e);
        } finally {
            pstmt.close();
        }

    }
}
