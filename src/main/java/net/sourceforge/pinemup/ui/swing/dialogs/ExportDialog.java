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
import net.sourceforge.pinemup.core.io.notes.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.notes.stream.export.NotesTextExportWriter;
import net.sourceforge.pinemup.core.io.utils.FileUtils;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.ui.swing.dialogs.file.FileDialogCreator;
import net.sourceforge.pinemup.ui.swing.utils.SwingUtils;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

public class ExportDialog extends JDialog {
   private static final int DIALOG_WIDTH = 250;
   private static final int DIALOG_HEIGHT = 300;

   /** Store references to some components in order to interact with them on certain actions. */
   private final JCheckBox[] catBoxes;
   private final JCheckBox allCatsBox;

   ExportDialog() {
      super();
      setTitle(I18N.getInstance().getString("exportdialog.title"));

      JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      topPanel.add(new JLabel(I18N.getInstance().getString("exportdialog.toplabel")));

      // category checkboxes
      int numberOfRows = CategoryManager.getInstance().getNumberOfCategories();
      JPanel checkBoxPanel = new JPanel(new GridLayout(numberOfRows + 2, 1));
      String[] cats = CategoryManager.getInstance().getCategoryNames();
      JPanel[] catPanel = new JPanel[numberOfRows];
      catBoxes = new JCheckBox[numberOfRows];
      JPanel allCatsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      allCatsBox = new JCheckBox("(" + I18N.getInstance().getString("exportdialog.allcategoriescheckbox") + ")");
      allCatsBox.setSelected(true);
      allCatsBox.addActionListener(e -> onSelectAllStateChanged());
      allCatsPanel.add(allCatsBox);
      checkBoxPanel.add(allCatsPanel);
      for (int i = 0; i < numberOfRows; i++) {
         catPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
         catBoxes[i] = new JCheckBox((i + 1) + ": " + cats[i]);
         catBoxes[i].setSelected(true);
         catBoxes[i].addActionListener(e -> updateSelectAllCheckBoxState());
         catPanel[i].add(catBoxes[i]);
         checkBoxPanel.add(catPanel[i]);
      }

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setViewportView(checkBoxPanel);

      JPanel buttonPanel = new JPanel();
      JButton okButton = new JButton(I18N.getInstance().getString("okbutton"));
      okButton.addActionListener(e -> exportSelectedCategories());
      JButton cancelButton = new JButton(I18N.getInstance().getString("cancelbutton"));
      cancelButton.addActionListener(e -> closeDialog());
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);

      JPanel main = new JPanel(new BorderLayout());
      main.add(topPanel, BorderLayout.NORTH);
      main.add(scrollPane, BorderLayout.CENTER);
      main.add(buttonPanel, BorderLayout.SOUTH);
      setContentPane(main);

      setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
      SwingUtils.centerWindowOnScreen(this);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setVisible(true);
   }

   private void closeDialog() {
      setVisible(false);
      dispose();
   }

   private void exportSelectedCategories() {
      List<Category> catsToExport = new LinkedList<>();
      for (int i = 0; i < catBoxes.length; i++) {
         if (catBoxes[i].isSelected()) {
            catsToExport.add(CategoryManager.getInstance().getCategoryByNumber(i));
         }
      }

      if (FileDialogCreator.getExportFileDialogInstance().showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
         String fileName = FileDialogCreator.getExportFileDialogInstance().getSelectedFile().getAbsolutePath();
         String checkedFileName = FileUtils.checkAndAddExtension(fileName, ".txt");

         NotesFileWriter exportFileWriter = new NotesFileWriter(new NotesTextExportWriter());
         exportFileWriter.writeCategoriesToFile(catsToExport, checkedFileName);
      }

      closeDialog();
   }

   private void onSelectAllStateChanged() {
      for (JCheckBox catBox : catBoxes) {
         catBox.setSelected(allCatsBox.isSelected());
      }
   }

   public void updateSelectAllCheckBoxState() {
      boolean allCategoriesSelected = true;
      for (JCheckBox catBox : catBoxes) {
         allCategoriesSelected &= catBox.isSelected();
         if (!allCategoriesSelected) {
            break;
         }
      }
      allCatsBox.setSelected(allCategoriesSelected);
   }
}
