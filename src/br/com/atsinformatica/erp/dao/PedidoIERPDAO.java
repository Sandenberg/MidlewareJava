package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ParaFatBean;
import br.com.atsinformatica.erp.entity.PedidoIERPBean;
import br.com.atsinformatica.erp.entity.ProdutoERPBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class PedidoIERPDAO implements IGenericDAO<PedidoIERPBean> {

    private static Logger logger = Logger.getLogger(PedidoIERPDAO.class);
    private Connection conn;

    @Override
    public void gravar(PedidoIERPBean pedidoIERPBean){
        PreparedStatement pstmt = null;
        try {
            PedidoIERPBean tributPedido = retornaTribProd(pedidoIERPBean);
            conn = ConexaoATS.getConnection();
            String sql = " INSERT INTO PEDIDOI "
                    + "                    (CODEMPRESA, TIPOPEDIDO, CODPEDIDO, CODCLIENTE, "
                    + "                     CODPROD, CODGRADE, QUANTIDADE, PRECOUNIT, UNIDADESAIDA, "
                    + "                     TOTALITEM, PESO, ALIQICM, ALIQIPI, BASEICM, CODTRIBUT ) "
                    + "              VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pedidoIERPBean.getCodEmpresa());
            pstmt.setString(2, "55");
            pstmt.setString(3, pedidoIERPBean.getCodPedido());
            pstmt.setString(4, pedidoIERPBean.getCodClienteERP());
            pstmt.setString(5, pedidoIERPBean.getCodProdERP());
            pstmt.setString(6, pedidoIERPBean.getCodGradERP());
            pstmt.setDouble(7, pedidoIERPBean.getQuantidade());
            pstmt.setDouble(8, pedidoIERPBean.getPrecoUnit());
            pstmt.setString(9, pedidoIERPBean.getUnidadeSaida());
            pstmt.setDouble(10, pedidoIERPBean.getTotalItem());
            pstmt.setDouble(11, pedidoIERPBean.getPeso());
            pstmt.setDouble(12, tributPedido.getAliqIcm());
            pstmt.setDouble(13, tributPedido.getAliqIpi());
            pstmt.setDouble(14, tributPedido.getBaseIcm());
            pstmt.setString(15, tributPedido.getCodTribut());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao gravar item do pedido", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    /**
     * Retorna tributação
     *
     * @param codProd
     * @param uf
     * @return
     * @throws SQLException
     */
    public PedidoIERPBean retornaTribProd(PedidoIERPBean pedidoIErpBean) {
        ProdutoDAO prodDao = new ProdutoDAO();
        ParaFATDAO parafatDao = new ParaFATDAO();
        try {
            ProdutoERPBean produto = prodDao.abrir(pedidoIErpBean.getCodProdERP());
            ParaFatBean parafat = parafatDao.retornaTipoRegiao();
            if (produto != null) {
               // pedidoIErpBean = new PedidoIERPBean();
                pedidoIErpBean.setAliqIpi(produto.getAliqIPI());
                pedidoIErpBean.setAliqIcm(produto.getAliqICM00());
                pedidoIErpBean.setBaseIcm(produto.getBaseICM00());
                pedidoIErpBean.setCodTribut(produto.getCodTribut00());
                if (parafat != null) {
                    if (parafat.getRegiao01().contains(pedidoIErpBean.getUf())) {
                        pedidoIErpBean.setAliqIcm(produto.getAliqICM01());
                        pedidoIErpBean.setBaseIcm(produto.getBaseICM01());
                        pedidoIErpBean.setCodTribut(produto.getCodTribut01());
                    }
                    if (parafat.getRegiao02().contains(pedidoIErpBean.getUf())) {
                        pedidoIErpBean.setAliqIcm(produto.getAliqICM02());
                        pedidoIErpBean.setBaseIcm(produto.getBaseICM02());
                        pedidoIErpBean.setCodTribut(produto.getCodTribut02());
                    }
                    if (parafat.getRegiao03().contains(pedidoIErpBean.getUf())) {
                        pedidoIErpBean.setAliqIcm(produto.getAliqICM03());
                        pedidoIErpBean.setBaseIcm(produto.getBaseICM03());
                        pedidoIErpBean.setCodTribut(produto.getCodTribut03());
                    }
                }
            }
            return pedidoIErpBean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void alterar(PedidoIERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PedidoIERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PedidoIERPBean> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Retorna lista de itens do pedido
     * @param codPedido código do pedido
     * @param codEmpresa código da empresa
     * @return List<PedidoIERPBean>
     * @throws SQLException 
     */
    public List<PedidoIERPBean> listaItensPedido(String codPedido, String codEmpresa) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PedidoIERPBean> listaItensPedido = new ArrayList<>();
        try{
           conn = ConexaoATS.getConnection();
           String sql = "SELECT CODPROD, CODGRADE, CODEMPRESA, TIPOPEDIDO   "
                      + "CODPEDIDO, PRECOUNIT, QUANTIDADE                   "
                      + "FROM PEDIDOI                                       "
                      + "WHERE CODPEDIDO = ? AND CODEMPRESA = ?             ";
           pstmt = conn.prepareStatement(sql);
           pstmt.setString(1, codPedido);
           pstmt.setString(2, codEmpresa);
           rs = pstmt.executeQuery();
           PedidoIERPBean pedidoI = null;
           while(rs.next()){
               pedidoI = new PedidoIERPBean();
               pedidoI.setCodEmpresa(rs.getString("CODEMPRESA"));
               pedidoI.setQuantidade(rs.getDouble("QUANTIDADE"));
               pedidoI.setCodPedido(rs.getString("CODPEDIDO"));
               pedidoI.setPrecoUnit(rs.getDouble("PRECOUNIT"));
               pedidoI.setCodGradERP(rs.getString("CODGRADE"));
               pedidoI.setCodProdERP(rs.getString("CODPROD"));
               listaItensPedido.add(pedidoI);
           }
           logger.info("Lista de itens do pedido retornada com sucesso.");
           return listaItensPedido;
        }catch(Exception e){
           throw new RuntimeException("Erro ao retornar lista de itens do pedido: ", e);
        }finally{
            try {
                pstmt.close();
            } catch (Exception e) {
            }
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }
}
