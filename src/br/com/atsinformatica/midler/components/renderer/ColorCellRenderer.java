/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.components.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderizador para campo de status
 *
 * @author AlexsanderPimenta
 */
public class ColorCellRenderer extends DefaultTableCellRenderer {

    public ColorCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            if (table.getValueAt(row, column).equals("Erro")) {
                cellComponent.setForeground(Color.red);
            } else {
                cellComponent.setForeground(Color.black);
            }
        }
        return cellComponent;
    }
}
