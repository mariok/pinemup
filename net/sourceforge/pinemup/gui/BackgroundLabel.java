/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
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

import java.awt.*;
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
         new Color(255, 255, 185),
         new Color(255, 235, 70)
      },
      {
         "green",
         new Color(100, 255, 100),
         new Color(0, 255, 0)         
      },
      {
         "red",
         new Color(255, 96, 96),
         new Color(255, 11, 11)         
      },
      {
         "blue",
         new Color(154, 154, 255),
         new Color(93, 93, 208)
         
      }
   };
   
   private NoteWindow parentWindow;
   private byte myColor;
   
   public void paintComponent(Graphics g) {
       setBounds(0,0,parentWindow.getWidth(),parentWindow.getHeight());
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
       g2.fillRect(0,0,w,h);
   }
   
   public BackgroundLabel(NoteWindow w, byte c) {
      super();
      parentWindow = w;
      myColor = c;
      setBounds(0,0,parentWindow.getWidth(),parentWindow.getHeight());
      setFocusable(false);
      setOpaque(false);
   }
   
   public void setMyColor(byte c) {
      myColor = c;
   }
   
   public static String getColorName(byte nr) {
      return (String)COLORS[nr][0];
   }
   
   public static int getNumberOfColors() {
      return COLORS.length;
   }
   
   public static Color getColor(byte nr) {
      return (Color)COLORS[nr][1];
   }
   
   public static String[] getColorNames() {
      String[] names = new String[COLORS.length];
      for (int i=0;i<COLORS.length;i++) {
         names[i] = (String)COLORS[i][0];
      }
      return names;
   }
}
