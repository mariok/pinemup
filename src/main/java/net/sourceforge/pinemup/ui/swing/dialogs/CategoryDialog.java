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

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.core.model.NoteColor;
import net.sourceforge.pinemup.ui.swing.utils.SwingUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public final class CategoryDialog extends JDialog implements DocumentListener, ListSelectionListener {
   private static final long serialVersionUID = 1L;

   private static CategoryDialog instance;

   /** Keep references to some controls in order to be able to update their status later. */
   private final JButton moveUpButton;
   private final JButton moveDownButton;
   private final JButton deleteButton;
   private final JTable catTable;
   private final DefaultTableModel catTableModel;
   private final JTextField catNameField;
   private final JCheckBox defaultBox;
   private final JComboBox<NoteColor> colorBox;

   /** Used to remove the default-category-marker from the old default category row after a change. */
   private int rowNumberOfCurrentDefaultCategory;

   /** References the currently selected category. */
   private Category selectedCategory;

   /** Used to temporarily disable the instant-updating of the category-attributes in the table. */
   private boolean trackCategoryChangesInTable;

   public static void showInstance() {
      if (CategoryDialog.instance == null || !CategoryDialog.instance.isVisible()) {
         instance = new CategoryDialog();
      }
   }

   private CategoryDialog() {
      super();
      setTitle(I18N.getInstance().getString("categorydialog.title"));

      JPanel buttonPanel = new JPanel();
      JButton closeButton = new JButton(I18N.getInstance().getString("closebutton"));
      buttonPanel.add(closeButton);

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      catTableModel = createTableModel();
      catTable = makeCatTable(catTableModel);
      scrollPane.setViewportView(catTable);
      catNameField = new JTextField(15);
      defaultBox = new JCheckBox(I18N.getInstance().getString("categorydialog.defaultcheckbox"));
      colorBox = new JComboBox<>(NoteColor.values());
      JPanel catEditPanel = makeCatEditPanel(catNameField, defaultBox, colorBox);

      moveUpButton = new JButton("^");
      moveUpButton.setEnabled(false);
      JPanel moveUpPanel = new JPanel();
      moveUpPanel.add(moveUpButton);

      moveDownButton = new JButton("v");
      moveDownButton.setEnabled(false);
      JPanel moveDownPanel = new JPanel();
      moveDownPanel.add(moveDownButton);

      deleteButton = new JButton("X");
      deleteButton.setEnabled(false);
      JPanel deletePanel = new JPanel();
      deletePanel.add(deleteButton);

      JButton addCategoryButton = new JButton("+");
      addCategoryButton.setEnabled(true);
      JPanel addPanel = new JPanel();
      addPanel.add(addCategoryButton);

      JPanel emptyPanel = new JPanel();
      JPanel emptyPanel2 = new JPanel();

      // place items on center-panel
      GridBagLayout gbl = new GridBagLayout();
      JPanel centerPanel = new JPanel(gbl);
      GridBagConstraints constraints = createGridBagConstraints();
      placeOnPanelWithGridBagLayout(centerPanel, scrollPane, constraints, 0, 0, 1, 4, 100, 100);
      placeOnPanelWithGridBagLayout(centerPanel, catEditPanel, constraints, 0, 4, 1, 2, 0, 0);
      placeOnPanelWithGridBagLayout(centerPanel, moveUpPanel, constraints, 1, 0, 1, 1, 0, 0);
      placeOnPanelWithGridBagLayout(centerPanel, moveDownPanel, constraints, 1, 1, 1, 1, 0, 0);
      placeOnPanelWithGridBagLayout(centerPanel, deletePanel, constraints, 1, 2, 1, 1, 0, 0);
      placeOnPanelWithGridBagLayout(centerPanel, emptyPanel, constraints, 1, 3, 1, 1, 0, 100);
      placeOnPanelWithGridBagLayout(centerPanel, addPanel, constraints, 1, 4, 1, 1, 0, 0);
      placeOnPanelWithGridBagLayout(centerPanel, emptyPanel2, constraints, 1, 5, 1, 1, 0, 0);

      Container main = getContentPane();
      main.setLayout(new BorderLayout());
      main.add(buttonPanel, BorderLayout.SOUTH);
      main.add(centerPanel, BorderLayout.CENTER);

      // add behavior to controls
      addCategoryButton.addActionListener(e -> addCategory());
      moveUpButton.addActionListener(e -> moveSelectedCategory(true));
      moveDownButton.addActionListener(e -> moveSelectedCategory(false));
      deleteButton.addActionListener(e -> deleteSelectedCategory());
      closeButton.addActionListener(e -> closeDialog());

      // automatically set window-size
      pack();

      SwingUtils.centerWindowOnScreen(this);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setVisible(true);
   }

   private DefaultTableModel createTableModel() {
      return new DefaultTableModel() {
         private static final long serialVersionUID = 1L;

         // prevent all cells from being edited
         @Override
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      };
   }

   private JTable makeCatTable(DefaultTableModel tableModel) {
      String[] columnNames = {I18N.getInstance().getString("categorydialog.defaultcolumn"),
            I18N.getInstance().getString("categorydialog.namecolumn"), I18N.getInstance().getString("categorydialog.colorcolumn")};
      tableModel.setColumnIdentifiers(columnNames);
      Object[][] rowData = makeDataArray();
      for (Object[] row : rowData) {
         tableModel.addRow(row);
      }

      JTable jt = new JTable(tableModel);
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
      Object[][] data = new Object[anz][];
      int index = 0;
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         if (cat.isDefaultCategory()) {
            rowNumberOfCurrentDefaultCategory = index;
         }
         data[index] = createRowForCategory(cat);
         index++;
      }
      return data;
   }

   private String[] createRowForCategory(Category category) {
      String defaultMarker = category.isDefaultCategory() ? "!" : "";
      return new String[] { defaultMarker, category.getName(), String.valueOf(category.getDefaultNoteColor().getCode()) };
   }

   private JPanel makeCatEditPanel(JTextField catNameField, JCheckBox defaultBox, JComboBox<NoteColor> colorBox) {
      JPanel p = new JPanel();
      catNameField.getDocument().addDocumentListener(this);
      catNameField.setEnabled(false);
      p.add(catNameField);
      defaultBox.addActionListener(e -> {
         makeSelectedCategoryDefault();
         defaultBox.setEnabled(false);
      });
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

      colorBox.setRenderer(cr);
      colorBox.addActionListener(e -> updateCatColor());
      colorBox.setEnabled(false);

      p.add(colorBox);

      p.setBorder(new TitledBorder(I18N.getInstance().getString("categorydialog.editborder")));

      return p;
   }

   private void placeOnPanelWithGridBagLayout(JPanel panel, Component component, GridBagConstraints constraints, int xPos, int yPos, int width, int height, int weightX, int weightY) {
      constraints.gridx = xPos;
      constraints.gridy = yPos;
      constraints.gridwidth = width;
      constraints.gridheight = height;
      constraints.weightx = weightX;
      constraints.weighty = weightY;
      ((GridBagLayout)panel.getLayout()).setConstraints(component, constraints);
      panel.add(component);
   }

   private GridBagConstraints createGridBagConstraints() {
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.insets = new Insets(1, 1, 1, 1);
      constraints.anchor = GridBagConstraints.NORTHWEST;
      constraints.fill = GridBagConstraints.BOTH;

      return constraints;
   }

   private void deleteSelectedCategory() {
      boolean confirmed = true;
      if (selectedCategory.getNumberOfNotes() > 0) {
         confirmed = JOptionPane.showConfirmDialog(null,
               I18N.getInstance().getString("categorydialog.deletemessages", selectedCategory.getNumberOfNotes()),
               I18N.getInstance().getString("categorydialog.deletemessagestitle"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
      }

      if (confirmed) {
         boolean wasDefaultCategory = selectedCategory.isDefaultCategory();
         selectedCategory.hideAllNotes();
         CategoryManager.getInstance().removeCategory(selectedCategory);
         catTableModel.removeRow(catTable.getSelectedRow());
         if (wasDefaultCategory) {
            // removing the default category automatically sets the first category as default
            catTableModel.setValueAt("!", 0, 0);
            rowNumberOfCurrentDefaultCategory = 0;
         }
         selectedCategory = null;

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
      int selectedRowIndex = catTable.getSelectedRow();
      int indexToAdd = moveUp ? -1 : 1;

      if (selectedCategory.isDefaultCategory()) {
         rowNumberOfCurrentDefaultCategory += indexToAdd;
      } else if (CategoryManager.getInstance().getCategoryByNumber(selectedRowIndex + indexToAdd).isDefaultCategory()) {
         rowNumberOfCurrentDefaultCategory += (-1) * indexToAdd;
      }

      if (moveUp) {
         CategoryManager.getInstance().moveCategoryUp(selectedCategory);
      } else {
         CategoryManager.getInstance().moveCategoryDown(selectedCategory);
      }

      catTableModel.moveRow(selectedRowIndex, selectedRowIndex, selectedRowIndex + indexToAdd);
      catTable.setRowSelectionInterval(selectedRowIndex + indexToAdd, selectedRowIndex + indexToAdd);
   }

   private void makeSelectedCategoryDefault() {
      if (trackCategoryChangesInTable) {
         catTableModel.setValueAt("", rowNumberOfCurrentDefaultCategory, 0);
         rowNumberOfCurrentDefaultCategory = catTable.getSelectedRow();
         CategoryManager.getInstance().setDefaultCategory(selectedCategory);
         catTableModel.setValueAt("!", rowNumberOfCurrentDefaultCategory, 0);
      }
   }

   private void closeDialog() {
      setVisible(false);
      dispose();
   }

   private void addCategory() {
      String catName = "<" + I18N.getInstance().getString("categorydialog.defaultcategoryname") + ">";
      Category newCat = new Category(catName, false, NoteColor.DEFAULT_COLOR);
      CategoryManager.getInstance().addCategory(newCat);

      Object[] row = createRowForCategory(newCat);
      catTableModel.addRow(row);
      catTable.setRowSelectionInterval(catTableModel.getRowCount() - 1, catTableModel.getRowCount() - 1);

      catNameField.setSelectionStart(0);
      catNameField.setSelectionEnd(catNameField.getText().length());
      catNameField.requestFocus();
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
      if (trackCategoryChangesInTable) {
         String name = catNameField.getText();
         catTable.getModel().setValueAt(name, catTable.getSelectedRow(), 1);
         selectedCategory.setName(name);
      }
   }

   private void updateCatColor() {
      if (trackCategoryChangesInTable) {
         NoteColor catColor = colorBox.getItemAt(colorBox.getSelectedIndex());
         int colorCode = colorBox.getSelectedIndex();
         catTable.getModel().setValueAt(String.valueOf(colorCode), catTable.getSelectedRow(), 2);
         selectedCategory.setDefaultNoteColor(catColor);
         colorBox.setBackground(catColor.getColor1());
      }
   }

   @Override
   public void valueChanged(ListSelectionEvent e) {
      int selectedRow = catTable.getSelectedRow();
      if (selectedRow != -1) {
         selectedCategory = CategoryManager.getInstance().getCategoryByNumber(selectedRow);
      } else {
         selectedCategory = null;
      }

      moveDownButton.setEnabled(selectedRow < catTableModel.getRowCount() - 1);
      moveUpButton.setEnabled(selectedRow > 0);
      deleteButton.setEnabled(catTableModel.getRowCount() > 1);

      catNameField.setEnabled(true);
      defaultBox.setEnabled(true);
      colorBox.setEnabled(true);

      updateFieldsWithValuesFromSelectedCategory();
   }

   private void updateFieldsWithValuesFromSelectedCategory() {
      if (selectedCategory != null) {
         trackCategoryChangesInTable = false;
         catNameField.setText(selectedCategory.getName());
         defaultBox.setSelected(selectedCategory.isDefaultCategory());
         defaultBox.setEnabled(!defaultBox.isSelected());
         colorBox.setSelectedItem(selectedCategory.getDefaultNoteColor());
         colorBox.setBackground(selectedCategory.getDefaultNoteColor().getColor1());
         trackCategoryChangesInTable = true;
      }
   }

}
