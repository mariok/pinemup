package net.sourceforge.pinemup.io;

public abstract class ServerConnection {
   public static final int NUMBER_OF_SERVERTYPES = 1;
   public static final int FTP_CONNECTION = 0;
   protected String type;
   
   public static ServerConnection createServerConnection(int serverType) {
      switch (serverType) {
      case FTP_CONNECTION: return new FTPConnection();
      default: return new FTPConnection();
      }
   }
   
   public String getType() {
      return type;
   }
   
   public abstract void exportNotesToServer();
   public abstract void importNotesFromServer();
}
