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

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.ui.swing.NoteWindow;
import net.sourceforge.pinemup.ui.swing.NoteWindowManager;

class MenuCreator implements ActionListener {
   private CategoryJMenuItem[] categoryItemJ = null;
   private CategoryMenuItem[] categoryItem = null;
   private JMenuItem[] basicItemJ = null;
   private MenuItem[] basicItem = null;

   private String[] getBasicItemTexts() {
      String[] s = {I18N.getInstance().getString("menu.addnoteitem"), I18N.getInstance().getString("menu.showallnotesitem"),
            I18N.getInstance().getString("menu.hideallnotesitem")};
      return s;
   }

   private String[] getCategoryItemTexts() {
      String[] s = {I18N.getInstance().getString("menu.categorymenu.hidenotes"),
            I18N.getInstance().getString("menu.categorymenu.shownotes"), I18N.getInstance().getString("menu.categorymenu.showonlynotes"),
            I18N.getInstance().getString("menu.categorymenu.setasdefault")};
      return s;
   }

   public JMenuItem[] getBasicJMenuItems() {
      String[] texts = getBasicItemTexts();
      basicItemJ = new JMenuItem[texts.length];
      for (int i = 0; i < texts.length; i++) {
         basicItemJ[i] = new JMenuItem(texts[i]);
         basicItemJ[i].addActionListener(this);
      }
      return basicItemJ;
   }

   public MenuItem[] getBasicMenuItems() {
      String[] texts = getBasicItemTexts();
      basicItem = new MenuItem[texts.length];
      for (int i = 0; i < texts.length; i++) {
         basicItem[i] = new MenuItem(texts[i]);
         basicItem[i].addActionListener(this);
      }
      return basicItem;
   }

   public JMenu getCategoryActionsJMenu(String title, Category c) {
      JMenu menu = new JMenu(title);
      String[] texts = getCategoryItemTexts();
      categoryItemJ = new CategoryJMenuItem[texts.length];
      for (int i = 0; i < texts.length; i++) {
         categoryItemJ[i] = new CategoryJMenuItem(texts[i], c);
         categoryItemJ[i].addActionListener(this);
         menu.add(categoryItemJ[i]);
         switch (i) {
         case 2:
            menu.addSeparator();
            break;
         default:
            break;
         }
      }
      return menu;
   }

   public Menu getCategoryActionsMenu(String title, Category c) {
      Menu menu = new Menu(title);
      String[] texts = getCategoryItemTexts();
      categoryItem = new CategoryMenuItem[texts.length];
      for (int i = 0; i < texts.length; i++) {
         categoryItem[i] = new CategoryMenuItem(texts[i], c);
         categoryItem[i].addActionListener(this);
         menu.add(categoryItem[i]);
         switch (i) {
         case 2:
            menu.addSeparator();
            break;
         default:
            break;
         }
      }
      return menu;
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if ((basicItem != null && src == basicItem[0]) || (basicItemJ != null && src == basicItemJ[0])) {
         Note newNote = CategoryManager.getInstance().createNoteAndAddToDefaultCategory();
         NoteWindow window = NoteWindowManager.getInstance().createNoteWindowForNote(newNote);
         window.jumpIntoTextArea();
      } else if ((basicItem != null && src == basicItem[1]) || (basicItemJ != null && src == basicItemJ[1])) {
         CategoryManager.getInstance().unhideAllNotes();
         NoteWindowManager.getInstance().createNoteWindowsForAllVisibleNotes();
      } else if ((basicItem != null && src == basicItem[2]) || (basicItemJ != null && src == basicItemJ[2])) {
         CategoryManager.getInstance().hideAllNotes();
      } else if ((categoryItem != null && src == categoryItem[0]) || (categoryItemJ != null && src == categoryItemJ[0])) {
         ((MenuItemWithCategory)src).getCategory().hideAllNotes();
      } else if ((categoryItem != null && src == categoryItem[1]) || (categoryItemJ != null && src == categoryItemJ[1])) {
         ((MenuItemWithCategory)src).getCategory().unhideAllNotes();
      } else if ((categoryItem != null && src == categoryItem[2]) || (categoryItemJ != null && src == categoryItemJ[2])) {
         CategoryManager.getInstance().showOnlyNotesOfCategory(((MenuItemWithCategory)src).getCategory());
      } else if ((categoryItem != null && src == categoryItem[3]) || (categoryItemJ != null && src == categoryItemJ[3])) {
         CategoryManager.getInstance().setDefaultCategory(((MenuItemWithCategory)src).getCategory());
      }
   }

   private static class CategoryMenuItem extends MenuItem implements MenuItemWithCategory {
      private static final long serialVersionUID = -8783854932523988763L;

      private Category myCat;

      public CategoryMenuItem(String title, Category c) {
         super(title);
         myCat = c;
      }

      @Override
      public Category getCategory() {
         return myCat;
      }
   }

   private static class CategoryJMenuItem extends JMenuItem implements MenuItemWithCategory {
      private static final long serialVersionUID = 7661460974200116943L;

      private Category myCat;

      public CategoryJMenuItem(String title, Category c) {
         super(title);
         myCat = c;
      }

      @Override
      public Category getCategory() {
         return myCat;
      }
   }

   private interface MenuItemWithCategory {
      Category getCategory();
   }
}
