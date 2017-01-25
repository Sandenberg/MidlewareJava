/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author kennedimalheiros
 */
public class TriggerTranspordadora implements ITrigger {

    private Logger logger = Logger.getLogger(TriggerTranspordadora.class);
    private Connection conn;

    @Override
    public void createTriggerAu() throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger transpt_ecomm_au for transpt\n" +
                             "active after update position 0\n" +
                             "AS                                                                                    \n" +
                             "DECLARE VARIABLE MCOD varchar50;\n" +
                             " begin\n" +
                             "   SELECT first 1 histintegecom.codentidade\n" +
                             "   FROM histintegecom\n" +
                             "   where histintegecom.codentidade = New.codtranspt\n" +
                             "   INTO :MCOD;\n" +
                             "    If((Old.INTEGRAECOM = 'S') and ((Old.IDTRANSPTECOM is not null) and (Old.IDTRANSPTECOM <> 0)) )\n" +
                             "       Then\n" +
                             "          Begin\n" +
                             "             INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)\n" +
                             "             VALUES ('transportadora', NEW.codtranspt, current_timestamp, NULL, 'update', 'Pendente');\n" +
                             "          End\n" +
                             "    Else                                                                                  \n" +
                             "       If(Old.INTEGRAECOM = 'S' and ((Old.IDTRANSPTECOM is null) and (Old.IDTRANSPTECOM = 0)) )\n" +
                             "          Then\n" +
                             "             Begin\n" +
                             "                If (MCOD IS NULL) then\n" +
                             "                   INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)\n" +
                             "                   VALUES ('transportadora', Old.codtranspt, current_timestamp, NULL, 'insert', 'Pendente');\n" +
                             "             END\n" +
                             " end                                    \n ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER UPDATE da Transportadora, criada com sucesso. ");
        } catch (Exception e) {
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
        try {
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger transpt_ecomm_ai for transpt                                                  \n "
                    + "active after insert position 0                                                                      \n "
                    + "AS                                                                                                  \n "
                    + "begin                                                                                               \n "
                    + "   if(New.INTEGRAECOM = 'S')then                                                                    \n "
                    + "      begin                                                                                         \n "
                    + "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)              \n "
                    + "         VALUES ('transportadora', NEW.codtranspt, current_timestamp, NULL, 'insert', 'Pendente');              \n "
                    + "   end                                                                                              \n "
                    + "end                                                                                                    ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER INSERT da Transportadora, criada com sucesso. ");
        } catch (Exception e) {
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
            String trigger = "CREATE OR ALTER trigger transpt_ecomm_ad for transpt                                                 \n " +
                             "active after delete position 0                                                              \n " +
                             "AS                                                                                          \n " +
                             "   begin                                                                                    \n " +
                             "      if((Old.INTEGRAECOM = 'S') and ((Old.IDTRANSPTECOM is not null) and (Old.IDTRANSPTECOM <> 0)))then                              \n " +                    
                             "         begin                                                                              \n " +
                             "            INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n " +
                             "            VALUES ('transportadora', Old.IDTRANSPTECOM, current_timestamp, NULL, 'delete', 'Pendente');\n " +
                             "      end                                                                                   \n " +
                             "end                                                                                            ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER DELETE da Transportadora, criada com sucesso. ");
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
