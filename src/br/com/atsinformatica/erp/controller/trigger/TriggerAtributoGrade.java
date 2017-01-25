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
public class TriggerAtributoGrade implements ITrigger {
    
    private Connection conn;

     /**
     * Cria trigger AFTER UPDATE
     */
   @Override
   public void createTriggerAi() throws SQLException{
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger atributogradeecom_ai for atributogradeecom                                   \n " +
                             "active after insert position 0                                                              \n " +
                             "AS                                                                                          \n " +
                             "begin                                                                                       \n " +
                             "  INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)     \n " +
                             "  VALUES ('atributograde', NEW.codatributo, current_timestamp, NULL, 'insert','Pendente');  \n " +
                             "end                                                                                    ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerAtributoGrade.class).info("Trigger AFTER INSERT de atributo grade, criada com sucesso. ");
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
    
    /**
     * Cria trigger AFTER UPDATE
     */
   @Override
    public void createTriggerAu() throws SQLException{
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger atributogradeecom_au for atributogradeecom                         \n " +
                             "active after update position 0                                                    \n " +
                             "AS                                                                                \n " +
                             "begin                                                                             \n " +
                             "  if (Old.idatributoecom <> 0) then                                                 \n " +
                             "    INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS) \n " +
                             "    VALUES ('atributograde', NEW.codatributo, current_timestamp, NULL, 'update', 'Pendente'); \n " +
                             "end                                                                                  ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerAtributoGrade.class).info("Trigger AFTER UPDATE de atributo grade, criada com sucesso. ");
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
        
    /**
     * Cria trigger AFTER DELETE
     */
   @Override
    public void createTriggerAd() throws SQLException{
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger atributogradeecom_ad for atributogradeecom                                        \n " +
                             "active after delete position 0                                                                   \n " +
                             "AS                                                                                               \n " +
                             "begin                                                                                            \n " +
                             "   If(old.idatributoecom <> 0) Then                                                              \n " +
                             "     INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)       \n " +
                             "     VALUES ('atributograde', Old.idatributoecom, current_timestamp, NULL, 'delete', 'Pendente');\n " +
                             "end                                                                                       ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerAtributoGrade.class).info("Trigger AFTER DELETE de atributo grade, criada com sucesso. ");
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
    
}
