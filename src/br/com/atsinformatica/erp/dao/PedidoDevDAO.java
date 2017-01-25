package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.PedidoDevERPBean;
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
@GenericType(typeClass = TipoEntidade.PEDIDOTROCA)
public class PedidoDevDAO extends GenericDAO<PedidoDevERPBean> {
    private Connection conn;

    @Override
    public Class<PedidoDevERPBean> getClasseBean() {
        return PedidoDevERPBean.class;
    }
    
    @Override
    public PedidoDevERPBean abrir(String id) throws SQLException {
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       ParaEcomDAO paraEcomDAO = new ParaEcomDAO(); 
       PedidoDevERPBean pedidoDev = null;
       try{           
           conn = ConexaoATS.getConnection();
           String codEmpresa = paraEcomDAO.listaTodos().get(0).getCodEmpresaEcom();
           String sql = "SELECT CODPEDIDO, CODPEDIDODEVOLUCAO        "
                      + "From PEDIDOC                                "
                      + "WHERE CODPEDIDO = ? AND TIPOPEDIDO = '51'   "
                      + "AND CODEMPRESA = ?                          ";
           pstmt = conn.prepareStatement(sql);
           pstmt.setString(1, id);
           pstmt.setString(2, codEmpresa);
           rs = pstmt.executeQuery();
           while(rs.next()){
               pedidoDev = new PedidoDevERPBean();
               pedidoDev.setCodPedido(rs.getString("CODPEDIDO"));
               pedidoDev.setCodPedidoDevolucao(rs.getString("CODPEDIDODEVOLUCAO"));
           }
           Logger.getLogger(PedidoDevDAO.class).info("Pedido de devolução retornado com sucesso");
           return pedidoDev;        
       }catch(Exception e){
           Logger.getLogger(PedidoDevDAO.class).error("Erro ao retornar pedido de devolução: "+e);
           return null;           
       }finally{
           pstmt.close();
           rs.close();
       }
    }
    
    
    
}
