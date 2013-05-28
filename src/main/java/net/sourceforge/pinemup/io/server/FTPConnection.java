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

package net.sourceforge.pinemup.io.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;

class FTPConnection extends ServerConnection {

   @Override
   public void importNotesFromServer() {
      boolean downloaded = true;

      FileOutputStream fos = null;
      InputStream is = null;
      try {
         makeBackupFile();
         File f = new File(UserSettings.getInstance().getNotesFile());
         fos = new FileOutputStream(f);
         String filename = f.getName();
         String ftpString = "ftp://" + UserSettings.getInstance().getServerUser() + ":" + UserSettings.getInstance().getServerPasswd()
               + "@" + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir() + filename + ";type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         is = urlc.getInputStream();
         int nextByte = is.read();
         while (nextByte != -1) {
            fos.write(nextByte);
            nextByte = is.read();
         }
      } catch (IOException e) {
         downloaded = false;
      } finally {
         try {
            if (fos != null) {
               fos.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
         try {
            if (is != null) {
               is.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      if (downloaded) {
         deleteBackupFile();
         UserSettings.getInstance().getUserInputRetriever()
               .showInfoMessageToUser(I18N.getInstance().getString("info.title"), I18N.getInstance().getString("info.notesfiledownloaded"));
      } else {
         restoreFileFromBackup();
         UserSettings
               .getInstance()
               .getUserInputRetriever()
               .showInfoMessageToUser(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.notesfilenotdownloaded"));
      }
   }

   @Override
   public void exportNotesToServer() {
      boolean uploaded = true;
      String completeFilename = UserSettings.getInstance().getNotesFile();
      File f = new File(completeFilename);
      String filename = f.getName();
      String ftpString = "ftp://" + UserSettings.getInstance().getServerUser() + ":" + UserSettings.getInstance().getServerPasswd() + "@"
            + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir() + filename + ";type=i";

      FileInputStream fis = null;
      OutputStream os = null;
      try {
         fis = new FileInputStream(f);
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         os = urlc.getOutputStream();

         int nextByte = fis.read();
         while (nextByte != -1) {
            os.write(nextByte);
            nextByte = fis.read();
         }

      } catch (IOException e) {
         uploaded = false;
         UserSettings
               .getInstance()
               .getUserInputRetriever()
               .showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.notesfilenotuploaded"));
      } finally {
         try {
            if (fis != null) {
               fis.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
         try {
            if (os != null) {
               os.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      if (uploaded) {
         UserSettings.getInstance().getUserInputRetriever()
               .showInfoMessageToUser(I18N.getInstance().getString("info.title"), I18N.getInstance().getString("info.notesfileuploaded"));
      }
   }
}
