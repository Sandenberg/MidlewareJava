/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.components.renderer;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author AlexsanderPimenta
 */
public class DateCellRenderer extends DefaultTableCellRenderer{
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public DateCellRenderer(){
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
        if (value != null) {
             label.setText(dateFormat.format(((Date)value).getTime())); 
             this.setHorizontalAlignment(CENTER);
        }
        return this;
    }
}
