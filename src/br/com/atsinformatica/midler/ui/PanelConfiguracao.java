package br.com.atsinformatica.midler.ui;

import br.com.atsinformatica.erp.entity.AgenciaERPBean;
import br.com.atsinformatica.erp.dao.AgenciaERPDAO;
import br.com.atsinformatica.erp.entity.BancoERPBean;
import br.com.atsinformatica.erp.dao.BancoERPDAO;
import br.com.atsinformatica.erp.entity.ContaBean;
import br.com.atsinformatica.erp.dao.ContaDAO;
import br.com.atsinformatica.erp.dao.FilialERPDAO;
import br.com.atsinformatica.erp.dao.ParaEcomDAO;
import br.com.atsinformatica.erp.dao.ParaFATDAO;
import br.com.atsinformatica.erp.dao.ParaStatusPedidoDAO;
import br.com.atsinformatica.erp.dao.ParaUrlDAO;
import br.com.atsinformatica.erp.dao.PrazoPagamentoDAO;
import br.com.atsinformatica.erp.dao.TipoVendaDAO;
import br.com.atsinformatica.erp.entity.PrazoPagamentoERPBean;
import br.com.atsinformatica.erp.dao.VendedorERPDAO;
import br.com.atsinformatica.erp.entity.FilialERPBean;
import br.com.atsinformatica.erp.entity.ParaEcomBean;
import br.com.atsinformatica.erp.entity.ParaStatusPedidoBean;
import br.com.atsinformatica.erp.entity.ParaUrlWsdlBean;
import br.com.atsinformatica.erp.entity.TipoVendaBean;
import br.com.atsinformatica.erp.entity.VendedorERPBean;
import br.com.atsinformatica.midler.Main;
import br.com.atsinformatica.midler.components.ComponentUtil;
import br.com.atsinformatica.midler.components.renderer.EnumDescritivelTableCellRenderer;
import br.com.atsinformatica.midler.entity.ERPBean;
import br.com.atsinformatica.midler.entity.FileERPBean;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.exception.GenericMiddleException;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.properties.PropertiesManager;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.midler.ui.util.PasswordUtil;
import br.com.atsinformatica.prestashop.clientDAO.GenericPrestashopDAO;
import br.com.atsinformatica.prestashop.controller.OrderStateController;
import br.com.atsinformatica.prestashop.model.list.OrderStates;
import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttribute;
import br.com.atsinformatica.prestashop.model.root.OrderState;
import br.com.atsinformatica.utils.Funcoes;
import br.com.atsinformatica.utils.SingletonUtil;
import br.com.atsinformatica.utils.StringUtil;
import org.apache.log4j.Logger;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.ObjectTableModel;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

/**
 *
 * @author AlexsanderPimenta
 */
public class PanelConfiguracao extends javax.swing.JPanel {

    private PanelPrincipal principal = PanelPrincipal.getInstance();
    private AnnotationResolver resolverUrl = new AnnotationResolver(ParaUrlWsdlBean.class);
    private ObjectTableModel urlModel = new ObjectTableModel(resolverUrl, "codParaUrlWsdl,urlWSDL,urlKey");
    private AnnotationResolver resolverStatus = new AnnotationResolver(ParaStatusPedidoBean.class);
    private ObjectTableModel statusModel = new ObjectTableModel(resolverStatus, "id,nome,modulo,status,principal");
    private String codParaEcom;
    private static Logger logger = Logger.getLogger(PanelConfiguracao.class);

    /**
     * Creates new form PanelConfiguracao
     */
    public PanelConfiguracao() {
        initComponents();
        jToolBar1.setFloatable(false);
        
        //iniciando painel de configurações
        //desabilita campos       
        habDesabCampos(false);
        configuraGrids();
        limparStatus();
        
        //carrega arquivo de configurações        
        carregaArquivoConfig();
        //verifica se banco foi criado em diretorio especificado
        if (jBincluir.isEnabled()) {
            jBincluir.requestFocus();
        }
        if (jBalterar.isEnabled()) {
            jBalterar.requestFocus();
        }
        //jCTipoVenda.setVisible(false);
        //jLTipoVenda.setVisible(false);
        
        /** Implementação temporária */
        jBTSelecionaDirErp1.setVisible(false);
    }

    /**
     * Caso não haja conexão com o bd, deverá ser chamado esse método.
     * @param semConexao
     */
    public PanelConfiguracao(boolean semConexao){
        if (semConexao){
            initComponents();
            jToolBar1.setFloatable(false);
            //iniciando painel de configurações
            //desabilita campos       
            habDesabCampos(false);
            configuraGrids();
            //carrega arquivo de configurações        
            carregaArquivoConfig();
            //verifica se banco foi criado em diretorio especificado
            if (jBincluir.isEnabled()) {
                jBincluir.requestFocus();
            }
            if (jBalterar.isEnabled()) {
                jBalterar.requestFocus();
            }
           // jCTipoVenda.setVisible(false);
           // jLTipoVenda.setVisible(false);      
        }
    }
    
    /**
     * Carrega arquivo de configurações
     */
    private void carregaArquivoConfig() {
        try {
            //verifica se arquivo existe
            if (PropertiesManager.isFileExists()) {
                ParaEcomBean bean = ParaEcomService.getParams();
                try {
                    /** Preenche os comboboxes com as opções */
                    carregaComboFiliais();
                    carregaComboVendedores();
                    carregaComboBanco();
                    carregaPrazos();
                    preencheGrids();
                    
                    /** Preenche os campos com os valores salvos */
                    setCodParaEcom(bean.getCodparaecom());
                    
                    jtQtdemant.setText(String.valueOf(bean.getQtdMantido()));
                    jRSim2.setSelected(bean.getAtivaSincronizacao() == 1);
                    jRNao2.setSelected(bean.getAtivaSincronizacao() == 0);
                    selecionaFilial(bean.getCodEmpresaEcom());
                    selecionaVendedor(bean.getCodVendendEcom());
                    carregaTVenda(bean.getCodTVenda());
                    if (bean.getCodBanco() != null) {
                        selecionaBanco(bean.getCodBanco());
                    }
                    if (bean.getAgencia() != null) {
                        selecionaAgencia(bean.getAgencia());
                    }
                    if (bean.getConta() != null) {
                        selecionaConta(bean.getConta());
                    }
                    if (bean.getCodPrazo() != null) {
                        selecionaPrazo(bean.getCodPrazo());
                    }
                    txtDirImg.setText(bean.getDiretorioImagens() != null ? bean.getDiretorioImagens() : "");
                } catch (Exception ex) {
                    bean = null;
                }
                
                // jBfechar.setEnabled(true);
                Properties config = PropertiesManager.getConfig();
                //carrega arquivo de configurações
                jTdiretorioERP1.setText(PasswordUtil.decrypt(config.getProperty("erp.diretorio")));
                jTUsuarioERP1.setText(PasswordUtil.decrypt(config.getProperty("erp.usuario")));
                jTsenhaERP1.setText(PasswordUtil.decrypt(config.getProperty("erp.senha")));
                
                jBalterar.setEnabled(true);
                jBincluir.setEnabled(false);
                jBfechar.setEnabled(true);
            }
        } catch (Exception e) {
            System.out.println("Erro : " + e);
            //logger.error("Erro ao carregar arquivo de configuração: " + e.getMessage());
        }
    }
    
    private void setaArquivoConfiguracao(ERPBean erpBean) throws IOException {
        Properties config = PropertiesManager.getConfig();
        config.setProperty("erp.diretorio", PasswordUtil.encrypt(erpBean.getCaminho()));
        config.setProperty("erp.usuario", PasswordUtil.encrypt(erpBean.getUsuario()));
        config.setProperty("erp.senha", PasswordUtil.encrypt(erpBean.getSenha()));
        
        PropertiesManager.setConfig(config);
    }
    
    private void carregaTVenda(String codTVenda){
        ParaFATDAO paraFATDAO = new ParaFATDAO();
        String infTVenda = paraFATDAO.retornaInfTVenda();
        if(infTVenda!=null){
            jCTipoVenda.setVisible(infTVenda.equals("S"));
            jLTipoVenda.setVisible(infTVenda.equals("S"));
            jPanel3.updateUI();
            carregaTipoVenda();
            if(codTVenda!=null){
                selecionaTipoVenda(codTVenda);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanelAbas = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jRSim2 = new javax.swing.JRadioButton();
        jRNao2 = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jtQtdemant = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        txtDirImg = new javax.swing.JTextField();
        btnAlterarDirImg = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jCBanco = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jCBAgencia = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jCBConta = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jCBPrazo = new javax.swing.JComboBox();
        jPInvertaloSinc2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jBTSelecionaDirErp1 = new javax.swing.JButton();
        jTdiretorioERP1 = new javax.swing.JTextField();
        jTUsuarioERP1 = new javax.swing.JTextField();
        jTsenhaERP1 = new javax.swing.JPasswordField();
        jLabel14 = new javax.swing.JLabel();
        jBConexao = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jCbFilial = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jCBVendedor = new javax.swing.JComboBox();
        jLTipoVenda = new javax.swing.JLabel();
        jCTipoVenda = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTURL = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTbUrl = new javax.swing.JTable();
        jTUrlKey = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cmbStatusPedido = new javax.swing.JComboBox();
        chPrincipal = new javax.swing.JCheckBox();
        btnSalvarStatus = new javax.swing.JToggleButton();
        btnCancelarStatus = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTbStatus = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        btnSyncStatus = new javax.swing.JToggleButton();
        jBfechar = new javax.swing.JButton();
        jBcancelar = new javax.swing.JButton();
        jBgravar = new javax.swing.JButton();
        jBalterar = new javax.swing.JButton();
        jBincluir = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ativar sincronização", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 11))); // NOI18N
        jPanel4.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        buttonGroup1.add(jRSim2);
        jRSim2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jRSim2.setText("Sim");

        buttonGroup1.add(jRNao2);
        jRNao2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jRNao2.setText("Não");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jRSim2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRNao2)
                .addContainerGap(802, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRSim2)
                    .addComponent(jRNao2))
                .addGap(5, 5, 5))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Qtd. de Registros"));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setText("Apresentados no painel:");

        jtQtdemant.setToolTipText("Quantidade de registros apresentados no painel de histórico.");
        jtQtdemant.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtQtdemantFocusLost(evt);
            }
        });
        jtQtdemant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtQtdemantKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addGap(10, 10, 10)
                .addComponent(jtQtdemant, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtQtdemant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Diretório das imagens", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11))); // NOI18N

        txtDirImg.setEnabled(false);

        btnAlterarDirImg.setText("Alterar");
        btnAlterarDirImg.setEnabled(false);
        btnAlterarDirImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarDirImgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(txtDirImg, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(btnAlterarDirImg)
                .addGap(10, 10, 10))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDirImg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterarDirImg))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(214, Short.MAX_VALUE))
        );

        jPanelAbas.addTab("Sincronização", jPanel8);

        jToolBar1.setBorder(null);
        jToolBar1.setRollover(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Transferência bancária", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel10.setText("Banco:");

        jCBanco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCBancoMouseClicked(evt);
            }
        });
        jCBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBancoActionPerformed(evt);
            }
        });

        jLabel15.setText("Agência:");

        jCBAgencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCBAgenciaMouseClicked(evt);
            }
        });
        jCBAgencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAgenciaActionPerformed(evt);
            }
        });

        jLabel16.setText("Conta:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jCBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jCBConta, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(276, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pagamento na entrega", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel11.setText("Prazo:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBPrazo, 0, 160, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel11)
                .addGap(5, 5, 5)
                .addComponent(jCBPrazo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPInvertaloSinc2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Banco de dados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel12.setText("Caminho completo do banco de dados:");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel13.setText("Usuário:");

        jBTSelecionaDirErp1.setText("Selecionar");
        jBTSelecionaDirErp1.setEnabled(false);
        jBTSelecionaDirErp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTSelecionaDirErp1ActionPerformed(evt);
            }
        });

        jTdiretorioERP1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTdiretorioERP1FocusLost(evt);
            }
        });

        jTsenhaERP1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTsenhaERP1FocusLost(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel14.setText("Senha:");

        jBConexao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/connect.png"))); // NOI18N
        jBConexao.setText("Testar conexão");
        jBConexao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBConexaoActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel22.setText("Ex. local: \"//C:\\local_do_banco\\banco.fb\"");

        jLabel23.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel23.setText("Ex. remoto: \"192.168.0.1/3050:/local_do_banco/banco.fb\"");

        javax.swing.GroupLayout jPInvertaloSinc2Layout = new javax.swing.GroupLayout(jPInvertaloSinc2);
        jPInvertaloSinc2.setLayout(jPInvertaloSinc2Layout);
        jPInvertaloSinc2Layout.setHorizontalGroup(
            jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                        .addGroup(jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel23))
                            .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                                .addGroup(jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                                        .addComponent(jTUsuarioERP1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addGroup(jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                                                .addComponent(jTsenhaERP1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(jBConexao)))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                        .addComponent(jTdiretorioERP1)
                        .addGap(10, 10, 10)
                        .addComponent(jBTSelecionaDirErp1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))))
        );
        jPInvertaloSinc2Layout.setVerticalGroup(
            jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInvertaloSinc2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel12)
                .addGap(5, 5, 5)
                .addGroup(jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTdiretorioERP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBTSelecionaDirErp1))
                .addGap(10, 10, 10)
                .addGroup(jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel22)
                    .addComponent(jLabel14))
                .addGap(3, 3, 3)
                .addGroup(jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addGroup(jPInvertaloSinc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTUsuarioERP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTsenhaERP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBConexao)))
                .addGap(10, 10, 10))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados da empresa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel2.setText("Filial e-commerce");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel6.setText("Vendedor");

        jLTipoVenda.setText("Tipo de venda");

        jCTipoVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTipoVendaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCbFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLTipoVenda)
                    .addComponent(jCTipoVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLTipoVenda)
                        .addGap(5, 5, 5)
                        .addComponent(jCTipoVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(5, 5, 5)
                        .addComponent(jCBVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(5, 5, 5)
                        .addComponent(jCbFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPInvertaloSinc2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPInvertaloSinc2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jPanelAbas.addTab("Banco de dados", jPanel3);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel3.setText("Informe WSDL ou URL do WebService:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/arrow_down.png"))); // NOI18N
        jButton1.setText("Adcionar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTbUrl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Cod.", "Url/WSDL", "Chave"
            }
        ));
        jTbUrl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTbUrlKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTbUrl);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel5.setText("Informe chave do WebService (caso necessário)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTURL, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jTUrlKey, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jTUrlKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelAbas.addTab("WebService", jPanel5);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("ID:");

        txtId.setEditable(false);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Status correspondente:");

        chPrincipal.setText("Principal");

        btnSalvarStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/arrow_down.png"))); // NOI18N
        btnSalvarStatus.setText("Salvar");
        btnSalvarStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarStatusActionPerformed(evt);
            }
        });

        btnCancelarStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/cancel.png"))); // NOI18N
        btnCancelarStatus.setText("Cancelar");
        btnCancelarStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarStatusActionPerformed(evt);
            }
        });

        jTbStatus.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Modulo", "Status correspondente", "Principal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTbStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTbStatusMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTbStatus);
        if (jTbStatus.getColumnModel().getColumnCount() > 0) {
            jTbStatus.getColumnModel().getColumn(0).setMinWidth(100);
            jTbStatus.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTbStatus.getColumnModel().getColumn(0).setMaxWidth(100);
            jTbStatus.getColumnModel().getColumn(3).setMinWidth(250);
            jTbStatus.getColumnModel().getColumn(3).setPreferredWidth(250);
            jTbStatus.getColumnModel().getColumn(3).setMaxWidth(250);
            jTbStatus.getColumnModel().getColumn(4).setMinWidth(80);
            jTbStatus.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTbStatus.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Nome:");

        txtNome.setEditable(false);

        btnSyncStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/refresh.png"))); // NOI18N
        btnSyncStatus.setText(" Sincronizar");
        btnSyncStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSyncStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(10, 10, 10))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbStatusPedido, 0, 366, Short.MAX_VALUE)
                                .addGap(10, 10, 10)
                                .addComponent(chPrincipal))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNome)))
                        .addGap(10, 10, 10)
                        .addComponent(btnSalvarStatus)
                        .addGap(10, 10, 10)
                        .addComponent(btnCancelarStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addComponent(btnSyncStatus)
                        .addContainerGap())))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvarStatus)
                    .addComponent(btnCancelarStatus)
                    .addComponent(btnSyncStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbStatusPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chPrincipal))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelAbas.addTab("Status de pagamento", jPanel10);

        jBfechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/door_out.png"))); // NOI18N
        jBfechar.setText("Fechar");
        jBfechar.setToolTipText("Fechar");
        jBfechar.setFocusable(false);
        jBfechar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBfechar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBfechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBfecharActionPerformed(evt);
            }
        });

        jBcancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/cancel.png"))); // NOI18N
        jBcancelar.setText("Cancelar");
        jBcancelar.setToolTipText("Cancelar");
        jBcancelar.setEnabled(false);
        jBcancelar.setFocusable(false);
        jBcancelar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBcancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBcancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBcancelarActionPerformed(evt);
            }
        });

        jBgravar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/save.png"))); // NOI18N
        jBgravar.setText("Gravar");
        jBgravar.setToolTipText("Gravar");
        jBgravar.setEnabled(false);
        jBgravar.setFocusable(false);
        jBgravar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBgravar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBgravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBgravarActionPerformed(evt);
            }
        });

        jBalterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/pencil3 (2).png"))); // NOI18N
        jBalterar.setText("Alterar");
        jBalterar.setToolTipText("Alterar");
        jBalterar.setEnabled(false);
        jBalterar.setFocusable(false);
        jBalterar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBalterar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBalterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBalterarActionPerformed(evt);
            }
        });

        jBincluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/add.png"))); // NOI18N
        jBincluir.setText("Incluir");
        jBincluir.setToolTipText("Incluir");
        jBincluir.setFocusable(false);
        jBincluir.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBincluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBincluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBincluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelAbas)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBincluir)
                        .addGap(0, 0, 0)
                        .addComponent(jBalterar)
                        .addGap(0, 0, 0)
                        .addComponent(jBgravar)
                        .addGap(0, 0, 0)
                        .addComponent(jBcancelar)
                        .addGap(0, 0, 0)
                        .addComponent(jBfechar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBincluir)
                    .addComponent(jBalterar)
                    .addComponent(jBgravar)
                    .addComponent(jBcancelar)
                    .addComponent(jBfechar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelAbas)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCbLFiliaisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCbLFiliaisMouseClicked
    }//GEN-LAST:event_jCbLFiliaisMouseClicked

    private void jCbLFiliaisMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCbLFiliaisMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCbLFiliaisMousePressed

    private void jBincluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBincluirActionPerformed
        habDesabCampos(true);
        PanelPrincipal.getInstance().setjOperacao("Inclusão");
        principal.setjOperacao("Inclusão");
        Funcoes.limpaTela(jPanel3);
        jBcancelar.setEnabled(true);
        jBgravar.setEnabled(true);
        jBalterar.setEnabled(false);
        jBincluir.setEnabled(false);
        jBTSelecionaDirErp1.setEnabled(true);
        btnAlterarDirImg.setEnabled(true);
        //jTPrazo.setText("1");
    }//GEN-LAST:event_jBincluirActionPerformed

    private void jBalterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBalterarActionPerformed
        habDesabCampos(true);
        principal.setjOperacao("Alteração");
        jBcancelar.setEnabled(true);
        jBgravar.setEnabled(true);
        jBalterar.setEnabled(false);
        jBincluir.setEnabled(false);
        jTdiretorioERP1.requestFocus();
        jBTSelecionaDirErp1.setEnabled(true);
        btnAlterarDirImg.setEnabled(true);
    }//GEN-LAST:event_jBalterarActionPerformed

    private void jBgravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBgravarActionPerformed
        if (!this.validaCampos()) {
            return;
        }
        
        ERPBean erp = new ERPBean();
        Connection conn = null;
        try {
            conn = ConexaoATS.getConnection();   
            conn.setAutoCommit(false);
            
            erp.setCaminho(jTdiretorioERP1.getText());
            erp.setUsuario(jTUsuarioERP1.getText());
            erp.setSenha(String.copyValueOf(jTsenhaERP1.getPassword()));
            setaArquivoConfiguracao(erp);
            
            if (conn == null) {
                Main.MODE_CONFIG = true;
                PanelPrincipal.getInstance().habDesabBotoes(true);                
            } else {
                Main.MODE_CONFIG = false;
                cadastraParaEcom();
                cadastraParaUrl();  
                cadastraParaStatus();
                // Habilitando os botões do painel principal.
                PanelPrincipal.getInstance().habDesabBotoes(true);
            }
            if (PropertiesManager.isFileExists()) {
                jBalterar.setEnabled(true);
                jBincluir.setEnabled(false);
            }
            else {
                jBalterar.setEnabled(false);
                jBincluir.setEnabled(true);
            }
            
            conn.commit();
            
            habDesabCampos(false);                
            jBincluir.setEnabled(false);
            jBcancelar.setEnabled(false);
            jBgravar.setEnabled(false);
            
            JOptionPane.showMessageDialog(null, "Configurações salvas com sucesso! A aplicação será encerrada para "
                    + "que as configurações sejam efetivadas.");
            Funcoes.reiniciaAplicacao(); 
        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {}            
            
            logger.error("Erro ao gravar os parametros.", ex);
            if (ex instanceof GenericMiddleException) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao gravar os parâmetros. Contate o suporte técnico.");
            }
        }
    }//GEN-LAST:event_jBgravarActionPerformed

    private void jBcancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBcancelarActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Deseja cancelar a operação", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            principal.setjOperacao("");
            if (!PropertiesManager.isFileExists()) {
                jBincluir.setEnabled(true);
                jBalterar.setEnabled(false);
                Funcoes.limpaTela(jPanel3);
                Funcoes.limpaTela(jPanel5);
                urlModel.clear();

            } else {
                carregaArquivoConfig();
            }
            habDesabCampos(false);
            //urlModel.clear();
            jBConexao.setEnabled(false);
            jBcancelar.setEnabled(false);
            jBgravar.setEnabled(false);
            jBTSelecionaDirErp1.setEnabled(false);
            btnAlterarDirImg.setEnabled(false);
            jBfechar.setEnabled(true);
            jPanelAbas.setSelectedIndex(0);
            PanelPrincipal.getInstance().habDesabBotoes(true);
        }
    }//GEN-LAST:event_jBcancelarActionPerformed

    private void jBfecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBfecharActionPerformed
        PanelPrincipal.jMainPanel.setEnabled(false);
        principal.setjOperacao("");
        principal.setjStatus("");

        //fecha tudo
        this.removeAll();
        //atualiza ui
        this.updateUI();
        setBorder(null);
    }//GEN-LAST:event_jBfecharActionPerformed

    private void jBConexaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBConexaoActionPerformed
        try {
            try {
                logger.info("Tentando criar a conexão.");
                ConexaoATS.criaNovaConexao(jTdiretorioERP1.getText(), jTUsuarioERP1.getText(), jTsenhaERP1.getText());
                JOptionPane.showMessageDialog(null, "Conexão criada com sucesso.");
                logger.info("Conexão criada com sucesso.");
            } catch (Exception e) {
                logger.error("Falha ao criar conexão.", e);
                Main.MODE_CONFIG = true;
                PanelPrincipal.getInstance().habDesabBotoes(false);
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            
            // Buscando as configurações do banco.
            ERPBean erp = new ERPBean();
            erp.setCaminho(jTdiretorioERP1.getText());
            erp.setUsuario(jTUsuarioERP1.getText());
            erp.setSenha(String.copyValueOf(jTsenhaERP1.getPassword()));
            setaArquivoConfiguracao(erp);
            
            if (ConexaoATS.getConnection() != null) {
                carregaComboFiliais();
                carregaComboVendedores();
                carregaComboBanco();
                carregaPrazos();
                // Busca configurações do banco.
                carregaArquivoConfig();
                jBalterar.setEnabled(false);
            }
            
            //carregaComboContas();
            carregaTVenda(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jBConexaoActionPerformed

    private void jTsenhaERP1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTsenhaERP1FocusLost
        if (jTsenhaERP1.getPassword().length > 1 && jTsenhaERP1.getPassword().length < 5) {
            JOptionPane.showMessageDialog(null, "Informe no mínimo 5 caracteres para a senha.");
            jTsenhaERP1.requestFocus();
        }
    }//GEN-LAST:event_jTsenhaERP1FocusLost

    private void jTdiretorioERP1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTdiretorioERP1FocusLost
        //String str = Funcoes.removeEspacosEmBranco(jTdiretorioERP1.getText());
        //jTdiretorioERP1.setText(str);
    }//GEN-LAST:event_jTdiretorioERP1FocusLost

    private void jBTSelecionaDirErp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTSelecionaDirErp1ActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            FileERPBean file = new FileERPBean();
            file.setCaminho(chooser.getSelectedFile().getAbsolutePath());
            file.setNomeArquivo(chooser.getSelectedFile().getName());
            jTdiretorioERP1.setText((String) file.getCaminho());
        }
    }//GEN-LAST:event_jBTSelecionaDirErp1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ParaUrlWsdlBean paraUrl = new ParaUrlWsdlBean();
        paraUrl.setUrlWSDL(jTURL.getText());
        paraUrl.setUrlKey(jTUrlKey.getText());
        if (paraUrl.getUrlWSDL() != null && paraUrl.getUrlKey() != null) {
            GenericPrestashopDAO prestashop = new GenericPrestashopDAO();
            if (prestashop.getWebService(paraUrl)) {
                JOptionPane.showMessageDialog(null, "Conectado ao WebService com sucesso!");
                urlModel.add(paraUrl);
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao conectar no WebService!");
                jTURL.setText("");
                jTUrlKey.setText("");
                jTURL.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Informe url e chave do WebService.");
            jTURL.requestFocus();
            return;
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTbUrlKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTbUrlKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            urlModel.remove(jTbUrl.getSelectedRow());
        }
    }//GEN-LAST:event_jTbUrlKeyReleased

    private void jtQtdemantFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtQtdemantFocusLost
        if (jtQtdemant.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "Informe uma quantidade de registros a serem mantidos, acima de 0");
            jtQtdemant.requestFocus();
        }
    }//GEN-LAST:event_jtQtdemantFocusLost

    private void jtQtdemantKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtQtdemantKeyTyped
        String caracteres = "0123456789";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_jtQtdemantKeyTyped

    private void jCBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBancoActionPerformed
        jCBConta.removeAllItems();
        if (jCBanco.getSelectedItem() != null) {
            carregaComboAgencia(((BancoERPBean) jCBanco.getSelectedItem()).getCodBanco());
        } else {
            jCBAgencia.removeAllItems();

        }
    }//GEN-LAST:event_jCBancoActionPerformed

    private void jCBAgenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAgenciaActionPerformed
        if (jCBAgencia.getSelectedItem() != null) {
            AgenciaERPBean agencia = (AgenciaERPBean) jCBAgencia.getSelectedItem();
            carregaComboContas(agencia.getCodBanco(), agencia.getCodAgencia());
        } else {
            jCBConta.removeAllItems();
        }
    }//GEN-LAST:event_jCBAgenciaActionPerformed

    private void jCBancoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCBancoMouseClicked
    }//GEN-LAST:event_jCBancoMouseClicked

    private void jCBAgenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCBAgenciaMouseClicked
    }//GEN-LAST:event_jCBAgenciaMouseClicked

    private void btnAlterarDirImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarDirImgActionPerformed
        String inicial = txtDirImg.getText();
        if(inicial.isEmpty() || !new File(inicial).exists()){
            inicial = "/";
        }
        
        JFileChooser jfc = new JFileChooser(inicial);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        Integer opt = jfc.showSaveDialog(this);
        if(opt == JFileChooser.APPROVE_OPTION) {
            txtDirImg.setText(jfc.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnAlterarDirImgActionPerformed

    private void jTbStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTbStatusMouseClicked
        if (evt.getClickCount() == 2 && jTbStatus.isEnabled()) {
            if (!txtId.getText().isEmpty()) {
                int resposta = JOptionPane.showConfirmDialog(null, "As alterações do item selecionado serão "
                        + "pedidas, deseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            
            ParaStatusPedidoBean bean = ((ParaStatusPedidoBean) statusModel.getValue(jTbStatus.getSelectedRow()));
            txtId.setText(String.valueOf(bean.getId()));
            txtNome.setText(bean.getNome());
            cmbStatusPedido.setSelectedItem(bean.getStatus());
            chPrincipal.setSelected(bean.isPrincipal());
            
            cmbStatusPedido.setEnabled(true);
            chPrincipal.setEnabled(true);
            btnSalvarStatus.setEnabled(true);
            btnCancelarStatus.setEnabled(true);
        }
    }//GEN-LAST:event_jTbStatusMouseClicked

    private void btnSyncStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSyncStatusActionPerformed
        if (!statusModel.getData().isEmpty()) {
            int resposta = JOptionPane.showConfirmDialog(null, "Essa ação irá perder alterações não salvas, deseja continuar?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.NO_OPTION) {
                return;
            }
        }

        // Cria o dialogo
        final JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setPreferredSize(new Dimension(300, 20));

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(progress);

        final JDialog dialog = new JDialog();            
        dialog.add(panel);

        dialog.setModal(true);
        dialog.setTitle("Aguarde, sincronizando status...");
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
                
        new Thread(){
            @Override
            public void run() {
                List backup = statusModel.getData();
                try {
                    statusModel.clear();
                    
                    OrderStateController orderController = SingletonUtil.get(OrderStateController.class);
                    OrderStates itens = orderController.listItens();
                    
                    progress.setIndeterminate(false);
                    progress.setMaximum(itens.getOrderStates().size());
                    progress.setValue(0);
                    
                    // Ordena por ID
                    Collections.sort(itens.getOrderStates(), new Comparator<AccessXMLAttribute>() {
                        @Override
                        public int compare(AccessXMLAttribute o1, AccessXMLAttribute o2) {
                            Integer i1 = Integer.parseInt(o1.getId());
                            Integer i2 = Integer.parseInt(o2.getId());
                            
                            return i1.compareTo(i2);
                        }
                    });
                        
                    ParaStatusPedidoDAO dao = SingletonUtil.get(ParaStatusPedidoDAO.class);
                    for (AccessXMLAttribute attr : itens.getOrderStates()) {
                        OrderState order = orderController.get(attr.getId());

                        ParaStatusPedidoBean bean = new ParaStatusPedidoBean();
                        bean.setId(order.getId());
                        bean.setNome(order.getName().getTextName());
                        bean.setModulo(order.getModuleName());

                        ParaStatusPedidoBean temp = dao.abrir(attr.getId());
                        if (temp != null) {
                            bean.setStatus(temp.getStatus());
                            bean.setPrincipal(temp.isPrincipal());
                        }
                        
                        statusModel.add(bean);
                        progress.setValue(progress.getValue() + 1);
                    }
                    
                    JOptionPane.showMessageDialog(null, "Sincronização concluida com sucesso.");
                } catch (Throwable e) {
                    statusModel.setData(backup);
                    
                    logger.error("Erro ao sincronizar status.", e);
                    JOptionPane.showMessageDialog(null, "Falha durante a sincronização.");
                } finally {
                    dialog.setVisible(false);
                }
            }
        }.start();
        dialog.setVisible(true);
    }//GEN-LAST:event_btnSyncStatusActionPerformed

    private void btnCancelarStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarStatusActionPerformed
        this.limparStatus();
    }//GEN-LAST:event_btnCancelarStatusActionPerformed

    private void btnSalvarStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarStatusActionPerformed
        try {
            StatusPedido statusPedido = (StatusPedido) cmbStatusPedido.getSelectedItem();
            if (statusPedido == null) {
                JOptionPane.showMessageDialog(null, "Selecione o status correspondente.");
                return;
            }
            
            List<ParaStatusPedidoBean> dados = statusModel.getData();
            ParaStatusPedidoBean bean = null;
            ParaStatusPedidoBean principal = null;
            
            if (!txtId.getText().isEmpty()) {
                Integer id = Integer.parseInt(txtId.getText());
                for (ParaStatusPedidoBean aux : dados) {
                    if (aux.getId().equals(id)) {
                        bean = aux;
                    } else if(cmbStatusPedido.getSelectedItem().equals(aux.getStatus()) 
                            && aux.isPrincipal()) {
                        principal = aux;
                    }
                }
            }
            
            if (bean == null) {
                JOptionPane.showMessageDialog(null, "Não foi possível localizar esse status.");
                return;
            }            
            if (principal != null && chPrincipal.isSelected()) {
                StringBuilder msg = new StringBuilder()
                        .append("Já existe outro registro (").append(principal.getId()).append(" - ")
                        .append(principal.getNome()).append(") com o status correspondente ")
                        .append("\"").append(statusPedido.getDescricao()).append("\"")
                        .append(" marcado como principal.\nDeseja altera-lo e tornar o atual status (")
                        .append(bean.getId()).append(" - ").append(bean.getNome())
                        .append(") como principal?");
                
                int resposta = JOptionPane.showConfirmDialog(null, msg.toString(), "Confirmação", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.NO_OPTION) {
                    return;
                } else {
                    principal.setPrincipal(false);
                }
            }
           
            bean.setStatus(statusPedido);
            bean.setPrincipal(chPrincipal.isSelected());
            statusModel.setData(dados);
            this.limparStatus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao salvar alterações.");
        }
    }//GEN-LAST:event_btnSalvarStatusActionPerformed

    private void jCTipoVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTipoVendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTipoVendaActionPerformed

    private void limparStatus(){
        txtId.setText("");
        txtNome.setText("");
        cmbStatusPedido.setSelectedItem(null);
        chPrincipal.setSelected(false);
        
        cmbStatusPedido.setEnabled(false);
        chPrincipal.setEnabled(false);
        btnSalvarStatus.setEnabled(false);
        btnCancelarStatus.setEnabled(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterarDirImg;
    private javax.swing.JToggleButton btnCancelarStatus;
    private javax.swing.JToggleButton btnSalvarStatus;
    private javax.swing.JToggleButton btnSyncStatus;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chPrincipal;
    private javax.swing.JComboBox cmbStatusPedido;
    private javax.swing.JButton jBConexao;
    private javax.swing.JButton jBTSelecionaDirErp1;
    private javax.swing.JButton jBalterar;
    private javax.swing.JButton jBcancelar;
    private javax.swing.JButton jBfechar;
    private javax.swing.JButton jBgravar;
    private javax.swing.JButton jBincluir;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jCBAgencia;
    private javax.swing.JComboBox jCBConta;
    private javax.swing.JComboBox jCBPrazo;
    private javax.swing.JComboBox jCBVendedor;
    private javax.swing.JComboBox jCBanco;
    private javax.swing.JComboBox jCTipoVenda;
    private javax.swing.JComboBox jCbFilial;
    private javax.swing.JLabel jLTipoVenda;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPInvertaloSinc2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jPanelAbas;
    private javax.swing.JRadioButton jRNao2;
    private javax.swing.JRadioButton jRSim2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTURL;
    private javax.swing.JTextField jTUrlKey;
    private javax.swing.JTextField jTUsuarioERP1;
    private javax.swing.JTable jTbStatus;
    private javax.swing.JTable jTbUrl;
    private javax.swing.JTextField jTdiretorioERP1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPasswordField jTsenhaERP1;
    private javax.swing.JTextField jtQtdemant;
    private javax.swing.JTextField txtDirImg;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

    private void configuraGrids() {
        jTbUrl.setModel(urlModel);
        jTbUrl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        urlModel.setEditableDefault(false);
        urlModel.setColEditable(1, true);
        urlModel.setColEditable(2, true);
        TableColumn colCod = jTbUrl.getColumnModel().getColumn(0);
        colCod.setPreferredWidth(70);
        TableColumn colUrl = jTbUrl.getColumnModel().getColumn(1);
        colUrl.setPreferredWidth(311);
        TableColumn colUrlKey = jTbUrl.getColumnModel().getColumn(2);
        colUrlKey.setPreferredWidth(311);
        
        // Configura o painel de status
        ComponentUtil.preencheCompo(cmbStatusPedido, StatusPedido.values());
        jTbStatus.setModel(statusModel); // Configura o grid
        jTbStatus.getColumnModel().getColumn(0).setMaxWidth(100);
        jTbStatus.getColumnModel().getColumn(0).setMinWidth(100);
        jTbStatus.getColumnModel().getColumn(3).setCellRenderer(new EnumDescritivelTableCellRenderer());
        jTbStatus.getColumnModel().getColumn(3).setMaxWidth(250);
        jTbStatus.getColumnModel().getColumn(3).setMinWidth(250);
        jTbStatus.getColumnModel().getColumn(4).setMaxWidth(80);
        jTbStatus.getColumnModel().getColumn(4).setMinWidth(80);
    }

    private void preencheGrids() {
        try {
            ParaUrlDAO dao = SingletonUtil.get(ParaUrlDAO.class);
            urlModel.clear();
            List<ParaUrlWsdlBean> listaParaUrl = dao.listaTodos();
            if (!listaParaUrl.isEmpty()) {
                for (ParaUrlWsdlBean bean : listaParaUrl) {
                    urlModel.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            ParaStatusPedidoDAO dao = SingletonUtil.get(ParaStatusPedidoDAO.class);
            statusModel.clear();
            List<ParaStatusPedidoBean> lista = dao.listaTodos();
            if (lista != null) {
                for (ParaStatusPedidoBean bean : lista) {
                    statusModel.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cadastra registro na tabela ParaECOM
     */
    private void cadastraParaEcom() {
        ParaEcomBean paraEcom = new ParaEcomBean();
        ParaEcomDAO dao = new ParaEcomDAO();
        paraEcom.setQtdMantido(Integer.parseInt(jtQtdemant.getText()));
        paraEcom.setAtivaSincronizacao(Funcoes.retornaValorNum(jRSim2.isSelected()));
        paraEcom.setCodparaecom(getCodParaEcom());
        paraEcom.setCodEmpresaEcom(((FilialERPBean) jCbFilial.getSelectedItem()).getCodEmpresa());
        paraEcom.setCodVendendEcom(((VendedorERPBean) jCBVendedor.getSelectedItem()).getCodVendedor());            
        paraEcom.setDiretorioImagens(txtDirImg.getText());

        if (jCBanco.getSelectedItem() != null) {
            paraEcom.setCodBanco(((BancoERPBean) jCBanco.getSelectedItem()).getCodBanco());
        }
        if (jCBAgencia.getSelectedItem() != null) {
            paraEcom.setAgencia(((AgenciaERPBean) jCBAgencia.getSelectedItem()).getCodAgencia());
        }
        if (jCBConta.getSelectedItem() != null) {
            paraEcom.setConta(((ContaBean) jCBConta.getSelectedItem()).getConta());
        }
        if (jCBPrazo.getSelectedItem() != null) {
            paraEcom.setCodPrazo(((PrazoPagamentoERPBean) jCBPrazo.getSelectedItem()).getCodPrazo());
        }
        if(jCTipoVenda.isVisible()){
           if(!jCTipoVenda.getSelectedItem().equals("")){
              paraEcom.setCodTVenda(((TipoVendaBean)jCTipoVenda.getSelectedItem()).getCodTVenda());
           }
        }

        if (paraEcom.getCodparaecom() == null) {
            dao.gravar(paraEcom);
        } else{
            dao.alterar(paraEcom);
        }
    }

    private void cadastraParaUrl() {
        List<ParaUrlWsdlBean> listaParaUrl = urlModel.getData();
        ParaUrlDAO dao = new ParaUrlDAO();
        for (ParaUrlWsdlBean paraUrl : listaParaUrl) {
            if(paraUrl.getUrlKey() != null){
                paraUrl.setUrlKey(paraUrl.getUrlKey().replaceAll("\\W", ""));
            }

            dao.gravar(paraUrl);
        }
        verificaDeletados(listaParaUrl);
    }
    
    private void cadastraParaStatus() {
        List<ParaStatusPedidoBean> lista = statusModel.getData();
        ParaStatusPedidoDAO dao = SingletonUtil.get(ParaStatusPedidoDAO.class);
        for (ParaStatusPedidoBean bean : lista) {
            if (bean.getStatus() == null) {
                throw new GenericMiddleException("Não foi informado status relacionado para um ou mais status de pagamento.");
            }
            dao.gravar(bean);
        }
        dao.deletaDiferentesDe(lista);
    }

    /**
     * @return the codParaEcom
     */
    public String getCodParaEcom() {
        return codParaEcom;
    }

    /**
     * @param codParaEcom the codParaEcom to set
     */
    public void setCodParaEcom(String codParaEcom) {
        this.codParaEcom = codParaEcom;
    }

    /**
     * Verifica quais itens foram deletados na grid
     *
     * @param listaData
     */
    private void verificaDeletados(List<ParaUrlWsdlBean> listaGrid) {
        ParaUrlDAO dao = new ParaUrlDAO();
        List<ParaUrlWsdlBean> listaParaUrl = dao.listaTodos();
        for (ParaUrlWsdlBean urlErp : listaParaUrl) {
            if (!listaGrid.contains(urlErp)) {
                dao.deletar(urlErp.getCodParaUrlWsdl());
            }
        }
    }

    /**
     * Verifica se campos obrigatorios estão vazios
     */
    public boolean validaCampos() {
        Set<String> msg = new LinkedHashSet<>();
        
        this.validaCampo(jTdiretorioERP1, "O diretório do banco ERP", msg, 0);
        this.validaCampo(jTUsuarioERP1, "O usuário do banco ERP", msg, 0);
        this.validaCampo(jTsenhaERP1, "A senha do banco ERP", msg, 0);
        this.validaCampo(jCbFilial, "A filial", msg, 0);
        this.validaCampo(jCBVendedor, "O vendedor", msg, 0);
        this.validaCampo(jCBanco, "O banco", msg, 0);
        this.validaCampo(jCBAgencia, "A agência", msg, 0);
        this.validaCampo(jCBConta, "A conta", msg, 0);
        this.validaCampo(jCBPrazo, "O prazo", msg, 0);
        
        if(jCBPrazo.getSelectedItem() !=null){
            if (temMaisQue1Prazo((PrazoPagamentoERPBean) jCBPrazo.getSelectedItem())) {
                this.requestFocus(jCBPrazo, 0, msg);
                msg.add("O prazo de pagamento deve ter apenas 1 prazo ou 1 parcela.");
            }
        }
        
        if (jCTipoVenda.isVisible()) {
            this.validaCampo(jCTipoVenda, "O tipo de venda do e-commerce", msg, 0);
        }
        
        if (urlModel.getData().isEmpty()) {
            this.requestFocus(jTURL, 1, msg);
            msg.add("Informe ao menos um WSDL ou URL do WebService.");
        } else {
            List<ParaUrlWsdlBean> listaParaUrl = urlModel.getData();
            for (ParaUrlWsdlBean paraUrl : listaParaUrl) {
                String url = paraUrl.getUrlKey().replaceAll("\\W", "");
                paraUrl.setUrlKey(url);
                
                if (paraUrl.getUrlWSDL().trim().isEmpty() || url.isEmpty()) {
                    this.requestFocus(jTURL, 1, msg);
                    msg.add("Informe URL/WSDL e chave para todos os webservices cadastrados.");
                }
            }
        }
        
        if (txtDirImg.getText().isEmpty()) {
            this.requestFocus(btnAlterarDirImg, 0, msg);
            msg.add("O diretório das imagens é de preenchimento obrigatório.");
        } else if (!new File(txtDirImg.getText()).exists()) {
            this.requestFocus(btnAlterarDirImg, 0, msg);
            msg.add("O diretório das imagens informado é inválido.");
        }
        
        if (!msg.isEmpty()) {
            JOptionPane.showMessageDialog(null, StringUtil.unir("\n", msg));
            return false;
        } else {
            return true;
        }
    }
    
    private void validaCampo(JTextComponent component, String name, Set<String> msg, int tab){
        if (component.getText().trim().isEmpty()) {
            this.requestFocus(component, tab, msg);
            msg.add(MessageFormat.format("{0} é de preenchimento obrigatório.", name));
        }
    }
    
    private void validaCampo(JComboBox component, String name, Set<String> msg, int tab){
        if (component.getSelectedItem() == null) {
            this.requestFocus(component, tab, msg);
            msg.add(MessageFormat.format("{0} é de preenchimento obrigatório.", name));
        }
    }
    
    private void requestFocus(JComponent component, int tab, Set<String> msg){
        if(msg.isEmpty()){ // Primeiro problema
            component.requestFocus();
            jPanelAbas.setSelectedIndex(tab);
       }
    }
    
    /**
     * Verifica de prago de pagamento possui mais de 1 prazo ou parcelas 
     * É permitido apenas a gravação de prazos que possuam 1 parcela ou 1 prazo
     * @param prazo
     * @return 
     */
    private boolean temMaisQue1Prazo(PrazoPagamentoERPBean prazo){
        int countPrazos = 0;
        if(prazo!=null){
            if(prazo.getPrazo1() != 0)
                countPrazos +=1;
            if(prazo.getPrazo2()!=0)
                countPrazos += 1;
            if(prazo.getPrazo3()!=0)
                countPrazos += 1;
            if(prazo.getPrazo4()!=0)
                countPrazos += 1;
            if(prazo.getPrazo5()!=0)
                countPrazos += 1;
            if(prazo.getPrazo6()!=0)
                countPrazos += 1;
            if(prazo.getPrazo7()!=0)
                countPrazos += 1;
            if(countPrazos > 1 || prazo.getNumparcelas()>1)
                return true;
        }
        return false;
    }

    private void carregaComboFiliais() {
        try {
            List<FilialERPBean> filiais = new FilialERPDAO().listaTodos();
            if (filiais != null) {
                jCbFilial.removeAllItems();
                for (FilialERPBean filial : filiais) {
                    jCbFilial.addItem(filial);
                }
            } else {
                jCbFilial.removeAll();
                jCbFilial.removeAllItems();
            }
        } catch (Exception e) {
        }
    }

    private void carregaComboVendedores() {
        try {
            List<VendedorERPBean> vendedores = new VendedorERPDAO().listaTodos();
            if (vendedores != null) {
                jCBVendedor.removeAllItems();
                for (VendedorERPBean vendedor : vendedores) {
                    jCBVendedor.addItem(vendedor);
                }
            } else {
                jCBVendedor.removeAll();
                jCBVendedor.removeAllItems();
            }
        } catch (Exception e) {
        }
    }
    
    private void carregaComboContas(String codBanco, String codAgencia) {
        ContaDAO contaDao = new ContaDAO();
        try {
            List<ContaBean> contas = contaDao.listaPorBancoAgencia(codBanco, codAgencia);
            if (contas != null) {
                jCBConta.removeAllItems();
                jCBConta.addItem(null);
                for (ContaBean conta : contas) {
                    jCBConta.addItem(conta);
                }
            } else {
                jCBConta.removeAll();
                jCBConta.removeAllItems();
            }
        } catch (Exception e) {
        }
    }

    private void carregaPrazos() {
        PrazoPagamentoDAO prazoDAO = new PrazoPagamentoDAO();
        try {
            List<PrazoPagamentoERPBean> prazos = prazoDAO.listaTodos();
            if (prazos != null) {
                jCBPrazo.removeAllItems();
                jCBPrazo.addItem(null);
                for (PrazoPagamentoERPBean prazo : prazos) {
                    jCBPrazo.addItem(prazo);
                }
            } else {
                jCBPrazo.removeAll();
                jCBPrazo.removeAllItems();
            }
        } catch (Exception e) {
        }
    }

    public void selecionaFilial(String codFilial) {
        for (int i = 0; i < jCbFilial.getItemCount(); i++) {
            if (codFilial.equals(((FilialERPBean) jCbFilial.getItemAt(i)).getCodEmpresa())) {
                jCbFilial.setSelectedIndex(i);
            }
        }
    }

    public void selecionaVendedor(String codVendedor) {
        for (int i = 0; i < jCBVendedor.getItemCount(); i++) {
            if (codVendedor.equals(((VendedorERPBean) jCBVendedor.getItemAt(i)).getCodVendedor())) {
                jCBVendedor.setSelectedIndex(i);
            }
        }
    }

    public void selecionaConta(String conta) {
        for (int i = 0; i < jCBConta.getItemCount(); i++) {
            if (jCBConta.getItemAt(i) !=null) {
                if (conta.equals(((ContaBean) jCBConta.getItemAt(i)).getConta())) {
                    jCBConta.setSelectedIndex(i);
                }
            }

        }
    }

    private void habDesabCampos(boolean hab) {
        Funcoes.habilitaDesabCampos(jPInvertaloSinc2, hab);
        Funcoes.habilitaDesabCampos(jPanel1, hab);
        Funcoes.habilitaDesabCampos(jPanel3, hab);
        Funcoes.habilitaDesabCampos(jPanel4, hab);
        Funcoes.habilitaDesabCampos(jPanel5, hab);
        Funcoes.habilitaDesabCampos(jPanel2, hab);
        Funcoes.habilitaDesabCampos(jPanel6, hab);
        Funcoes.habilitaDesabCampos(jPanel7, hab);
        Funcoes.habilitaDesabCampos(jPanel8, hab);
        
        jTbUrl.setEnabled(hab);
        jTbStatus.setEnabled(hab);
        btnSyncStatus.setEnabled(hab);
    }

    private void carregaComboBanco() {
        BancoERPDAO bancoDao = new BancoERPDAO();
        try {
            List<BancoERPBean> bancos = bancoDao.listaTodos();
            if (bancos != null) {
                jCBanco.removeAllItems();
                jCBanco.addItem(null);
                for (BancoERPBean banco : bancos) {
                    jCBanco.addItem(banco);
                }
            } else {
                jCBanco.removeAll();
                jCBanco.removeAllItems();
            }

        } catch (Exception e) {
        }
    }

    private void carregaComboAgencia(String codBanco) {
        AgenciaERPDAO agenciaDAO = new AgenciaERPDAO();
        try {
            List<AgenciaERPBean> agencias = agenciaDAO.listByCodBanco(codBanco);
            if (agencias != null) {
                jCBAgencia.removeAllItems();
                jCBAgencia.addItem(null);
                for (AgenciaERPBean agencia : agencias) {
                    jCBAgencia.addItem(agencia);
                }
            } else {
                jCBAgencia.removeAll();
                jCBAgencia.removeAllItems();
            }
        } catch (Exception e) {
        }
    }

    private void selecionaBanco(String codBanco) {
        for (int i = 0; i < jCBanco.getItemCount(); i++) {
            if (jCBanco.getItemAt(i) !=null) {
                if (codBanco.equals(((BancoERPBean) jCBanco.getItemAt(i)).getCodBanco())) {
                    jCBanco.setSelectedIndex(i);
                }
            }

        }
    }

    private void selecionaAgencia(String agencia) {
        for (int i = 0; i < jCBAgencia.getItemCount(); i++) {
            if (jCBAgencia.getItemAt(i) !=null) {
                if (agencia.equals(((AgenciaERPBean) jCBAgencia.getItemAt(i)).getCodAgencia())) {
                    jCBAgencia.setSelectedIndex(i);
                }
            }

        }
    }

    private void selecionaPrazo(String codPrazo) {
        for (int i = 0; i < jCBPrazo.getItemCount(); i++) {
            if (jCBPrazo.getItemAt(i) !=null) {
                if (codPrazo.equals(((PrazoPagamentoERPBean) jCBPrazo.getItemAt(i)).getCodPrazo())) {
                    jCBPrazo.setSelectedIndex(i);
                }
            }

        }
    }

    private void carregaTipoVenda() {
        TipoVendaDAO tipoVendaDAO = new TipoVendaDAO();
        try {
            List<TipoVendaBean> tipoVendas = tipoVendaDAO.listaTodos();
            if (tipoVendas != null) {
                jCTipoVenda.removeAllItems();
                jCTipoVenda.addItem(null);
                for (TipoVendaBean tipoVendaBean : tipoVendas) {
                    jCTipoVenda.addItem(tipoVendaBean);
                }
            } else {
                jCTipoVenda.removeAll();
                jCTipoVenda.removeAllItems();
            }
        } catch (Exception e) {
        }
    }


    private void selecionaTipoVenda(String codTVenda) {
        for (int i = 0; i < jCTipoVenda.getItemCount(); i++) {
            if (jCTipoVenda.getItemAt(i) !=null) {
                if (codTVenda.equals(((TipoVendaBean) jCTipoVenda.getItemAt(i)).getCodTVenda())) {
                    jCTipoVenda.setSelectedIndex(i);
                }
            }

        }
    }
}
