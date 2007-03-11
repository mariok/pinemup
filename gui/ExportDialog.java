/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
 *
 *
 * pin 'em up is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * pin 'em up is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with pin 'em up; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import logic.*;

public class ExportDialog extends JDialog implements ActionListener {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JButton okButton, cancelButton;
   private JCheckBox[] catBox;
   private JCheckBox allCatsBox;
   
   public ExportDialog() {
      super();
      setTitle("export notes");
      JPanel main = new JPanel(new BorderLayout());
      JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      topPanel.add(new JLabel("choose categories to export:"));
      main.add(topPanel, BorderLayout.NORTH);
      
      // Category Checkboxes
      final int ROWS = 5;
      JPanel checkBoxPanel = new JPanel (new GridLayout(ROWS+1,1));
      String[] cats = MainApp.getUserSettings().getCategoryNames();
      JPanel[] catPanel = new JPanel[ROWS];
      catBox = new JCheckBox[ROWS];
      JPanel allCatsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      allCatsBox = new JCheckBox("(all)");
      allCatsBox.setSelected(true);
      allCatsBox.addActionListener(this);
      allCatsPanel.add(allCatsBox);
      checkBoxPanel.add(allCatsPanel);
      for (int i=0; i<ROWS; i++) {
         catPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
         catBox[i] = new JCheckBox((i+1)+": "+cats[i]);
         catBox[i].setSelected(true);
         catPanel[i].add(catBox[i]);
         checkBoxPanel.add(catPanel[i]);
      }
      main.add(checkBoxPanel, BorderLayout.CENTER);
      
      JPanel buttonPanel = new JPanel();
      okButton = new JButton("OK");
      okButton.addActionListener(this);
      cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(this);
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);
      main.add(buttonPanel, BorderLayout.SOUTH);
      
      setContentPane(main);
      setSize(200,250);
      
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
         boolean[] catChecked = new boolean[5];
         for (int i=0; i<5; i++) {
            catChecked[i] = catBox[i].isSelected();
         }
         NoteIO.ExportNotesToTextFile(MainApp.getMainApp().getNotes(), catChecked);
         setVisible(false);
         dispose();
      } else if (src == allCatsBox) {
        for (int i=0; i<5; i++) {
           catBox[i].setSelected(allCatsBox.isSelected());
        }
      }
   }
   
}
