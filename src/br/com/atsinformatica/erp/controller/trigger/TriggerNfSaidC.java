/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller.trigger;

import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class TriggerNfSaidC implements ITrigger {
    private Connection conn;
    private static Logger logger = Logger.getLogger(TriggerNfSaidC.class);

    @Override
    public void createTriggerAu() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createTriggerAi() throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String qryNfsaidC = 
                    "CREATE OR ALTER trigger nfsaidcecom_ai for nfsaidc\n" +
                    "active after insert position 0\n" +
                    "AS\n" +
                    "declare variable vtipopedido varchar2;\n" +
                    "declare variable vidpedidoecom inteiro;\n" +
                    "declare variable vcodpedido varchar8;\n" +
                    "begin\n" +
                    "     Select pc.tipopedido,pc.idpedidoecom, pc.codpedido\n" +
                    "     from pedidoc\n" +
                    "     inner join pedidoc as pc on pc.codpedido = pedidoc.codpedidoimportado\n" +
                    "                             and pc.tipopedido = pedidoc.tipopedidoimportado and pedidoc.codempresa = pc.codempresa\n" +
                    "     where pedidoc.codpedido = New.codpedido\n" +
                    "       and pedidoc.tipopedido = New.tipopedido\n" +
                    "       and pedidoc.codempresa = New.codempresa\n" +
                    "     into vtipopedido, vidpedidoecom, vcodpedido;\n" +
                    "\n" +
                    "     if  (((vidpedidoecom <> 0) AND (vidpedidoecom is Not null)) AND (vtipopedido = '55')) Then\n" +
                    "        INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)  \n" +
                    "        VALUES ('statusnfsaidecom', :vcodpedido, current_timestamp, NULL, 'insert', 'Pendente');\n" +
                    "end";
            pstmt = conn.prepareStatement(qryNfsaidC);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER INSERT da nota fiscal, criada com sucesso.");
        }catch(Exception e){
            logger.error("Erro ao criar trigger", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {
            }
        }
    }

    @Override
    public void createTriggerAd() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
