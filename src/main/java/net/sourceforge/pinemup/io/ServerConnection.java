/*
 * pin 'em up
 *
 * Copyright (C) 2007-2011 by Mario Ködding
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
   public static final int FTP_CONNECTION = 0;
   public static final int WEBDAV_CONNECTION = 1;
   public static final int WEBDAVS_CONNECTION = 2;
   public static final String[] SERVERTYPE_NAMES = {
      "FTP",
      "WebDAV",
      "WebDAVs"
   };

   public static ServerConnection createServerConnection(int serverType) {
      switch (serverType) {
      case FTP_CONNECTION: return new FTPConnection();
      case WEBDAV_CONNECTION: return new WebdavConnection(false);
      case WEBDAVS_CONNECTION: return new WebdavConnection(true);
      default: return new FTPConnection();
      }
   }

   protected void makeBackupFile() { //to create a backup before downloading from server
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
