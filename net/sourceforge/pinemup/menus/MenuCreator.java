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

package net.sourceforge.pinemup.menus;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import net.sourceforge.pinemup.logic.*;

public class MenuCreator implements ActionListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   private JMenuItem[] basicItemJ = null, categoryItemJ = null;
   private MenuItem[] basicItem = null, categoryItem = null;

   private CategoryList categories;
   
   private UserSettings settings;
   
   private static final String[] BASICITEMTEXT = {
      "add note",
      "show all notes",
      "hide all notes"
   };
   
   private static final String[] CATEGORYITEMTEXT = {
      "hide notes of this category",
      "show all notes of this category",
      "show only notes of this category",
      "set as default",
      "move up in list",
      "rename this category",
      "remove this category"
   };
   
   public MenuCreator(CategoryList cl, UserSettings s) {
      categories = cl;
      settings = s;
   }

   public JMenuItem[] getBasicJMenuItems() {
      basicItemJ = new JMenuItem[BASICITEMTEXT.length];
      for (int i=0; i<BASICITEMTEXT.length; i++) {
         basicItemJ[i] = new JMenuItem(BASICITEMTEXT[i]);
         basicItemJ[i].addActionListener(this);
      }
      return basicItemJ;
   }
   
   public MenuItem[] getBasicMenuItems() {
      basicItem = new MenuItem[BASICITEMTEXT.length];
      for (int i=0; i<BASICITEMTEXT.length; i++) {
         basicItem[i] = new MenuItem(BASICITEMTEXT[i]);
         basicItem[i].addActionListener(this);
      }
      return basicItem;
   }   
   
   public JMenu getCategoryActionsJMenu(String title, Category c) {
      JMenu menu = new JMenu(title);
      for (int i=0; i<CATEGORYITEMTEXT.length; i++) {
         categoryItemJ[i] = new JMenuItem(CATEGORYITEMTEXT[i]);
         categoryItemJ[i].addActionListener(this);
         menu.add(categoryItemJ[i]);
         switch(i) {
         case 2:
         case 5:
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
      for (int i=0; i<CATEGORYITEMTEXT.length; i++) {
         categoryItem[i] = new MenuItem(CATEGORYITEMTEXT[i]);
         categoryItem[i].addActionListener(this);
         menu.add(categoryItem[i]);
         switch(i) {
         case 2:
         case 5:
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
      if ((basicItemJ != null && src == basicItemJ[0]) || (basicItem != null && src == basicItem[0])) {
         Category defCat = categories.getDefaultCategory();
         if (defCat != null) {
            Note newNote = new Note("",settings,categories);
            defCat.getNotes().add(newNote);
            newNote.showIfNotHidden();
            newNote.jumpInto();
         }
      } else if ((basicItemJ != null && src == basicItemJ[1]) || (basicItem != null && src == basicItem[1])) {
         categories.unhideAndShowAllNotes();
      } else if ((basicItemJ != null && src == basicItemJ[2]) || (basicItem != null && src == basicItem[2])) {
         categories.hideAllNotes();
      }else if ((categoryItemJ != null && src == categoryItemJ[0]) || (categoryItem != null && src == categoryItem[0])) {
         myCategory.getNotes().hideAllNotes();
      } else if ((categoryItemJ != null && src == categoryItemJ[1]) || (categoryItem != null && src == categoryItem[1])) {
         myCategory.getNotes().unhideAndShowAllNotes();
      } else if ((categoryItemJ != null && src == categoryItemJ[2]) || (categoryItem != null && src == categoryItem[2])) {
         categories.showOnlyNotesOfCategory(myCategory);
      } else if ((categoryItemJ != null && src == categoryItemJ[3]) || (categoryItem != null && src == categoryItem[3])) {
         categories.setDefaultCategory(myCategory);
      } else if ((categoryItemJ != null && src == categoryItemJ[4]) || (categoryItem != null && src == categoryItem[4])) {
         categories.moveCategoryUp(myCategory);
      } else if ((categoryItemJ != null && src == categoryItemJ[5]) || (categoryItem != null && src == categoryItem[5])) {
         String newname = JOptionPane.showInputDialog(this, "Category name:");
         if (newname != null) {
            myCategory.rename(newname);
         }
      } else if ((categoryItemJ != null && src == categoryItemJ[6]) || (categoryItem != null && src == categoryItem[6])) {
         boolean confirmed = true;
         if (categories.getNumberOfCategories() == 1) {
            confirmed = false;
            JOptionPane.showMessageDialog(null, "The last remaining category cannot be removed!","pin 'em up - error",JOptionPane.WARNING_MESSAGE);
         } else if (myCategory.getNotes().getNumberOfNotes() > 0) {
            confirmed = JOptionPane.showConfirmDialog(null, "All " + myCategory.getNotes().getNumberOfNotes() + " notes in this category will be deleted! Proceed?","Remove category",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
         }
         
         if (confirmed) {
            myCategory.getNotes().hideAllNotes();
            categories.remove(myCategory);
            if (myCategory.isDefaultCategory()) {
               categories.getCategory().setDefault(true);
            }
         }
      }
   }
}
