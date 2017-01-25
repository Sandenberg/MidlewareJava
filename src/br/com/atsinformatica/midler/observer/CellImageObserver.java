/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.midler.observer;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import javax.swing.JTable;

/**
 * Observer de imagem dentro da grid
 * @author AlexsanderPimenta
 */
public class CellImageObserver implements ImageObserver {
    JTable table;
    int row;
    int col;

    public CellImageObserver(JTable table, int row, int col) {
        this.table = table;
        this.row = row;
        this.col = col;
    }
    
    
    @Override
    public boolean imageUpdate(Image image, int flags, int x, int y, int w, int h) {
        if((flags & (FRAMEBITS | ALLBITS))!= 0){
            Rectangle rect = table.getCellRect(row, col, false);
            table.repaint(rect);
            
        }
        return (flags & (ALLBITS | ABORT)) == 0;
    }
    
}
