package br.com.atsinformatica.midler.ui;

import br.com.atsinformatica.erp.dao.ProdImgDAO;
import br.com.atsinformatica.erp.entity.ImgProdBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.service.BackupImagemService;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.midler.ui.util.InternetUtil;
import br.com.atsinformatica.utils.Function;
import com.alee.utils.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * @author niwrodrigues
 */
public class PanelImagens extends javax.swing.JPanel {
    private final Logger logger = Logger.getLogger(PanelImagens.class);
    
    private ProdImgDAO imageDAO;
    
    public PanelImagens() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLogLocal = new javax.swing.JTextArea();
        progressBarLocal = new javax.swing.JProgressBar();
        btnBackupLocal = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtLogEcom = new javax.swing.JTextArea();
        progressBarEcom = new javax.swing.JProgressBar();
        btnBackupEcom = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Esta ação fará backup das imagens do e-commerce que estão espalhadas neste computador e de imagens que estão em sites de terceiros, fazendo uma cópia de segurança para a pasta informa das configurações do middle.");

        txtLogLocal.setEditable(false);
        txtLogLocal.setColumns(20);
        txtLogLocal.setRows(5);
        jScrollPane1.setViewportView(txtLogLocal);

        btnBackupLocal.setText("Fazer backup");
        btnBackupLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupLocalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(progressBarLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBackupLocal))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBarLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBackupLocal)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Imagens locais", jPanel1);

        jLabel2.setText("Esta ação fará backup das imagens que estão no e-commerce, fazendo download para a pasta configurada nos parametros do middle. Logo após essas imagens serem baixadas as mesmas serão vinculadas ao produto.");

        txtLogEcom.setEditable(false);
        txtLogEcom.setColumns(20);
        txtLogEcom.setRows(5);
        jScrollPane2.setViewportView(txtLogEcom);

        btnBackupEcom.setText("Fazer backup");
        btnBackupEcom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupEcomActionPerformed(evt);
            }
        });

        jLabel3.setText("Cuidado, esta ação pode duplicar as imagens do produto.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(progressBarEcom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBackupEcom))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBarEcom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBackupEcom)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Imagens do e-commerce", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackupLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackupLocalActionPerformed
        if (ParaEcomService.getDiretorioImagens().isEmpty()) {
            txtLogLocal.setText("Não foi configurado nos parâmetros do middle, o diretório das imagens.");
            return;
        }
        
        if (!new File(ParaEcomService.getDiretorioImagens()).exists()) {
            txtLogLocal.setText("O caminho configurado para as imagens não existe ou é inválido.");
            return;
        }
        
        // Busca todas as imagens
        final List<ImgProdBean> listaImgs = this.getImageDAO().listAll();
        if (listaImgs == null || listaImgs.isEmpty()) {
            txtLogLocal.setText("Não foi encontrada nenhuma imagem de produto.");
            return;
        }
        
        btnBackupLocal.setEnabled(false);
        progressBarLocal.setValue(0);
        progressBarLocal.setMaximum(listaImgs.size());
        
        new Thread(){
            @Override
            public void run() {
                // Agrupa pelo código do produto
                Map<String, List<ImgProdBean>> agrupamento = new TreeMap<>();
                for (ImgProdBean imgPro : listaImgs) {
                    if (!agrupamento.containsKey(imgPro.getCodProd())) {
                        agrupamento.put(imgPro.getCodProd(), new ArrayList<ImgProdBean>());
                    }

                    agrupamento.get(imgPro.getCodProd()).add(imgPro);
                }

                // Limpa
                txtLogLocal.setText("");
                for (String key : agrupamento.keySet()) {
                    List<ImgProdBean> novasImagens = new ArrayList<>();

                    for (ImgProdBean imgProd : agrupamento.get(key)) {
                        if (imgProd.getUrlImagem() == null || imgProd.getUrlImagem().isEmpty()) {
                            registraLogLocal(imgProd, "URL da imagem inválida.");
                            progressBarLocal.setValue(progressBarLocal.getValue() + 1);
                            continue;
                        }

                        String nome = null;
                        if (imgProd.getUrlImagem().toUpperCase().matches("^HTTP(.+)")) {
                            nome = processaImagemWeb(imgProd);
                        } else {
                            nome = processaImagemLocal(imgProd);
                        }

                        if (nome != null) {
                            imgProd.setUrlImagem(nome);
                            novasImagens.add(imgProd);
                        }
                        progressBarLocal.setValue(progressBarLocal.getValue() + 1);
                    }

                    Connection connection = null;
                    try {
                        connection = ConexaoATS.getConnection();
                        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                        connection.setAutoCommit(false);
                        
                        getImageDAO().deletaImagensProd(key);    
//                        int contador = 1;
                        for (ImgProdBean imagem : novasImagens) {
//                            if (imagem.getPosicao() == null || imagem.getPosicao() == 0) {
//                                imagem.setPosicao(contador);
//                                contador++;
//                            }
                            getImageDAO().gravar(key, imagem.getUrlImagem(), imagem.getPosicao());
                        }

                        connection.commit();
                        registraLogLocal(null, "Produto " + key + " processado com sucesso.");
                    } catch (Exception e) {
                        try {
                            connection.rollback();
                        } catch (SQLException | NullPointerException ex) {
                            logger.error("Erro ao atualizar imagens.", ex);
                        }

                        // Rollback das cópias
                        for (ImgProdBean imagem : novasImagens) {
                            try {
                                File file = new File(ParaEcomService.getDiretorioImagens() + imagem.getUrlImagem());
                                FileUtils.deleteFile(file);
                            } catch (Exception ex) {}
                        }
                    }
                }
                
                JOptionPane.showMessageDialog(null, "Backup concluido, verifique o log para mais detalhes.");
                btnBackupLocal.setEnabled(true);
            }
        }.start();
    }//GEN-LAST:event_btnBackupLocalActionPerformed

    private void btnBackupEcomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackupEcomActionPerformed
        if (!InternetUtil.isConectado()) {
            txtLogEcom.setText("Sem acesso a internet! Por favor verifique sua conexão");
            return;
        }
        
        int x = JOptionPane.showConfirmDialog(this,"Deseja iniciar backup da loja virtual?", "Atenção", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); 
        
        if (x == JOptionPane.OK_OPTION){ 
            progressBarEcom.setIndeterminate(true);
            btnBackupEcom.setEnabled(false);
            txtLogEcom.setText("");
            
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        BackupImagemService.realizaBackup(new Function<String>() {
                            @Override
                            public void run(String p) {
                                registraLogEcom(p);
                            }
                        });
                        
                        registraLogEcom("\nBackup realizado com sucesso.\nSuas imagens estão em \"" +
                                ParaEcomService.getDiretorioImagens() + "\"");
                    } catch (Exception e) {
                        registraLogEcom("Erro ao realizar backup");
                    } finally {
                        btnBackupEcom.setEnabled(true);
                        progressBarEcom.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Backup concluido, verifique o log para mais detalhes.");
                    }
                }
            };  
            thread.setName("Backup das imagens");
            thread.start();
        } 
    }//GEN-LAST:event_btnBackupEcomActionPerformed

    private String processaImagemWeb(ImgProdBean imgProd){
        try {
            Pattern pattern = Pattern.compile("[http|https]://(.+)[\\\\|/]((.+)\\.(.+))$");
            Matcher matcher = pattern.matcher(imgProd.getUrlImagem());
            
            if (!matcher.find()) {
                registraLogLocal(imgProd, "Não foi possível mapear a URL.");
                return imgProd.getUrlImagem();
            }
            
            String nome = this.getNomeImagem(matcher.group(2));
            URL website = new URL(imgProd.getUrlImagem());
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(ParaEcomService.getDiretorioImagens() + nome);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            
            registraLogLocal(imgProd, "Download realizado com sucesso.");
            return nome;
        } catch (IOException e) {
            registraLogLocal(imgProd, "Falha ao fazer download da imagem.");
            return imgProd.getUrlImagem(); //Em casos de erro não remove o URL.
        }
    }
    
    private String processaImagemLocal(ImgProdBean imgProd){
        File imagem = new File(imgProd.getUrlImagem());
        if (!imagem.exists()) {
            imagem = new File(ParaEcomService.getDiretorioImagens() + imagem.getName());
            if (!imagem.exists()) {
                registraLogLocal(imgProd, "Imagem não encontrada.");
                return null;
            } else {
                // Imagem já esta correta
                return imagem.getName();
            }
        }

        if (ParaEcomService.getDiretorioImagens().equals((imagem.getParent() + "\\"))) {
            registraLogLocal(imgProd, "Imagem já está no local correto.");
            return imagem.getName();
        } else {
            try {
                String nomeDestino = this.getNomeImagem(imagem.getName());
                FileUtils.copyFile(imagem.getAbsolutePath(), ParaEcomService.getDiretorioImagens() + nomeDestino);

                
                registraLogLocal(imgProd, "Imagem \"" + nomeDestino + "\" copiada com sucesso, origuem:");
                return nomeDestino;
            } catch (Exception e) {
                registraLogLocal(imgProd, "Falha ao copiar imagem.");
                return null;
            }   
        }
    }
    
    private void registraLogLocal(ImgProdBean imgProd, String log){
        StringBuilder text = new StringBuilder();
        text.append(txtLogLocal.getText());
        
        if (!text.toString().isEmpty()) {
            text.append("\n");
        }
        
        if (imgProd != null) {
            text.append(MessageFormat.format("Produto {0}: {1} \"{2}\"", imgProd.getCodProd(), log, imgProd.getUrlImagem()));
        } else {
            text.append(log);
        }
        txtLogLocal.setText(text.toString());
    }
    
    private void registraLogEcom(String log){
        StringBuilder text = new StringBuilder();
        text.append(txtLogEcom.getText());
        
        if (!text.toString().isEmpty()) {
            text.append("\n");
        }
        text.append(log);
        txtLogEcom.setText(text.toString());
    }
    
    private String getNomeImagem(String nomeOriginal){
        Pattern pattern = Pattern.compile("(.+)\\.(.+)$");
        Matcher matcher = pattern.matcher(nomeOriginal);
       
        if (!matcher.find()) {
            throw new RuntimeException("Falha ao processar nome do arquivo.");
        }
        
        String nome = nomeOriginal;
        int index = 1;
        while (new File(ParaEcomService.getDiretorioImagens() + nome).exists()) {
            nome = matcher.group(1) + "-" + index + "." + matcher.group(2);
            index++;
        }
        
        return nome;
    }
        
    public ProdImgDAO getImageDAO() {
        if (imageDAO == null) {
            imageDAO = new ProdImgDAO();
        }
        return imageDAO;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackupEcom;
    private javax.swing.JButton btnBackupLocal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JProgressBar progressBarEcom;
    private javax.swing.JProgressBar progressBarLocal;
    private javax.swing.JTextArea txtLogEcom;
    private javax.swing.JTextArea txtLogLocal;
    // End of variables declaration//GEN-END:variables
}
