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
public class TriggerControleEntrega implements ITrigger {
    private Logger logger = Logger.getLogger(TriggerControleEntrega.class);
    private Connection conn;

    @Override
    public void createTriggerAu() throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String qryControleEntrega = 
                    "CREATE OR ALTER trigger controleentregaecom_au for controleentrega                                     \n" +
                    "active after update position 0                                                                         \n" +
                    "AS                                                                                                     \n" +
                    "declare variable                                                                                       \n" +
                    "idpedidoecom integer;                                                                                  \n" +
                    "begin                                                                                                  \n" +
                    "   SELECT first 1 p.idpedidoecom FROM pedidoc p                                                        \n" +
                    "   WHERE p.codpedido = Old.codpedido AND p.codempresa = Old.codempresa                                 \n" +
                    "   AND p.tipopedido = Old.tipopedido INTO idpedidoecom;                                                \n" +
                    "   if (Old.tipopedido = '55' AND idpedidoecom IS NOT NULL AND idpedidoecom <> 0) then                  \n" +
                    "      INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)            \n" +
                    "      VALUES ('statuscontroleentrega', Old.codpedido, current_timestamp, NULL, 'update', 'Pendente');  \n" +
                    "end                                                                                                    \n";
            pstmt = conn.prepareStatement(qryControleEntrega);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER UPDATE do controle de entrega, criada com sucesso.");
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
    public void createTriggerAi() throws SQLException {
        
    }

    @Override
    public void createTriggerAd() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
