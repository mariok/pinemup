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

import javax.xml.stream.XMLStreamException;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.ConnectionType;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServerConnection {
   private static final Logger LOG = LoggerFactory.getLogger(ServerConnection.class);

   protected String serverPassword;

   public static ServerConnection createServerConnection(ConnectionType serverType, String password) {
      if (serverType == ConnectionType.WEBDAV) {
         return new WebdavConnection(password);
      } else if (serverType == ConnectionType.WEBDAVS) {
         return new WebdavSSLConnection(password);
      } else {
         return new FTPConnection(password);
      }
   }

   protected ServerConnection(String serverPassword) {
      super();
      this.serverPassword = serverPassword;
   }

   public List<Category> importCategoriesFromServer() throws IOException {
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
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               LOG.error("Error during attempt to close stream.", e);
            }
         }
      }

      return categoriesFromServer;
   }

   public boolean exportNotesToServer(List<Category> categories) throws IOException, XMLStreamException {
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
      } finally {
         if (os != null) {
            try {
               os.close();
            } catch (IOException e) {
               LOG.error("Error during attempt to close stream.", e);
            }
         }
      }

      return uploaded;
   }

   private void setDefaultAuthenticator() {
      Authenticator.setDefault(new Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(UserSettings.getInstance().getServerUser(), serverPassword.toCharArray());
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
