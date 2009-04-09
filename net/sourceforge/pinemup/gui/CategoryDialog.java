/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2008 by Mario Koedding
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
import java.util.ListIterator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.pinemup.logic.*;
import net.sourceforge.pinemup.menus.TrayMenu;

public class CategoryDialog extends JDialog implements ActionListener,DocumentListener,ListSelectionListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JButton closeButton, moveUpButton, moveDownButton, deleteButton, addButton;
   
   private CategoryManager categories;
   private JTable catTable;
   private DefaultTableModel catTableModel;
   private JTextField catNameField;
   private JCheckBox defaultBox;
   private JComboBox colorBox;
   
   private int noOfCategories;
   private int defCat;
   private int selectedRow;
   private Category selectedCat;
   
   private boolean trackChanges;
   
   public CategoryDialog(CategoryManager c) {
      super();
      setTitle("manage categories");
      categories = c;
      trackChanges = false;
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

      JScrollPane sp = new JScrollPane();
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      catTable = makeCatTable();
      sp.setViewportView(catTable);
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 4;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(sp, gbc);
      centerPanel.add(sp);
      
      //create items
      JPanel catEditPanel = makeCatEditPanel();
      JPanel moveUpPanel = new JPanel();
      JPanel moveDownPanel = new JPanel();
      JPanel deletePanel = new JPanel();
      JPanel emptyPanel = new JPanel();
      JPanel addPanel = new JPanel();
      JPanel emptyPanel2 = new JPanel();
      moveUpButton = new JButton("^");
      moveUpButton.addActionListener(this);
      moveUpButton.setEnabled(false);
      moveDownButton = new JButton("v");
      moveDownButton.addActionListener(this);
      moveDownButton.setEnabled(false);
      deleteButton = new JButton("X");
      deleteButton.addActionListener(this);
      deleteButton.setEnabled(false);
      addButton = new JButton("+");
      addButton.addActionListener(this);
      addButton.setEnabled(true);
      moveUpPanel.add(moveUpButton);
      moveDownPanel.add(moveDownButton);
      deletePanel.add(deleteButton);
      addPanel.add(addButton);
      
      //place items on panel
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(moveUpPanel, gbc);
      centerPanel.add(moveUpPanel);
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbl.setConstraints(moveDownPanel, gbc);
      centerPanel.add(moveDownPanel);
      gbc.gridx = 1;
      gbc.gridy = 2;
      gbl.setConstraints(deletePanel, gbc);
      centerPanel.add(deletePanel);
      gbc.gridx = 1;
      gbc.gridy = 3;
      gbc.weighty = 100;
      gbl.setConstraints(emptyPanel, gbc);
      centerPanel.add(emptyPanel);
      gbc.gridx = 1;
      gbc.gridy = 4;
      gbc.weighty = 0;
      gbl.setConstraints(addPanel, gbc);
      centerPanel.add(addPanel);
      gbc.gridx = 1;
      gbc.gridy = 5;
      gbc.gridheight = 1;
      gbl.setConstraints(emptyPanel2, gbc);
      centerPanel.add(emptyPanel2);
      gbc.gridx = 0;
      gbc.gridy = 4;
      gbc.gridheight = 2;
      gbl.setConstraints(catEditPanel, gbc);
      centerPanel.add(catEditPanel);
  
      setSize(500,400);
      
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
      catTableModel = new DefaultTableModel() {
         /**
          * 
          */
         private static final long serialVersionUID = 1L;
         
         // prevent all cells from being edited
         public boolean isCellEditable(int row,int column) {
           return false;
         }
      };
      
      String[] columnNames = {"Default","Name","Color"};
      catTableModel.setColumnIdentifiers(columnNames);
      Object[][] rowData = makeDataArray();
      for (int i=0;i<rowData.length;i++) {
         catTableModel.addRow(rowData[i]);
      }
      
      JTable jt = new JTable(catTableModel);
      jt.setColumnSelectionAllowed(false);
      jt.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
      jt.getSelectionModel().addListSelectionListener(this);
      jt.setDefaultRenderer(Object.class, new ColoredTableCellRenderer());
      jt.getColumnModel().getColumn(0).setPreferredWidth(70);
      jt.getColumnModel().getColumn(1).setPreferredWidth(400);
      jt.getColumnModel().getColumn(2).setPreferredWidth(70);
      return jt;
   }
   
   private Object[][] makeDataArray() {
      int anz = categories.getNumberOfCategories();
      Object[][] data = new Object[anz][3];
      ListIterator<Category> l = categories.getListIterator();
      Category tc;
      int index;
      while (l.hasNext()) {
         index = l.nextIndex();
         tc = l.next();
         if (tc.isDefaultCategory()) {
            data[index][0] = "!";
            defCat = index;
         } else {
            data[index][0] = "";
         }
         data[index][1] = tc.getName();
         data[index][2] = String.valueOf(tc.getDefaultNoteColor());
      }
      return data;
   }
   
   private JPanel makeCatEditPanel() {
      JPanel p = new JPanel();
      catNameField = new JTextField(15);
      catNameField.getDocument().addDocumentListener(this);
      catNameField.setEnabled(false);
      p.add(catNameField);
      defaultBox = new JCheckBox("Default Category");
      defaultBox.addActionListener(this);
      defaultBox.setEnabled(false);
      p.add(defaultBox);
      
      DefaultListCellRenderer cr = new DefaultListCellRenderer() {
         /**
          * 
          */
         private static final long serialVersionUID = 1L;

         public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected,hasFocus);
            if (index>=0) {
               c.setBackground(BackgroundLabel.getColor((byte)index));
            }
            return c;
         }
      };
      colorBox = new JComboBox(BackgroundLabel.getColorNames());
      colorBox.setRenderer(cr);
      colorBox.addActionListener(this);
      colorBox.setEnabled(false);
      
      p.add(colorBox);
            
      p.setBorder(new TitledBorder("edit category"));
            
      return p;
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
      } else if (src == addButton) {
         addCategory();
      } else if (src == defaultBox) {
         makeSelectedCategoryDefault();
         defaultBox.setEnabled(false);
      } else if (src == colorBox) {
         updateCatColor();
      }
   }
   
   private void deleteSelectedCategory() {
      boolean confirmed = true;
      if (selectedCat.getNumberOfNotes() > 0) {
         confirmed = JOptionPane.showConfirmDialog(null, "All " + selectedCat.getNumberOfNotes() + " notes in this category will be deleted! Proceed?","Remove category",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
      }
      
      if (confirmed) {
         boolean isDef = selectedCat.isDefaultCategory();
         selectedCat.hideAllNotes();
         categories.removeCategory(selectedCat);
         catTableModel.removeRow(selectedRow);
         if (isDef) {
            categories.getListIterator().next().setDefault(true); //set first category as default
            catTableModel.setValueAt("!", 0, 0);
            defCat = 0;
         }
         PinEmUp.getMainApp().getTrayIcon().setPopupMenu(new TrayMenu(categories));
         noOfCategories--;
         selectedCat = null;
         
         //disable all Buttons and fields
         moveUpButton.setEnabled(false);
         moveDownButton.setEnabled(false);
         deleteButton.setEnabled(false);
         catNameField.setEnabled(false);
         defaultBox.setEnabled(false);
         colorBox.setEnabled(false);
      }
   }

   private void moveSelectedCategory(boolean moveUp) {
      if (moveUp) {
         if(selectedCat.isDefaultCategory()) {
            defCat--;
         } else if (categories.getCategoryByNumber(selectedRow-1).isDefaultCategory()) {
            defCat++;
         }
         categories.moveCategoryUp(selectedCat);
         catTableModel.moveRow(selectedRow, selectedRow, selectedRow-1);
         catTable.setRowSelectionInterval(selectedRow-1, selectedRow-1);
      } else {
         if(selectedCat.isDefaultCategory()) {
            defCat++;
         } else if (categories.getCategoryByNumber(selectedRow+1).isDefaultCategory()) {
            defCat--;
         }
         categories.moveCategoryDown(selectedCat);
         catTableModel.moveRow(selectedRow, selectedRow, selectedRow+1);
         catTable.setRowSelectionInterval(selectedRow+1, selectedRow+1);
      }
      PinEmUp.getMainApp().getTrayIcon().setPopupMenu(new TrayMenu(categories));
   }
   
   private void makeSelectedCategoryDefault() {
      if (trackChanges) {
         catTableModel.setValueAt("", defCat, 0);
         defCat = selectedRow;
         categories.setDefaultCategory(selectedCat);
         catTableModel.setValueAt("!", defCat, 0);
      }
   }
   
   private void addCategory() {
      trackChanges = false;
      String catName = "<new category>";
      catNameField.setText(catName);
      defaultBox.setSelected(false);
      colorBox.setSelectedIndex(0);
      categories.addCategory(new Category(catName,false,(byte)(0)));
      PinEmUp.getMainApp().getTrayIcon().setPopupMenu(new TrayMenu(categories));
      Object[] rowData = {"",catName,"0"};
      catTableModel.addRow(rowData);
      noOfCategories++;
      catTable.setRowSelectionInterval(noOfCategories-1, noOfCategories-1);
      catNameField.setSelectionStart(0);
      catNameField.setSelectionEnd(catNameField.getText().length());
      catNameField.requestFocus();
      trackChanges = true;
   }
   
   public void changedUpdate(DocumentEvent arg0) {
      updateCatName();
   }

   public void insertUpdate(DocumentEvent arg0) {
      updateCatName();
   }

   public void removeUpdate(DocumentEvent arg0) {
      updateCatName();
   }
   
   private void updateCatName() {
      if (trackChanges) {
         String name = catNameField.getText();
         catTable.getModel().setValueAt(name, selectedRow, 1);
         selectedCat.setName(name);
         PinEmUp.getMainApp().getTrayIcon().setPopupMenu(new TrayMenu(categories));
      }
   }
   
   private void updateCatColor() {
      if (trackChanges) {
         byte c = (byte)colorBox.getSelectedIndex();
         catTable.getModel().setValueAt(String.valueOf(c), selectedRow, 2);
         selectedCat.setDefaultNoteColor(c);
         colorBox.setBackground(BackgroundLabel.getColor(selectedCat.getDefaultNoteColor()));
      }
   }

   public void valueChanged(ListSelectionEvent e) {
      selectedRow = catTable.getSelectedRow();
      selectedCat = categories.getCategoryByNumber(selectedRow);

      //ENABLE OR DISABLE MOVEDOWN BUTTON
      if (selectedRow == noOfCategories-1) {
         moveDownButton.setEnabled(false);
      } else {
         moveDownButton.setEnabled(true);
      }
      
      //ENABLE OR DISABLE MOVEUP BUTTON
      if (selectedRow == 0) {
         moveUpButton.setEnabled(false);
      } else {
         moveUpButton.setEnabled(true);
      }
      
      //ENABLE DELETE BUTTON?
      if (noOfCategories > 1) {
         deleteButton.setEnabled(true);
      }
      
      //ENABLE EDIT-FIELDS
      catNameField.setEnabled(true);
      defaultBox.setEnabled(true);
      colorBox.setEnabled(true);
      
      //Insert values in fields
      trackChanges = false;
      catNameField.setText(selectedCat.getName());
      defaultBox.setSelected(selectedCat.isDefaultCategory());
      if (defaultBox.isSelected()) {
         defaultBox.setEnabled(false);
      } else {
         defaultBox.setEnabled(true);
      }
      colorBox.setSelectedIndex(selectedCat.getDefaultNoteColor());
      colorBox.setBackground(BackgroundLabel.getColor(selectedCat.getDefaultNoteColor()));
      trackChanges = true;
   }

}
