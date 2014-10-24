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

package net.sourceforge.pinemup.core.io.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.pinemup.core.io.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.file.NotesFileWriter;
import net.sourceforge.pinemup.core.settings.UserSettings;

class WebdavConnection extends ServerConnection {
   private static final String PROTOCOL_HTTP = "http";

   protected WebdavConnection(String serverPassword, NotesFileReader notesFileReader, NotesFileWriter notesFileWriter) {
      super(serverPassword, notesFileReader, notesFileWriter);
   }

   @Override
   protected String getUrlString(String fileName) {
      return getProtocol() + "://" + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir() + fileName;
   }

   protected String getProtocol() {
      return PROTOCOL_HTTP;
   }

   @Override
   protected URLConnection openURLConnection(URL url) throws IOException {
      HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
      urlc.setRequestMethod("PUT");

      return urlc;
   }

   @Override
   protected boolean isConnectionStateOkAfterDownload(URLConnection urlConnection) throws IOException {
      HttpURLConnection httpUrlConnection = (HttpURLConnection)urlConnection;

      return httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
   }

   @Override
   protected boolean isConnectionStateOkAfterUpload(URLConnection urlConnection) throws IOException {
      HttpURLConnection httpUrlConnection = (HttpURLConnection)urlConnection;

      return httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED
            || httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT;
   }
}
