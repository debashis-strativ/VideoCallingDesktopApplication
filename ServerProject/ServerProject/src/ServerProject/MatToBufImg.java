/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerProject;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

/**
 *
 * @author OldSpice
 */
public class MatToBufImg {

    Mat matrix;
    MatOfByte Matofbyte, mob2;
    String fileExten;

    public MatToBufImg() {
    }

    public MatToBufImg(Mat amatrix, String fileExt) {
        matrix = amatrix;
        fileExten = fileExt;
    }

    public void setMatrix(Mat amatrix, String fileExt) {
        matrix = amatrix;
        fileExten = fileExt;
        Matofbyte = new MatOfByte();
        
    }

    public BufferedImage getBufferedImage() {
        
        Highgui.imencode(fileExten, matrix, Matofbyte);
        byte[] byteArray = Matofbyte.toArray();
        
        BufferedImage bufImage = null;
        try {
            InputStream in= new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
           
        } catch (Exception ext) {
            ext.printStackTrace();
        }
        BufferedImage bi = bufImage;
        ImageIcon ii = null;
      //  ii = new ImageIcon(bi);
        Image newimg = bi.getScaledInstance(645,455,java.awt.Image.SCALE_SMOOTH);
        ii = new ImageIcon(newimg);
        Image i2 = ii.getImage();
        bufImage = new BufferedImage(i2.getWidth(null), i2.getHeight(null), BufferedImage.SCALE_SMOOTH);
        bufImage.getGraphics().drawImage(i2,0,0, null);
        return bufImage;
    }
}
