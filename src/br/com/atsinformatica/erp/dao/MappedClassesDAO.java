package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.midler.Main;
import br.com.atsinformatica.midler.annotation.GenericType;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * Classe que realiza mapeamento através do pacote em que se encontra
 *
 * @author AlexsanderPimenta
 */
public class MappedClassesDAO {

    private static Map<TipoEntidade, Class<?>> mapClasses;
    private static Map<TipoEntidade, GenericDAO> mapInstancias;    

    /**
     * Mapeia classes de um determinado pacote
     */
    private static void mapeiaClasses(){
        
        if (Main.MODE_DEVELOPER) {
            if (MappedClassesDAO.mapClasses == null) {
                try {
                    MappedClassesDAO.mapClasses = new HashMap();
                    MappedClassesDAO.mapInstancias = new HashMap();
                    //pacote a ser mapeado
                    String pckgname = "br.com.atsinformatica.erp.dao";
                    String path = URLDecoder.decode(MappedClassesDAO.class.getResource("./").getPath(), "UTF-8");
                    File file = new File(path);
                    //lista de classes deste pacote
                    String[] files = file.list();
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].endsWith(".class")) {
                                //classe
                                Class<?> cla = Class.forName(pckgname + '.' + files[i].substring(0, files[i].length() - 6));
                                //anotação
                                GenericType a = cla.getAnnotation(GenericType.class);
                                if (a != null && a instanceof GenericType) {
                                    //Insere classe mapeada
                                    MappedClassesDAO.mapClasses.put(((GenericType) a).typeClass(), (Class<?>) cla);
                                }
                            }
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException("Falha ao ler classes: "+ex);
                } catch (UnsupportedEncodingException ex) {
                    //Logger.getLogger(MappedClassesDAO.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(MappedClassesDAO.class.getName()).log(Priority.ERROR, null, ex);
                }
            }
        }
        else {
            if (MappedClassesDAO.mapClasses == null) {
                try {
                    MappedClassesDAO.mapClasses = new HashMap();
                    MappedClassesDAO.mapInstancias = new HashMap();
                    CodeSource src = MappedClassesDAO.class.getProtectionDomain().getCodeSource();
                    if (src != null) {
                        ZipInputStream zip = new ZipInputStream(src.getLocation().openStream());
                        while (true) {
                            ZipEntry e = zip.getNextEntry();
                            if (e == null) {
                                break;
                            }
                            if (e.getName().endsWith(".class")) {
                                Class<?> c = Class.forName(e.getName().replace(".class", "").replace("/", "."));
                                GenericType a = c.getAnnotation(GenericType.class);
                                if (a != null && a instanceof GenericType) {
                                    //Insere classe mapeada
                                    MappedClassesDAO.mapClasses.put(((GenericType) a).typeClass(), (Class<?>) c);
                                }
                            }
                        }
                                        
                    }
                } catch (Exception ex) {
                    throw new RuntimeException("Falha ao ler classes: " + ex);
                }
            }
        }
    }

    public static GenericDAO getInstancia(TipoEntidade entidade) {
        MappedClassesDAO.mapeiaClasses();

        if ((!MappedClassesDAO.mapInstancias.containsKey(entidade)) && 
                (MappedClassesDAO.mapClasses.get(entidade) == null || 
                !GenericDAO.class.isAssignableFrom(MappedClassesDAO.mapClasses.get(entidade)))) {
                // throw new RuntimeException("Não foi encontrada nenhuma classe para este tipo.");
                return null;
        }
        else {            
            try {
                GenericDAO gc = (GenericDAO) MappedClassesDAO.mapClasses.get(entidade).newInstance();
                MappedClassesDAO.mapInstancias.put(entidade, gc);
                return MappedClassesDAO.mapInstancias.get(entidade);
            } catch (Exception ex) {
                // throw new RuntimeException("Falha ao instanciar a classe");
                return null;
            }
        }
        
        
    }
}
