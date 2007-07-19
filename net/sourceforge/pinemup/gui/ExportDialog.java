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
import java.awt.event.*;

import javax.swing.*;

import net.sourceforge.pinemup.logic.*;

public class ExportDialog extends JDialog implements ActionListener {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JButton okButton, cancelButton;
   private JCheckBox[] catBox;
   private JCheckBox allCatsBox;
   
   private CategoryList categories;
   
   public ExportDialog(CategoryList c) {
      super();
      setTitle("export notes");
      categories = c;
      JScrollPane sp = new JScrollPane();
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      JPanel main = new JPanel(new BorderLayout());
      JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      topPanel.add(new JLabel("choose categories to export:"));
      main.add(topPanel, BorderLayout.NORTH);
      main.add(sp, BorderLayout.CENTER);
            
      // Category Checkboxes
      int rows = c.getNumberOfCategories();
      JPanel checkBoxPanel = new JPanel (new GridLayout(rows+2,1));
      String[] cats = c.getNames();
      JPanel[] catPanel = new JPanel[rows];
      catBox = new JCheckBox[rows];
      JPanel allCatsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      allCatsBox = new JCheckBox("(all)");
      allCatsBox.setSelected(true);
      allCatsBox.addActionListener(this);
      allCatsPanel.add(allCatsBox);
      checkBoxPanel.add(allCatsPanel);
      for (int i=0; i<rows; i++) {
         catPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
         catBox[i] = new JCheckBox((i+1)+": "+cats[i]);
         catBox[i].setSelected(true);
         catPanel[i].add(catBox[i]);
         checkBoxPanel.add(catPanel[i]);
      }
      sp.setViewportView(checkBoxPanel);
      
      JPanel buttonPanel = new JPanel();
      okButton = new JButton("OK");
      okButton.addActionListener(this);
      cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(this);
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);
      main.add(buttonPanel, BorderLayout.SOUTH);
      
      setContentPane(main);
      setSize(250,300);
      
      // center on screen
      int screenHeight = (int)getToolkit().getScreenSize().getHeight();
      int screenWidth = (int)getToolkit().getScreenSize().getWidth();
      int x = (screenWidth - getWidth()) / 2;
      int y = (screenHeight - getHeight()) / 2;
      setLocation(x, y);

      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setVisible(true);
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == cancelButton) {
         setVisible(false);
         dispose();
      } else if (src == okButton) {
         CategoryList c = categories;
         CategoryList catsToExport = new CategoryList();
         for (int i=0; i<catBox.length; i++) {
            if(catBox[i].isSelected()) {
               catsToExport.add(c.getCategory());
            }
            c = c.getNext();
         }
         
         NoteIO.exportCategoriesToTextFile(catsToExport);
         setVisible(false);
         dispose();
      } else if (src == allCatsBox) {
        for (int i=0; i<catBox.length; i++) {
           catBox[i].setSelected(allCatsBox.isSelected());
        }
      }
   }
   
}
