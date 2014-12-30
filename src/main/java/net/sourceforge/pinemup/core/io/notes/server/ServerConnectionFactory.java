package net.sourceforge.pinemup.core.io.notes.server;

import net.sourceforge.pinemup.core.io.notes.stream.NotesReader;
import net.sourceforge.pinemup.core.io.notes.stream.NotesWriter;
import net.sourceforge.pinemup.core.settings.ConnectionType;

public class ServerConnectionFactory {
   public static ServerConnection createServerConnection(ConnectionType serverType, String password,
         NotesReader notesReader, NotesWriter notesWriter) {
      if (serverType == ConnectionType.WEBDAV) {
         return new WebdavConnection(password, notesReader, notesWriter);
      } else if (serverType == ConnectionType.WEBDAVS) {
         return new WebdavSSLConnection(password, notesReader, notesWriter);
      } else {
         return new FTPConnection(password, notesReader, notesWriter);
      }
   }
}
