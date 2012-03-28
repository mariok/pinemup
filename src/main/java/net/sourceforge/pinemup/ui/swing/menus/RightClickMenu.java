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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.core.NoteColor;

public class RightClickMenu extends JPopupMenu {
   private static final long serialVersionUID = -3437718385990990890L;

   private static final String ACTIVE_SYMBOL = "->";

   public RightClickMenu(Note parentNote) {
      super();

      MenuCreator myMenuCreator = new MenuCreator();

      NoteMenuLogic noteMenuLogic = new NoteMenuLogic(parentNote);

      // add basic items
      for (JMenuItem item : myMenuCreator.getBasicJMenuItems()) {
         add(item);
      }
      addSeparator();

      // add additional items
      JMenuItem deleteNoteItem = new JMenuItem(I18N.getInstance().getString("menu.deletenoteitem"));
      deleteNoteItem.setActionCommand(NoteMenuLogic.ACTION_DELETE_NOTE);
      deleteNoteItem.addActionListener(noteMenuLogic);
      add(deleteNoteItem);
      addSeparator();

      // settings menu
      JMenu settingsMenu = new JMenu(I18N.getInstance().getString("menu.notesettings"));
      add(settingsMenu);
      addSeparator();

      JMenu fontSizeMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.fontsize"));
      settingsMenu.add(fontSizeMenu);
      for (int i = Note.MIN_FONT_SIZE; i < Note.MAX_FONT_SIZE; i++) {
         JMenuItem fontSizeItem = new JMenuItem(String.valueOf(i));
         if (i == parentNote.getFontSize()) {
            fontSizeItem.setText(ACTIVE_SYMBOL + " " + fontSizeItem.getText());
         } else {
            fontSizeItem.setText("  " + fontSizeItem.getText());
         }
         fontSizeItem.setActionCommand(NoteMenuLogic.ACTION_SET_NOTE_FONT_SIZE + "_" + i);
         fontSizeItem.addActionListener(noteMenuLogic);
         fontSizeMenu.add(fontSizeItem);
      }

      JMenu colorMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.color"));
      settingsMenu.add(colorMenu);
      for (NoteColor color : NoteColor.values()) {
         String prefix = "   ";
         if (color.equals(parentNote.getColor())) {
            prefix = "  " + ACTIVE_SYMBOL + " ";
         }
         JMenuItem colorItem = new JMenuItem(prefix + color.getLocalizedName());
         colorItem.setActionCommand(NoteMenuLogic.ACTION_SET_NOTE_COLOR + "_" + color.getCode());
         colorItem.addActionListener(noteMenuLogic);
         colorMenu.add(colorItem);
      }

      JMenu categoryMenu = new JMenu(I18N.getInstance().getString("category"));
      int i = 0;
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         String prefix = " ";
         if (cat == parentNote.getCategory()) {
            prefix = ACTIVE_SYMBOL + " ";
         }
         JMenuItem categoryItem = new JMenuItem(prefix + cat.getName());
         categoryItem.setActionCommand(NoteMenuLogic.ACTION_MOVE_NOTE_TO_CATEGORY + "_" + i);
         categoryItem.addActionListener(noteMenuLogic);
         categoryMenu.add(categoryItem);
         i++;
      }
      settingsMenu.add(categoryMenu);

      String prefix = "";
      if (parentNote.isAlwaysOnTop()) {
         prefix = ACTIVE_SYMBOL + " ";
      }
      JMenuItem alwaysOnTopItem = new JMenuItem(prefix + I18N.getInstance().getString("menu.notesettings.alwaysontop"));
      alwaysOnTopItem.setActionCommand(NoteMenuLogic.ACTION_TOGGLE_ALWAYS_ON_TOP);
      alwaysOnTopItem.addActionListener(noteMenuLogic);
      settingsMenu.add(alwaysOnTopItem);

      // category menu
      Category currentCat = parentNote.getCategory();
      add(myMenuCreator.getCategoryActionsJMenu(I18N.getInstance().getString("category") + " '" + currentCat.getName() + "'", currentCat));
   }
}
