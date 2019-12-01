
package ClientProject;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import javax.swing.JFrame;
import org.opencv.highgui.VideoCapture;
/**
 *
 * @author Debashis
 */

public class ClientReceiver implements Runnable
{
   private  Socket socketInstance;
   private JFrame clientFrame;
   private BufferedImage img;
   private DisplayWindow Canvas;
   private InputStream Inputstream; 
   private final ImageConvertion Imageconvertion;
   private DataInputStream ReceiveFromServer; 
   private VideoCapture camera;
   private byte[] bytearray;
   
    public ClientReceiver(Socket s, JFrame clientFrame,VideoCapture camera)
    {
        socketInstance = s;
        this.clientFrame = clientFrame;
        this.clientFrame.setVisible(true);
        this.camera=camera;
        
        try {
            
            Canvas =new DisplayWindow();
            
        } catch (IOException ex) {
        }
        
        clientFrame.add(Canvas);
       Imageconvertion=new ImageConvertion();
       
       new Thread(){
                  @Override
                  public void run()
                  {
                   try {
                  new   SpeakerPlay(9876);
                } catch (Exception ex) {
                }
               }
            }.start();
    
    }
    @Override
    public void run()
    {
       
        while(true)
        {
           
        try {
            
            Inputstream = this.socketInstance.getInputStream();
            this.ReceiveFromServer=new DataInputStream(Inputstream);
            int len=ReceiveFromServer.readInt();
            byte []bytearray=new byte[len];
         
            if (len > 0)
             {
            System.out.println(len);
            this.ReceiveFromServer.readFully(bytearray);
             }
            
            img=Imageconvertion.byteArrayToBufferedImage(bytearray);
            Canvas.setimage(img);
            Canvas.repaint();
            System.out.println("Paint");
            
        } catch (IOException ex) {
            camera.release();
            System.exit(0);
        }
        }
    }
    
     
}