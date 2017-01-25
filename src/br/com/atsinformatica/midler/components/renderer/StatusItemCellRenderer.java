package br.com.atsinformatica.midler.components.renderer;

import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.observer.CellImageObserver;
import br.com.atsinformatica.utils.ImageUtil;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author AlexsanderPimenta
 */
public class StatusItemCellRenderer extends DefaultTableCellRenderer{
    
    public StatusItemCellRenderer(){
        super();
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
        final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      
        label.setText(""); // Coluna sem texto
        this.setHorizontalAlignment(CENTER);
        
        StatusIntegracao status = StatusIntegracao.converteValor(String.valueOf(value));
        if(status != null){
            switch (status) {
                case SINCRONIZADO: {
                    label.setIcon(ImageUtil.criaIcon(10, 10, "icons\\Ok-icon.png"));
                    break;
                }
                case ERRO: {
                    label.setIcon(ImageUtil.criaIcon(11, 11, "icons\\notification_error.png"));
                    break;
                }
                case PENDENTE: {
                    label.setIcon(ImageUtil.criaIcon(12, 12, "icons\\waiting.png"));
                    break;
                }
                case EM_ANDAMENTO: {
                    ImageIcon icon = ImageUtil.criaIcon("icons\\spinner.gif");
                    icon.setImageObserver(new CellImageObserver(table, row, column));
                    label.setIcon(icon);
                    break;
                }
            }
        } else {
            /* Não conseguiu identificar o status - Limpa */
            label.setIcon(null);
        }
        
        return this;
    }
}
