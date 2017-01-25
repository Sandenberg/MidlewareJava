/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.ui.util;

import java.lang.reflect.Method;
import javax.swing.SwingWorker;

/**
 *
 * @author AlexsanderPimenta
 */
public class SwingThread extends SwingWorker {

    private Object obj;
    private String method;

    public SwingThread(Object obj, String method) {
        this.obj = obj;
        this.method = method;
    }

    @Override 
    protected Object doInBackground() throws Exception {
        try {
            Method something = this.obj.getClass().getDeclaredMethod(this.method, new Class[0]);
            something.invoke(this.obj, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    @Override
    protected void done() {
    
    }
}
