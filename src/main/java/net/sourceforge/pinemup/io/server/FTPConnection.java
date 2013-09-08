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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.pinemup.core.UserSettings;

class FTPConnection extends ServerConnection {
   protected FTPConnection(String serverPassword) {
      super(serverPassword);
   }

   @Override
   protected String getUrlString(String fileName) {
      return "ftp://" + UserSettings.getInstance().getServerUser() + ":" + serverPassword + "@"
            + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir() + fileName + ";type=i";
   }

   @Override
   protected URLConnection openURLConnection(URL url) throws IOException {
      URLConnection urlc = url.openConnection();
      urlc.setDoOutput(true);

      return urlc;
   }
}
