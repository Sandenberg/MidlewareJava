package br.com.atsinformatica.midler.service;

import br.com.atsinformatica.erp.controller.LogEcomErroController;
import br.com.atsinformatica.erp.dao.ClienteERPDAO;
import br.com.atsinformatica.erp.dao.CompclieDAO;
import br.com.atsinformatica.erp.dao.ComplementoPedidoEcomDAO;
import br.com.atsinformatica.erp.dao.HistoricoIntegraDAO;
import br.com.atsinformatica.erp.dao.PedidoCERPDAO;
import br.com.atsinformatica.erp.dao.PedidoIERPDAO;
import br.com.atsinformatica.erp.dao.ProdGradeERPDAO;
import br.com.atsinformatica.erp.dao.ProdutoDAO;
import br.com.atsinformatica.erp.dao.ReservaCERPDAO;
import br.com.atsinformatica.erp.dao.ReservaIERPDAO;
import br.com.atsinformatica.erp.entity.ClienteERPBean;
import br.com.atsinformatica.erp.entity.CompclieBean;
import br.com.atsinformatica.erp.entity.ComplementoPedidoEcomBean;
import br.com.atsinformatica.erp.entity.EnderecoERPBean;
import br.com.atsinformatica.erp.entity.EstadoERPBean;
import br.com.atsinformatica.erp.entity.HistoricoIntegraERPBean;
import br.com.atsinformatica.erp.entity.PedidoCBean;
import br.com.atsinformatica.erp.entity.PedidoCERPBean;
import br.com.atsinformatica.erp.entity.PedidoIERPBean;
import br.com.atsinformatica.erp.entity.ReservaCERP;
import br.com.atsinformatica.erp.entity.ReservaIERP;
import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.entity.enumeration.TipoOperacao;
import br.com.atsinformatica.midler.exception.ErroSyncException;
import br.com.atsinformatica.midler.exception.TipoOperacaoException;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.prestashop.clientDAO.OrderPrestashopDAO;
import br.com.atsinformatica.prestashop.controller.AddressController;
import br.com.atsinformatica.prestashop.controller.CarrierController;
import br.com.atsinformatica.prestashop.controller.CustomerController;
import br.com.atsinformatica.prestashop.controller.OrderController;
import br.com.atsinformatica.prestashop.controller.StateController;
import br.com.atsinformatica.prestashop.model.node.OrderRowNode;
import br.com.atsinformatica.prestashop.model.root.Order;
import br.com.atsinformatica.utils.DateUtil;
import br.com.atsinformatica.utils.FormaPagamentoEcom;
import br.com.atsinformatica.utils.Funcoes;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.swing.JDialog;
import org.apache.log4j.Logger;
import org.firebirdsql.jca.FBXAException;

/**
 *
 * @author niwrodrigues
 */
public class SyncPedidoTaskService {
    private final Logger logger = Logger.getLogger(SyncPedidoTaskService.class);
    
    private static SyncPedidoTaskService instance;
    
    private CustomerController customerController;
    private CarrierController carrierController;
    private AddressController addressController;
    private OrderController orderController;
    private StateController stateController;
    
    private ComplementoPedidoEcomDAO complementoDAO;
    private HistoricoIntegraDAO historicoDAO;
    private ReservaIERPDAO reservaItemDAO;
    private ProdGradeERPDAO prodGradeDAO;
    private OrderPrestashopDAO orderDAO;
    private PedidoIERPDAO pedidoItemDAO;
    private ReservaCERPDAO reservaDAO;
    private ClienteERPDAO clienteDAO;
    private PedidoCERPDAO pedidoDAO;
    private CompclieDAO compclieDAO;
    private ProdutoDAO produtoDAO;
    
    private static SyncPedidoTaskService getInstance() {
        if (instance == null) {
            instance = new SyncPedidoTaskService();
        }
        return instance;
    }
    
    public static void start(){
        SyncPedidoTaskService.getInstance().synchronize();
    }
    
    protected void synchronize() {
        /**
         * Baixa os pedidos do Ecom
         */
        try {
            List<PedidoCERPBean> lista = this.getOrderController().buscaListaPendentes();
            if (lista != null && !lista.isEmpty()) {                
                for (int i = 0 ; i < lista.size() ; i++) {
                    PedidoCERPBean pedido = lista.get(i);
                    
                    // Precisa verificar conexão com WS a cada item sincronizado
                    SyncService.verificaConexaoWS();
                    
                    Connection connection = ConexaoATS.getConnection();
                    HistoricoIntegraERPBean hist = new HistoricoIntegraERPBean();
                    hist.setEntidade(TipoEntidade.PEDIDO);
                    hist.setCodEntidade(pedido.getReference());
                    hist.setDataEnt(new Date());
                    hist.setTipoOperacao(TipoOperacao.INSERT);
                    hist.setStatus(StatusIntegracao.EM_ANDAMENTO);

                    try {
                        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                        connection.setAutoCommit(false);

                        getHistoricoDAO().gravar(hist);
                        connection.commit(); // Comita o histórico

                        // Tenta sincronizar
                        connection.setAutoCommit(false);
                        PedidoCERPBean pedidoOld = this.getPedidoDAO()
                                .retornaPedidoPorPedidoEcom(Integer.parseInt(pedido.getId_ecom()));
                        if (pedidoOld != null) {
                            this.enviaIdPedido(pedido.getId_ecom(), pedidoOld.getId_erp());
                            connection.rollback();
                        } else {
                            this.sincronizaPedido(pedido);
                            connection.commit();
                        }
        
                        // Concluido com sucesso
                        hist = this.getHistoricoDAO().abrir(String.valueOf(hist.getId()));
                        hist.setCodEntidade(pedido.getId_erp());
                        hist.setDataInteg(new Date());
                        hist.setStatus(StatusIntegracao.SINCRONIZADO);
                        this.getHistoricoDAO().alterar(hist);
                        connection.commit();
                    } catch (Throwable e) {
                        try {
                            connection.rollback();
                        } catch (SQLException ex) {
                            logger.debug("Falha ao aplicar rollback.", ex);
                        }
                        
                        try {
                            connection.setAutoCommit(false);
                            hist = this.getHistoricoDAO().abrir(String.valueOf(hist.getId()));
                            hist.setDataInteg(new Date());
                            hist.setStatus(StatusIntegracao.ERRO);
                            this.getHistoricoDAO().alterar(hist);
                            this.trataException(e, hist);
                            connection.commit();
                        } catch (Exception ex) {
                            logger.error("Falha ao gravar histórico.", ex);
                        }
                        logger.error("Falha durante a integração.", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Falha ao buscar itens para integrar.", e);
        }
        
        /**
         * Altera status do pedido - Cancela pedido
         */
        try {
            List<PedidoCBean> lista = this.getPedidoDAO().listaPedidosPendentes();
            if (lista != null && !lista.isEmpty()) {                
                for (PedidoCBean pedidoC : lista) {
                    // Precisa verificar conexão com WS a cada item sincronizado
                    SyncService.verificaConexaoWS();
                    
                    Connection connection = ConexaoATS.getConnection();
                    
                    try {
                        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                        connection.setAutoCommit(false);

                        // Verifica o status no e-commerce
                        Order order = null;
                        try {
                            order = this.getOrderDAO().getId(Order.URLORDER, pedidoC.getIdPedidoEcom());
                        } catch (Exception e) {
                            logger.error("Falha ao buscar pedido", e);
                            throw new ErroSyncException("Falha ao comunicar com o web service, verifique "
                                    + "sua conexão com a internet e as configurações do modulo integrador.");
                        }

                        //Se pedido cancelado na loja
                        if (order.getCurrent_state().equals(StatusPedido.CANCELADO)) {
                            this.getPedidoDAO().cancelaPedidoERP(pedidoC);

                            HistoricoIntegraERPBean hist = new HistoricoIntegraERPBean();
                            hist.setEntidade(TipoEntidade.PEDIDO);
                            hist.setCodEntidade(pedidoC.getCodPedido());
                            hist.setDataEnt(new Date());
                            hist.setTipoOperacao(TipoOperacao.UPDATE);
                            hist.setDataInteg(new Date());
                            hist.setStatus(StatusIntegracao.SINCRONIZADO);
                            hist.setCodEntidade(Funcoes.preencheCom(String.valueOf(order.getIdErp()),
                                    "0", 8, Funcoes.LEFT));
                            this.getHistoricoDAO().gravar(hist);
                        }
                        connection.commit();
                    } catch (Exception e) {
                        try {
                            connection.rollback();
                        } catch (SQLException ex) {
                            logger.debug("Falha ao aplicar rollback.", ex);
                        }

                        try {
                            HistoricoIntegraERPBean hist = this.getHistoricoDAO().procurar(pedidoC.getCodPedido());
                            boolean novo = false;
                            if (hist == null) {
                                novo = true;
                                hist = new HistoricoIntegraERPBean();
                            }
                            
                            hist.setEntidade(TipoEntidade.PEDIDO);
                            hist.setCodEntidade(pedidoC.getCodPedido());
                            hist.setDataEnt(new Date());
                            hist.setTipoOperacao(TipoOperacao.UPDATE);
                            hist.setDataInteg(new Date());
                            hist.setStatus(StatusIntegracao.ERRO);
                            if (novo) {
                                this.getHistoricoDAO().gravar(hist);
                            } else {
                                this.getHistoricoDAO().alterar(hist);
                            }
                            this.trataException(e, hist);
                            connection.commit();
                        } catch (Exception ex) {
                            logger.error("Falha ao gravar histórico.", ex);
                        }
                        logger.error("Falha durante a integração.", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Falha durante o processo de integração.", e);
        }
    }
        
    private void sincronizaPedido(PedidoCERPBean pedido) throws Exception{
        // Valida
        if (pedido.getCurrent_state() == null) {
            throw new RuntimeException("Não foi possível possível interpretar o status"
                    + " de pagamento, verifique as configurações do e-commerce.");
        } 

        // Busca o cliente e suas informações para gravar ou atualizar
        // Nome pouco usual ClienteERPBean = Cliente do Prestashop!!
        ClienteERPBean clienteEcom = this.getCustomerController().buscaClientePrestashop(pedido.getId_customer());
        EnderecoERPBean endereco = this.getAddressController().buscaEnderecoPrestashop(Integer.valueOf(pedido.getId_address_delivery()), Integer.valueOf(pedido.getId_address_invoice()));
        EstadoERPBean estado = null;
        if (endereco.getId_state() != null && !endereco.getId_state().trim().isEmpty()) {
            estado = this.getStateController().buscaEstadoPrestashop(endereco.getId_state(), endereco.getEstadoCob());
        }
        
        // Decide se vai atualizar ou incluir o cliente
        String codigoCliente;
        codigoCliente = this.getClienteDAO().verificaClienteEcomExisteCpfCnpj(clienteEcom.getTipoPessoa(), clienteEcom.getCpfCnpj());
        if (!codigoCliente.equals("")) {
            codigoCliente = this.getClienteDAO().atualizarClienteComEndereco(codigoCliente, clienteEcom, endereco, estado);
        } else {
            codigoCliente = this.getClienteDAO().gravarClienteComEndereco(clienteEcom, endereco, estado);
        }
        
        /** Insere ou atualiza endereço de nota fiscal eletronica */
        CompclieBean compClie = new CompclieBean();
        compClie.setCodCliente(codigoCliente);
        compClie.setEnderecoNFE(endereco.getEnderecoCob());
        compClie.setComplementoNFE(endereco.getComplemento());
        compClie.setNumeroNFE(endereco.getNumeroCob());
        this.getCompclieDAO().alterar(compClie);
        
        /** Preenche dados pedido */
        pedido.setObservacao(this.getCarrierController().getNameCarrier(Integer.valueOf(pedido.getId_carrier())));
        if (ParaEcomService.getCodTVenda() != null) {
            pedido.setCodTVenda(ParaEcomService.getCodTVenda());
        }
        
        /** Grava o pedido */
        String codPedido = this.getPedidoDAO().gravarPedido(pedido, codigoCliente, ParaEcomService.getCodEmpresaEcom(), ParaEcomService.getCodVendendEcom());
        pedido.setId_erp(codPedido);
        
        //grava dados complementares do pedido do e-commerce
        this.gravadadosComplementaresEcom(pedido);
        
        // Loop com itens do pedido
        for (OrderRowNode orderRowNode : pedido.getListItensPedido()) {
            /* Pegando o codigo do produtoERP */
            String codProduto = this.getProdutoDAO().retornaCodProdutoERP(String.valueOf(orderRowNode.getProductId()));
            if (codProduto == null) {
                throw new RuntimeException("Não foi possível localizar o produto correspondente ao id: " + orderRowNode.getProductId());
            } else {
                codProduto = Funcoes.preencheCom(codProduto, "0", 6, Funcoes.LEFT);
            }

            PedidoIERPBean pedidoItem = new PedidoIERPBean();
            if (estado != null) {
                pedidoItem.setUf(estado.getSigla());
            }
            pedidoItem.setCodEmpresa(ParaEcomService.getCodEmpresaEcom());
            pedidoItem.setCodPedido(codPedido);
            pedidoItem.setCodClienteERP(codigoCliente);
            pedidoItem.setCodProdERP(codProduto);
            pedidoItem.setQuantidade(orderRowNode.getProductQuantity());
            pedidoItem.setPrecoUnit(orderRowNode.getUnitPriceTaxIncl());
            pedidoItem.setCodGradERP(this.getProdGradeDAO().getGradeByAtributeId(orderRowNode.getProductAttributeId()));
            pedidoItem.setUnidadeSaida(this.getProdutoDAO().retornaUnidadeSaidaProdutoERP(codProduto));
            this.getPedidoItemDAO().gravar(pedidoItem);
        }
        
        /* Gravando complemento do pedido */
        this.getPedidoDAO().gravarPedidoCompl(codPedido, codigoCliente, endereco, estado, ParaEcomService.getCodEmpresaEcom());
        
        /* Gravando Cabeçalho da Reserva de de Pedido */
        ReservaCERP reserva = new ReservaCERP();
        reserva.setCodCliente(codigoCliente);
        reserva.setCodEmpresa(ParaEcomService.getCodEmpresaEcom());
        reserva.setCodPedido(codPedido);
        reserva.setDtReserva(new Date());
        String codReserva = this.getReservaDAO().gravarReserva(reserva);
        
        /* Loop para gravar Itens da Reserva do Pedido. */
        for (OrderRowNode orderRowNode : pedido.getListItensPedido()) {
            //Pegando o codigo do produtoERP
            String codProduto = this.getProdutoDAO().retornaCodProdutoERP(String.valueOf(orderRowNode.getProductId()));
            if (codProduto == null) {
                throw new RuntimeException("Não foi possível localizar o produto correspondente ao id: " + orderRowNode.getProductId());
            } else {
                codProduto = Funcoes.preencheCom(codProduto, "0", 6, Funcoes.LEFT);
            }

            ReservaIERP reservaItem = new ReservaIERP();
            reservaItem.setCodReservar(Integer.valueOf(codReserva));
            reservaItem.setBaixado("N");
            reservaItem.setCodGrade(this.getProdGradeDAO().getGradeByAtributeId(orderRowNode.getProductAttributeId()));
            reservaItem.setCodProd(codProduto);
            reservaItem.setPreco(orderRowNode.getUnitPriceTaxIncl());
            reservaItem.setQuantidade(orderRowNode.getProductQuantity());
            
            /* Gravando Item na Reserva de Pedido */
            this.getReservaItemDAO().gravar(reservaItem);
        }      
        
        // Gravado id do erp na loja virtual
        this.enviaIdPedido(pedido.getId_ecom(), codPedido);
    }
    
    private void gravadadosComplementaresEcom(PedidoCERPBean pedidoBean){
        ComplementoPedidoEcomBean bean = new ComplementoPedidoEcomBean();
        bean.setCodErp(pedidoBean.getId_erp());
        bean.setCodReferencia(pedidoBean.getReference());
        bean.setCodTransacao(pedidoBean.getIdTransaction());
        bean.setDataPagamento(pedidoBean.getDateUpd());
        bean.setFormaPagamento(FormaPagamentoEcom.indiceOf(pedidoBean.getPayment()));
        bean.setIdPedidoEcom(Integer.valueOf(pedidoBean.getId_ecom()));
        bean.setValorPago(Double.valueOf(pedidoBean.getTotal_paid_real()));
        bean.setCodEmpresa(ParaEcomService.getCodEmpresaEcom());
        bean.setTipoPedido("55");
        bean.setQtdeParcelaEcom(pedidoBean.getInstallmentCounts());

        if (bean.getFormaPagamento() == 3) {
            if (ParaEcomService.getCodBanco() != null) {
                bean.setCodBanco(ParaEcomService.getCodBanco());
            }
            if (ParaEcomService.getAgencia() != null) {
                bean.setAgencia(ParaEcomService.getAgencia());
            }
            if (ParaEcomService.getConta() != null) {
                bean.setConta(ParaEcomService.getConta());
            }
        } else if (bean.getFormaPagamento() == 4) {
            if (ParaEcomService.getCodPrazo() != null) {
                bean.setCodPrazo(ParaEcomService.getCodPrazo());
            }
        }
        this.getComplementoDAO().gravar(bean);
    }
        
    private void enviaIdPedido(String idEcom, String codPedido){
        this.getOrderController().updateIdErp(Integer.parseInt(idEcom), Integer.parseInt(codPedido));
    }
    
    private void trataException(Throwable e, HistoricoIntegraERPBean hist){
        String mensage = null;
        if (e.getCause() instanceof ErroSyncException) {
            mensage = ((ErroSyncException) e.getCause()).getMessage();
        }
        if (e.getCause() instanceof ConnectException || e instanceof ConnectException) {
            mensage = "Instabilidade na conexão com a internet.";            
        }
        if (e instanceof InvocationTargetException) {
            mensage = e.getCause().getMessage();
        }
        if (mensage == null || mensage.isEmpty()) {
            mensage = e.getMessage();
        }
        if (mensage == null || mensage.isEmpty()) {
            mensage = e.getClass().getSimpleName();
        }

        try {
            LogEcomErroController.geraErroLog(hist.getId(), "Erro ao efetuar " + hist.getTipoOperacao() + ": " + mensage);
        } catch (Exception ex) {
            logger.error("Erro ao registrar exception", ex);
        }
    }

    /* ******************* */
    
    private OrderController getOrderController() {
        if(this.orderController == null){
            this.orderController = new OrderController();
        }
        return orderController;
    }
    
    private PedidoCERPDAO getPedidoDAO() {
        if(this.pedidoDAO == null){
            this.pedidoDAO = new PedidoCERPDAO();
        }
        return pedidoDAO;
    }

    public ComplementoPedidoEcomDAO getComplementoDAO() {
        if(this.complementoDAO == null){
            this.complementoDAO = new ComplementoPedidoEcomDAO();
        }
        return complementoDAO;
    }

    public HistoricoIntegraDAO getHistoricoDAO() {
        if(this.historicoDAO == null){
            this.historicoDAO = new HistoricoIntegraDAO();
        }
        return historicoDAO;
    }

    public CustomerController getCustomerController() {
        if (this.customerController == null) {
            this.customerController = new CustomerController();
        }
        return customerController;
    }

    public AddressController getAddressController() {
        if (this.addressController == null) {
            this.addressController = new AddressController();
        }
        return addressController;
    }

    public StateController getStateController() {
        if (this.stateController == null) {
            this.stateController = new StateController();
        }
        return stateController;
    }

    public ClienteERPDAO getClienteDAO() {
        if (this.clienteDAO == null) {
            this.clienteDAO = new ClienteERPDAO();
        }
        return clienteDAO;
    }

    public CompclieDAO getCompclieDAO() {
        if (this.compclieDAO == null) {
            this.compclieDAO = new CompclieDAO();
        }
        return compclieDAO;
    }

    public CarrierController getCarrierController() {
        if(this.carrierController == null){
            this.carrierController = new CarrierController();
        }
        return carrierController;
    }

    public PedidoIERPDAO getPedidoItemDAO() {
        if (this.pedidoItemDAO == null) {
            this.pedidoItemDAO = new PedidoIERPDAO();
        }
        return pedidoItemDAO;
    }

    public ProdutoDAO getProdutoDAO() {
        if(this.produtoDAO == null){
            this.produtoDAO = new ProdutoDAO();
        }
        return produtoDAO;
    }

    public ProdGradeERPDAO getProdGradeDAO() {
        if (this.prodGradeDAO == null) {
            this.prodGradeDAO = new ProdGradeERPDAO();
        }
        return prodGradeDAO;
    }

    public ReservaCERPDAO getReservaDAO() {
        if (this.reservaDAO == null) {
            this.reservaDAO = new ReservaCERPDAO();
        }
        return reservaDAO;
    }

    public ReservaIERPDAO getReservaItemDAO() {
        if (this.reservaItemDAO == null) {
            this.reservaItemDAO = new ReservaIERPDAO();
        }
        return reservaItemDAO;
    }
    
    public OrderPrestashopDAO getOrderDAO() {
        if (this.orderDAO == null) {
            this.orderDAO = new OrderPrestashopDAO();
        }
        return orderDAO;
    }
}
