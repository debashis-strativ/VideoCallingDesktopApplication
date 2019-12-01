package ClientProject;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.Socket;
import javax.swing.JFrame;
import org.opencv.highgui.VideoCapture;

/**
 *
 * @author Debashis
 */
public class ClientHandler  {

    public static void main(String args[]) throws IOException, Exception {
        System.loadLibrary("opencv_java2413");
        JFrame frame = new JFrame(" Caller ");
        frame.setSize(600,600);
        frame.setLocation(600,0);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
      // Socket socket = new Socket("192.168.43.242",6231);
      
        Socket socket = new Socket("192.168.0.100",6231);
        VideoCapture camera =new VideoCapture(0);
        
        ClientReceiver clientReceiver = new ClientReceiver(socket,frame,camera);
        Thread t = new Thread(clientReceiver);
        t.start();  
     
        ClientWriter clientwriter = new ClientWriter(socket,camera);
        Thread t1 = new Thread(clientwriter);
        t1.start();
     
    }
}
