/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.ui;

import br.com.atsinformatica.erp.dao.ComplementoPedidoEcomDAO;
import br.com.atsinformatica.erp.dao.ParaEcomDAO;
import br.com.atsinformatica.erp.dao.PedidoCERPDAO;
import br.com.atsinformatica.erp.entity.ParaEcomBean;
import br.com.atsinformatica.midler.components.renderer.CenterCellRenderer;
import br.com.atsinformatica.midler.components.renderer.DateTimeCellRenderer;
import br.com.atsinformatica.midler.components.renderer.MoneyCellRenderer;
import br.com.atsinformatica.erp.entity.ComplementoPedidoEcomBean;
import br.com.atsinformatica.midler.components.renderer.FormaPagamentoCellRenderer;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.ObjectTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class PanelListaPedidoPagos extends javax.swing.JPanel {
    private static Logger logger = Logger.getLogger(PanelListaPedidoPagos.class);

    private AnnotationResolver resolverSinc = new AnnotationResolver(ComplementoPedidoEcomBean.class);
    private String fields = "codReferencia,codErp,dataPagamento,formaPagamento,codTransacao,valorPago,idPedidoEcom";
    //model para grid  de pedido pago
    private ObjectTableModel modelPedidoPago = new ObjectTableModel(resolverSinc, fields);
    //Linha da Tabela selecionada, vai ser capturada no evento MousePressed
    private int linhaSelecionada = -1;
    private ParaEcomBean paraEcom;

    /**
     * Creates new form PanelListaPedidoPagos
     */
    public PanelListaPedidoPagos() {
        initComponents();
        carregaGridPedidoPago();
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTbPedidoPago = new javax.swing.JTable();
        jBAtualizar = new javax.swing.JButton();
        jBfechar = new javax.swing.JButton();

        jTbPedidoPago.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTbPedidoPago.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Cod. Ref. Loja", "Cod. Pedido Resulth", "Data de pagamento", "Forma de pagamento", "Código da transação", "Valor pago"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTbPedidoPago.getTableHeader().setReorderingAllowed(false);
        jTbPedidoPago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTbPedidoPagoMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTbPedidoPago);

        jBAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons/refresh.png"))); // NOI18N
        jBAtualizar.setText("Atualizar");
        jBAtualizar.setToolTipText("Atualizar");
        jBAtualizar.setFocusable(false);
        jBAtualizar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBAtualizar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAtualizarActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(830, Short.MAX_VALUE)
                .addComponent(jBAtualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBfechar)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(406, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBfechar)
                    .addComponent(jBAtualizar))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(92, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTbPedidoPagoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTbPedidoPagoMousePressed
        //Seleciona a linha no ponto em que o mouse é clicado
        //Point point = evt.getPoint();
        //linhaSelecionada = jTbListaPedido.rowAtPoint(point);
        //jTbListaPedido.setRowSelectionInterval(linhaSelecionada, linhaSelecionada);
    }//GEN-LAST:event_jTbPedidoPagoMousePressed

    private void jBAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAtualizarActionPerformed
        refresh();
    }//GEN-LAST:event_jBAtualizarActionPerformed

    private void jBfecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBfecharActionPerformed
        PanelPrincipal.jMainPanel.setEnabled(false);
        PanelPrincipal.getInstance().setjStatus("");        
        //fecha tudo
        this.removeAll();
        //atualiza ui
        this.updateUI();
        this.setVisible(false);
        setBorder(null);
    }//GEN-LAST:event_jBfecharActionPerformed

    /**
     * Atualiza lista de pedidos pagos na loja e que ainda não foram
     */
    public void refresh() {
        ComplementoPedidoEcomDAO complementoPedidoEcomDAO = new ComplementoPedidoEcomDAO();
        List<ComplementoPedidoEcomBean> listaComplementoPedido = new ArrayList<>();
        List<ComplementoPedidoEcomBean> listaAux = new ArrayList<>();
        PedidoCERPDAO pedidoERPDAO = new PedidoCERPDAO();
        ParaEcomDAO paraEcomDao = new ParaEcomDAO();
        modelPedidoPago.clear();
        try {
            listaComplementoPedido = complementoPedidoEcomDAO.listaTodos();
            ParaEcomBean paraEcomBean = paraEcomDao.listaTodos().get(0);
            if (!listaComplementoPedido.isEmpty()) {                     
                //adiciona na lista auxiliar somente pedidos que foram pagos na loja e que ainda não estão faturados
                for (ComplementoPedidoEcomBean complementoPedidoEcomBean : listaComplementoPedido) {
                    if (pedidoERPDAO.pendenteFaturamento(complementoPedidoEcomBean.getCodErp(), paraEcomBean.getCodEmpresaEcom())) {
                        listaAux.add(complementoPedidoEcomBean);
                    }
                }
                modelPedidoPago.addAll(listaAux);
            } else {
                preencheGrid();
            }
        } catch (SQLException ex) {
            logger.error("Erro ao retornar lista de pedidos pagos: " + ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAtualizar;
    private javax.swing.JButton jBfechar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTbPedidoPago;
    // End of variables declaration//GEN-END:variables

    
    private void carregaGridPedidoPago() {
        jTbPedidoPago.setModel(modelPedidoPago);
        jTbPedidoPago.setAutoCreateRowSorter(true);
        refresh();
        jTbPedidoPago.getColumnModel().getColumn(6).setMaxWidth(0);
        jTbPedidoPago.getColumnModel().getColumn(6).setMinWidth(0);
        jTbPedidoPago.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);
        jTbPedidoPago.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);
        jTbPedidoPago.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTbPedidoPago.getColumnModel().getColumn(1).setPreferredWidth(100);
        jTbPedidoPago.getColumnModel().getColumn(1).setCellRenderer(new CenterCellRenderer());
        jTbPedidoPago.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTbPedidoPago.getColumnModel().getColumn(2).setCellRenderer(new DateTimeCellRenderer());
        jTbPedidoPago.getColumnModel().getColumn(3).setPreferredWidth(250);
        jTbPedidoPago.getColumnModel().getColumn(3).setCellRenderer(new FormaPagamentoCellRenderer());
        jTbPedidoPago.getColumnModel().getColumn(4).setPreferredWidth(350);
        jTbPedidoPago.getColumnModel().getColumn(4).setCellRenderer(new CenterCellRenderer());
        jTbPedidoPago.getColumnModel().getColumn(5).setPreferredWidth(100);
        jTbPedidoPago.getColumnModel().getColumn(5).setCellRenderer(new MoneyCellRenderer());
    }

    private void preencheGrid() {
        modelPedidoPago.clear();
        for (int i = 0; i < 16; i++) {
            modelPedidoPago.add(null);
        }
    }
}
