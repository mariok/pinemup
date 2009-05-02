package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.SSLHandshakeException;
import javax.swing.JOptionPane;
import sun.misc.BASE64Encoder;


import net.sourceforge.pinemup.gui.I18N;
import net.sourceforge.pinemup.logic.UserSettings;

public class WebdavConnection extends ServerConnection {
   private String protocol;
   
   public WebdavConnection(boolean sslEnabled) {
      if (sslEnabled) {
         protocol = "https";
      } else {
         protocol = "http";
      }
   }
   
   public WebdavConnection() { //default without ssl
      protocol = "http";
   }
   
   public void importNotesFromServer() {
      boolean downloaded = true;
      try {
         makeBackupFile();
         File f = new File(UserSettings.getInstance().getNotesFile());
         FileOutputStream fos = new FileOutputStream(f);
         String urlString = protocol + "://" + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir() + f.getName();
         URL url = new URL(urlString);
         HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
         urlc.setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode((new String(UserSettings.getInstance().getServerUser() + ":" + UserSettings.getInstance().getServerPasswdString())).getBytes()));
         InputStream is = urlc.getInputStream();
         int nextByte = is.read();
         while(nextByte != -1) {
            fos.write(nextByte);
            nextByte = is.read();
         }
         fos.close();
         if (urlc.getResponseCode() != 200) {
            downloaded = false;
         }
         System.out.println(urlc.getResponseCode() + " --- " + urlc.getResponseMessage());
      } catch (SSLHandshakeException e) { //Certificate error (self-signed?)
         //TODO: Show popup message
         System.err.println("SSL Error");
         downloaded = false;
      } catch (Exception e) {
         downloaded = false;
      }
      if (downloaded) {
         deleteBackupFile();
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfiledownloaded"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
      } else {
         restoreFileFromBackup();
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotdownloaded"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      }
   }

   public void exportNotesToServer() {
      boolean uploaded = true;
      try {
         File f = new File(UserSettings.getInstance().getNotesFile());
         FileInputStream fis = new FileInputStream(f);
         String urlString = protocol + "://" + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir() + f.getName();
         URL url = new URL(urlString);
         HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
         urlc.setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode((new String(UserSettings.getInstance().getServerUser() + ":" + UserSettings.getInstance().getServerPasswdString())).getBytes())); 
         urlc.setDoOutput(true);
         urlc.setRequestMethod("PUT");
         OutputStream  os = urlc.getOutputStream();
         int nextByte = fis.read();
         while (nextByte != -1) {
            os.write(nextByte);
            nextByte = fis.read();
         }
         os.close();
         if (urlc.getResponseCode() != 201 && urlc.getResponseCode() != 204) {
            uploaded = false;
         }
      } catch (SSLHandshakeException e) { //Certificate error (self-signed?)
         //TODO: Show popup message
         System.err.println("SSL Error");
         uploaded = false;
      } catch (Exception e) {
         uploaded = false;
      }
      if (uploaded) {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfileuploaded"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotuploaded"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      }
   }
}
