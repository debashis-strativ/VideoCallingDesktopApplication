/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package ServerProject;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
public class ServerHandler extends JPanel {

    private static BufferedImage image;
    private JButton button = new JButton("capture");
    private OutputStream  outToClient;
    private InputStream   inputFramCLient;
    Socket s;
  
    int count = 1;
    static int exit=-9;
    
   public int port=6231;
   
    public ServerHandler() throws IOException {
    
         this.setLocation(600,200);  
         button.addActionListener((ActionListener) this);
         this.add(button);     
    }

    private BufferedImage getimage() {
        return image;
    }

    private void setimage(BufferedImage newimage) {
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
                 System.loadLibrary("opencv_java2413");

        JFrame frame = new JFrame(" Receiver ");
     
        frame.setLocation(600,0);
      //  frame.setVisible(true);
      VideoCapture camera =new VideoCapture(0);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        ServerSocket serverSocket = new ServerSocket(6231);
        
     while(true)
        {
            
            Socket Socket = serverSocket.accept();
           
            ServerWriter serverwriter = new ServerWriter(Socket, camera);
            Thread writerThread = new Thread(serverwriter);
            writerThread.start();  
        
            ServerReceiver serverReceiver=new ServerReceiver(Socket,frame,camera);
            Thread readThread = new Thread(serverReceiver);
            readThread.start();
        }
    }
        
  
 
}