/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.components.renderer;

import br.com.atsinformatica.utils.FormaPagamentoEcom;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author AlexsanderPimenta
 */
public class FormaPagamentoCellRenderer extends DefaultTableCellRenderer {

    public FormaPagamentoCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value!=null){
           label.setText(FormaPagamentoEcom.nomeOf((int) value).toUpperCase());    
        }        
        this.setHorizontalAlignment(CENTER);
        return this;
    }

    {
    }
}
