package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.gui.I18N;
import net.sourceforge.pinemup.logic.UserSettings;

public class FTPConnection extends ServerConnection {
   public void importNotesFromServer() {
      boolean downloaded = true;
      try {
         File f = new File(UserSettings.getInstance().getNotesFile());
         FileOutputStream fos = new FileOutputStream(f);
         String filename = f.getName();
         String ftpString = "ftp://" + UserSettings.getInstance().getServerUser() + ":"
               + UserSettings.getInstance().getServerPasswdString() + "@" + UserSettings.getInstance().getServerAddress()
               + UserSettings.getInstance().getServerDir() + filename + ";type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         InputStream is = urlc.getInputStream();
         int nextByte = is.read();
         while(nextByte != -1) {
            fos.write(nextByte);
            nextByte = is.read();
         }
         fos.close();
      } catch (Exception e) {
         downloaded = false;
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotdownloaded"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      }
      if (downloaded) {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfiledownloaded"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
      }
   }

   public void exportNotesToServer() {
      boolean uploaded = true;
      try {
         String completeFilename = UserSettings.getInstance().getNotesFile();
         File f = new File(completeFilename);
         String filename = f.getName();
         FileInputStream fis = new FileInputStream(f);
         String ftpString = "ftp://" + UserSettings.getInstance().getServerUser() + ":"
         + UserSettings.getInstance().getServerPasswdString() + "@" + UserSettings.getInstance().getServerAddress()
         + UserSettings.getInstance().getServerDir() + filename + ";type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         OutputStream  os = urlc.getOutputStream();
         
         int nextByte = fis.read();
         while (nextByte != -1) {
            os.write(nextByte);
            nextByte = fis.read();
         }
         os.close();
      } catch (Exception e) {
         uploaded = false;
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotuploaded"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      }
      if (uploaded) {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfileuploaded"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
      }
   }
}
