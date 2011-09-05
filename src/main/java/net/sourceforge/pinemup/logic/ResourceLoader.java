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

package net.sourceforge.pinemup.logic;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public final class ResourceLoader {
   private static ResourceLoader instance = new ResourceLoader();

   private static final String IMG_DIR = "img/";
   private static final String SCHEMA_DIR = "xsd/";

   private Image closeIcon1;
   private Image closeIcon2;
   private Image trayIcon;
   private Image scrollImage;

   public static ResourceLoader getInstance() {
      return ResourceLoader.instance;
   }

   private ResourceLoader() {
      closeIcon1 = loadImage("closeicon.png");
      closeIcon2 = loadImage("closeicon2.png");
      trayIcon = loadImage("icon" + getTrayIconSize() + ".png");
      scrollImage = loadImage("scroll.png");
   }

   private long getTrayIconSize() {
      long size = Math.round(SystemTray.getSystemTray().getTrayIconSize().getHeight());
      if ((size < 16)) {
         size = 16;
      } else if (size > 48) {
         size = 48;
      } else if (size % 8 != 0) {
         size = Math.round(size / 8) * 8;
      }
      return size;
   }

   private InputStream getResourceStream(String filename) {
      return getResourceStream(filename, "");
   }

   private InputStream getResourceStream(String filename, String dirname) {
      String name = "/" + dirname + filename;
      InputStream is = getClass().getResourceAsStream(name);
      return is;
   }

   private Image loadImage(String filename) {
      Image img = null;
      try {
         InputStream is = getResourceStream(filename, IMG_DIR);

         if (is != null) {
            byte[] buffer = new byte[0];
            byte[] temp = new byte[1024];
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
         // do nothing
      }

      return img;
   }

   public Image getCloseIcon(int nr) {
      switch(nr) {
      case 1: return closeIcon1;
      case 2: return closeIcon2;
      default: return closeIcon1;
      }
   }

   public Image getTrayIcon() {
      return trayIcon;
   }

   public Image getScrollImage() {
      return scrollImage;
   }

   public String getLicense() {
      String s = "";
      try {
         String filename = "COPYING";
         InputStream is = getResourceStream(filename);
         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         String nextLine = br.readLine();
         while (nextLine != null) {
            s += nextLine + "\r\n";
            nextLine = br.readLine();
         }
         br.close();
      } catch (IOException e) {
         e.printStackTrace();
      }

      return s;
   }

   public URL getSchemaFile(String version) {
      String filename = "notesfile-" + version + ".xsd";
      URL u = getClass().getResource("/" + SCHEMA_DIR +  filename);
      return u;
   }
}
