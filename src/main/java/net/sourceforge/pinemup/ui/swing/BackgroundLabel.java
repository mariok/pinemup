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

package net.sourceforge.pinemup.ui.swing;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

import net.sourceforge.pinemup.core.NoteColor;

class BackgroundLabel extends JLabel {
   private static final long serialVersionUID = 1L;

   private NoteWindow parentWindow;
   private NoteColor myColor;

   @Override
   public void paintComponent(Graphics g) {
      setBounds(0, 0, parentWindow.getWidth(), parentWindow.getHeight());
      int h = getHeight();
      int w = getWidth();
      Graphics2D g2 = (Graphics2D)g;

      for (int i = 1; i <= w; i++) {
         int startX = i;
         int startY = 0;
         int endX = startX + 1;
         int endY = startY + h;
         GradientPaint gradient = new GradientPaint(startX, startY, myColor.getColor1(), endX, endY, myColor.getColor2());
         g2.setPaint(gradient);
      }
      g2.fillRect(0, 0, w, h);
   }

   public BackgroundLabel(NoteWindow w, NoteColor color) {
      super();
      parentWindow = w;
      myColor = color;
      setBounds(0, 0, parentWindow.getWidth(), parentWindow.getHeight());
      setFocusable(false);
      setOpaque(false);
   }

   public void setMyColor(NoteColor color) {
      myColor = color;
   }
}
