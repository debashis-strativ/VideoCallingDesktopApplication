/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerProject;

import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author John
 */
public class MicrophoneOpen{

    int udpPort;
   InetAddress callerIP;
    public MicrophoneOpen( int udpPort) throws UnknownHostException {
      //  this.callerIP = InetAddress.getLocalHost();
     this.callerIP = InetAddress.getByName("192.168.43.162");

      //  this.callerIP = "localhost";
        this.udpPort = udpPort;
        
        captureAudio();

    }

    
    boolean stopaudioCapture = false;
    ByteArrayOutputStream byteOutputStream;
    AudioFormat adFormat;
    TargetDataLine Microphon;
    AudioInputStream InputStream;
   // SourceDataLine sourceLine;
    Graphics g;
    
    private void captureAudio() {
        try {
            adFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,6000,16,2,4,6000,false);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
            Microphon = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            Microphon.open(adFormat);
            Microphon.start();

            Thread captureThread = new Thread(new MicrophoneOpen.CaptureThread());
            captureThread.start();
        } catch (Exception e) {
            StackTraceElement stackEle[] = e.getStackTrace();
            for (StackTraceElement val : stackEle) {
                System.out.println(val);
            }
            System.exit(0);
        }
    }

    class CaptureThread extends Thread {

        byte tempBuffer[] = new byte[10000];

        public void run() {

            stopaudioCapture = false;
            try {
                DatagramSocket clientSocket = new DatagramSocket();
                //InetAddress IPAddress = InetAddress.getByName("DARKNESS");
                while (!stopaudioCapture) {
                    int cnt = Microphon.read(tempBuffer, 0, tempBuffer.length);
                    if (cnt > 0) {
                        DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length,callerIP,udpPort);
                        clientSocket.send(sendPacket);
                    }
                }
                byteOutputStream.close();
            } catch (Exception e) {
                System.out.println("CaptureThread::run()" + e);
                //System.exit(0);
            }
        }
    }
    
}
