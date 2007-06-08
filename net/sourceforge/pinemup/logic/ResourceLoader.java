/*
 * pin 'em up
 *
 * pin 'em up is the legal property of it's developers. 
 * Please refer to the COPYRIGHT file distributed with this 
 * source distribution.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package net.sourceforge.pinemup.logic;

import java.io.*;
import java.awt.*;

public class ResourceLoader {
   
   private static InputStream getResourceStream(String pkg, String filename) {
      String name = "/" + pkg.replace('.', '/') + "/" + filename;
      InputStream is = PinEmUp.getMainApp().getClass().getResourceAsStream(name);
      return is;
   }
   
   public static Image loadImage(String pkg, String filename) {
      Image img = null;
      try {
         InputStream is = getResourceStream(pkg, filename);
         
         if (is != null) {
            byte[] buffer = new byte[0];
            byte[] temp = new byte[1024];
            while(true) {
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
}
