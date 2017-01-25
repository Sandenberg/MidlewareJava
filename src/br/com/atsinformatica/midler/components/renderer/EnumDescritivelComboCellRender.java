package br.com.atsinformatica.midler.components.renderer;

import br.com.atsinformatica.midler.entity.EnumDescritivel;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *
 * @author niwrodrigues
 */
public class EnumDescritivelComboCellRender extends BasicComboBoxRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value != null && value instanceof EnumDescritivel) {
            EnumDescritivel item = (EnumDescritivel) value;
            setText(item.getDescricao());
        } else if (value == null || index == -1){
            setText("");
        } else {
            throw new UnsupportedOperationException("Valor não esperado.");
        }
        
        return this;
    }

}
