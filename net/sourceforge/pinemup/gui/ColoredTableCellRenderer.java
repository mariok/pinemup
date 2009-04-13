/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2009 by Mario Ködding
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
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
 
public class ColoredTableCellRenderer implements TableCellRenderer {
   private Color selectionColor = new Color(160, 160, 255);
   
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JLabel myCell = new JLabel((String)value);
      myCell.setOpaque(true);
      Border b = BorderFactory.createEmptyBorder(1, 1, 1, 1);
      myCell.setBorder(b);
      myCell.setFont(table.getFont());
      myCell.setForeground(table.getForeground());
      myCell.setBackground(table.getBackground());
      
      column = table.convertColumnIndexToModel(column);
      
      //set colors
      if (column == 2) {
         Color myColor = BackgroundLabel.getColor(Byte.parseByte((String)value));
         if (hasFocus || isSelected) {
            myCell.setBackground(myColor);
            myCell.setBorder(BorderFactory.createLineBorder(selectionColor, 3));
            myCell.setForeground(myColor);
         } else {
            myCell.setBackground(myColor);
            myCell.setForeground(myColor);
         }
      } else {
         if (hasFocus || isSelected) { 
               myCell.setBackground(selectionColor);
               myCell.setForeground(Color.white);
         }
      }
      
      //change alignment for first column
      if (column == 0) {
         myCell.setHorizontalAlignment(JLabel.CENTER);
      }
      return myCell;
   }
}
