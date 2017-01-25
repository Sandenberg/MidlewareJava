package br.com.atsinformatica.midler.service;

import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues - Transferido o fonte do AlexsanderPimenta para esta
 * classe
 */
public class StatusPagamentoService {
    private final Logger logger = Logger.getLogger(StatusPagamentoService.class);
    
    /*** #OldStatusChange
    Esse mecanismo foi descontinuado, o fonte deverá permanecer até termos um feedback dos clientes
    
    public void finalizarPedido(String codPedidoErp, int codPedidoEcom) {
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            
            ListaPedidoERPBean listaPedidoERPBean = new ListaPedidoERPBean();
            listaPedidoERPBean.setCodPedidoResulth(codPedidoErp);
            listaPedidoERPBean.setCodPedidoEcom(codPedidoEcom);

            listaPedidoERPBean.setDataFinalizacaoPedido(new Date());

            ListaPedidoDAO listaPedidoDAO = new ListaPedidoDAO();
            if (listaPedidoDAO.validacaoFinalizarPedido(listaPedidoERPBean)) {
                if (listaPedidoDAO.finalizarPedido(listaPedidoERPBean)) {
                    //Iniciando - Alterando Status na loja prestashop
                    OrderController orderController = new OrderController();
                    orderController.updateStatusOrder((codPedidoEcom), 14);
                    //Fim - Alterando Status na loja prestashop            
                    //Iniciando - Gravar Alteração de status na tabela de Historico do pedido
                    OrderHistoryController historyController = new OrderHistoryController();
                    historyController.insertOrderHistory((codPedidoEcom), 14);
                    //Fim - Gravar Alteração de status na tabela de Historico do pedido                    
                    JOptionPane.showMessageDialog(null, "Pedido finalizado com sucesso!");
                }
            } else {
                throw new ErroSyncException("Esse pedido não pode ser finalizado!");
            }
            
            conn.commit();
        } catch (Exception e) {
            logger.error("Erro ao finalizar pedidos: ", e);            
            try {
                conn.rollback();
            } catch (SQLException | NullPointerException ex) {}
            
            if( e instanceof ErroSyncException){
                JOptionPane.showMessageDialog(null, e.getMessage());
            } else {
                JOptionPane.showMessageDialog(null, "Falha durante o processo de finalização.");
            }
        }
    }
    
    public void statusAguadandoPagamento(int codStatus, String codPedidoErp, int codPedidoEcom) {
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            
            ListaPedidoERPBean listaPedidoERPBean = new ListaPedidoERPBean();
            listaPedidoERPBean.setCodPedidoResulth(codPedidoErp);
            listaPedidoERPBean.setCodPedidoEcom(codPedidoEcom);

            ListaPedidoDAO listaPedidoDAO = new ListaPedidoDAO();
            if (listaPedidoDAO.validacaoStatusAguadandoPagamento(listaPedidoERPBean)) {
                if (listaPedidoDAO.statusAguadandoPagamento(listaPedidoERPBean, codStatus)) {
                    //Iniciando - Alterando Status na loja prestashop
                    OrderController orderController = new OrderController();
                    orderController.updateStatusOrder((codPedidoEcom), codStatus);
                    //Fim - Alterando Status na loja prestashop
                    //Iniciando - Gravar Alteração de status na tabela de Historico do pedido
                    OrderHistoryController historyController = new OrderHistoryController();
                    historyController.insertOrderHistory((codPedidoEcom), codStatus);
                    //Fim - Gravar Alteração de status na tabela de Historico do pedido
                    switch (codStatus) {
                        case 1:
                            JOptionPane.showMessageDialog(null, "Status 'Aguardando confirmação do pagamento' alterado com sucesso!");
                            break;
                        case 3:
                            JOptionPane.showMessageDialog(null, "Status 'Separação de estoque' alterado com sucesso!");
                            break;
                        case 10:
                            JOptionPane.showMessageDialog(null, "Status 'Aguardando transferência bancária' alterado com sucesso!");
                    }
                }
            } else {
                throw new ErroSyncException("Status do pedido precisa estar diferente de 'Faturado', diferente de 'Enviado' ou em aberto!");
            }
            
            conn.commit();
        } catch (Exception e) {
            logger.error("Erro ao mudar status do pedido para: ", e);
            try {
                conn.rollback();
            } catch (SQLException | NullPointerException ex) {}
            
            if( e instanceof ErroSyncException){
                JOptionPane.showMessageDialog(null, e.getMessage());
            } else {
                JOptionPane.showMessageDialog(null, "Falha durante o processo de alteração do status.");
            }
        }
    }
    
    public void statusPagamentoRecusado(String codPedidoErp, int codPedidoEcom) {
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            
            ListaPedidoERPBean listaPedidoERPBean = new ListaPedidoERPBean();
            listaPedidoERPBean.setCodPedidoResulth(codPedidoErp);
            listaPedidoERPBean.setCodPedidoEcom(codPedidoEcom);
            ListaPedidoDAO listaPedidoDAO = new ListaPedidoDAO();
            if (listaPedidoDAO.validacaoStatusPagamentoRecusado(listaPedidoERPBean)) {
                if (listaPedidoDAO.StatusPagamentoRecusado(listaPedidoERPBean)) {
                    //Iniciando - Alterando Status na loja prestashop
                    OrderController orderController = new OrderController();
                    orderController.updateStatusOrder((codPedidoEcom), 8);
                    //Fim - Alterando Status na loja prestashop                    
                    //Iniciando - Gravar Alteração de status na tabela de Historico do pedido
                    OrderHistoryController historyController = new OrderHistoryController();
                    historyController.insertOrderHistory((codPedidoEcom), 8);
                    //Fim - Gravar Alteração de status na tabela de Historico do pedido                    
                    JOptionPane.showMessageDialog(null, "Status 'Pagamento recusado' do pedido alterado com sucesso!");
                }
            } else {
                throw new ErroSyncException("Esse pedido não pode ser alterado o status!");
            }

            conn.commit();
        } catch (Exception e) {
            logger.error("Erro ao mudar status do pedidos para 'Pagemento recusado': ", e);
            try {
                conn.rollback();
            } catch (SQLException | NullPointerException ex) {}
            
            if( e instanceof ErroSyncException){
                JOptionPane.showMessageDialog(null, e.getMessage());
            } else {
                JOptionPane.showMessageDialog(null, "Falha durante o processo de alteração do status.");
            }
        }
    }

    public void statusPagamentoAceito(String codPedidoErp, int codPedidoEcom) {
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            
            ListaPedidoERPBean listaPedidoERPBean = new ListaPedidoERPBean();
            listaPedidoERPBean.setCodPedidoResulth(codPedidoErp);
            listaPedidoERPBean.setCodPedidoEcom(codPedidoEcom);

            ListaPedidoDAO listaPedidoDAO = new ListaPedidoDAO();
            if (listaPedidoDAO.validacaoStatusPagamentoAceito(listaPedidoERPBean)) {
                if (listaPedidoDAO.statusPagamentoAceito(listaPedidoERPBean)) {
                    //Iniciando - Alterando Status na loja prestashop
                    OrderController orderController = new OrderController();
                    orderController.updateStatusOrder((codPedidoEcom), 2);
                    //Fim - Alterando Status na loja prestashop
                    //Iniciando - Gravar Alteração de status na tabela de Historico do pedido
                    OrderHistoryController historyController = new OrderHistoryController();
                    historyController.insertOrderHistory((codPedidoEcom), 2);
                    //Fim - Gravar Alteração de status na tabela de Historico do pedido  
                    JOptionPane.showMessageDialog(null, "Status 'Pagamento aceito' alterado com sucesso!");
                }
            } else {
                throw new ErroSyncException("Status do pedido precisa estar igual a 'Aguardando confirmação de pagamento' ou \n "
                                           + "'Aguardando transferência bancária' ou 'Separação de estoque'");
            }
            conn.commit();
        } catch (Exception e) {
            logger.error("Erro ao mudar status do pedido para 'Pagemento aceito': ", e);
            try {
                conn.rollback();
            } catch (SQLException | NullPointerException ex) {}
            
            if( e instanceof ErroSyncException){
                JOptionPane.showMessageDialog(null, e.getMessage());
            } else {
                JOptionPane.showMessageDialog(null, "Falha durante o processo de alteração do status.");
            }
        }
    }
    
    public void statusNotaFiscal(String codPedidoErp, int codPedidoEcom) {
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            
            ListaPedidoERPBean listaPedidoERPBean = new ListaPedidoERPBean();
            listaPedidoERPBean.setCodPedidoResulth(codPedidoErp);
            listaPedidoERPBean.setCodPedidoEcom(codPedidoEcom);

            ListaPedidoDAO listaPedidoDAO = new ListaPedidoDAO();
            if (listaPedidoDAO.validacaoStatusNotaFiscal(listaPedidoERPBean)) {
                if (listaPedidoDAO.StatusNotaFiscal(listaPedidoERPBean)) {
                    //Iniciando - Alterando Status na loja prestashop
                    OrderController orderController = new OrderController();
                    orderController.updateStatusOrder((codPedidoEcom), 13);
                    //Fim - Alterando Status na loja prestashop            
                    //Iniciando - Gravar Alteração de status na tabela de Historico do pedido
                    OrderHistoryController historyController = new OrderHistoryController();
                    historyController.insertOrderHistory((codPedidoEcom), 13);
                    //Fim - Gravar Alteração de status na tabela de Historico do pedido     
                    JOptionPane.showMessageDialog(null, "Status (Nota Fiscal) do pedido alterado com sucesso!");
                }
            } else {
                throw new ErroSyncException("Esse pedido não pode ser alterado o status!");
            }

            conn.commit();
        } catch (Exception e) {
            logger.error("Erro ao mudar status do pedidos para (Nota Fiscal):", e);
            try {
                conn.rollback();
            } catch (SQLException | NullPointerException ex) {}
            
            if( e instanceof ErroSyncException){
                JOptionPane.showMessageDialog(null, e.getMessage());
            } else {
                JOptionPane.showMessageDialog(null, "Falha durante o processo de alteração do status.");
            }
        }
    }
    
    public void statusPagamentoEstornado(String codPedidoErp, int codPedidoEcom) {
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            
            ListaPedidoERPBean listaPedidoERPBean = new ListaPedidoERPBean();
            listaPedidoERPBean.setCodPedidoResulth(codPedidoErp);
            listaPedidoERPBean.setCodPedidoEcom(codPedidoEcom);

            ListaPedidoDAO listaPedidoDAO = new ListaPedidoDAO();
            if (listaPedidoDAO.validacaoStatusPagamentoEstornado(listaPedidoERPBean)) {
                if (listaPedidoDAO.StatusPagamentoEstornado(listaPedidoERPBean)) {
                    //Iniciando - Alterando Status na loja prestashop
                    OrderController orderController = new OrderController();
                    orderController.updateStatusOrder((codPedidoEcom), 7);
                    //Fim - Alterando Status na loja prestashop
                    //Iniciando - Gravar Alteração de status na tabela de Historico do pedido
                    OrderHistoryController historyController = new OrderHistoryController();
                    historyController.insertOrderHistory((codPedidoEcom), 7);
                    //Fim - Gravar Alteração de status na tabela de Historico do pedido       
                    JOptionPane.showMessageDialog(null, "Status 'Pagamanto estornado' alterado com sucesso!");
                }
            } else {
                throw new ErroSyncException("Status do pedido deverá estar como 'Devolvido'");
            }
            
            conn.commit();
        } catch (Exception e) {
            logger.error("Erro ao mudar status do pedido para 'Pagamento estornado': ", e);
            try {
                conn.rollback();
            } catch (SQLException | NullPointerException ex) {}
            
            if( e instanceof ErroSyncException){
                JOptionPane.showMessageDialog(null, e.getMessage());
            } else {
                JOptionPane.showMessageDialog(null, "Falha durante o processo de alteração do status.");
            }
        }
    }
    
    public void statusPedidoDevolvido(String codPedidoErp, int codPedidoEcom) {
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();
            conn.setAutoCommit(false);
            
            ListaPedidoERPBean listaPedidoERPBean = new ListaPedidoERPBean();
            listaPedidoERPBean.setCodPedidoResulth(codPedidoErp);
            listaPedidoERPBean.setCodPedidoEcom(codPedidoEcom);

            ListaPedidoDAO listaPedidoDAO = new ListaPedidoDAO();
            if (listaPedidoDAO.validacaoStatusPedidoDevolvido(listaPedidoERPBean)) {
                if (listaPedidoDAO.StatusPedidoDevolvido(listaPedidoERPBean)) {
                    //Iniciando - Alterando Status na loja prestashop
                    OrderController orderController = new OrderController();
                    orderController.updateStatusOrder((codPedidoEcom), 9);
                    //Fim - Alterando Status na loja prestashop                    
                    //Iniciando - Gravar Alteração de status na tabela de Historico do pedido
                    OrderHistoryController historyController = new OrderHistoryController();
                    historyController.insertOrderHistory((codPedidoEcom), 9);
                    //Fim - Gravar Alteração de status na tabela de Historico do pedido   
                    JOptionPane.showMessageDialog(null, "Status (Pedido devolvido) do pedido alterado com sucesso!");
                }
            } else {
                throw new ErroSyncException("Esse pedido não pode ser alterado o status!");
            }

            conn.commit();
        } catch (Exception e) {
            logger.error("Erro ao mudar status do pedidos para (Pedido devolvido): ", e);
            try {
                conn.rollback();
            } catch (SQLException | NullPointerException ex) {}
            
            if( e instanceof ErroSyncException){
                JOptionPane.showMessageDialog(null, e.getMessage());
            } else {
                JOptionPane.showMessageDialog(null, "Falha durante o processo de alteração do status.");
            }
        }
    }
    
    */
}
