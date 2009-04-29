package net.sourceforge.pinemup.io;

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
   
   public abstract void exportNotesToServer();
   public abstract void importNotesFromServer();
}
