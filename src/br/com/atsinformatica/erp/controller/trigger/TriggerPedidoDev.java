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
public class TriggerPedidoDev implements ITrigger {
    private Connection conn;

    @Override
    public void createTriggerAu() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createTriggerAi() throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String querie = "CREATE OR ALTER trigger pedidocecom_ai_dev for pedidoc                                         \n" +
                            "active after insert position 0                                                                 \n" +
                            "AS                                                                                             \n" +
                            "declare variable                                                                               \n" +
                            "idpedidoecom integer;                                                                          \n" +
                            "declare variable                                                                               \n" +
                            "devvenda char;                                                                                 \n" +
                            "begin                                                                                          \n" +
                            "  Select first 1 pedidoc.idpedidoecom from pedidoc                                                     \n" +
                            "  where  pedidoc.codpedido  = New.codpedidodevolucao                                           \n" +
                            "  and    pedidoc.codempresa = New.codempresa                                                   \n" +
                            "  into idpedidoecom;                                                                           \n" +
                            "  Select first 1 cdoperc.devvenda from pedidoc                                                         \n" +
                            "  join   cdoperc on cdoperc.codoper = New.codoper                                              \n" +
                            "  where  pedidoc.codempresa = New.codempresa                                                   \n" +
                            "  into devvenda;                                                                               \n" +
                            "  --verifica se pedido original é da loja virtual--                                            \n" +
                            "  If (idpedidoecom <> 0 ) Then                                                                 \n" +
                            "     Begin                                                                                     \n" +
                            "     --Pedido de devolução---                                                                  \n" +
                            "     If (devvenda = 'S') Then                                                                  \n" +
                            "        Begin                                                                                  \n" +
                            "        INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)  \n" +
                            "        VALUES ('pedidodev', New.codpedido, current_timestamp, NULL, 'insert', 'Pendente');    \n" +
                            "     End                                                                                       \n" +
                            "  End                                                                                          \n" +
                            "                                                                                               \n" +
                            "end                                                                                            \n" ;
            pstmt = conn.prepareStatement(querie);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerPedidoDev.class).info("Trigger pedidocecom_ai_dev criada com sucesso.");
        }catch(Exception e){
            Logger.getLogger(this.getClass()).error("Erro ao criar trigger", e);
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
