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
public class SugestVendasTrigger  implements ITrigger{
    
    private Connection conn;

    @Override
    public void createTriggerAi() throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger sugestvendas_ecomm_ai for sugestvendas\n" +
                             "active after insert position 0\n" +
                             "AS\n" +
                             "declare variable idprodecom1 inteiro;\n" +
                             "declare variable idprodecom2 inteiro;\n" +
                             "begin\n" +
                             "  Select produto.idprodutoecom from produto                                              \n" +
                             "      where produto.codprod = New.codprod1 into idprodecom1;\n" +
                             "  Select produto.idprodutoecom from produto                                              \n" +
                             "      where produto.codprod = New.codprod2 into idprodecom2;\n" +
                             "  if ((idprodecom1<>0) and (idprodecom2<>0)) then\n" +
                             "     INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)\n" +
                             "         VALUES ('prodagregado', :idprodecom1 || '-' || :idprodecom2 , current_timestamp, NULL, 'insert', 'Pendente');\n" +
                             "end";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerAtributoGrade.class).info("Trigger AFTER INSERT da sugestão de vendas, criada com sucesso. ");
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
    public void createTriggerAu() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createTriggerAd() throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String trigger = "CREATE OR ALTER trigger sugestvendas_ecomm_ad for sugestvendas\n" +
                             "active after delete position 0\n" +
                             "AS\n" +
                             "declare variable idprodecom1 inteiro;\n" +
                             "declare variable idprodecom2 inteiro;\n" +
                             "begin\n" +
                             "  Select produto.idprodutoecom from produto                                              \n" +
                             "      where produto.codprod = Old.codprod1 into idprodecom1;\n" +
                             "  Select produto.idprodutoecom from produto                                              \n" +
                             "      where produto.codprod = Old.codprod2 into idprodecom2;\n" +
                             "  if ((idprodecom1<>0) and (idprodecom2<>0)) then\n" +
                             "     INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)\n" +
                             "         VALUES ('prodagregado', :idprodecom1 || '-' || :idprodecom2 , current_timestamp, NULL, 'delete', 'Pendente');\n" +
                             "end";
            pstmt = conn.prepareStatement(trigger);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerAtributoGrade.class).info("Trigger AFTER DELETE da sugestão de vendas, criada com sucesso. ");
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
