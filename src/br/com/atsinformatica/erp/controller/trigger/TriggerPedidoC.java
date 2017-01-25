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
public class TriggerPedidoC implements ITrigger {
    private Logger logger = Logger.getLogger(TriggerPedidoC.class);
    private Connection conn;

    @Override
    public void createTriggerAu() throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String qryPedidoC =  
                   "CREATE OR ALTER trigger pedidocecom_au for pedidoc\n" +
                    "active after update position 0\n" +
                    "AS\n" +
                    "begin\n" +
                    "  if((old.idpedidoecom is not null) AND (old.idpedidoecom <> 0) AND (old.tipopedido = '55')) then\n" +
                    "    begin\n" +
                    "      if((Old.faturado = 'N') AND (new.faturado = 'S')) then\n" +
                    "         begin\n" +
                    "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n" +
                    "         VALUES ('statuspedidoecom', Old.idpedidoecom ||';'||'PAGAMENTO_ACEITO', current_timestamp, NULL, 'update', 'Pendente');\n" +
                    "      end\n" +
                    "      if((Old.faturado='S') AND (new.faturado = 'N')) then\n" +
                    "         begin\n" +
                    "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n" +
                    "         VALUES ('statuspedidoecom', Old.idpedidoecom ||';'||'CANCELADO', current_timestamp, NULL, 'update', 'Pendente');\n" +
                    "      end\n" +
                    "      if((Old.faturado='S')AND(new.faturado = 'X')) then\n" +
                    "         begin\n" +
                    "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n" +
                    "         VALUES ('statuspedidoecom', Old.idpedidoecom ||';'||'CANCELADO', current_timestamp, NULL, 'update', 'Pendente');\n" +
                    "      end\n" +
                    "      if((Old.statuspedidoecom = 'PAGAMENTO_ACEITO') AND (New.statuspedidoecom = 'ENVIADO')) then\n" +
                    "         begin\n" +
                    "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n" +
                    "         VALUES ('statuspedidoecom', Old.idpedidoecom ||';'||'ENVIADO', current_timestamp, NULL, 'update', 'Pendente');\n" +
                    "      end\n" +
                    "      if((Old.statuspedidoecom = 'ENVIADO') AND (New.statuspedidoecom = 'ENTREGUE')) then\n" +
                    "         begin\n" +
                    "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n" +
                    "         VALUES ('statuspedidoecom', Old.idpedidoecom ||';'||'ENTREGUE', current_timestamp, NULL, 'update', 'Pendente');\n" +
                    "      end\n" +
                    "      if((Old.statuspedidoecom = 'ENTREGUE') AND (New.statuspedidoecom = 'DEVOLVIDO')) then\n" +
                    "         begin\n" +
                    "         INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n" +
                    "         VALUES ('statuspedidoecom', Old.idpedidoecom ||';'||'DEVOLVIDO', current_timestamp, NULL, 'update', 'Pendente');\n" +
                    "      end \n"+
                    "      if((Old.faturado='N')AND(new.faturado = 'X')) then \n" +
                    "         begin \n" +
                    "           INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS) \n" +
                    "           VALUES ('statuspedidoecom', Old.idpedidoecom ||';'||'CANCELADO', current_timestamp, NULL, 'update', 'Pendente'); \n" +
                    "      end \n"+
                    "  end\n" +                    
                    "end\n" +
                    "\n" +
                    "";
            pstmt = conn.prepareStatement(qryPedidoC);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER UPDATE do pedido, criada com sucesso");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createTriggerAd() throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String qryPedidoC = 
                    "\n" +
                    "CREATE OR ALTER trigger pedidocecom_ad for pedidoc\n" +
                    "active after delete position 0\n" +
                    "AS\n" +
                    "begin\n" +
                    "  if ((old.idpedidoecom <> 0) AND (old.tipopedido = '55')) then\n" +
                    "     begin\n" +
                    "     INSERT INTO HISTINTEGECOM (ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS)   \n" +
                    "     VALUES ('statuspedidoecom', Old.idpedidoecom ||';'||'CANCELADO', current_timestamp, NULL, 'update','Pendente');\n" +
                    "  end\n" +
                    "end\n" +
                    "\n" +
                    "";
            pstmt = conn.prepareStatement(qryPedidoC);
            pstmt.executeUpdate();
            logger.info("Trigger AFTER DELETE do pedido, criada com sucesso.");
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
