package br.com.atsinformatica.utils;

import java.util.Collection;

/**
 *
 * @author niwrodrigues
 */
public class StringUtil {
    
    public static Object unir(String separador, Collection<String> msg) {
        String[] array = new String[]{};
        array = msg.toArray(array);
        return StringUtil.unir(separador, array);
    }
    
    public static String unir(String separador, String[] itens){
        return StringUtil.unir(separador, separador, itens);
    }
    
    public static String unir(String separador, String ultimoSeparador, String[] itens){
        StringBuilder join = new StringBuilder();
        if (itens != null) {                    
            for (int i = 0; i < itens.length; i++) {
                join.append(itens[i]);
                
                if (i < itens.length - 2) {
                    join.append(separador);
                } else if (i < itens.length - 1) {
                    join.append(ultimoSeparador);
                }
            }
        }
        
        return join.toString();
    }
}
