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
public class TriggerGrade implements ITrigger {

    private Connection conn;

    @Override
    public void createTriggerAu() throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger gradeecom_au for grade                                       \n"
                           + "active after update position 0                                                       \n"
                           + "AS                                                                                   \n"
                           + "begin                                                                                \n"
                           + "    if((OLd.idgradeecom <> 0) or(OLd.idgradeecom is not null)) then                              \n"
                           + "       INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS) \n"
                           + "       VALUES ('grade', New.codgrade, current_timestamp, NULL, 'update', 'Pendente');            \n"
                           + "end                                                                                    ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerAtributoGrade.class).info("Trigger AFTER UPDATE de grade, criada com sucesso. ");
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
    }

    @Override
    public void createTriggerAd() throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger gradeecom_ad for grade                                        \n" 
                           + "active after delete position 0                                                        \n"
                           + "AS                                                                                    \n"
                           + "begin                                                                                 \n"
                           + "    if((OLd.idgradeecom <> 0) or(OLd.idgradeecom is not null)) then                               \n"
                           + "       INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)  \n"
                           + "       VALUES ('grade', Old.idgradeecom, current_timestamp, NULL, 'delete', 'Pendente');          \n"
                           + "end                                                                                     ";
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
