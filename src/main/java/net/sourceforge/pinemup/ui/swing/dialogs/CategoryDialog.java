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

package net.sourceforge.pinemup.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.model.NoteColor;
import net.sourceforge.pinemup.ui.swing.utils.SwingUtils;

public final class CategoryDialog extends JDialog implements ActionListener, DocumentListener, ListSelectionListener {
   private static final long serialVersionUID = 1L;

   private static CategoryDialog instance;

   private JButton closeButton, moveUpButton, moveDownButton, deleteButton, addButton;

   private JTable catTable;
   private DefaultTableModel catTableModel;
   private JTextField catNameField;
   private JCheckBox defaultBox;
   private JComboBox<NoteColor> colorBox;

   private int noOfCategories;
   private int defCat;
   private int selectedRow;
   private Category selectedCat;

   private boolean trackChanges;

   public static void showInstance() {
      if (CategoryDialog.instance == null || !CategoryDialog.instance.isVisible()) {
         instance = new CategoryDialog();
      }
   }

   private CategoryDialog() {
      super();
      setTitle(I18N.getInstance().getString("categorydialog.title"));
      trackChanges = false;
      noOfCategories = CategoryManager.getInstance().getNumberOfCategories();
      Container main = getContentPane();
      main.setLayout(new BorderLayout());

      JPanel buttonPanel = new JPanel();
      closeButton = new JButton(I18N.getInstance().getString("closebutton"));
      closeButton.addActionListener(this);
      buttonPanel.add(closeButton);
      main.add(buttonPanel, BorderLayout.SOUTH);

      GridBagLayout gbl = new GridBagLayout();
      JPanel centerPanel = new JPanel(gbl);
      main.add(centerPanel, BorderLayout.CENTER);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
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

      // create items
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

      // place items on panel
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

      // automatically set window-size
      pack();

      SwingUtils.centerWindowOnScreen(this);
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
         @Override
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      };

      String[] columnNames = {I18N.getInstance().getString("categorydialog.defaultcolumn"),
            I18N.getInstance().getString("categorydialog.namecolumn"), I18N.getInstance().getString("categorydialog.colorcolumn")};
      catTableModel.setColumnIdentifiers(columnNames);
      Object[][] rowData = makeDataArray();
      for (int i = 0; i < rowData.length; i++) {
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
      int anz = CategoryManager.getInstance().getNumberOfCategories();
      Object[][] data = new Object[anz][3];
      int index = 0;
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         if (cat.isDefaultCategory()) {
            data[index][0] = "!";
            defCat = index;
         } else {
            data[index][0] = "";
         }
         data[index][1] = cat.getName();
         data[index][2] = String.valueOf(cat.getDefaultNoteColor().getCode());
         index++;
      }
      return data;
   }

   private JPanel makeCatEditPanel() {
      JPanel p = new JPanel();
      catNameField = new JTextField(15);
      catNameField.getDocument().addDocumentListener(this);
      catNameField.setEnabled(false);
      p.add(catNameField);
      defaultBox = new JCheckBox(I18N.getInstance().getString("categorydialog.defaultcheckbox"));
      defaultBox.addActionListener(this);
      defaultBox.setEnabled(false);
      p.add(defaultBox);

      DefaultListCellRenderer cr = new DefaultListCellRenderer() {
         private static final long serialVersionUID = 1L;

         @Override
         public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
            if (index >= 0) {
               c.setBackground(NoteColor.values()[index].getColor1());
            }
            return c;
         }
      };
      colorBox = new JComboBox<>(NoteColor.values());
      colorBox.setRenderer(cr);
      colorBox.addActionListener(this);
      colorBox.setEnabled(false);

      p.add(colorBox);

      p.setBorder(new TitledBorder(I18N.getInstance().getString("categorydialog.editborder")));

      return p;
   }

   @Override
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
         confirmed = JOptionPane.showConfirmDialog(null,
               I18N.getInstance().getString("categorydialog.deletemessages", selectedCat.getNumberOfNotes()),
               I18N.getInstance().getString("categorydialog.deletemessagestitle"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
      }

      if (confirmed) {
         boolean isDef = selectedCat.isDefaultCategory();
         selectedCat.hideAllNotes();
         CategoryManager.getInstance().removeCategory(selectedCat);
         catTableModel.removeRow(selectedRow);
         if (isDef) {
            // set first category as default
            CategoryManager.getInstance().getCategories().get(0).setDefault(true);
            catTableModel.setValueAt("!", 0, 0);
            defCat = 0;
         }
         noOfCategories--;
         selectedCat = null;

         // disable all Buttons and fields
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
         if (selectedCat.isDefaultCategory()) {
            defCat--;
         } else if (CategoryManager.getInstance().getCategoryByNumber(selectedRow - 1).isDefaultCategory()) {
            defCat++;
         }
         CategoryManager.getInstance().moveCategoryUp(selectedCat);
         catTableModel.moveRow(selectedRow, selectedRow, selectedRow - 1);
         catTable.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
      } else {
         if (selectedCat.isDefaultCategory()) {
            defCat++;
         } else if (CategoryManager.getInstance().getCategoryByNumber(selectedRow + 1).isDefaultCategory()) {
            defCat--;
         }
         CategoryManager.getInstance().moveCategoryDown(selectedCat);
         catTableModel.moveRow(selectedRow, selectedRow, selectedRow + 1);
         catTable.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
      }
   }

   private void makeSelectedCategoryDefault() {
      if (trackChanges) {
         catTableModel.setValueAt("", defCat, 0);
         defCat = selectedRow;
         CategoryManager.getInstance().setDefaultCategory(selectedCat);
         catTableModel.setValueAt("!", defCat, 0);
      }
   }

   private void addCategory() {
      trackChanges = false;
      String catName = "<" + I18N.getInstance().getString("categorydialog.defaultcategoryname") + ">";
      catNameField.setText(catName);
      defaultBox.setSelected(false);
      colorBox.setSelectedIndex(0);
      Category newCat = new Category(catName, false, NoteColor.DEFAULT_COLOR);
      CategoryManager.getInstance().addCategory(newCat);
      Object[] rowData = {"", catName, "0"};
      catTableModel.addRow(rowData);
      noOfCategories++;
      catTable.setRowSelectionInterval(noOfCategories - 1, noOfCategories - 1);
      catNameField.setSelectionStart(0);
      catNameField.setSelectionEnd(catNameField.getText().length());
      catNameField.requestFocus();
      trackChanges = true;
   }

   @Override
   public void changedUpdate(DocumentEvent arg0) {
      updateCatName();
   }

   @Override
   public void insertUpdate(DocumentEvent arg0) {
      updateCatName();
   }

   @Override
   public void removeUpdate(DocumentEvent arg0) {
      updateCatName();
   }

   private void updateCatName() {
      if (trackChanges) {
         String name = catNameField.getText();
         catTable.getModel().setValueAt(name, selectedRow, 1);
         selectedCat.setName(name);
      }
   }

   private void updateCatColor() {
      if (trackChanges) {
         NoteColor catColor = colorBox.getItemAt(colorBox.getSelectedIndex());
         int colorCode = colorBox.getSelectedIndex();
         catTable.getModel().setValueAt(String.valueOf(colorCode), selectedRow, 2);
         selectedCat.setDefaultNoteColor(catColor);
         colorBox.setBackground(catColor.getColor1());
      }
   }

   @Override
   public void valueChanged(ListSelectionEvent e) {
      selectedRow = catTable.getSelectedRow();
      if (selectedRow != -1) {
         selectedCat = CategoryManager.getInstance().getCategoryByNumber(selectedRow);
      } else {
         selectedCat = null;
      }

      // ENABLE OR DISABLE MOVEDOWN BUTTON
      if (selectedRow == noOfCategories - 1) {
         moveDownButton.setEnabled(false);
      } else {
         moveDownButton.setEnabled(true);
      }

      // ENABLE OR DISABLE MOVEUP BUTTON
      if (selectedRow == 0) {
         moveUpButton.setEnabled(false);
      } else {
         moveUpButton.setEnabled(true);
      }

      // ENABLE DELETE BUTTON?
      if (noOfCategories > 1) {
         deleteButton.setEnabled(true);
      }

      // ENABLE EDIT-FIELDS
      catNameField.setEnabled(true);
      defaultBox.setEnabled(true);
      colorBox.setEnabled(true);

      // Insert values in fields
      if (selectedCat != null) {
         trackChanges = false;
         catNameField.setText(selectedCat.getName());
         defaultBox.setSelected(selectedCat.isDefaultCategory());
         if (defaultBox.isSelected()) {
            defaultBox.setEnabled(false);
         } else {
            defaultBox.setEnabled(true);
         }
         colorBox.setSelectedItem(selectedCat.getDefaultNoteColor());
         colorBox.setBackground(selectedCat.getDefaultNoteColor().getColor1());
         trackChanges = true;
      }
   }

}
