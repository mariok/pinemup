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

package net.sourceforge.pinemup.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class BackgroundLabel extends JLabel {
   /**
    *
    */
   private static final long serialVersionUID = 1L;

   //color-array
   private static final Object[][] COLORS = {
      {
         "yellow",
         new Color(254, 255, 204),
         new Color(255, 255, 154)
      },
      {
         "green",
         new Color(216, 255, 204),
         new Color(177, 255, 153)
      },
      {
         "blue",
         new Color(204, 213, 255),
         new Color(153, 170, 255)
      },
      {
         "turquoise",
         new Color(225, 247, 250),
         new Color(177, 240, 253)
      },
      {
         "orange",
         new Color(252, 234, 177),
         new Color(252, 221, 126)
      },
      {
         "magenta",
         new Color(255, 204, 230),
         new Color(255, 153, 204)
      },
      {
         "purple",
         new Color(222, 202, 252),
         new Color(192, 154, 255)
      },
      {
         "gray",
         new Color(209, 209, 209),
         new Color(184, 184, 184)
      },
      {
         "red",
         new Color(255, 166, 167),
         new Color(255, 115, 115)
      }
   };

   private NoteWindow parentWindow;
   private byte myColor;

   @Override
   public void paintComponent(Graphics g) {
       setBounds(0, 0, parentWindow.getWidth(), parentWindow.getHeight());
       int h = getHeight();
       int w = getWidth();
       Graphics2D g2 = (Graphics2D)g;

       for (int i=1; i<=w; i++) {
          int startX = i;
          int startY = 0;
          int endX = startX+1;
          int endY = startY+h;
          GradientPaint gradient = new GradientPaint(startX, startY, (Color)COLORS[myColor][1], endX, endY, (Color)COLORS[myColor][2]);
          g2.setPaint(gradient);
       }
       g2.fillRect(0, 0, w, h);
   }

   public BackgroundLabel(NoteWindow w, byte c) {
      super();
      parentWindow = w;
      myColor = c;
      setBounds(0, 0, parentWindow.getWidth(), parentWindow.getHeight());
      setFocusable(false);
      setOpaque(false);
   }

   public void setMyColor(byte c) {
      myColor = c;
   }

   public static String getColorName(byte nr) {
      return I18N.getInstance().getString("color." + (String)COLORS[nr][0]);
   }

   public static int getNumberOfColors() {
      return COLORS.length;
   }

   public static Color getColor(byte nr) {
      return (Color)COLORS[nr][1];
   }

   public static String[] getColorNames() {
      String[] names = new String[COLORS.length];
      for (byte i=0; i<COLORS.length; i++) {
         names[i] = BackgroundLabel.getColorName(i);
      }
      return names;
   }
}
