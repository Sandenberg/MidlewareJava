package br.com.atsinformatica.midler.components;

import br.com.atsinformatica.midler.components.renderer.EnumDescritivelComboCellRender;
import br.com.atsinformatica.midler.entity.EnumDescritivel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author niwrodrigues
 */
public class ComponentUtil {
    
    /**
     * Preenche o combobox informado com as opções do enum informado
     */
    public static void preencheCompo(JComboBox comboBox, EnumDescritivel... opcoes){
        /* Caso não seja do render correto, atualiza */
        if (!(comboBox.getRenderer() instanceof EnumDescritivelComboCellRender)) {
            comboBox.setRenderer(new EnumDescritivelComboCellRender());
        }
        
        /* Converte para lista e adiciona opção vazia */
        List<EnumDescritivel> list = new ArrayList<>(Arrays.asList(opcoes));
        list.add(null);
        
        /* Ordena */
        Collections.sort(list, new Comparator<EnumDescritivel>(){
            @Override
            public int compare(EnumDescritivel e1, EnumDescritivel e2) {
                if (e1 == null && e2 == null) {
                    return 0;
                } else if (e1 == null) {
                    return -1;
                } else if (e2 == null) {
                    return 1;
                }
                return e1.getDescricao().compareTo(e2.getDescricao());
            }
        });
        
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (EnumDescritivel opcao : list) {
            model.addElement(opcao);
        }
        
        comboBox.setModel(model);
    }
}
