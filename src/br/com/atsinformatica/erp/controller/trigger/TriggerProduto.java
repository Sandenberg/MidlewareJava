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
public class TriggerProduto implements ITrigger {
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
            String trigger = "CREATE OR ALTER trigger produto_ecomm_au for produto\n" +
                             "active after update position 0\n" +
                             "AS                                                                                    \n" +
                             "DECLARE VARIABLE MCOD varchar50;\n" +
                             "Begin\n" +
                             "   SELECT first 1 histintegecom.codentidade\n" +
                             "   FROM histintegecom\n" +
                             "   where histintegecom.codentidade = New.codprod\n" +
                             "   INTO :MCOD;\n" +
                             "   If((Old.importaprodecom = 1)\n" +
                             "      and ((old.idprodutoecom is not null)and(old.idprodutoecom <> 0))  )\n" +
                             "      Then\n" +
                             "         Begin\n" +
                             "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)\n" +
                             "         VALUES ('produto', Old.codprod, current_timestamp, NULL, 'update', 'Pendente');\n" +
                             "   End\n" +
                             "    If ((New.importaprodecom = 1)  and ((Old.idprodutoecom = 0) or (Old.idprodutoecom is null)))\n" +
                             "      Then\n" +
                             "         Begin\n" +
                             "         If (MCOD IS NULL) then\n" +
                             "            INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)\n" +
                             "            VALUES ('produto', Old.codprod, current_timestamp, NULL, 'insert', 'Pendente');\n" +
                             "\n" +
                             "   End\n" +
                             "End";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER UPDATE do produto, criada com sucesso. ");
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
            String trigger = "CREATE OR ALTER trigger produto_ecomm_ai for produto                                           \n " +
                             "active after insert position 0                                                        \n " +
                             "AS                                                                                    \n " +
                             "begin                                                                                 \n " +
                             "if(New.importaprodecom = 1 )then                                                      \n " +
                             "   begin                                                                              \n " +
                             "      INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n " +
                             "      VALUES ('produto', NEW.codprod, current_timestamp, NULL, 'insert', 'Pendente');             \n " +
                             "   end                                                                                \n " +
                             "end                                                                                      ";
            
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER INSERT do produto, criada com sucesso. ");
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
            String trigger = "CREATE OR ALTER trigger produto_ecomm_ad for produto                                           \n" +
                             "active after delete position 0                                                        \n" +
                             "AS                                                                                    \n" +
                             "begin                                                                                 \n" +
                             "  if(Old.importaprodecom = 1)then                                                     \n" +
                             "     begin                                                                            \n" +
                             "       INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)  \n" +
                             "       VALUES ('produto', Old.idprodutoecom, current_timestamp, NULL, 'delete', 'Pendente');      \n" +
                             "     end                                                                              \n" +
                             "end                                                                                   ";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER DELETE do produto, criada com sucesso. ");
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
