
package ServerProject;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JFrame;
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
 * @author eh5i0
 */
public class ServerWriter implements Runnable{
    
    
    
    private OutputStream outToClient;
    private byte[] ByteArrray;
    private DataOutputStream DataoutputStream;
   private ImageConvertion imageconvertion;
   private Socket socket;
   private   Mat Mat_Image;
      private DisplayWindow  Canvas;
   private JFrame j;
   private   VideoCapture camera = null;
   
    public ServerWriter(Socket outToClient,VideoCapture camera) throws IOException{
       this.camera=camera;
         imageconvertion=new ImageConvertion();
         this.socket=outToClient;
        this.outToClient = outToClient.getOutputStream();
        new Thread(){
                  @Override
                  public void run()
                  {
                      try {
                 
                 new  MicrophoneOpen(9876);
                } catch (Exception ex) {                }
               }
            }.start();
    }
    
    

    @Override
    public void run() {
         System.loadLibrary("opencv_java2413");
         framehabdle();
         Mat_Image = new Mat();
        
        MatToBufImg mat2Buf = new MatToBufImg();
        
      
        try {
            //camera = new VideoCapture(0);
        } catch (Exception xx) {
            xx.printStackTrace();
        }
        if (camera.open(0)) {
            while (true) {
            
               camera.read(Mat_Image);
                
               if (!Mat_Image.empty()) 
                {
                  facedetection();
                mat2Buf.setMatrix(Mat_Image, ".jpg");
                  Canvas.setimage(mat2Buf.getBufferedImage());
           Canvas.repaint();
             
                 try {
                  this.ByteArrray=  imageconvertion.bufferedImageToByteArray(mat2Buf.getBufferedImage());
                  this.DataoutputStream=new DataOutputStream(this.outToClient);
                  this.DataoutputStream.writeInt(this.ByteArrray.length);
                  this.DataoutputStream.write(ByteArrray,0,this.ByteArrray.length);
                 
                
                   this.outToClient.flush();
                System.out.println(" sucessfully write");
                           
                     } 
                    catch (IOException ex)
                    {
                         camera.release();
                         System.exit(0);
                    }
     
               
                    }
                
                     else {
                    System.out.println("problems with webcam image capture");
                    break;
                }
               
              
                    
            }
        }
       
        camera.release();
    }
    public void framehabdle()
    {
         Canvas =new DisplayWindow();
         j=new JFrame("You");
         j.setSize(400,400);
         j.setVisible(true);
         j.add(Canvas);
         j.setLayout(null);
         Canvas.setBounds(0,0,400,400);
          j.addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent evt) {
     
           camera.release();
           System.out.println("closed");
           System.exit(0);      
   }
  });
        
    }
    public void facedetection ()
    {
            CascadeClassifier faceDetector = new CascadeClassifier("C:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");

        MatOfRect faceDetections = new MatOfRect();
                        
         faceDetector.detectMultiScale(this.Mat_Image, faceDetections);
         
                     for (Rect rect : faceDetections.toArray()) {
                         
        Core.rectangle(this.Mat_Image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,0, 255),2,2,0);// mat2Buf, mat2Buf);
                   
                     }
                     
         System.out.println("face detection: " + faceDetections.toArray().length);
         
                 if (faceDetections.toArray().length == 0) {
                     
                        System.out.println(" Face is not here !");
              }
                 
                 
    }
    }
    

