/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.components.renderer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author AlexsanderPimenta
 */
public class LeftCellRenderer extends DefaultTableCellRenderer {
    
    public LeftCellRenderer(){
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
            label.setText((String)value);
            this.setHorizontalAlignment(LEFT);
        }
        return this;
    }
    
}
