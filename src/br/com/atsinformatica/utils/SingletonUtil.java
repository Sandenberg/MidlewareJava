package br.com.atsinformatica.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Mantem apenas uma instancias de classes singletons
 * 
 * @author niwrodrigues
 */
public class SingletonUtil {

    private static Map<Class<?>, Object> instancias;

    public static <T> T get(Class<T> classe) throws NullPointerException {
        if (instancias == null) {
            instancias = new HashMap<>();
        }
        if (!instancias.containsKey(classe)) {
            try {
                instancias.put(classe, classe.newInstance());
            } catch (Exception e) {
                throw new NullPointerException();
            }
        }
        
        return (T) instancias.get(classe);
    }
    
}
