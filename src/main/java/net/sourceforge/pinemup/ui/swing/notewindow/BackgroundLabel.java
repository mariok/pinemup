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

package net.sourceforge.pinemup.ui.swing.notewindow;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

import net.sourceforge.pinemup.core.model.NoteColor;

class BackgroundLabel extends JLabel {
   private static final long serialVersionUID = -8780099589968016013L;

   private NoteColor myColor;

   @Override
   public void paintComponent(Graphics g) {
      int width = getWidth();
      int height = getHeight();

      Graphics2D g2 = (Graphics2D)g;

      for (int i = 1; i <= width; i++) {
         int startX = i;
         int startY = 0;
         int endX = startX + 1;
         int endY = startY + height;
         GradientPaint gradient = new GradientPaint(startX, startY, myColor.getColor1(), endX, endY, myColor.getColor2());
         g2.setPaint(gradient);
      }
      g2.fillRect(0, 0, width, height);
   }

   public BackgroundLabel(NoteColor color, int width, int height) {
      super();
      myColor = color;
      updateSize(width, height);
      setFocusable(false);
      setOpaque(false);
   }

   public void setMyColor(NoteColor color) {
      myColor = color;
   }

   public void updateSize(int width, int height) {
      setBounds(0, 0, width, height);
   }
}
