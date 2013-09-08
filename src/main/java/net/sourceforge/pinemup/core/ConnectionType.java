package net.sourceforge.pinemup.core;

public enum ConnectionType {
   FTP((short)0, "FTP"),
   WEBDAV((short)1, "WebDAV"),
   WEBDAVS((short)2, "WebDAVs");

   private short code;
   private String name;

   private ConnectionType(short code, String name) {
      this.code = code;
      this.name = name;
   }

   public short getCode() {
      return code;
   }

   public String getName() {
      return name;
   }

   public static ConnectionType getConnectionTypeByCode(short code) {
      return ConnectionType.values()[code];
   }

   @Override
   public String toString() {
      return name;
   }
}
