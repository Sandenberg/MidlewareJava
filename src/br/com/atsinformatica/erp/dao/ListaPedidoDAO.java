package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ListaPedidoERPBean;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.util.ArrayList;

/**
 *
 * @author kennedimalheiros
 */
public class ListaPedidoDAO implements IGenericDAO<ListaPedidoERPBean> {

    private static Logger logger = Logger.getLogger(ListaPedidoDAO.class);
    private Connection conn;

    @Override
    public void gravar(ListaPedidoERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(ListaPedidoERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListaPedidoERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListaPedidoERPBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT PC.CODPEDIDO, PC.IDPEDIDOECOM, C.NOME, PC.TOTALPEDIDO,"
                    + "       PC.STATUSPEDIDOECOM, PC.DATAPEDIDO, PC.OBSERVACAO, PC.DATASINCECOM"
                    + "  FROM PEDIDOC PC JOIN"
                    + "       CLIENTE C ON C.CODCLIENTE = PC.CODCLIENTE"
                    + "  WHERE ((PC.IDPEDIDOECOM IS NOT NULL ) AND (pc.idpedidoecom <> 0))"
                    + "   AND PC.STATUSPEDIDOECOM <> 14"
                    + "   AND PC.STATUSPEDIDOECOM <> 6"
                    + "   AND PC.STATUSPEDIDOECOM >  0 ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            List<ListaPedidoERPBean> listaPedBean = new ArrayList<>();
            while (rs.next()) {
                ListaPedidoERPBean bean = new ListaPedidoERPBean(rs);
                listaPedBean.add(bean);
            }
            return listaPedBean;
        } catch (Exception e) {
            return null;
        } finally {
            stmt.close();
            rs.close();
        }

    }

    @Override
    public String ultimoRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    #OldStatusChange
//    public boolean validacaoFinalizarPedido(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//
//            String sql = "   SELECT * FROM PEDIDOC P"
//                    + "           WHERE P.CODPEDIDO = ?"
//                    + "             AND P.IDPEDIDOECOM = ?"
//                    + "             AND P.STATUSPEDIDOECOM = 5"; //Finaliza se o Pedido for entregue.
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação de Finalizar Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação de Finalizar Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean finalizarPedido(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ? , p.DATAFINALIZAPEDIDOECOM = ? "
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "14");
//            pstmt.setDate(2, new Date(listaPedidoERPBean.getDataFinalizacaoPedido().getTime()));
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(3, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(4, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Finalizado)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Finalizar Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }
    
//    #OldStatusChange
//    public boolean validacaoCancelarPedido(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//
//            String sql = "   SELECT * FROM PEDIDOC P"
//                    + "           WHERE P.CODPEDIDO = ?"
//                    + "             AND P.IDPEDIDOECOM = ?"
//                    + "             AND P.STATUSPEDIDOECOM = 1 "
//                    + "              OR P.STATUSPEDIDOECOM = 8 ";
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação de cancelar Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação de cancelar Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean cancelarPedido(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ? , p.DATAFINALIZAPEDIDOECOM = ? "
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "6");
//            pstmt.setDate(2, new Date(listaPedidoERPBean.getDataFinalizacaoPedido().getTime()));
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(3, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(4, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Cancelado)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Cancelar Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

//    #OldStatusChange
//    public boolean validacaoStatusAguadandoPagamento(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//
//            String sql = "  SELECT * FROM PEDIDOC P\n" +
//                         "    WHERE P.IDPEDIDOECOM =  ?\n" +
//                         "    AND p.codpedido = ?\n" +
//                         "    AND ((P.STATUSPEDIDOECOM <> 6) \n" +
//                         "    AND (P.STATUSPEDIDOECOM <> 14) \n" +
//                         "    AND (P.STATUSPEDIDOECOM <> 2) \n" +
//                         "    AND (P.STATUSPEDIDOECOM <> 5) "+
//                         "    AND (P.STATUSPEDIDOECOM  <> 7)) "+
//                         "    AND (P.STATUSPEDIDOECOM <> 4) \n";
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, listaPedidoERPBean.getCodPedidoEcom());
//            pstmt.setString(2, listaPedidoERPBean.getCodPedidoResulth());
//            rs = pstmt.executeQuery();
//            logger.info("Verificação de Status (AguadandoPagamento) do Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação de Status (AguadandoPagamento) do Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean statusAguadandoPagamento(ListaPedidoERPBean listaPedidoERPBean, int codStatus) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ? "
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setInt(1, codStatus);
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(2, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(3, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Aguadando Pagamento)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Mudar status do Pedido para (Aguadando Pagamento): " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

//    #OldStatusChange
//    public boolean validacaoStatusPagamentoRecusado(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//
//            String sql = "   SELECT * FROM PEDIDOC P "
//                    + "           WHERE P.CODPEDIDO = ? "
//                    + "             AND P.IDPEDIDOECOM = ? "
//                    + "             AND ((P.STATUSPEDIDOECOM = 1) OR  " //Aguardando confirmação de pagamento
//                    + "                  (P.STATUSPEDIDOECOM = 3) OR  " //Aguardando transferência bancária
//                    + "                  (P.STATUSPEDIDOECOM = 10))";   //Separação de estoque
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação para alteração do Status (Pagamento Recusado) do Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação para alteração do Status (Pagamento Recusado) do Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean StatusPagamentoRecusado(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//           conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ? "
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "8");
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(2, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(3, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Pagamento Recusado)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Mudar status do Pedido para (Pagamento Recusado): " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

//    #OldStatusChange
//    public boolean validacaoStatusPagamentoAceito(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " SELECT P.idpedidoecom FROM PEDIDOC P " +
//                         "                     WHERE P.CODPEDIDO = ? " +
//                         "                     AND P.IDPEDIDOECOM = ? " +
//                         "                    AND ((P.STATUSPEDIDOECOM = 1) " + //aguardando confirmação de pagamento
//                         "                    OR  (P.STATUSPEDIDOECOM = 10) " + // aguardando transferência bancária
//                         "                    OR  (P.STATUSPEDIDOECOM = 16) " + //Aguardando pagamento
//                         "                    OR  (P.STATUSPEDIDOECOM = 3)) " + //Separação de estoque
//                         "                    AND ((P.STATUSPEDIDOECOM <> 4) " + //Enviado
//                         "                    AND  (P.STATUSPEDIDOECOM <> 6) " + //Cancelado
//                         "                    AND  (P.STATUSPEDIDOECOM <> 7)) "; //Estornado
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação para alteração do Status (Pagamento Aceito) do Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação para alteração do Status (Pagamento Aceito) do Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean statusPagamentoAceito(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ? "
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "2");
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(2, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(3, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Pagamento Aceito)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Mudar status do Pedido para (Pagamento Aceito): " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

//    #OldStatusChange
//    public boolean validacaoStatusNotaFiscal(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = "   SELECT * FROM PEDIDOC P "
//                    + "           WHERE P.CODPEDIDO = ? "
//                    + "             AND P.IDPEDIDOECOM = ? "
//                    + "             AND ((P.STATUSPEDIDOECOM = 2) " //Faturado 
//                    + "              OR (P.STATUSPEDIDOECOM = 9)) "; //Devolvido
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação para alteração do Status (Nota Fiscal) do Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação para alteração do Statos (Nota Fiscal) do Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean StatusNotaFiscal(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ? "
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "13");
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(2, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(3, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Nota Fiscal)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Mudar status do Pedido para (Nota Fiscal): " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

//    #OldStatusChange
//    public boolean validacaoStatusEnviado(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//
//            String sql = "   SELECT * FROM PEDIDOC P "
//                    + "           WHERE P.CODPEDIDO = ? "
//                    + "             AND P.IDPEDIDOECOM = ? "
//                    + "             AND P.STATUSPEDIDOECOM = 13"; //SE ESTIVER COM O STATUS (PREPARAÇÃO EM PROGRESSO) E PORQUE JA FOI FEITO O PAGAMENTO.
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação para alteração do Status (Enviado) do Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação para alteração do Statos (Enviado) do Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean StatusEnviado(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ?, P.DATAENVIOPEDIDOECOM = ?, P.CODRASTREIAECOM = ?"
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "4");
//            pstmt.setDate(2, new Date(listaPedidoERPBean.getDataEnvioPedidoEcom().getTime()));
//            pstmt.setString(3, listaPedidoERPBean.getCodRastreiaEcom());
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(4, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(5, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Enviado)");
//            logger.info("Mudança de status de Pedido para (Enviado)com sucesso!");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Mudar status do Pedido para (Enviado): " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

//    #OldStatusChange
//    public boolean validacaoStatusEntregue(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//
//            String sql = "   SELECT * FROM PEDIDOC P "
//                    + "           WHERE P.CODPEDIDO = ? "
//                    + "             AND P.IDPEDIDOECOM = ? "
//                    + "             AND P.STATUSPEDIDOECOM = 4 "; //Estar com o status Enviado.
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação para alteração do Status (Entregue) do Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação para alteração do Statos (Entregue) do Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean StatusEntregue(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ?, P.CODRETORNOENVIOECOM = ?"
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "5");
//            pstmt.setString(2, listaPedidoERPBean.getNumDocumetoEntrega());
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(3, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(4, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Entregue)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Mudar status do Pedido para (Entregue): " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

//    #OldStatusChange
//    public boolean validacaoStatusPedidoDevolvido(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//
//            String sql = "   SELECT * FROM PEDIDOC P "
//                    + "           WHERE P.CODPEDIDO = ? "
//                    + "             AND P.IDPEDIDOECOM = ? "
//                    + "             AND P.STATUSPEDIDOECOM = 5 "; //Validação se Pedido foi entregue.
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação para alteração do Status (Pedido devolvido) do Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação para alteração do Statos (Pedido devolvido) do Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean StatusPedidoDevolvido(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ? "
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "9");
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(2, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(3, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Pedido Devolvido)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Mudar status do Pedido para (Pedido Devolvido): " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

//    #OldStatusChange
//    public boolean validacaoStatusPagamentoEstornado(ListaPedidoERPBean listaPedidoERPBean) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = ConexaoATS.getConnection();
//
//            String sql = "   SELECT * FROM PEDIDOC P "
//                    + "           WHERE P.CODPEDIDO = ? "
//                    + "             AND P.IDPEDIDOECOM = ? "
//                    + "             AND P.STATUSPEDIDOECOM = 9 "; //Pedido devolvido.
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(2, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//            rs = pstmt.executeQuery();
//            logger.info("Verificação para alteração do Status (Pagamento estornado) do Pedido com sucesso. ");
//            if (rs.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("Erro na Verificação para alteração do Statos (Pagamento estornado) do Pedido: " + e);
//            return false;
//        } finally {
//            pstmt.close();
//            rs.close();
//        }
//    }

//    #OldStatusChange
//    public boolean StatusPagamentoEstornado(ListaPedidoERPBean listaPedidoERPBean) throws SQLException, InstantiationException {
//        PreparedStatement pstmt = null;
//        try {
//            conn = ConexaoATS.getConnection();
//            String sql = " UPDATE PEDIDOC P SET P.STATUSPEDIDOECOM = ? "
//                    + "  WHERE P.CODPEDIDO = ?"
//                    + "    AND P.IDPEDIDOECOM = ?";
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, "7");
//            //Função preencheCom completa o Codigo do Pedido para 8, completando com zeros a esquerda.
//            pstmt.setString(2, Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT));
//            pstmt.setString(3, String.valueOf(listaPedidoERPBean.getCodPedidoEcom()));
//
//            pstmt.executeUpdate();
//
//            //Gerando log
//            LogERP.geraLog("PEDIDOC", Funcoes.preencheCom(String.valueOf(listaPedidoERPBean.getCodPedidoResulth()), "0", 8, Funcoes.LEFT), "Alteração", "Mudança de status de Pedido para (Pagamento Estornado)");
//            return true;
//        } catch (SQLException e) {
//            logger.error("Erro ao Mudar status do Pedido para (Pagamento estornado): " + e);
//            return false;
//        } finally {
//            pstmt.close();
//        }
//    }

}
