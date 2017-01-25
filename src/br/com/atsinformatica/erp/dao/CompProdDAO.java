package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ParacadBean;
import br.com.atsinformatica.erp.entity.ProdutoERPBean;
import br.com.atsinformatica.midler.annotation.GenericType;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
@GenericType(typeClass = TipoEntidade.COMPPROD)
public class CompProdDAO extends GenericDAO<ProdutoERPBean> {

    private Connection conn = null;

    @Override
    public Class<ProdutoERPBean> getClasseBean() {
        return ProdutoERPBean.class;
    }

    public double retornaEstoqueProd(String codProd) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ParaEcomDAO paraEcom = new ParaEcomDAO();
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT (compprod.estoque - compprod.quantbloqueada) AS estoquedisponivel  "
                    + "FROM compprod                                                             "
                    + "WHERE compprod.codprod = ? AND compprod.codempresa = ?                    ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codProd);
            pstmt.setString(2, paraEcom.listaTodos().get(0).getCodEmpresaEcom());
            rs = pstmt.executeQuery();
            double estoque = 0;
            while (rs.next()) {
                estoque = rs.getDouble("estoquedisponivel");
            }
            return estoque;
        } catch (Exception e) {
            Logger.getLogger(CompProdDAO.class).error("Erro ao retornar estoque do produto: " + e);
            return 0;
        } finally {
            rs.close();
            pstmt.close();
        }
    }

     @Override
    public ProdutoERPBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ParaEcomDAO paraEcom = new ParaEcomDAO();
        ParacaDAO paracaDAO = new ParacaDAO();
        try {
            System.out.println("Retornando produto");
            String codEcompresa = paraEcom.listaTodos().get(0).getCodEmpresaEcom();
            ParacadBean paracadBean = paracaDAO.listaTodos().get(0);
            conn = ConexaoATS.getConnection();
            String sql = "SELECT produto.codprod, (compprod.estoque - compprod.quantbloqueada) AS estoquedisponivel, "
                    + "produto.descricao, dadosadicecom.nomeprod, dadosadicecom.preco,                               "
                    + "dadosadicecom.codcategoria, dadosadicecom.condicao, dadosadicecom.codbarras,                  "
                    + "dadosadicecom.altura, dadosadicecom.largura, dadosadicecom.profundidade,                      "
                    + "dadosadicecom.codatributo1, dadosadicecom.codatributo2, dadosadicecom.palavraschave,          "
                    + "dadosadicecom.metadescricao, dadosadicecom.descricaocompleta, dadosadicecom.descricaobreve, produto.grade,                  "
                    + "produto.codfabric, produto.idprodutoecom, produto.peso, produto.ativo,                        "
                    + "dadosadicecom.mostranaloja, dadosadicecom.emoferta, produto.ALIQICMSREG00,                    "
                    + "produto.ALIQICMSREG01, produto.ALIQICMSREG02, produto.ALIQICMSREG03,                          "
                    + "produto.BASEICMSREG00, produto.BASEICMSREG01, produto.BASEICMSREG02, produto.BASEICMSREG03,    "
                    + "produto.CODTRIBUT00, produto.CODTRIBUT01, produto.CODTRIBUT02, produto.CODTRIBUT03,           "
                    + "produto.preco preco1, produto.preco2, produto.preco3, produto.preco4,                                "
                    + "produto.ALIQIPI, produto.reffabricante, produto.codfabric, produto.importaprodecom            "
                    + "FROM produto                                                                                  "
                    + "LEFT JOIN dadosadicecom on dadosadicecom.codprod = produto.codprod                            "
                    + "JOIN compprod ON produto.codprod = compprod.codprod                                           "
                    + "where produto.codprod = ?                                                                     "
                    + "AND compprod.codempresa = ?                                                                   ";
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
                        prodBean.setPrecoCheio(rs.getDouble("preco"));
                    else
                        prodBean.setPrecoCheio(rs.getDouble("preco"+paracadBean.getPrecoEcommerce()));                    
                }
            }
            Logger.getLogger(CompProdDAO.class).info("Produto retornado com sucesso.");
            return prodBean;
        } catch (Exception e) {
            Logger.getLogger(CompProdDAO.class).error("Erro ao retornar produto: " + e);
            return null;
        } 
    }
}
