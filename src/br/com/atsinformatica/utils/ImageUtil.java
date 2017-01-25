package br.com.atsinformatica.utils;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author niwrodrigues
 */
public class ImageUtil {
    
    public static ImageIcon criaIcon(String caminho){
        return criaIcon(-1, -1, caminho);
    }
    
    public static ImageIcon criaIcon(int width, int height, String caminho){
        ImageIcon image = new ImageIcon(System.getProperty("user.dir") + "\\src\\assets\\" + caminho);
        if (width > 0 && height > 0) {
            image = resizeImage(width, height, image);
        }
        
        return image;
    }
    
    /**
     * Redimensiona o tamanho da imagem original
     *
     * @param width largura
     * @param height altura
     * @param icon imagem
     * @return ImageIcon
     */
    public static ImageIcon resizeImage(int width, int height, ImageIcon icon) {
        if (icon != null) {
            Image img = icon.getImage();
            Image imgResized = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            icon = new ImageIcon(imgResized);
            return icon;
        } else {
            return null;
        }
    }
}
