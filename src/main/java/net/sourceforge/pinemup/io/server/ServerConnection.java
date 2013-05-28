/*
 * pin 'em up
 *
 * Copyright (C) 2007-2013 by Mario Ködding
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
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.SSLHandshakeException;

import net.sourceforge.pinemup.core.I18N;
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
         return new WebdavConnection();
      } else if (serverType == ConnectionType.WEBDAVS) {
         return new WebdavSSLConnection();
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

   public void importNotesFromServer() {
      boolean downloaded = true;

      FileOutputStream fos = null;
      InputStream is = null;
      try {
         makeBackupFile();
         File f = new File(UserSettings.getInstance().getNotesFile());
         fos = new FileOutputStream(f);
         setDefaultAuthenticator();
         URL url = new URL(getUrlString(f.getName()));
         URLConnection urlc = url.openConnection();
         is = urlc.getInputStream();
         int nextByte = is.read();
         while (nextByte != -1) {
            fos.write(nextByte);
            nextByte = is.read();
         }
         if (!isConnectionStateOkAfterDownload(urlc)) {
            downloaded = false;
         }
      } catch (SSLHandshakeException e) { // Certificate error (self-signed?)
         UserSettings
               .getInstance()
               .getUserInputRetriever()
               .showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.sslcertificateerror"));
         downloaded = false;
      } catch (Exception e) {
         downloaded = false;
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if (fos != null) {
            try {
               fos.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
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
               .showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.notesfilenotdownloaded"));
      }
   }

   public void exportNotesToServer() {
      boolean uploaded = true;

      FileInputStream fis = null;
      OutputStream os = null;
      try {
         File f = new File(UserSettings.getInstance().getNotesFile());
         fis = new FileInputStream(f);

         setDefaultAuthenticator();
         URL url = new URL(getUrlString(f.getName()));
         URLConnection urlc = openURLConnection(url);
         urlc.setDoOutput(true);
         os = urlc.getOutputStream();
         int nextByte = fis.read();
         while (nextByte != -1) {
            os.write(nextByte);
            nextByte = fis.read();
         }
         if (!isConnectionStateOkAfterUpload(urlc)) {
            uploaded = false;
         }
      } catch (SSLHandshakeException e) {
         // certificate error (self-signed?)
         UserSettings
               .getInstance()
               .getUserInputRetriever()
               .showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.sslcertificateerror"));
         uploaded = false;
      } catch (Exception e) {
         uploaded = false;
      } finally {
         if (os != null) {
            try {
               os.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if (fis != null) {
            try {
               fis.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      if (uploaded) {
         UserSettings.getInstance().getUserInputRetriever()
               .showInfoMessageToUser(I18N.getInstance().getString("info.title"), I18N.getInstance().getString("info.notesfileuploaded"));
      } else {
         UserSettings
               .getInstance()
               .getUserInputRetriever()
               .showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.notesfilenotuploaded"));
      }
   }

   private void setDefaultAuthenticator() {
      Authenticator.setDefault(new Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(UserSettings.getInstance().getServerUser(), UserSettings.getInstance().getServerPasswd()
                  .toCharArray());
         }
      });
   }

   protected boolean isConnectionStateOkAfterDownload(URLConnection urlConnection) throws IOException {
      return true;
   }

   protected boolean isConnectionStateOkAfterUpload(URLConnection urlConnection) throws IOException {
      return true;
   }

   protected abstract String getUrlString(String fileName);

   protected abstract URLConnection openURLConnection(URL url) throws IOException;
}
