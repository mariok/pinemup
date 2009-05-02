package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.sourceforge.pinemup.logic.UserSettings;

public abstract class ServerConnection {
   public static final int FTP_CONNECTION = 0;
   public static final int WEBDAV_CONNECTION = 1;
   public static final int WEBDAVS_CONNECTION = 2;
   public static final String[] SERVERTYPE_NAMES = {
      "FTP",
      "WebDAV",
      "WebDAVs"
   };
   protected String type;
   
   public static ServerConnection createServerConnection(int serverType) {
      switch (serverType) {
      case FTP_CONNECTION: return new FTPConnection();
      case WEBDAV_CONNECTION: return new WebdavConnection(false);
      case WEBDAVS_CONNECTION: return new WebdavConnection(true);
      default: return new FTPConnection();
      }
   }
   
   public String getType() {
      return type;
   }
   
   protected void makeBackupFile() { //to create a backup before downloading from server
      File f = new File(UserSettings.getInstance().getNotesFile());
      File bf = new File(UserSettings.getInstance().getNotesFile() + ".bak");

      try {
         FileInputStream fis = new FileInputStream(f);
         FileOutputStream fos = new FileOutputStream(bf);
         int nextByte;
         while((nextByte = fis.read()) != -1) {
            fos.write(nextByte);
         }
         fis.close();
         fos.close();
      } catch (Exception e) {
         //do nothing
      }
   }
   
   protected void deleteBackupFile() { //to delete backup file after successful download
      File bf = new File(UserSettings.getInstance().getNotesFile() + ".bak");
      if (bf.exists()) {
         bf.delete();
      }
   }
   
   protected void restoreFileFromBackup() { //to restore original file after download failed
      File f = new File(UserSettings.getInstance().getNotesFile());
      File bf = new File(UserSettings.getInstance().getNotesFile() + ".bak");
      if (f.exists()) {
         f.delete();
      }
      bf.renameTo(f);
   }
   
   
   public abstract void exportNotesToServer();
   public abstract void importNotesFromServer();
}
