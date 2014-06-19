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

package net.sourceforge.pinemup.core.io;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.pinemup.core.settings.CommonConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ResourceLoader {
   private static final String IMG_DIR = "img/";
   private static final String SCHEMA_DIR = "xsd/";
   private static final int TEMP_BUFFER_SIZE = 1024;

   private static final int TRAYICON_SIZE_STEP = 8;
   private static final int TRAYICON_MIN_SIZE = 16;
   private static final int TRAYICON_MAX_SIZE = 48;

   private static final String TEXT_RESOURCES_ENCODING = "UTF-8";

   private static final Logger LOG = LoggerFactory.getLogger(ResourceLoader.class);

   private Image closeIcon1;
   private Image closeIcon2;
   private Image trayIcon;
   private Image scrollImage;

   private static class Holder {
      private static final ResourceLoader INSTANCE = new ResourceLoader();
   }

   public static ResourceLoader getInstance() {
      return Holder.INSTANCE;
   }

   private ResourceLoader() {
      closeIcon1 = loadImage("closeicon.png");
      closeIcon2 = loadImage("closeicon2.png");
      trayIcon = loadImage("icon" + getTrayIconSize() + ".png");
      scrollImage = loadImage("scroll.png");
   }

   private long getTrayIconSize() {
      long size = Math.round(SystemTray.getSystemTray().getTrayIconSize().getHeight());
      if ((size < TRAYICON_MIN_SIZE)) {
         size = TRAYICON_MIN_SIZE;
      } else if (size > TRAYICON_MAX_SIZE) {
         size = TRAYICON_MAX_SIZE;
      } else if (size % TRAYICON_SIZE_STEP != 0) {
         size = (size / TRAYICON_SIZE_STEP) * TRAYICON_SIZE_STEP;
      }
      return size;
   }

   private InputStream getResourceStream(String filename) {
      return getResourceStream(filename, "");
   }

   private InputStream getResourceStream(String filename, String dirname) {
      String name = "/" + dirname + filename;
      return getClass().getResourceAsStream(name);
   }

   private Image loadImage(String filename) {
      Image img = null;

      try {
         InputStream is = getResourceStream(filename, IMG_DIR);

         if (is != null) {
            byte[] buffer = new byte[0];
            byte[] temp = new byte[TEMP_BUFFER_SIZE];
            while (true) {
               int len = is.read(temp);
               if (len <= 0) {
                  break;
               }
               byte[] newBuffer = new byte[buffer.length + len];
               System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
               System.arraycopy(temp, 0, newBuffer, buffer.length, len);
               buffer = newBuffer;
            }
            img = Toolkit.getDefaultToolkit().createImage(buffer);
            is.close();
         }
      } catch (IOException e) {
         LOG.error("Error while loading image " + filename + ".", e);
      }

      return img;
   }

   public Image getCloseIcon(int nr) {
      switch (nr) {
      case 1:
         return closeIcon1;
      case 2:
         return closeIcon2;
      default:
         return closeIcon1;
      }
   }

   public Image getTrayIcon() {
      return trayIcon;
   }

   public Image getScrollImage() {
      return scrollImage;
   }

   public String getLicense() {
      return getFileAsString("COPYING", null);
   }

   public String getAboutPage() {
      Map<String, String> parameters = new HashMap<>();
      parameters.put("VERSION", CommonConfiguration.getApplicationVersion());
      parameters.put("CURRENTYEAR", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
      return getFileAsString("about.html", parameters);
   }

   public String getAuthorsPage() {
      return getFileAsString("authors.html", null);
   }

   private String getFileAsString(String filename, Map<String, String> parameters) {
      StringBuilder s = new StringBuilder();

      try {
         InputStream is = getResourceStream(filename);
         BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName(TEXT_RESOURCES_ENCODING).newDecoder()));
         String nextLine = br.readLine();

         while (nextLine != null) {
            nextLine = replacePlaceholdersWithParameters(nextLine, parameters);
            s.append(nextLine);
            s.append("\r\n");
            nextLine = br.readLine();
         }
         br.close();
      } catch (IOException e) {
         LOG.error("Error while loading file " + filename + ".", e);
      }

      return s.toString();
   }

   private String replacePlaceholdersWithParameters(String text, Map<String, String> parameters) {
      String newText = text;

      if (parameters != null) {
         for (Entry<String, String> param : parameters.entrySet()) {
            newText = newText.replaceAll("\\$\\{" + param.getKey() + "\\}", param.getValue());
         }
      }

      return newText;
   }

   public URL getSchemaFile(String version) {
      String filename = "notesfile-" + version + ".xsd";
      return getClass().getResource("/" + SCHEMA_DIR + filename);
   }
}
