
package br.com.atsinformatica.midler.components.renderer;

import br.com.atsinformatica.midler.entity.EnumDescritivel;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**.
 * 
 * @author niwrodrigues
 */
public class EnumDescritivelTableCellRenderer extends DefaultTableCellRenderer {

    public EnumDescritivelTableCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof EnumDescritivel) {
            label.setText(((EnumDescritivel) value).getDescricao());
        }
        
        return this;
    }
}
