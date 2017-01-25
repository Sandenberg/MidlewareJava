/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.ui;

import br.com.atsinformatica.erp.controller.LogEcomErroController;
import br.com.atsinformatica.erp.dao.CartConERPDAO;
import br.com.atsinformatica.erp.dao.ComplementoPedidoEcomDAO;
import br.com.atsinformatica.erp.dao.HistoricoIntegraDAO;
import br.com.atsinformatica.erp.dao.PedidoCERPDAO;
import br.com.atsinformatica.erp.entity.PedidoCERPBean;
import br.com.atsinformatica.erp.entity.ComplementoPedidoEcomBean;
import br.com.atsinformatica.erp.entity.HistoricoIntegraERPBean;
import br.com.atsinformatica.erp.entity.UsuarioERPBean;
import br.com.atsinformatica.midler.Main;
import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.midler.service.SyncService;
import br.com.atsinformatica.midler.ui.util.InternetUtil;
import br.com.atsinformatica.prestashop.clientDAO.OrderPrestashopDAO;
import br.com.atsinformatica.prestashop.model.root.Order;
import br.com.atsinformatica.utils.Function;
import br.com.atsinformatica.utils.ImageUtil;
import br.com.atsinformatica.utils.SingletonUtil;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public final class PanelPrincipal extends javax.swing.JFrame {
    private static final Logger logger = Logger.getLogger(PanelPrincipal.class);
    
    private static PanelPrincipal instance = null;
    private TrayIcon trayIcon;
    private PanelImagens panelImagens;
    private PanelHistorico panelHistorico;
    private PanelConfiguracao panelConfiguracao;
    private PanelListaPedidoPagos panelPedidoPago;
    
    /** Creates new form PanelPrincipal */
    public PanelPrincipal() {
        initComponents();
        
        // Verifica conexão com a internet
        InternetUtil.iniciaVerificacao(new Function<Boolean>() {
            final ImageIcon imgConectado = new javax.swing.ImageIcon(
                getClass().getResource("/assets/images/sinal_verde_70w.png")); 
            final ImageIcon imgDesconectado = new javax.swing.ImageIcon(
                getClass().getResource("/assets/images/sinal_vermelho_70w.png")); 
            
            Boolean exibiu = false;
            
            @Override
            public void run(Boolean conectado) {     
                String msg = "";
                
                if (conectado) { // Conectado à internet
                    jlSemaforo.setIcon(imgConectado);
                    jlSemaforo.setToolTipText("Conectado à internet");
                    
                    // Tenta conexão ao web service
                    if (!Main.MODE_CONFIG) {
                        try {
                            SyncService.verificaConexaoWS();
                        } catch (Exception e) {
                            msg = "Não foi possível conectar ao web service.";
                        }
                    }
                } else { // Não conectado à internet
                    jlSemaforo.setIcon(imgDesconectado);
                    jlSemaforo.setToolTipText("Sem acesso à internet");    
                    msg = "Não foi possível conectar a internet.";
                }
                
                lbErroAtualizar.setText(msg);
                lbErroAtualizar.setForeground(new Color(255,0,0));
                
                if (!msg.isEmpty()){
                    if (!exibiu && trayIcon != null) {
                        trayIcon.displayMessage(getTitle(), msg, TrayIcon.MessageType.ERROR);
                        exibiu = true;
                    }
                } else {
                    if (SyncService.isInProcess()) {
                        lbErroAtualizar.setText("Sincronização em processo.");
                        lbErroAtualizar.setForeground(new Color(0,105,0));
                    }
                    exibiu = false;
                }
            }
        });
        
        this.uJPanelImagem1.setImagem(new File(System.getProperty("user.dir") + "\\src\\assets\\"));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "\\src\\assets\\images\\ecom.png"));
        this.setTitle("Modulo Integrador E-commerce ATS - " + Main.VERSION);
        // this.jLSpinner.setVisible(false);
        this.setExtendedState(MAXIMIZED_BOTH);
        //jMainPanel.setSize(this.getWidth(), 430);
        //centraliza
        this.setLocationRelativeTo(null);

        if (!Main.MODE_CONFIG) {            
            this.iniciaSincronizacao();
        } else {
            habDesabBotoes(true);
        }
        
        jMainPanel.requestFocus();
        jLabel1.setText("");
        jPStatusBar1.setVisible(false);
             
        /** Confirmação ao sair */
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmAndExit();
            }
        });
        
        // Adicionando a função da tecla ESC
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),"forward");
        this.getRootPane().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);
        this.getRootPane().getActionMap().put("forward", new AbstractAction(){
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (jMainPanel.isEnabled()) {
                    jMainPanel.removeAll();
                    jMainPanel.updateUI();
                    jMainPanel.setEnabled(false);
                } else {
                    confirmAndExit();
                }
            }     
        });
        
        // Oculta area para usuários que não são mestres
        if (!UsuarioERPBean.getInstance().isMestre()) {
            btnImagens.setVisible(false);
        }
        
        // Oculta temporariamente o botão pedidos pagos 
        btPedidosPagos.setVisible(false);
     
        this.startToTray();
    }
    
    private void confirmAndExit(){
        int confirm = JOptionPane.showConfirmDialog(null, 
                "Deseja realmente sair?",  "Atenção!", 
                JOptionPane.YES_NO_OPTION);

        if ( confirm == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
    
    private void iniciaSincronizacao() {
        /* Conserta status que falharam ao fechar o middle */
        HistoricoIntegraDAO dao = SingletonUtil.get(HistoricoIntegraDAO.class);
        List<HistoricoIntegraERPBean> list = dao.listaEmAndamento();
        if (list != null) {
            for (HistoricoIntegraERPBean hist : list) {
                Connection connection = ConexaoATS.getConnection();
                try {
                    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                    connection.setAutoCommit(false);
                    if (hist.getEntidade() == TipoEntidade.PEDIDO ) {
                        hist.setStatus(StatusIntegracao.ERRO);
                        LogEcomErroController.geraErroLog(hist.getId(), "Processo interrompido inesperadamente.");
                    } else {
                        hist.setStatus(StatusIntegracao.PENDENTE);
                    }

                    dao.alterar(hist);
                    connection.commit();
                } catch (Exception e){
                    try {
                        connection.rollback();
                    } catch (SQLException ex) {}
                }
            }
        }
        
        
        /* Inicia a Thread para sincronização */
        SyncService.initialize();
//        SendMailService.initialize();
    }
    
    public void habDesabBotoes(boolean habDesab) {
        JHistorico.setEnabled(habDesab);
        JConfiguracao.setEnabled(habDesab);
        jMenuItem2.setEnabled(habDesab);
        jMenuItem4.setEnabled(habDesab);
        btPedidosPagos.setEnabled(habDesab);
        if (!habDesab) {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        } else {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        
        if (Main.MODE_CONFIG && habDesab) {
            JHistorico.setEnabled(false);            
            JConfiguracao.setEnabled(false);
            jMenuItem2.setEnabled(false);
            jMenuItem4.setEnabled(false);
            btPedidosPagos.setEnabled(false);
            JConfiguracao.setEnabled(true);   
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

        jMainPanel = new javax.swing.JPanel();
        jPStatusBar = new javax.swing.JPanel();
        jPStatusBar1 = new javax.swing.JPanel();
        jOperacao = new javax.swing.JLabel();
        jPStatusBar2 = new javax.swing.JPanel();
        jStatus = new javax.swing.JLabel();
        jlSemaforo = new javax.swing.JLabel();
        lbErroAtualizar = new javax.swing.JLabel();
        jPRedBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        JSair = new javax.swing.JButton();
        JConfiguracao = new javax.swing.JButton();
        JHistorico = new javax.swing.JButton();
        btPedidosPagos = new javax.swing.JButton();
        btnImagens = new javax.swing.JButton();
        uJPanelImagem1 = new componentes.UJPanelImagem();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        jMainPanel.setEnabled(false);

        javax.swing.GroupLayout jMainPanelLayout = new javax.swing.GroupLayout(jMainPanel);
        jMainPanel.setLayout(jMainPanelLayout);
        jMainPanelLayout.setHorizontalGroup(
            jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1115, Short.MAX_VALUE)
        );
        jMainPanelLayout.setVerticalGroup(
            jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 373, Short.MAX_VALUE)
        );

        jPStatusBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPStatusBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPStatusBar1Layout = new javax.swing.GroupLayout(jPStatusBar1);
        jPStatusBar1.setLayout(jPStatusBar1Layout);
        jPStatusBar1Layout.setHorizontalGroup(
            jPStatusBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPStatusBar1Layout.createSequentialGroup()
                .addComponent(jOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 40, Short.MAX_VALUE))
        );
        jPStatusBar1Layout.setVerticalGroup(
            jPStatusBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jOperacao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPStatusBar2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPStatusBar2Layout = new javax.swing.GroupLayout(jPStatusBar2);
        jPStatusBar2.setLayout(jPStatusBar2Layout);
        jPStatusBar2Layout.setHorizontalGroup(
            jPStatusBar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPStatusBar2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPStatusBar2Layout.setVerticalGroup(
            jPStatusBar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jlSemaforo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/images/sinal_amarelo.png"))); // NOI18N
        jlSemaforo.setToolTipText("Status da Internet");

        lbErroAtualizar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lbErroAtualizar.setForeground(new java.awt.Color(255, 0, 0));
        lbErroAtualizar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPStatusBarLayout = new javax.swing.GroupLayout(jPStatusBar);
        jPStatusBar.setLayout(jPStatusBarLayout);
        jPStatusBarLayout.setHorizontalGroup(
            jPStatusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPStatusBarLayout.createSequentialGroup()
                .addComponent(jPStatusBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPStatusBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbErroAtualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlSemaforo))
        );
        jPStatusBarLayout.setVerticalGroup(
            jPStatusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPStatusBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPStatusBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jlSemaforo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbErroAtualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPRedBar.setBackground(new java.awt.Color(153, 9, 12));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(248, 248, 248));
        jLabel1.setText("    ");
        jPRedBar.add(jLabel1);

        JSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/Log Out.png"))); // NOI18N
        JSair.setText("Sair");
        JSair.setToolTipText("Sair");
        JSair.setBorder(null);
        JSair.setContentAreaFilled(false);
        JSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JSairMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JSairMouseExited(evt);
            }
        });
        JSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JSairActionPerformed(evt);
            }
        });

        JConfiguracao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/config.png"))); // NOI18N
        JConfiguracao.setText("Configurações");
        JConfiguracao.setToolTipText("Configurações");
        JConfiguracao.setBorder(null);
        JConfiguracao.setBorderPainted(false);
        JConfiguracao.setContentAreaFilled(false);
        JConfiguracao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JConfiguracao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JConfiguracao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JConfiguracaoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JConfiguracaoMouseExited(evt);
            }
        });
        JConfiguracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JConfiguracaoActionPerformed(evt);
            }
        });

        JHistorico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/application_view_list (1).png"))); // NOI18N
        JHistorico.setText("Histórico");
        JHistorico.setToolTipText("Histórico");
        JHistorico.setBorder(null);
        JHistorico.setBorderPainted(false);
        JHistorico.setContentAreaFilled(false);
        JHistorico.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JHistorico.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JHistorico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JHistoricoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JHistoricoMouseExited(evt);
            }
        });
        JHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JHistoricoActionPerformed(evt);
            }
        });

        btPedidosPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/Invoice-icon.png"))); // NOI18N
        btPedidosPagos.setText("Pedidos Pagos");
        btPedidosPagos.setToolTipText("Pedidos Pagos");
        btPedidosPagos.setBorder(null);
        btPedidosPagos.setBorderPainted(false);
        btPedidosPagos.setContentAreaFilled(false);
        btPedidosPagos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btPedidosPagos.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btPedidosPagos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btPedidosPagos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btPedidosPagosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btPedidosPagosMouseExited(evt);
            }
        });
        btPedidosPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPedidosPagosActionPerformed(evt);
            }
        });

        btnImagens.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/image_icon.png"))); // NOI18N
        btnImagens.setText("Imagens");
        btnImagens.setToolTipText("Sair");
        btnImagens.setBorder(null);
        btnImagens.setContentAreaFilled(false);
        btnImagens.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImagens.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImagens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImagensActionPerformed(evt);
            }
        });

        uJPanelImagem1.setPreferredSize(new java.awt.Dimension(99, 61));

        javax.swing.GroupLayout uJPanelImagem1Layout = new javax.swing.GroupLayout(uJPanelImagem1);
        uJPanelImagem1.setLayout(uJPanelImagem1Layout);
        uJPanelImagem1Layout.setHorizontalGroup(
            uJPanelImagem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 118, Short.MAX_VALUE)
        );
        uJPanelImagem1Layout.setVerticalGroup(
            uJPanelImagem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btPedidosPagos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImagens, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JConfiguracao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JSair, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(uJPanelImagem1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnImagens, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JHistorico, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JConfiguracao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btPedidosPagos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
            .addComponent(uJPanelImagem1, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
        );

        jMenuBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));

        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("Inicio");
        jMenu1.setRequestFocusEnabled(false);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/application_view_list.png"))); // NOI18N
        jMenuItem2.setText("Histórico");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/door_out.png"))); // NOI18N
        jMenuItem4.setText("Sair");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPRedBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPStatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPRedBar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JConfiguracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JConfiguracaoActionPerformed
        jMainPanel.setEnabled(true);
        //limpa status
        this.setjStatus("");
        //seta status
        this.jPStatusBar1.setVisible(true);
        this.setjStatus("Cadastro de configurações");
        //remove todos componentes do painel e atualiza painel
        jMainPanel.removeAll();
        jMainPanel.updateUI();
        panelConfiguracao = new PanelConfiguracao();
        panelConfiguracao.setSize(jMainPanel.getWidth(), 495);
        //adciona o painel no painel principal
        jMainPanel.add(panelConfiguracao);
        if (panelHistorico != null) {
            panelHistorico.setVisible(false);
        }
        if(panelPedidoPago != null){
            panelPedidoPago.setVisible(false);
        }
        panelConfiguracao.setVisible(true);
    }//GEN-LAST:event_JConfiguracaoActionPerformed
    
    /**
     * Esse método só deve ser chamado quando o não há conexão com o banco de 
     * dados.
     */
    public void configurarMiddler() {
        jMainPanel.setEnabled(true);
        //limpa status
        this.setjStatus("");
        //seta status
        this.jPStatusBar1.setVisible(true);
        this.setjStatus("Cadastro de configurações");
        //remove todos componentes do painel e atualiza painel
        jMainPanel.removeAll();
        jMainPanel.updateUI();
        panelConfiguracao = new PanelConfiguracao();
        panelConfiguracao.setSize(jMainPanel.getWidth(), 495);
        //adciona o painel no painel principal
        jMainPanel.add(panelConfiguracao);
        if (panelHistorico != null) {
            panelHistorico.setVisible(false);
        }
        if(panelPedidoPago != null){
            panelPedidoPago.setVisible(false);
        }
        panelConfiguracao.setVisible(true);
    }
    
    private void JSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JSairActionPerformed
        confirmAndExit();
    }//GEN-LAST:event_JSairActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JHistoricoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JSairActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void JHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JHistoricoActionPerformed
            
        jMainPanel.setEnabled(true);
        jMainPanel.removeAll();
        jMainPanel.updateUI();        
        this.setjStatus("Histórico de sincronizações");
        panelHistorico = new PanelHistorico();
        panelHistorico.setSize(jMainPanel.getWidth(), jMainPanel.getHeight());
        //adciona o painel no painel principal
        jMainPanel.add(panelHistorico);
        if (panelConfiguracao != null) {
            panelConfiguracao.setVisible(false);
        }
        if(panelPedidoPago != null){
            panelPedidoPago.setVisible(false);
        }      
        
        panelHistorico.setVisible(true);
        jPStatusBar1.setVisible(false);
    }//GEN-LAST:event_JHistoricoActionPerformed

    private void JConfiguracaoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JConfiguracaoMouseEntered
        JConfiguracao.setContentAreaFilled(true);
    }//GEN-LAST:event_JConfiguracaoMouseEntered

    private void JConfiguracaoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JConfiguracaoMouseExited
        JConfiguracao.setContentAreaFilled(false);
    }//GEN-LAST:event_JConfiguracaoMouseExited

    private void JSairMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JSairMouseEntered
        JSair.setContentAreaFilled(true);
    }//GEN-LAST:event_JSairMouseEntered

    private void JSairMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JSairMouseExited
        JSair.setContentAreaFilled(false);
    }//GEN-LAST:event_JSairMouseExited

    private void JHistoricoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JHistoricoMouseEntered
        JHistorico.setContentAreaFilled(true);
    }//GEN-LAST:event_JHistoricoMouseEntered

    private void JHistoricoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JHistoricoMouseExited
        JHistorico.setContentAreaFilled(false);
    }//GEN-LAST:event_JHistoricoMouseExited

    private void btPedidosPagosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btPedidosPagosMouseEntered
        btPedidosPagos.setContentAreaFilled(true);
    }//GEN-LAST:event_btPedidosPagosMouseEntered

    private void btPedidosPagosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btPedidosPagosMouseExited
       btPedidosPagos.setContentAreaFilled(false);
    }//GEN-LAST:event_btPedidosPagosMouseExited

    private void btPedidosPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPedidosPagosActionPerformed
        if (!InternetUtil.isConectado()) {
            JOptionPane.showMessageDialog(this, "Sem acesso a internet! \n"
                    + "Por favor verifique sua conexão");
            
            return;
        }
        
        jMainPanel.setEnabled(true);
        jMainPanel.removeAll();
        jMainPanel.updateUI();
        this.setjStatus("Pedidos pagos");
        panelPedidoPago = new PanelListaPedidoPagos();
        //  panelPedido.requestFocus();
        panelPedidoPago.setSize(jMainPanel.getWidth(), jMainPanel.getHeight());
        jMainPanel.add(panelPedidoPago);
        btPedidosPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/Invoice-icon.png")));
        if (panelConfiguracao != null) {
            panelConfiguracao.setVisible(false);
            panelConfiguracao = null;
        }
        if (panelHistorico != null) {
            panelHistorico.setVisible(false);
            panelHistorico = null;
        } 
        jPStatusBar1.setVisible(false);
        
    }//GEN-LAST:event_btPedidosPagosActionPerformed

    private void btnImagensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImagensActionPerformed
        jMainPanel.setEnabled(true);
        jMainPanel.removeAll();
        jMainPanel.updateUI();        
        this.setjStatus(btnImagens.getText());
        panelImagens = new PanelImagens();
        panelImagens.setSize(jMainPanel.getWidth(), jMainPanel.getHeight());
        //adciona o painel no painel principal
        jMainPanel.add(panelImagens);    
        panelImagens.setVisible(true);
        jPStatusBar1.setVisible(false);
    }//GEN-LAST:event_btnImagensActionPerformed

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        this.minimizeToTray();
    }//GEN-LAST:event_formWindowIconified

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelPrincipal().setVisible(true);
            }
        });
    }
    /**
     * @return the jStatus
     */
    public String getjStatus() {
        return jStatus.getText();
    }

    /**
     * @param jStatus the jStatus to set
     */
    public void setjStatus(String status) {
        this.jLabel1.setText(status);
        this.jStatus.setText(status);
        if (status.equals("")) {
            jPStatusBar1.setVisible(false);
        }
    }

    /**
     * @return the jOperacao
     */
    public String getjOperacao() {
        return jOperacao.getText();
    }

    /**
     * @param jOperacao the jOperacao to set
     */
    public void setjOperacao(String operacao) {
        this.jOperacao.setText(operacao);
    }

    public static PanelPrincipal getInstance() {
        if (instance == null) {
            instance = new PanelPrincipal();
        }
        return instance;
    }

    public JPanel getJMainPanel() {
        return this.getJMainPanel();
    }
    
    /**
     * TODO: Verificar utilidade
     * Atualiza lista de pedidos pagos por meio de pagamento na loja
     */
    public void atualizaPedidosPagos(){
        PedidoCERPDAO pedidoDao = new PedidoCERPDAO();
        OrderPrestashopDAO orderDao = new OrderPrestashopDAO();
        ComplementoPedidoEcomDAO complementoPedidoDao = new ComplementoPedidoEcomDAO();
        CartConERPDAO cartConDAO = new CartConERPDAO();
        try{            
            List<PedidoCERPBean> listaPedidos = pedidoDao.listaPedidosPendentes(ParaEcomService.getCodEmpresaEcom());
            for(PedidoCERPBean pedidoBean : listaPedidos){
                Order order = orderDao.getId(Order.URLORDER, Integer.valueOf(pedidoBean.getId_ecom()));
                //se pedido foi pago
                if(order.getCurrent_state().equals(StatusPedido.PAGAMENTO_ACEITO)){
                    ComplementoPedidoEcomBean complementoPedidoEcomBean = complementoPedidoDao.abrir(pedidoBean.getId_erp());
                    Date dataAtualizacao = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(order.getDateUpd());
                    if(order.getInstallmentCounts()!=0){
                        switch (order.getPayment()) {
                            case "PagSeguro":
                                complementoPedidoEcomBean.setCodConvenio(cartConDAO.retornaCodConvenio(1, order.getInstallmentCounts()));
                                break;
                            case "PayPal":
                                complementoPedidoEcomBean.setCodConvenio(cartConDAO.retornaCodConvenio(2, order.getInstallmentCounts()));
                                break;
                            case "Cielo":
                                complementoPedidoEcomBean.setCodConvenio(cartConDAO.retornaCodConvenio(3, order.getInstallmentCounts()));
                                break;
                        }
                    }
                    complementoPedidoEcomBean.setQtdeParcelaEcom(order.getInstallmentCounts());
                    complementoPedidoEcomBean.setDataPagamento(dataAtualizacao);
                    complementoPedidoEcomBean.setValorPago(Double.valueOf(order.getTotal_paid()));
                    complementoPedidoEcomBean.setCodTransacao(order.getIdTransaction());
                    complementoPedidoEcomBean.setCodEmpresa(ParaEcomService.getCodEmpresaEcom());
                    complementoPedidoEcomBean.setTipoPedido("55");
                    complementoPedidoDao.alterar(complementoPedidoEcomBean);              
                    if(panelPedidoPago !=null && panelPedidoPago.isVisible()){
                        panelPedidoPago.refresh();                        
                    }else
                        btPedidosPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/images/new_payment.gif")));
                }
            } 
            logger.info("Lista de pedidos pagos, atualiza com sucesso.");
        }catch(Exception e){
            logger.error("Erro ao retornar lista de pedidos pagos: "+e);            
        }      
    }
    
    private void startToTray(){
        this.moveToTray(true);
    }
    
    private void minimizeToTray(){
        this.moveToTray(false);
    }
       
    private void moveToTray(Boolean start){
        if (!SystemTray.isSupported() || Main.MODE_CONFIG) {
            return;
        }
        
        final PopupMenu popup = new PopupMenu();
        final SystemTray tray = SystemTray.getSystemTray();
        
        if (trayIcon == null) {
            trayIcon = new TrayIcon(ImageUtil.criaIcon(16, 16, "images\\ecom.png")
                    .getImage(), this.getTitle());      

            /** Abrir o middle */
            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showFrame();
                }
            };

            MenuItem abrir = new MenuItem("Abrir");
            abrir.addActionListener(listener);
            abrir.setFont(new Font(null, Font.BOLD, 12));

            MenuItem sair = new MenuItem("Sair");
            sair.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    confirmAndExit();
                }
            });

            popup.add(abrir);
            popup.add(sair);

            trayIcon.addActionListener(listener);
            trayIcon.setPopupMenu(popup);

            try {
                tray.add(trayIcon);
                
                String msg = start ? "O módulo integrador foi iniciado." 
                        : "O módulo integrador continua em execução.";
                trayIcon.displayMessage(this.getTitle(), msg, TrayIcon.MessageType.INFO);
            } catch (AWTException ex) {
                logger.error("Falha ao minimizar aplicação.", ex);
            }
        }
        
        this.setVisible(false);
    }
    
    public void showFrame(){
        this.setVisible(true);
        this.setState(Frame.NORMAL);
        this.toFront();

        if (trayIcon != null) {
            SystemTray tray = SystemTray.getSystemTray();
            tray.remove(trayIcon);
            trayIcon = null;
        }        
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JConfiguracao;
    private javax.swing.JButton JHistorico;
    private javax.swing.JButton JSair;
    private javax.swing.JButton btPedidosPagos;
    private javax.swing.JButton btnImagens;
    private javax.swing.JLabel jLabel1;
    public static javax.swing.JPanel jMainPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JLabel jOperacao;
    private javax.swing.JPanel jPRedBar;
    private javax.swing.JPanel jPStatusBar;
    private javax.swing.JPanel jPStatusBar1;
    private javax.swing.JPanel jPStatusBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jStatus;
    public javax.swing.JLabel jlSemaforo;
    public static javax.swing.JLabel lbErroAtualizar;
    private componentes.UJPanelImagem uJPanelImagem1;
    // End of variables declaration//GEN-END:variables

}
