package net.sourceforge.pinemup.core.settings;

public enum ConnectionType {
   FTP((short)0, "FTP"),
   WEBDAV((short)1, "WebDAV"),
   WEBDAVS((short)2, "WebDAVs");

   private final short code;
   private final String name;

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
