/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package continuity;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Cypher
 */
public class Continuity {
    
    public static PopupMenu menu;
    private static ServerThread serverThread;
    private static InetAddress ip;
    public static String path;
    /**
     * @param args the command line arguments
     * @throws java.awt.AWTException
     */
    public static void main(String[] args) throws AWTException {
        if(!SystemTray.isSupported()){
            System.out.println("System Tray is not supported");
            return;
        }
        
        SystemTray tray = SystemTray.getSystemTray();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(path+"continuity.jpg");
        
        menu = new PopupMenu();
        
        MenuItem startServerItem = new MenuItem("Start Server");
        startServerItem.addActionListener((ActionEvent e) -> {
            try {
                ip = InetAddress.getLocalHost();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Continuity.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null,"Server Started at \n"+ip.getHostAddress());
            serverThread = new ServerThread();
            serverThread.start();
        });
        menu.add(startServerItem);
        
        MenuItem stopServerItem = new MenuItem("Stop Server");
        stopServerItem.addActionListener((ActionEvent e) -> {
            serverThread.close();
            JOptionPane.showMessageDialog(null,"Server Stopped");
        });
        
        menu.add(stopServerItem);
 
        MenuItem settingsItem = new MenuItem("Directory");
        settingsItem.addActionListener((ActionEvent e) -> {
//            JPanel jp = new JPanel();
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.showSaveDialog(null);
            path = jfc.getCurrentDirectory().getAbsolutePath();
        });
        menu.add(settingsItem);
        
        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        menu.add(closeItem);
        
        
        
        TrayIcon icon = new TrayIcon(image,"Continuity",menu);
        icon.setImageAutoSize(true);
        
        tray.add(icon);
        
    }
    
     public static void messagePrint() {
         System.out.println("item closed");
     }

}
