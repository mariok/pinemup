/*
 * pin 'em up
 *
 * Copyright (C) 2007-2012 by Mario Ködding
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.sourceforge.pinemup.core.UserSettings;

public abstract class ServerConnection {
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

   public static ServerConnection createServerConnection(ConnectionType serverType) {
      if (serverType == ConnectionType.WEBDAV) {
         return new WebdavConnection(false);
      } else if (serverType == ConnectionType.WEBDAVS) {
         return new WebdavConnection(true);
      } else {
         return new FTPConnection();
      }
   }

   protected void makeBackupFile() {
      File f = new File(UserSettings.getInstance().getNotesFile());
      File bf = new File(UserSettings.getInstance().getNotesFile() + ".bak");

      try {
         FileInputStream fis = new FileInputStream(f);
         FileOutputStream fos = new FileOutputStream(bf);
         int nextByte;
         while ((nextByte = fis.read()) != -1) {
            fos.write(nextByte);
         }
         fis.close();
         fos.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   protected void deleteBackupFile() {
      File bf = new File(UserSettings.getInstance().getNotesFile() + ".bak");
      if (bf.exists()) {
         bf.delete();
      }
   }

   protected void restoreFileFromBackup() {
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
