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
   
   private CategoryJMenuItem[] categoryItemJ = null;
   private CategoryMenuItem[] categoryItem = null;
   private JMenuItem[] basicItemJ = null;
   private MenuItem[] basicItem = null;

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
      "set as default"
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
      categoryItemJ = new CategoryJMenuItem[CATEGORYITEMTEXT.length];
      for (int i=0; i<CATEGORYITEMTEXT.length; i++) {
         categoryItemJ[i] = new CategoryJMenuItem(CATEGORYITEMTEXT[i],c);
         categoryItemJ[i].addActionListener(this);
         menu.add(categoryItemJ[i]);
         switch(i) {
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
      categoryItem = new CategoryMenuItem[CATEGORYITEMTEXT.length];
      for (int i=0; i<CATEGORYITEMTEXT.length; i++) {
         categoryItem[i] = new CategoryMenuItem(CATEGORYITEMTEXT[i],c);
         categoryItem[i].addActionListener(this);
         menu.add(categoryItem[i]);
         switch(i) {
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
         Category defCat = categories.getDefaultCategory();
         if (defCat != null) {
            Note newNote = new Note("",settings,categories,defCat.getDefaultNoteColor());
            defCat.getNotes().add(newNote);
            newNote.showIfNotHidden();
            newNote.jumpInto();
         }
      } else if ((basicItem != null && src == basicItem[1]) || (basicItemJ != null && src == basicItemJ[1])) {
         categories.unhideAndShowAllNotes();
      } else if ((basicItem != null && src == basicItem[2]) || (basicItemJ != null && src == basicItemJ[2])) {
         categories.hideAllNotes();
      } else if ((categoryItem != null && src == categoryItem[0]) || (categoryItemJ != null && src == categoryItemJ[0])) {
         ((MenuItemWithCategory)src).getCategory().getNotes().hideAllNotes();
      } else if ((categoryItem != null && src == categoryItem[1]) || (categoryItemJ != null && src == categoryItemJ[1])) {
         ((MenuItemWithCategory)src).getCategory().getNotes().unhideAndShowAllNotes();
      } else if ((categoryItem != null && src == categoryItem[2]) || (categoryItemJ != null && src == categoryItemJ[2])) {
         categories.showOnlyNotesOfCategory(((MenuItemWithCategory)src).getCategory());
      } else if ((categoryItem != null && src == categoryItem[3]) || (categoryItemJ != null && src == categoryItemJ[3])) {
         categories.setDefaultCategory(((MenuItemWithCategory)src).getCategory());
      }
      
      // save notes to file after every change
      NoteIO.writeCategoriesToFile(categories, settings);
   }
   
class CategoryMenuItem extends MenuItem implements MenuItemWithCategory {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private Category myCat;
   
   public CategoryMenuItem(String title, Category c) {
      super(title);
      myCat = c;
   }
   
   public Category getCategory() {
      return myCat;
   }
}

class CategoryJMenuItem extends JMenuItem implements MenuItemWithCategory {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private Category myCat;
   
   public CategoryJMenuItem(String title, Category c) {
      super(title);
      myCat = c;
   }
   
   public Category getCategory() {
      return myCat;
   }
}

interface MenuItemWithCategory {
   public Category getCategory();
}

}
