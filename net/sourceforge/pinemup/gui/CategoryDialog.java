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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.pinemup.logic.*;
import net.sourceforge.pinemup.menus.TrayMenu;

public class CategoryDialog extends JDialog implements ActionListener,DocumentListener,ListSelectionListener,TableModelListener {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JButton closeButton, moveUpButton, moveDownButton, deleteButton, addCatButton;
   
   private CategoryList categories;
   private UserSettings settings;
   private JTable catTable;
   private DefaultTableModel catTableModel;
   private JTextField catNameField;
   
   private int noOfCategories;
   
   public CategoryDialog(CategoryList c, UserSettings s) {
      super();
      setTitle("manage categories");
      categories = c;
      settings = s;
      noOfCategories = categories.getNumberOfCategories();
      Container main = getContentPane();
      main.setLayout(new BorderLayout());

      JPanel buttonPanel = new JPanel();
      closeButton = new JButton("close");
      closeButton.addActionListener(this);
      buttonPanel.add(closeButton);
      main.add(buttonPanel,BorderLayout.SOUTH);
      
      GridBagLayout gbl = new GridBagLayout();
      JPanel centerPanel = new JPanel(gbl);
      main.add(centerPanel,BorderLayout.CENTER);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;

      JPanel addCatPanel = new JPanel();
      catNameField = new JTextField(15);
      catNameField.getDocument().addDocumentListener(this);
      addCatButton = new JButton("add");
      addCatButton.setEnabled(false);
      addCatButton.addActionListener(this);
      addCatPanel.add(catNameField);
      addCatPanel.add(addCatButton);
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 3;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(addCatPanel, gbc);
      centerPanel.add(addCatPanel);
      
      JScrollPane sp = new JScrollPane();
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 3;
      gbc.gridheight = 6;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(sp, gbc);
      centerPanel.add(sp);
      
      JPanel moveUpPanel = new JPanel();
      JPanel moveDownPanel = new JPanel();
      JPanel deletePanel = new JPanel();
      JPanel emptyPanel = new JPanel();
      moveUpButton = new JButton("^");
      moveUpButton.addActionListener(this);
      moveUpButton.setEnabled(false);
      moveDownButton = new JButton("v");
      moveDownButton.addActionListener(this);
      moveDownButton.setEnabled(false);
      deleteButton = new JButton("X");
      deleteButton.addActionListener(this);
      deleteButton.setEnabled(false);
      moveUpPanel.add(moveUpButton);
      moveDownPanel.add(moveDownButton);
      deletePanel.add(deleteButton);
      addCatPanel.add(catNameField);
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridx = 3;
      gbc.gridy = 1;
      gbl.setConstraints(moveUpPanel, gbc);
      centerPanel.add(moveUpPanel);
      gbc.gridx = 3;
      gbc.gridy = 2;
      gbl.setConstraints(moveDownPanel, gbc);
      centerPanel.add(moveDownPanel);
      gbc.gridx = 3;
      gbc.gridy = 3;
      gbl.setConstraints(deletePanel, gbc);
      centerPanel.add(deletePanel);
      gbc.gridx = 3;
      gbc.gridy = 4;
      gbc.gridheight = 3;
      gbl.setConstraints(emptyPanel, gbc);
      centerPanel.add(emptyPanel);
      
      catTable = makeCatTable();
      sp.setViewportView(catTable);
      
      setSize(400,400);
      
      // center on screen
      int screenHeight = (int)getToolkit().getScreenSize().getHeight();
      int screenWidth = (int)getToolkit().getScreenSize().getWidth();
      int x = (screenWidth - getWidth()) / 2;
      int y = (screenHeight - getHeight()) / 2;
      setLocation(x, y);

      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setVisible(true);
   }
   
   private JTable makeCatTable() {
      catTableModel = new DefaultTableModel();
      catTableModel.addColumn("Default");
      catTableModel.addColumn("Name");
      catTableModel.addColumn("Color");
      Object[][] rowData = makeDataArray();
      for (int i=0;i<rowData.length;i++) {
         catTableModel.addRow(rowData[i]);         
      }
      JTable jt = new JTable(catTableModel);
      jt.setColumnSelectionAllowed(false);
      jt.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
      jt.getSelectionModel().addListSelectionListener(this);
      return jt;
   }
   
   private Object[][] makeDataArray() {
      int anz = categories.getNumberOfCategories();
      CategoryList c = categories;
      Object[][] data = new Object[anz][3];
      for (int i = 0; i < anz; i++) {
         if (c.getCategory().isDefaultCategory()) {
            data[i][0] = "X";
         } else {
            data[i][0] = "";
         }
         data[i][1] = c.getCategory().getName();
         data[i][2] = "";
         c = c.getNext();
      }
      return data;
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == closeButton) {
         setVisible(false);
         dispose();
      } else if (src == deleteButton) {
         deleteSelectedCategory();
      } else if (src == moveUpButton) {
         moveSelectedCategory(true);
      } else if (src == moveDownButton) {
         moveSelectedCategory(false);
      } else if (src == addCatButton) {
         addCategory();
      }
   }
   
   private void deleteSelectedCategory() {
      int rowNo = catTable.getSelectedRow();
      Category myCat = categories.getCategoryByNumber(rowNo); 
      boolean confirmed = true;
      if (myCat.getNotes().getNumberOfNotes() > 0) {
         confirmed = JOptionPane.showConfirmDialog(null, "All " + myCat.getNotes().getNumberOfNotes() + " notes in this category will be deleted! Proceed?","Remove category",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
      }
      
      if (confirmed) {
         myCat.getNotes().hideAllNotes();
         categories.remove(myCat);
         catTableModel.removeRow(rowNo);
         if (myCat.isDefaultCategory()) {
            categories.getCategory().setDefault(true);
            catTableModel.setValueAt("X", 0, 0);
         }
         PinEmUp.getMainApp().getTrayIcon().setPopupMenu(new TrayMenu(categories,settings));
         noOfCategories--;
         deleteButton.setEnabled(false);
      }
   }

   private void moveSelectedCategory(boolean moveUp) {
      int rowNo = catTable.getSelectedRow();
      Category myCat = categories.getCategoryByNumber(rowNo);
      if (moveUp) {
         categories.moveCategoryUp(myCat);
         catTableModel.moveRow(rowNo, rowNo, rowNo-1);
         catTable.setRowSelectionInterval(rowNo-1, rowNo-1);
      } else {
         categories.moveCategoryDown(myCat);
         catTableModel.moveRow(rowNo, rowNo, rowNo+1);
         catTable.setRowSelectionInterval(rowNo+1, rowNo+1);
      }
      PinEmUp.getMainApp().getTrayIcon().setPopupMenu(new TrayMenu(categories,settings));
   }
   
   private void addCategory() {
      String catName = catNameField.getText();
      categories.add(new Category(catName,new NoteList(),false,(byte)(0)));
      PinEmUp.getMainApp().getTrayIcon().setPopupMenu(new TrayMenu(categories,settings));
      Object[] rowData = {"",catName,""};
      catTableModel.addRow(rowData);
      noOfCategories++;
      catTable.setRowSelectionInterval(noOfCategories-1, noOfCategories-1);
      catNameField.setText("");
      addCatButton.setEnabled(false);
   }
   
   public void changedUpdate(DocumentEvent arg0) {
      if (arg0.getDocument().getLength() > 0) {
         addCatButton.setEnabled(true);
      } else {
         addCatButton.setEnabled(false);
      }
   }

   public void insertUpdate(DocumentEvent arg0) {
      if (arg0.getDocument().getLength() > 0) {
         addCatButton.setEnabled(true);
      }
   }

   public void removeUpdate(DocumentEvent arg0) {
      if (arg0.getDocument().getLength() == 0) {
         addCatButton.setEnabled(false);
      }
   }

   public void valueChanged(ListSelectionEvent e) {
      int rowNo = catTable.getSelectedRow();
      
      //ENABLE OR DISABLE MOVEDOWN BUTTON
      if (rowNo == noOfCategories-1) {
         moveDownButton.setEnabled(false);
      } else {
         moveDownButton.setEnabled(true);
      }
      
      //ENABLE OR DISABLE MOVEUP BUTTON
      if (rowNo == 0) {
         moveUpButton.setEnabled(false);
      } else {
         moveUpButton.setEnabled(true);
      }
      
      //ENABLE DELETE BUTTON?
      if (noOfCategories > 1) {
         deleteButton.setEnabled(true);
      }
   }

   public void tableChanged(TableModelEvent e) {
      //TODO
   }

}
