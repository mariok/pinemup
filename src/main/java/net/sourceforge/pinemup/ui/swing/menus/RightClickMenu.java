/*
 * pin 'em up
 *
 * Copyright (C) 2007-2012 by Mario Ködding
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

package net.sourceforge.pinemup.ui.swing.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.core.NoteColor;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.ui.I18N;

public class RightClickMenu extends JPopupMenu implements ActionListener {
   private static final long serialVersionUID = 1L;

   private static final String ACTIVE_SYMBOL = "->";
   private static final int NUMBER_OF_FONTSIZES = 26;

   private Note parentNote;

   private JMenuItem deleteNoteItem, alwaysOnTopOnItem, alwaysOnTopOffItem;
   private JMenu fontSizeMenu, colorMenu, categoryMenu;

   public RightClickMenu(Note n) {
      super();
      parentNote = n;

      // create MenuCreator
      MenuCreator myMenuCreator = new MenuCreator();

      // add basic items
      JMenuItem[] basicItems = myMenuCreator.getBasicJMenuItems();
      for (int i = 0; i < basicItems.length; i++) {
         add(basicItems[i]);
      }
      addSeparator();

      // add additional items
      deleteNoteItem = new JMenuItem(I18N.getInstance().getString("menu.deletenoteitem"));
      deleteNoteItem.addActionListener(this);
      add(deleteNoteItem);
      addSeparator();

      // settings menu
      JMenu settingsMenu = new JMenu(I18N.getInstance().getString("menu.notesettings"));

      fontSizeMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.fontsize"));
      for (int i = 0; i < NUMBER_OF_FONTSIZES; i++) {
         JMenuItem fontSizeItem = new JMenuItem(String.valueOf(i + 5));
         if (i + 5 == parentNote.getFontSize()) {
            fontSizeItem.setText(ACTIVE_SYMBOL + " " + fontSizeItem.getText());
         } else {
            fontSizeItem.setText("  " + fontSizeItem.getText());
         }
         fontSizeItem.addActionListener(this);

         fontSizeMenu.add(fontSizeItem);
      }

      colorMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.color"));
      for (NoteColor color : NoteColor.values()) {
         String prefix = " ";
         if (color.equals(parentNote.getColor())) {
            prefix = ACTIVE_SYMBOL + " ";
         }
         JMenuItem colorItem = new JMenuItem(prefix + color.getLocalizedName());
         colorItem.setText("  " + colorItem.getText());
         colorItem.addActionListener(this);
         colorMenu.add(colorItem);
      }

      categoryMenu = new JMenu(I18N.getInstance().getString("category"));
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         String prefix = " ";
         if (cat == parentNote.getCategory()) {
            prefix = ACTIVE_SYMBOL + " ";
         }
         JMenuItem categoryItem = new JMenuItem(prefix + cat.getName());
         categoryItem.addActionListener(this);
         categoryMenu.add(categoryItem);
      }

      JMenu alwaysOnTopMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.alwaysontop"));
      String[] aot = {"  ", "  "};
      if (parentNote.isAlwaysOnTop()) {
         aot[0] = ACTIVE_SYMBOL + " ";
      } else {
         aot[1] = ACTIVE_SYMBOL + " ";
      }
      alwaysOnTopOnItem = new JMenuItem(aot[0] + I18N.getInstance().getString("enabled"));
      alwaysOnTopOffItem = new JMenuItem(aot[1] + I18N.getInstance().getString("disabled"));
      alwaysOnTopOnItem.addActionListener(this);
      alwaysOnTopOffItem.addActionListener(this);
      alwaysOnTopMenu.add(alwaysOnTopOnItem);
      alwaysOnTopMenu.add(alwaysOnTopOffItem);

      settingsMenu.add(alwaysOnTopMenu);
      settingsMenu.add(categoryMenu);
      settingsMenu.add(fontSizeMenu);
      settingsMenu.add(colorMenu);
      add(settingsMenu);
      addSeparator();

      // category menu
      Category currentCat = parentNote.getCategory();
      add(myMenuCreator.getCategoryActionsJMenu(I18N.getInstance().getString("category") + " '" + currentCat.getName() + "'", currentCat));
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == deleteNoteItem) {
         boolean confirmed = true;
         if (UserSettings.getInstance().getConfirmDeletion()) {
            confirmed = JOptionPane.showConfirmDialog(this, I18N.getInstance().getString("confirm.deletenote"), I18N.getInstance()
                  .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
         }
         Category currentCat = parentNote.getCategory();
         if (confirmed && currentCat != null) {
            parentNote.setHidden(true);
            currentCat.removeNote(parentNote);
         }
      } else if (src == alwaysOnTopOnItem) {
         parentNote.setAlwaysOnTop(true);
      } else if (src == alwaysOnTopOffItem) {
         parentNote.setAlwaysOnTop(false);
      } else {
         for (int i = 0; i < categoryMenu.getItemCount(); i++) {
            if (src == categoryMenu.getItem(i)) {
               parentNote.moveToCategory(CategoryManager.getInstance().getCategoryByNumber(i));
            }
         }

         for (int i = 0; i < fontSizeMenu.getItemCount(); i++) {
            if (src == fontSizeMenu.getItem(i)) {
               parentNote.setFontSize((short)(i + 5));
            }
         }

         for (byte i = 0; i < colorMenu.getItemCount(); i++) {
            if (src == colorMenu.getItem(i)) {
               parentNote.setColor(NoteColor.getNoteColorByCode(i));
            }
         }
      }
   }
}
