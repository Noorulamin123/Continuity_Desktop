/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package continuity;

import static continuity.Continuity.path;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cypher
 */
public class ReceiveThread extends Thread{
    
    private static ServerSocket receiveServerSocket;
    private static Socket receiveSocket;
    


     @Override
    public void run(){
        super.run();
        receive();
    }

    private void receive() {
        try {
            receiveServerSocket = new ServerSocket(3334);
            receiveSocket = receiveServerSocket.accept();
            if (receiveSocket.isConnected()){
                System.out.println("Connection established");

                FileOutputStream fos = new FileOutputStream(path+"image.jpg");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                InputStream is = receiveSocket.getInputStream();

                byte[] aByte = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(aByte)) != -1)
                {
                    bos.write(aByte, 0, bytesRead);
                    System.out.println("sendFile" + bytesRead);
                }
                receiveSocket.close();
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public void close(){
        try {
            receiveSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        interrupt();
    }
}
