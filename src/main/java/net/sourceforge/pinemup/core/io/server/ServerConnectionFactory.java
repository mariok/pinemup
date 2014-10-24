package net.sourceforge.pinemup.core.io.server;

import net.sourceforge.pinemup.core.io.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.file.NotesFileWriter;
import net.sourceforge.pinemup.core.settings.ConnectionType;

public class ServerConnectionFactory {
   public static ServerConnection createServerConnection(ConnectionType serverType, String password,
         NotesFileReader notesFileReader, NotesFileWriter notesFileWriter) {
      if (serverType == ConnectionType.WEBDAV) {
         return new WebdavConnection(password, notesFileReader, notesFileWriter);
      } else if (serverType == ConnectionType.WEBDAVS) {
         return new WebdavSSLConnection(password, notesFileReader, notesFileWriter);
      } else {
         return new FTPConnection(password, notesFileReader, notesFileWriter);
      }
   }
}
