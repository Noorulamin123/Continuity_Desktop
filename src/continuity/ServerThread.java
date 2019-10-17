/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package continuity;

import static continuity.Continuity.menu;
import static continuity.Continuity.path;
import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 *
 * @author Cypher
 */
public class ServerThread extends Thread{
    private static boolean flag;
    public static boolean signal = false;
    private static String Result;
    private static ReceiveThread receiveThread;
    private static ServerSocket serverSocket;
    private static Socket socket;
    public static MenuItem captureItem;
    @Override
    public void run(){
        super.run();
        try {
            connect();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void connect() throws IOException {
        flag = true;
        

        serverSocket = new ServerSocket(3330);
        while(flag){
            socket = serverSocket.accept();
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            if(socket.isConnected()){
                signal = true;  
                JOptionPane.showMessageDialog(null,"Connection Established");
                captureItem = new MenuItem("Capture");
                captureItem.addActionListener((ActionEvent e) -> {
                    JOptionPane.showMessageDialog(null,"Capture Activity started!");
                    try {
                        outputStream.writeInt(1);
                        Result = inputStream.readUTF();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    System.out.println(Result);
                    receiveThread = new ReceiveThread();
                    receiveThread.start();
                    try {
                        receiveThread.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    try {
                        Desktop.getDesktop().open(new File(path));
                    } catch (IOException ex) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });     
                menu.add(captureItem);
            }
        }
    }
    
    public void close(){
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        receiveThread.interrupt();
        interrupt();
    }
}
