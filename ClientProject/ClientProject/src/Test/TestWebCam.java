/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Skpe;

import ClientProject.MatToBufImg;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author OldSpice
 */
public class TestWebCam extends JPanel implements ActionListener {

    private BufferedImage image;
    private  static JButton button = new JButton("capture");
  static int  exit=-9;
    int count = 1;

    public TestWebCam() {
        super();
        button.addActionListener((ActionListener) this);
        this.add(button);
    }

    private BufferedImage getimage() {
        return image;
    }

    public void setimage(BufferedImage newimage) {
        image = newimage;
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (this.image == null) {
            return;
        }
       g.drawImage(this.image,15,50, this.image.getWidth(), this.image.getHeight(),null);
       g.draw3DRect(10,45,this.image.getWidth()+10,this.image.getHeight()+10, true);
       g.draw3DRect(7,42,this.image.getWidth()+16,this.image.getHeight()+16, true);

    }

    public static void main(String args[]) throws Exception {
        JFrame frame = new JFrame("   WebCam");
     
     frame.add(button);
       System.loadLibrary("opencv_java2413");
       
  // CascadeClassifier faceDetector=new CascadeClassifier("C:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_silverware.xml");
  // CascadeClassifier faceDetector=new CascadeClassifier("C:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_profileface.xml");
      CascadeClassifier faceDetector = new CascadeClassifier("C:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");
       
      TestWebCam canvas = new TestWebCam();

        frame.add(canvas);
  
        frame.setVisible(true);
        
        Mat webcam_image = new Mat();
        
        MatToBufImg mat2Buf = new MatToBufImg();
        
        VideoCapture camera = null;
        try {
            camera = new VideoCapture(0);
        } catch (Exception xx) {
            xx.printStackTrace();
        }
        if (camera.open(0)) {
            while (true) {
                camera.read(webcam_image);
                if (!webcam_image.empty()) {
                    
                   frame.setSize(webcam_image.width()+50, webcam_image.height()+85);
                   
                        MatOfRect faceDetections = new MatOfRect();
                        
         faceDetector.detectMultiScale(webcam_image, faceDetections);
         
                     for (Rect rect : faceDetections.toArray()) {
                         
        Core.rectangle(webcam_image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,0, 255),2,2,0);// mat2Buf, mat2Buf);
                   
                     }
                     
         System.out.println("face detection: " + faceDetections.toArray().length);
         
                 if (faceDetections.toArray().length == 0) {
                     
                        System.out.println(" Face is not here !");
              }
                 
                    mat2Buf.setMatrix(webcam_image, ".jpg");
                    
                    canvas.setimage(mat2Buf.getBufferedImage());
                    canvas.repaint();
                 
                   
                }
                 else {
                    System.out.println("problems with webcam image capture");
                    break;
                } 
                   if(exit==0)
                {
                    System.out.println("ses");
                    camera.release();
                    
                    System.exit(0);
                }
                frame.addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent evt) {
            
           exit=0;
           System.out.println(" ses");
               // 
   }
  });
            }
            
        }
    }
        
    @Override
    public void actionPerformed(ActionEvent e) {
        String ans = JOptionPane.showInputDialog(null, "Enter your Choice\nColor image/Grey image");
        System.out.println(ans);
        
        BufferedImage bi = image;
        ImageIcon ii = null;
        ii = new ImageIcon(bi);
        Image newimg =bi.getScaledInstance(ii.getIconWidth(),ii.getIconHeight(), java.awt.Image.SCALE_SMOOTH);
        ii = new ImageIcon(newimg);
        Image i2 = ii.getImage();
        image = new BufferedImage(i2.getWidth(null), i2.getHeight(null),BufferedImage.SCALE_SMOOTH);
        image.getGraphics().drawImage(i2,0,0, null);
        RenderedImage rendered = null;
        if (i2 instanceof RenderedImage)
        {
            rendered = (RenderedImage) i2;
        }
        else {
            BufferedImage buffered = null;
            if (!ans.equalsIgnoreCase("color")) {
                buffered = new BufferedImage(
                        ii.getIconWidth(),
                        ii.getIconHeight(),
                        BufferedImage.TYPE_BYTE_GRAY);
            }
            else {
                buffered = new BufferedImage(
                        ii.getIconWidth(),
                        ii.getIconHeight(),
                        BufferedImage.SCALE_SMOOTH);
            }
            Graphics2D g = buffered.createGraphics();
            g.drawImage(i2,0,0, null);
            g.dispose();
            rendered = buffered;
        }
        try {
            
            ImageIO.write(rendered, "JPEG", new File("saved.jpg"));
        } catch (Exception ex) {
        }
    }
     public static void facedetection(){
         
        
    }
}