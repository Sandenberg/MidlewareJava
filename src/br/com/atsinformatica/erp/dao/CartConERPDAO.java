package br.com.atsinformatica.erp.dao;

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
public class CartConERPDAO {
    private Connection conn;
    private static final Logger logger = Logger.getLogger(CartConERPDAO.class);
    
    
    /**
     * Retorna código co convênio
     * @param formaPagamento forma de pagamento
     * @param parcelas parcelas
     * @return
     * @throws SQLException 
     */
    public String retornaCodConvenio(int formaPagamento, int parcelas) throws SQLException{
        String codConvenio = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select codigo from cartcon "
                       + "where formapagamentoecom = ? "
                       + "and numeroparcelaecom =?   ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, formaPagamento);
            pstmt.setInt(2, parcelas);
            rs = pstmt.executeQuery();
            while(rs.next()){
                codConvenio = rs.getString("codigo");
            }
            logger.info("Código do convênio, retornado com sucesso.");
            return codConvenio;
        }catch(Exception e){
            logger.error("Erro ao retornar código do convênio: "+e);            
            return null;
        }finally{
            rs.close();
            pstmt.close();
        }
    }
    
}
