package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ComplementoPedidoEcomBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import org.apache.log4j.Logger;
import java.sql.Connection;
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
public class ComplementoPedidoEcomDAO implements IGenericDAO<ComplementoPedidoEcomBean> {
    private Connection conn;
    private static final Logger logger = Logger.getLogger(ComplementoPedidoEcomDAO.class);

    @Override
    public void gravar(ComplementoPedidoEcomBean object) {
        PreparedStatement pstmt = null;        
        try{
            conn = ConexaoATS.getConnection();
            String querie = "INSERT INTO COMPLEMENTOPEDIDOECOM (CODEMPRESA, TIPOPEDIDO,CODPEDIDO, IDPEDIDOECOM, FORMAPAGAMENTO, "
                          + "CODTRANSACAO, DATAPAGAMENTO, VALORPAGO, REFERENCIAPEDIDO, CODBANCO,         "
                          + "AGENCIA, CONTA, CODPRAZO, CODCONVENIO, QTDEPARCELAECOM                                     ) "
                          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?                                ) ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getCodEmpresa());
            pstmt.setString(2, object.getTipoPedido());
            pstmt.setString(3, object.getCodErp());
            pstmt.setInt(4, object.getIdPedidoEcom());
            pstmt.setInt(5, object.getFormaPagamento());
            pstmt.setString(6, object.getCodTransacao());
            pstmt.setDate(7, new java.sql.Date(object.getDataPagamento().getTime()));
            pstmt.setDouble(8, object.getValorPago());
            pstmt.setString(9, object.getCodReferencia());
            pstmt.setString(10, object.getCodBanco());
            pstmt.setString(11, object.getAgencia());
            pstmt.setString(12, object.getConta());
            pstmt.setString(13, object.getCodPrazo());
            pstmt.setString(14, object.getCodConvenio());
            pstmt.setInt(15, object.getQtdeParcelaEcom());
            pstmt.executeUpdate();
        }catch(Exception e){
            logger.error("Erro ao gravar dados do complemento do pedido e-commerce", e);
            throw new RuntimeException(e);
        }finally{
            try {
                pstmt.close();
            } catch (Exception e) {
                logger.error("Erro ao fechar PreparedStatement", e);  
            }
        }            
    }

    
    @Override
    public void alterar(ComplementoPedidoEcomBean object) throws SQLException {
        PreparedStatement pstmt = null;        
        try{
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE COMPLEMENTOPEDIDOECOM "
                          + "SET IDPEDIDOECOM = ?,        "
                          + "   FORMAPAGAMENTO = ?,       "
                          + "   CODTRANSACAO = ?,         "
                          + "   DATAPAGAMENTO = ?,        "
                          + "   VALORPAGO = ?,            "
                          + "   REFERENCIAPEDIDO = ?,     "
                          + "   PAGONALOJA  = ?,          "
                          + "   CODBANCO = ?,             "
                          + "   AGENCIA = ?,              "
                          + "   CONTA = ?,                "
                          + "   CODPRAZO = ?,             "
                          + "   CODCONVENIO  = ?,         "
                          + "   CODEMPRESA = ?,           "
                          + "   TIPOPEDIDO = ?,           "
                          + "   QTDEPARCELAECOM = ?       "
                          + "WHERE (CODPEDIDO = ?)        ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setInt(1, object.getIdPedidoEcom());
            pstmt.setInt(2, object.getFormaPagamento());
            pstmt.setString(3, object.getCodTransacao());
            pstmt.setDate(4, new java.sql.Date(object.getDataPagamento().getTime()));
            pstmt.setDouble(5, object.getValorPago());
            pstmt.setString(6, object.getCodReferencia());
            pstmt.setString(7, "S");
            pstmt.setString(8, object.getCodBanco());
            pstmt.setString(9, object.getAgencia());
            pstmt.setString(10,object.getConta());
            pstmt.setString(11,object.getCodPrazo());
            pstmt.setString(12,object.getCodConvenio());
            pstmt.setString(13,object.getCodEmpresa());
            pstmt.setString(14,object.getTipoPedido());
            pstmt.setInt(15, object.getQtdeParcelaEcom());
            pstmt.setString(16, object.getCodErp());            
            pstmt.executeUpdate();
            logger.info("Dados do complemento do pedido do e-commerce, salvos com sucesso!");
        }catch(Exception e){
            logger.error("Erro ao gravar dados do complemento do pedido e-commerce: "+e);
        }finally{
            pstmt.close();
        }
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComplementoPedidoEcomBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT codpedido, idpedidoecom, formapagamento,    " +
                         "       codtransacao, datapagamento, valorpago,     " +
                         "       referenciapedido                            " +
                         "from complementopedidoecom                         " +
                         "where codpedido = ?                                ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            ComplementoPedidoEcomBean complPedidoEcom = null;
            while(rs.next()){
                complPedidoEcom = new ComplementoPedidoEcomBean();
                complPedidoEcom.setCodErp(rs.getString("codpedido"));
                complPedidoEcom.setIdPedidoEcom(rs.getInt("idpedidoecom"));
                complPedidoEcom.setFormaPagamento(rs.getInt("formapagamento"));
                complPedidoEcom.setDataPagamento(rs.getDate("datapagamento"));
                complPedidoEcom.setValorPago(rs.getDouble("valorpago"));
                complPedidoEcom.setCodTransacao(rs.getString("codtransacao"));
                complPedidoEcom.setCodReferencia(rs.getString("referenciapedido"));
            }
            logger.info("Dados complementares do pedido do e-commerce, retornado com sucesso");
            return complPedidoEcom;
        }catch(Exception e){
            logger.error("Erro ao retornar dados complementares do pedido do e-commerce: "+e);
            return null;
            
        }finally{
            pstmt.close();
            rs.close();
        }
    }

   
    @Override
    public List<ComplementoPedidoEcomBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        List<ComplementoPedidoEcomBean> listaPedidosPago = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT codpedido, idpedidoecom, formapagamento,    " +
                         "       codtransacao, datapagamento, valorpago,     " +
                         "       referenciapedido                            " +
                         "from complementopedidoecom                         " +
                         "where pagonaloja = 'S'                             "
                       + "order by codpedido desc                            ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                ComplementoPedidoEcomBean complPedidoEcom = new ComplementoPedidoEcomBean();
                complPedidoEcom.setCodErp(rs.getString("codpedido"));
                complPedidoEcom.setIdPedidoEcom(rs.getInt("idpedidoecom"));
                complPedidoEcom.setFormaPagamento(rs.getInt("formapagamento"));
                complPedidoEcom.setDataPagamento(rs.getDate("datapagamento"));
                complPedidoEcom.setValorPago(rs.getDouble("valorpago"));
                complPedidoEcom.setCodTransacao(rs.getString("codtransacao"));
                complPedidoEcom.setCodReferencia(rs.getString("referenciapedido"));
                listaPedidosPago.add(complPedidoEcom);
            }
            logger.info("Lista de pedidos pagos na loja virtual, retornada com sucesso.");
            return listaPedidosPago;
        }catch(Exception e){
            logger.error("Erro ao retornar lista de pedidos pagos na loja virtual: "+e);
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
