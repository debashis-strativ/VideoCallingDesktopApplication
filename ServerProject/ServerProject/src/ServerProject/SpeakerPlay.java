/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerProject;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author John
 */
public class SpeakerPlay extends Thread{

    int udpPort;

    public SpeakerPlay(int udpPort) {
        this.udpPort = udpPort;
        //runVOIP();
        start();
    }
    
    public void run(){
        runVOIP();
    }

    ByteArrayOutputStream byteOutputStream;
    AudioFormat adFormat;
    AudioInputStream InputStream;
    SourceDataLine speaker;

    public void runVOIP() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(udpPort);
            byte[] receiveData = new byte[10000];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
             System.out.println("RECEIVED: " + receivePacket.getAddress().getHostAddress() + " " + receivePacket.getPort());
                try {
                    
                    byte audioData[] = receivePacket.getData();
                    InputStream byteInputStream = new ByteArrayInputStream(audioData);
                    AudioFormat adFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,6000,16,2,4,6000,false);
                    InputStream = new AudioInputStream(byteInputStream, adFormat, audioData.length / adFormat.getFrameSize());
                    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
                    speaker = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                    speaker.open(adFormat);
                    speaker.start();
                    Thread playThread = new Thread(new SpeakerPlay.PlayThread());
                    playThread.start();
                } catch (Exception e) {
                    System.out.println(e);
                    //System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class PlayThread extends Thread {

        byte tempBuffer[] = new byte[10000];

        public void run() {
            try {
                int cnt;
                while ((cnt = InputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        speaker.write(tempBuffer, 0, cnt);
                    }
                }
                //  sourceLine.drain();
                // sourceLine.close();
            } catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }
    
}
