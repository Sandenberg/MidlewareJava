package br.com.atsinformatica.midler.ui;

import br.com.atsinformatica.erp.controller.CategoriaERPController;
import br.com.atsinformatica.erp.controller.ProdutoERPController;
import br.com.atsinformatica.erp.controller.trigger.TriggerController;
import br.com.atsinformatica.midler.components.renderer.DateTimeCellRenderer;
import br.com.atsinformatica.erp.dao.HistoricoIntegraDAO;
import br.com.atsinformatica.erp.dao.ProdutoDAO;
import br.com.atsinformatica.erp.entity.HistoricoIntegraERPBean;
import br.com.atsinformatica.erp.entity.UsuarioERPBean;
import br.com.atsinformatica.midler.components.ComponentUtil;
import br.com.atsinformatica.midler.components.HistoricoIntegraResolver;
import br.com.atsinformatica.midler.components.renderer.EnumDescritivelTableCellRenderer;
import br.com.atsinformatica.midler.components.renderer.RightCellRenderer;
import br.com.atsinformatica.midler.components.renderer.StatusItemCellRenderer;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.entity.enumeration.TipoOperacao;
import br.com.atsinformatica.midler.entity.enumeration.TipoPeriodo;
import br.com.atsinformatica.midler.entity.filter.HistoricoFilter;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.service.SyncService;
import br.com.atsinformatica.midler.ui.util.SwingThread;
import br.com.atsinformatica.prestashop.controller.CategoriaController;
import br.com.atsinformatica.prestashop.controller.ProductController;
import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttribute;
import br.com.atsinformatica.prestashop.model.list.prestashop.WSItens;
import br.com.atsinformatica.prestashop.model.root.Category;
import br.com.atsinformatica.prestashop.model.root.Product;
import br.com.atsinformatica.utils.DateUtil;
import br.com.atsinformatica.utils.ImageUtil;
import br.com.atsinformatica.utils.SingletonUtil;
import br.com.atsinformatica.utils.StringUtil;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.ObjectTableModel;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultCaret;
import org.apache.log4j.Logger;
import org.firebirdsql.jca.FBXAException;

/**
 *
 * @author AlexsanderPimenta
 */
public class PanelHistorico extends javax.swing.JPanel {
    private final static Logger logger = Logger.getLogger(PanelHistorico.class);
    
    private final HistoricoIntegraDAO historicoIntegraDAO; // DAO de histórico
    private final AnnotationResolver resolverSinc; //Resolver para grid sincronizar
    private final ObjectTableModel modelSincronizar; //Model para grid  sincronizar
    private final String[] fields;
    private boolean processandoSync;
    private HistoricoIntegraERPBean linhaSelecionada;
    private HistoricoFilter historicoFilter;

    public PanelHistorico() {
        initComponents();   
        /* Inicia propriedades para grid */
        resolverSinc = new AnnotationResolver(HistoricoIntegraResolver.class);
        fields = new String[]{"id", "tipoEntidade", "codigoEntidade", "dataEntrada", 
            "dataIntegracao", "tipoOperacao", "status", "outrasInformacoes"};
        modelSincronizar = new ObjectTableModel(resolverSinc, StringUtil.unir(",", fields));
        
        /* Instancia os DAOs e Services */
        historicoIntegraDAO = SingletonUtil.get(HistoricoIntegraDAO.class);
        
        configuraGridHistorico();
        configuraMenuPopup();
        
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        if (!UsuarioERPBean.getInstance().isMestre()){
            jTabbedPane1.remove(1);
        }
        
        /** Preenche os combos do filtro */
        ComponentUtil.preencheCompo(cmbTipo, TipoEntidade.values());
        ComponentUtil.preencheCompo(cmbTipoOperacao, TipoOperacao.values());
        ComponentUtil.preencheCompo(cmbTipoPeriodo, TipoPeriodo.values());
        ComponentUtil.preencheCompo(cmbStatus, StatusIntegracao.values());
        lbErro.setVisible(false);
        /* Permite limpar o campo */
        txtDataInicio.setFocusLostBehavior(JFormattedTextField.PERSIST);
        txtDataFinal.setFocusLostBehavior(JFormattedTextField.PERSIST);
        
        this.limpaFiltros();
        
        if (!ParaEcomService.getAtivaSincronizacao()) {
            jBtSincronizacao.setEnabled(false);
            jBtSincronizacao.setText("Sincronização desabilitada");
        }
        
        // Inicializa legenda
        if (panelLegenda.getComponents() != null) {
            for (Component component : panelLegenda.getComponents()) {
                try {
                    JLabel label = (JLabel) component;
                    StatusIntegracao status = StatusIntegracao.valueOf(label.getText());
                    label.setText(status.getDescricao());
                    
                    if (label.getIcon() != null && status != StatusIntegracao.EM_ANDAMENTO) {
                        ImageIcon img = (ImageIcon) label.getIcon();
                        label.setIcon(ImageUtil.resizeImage(13, 13, img));
                    }
                } catch (Exception e) {
                    component.setVisible(false);
                }
            }
        }
        
        this.carregaUltimosRegistros();
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTbSincronizar = new javax.swing.JTable();
        jBtSincronizacao = new javax.swing.JButton();
        jBfechar1 = new javax.swing.JButton();
        lbErroAtualizar = new javax.swing.JLabel();
        panelLegenda = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        panelFiltros = new javax.swing.JPanel();
        panelFiltros1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox();
        lbTipoPeriodo = new javax.swing.JLabel();
        cmbTipoPeriodo = new javax.swing.JComboBox();
        panelFiltros2 = new javax.swing.JPanel();
        lbTipoOperacao = new javax.swing.JLabel();
        lbPeriodo = new javax.swing.JLabel();
        cmbTipoOperacao = new javax.swing.JComboBox();
        txtDataInicio = new javax.swing.JFormattedTextField();
        lbAte = new javax.swing.JLabel();
        txtDataFinal = new javax.swing.JFormattedTextField();
        panelFiltros3 = new javax.swing.JPanel();
        lbStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox();
        jBtSincronizacao1 = new javax.swing.JButton();
        lbErro = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel1.setPreferredSize(new java.awt.Dimension(1135, 290));

        jTbSincronizar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Entidade", "Cód. Entidade", "Data entrada", "Data importacao", "Tipo da operação", "Status", "Outras info"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTbSincronizar.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jTbSincronizar.getTableHeader().setResizingAllowed(false);
        jTbSincronizar.getTableHeader().setReorderingAllowed(false);
        jTbSincronizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTbSincronizarMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTbSincronizarMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTbSincronizar);

        jBtSincronizacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/refresh.png"))); // NOI18N
        jBtSincronizacao.setText("Sincronizar agora");
        jBtSincronizacao.setToolTipText("");
        jBtSincronizacao.setActionCommand("");
        jBtSincronizacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtSincronizacaoActionPerformed(evt);
            }
        });

        jBfechar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/door_out.png"))); // NOI18N
        jBfechar1.setText("Fechar");
        jBfechar1.setToolTipText("Fechar");
        jBfechar1.setFocusable(false);
        jBfechar1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBfechar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBfechar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBfechar1ActionPerformed(evt);
            }
        });

        lbErroAtualizar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lbErroAtualizar.setForeground(new java.awt.Color(255, 0, 0));
        lbErroAtualizar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbErroAtualizar.setText("#");
        lbErroAtualizar.setPreferredSize(new java.awt.Dimension(8, 15));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/notification_error.png"))); // NOI18N
        jLabel4.setText("ERRO");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/Ok-icon.png"))); // NOI18N
        jLabel6.setText("SINCRONIZADO");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/spinner.gif"))); // NOI18N
        jLabel5.setText("EM_ANDAMENTO");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/waiting.png"))); // NOI18N
        jLabel3.setText("PENDENTE");

        javax.swing.GroupLayout panelLegendaLayout = new javax.swing.GroupLayout(panelLegenda);
        panelLegenda.setLayout(panelLegendaLayout);
        panelLegendaLayout.setHorizontalGroup(
            panelLegendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLegendaLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addGap(15, 15, 15)
                .addComponent(jLabel5)
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addGap(0, 0, 0))
        );
        panelLegendaLayout.setVerticalGroup(
            panelLegendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLegendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panelLegenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbErroAtualizar, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBtSincronizacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBfechar1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jBtSincronizacao)
                        .addComponent(lbErroAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jBfechar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelLegenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        jTabbedPane1.addTab("ERP", jPanel1);

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane2.setViewportView(textArea);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/refresh.png"))); // NOI18N
        jButton1.setText("Atualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        progressBar.setForeground(new java.awt.Color(153, 9, 12));
        progressBar.setMaximum(1000);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(10, 10, 10))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jButton1)
                .addGap(5, 5, 5))
        );

        jTabbedPane1.addTab("Loja Virtual", jPanel2);

        panelFiltros.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        jLabel1.setText("Tipo:");

        cmbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoItemStateChanged(evt);
            }
        });

        lbTipoPeriodo.setText("Tipo de período:");

        cmbTipoPeriodo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoPeriodoItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelFiltros1Layout = new javax.swing.GroupLayout(panelFiltros1);
        panelFiltros1.setLayout(panelFiltros1Layout);
        panelFiltros1Layout.setHorizontalGroup(
            panelFiltros1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltros1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltros1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbTipoPeriodo)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFiltros1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbTipoPeriodo, 0, 158, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelFiltros1Layout.setVerticalGroup(
            panelFiltros1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltros1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltros1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFiltros1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTipoPeriodo)
                    .addComponent(cmbTipoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbTipoOperacao.setText("Tipo de operação:");

        lbPeriodo.setText("Período:");

        cmbTipoOperacao.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoOperacaoItemStateChanged(evt);
            }
        });

        try {
            txtDataInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataInicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDataInicio.setText("__/__/____");
        txtDataInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataInicioFocusLost(evt);
            }
        });

        lbAte.setText("Até");

        try {
            txtDataFinal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataFinal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDataFinal.setText("__/__/____");
        txtDataFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataFinalFocusLost(evt);
            }
        });

        javax.swing.GroupLayout panelFiltros2Layout = new javax.swing.GroupLayout(panelFiltros2);
        panelFiltros2.setLayout(panelFiltros2Layout);
        panelFiltros2Layout.setHorizontalGroup(
            panelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltros2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbPeriodo)
                    .addComponent(lbTipoOperacao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbTipoOperacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFiltros2Layout.createSequentialGroup()
                        .addComponent(txtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbAte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                        .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelFiltros2Layout.setVerticalGroup(
            panelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltros2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTipoOperacao)
                    .addComponent(cmbTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPeriodo)
                    .addComponent(txtDataInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAte)
                    .addComponent(txtDataFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        lbStatus.setText("Status:");

        cmbStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbStatusItemStateChanged(evt);
            }
        });

        jBtSincronizacao1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/refresh.png"))); // NOI18N
        jBtSincronizacao1.setText("Atualiza lista");
        jBtSincronizacao1.setToolTipText("");
        jBtSincronizacao1.setActionCommand("");
        jBtSincronizacao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtSincronizacao1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFiltros3Layout = new javax.swing.GroupLayout(panelFiltros3);
        panelFiltros3.setLayout(panelFiltros3Layout);
        panelFiltros3Layout.setHorizontalGroup(
            panelFiltros3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltros3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFiltros3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFiltros3Layout.createSequentialGroup()
                        .addComponent(jBtSincronizacao1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelFiltros3Layout.createSequentialGroup()
                        .addComponent(cmbStatus, 0, 179, Short.MAX_VALUE)
                        .addGap(7, 7, 7))))
        );
        panelFiltros3Layout.setVerticalGroup(
            panelFiltros3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltros3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltros3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbStatus)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtSincronizacao1)
                .addContainerGap())
        );

        lbErro.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lbErro.setForeground(new java.awt.Color(255, 0, 0));
        lbErro.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbErro.setText("Erro na data");
        lbErro.setToolTipText("");
        lbErro.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout panelFiltrosLayout = new javax.swing.GroupLayout(panelFiltros);
        panelFiltros.setLayout(panelFiltrosLayout);
        panelFiltrosLayout.setHorizontalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosLayout.createSequentialGroup()
                .addComponent(panelFiltros1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFiltros2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFiltros3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbErro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelFiltrosLayout.setVerticalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosLayout.createSequentialGroup()
                .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panelFiltros3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelFiltros1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelFiltros2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbErro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1011, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /** Duplo clique na linha */
    private void jTbSincronizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTbSincronizarMouseClicked
        if (evt.getClickCount() == 2) {
            HistoricoIntegraERPBean bean = ((HistoricoIntegraResolver) modelSincronizar
                    .getValue(jTbSincronizar.getSelectedRow())).getBean();
            if (bean.getStatus().equals(StatusIntegracao.ERRO)) {
                LogErrosDialog dialog = new LogErrosDialog(null, true);
                dialog.refreshData(bean.getId());
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_jTbSincronizarMouseClicked

    /* Botão fechar */
    private void jBfechar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBfechar1ActionPerformed
        PanelPrincipal.jMainPanel.setEnabled(false);
        PanelPrincipal.getInstance().setjStatus("");
        //fecha tudo
        this.removeAll();
        //atualiza ui
        this.updateUI();
        setBorder(null);
    }//GEN-LAST:event_jBfechar1ActionPerformed

    /* Botão atualiza */
    private void jBtSincronizacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtSincronizacaoActionPerformed
        carregaUltimosRegistros();        
        SyncService.forceRun();
    }//GEN-LAST:event_jBtSincronizacaoActionPerformed

    /* Invoca a atualização de base de dados */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(!processandoSync){
            SwingThread thread = new SwingThread(this, "iniciaSincLojaERP");
            thread.execute();
            jButton1.setEnabled(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoItemStateChanged
        this.getHistoricoFilter().setEntidade((TipoEntidade) cmbTipo.getSelectedItem());
    }//GEN-LAST:event_cmbTipoItemStateChanged

    private void cmbTipoOperacaoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoOperacaoItemStateChanged
        this.getHistoricoFilter().setTipoOperacao((TipoOperacao) cmbTipoOperacao.getSelectedItem());
    }//GEN-LAST:event_cmbTipoOperacaoItemStateChanged

    private void cmbTipoPeriodoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoPeriodoItemStateChanged
        this.getHistoricoFilter().setTipoPeriodo( (TipoPeriodo) cmbTipoPeriodo.getSelectedItem());
        this.validaTipoPeriodo();
    }//GEN-LAST:event_cmbTipoPeriodoItemStateChanged

    private void cmbStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbStatusItemStateChanged
        this.getHistoricoFilter().setStatus((StatusIntegracao) cmbStatus.getSelectedItem());
    }//GEN-LAST:event_cmbStatusItemStateChanged

    private void txtDataInicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataInicioFocusLost
        if(!this.campoDataVazio(txtDataInicio)){
            if (!DateUtil.validaData(txtDataInicio.getText())) {
                informaErro("Data de início é inválida.");
                this.getHistoricoFilter().setDataInicio(null);
                return;
            }
            
            this.getHistoricoFilter().setDataInicio(DateUtil.stringParaData(txtDataInicio.getText()));
        }
        this.validaTipoPeriodo();
    }//GEN-LAST:event_txtDataInicioFocusLost

    private void txtDataFinalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataFinalFocusLost
        if(!this.campoDataVazio(txtDataFinal)){
            if(!DateUtil.validaData(txtDataFinal.getText())){
                informaErro("Data final é inválida.");
                this.getHistoricoFilter().setDataFinal(null);
                return;
            }
            if (!this.campoDataVazio(txtDataInicio) &&
                DateUtil.validaData(txtDataInicio.getText()) &&
                DateUtil.comparaDatas(DateUtil.stringParaData(txtDataInicio.getText()), DateUtil.stringParaData(txtDataFinal.getText()))) {
                informaErro("A data final não pode ser maior que a inicial.");
                this.getHistoricoFilter().setDataFinal(null);
                return;
            }
            
            this.getHistoricoFilter().setDataFinal(DateUtil.stringParaData(txtDataFinal.getText()));
        }
        this.validaTipoPeriodo();
    }//GEN-LAST:event_txtDataFinalFocusLost

    /**
     * Armazena a linha que foi precionado o botão direito do mouse
     * @param evt 
     */
    private void jTbSincronizarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTbSincronizarMousePressed
        Point point = evt.getPoint();
        int indexSelecionado = jTbSincronizar.rowAtPoint(point);
        
        if (indexSelecionado >= 0 ) {
            linhaSelecionada = ((HistoricoIntegraResolver) modelSincronizar.getValue(indexSelecionado)).getBean();
            jTbSincronizar.setRowSelectionInterval(indexSelecionado, indexSelecionado);
        }
    }//GEN-LAST:event_jTbSincronizarMousePressed

    private void jBtSincronizacao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtSincronizacao1ActionPerformed
        carregaUltimosRegistros();
    }//GEN-LAST:event_jBtSincronizacao1ActionPerformed
    
    /** Caso necessário pede o preenchimento do tipo de período */
    private void validaTipoPeriodo() {
        if (((!this.campoDataVazio(txtDataInicio) && DateUtil.validaData(txtDataInicio.getText()))
                || (!this.campoDataVazio(txtDataFinal) && DateUtil.validaData(txtDataFinal.getText())))
                && cmbTipoPeriodo.getSelectedItem() == null) {
            informaErro("Para filtrar por data informe o tipo de período.");
        } else {
            informaErro(null);
        }
    }
    
    private void configuraGridHistorico() {
        jTbSincronizar.setModel(modelSincronizar);
        jTbSincronizar.setAutoCreateRowSorter(false);
        jTbSincronizar.getTableHeader().setResizingAllowed(false);
        jTbSincronizar.setRowHeight(20);
         
        TableColumn colunaID = jTbSincronizar.getColumnModel().getColumn(0);
        colunaID.setPreferredWidth(50);
        
        TableColumn colEntidade = jTbSincronizar.getColumnModel().getColumn(1);
        colEntidade.setPreferredWidth(290);
        colEntidade.setCellRenderer(new EnumDescritivelTableCellRenderer());
        
        TableColumn colCodEntidade = jTbSincronizar.getColumnModel().getColumn(2);
        colCodEntidade.setPreferredWidth(60);
        colCodEntidade.setCellRenderer(new RightCellRenderer());
        
        TableColumn colEnt = jTbSincronizar.getColumnModel().getColumn(3);
        colEnt.setPreferredWidth(105);
        colEnt.setCellRenderer(new DateTimeCellRenderer());
        
        TableColumn colInt = jTbSincronizar.getColumnModel().getColumn(4);
        colInt.setPreferredWidth(105);
        colInt.setCellRenderer(new DateTimeCellRenderer());
        
        TableColumn colTipoOper = jTbSincronizar.getColumnModel().getColumn(5);
        colTipoOper.setPreferredWidth(90);
        colTipoOper.setCellRenderer(new EnumDescritivelTableCellRenderer());
        
        TableColumn colStatus = jTbSincronizar.getColumnModel().getColumn(6);
        colStatus.setPreferredWidth(68);
        colStatus.setCellRenderer(new StatusItemCellRenderer());
        
        TableColumn colOutasInfo = jTbSincronizar.getColumnModel().getColumn(7);
        colOutasInfo.setPreferredWidth(340);
    }
    
    /**
     * Cria e configura o menu, não monta as opções
     */
    private void configuraMenuPopup(){
        final JPopupMenu popup = new JPopupMenu();
        
        /* Só habilita o botão se for pedido */
        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (linhaSelecionada == null) {
                    logger.error("Falha de integridade, não foi selecionado nenhuma linha.");
                    popup.hide();
                }
                
                /** #OldStatusChange
                if(TipoEntidade.PEDIDO.equals(linhaSelecionada.getEntidade())){
                    montaOpcoesMenuPedido(popup);
                    popup.show();
                } */
                
                if (!TipoEntidade.PEDIDO.equals(linhaSelecionada.getEntidade()) 
                        && StatusIntegracao.ERRO.equals(linhaSelecionada.getStatus())){
                    
                    montaOpcoesMenuErro(popup);
                    popup.show();
                } else {
                    popup.hide();
                }
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        
        jTbSincronizar.setComponentPopupMenu(popup);
    }
    
    private void montaOpcoesMenuErro(JPopupMenu popup){
        popup.removeAll();
        
        this.addOpcaoMenu(popup, "Tentar novamente", new FunctionMenu() {
            protected void run() {
                Connection conn = null;
                try {
                    conn = ConexaoATS.getConnection();
                    conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                    conn.setAutoCommit(false);
                    
                    linhaSelecionada.setStatus(StatusIntegracao.PENDENTE);
                    historicoIntegraDAO.alterar(linhaSelecionada);
                    conn.commit();
                    
                    JOptionPane.showMessageDialog(null, "Nova tentativa agendada com sucesso.");
                } catch (Exception e) {
                    try {
                        conn.rollback();
                    } catch (SQLException | NullPointerException ex) {}
                    logger.error("Falha ao agendar", e);
                    JOptionPane.showMessageDialog(null, "Falha ao agendar nova tentativa.");
                }
            }
        });
    }
            
    private JMenuItem addOpcaoMenu(MenuElement menu, String label, final FunctionMenu function){
        JMenuItem item = new JMenuItem(label);
        if (function != null) {
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (function.getMensage() != null) {
                        int yes = JOptionPane.showConfirmDialog(null, function.getMensage(), "Confirmação", JOptionPane.YES_NO_OPTION);
                        if (yes != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    function.run();
                }
            });
        }
        
        if(menu instanceof JPopupMenu){
            return ((JPopupMenu) menu).add(item);
        } else if(menu instanceof JMenu) {
            return ((JMenu) menu).add(item);
        } else {
            throw new RuntimeException("Tipo de menu não conhecido.");
        }
    }
        
    /**
     * Busca os últimos registros do histórico
     */
    private boolean emProcesso;  
    private void carregaUltimosRegistros() {
        if (emProcesso) {
            return;
        }
               
        try {
            emProcesso = true;

            Connection connection = ConexaoATS.criaNovaConexao();
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setReadOnly(true);
            connection.setAutoCommit(false);

            /** Atual linha selecionada */
            int selecao = jTbSincronizar.getSelectedRow();

            List<HistoricoIntegraERPBean> ultimos = historicoIntegraDAO.listaUltimos(
                    getHistoricoFilter(),
                    ParaEcomService.getQtdMantido());

            // Converte bean para resolver
            List<HistoricoIntegraResolver> resolvers = new LinkedList<>();
            for (HistoricoIntegraERPBean bean : ultimos) {
                resolvers.add(new HistoricoIntegraResolver(bean));
            }

            modelSincronizar.clear();
            modelSincronizar.addAll(resolvers);

            /** Como é somente leitura usa o roolback para evitar o deadlock */
            connection.rollback();

            /** Mantem a mesma linha selecionada */
            if (selecao >= 0 && selecao < modelSincronizar.getRowCount()) {
                jTbSincronizar.setRowSelectionInterval(selecao, selecao);
            }
            lbErroAtualizar.setText("");
        } catch (Exception e) {
            logger.info("Erro ao retornar últimos ítens", e);
            lbErroAtualizar.setText(DateUtil.dataHoraParaString(new Date()) 
                    + " - Não foi possível buscar os últimos registros.");

            if (e instanceof FBXAException || e.getCause() instanceof FBXAException) {
                ConexaoATS.fechaConexao();
            }
        } finally {
            emProcesso = false;
        }
    }
        
    /* Limpa os filtros utilizados */
    private void limpaFiltros() {
        cmbTipo.setSelectedItem(null);
        cmbTipoOperacao.setSelectedItem(null);
        cmbTipoPeriodo.setSelectedItem(null);
        cmbStatus.setSelectedItem(null);

        txtDataInicio.setText("");
        txtDataFinal.setText("");
        this.historicoFilter = null;
    }

    private boolean campoDataVazio(JFormattedTextField txtData) {
        return txtData.getText().replaceAll("[^0-9]", "").isEmpty();
    }
    
    private void informaErro(String erro){
        if(erro == null || erro.isEmpty()){
            lbErro.setText("");
            lbErro.setVisible(false);
        } else {
            lbErro.setText(erro);
            lbErro.setVisible(true);
        }
    }

    private HistoricoFilter getHistoricoFilter() {
        if (this.historicoFilter == null) {
            this.historicoFilter = new HistoricoFilter();
        }
        return historicoFilter;
    }
    
    /**
     * TODO: verificar se essa integração deveria esta nessa classe
     * 
     * Inicia processo de sincronização de dados sentido loja virtual / ERP
     */
    public void iniciaSincLojaERP(){
        try {
            processandoSync = true;
            textArea.setText("");        
            progressBar.setIndeterminate(true);
            sincronizaCategorias(); 
            sincronizaProdutos();
            progressBar.setIndeterminate(false);
            jButton1.setEnabled(true);
        } catch (Exception e) {
            logger.error("Falha durante o processo de sincronização", e);
            JOptionPane.showMessageDialog(null, "Ocorreu um erro durante o processo de sincronização. Verifique o log para mais informações.");
        } finally {
            processandoSync = false;
        }
    }
    
    /**
     * Inicia processo de sincronização de categorias
     */
    public void sincronizaCategorias(){
        CategoriaController catController = new CategoriaController();
        CategoriaERPController catERPController = new CategoriaERPController();
        TriggerController triggerController = new TriggerController();
        textArea.append("Retornando categorias da loja virtual. \n");
        triggerController.setActive("CATEGORIA_ECOMM_AI", "INACTIVE");
        triggerController.setActive("CATEGORIA_ECOMM_AU", "INACTIVE");
        List<Category> categorias = catController.listAll() ;
        if(!categorias.isEmpty()){
            try {         
                catERPController.cadastraCategorias(categorias);
                triggerController.setActive("CATEGORIA_ECOMM_AI", "ACTIVE");
                triggerController.setActive("CATEGORIA_ECOMM_AU", "ACTIVE");              
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(PanelHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
     public void sincronizaProdutos(){
        ProdutoERPController prodErpController = new ProdutoERPController();  
        ProductController prodController = new ProductController();
        TriggerController tController = new TriggerController();
        ProdutoDAO prodDao = new ProdutoDAO();
        try {
            textArea.append("Retornando produtos da loja virtual. \n ");
            WSItens itens = prodController.listItens();
            tController.setActive("PRODUTO_ECOMM_AI", "INACTIVE");
            tController.setActive("PRODUTO_ECOMM_AU", "INACTIVE");
            for(AccessXMLAttribute attribute : itens.getProducts().getProduct()){
                Product prod = prodController.getProductByAttribute(attribute.getId());                
                if(prod!=null){
                     textArea.append("Retornando produto: "+prod.getName().getTextName() + "\n");
                     if(!prodDao.produtoExiste(Integer.parseInt(prod.getId().getContent()))){
                        textArea.append("Gravando dados complementares do produto " + prod.getName().getTextName() + "\n");
                        prodErpController.cadastraProduto(prod);
                        PanelHistorico.textArea.append("Dados complementares do produto  " + prod.getName().getTextName() + " gravado com sucesso " + "\n"); 
                     }
                                       
                }            
            }
            tController.setActive("PRODUTO_ECOMM_AI", "ACTIVE");
            tController.setActive("PRODUTO_ECOMM_AU", "ACTIVE");
        } catch (Exception ex) {
            Logger.getLogger(PanelHistorico.class.getName()).error("Erro ao gravar dados complementares do produto: "+ex);
            textArea.append("Erro ao gravar dados complementares do produto: "+ex);
            progressBar.setIndeterminate(false);                        
        } 
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JComboBox cmbTipo;
    private javax.swing.JComboBox cmbTipoOperacao;
    private javax.swing.JComboBox cmbTipoPeriodo;
    private javax.swing.JButton jBfechar1;
    private javax.swing.JButton jBtSincronizacao;
    private javax.swing.JButton jBtSincronizacao1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTbSincronizar;
    private javax.swing.JLabel lbAte;
    private javax.swing.JLabel lbErro;
    private javax.swing.JLabel lbErroAtualizar;
    private javax.swing.JLabel lbPeriodo;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lbTipoOperacao;
    private javax.swing.JLabel lbTipoPeriodo;
    private javax.swing.JPanel panelFiltros;
    private javax.swing.JPanel panelFiltros1;
    private javax.swing.JPanel panelFiltros2;
    private javax.swing.JPanel panelFiltros3;
    private javax.swing.JPanel panelLegenda;
    public static javax.swing.JProgressBar progressBar;
    public static javax.swing.JTextArea textArea;
    private javax.swing.JFormattedTextField txtDataFinal;
    private javax.swing.JFormattedTextField txtDataInicio;
    // End of variables declaration//GEN-END:variables
    
    /** Classe interna */
    private abstract class FunctionMenu {
        private final String mensage;

        private FunctionMenu() {
            this.mensage = null;
        }

        private FunctionMenu(String mensage) {
            this.mensage = mensage;
        }

        private String getMensage() {
            return mensage;
        }     
  
        protected abstract void run();
    }
}
