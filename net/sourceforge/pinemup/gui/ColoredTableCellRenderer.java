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
      
      if (column == 2) {
         Color myColor = BackgroundLabel.getColor(Byte.parseByte((String)value));
         if (hasFocus || isSelected) {
            myCell.setBackground(selectionColor);
            myCell.setForeground(selectionColor);
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
      return myCell;
   }
}
