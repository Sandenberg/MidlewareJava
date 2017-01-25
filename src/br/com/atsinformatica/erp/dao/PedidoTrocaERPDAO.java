package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.PedidocTrocaERPBean;
import br.com.atsinformatica.midler.annotation.GenericType;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author AlexsanderPimenta
 */
@GenericType(typeClass = TipoEntidade.PEDIDOTROCA)
public class PedidoTrocaERPDAO extends GenericDAO<PedidocTrocaERPBean> {
    private Connection conn;

    @Override
    public Class<PedidocTrocaERPBean> getClasseBean() {
        return PedidocTrocaERPBean.class;
    }
    
    /**
     * Retorna dados do pedido de troca
     * @param id do pedido no erp
     * @return PedidoCBean
     * @throws SQLException 
     */
    @Override
    public PedidocTrocaERPBean abrir(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        ParaEcomDAO paraEcomDAO = new ParaEcomDAO();
        try{
            conn = ConexaoATS.getConnection();
            String sql = " SELECT CODPEDIDOVENDA, CODPEDIDOORIGINAL, CODPEDIDOTROCA           "
                       + " FROM PEDIDOC                                                       "
                       + " WHERE CODPEDIDO = ? AND CODEMPRESA = ?                             ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, paraEcomDAO.listaTodos().get(0).getCodEmpresaEcom());
            rs = pstmt.executeQuery();
            PedidocTrocaERPBean pedidocTrocaERPBean = null;
            while(rs.next()){
                pedidocTrocaERPBean = new PedidocTrocaERPBean();
                pedidocTrocaERPBean.setCodPedidoOriginal(rs.getString("codpedidooriginal"));
                pedidocTrocaERPBean.setCodPedidoTroca(rs.getString("codpedidotroca"));
                pedidocTrocaERPBean.setCodPedidoVenda(rs.getString("codpedidovenda"));
            }
            return pedidocTrocaERPBean;
        }catch(Exception e){
            return null;       
        }finally{
            rs.close();
            pstmt.close();
        }
    }
    
    
}
