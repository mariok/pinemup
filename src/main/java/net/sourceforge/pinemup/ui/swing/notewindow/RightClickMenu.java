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

package net.sourceforge.pinemup.ui.swing.notewindow;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.core.NoteColor;
import net.sourceforge.pinemup.ui.swing.menus.logic.CategoryMenuLogic;
import net.sourceforge.pinemup.ui.swing.menus.logic.NoteMenuLogic;
import net.sourceforge.pinemup.ui.swing.menus.logic.CategoryMenuLogic.CategoryAction;
import net.sourceforge.pinemup.ui.swing.menus.logic.GeneralMenuLogic;
import net.sourceforge.pinemup.ui.swing.menus.logic.GeneralMenuLogic.GeneralAction;

public class RightClickMenu extends JPopupMenu {
   private static final long serialVersionUID = -3437718385990990890L;

   public RightClickMenu(Note parentNote) {
      super();

      NoteMenuLogic noteMenuLogic = new NoteMenuLogic(parentNote);

      // add basic items
      for (JMenuItem item : getBasicMenuItems()) {
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
      for (int i = Note.MIN_FONT_SIZE; i <= Note.MAX_FONT_SIZE; i++) {
         boolean activeFontSize = i == parentNote.getFontSize();
         JRadioButtonMenuItem fontSizeItem = new JRadioButtonMenuItem(String.valueOf(i), activeFontSize);
         fontSizeItem.setActionCommand(NoteMenuLogic.ACTION_SET_NOTE_FONT_SIZE + "_" + i);
         fontSizeItem.addActionListener(noteMenuLogic);
         fontSizeMenu.add(fontSizeItem);
      }

      JMenu colorMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.color"));
      settingsMenu.add(colorMenu);
      for (NoteColor color : NoteColor.values()) {
         boolean isActiveColor = color.equals(parentNote.getColor());
         JRadioButtonMenuItem colorItem = new JRadioButtonMenuItem(color.getLocalizedName(), isActiveColor);
         colorItem.setActionCommand(NoteMenuLogic.ACTION_SET_NOTE_COLOR + "_" + color.getCode());
         colorItem.addActionListener(noteMenuLogic);
         colorMenu.add(colorItem);
      }

      JMenu categoryMenu = new JMenu(I18N.getInstance().getString("category"));
      int i = 0;
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         boolean isActiveCategory = cat == parentNote.getCategory();
         JRadioButtonMenuItem categoryItem = new JRadioButtonMenuItem(cat.getName(), isActiveCategory);
         categoryItem.setActionCommand(NoteMenuLogic.ACTION_MOVE_NOTE_TO_CATEGORY + "_" + i);
         categoryItem.addActionListener(noteMenuLogic);
         categoryMenu.add(categoryItem);
         i++;
      }
      settingsMenu.add(categoryMenu);

      JCheckBoxMenuItem alwaysOnTopItem = new JCheckBoxMenuItem(I18N.getInstance().getString("menu.notesettings.alwaysontop"),
            parentNote.isAlwaysOnTop());
      alwaysOnTopItem.setActionCommand(NoteMenuLogic.ACTION_TOGGLE_ALWAYS_ON_TOP);
      alwaysOnTopItem.addActionListener(noteMenuLogic);
      settingsMenu.add(alwaysOnTopItem);

      // category menu
      Category currentCat = parentNote.getCategory();
      add(getCategoryActionsMenu(I18N.getInstance().getString("category") + " '" + currentCat.getName() + "'", currentCat));
   }

   private JMenu getCategoryActionsMenu(String title, Category c) {
      JMenu menu = new JMenu(title);
      CategoryMenuLogic catMenuLogic = new CategoryMenuLogic(c);
      int i = 0;
      for (CategoryAction action : CategoryMenuLogic.CategoryAction.values()) {
         JMenuItem menuItem = new JMenuItem(I18N.getInstance().getString(action.getI18nKey()));
         menuItem.setActionCommand(action.toString());
         menuItem.addActionListener(catMenuLogic);
         menu.add(menuItem);
         if (i == 2) {
            menu.addSeparator();
         }
         i++;
      }
      return menu;
   }

   private List<JMenuItem> getBasicMenuItems() {
      GeneralMenuLogic basicMenuLogic = new GeneralMenuLogic();
      List<JMenuItem> menuItems = new ArrayList<>();
      for (GeneralAction action : GeneralAction.values()) {
         JMenuItem menuItem = new JMenuItem(I18N.getInstance().getString(action.getI18nKey()));
         menuItem.setActionCommand(action.toString());
         menuItem.addActionListener(basicMenuLogic);
         menuItems.add(menuItem);
      }
      return menuItems;
   }
}
