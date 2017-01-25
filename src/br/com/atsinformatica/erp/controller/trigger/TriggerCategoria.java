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
public class TriggerCategoria implements ITrigger {
   private Logger logger = Logger.getLogger(TriggerProduto.class);
   private Connection conn;
     
    /**
     * Cria trigger AFTER UPDATE
     */
   @Override
   public void createTriggerAu() throws SQLException{
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger categoria_ecomm_au for categoriasecom            \n " +
                             "active after update position 0                                           \n " +
                             "AS                                                                       \n " +
                             "begin                                                                    \n " +
                             "  if (Old.idcategoriaecom <> 0) then                                     \n " +
                             "      INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT,         \n " +                    
                             "      DATAINT, TIPOOPER, STATUS)                                         \n " +
                             "      VALUES ('categoria', NEW.codcategoria, current_timestamp,          \n "
                           + "      NULL, 'update', 'Pendente');                                       \n " +
                             "end                                                                 ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER UPDATE de categoria, criada com sucesso. ");
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
    
    /**
     * Cria trigger AFTER UPDATE
     */
   @Override
    public void createTriggerAi() throws SQLException{
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger categoria_ecomm_ai for categoriasecom         \n " +
                             "active after insert position 0                               \n " +
                             "AS                                                           \n " +
                             "begin                                                        \n " +
                             "   INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE,         \n " +
                             "   DATAENT, DATAINT, TIPOOPER, STATUS)                       \n " +
                             "   VALUES ('categoria', NEW.codcategoria,                    \n " +
                             "   current_timestamp, NULL, 'insert', 'Pendente');           \n " +
                             "end                                                     ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER INSERT de categoria, criada com sucesso. ");
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
        
    /**
     * Cria trigger AFTER DELETE
     */
   @Override
    public void createTriggerAd() throws SQLException{
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger categoria_ecomm_ad for categoriasecom                \n " +
                             "active after delete position 0                                      \n " +
                             "AS                                                                  \n " +
                             "begin                                                               \n " +
                             "If (Old.idcategoriaecom <> 0) then                                  \n " +
                             "   INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE,                \n " +
                             "   DATAENT, DATAINT, TIPOOPER, STATUS)                              \n " +
                             "   VALUES ('categoria',Old.idcategoriaecom, current_timestamp,        \n " +
                             "   NULL, 'delete', 'Pendente');                                     \n " +
                             "end                                                            ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER DELETE de categoria, criada com sucesso. ");
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
