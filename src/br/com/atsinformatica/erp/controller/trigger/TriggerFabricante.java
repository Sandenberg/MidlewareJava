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
public class TriggerFabricante implements ITrigger {
    private Logger logger = Logger.getLogger(TriggerProduto.class);
    private Connection conn;
    
    
    @Override
    public void createTriggerAu() throws SQLException {
       PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger fabricante_ecomm_au for cadfabr       \n " +
                             "active after update position 0                       \n " +
                             "AS                                                   \n " +
                             "begin                                                \n " +
                             "if ((Old.idfabricanteecom <> 0) or \n"
                           + "(Old.idfabricanteecom is not null)) then                  \n " +
                             "    INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE,\n " +
                             "    DATAENT, DATAINT, TIPOOPER, STATUS)                      \n " +
                             "    VALUES ('fabricante', New.codfabric,             \n " +
                             "    current_timestamp, NULL, 'update', 'Pendente');              \n " +
                             "end                                                     ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER UPDATE de fabricante, criada com sucesso. ");
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
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger fabricante_ecomm_ai for cadfabr       \n " +
                             "active after insert position 0                       \n " +
                             "AS                                                   \n " +
                             "begin                                                \n " +
                             "    INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE,\n " +
                             "    DATAENT, DATAINT, TIPOOPER, STATUS)                      \n " +
                             "    VALUES ('fabricante', New.codfabric,             \n " +
                             "    current_timestamp, NULL, 'insert', 'Pendente');              \n " +
                             "end                                                     ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER INSERT de fabricante, criada com sucesso. ");
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
         PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger fabricante_ecomm_ad for cadfabr       \n " +
                             "active after delete position 0                       \n " +
                             "AS                                                   \n " +
                             "begin                                                \n " +
                             "if ((Old.idfabricanteecom <> 0) or \n"
                           + "(Old.idfabricanteecom is not null)) then                  \n " +
                             "    INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE,\n " +
                             "    DATAENT, DATAINT, TIPOOPER, STATUS)                      \n " +
                             "    VALUES ('fabricante', Old.idfabricanteecom,      \n " +
                             "    current_timestamp, NULL, 'delete', 'Pendente');              \n " +
                             "end                                                     ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER DELETE de fabricante, criada com sucesso. ");
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
    
}
