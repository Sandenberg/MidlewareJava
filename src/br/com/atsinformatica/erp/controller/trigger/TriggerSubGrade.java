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
public class TriggerSubGrade implements ITrigger {
    
    private Connection conn;

    @Override
    public void createTriggerAu() throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String trigger = "  CREATE OR ALTER trigger subgradeecom_au for subgrade                            \n " +
                             "  active after update position 0                                                  \n " +
                             "  AS                                                                              \n " +
                             "  begin                                                                           \n " +
                             "  if((OLd.idsubgradeecom is not null)or(OLd.idsubgradeecom <> 0)) then                                                \n " +
                             "  INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n " +
                             "  VALUES ('subgrade', New.codsubgrade, current_timestamp, NULL, 'update', 'Pendente');        \n " +
                             "  end                                                                                " ;
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerAtributoGrade.class).info("Trigger AFTER UPDATE de subgrade, criada com sucesso. ");
        } catch (Exception e) {
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
    public void createTriggerAi() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createTriggerAd() throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String trigger = "   CREATE OR ALTER trigger subgradeecom_ad for subgrade                                 \n " 
                    + "          active after delete position 0                                                       \n "
                    + "          AS                                                                                   \n "
                    + "          begin                                                                                \n "
                    + "            if((OLd.idsubgradeecom is not null)or(OLd.idsubgradeecom <> 0)) then                            \n " 
                    + "          INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)        \n "
                    + "          VALUES ('subgrade', Old.idsubgradeecom, current_timestamp, NULL, 'delete', 'Pendente');          \n "
                    + "          end                                                                                     ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerAtributoGrade.class).info("Trigger AFTER DELETE de grade, criada com sucesso. ");
        } catch (Exception e) {
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
