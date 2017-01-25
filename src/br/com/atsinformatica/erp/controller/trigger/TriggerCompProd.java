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
public class TriggerCompProd implements ITrigger {
     private Logger logger = Logger.getLogger(TriggerProduto.class);
     private Connection conn;
     
  
    @Override
    public void createTriggerAu() throws SQLException {
       PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger compprod_ecomm_au for compprod\n" +
                             "active after update position 0\n" +
                             "AS declare variable                                                                          \n" +
                             "integra integer;\n" +
                             "DECLARE VARIABLE\n" +
                             "MCOD varchar50;                                                                             \n" +
                             "DECLARE VARIABLE\n" +
                             "idprodecom integer;\n" +
                             "   Begin\n" +
                             "   --verificar se não existe integração pendente\n" +
                             "   -- gerar integração apenas se não tiver\n" +
                             "   SELECT first 1 histintegecom.codentidade\n" +
                             "   FROM histintegecom\n" +
                             "   where histintegecom.codentidade = New.codprod\n" +
                             "   and histintegecom.status = 'Pendente'\n" +
                             "   into MCOD;\n" +
                             "      Select produto.importaprodecom from produto                                            \n" +
                             "      where produto.codprod = New.codprod into integra;                                      \n" +
                             "      Select produto.idprodutoecom from produto                                              \n" +
                             "      where produto.codprod = New.codprod into idprodecom;                                   \n" +
                             "   if(MCOD is Null) Then\n" +
                             "      Begin\n" +
                             "      If(integra = 1 And idprodecom <> 0) Then                                                  \n" +
                             "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)\n" +
                             "         VALUES ('compprod', New.codprod, current_timestamp, NULL, 'update', 'Pendente');\n" +
                             "   end\n" +
                             "end";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER UPDATE de movimentações, criada com sucesso. ");
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
