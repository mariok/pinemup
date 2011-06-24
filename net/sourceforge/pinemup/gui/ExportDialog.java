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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sourceforge.pinemup.io.NoteIO;
import net.sourceforge.pinemup.logic.Category;
import net.sourceforge.pinemup.logic.CategoryManager;

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
      setTitle(I18N.getInstance().getString("exportdialog.title"));
      JScrollPane sp = new JScrollPane();
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      JPanel main = new JPanel(new BorderLayout());
      JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      topPanel.add(new JLabel(I18N.getInstance().getString("exportdialog.toplabel")));
      main.add(topPanel, BorderLayout.NORTH);
      main.add(sp, BorderLayout.CENTER);

      // Category Checkboxes
      int rows = CategoryManager.getInstance().getNumberOfCategories();
      JPanel checkBoxPanel = new JPanel (new GridLayout(rows+2, 1));
      String[] cats = CategoryManager.getInstance().getCategoryNames();
      JPanel[] catPanel = new JPanel[rows];
      catBox = new JCheckBox[rows];
      JPanel allCatsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      allCatsBox = new JCheckBox("(" + I18N.getInstance().getString("exportdialog.allcategoriescheckbox") + ")");
      allCatsBox.setSelected(true);
      allCatsBox.addActionListener(this);
      allCatsPanel.add(allCatsBox);
      checkBoxPanel.add(allCatsPanel);
      for (int i=0; i<rows; i++) {
         catPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
         catBox[i] = new JCheckBox((i+1) + ": " + cats[i]);
         catBox[i].setSelected(true);
         catBox[i].addActionListener(this);
         catPanel[i].add(catBox[i]);
         checkBoxPanel.add(catPanel[i]);
      }
      sp.setViewportView(checkBoxPanel);

      JPanel buttonPanel = new JPanel();
      okButton = new JButton(I18N.getInstance().getString("okbutton"));
      okButton.addActionListener(this);
      cancelButton = new JButton(I18N.getInstance().getString("cancelbutton"));
      cancelButton.addActionListener(this);
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);
      main.add(buttonPanel, BorderLayout.SOUTH);

      setContentPane(main);
      setSize(250, 300);

      // center on screen
      int screenHeight = (int)getToolkit().getScreenSize().getHeight();
      int screenWidth = (int)getToolkit().getScreenSize().getWidth();
      int x = (screenWidth - getWidth()) / 2;
      int y = (screenHeight - getHeight()) / 2;
      setLocation(x, y);

      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == cancelButton) {
         setVisible(false);
         dispose();
      } else if (src == okButton) {
         List<Category> catsToExport = new LinkedList<Category>();
         for (int i=0; i<catBox.length; i++) {
            if (catBox[i].isSelected()) {
               catsToExport.add(CategoryManager.getInstance().getCategoryByNumber(i));
            }
         }

         NoteIO.exportCategoriesToTextFile(catsToExport.listIterator());
         setVisible(false);
         dispose();
      } else if (src == allCatsBox) {
        for (int i=0; i<catBox.length; i++) {
           catBox[i].setSelected(allCatsBox.isSelected());
        }
      } else {
         for (int i=0; i<catBox.length; i++) {
            if (src == catBox[i] && allCatsBox.isSelected() && !catBox[i].isSelected()) {
               allCatsBox.setSelected(false);
            }
         }
      }
   }

}
