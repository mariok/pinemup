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

package net.sourceforge.pinemup.core.io.notes.server;

import net.sourceforge.pinemup.core.io.notes.stream.NotesReader;
import net.sourceforge.pinemup.core.io.notes.stream.NotesWriter;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.core.settings.UserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public abstract class ServerConnection {
   private static final Logger LOG = LoggerFactory.getLogger(ServerConnection.class);

   private final NotesReader notesReader;
   private final NotesWriter notesWriter;

   private final String serverPassword;

   protected ServerConnection(String serverPassword, NotesReader notesReader, NotesWriter notesWriter) {
      super();
      this.serverPassword = serverPassword;
      this.notesReader = notesReader;
      this.notesWriter = notesWriter;
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
         categoriesFromServer = notesReader.readCategoriesFromInputStream(is);
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
         boolean writtenSuccessfully = notesWriter.writeCategoriesToOutputStream(categories, os);

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

   protected String getServerPassword() {
      return serverPassword;
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
