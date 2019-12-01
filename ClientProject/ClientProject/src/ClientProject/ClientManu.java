/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientProject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Debashis
 */
public class ClientManu extends javax.swing.JFrame {

    private static Socket socket;
    private static DataOutputStream data;
    public static AudioStream audiostream;
    
    public ClientManu() throws IOException 
    {      
        super("Client");
        initComponents();
        
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CallButton = new javax.swing.JButton();
        ReceiveButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 255, 204));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CallButton.setText("Call");
        CallButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CallButtonActionPerformed(evt);
            }
        });
        getContentPane().add(CallButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 130, 97, -1));

        ReceiveButton.setText("Receive");
        ReceiveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReceiveButtonActionPerformed(evt);
            }
        });
        getContentPane().add(ReceiveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 171, 96, -1));

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel1.setText("MVdo");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 114, 53));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ClientProject/ima.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CallButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CallButtonActionPerformed
       
         try {
             {
          data=new DataOutputStream(socket.getOutputStream());
          data.writeInt(11);
          this.CallButton.setVisible(false);
          this.ReceiveButton.setVisible(false);
             }
            
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_CallButtonActionPerformed

    private void ReceiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReceiveButtonActionPerformed
        try {
             AudioPlayer.player.stop(audiostream);
             data=new DataOutputStream( socket.getOutputStream());
             data.writeInt(12);
             this.setVisible(false);
             new Thread()
        {
            public void run()
            {
                try {
                    
                    ClientHandler.main(null);
                    
                } catch (Exception ex) {
                }
            }
        }.start();
             
        } catch (IOException ex) {
            
        }
           
             
           
       
    }//GEN-LAST:event_ReceiveButtonActionPerformed

 public void palysound() throws Exception
 {
    String song = "o.wav";
    FileInputStream s=new FileInputStream(new File(song));
    audiostream=new AudioStream(s);
    AudioPlayer.player.start(audiostream);
   
 }
    public static void main(String args[]) throws Exception  {
        
    socket=new Socket("192.168.0.100",6666);
   
    ClientManu menu=new ClientManu();
    menu.setVisible(true);
    menu.ReceiveButton.setVisible(false);
    int i;
        while(true)
             { 
        
          try {
            DataInputStream   data = new DataInputStream(socket.getInputStream());
            i =data.readInt();
            System.out.println(i);                 
        if(i==9) 
        {
            menu.ReceiveButton.setVisible(true);
            menu.CallButton.setVisible(false);
            menu.palysound();
        }
        if(i==10) //i=10 when call receive from server
        {  
            menu.setVisible(false);
            
        new Thread()
        {
            public void run()
            {
                
                try {
                    ClientHandler.main(null);
                } catch (Exception ex) {
                }
            }
        }.start();
        
       }
            } catch (IOException ex) {
                          
                      }
                        
             }
          
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CallButton;
    private javax.swing.JButton ReceiveButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
