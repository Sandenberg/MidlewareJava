/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Documento que limita quantidade de caracteres digitados em um campo
 * @author AlexsanderPimenta
 */
public class LimitedDocument extends PlainDocument {
    //tamanho maximo inicial
    private int maxSize = 10;
    /*
     * Construtor padrão que recebe o tamanho máximo
     */
    public LimitedDocument(int maxSize){
        this.maxSize = maxSize;
    }
    
    
    /**
     * Limita caracteres digitados
     * @param offset
     * @param str
     * @param attr
     * @throws BadLocationException 
     */
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException{
        if(str == null) return;
        
        String pastString = this.getText(0, this.getLength());
        int newSize = pastString.length() + str.length();
        
        if(newSize <= maxSize)
            super.insertString(offset, str, attr);
        else
            super.insertString(offset, "", attr);
        
    }
    
}
