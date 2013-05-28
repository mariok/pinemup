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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;

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

   public List<Category> importCategoriesFromServer() {
      List<Category> categoriesFromServer = null;

      InputStream is = null;
      try {
         File f = new File(UserSettings.getInstance().getNotesFile());
         setDefaultAuthenticator();
         URL url = new URL(getUrlString(f.getName()));
         URLConnection urlc = url.openConnection();
         is = urlc.getInputStream();
         categoriesFromServer = NotesFileManager.getInstance().readCategoriesFromInputStream(is);
         if (!isConnectionStateOkAfterDownload(urlc)) {
            categoriesFromServer = null;
         }
      } catch (SSLHandshakeException e) {
         // certificate error (self-signed?)
         UserSettings
               .getInstance()
               .getUserInputRetriever()
               .showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.sslcertificateerror"));
      } catch (Exception e) {
         categoriesFromServer = null;
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }

      return categoriesFromServer;
   }

   public boolean exportNotesToServer(List<Category> categories) {
      boolean uploaded = true;

      OutputStream os = null;
      try {
         File f = new File(UserSettings.getInstance().getNotesFile());

         setDefaultAuthenticator();
         URL url = new URL(getUrlString(f.getName()));
         URLConnection urlc = openURLConnection(url);
         urlc.setDoOutput(true);
         os = urlc.getOutputStream();
         boolean writtenSuccessfully = NotesFileManager.getInstance().writeCategoriesToOutputStream(categories, os);

         if (!isConnectionStateOkAfterUpload(urlc) || !writtenSuccessfully) {
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
      }

      return uploaded;
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
