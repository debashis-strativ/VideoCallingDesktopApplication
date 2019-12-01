
package ServerProject;


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
public class ServerReceiver implements Runnable
{
    private final Socket socketInstance;
    
    private JFrame servertFrame;
    private BufferedImage img;
   
   private DisplayWindow Canvas;
    private DisplayWindow Canvas1;
  private InputStream Inputstream; 
  int i =0;
   private ImageConvertion Imageconvertion;
   private DataInputStream ReceiveFromClient; 
   private VideoCapture camera;
   private byte[] bytearray;
   
    public ServerReceiver(Socket s, JFrame clientFrame,VideoCapture camera) throws IOException
    {
        socketInstance = s;
        this.servertFrame = clientFrame;
       this.servertFrame.setVisible(true);
       this.camera=camera;
       Canvas =new DisplayWindow();
       Canvas1 =new DisplayWindow(); 
       
       servertFrame.setSize(600,600);
       this.servertFrame.setLayout(null);
       Canvas.setBounds(0,0,600,600);
        Canvas1.setBounds(0,0,100,100);
       this.servertFrame.add(Canvas);
      //   this.servertFrame.add(Canvas1);
      
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
            this.ReceiveFromClient=new DataInputStream(Inputstream);
            int len=ReceiveFromClient.readInt();
            byte []bytearray=new byte[len];
            
             if (len > 0)
             {
           System.out.println(len);
           this.ReceiveFromClient.readFully(bytearray);
             }
             
           img=Imageconvertion.byteArrayToBufferedImage(bytearray);
          
           Canvas.setimage(img);
           Canvas.repaint();
          Canvas1.setimage(img);
           Canvas1.repaint();
            System.out.println("Paint");
            
        } catch (IOException ex) {
            this.camera.release();
            System.exit(0);
        }
        }
    }
    
     
}