/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2009 by Mario Ködding
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

import net.sourceforge.pinemup.gui.I18N;
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

   private static String[] basicItemText = {
      I18N.getInstance().getString("menu.addnoteitem"),
      I18N.getInstance().getString("menu.showallnotesitem"),
      I18N.getInstance().getString("menu.hideallnotesitem")
   };
   
   private static String[] categoryItemText = {
      I18N.getInstance().getString("menu.categorymenu.hidenotes"),
      I18N.getInstance().getString("menu.categorymenu.shownotes"),
      I18N.getInstance().getString("menu.categorymenu.showonlynotes"),
      I18N.getInstance().getString("menu.categorymenu.setasdefault")
   };
   
   public JMenuItem[] getBasicJMenuItems() {
      basicItemJ = new JMenuItem[basicItemText.length];
      for (int i=0; i<basicItemText.length; i++) {
         basicItemJ[i] = new JMenuItem(basicItemText[i]);
         basicItemJ[i].addActionListener(this);
      }
      return basicItemJ;
   }
   
   public MenuItem[] getBasicMenuItems() {
      basicItem = new MenuItem[basicItemText.length];
      for (int i=0; i<basicItemText.length; i++) {
         basicItem[i] = new MenuItem(basicItemText[i]);
         basicItem[i].addActionListener(this);
      }
      return basicItem;
   }   
   
   public JMenu getCategoryActionsJMenu(String title, Category c) {
      JMenu menu = new JMenu(title);
      categoryItemJ = new CategoryJMenuItem[categoryItemText.length];
      for (int i=0; i<categoryItemText.length; i++) {
         categoryItemJ[i] = new CategoryJMenuItem(categoryItemText[i],c);
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
      categoryItem = new CategoryMenuItem[categoryItemText.length];
      for (int i=0; i<categoryItemText.length; i++) {
         categoryItem[i] = new CategoryMenuItem(categoryItemText[i],c);
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
         Category defCat = CategoryManager.getInstance().getDefaultCategory();
         if (defCat != null) {
            Note newNote = new Note("",defCat.getDefaultNoteColor());
            defCat.addNote(newNote);
            newNote.showIfNotHidden();
            newNote.jumpInto();
         }
      } else if ((basicItem != null && src == basicItem[1]) || (basicItemJ != null && src == basicItemJ[1])) {
         CategoryManager.getInstance().unhideAndShowAllNotes();
      } else if ((basicItem != null && src == basicItem[2]) || (basicItemJ != null && src == basicItemJ[2])) {
         CategoryManager.getInstance().hideAllNotes();
      } else if ((categoryItem != null && src == categoryItem[0]) || (categoryItemJ != null && src == categoryItemJ[0])) {
         ((MenuItemWithCategory)src).getCategory().hideAllNotes();
      } else if ((categoryItem != null && src == categoryItem[1]) || (categoryItemJ != null && src == categoryItemJ[1])) {
         ((MenuItemWithCategory)src).getCategory().unhideAndShowAllNotes();
      } else if ((categoryItem != null && src == categoryItem[2]) || (categoryItemJ != null && src == categoryItemJ[2])) {
         CategoryManager.getInstance().showOnlyNotesOfCategory(((MenuItemWithCategory)src).getCategory());
      } else if ((categoryItem != null && src == categoryItem[3]) || (categoryItemJ != null && src == categoryItemJ[3])) {
         CategoryManager.getInstance().setDefaultCategory(((MenuItemWithCategory)src).getCategory());
      }
      
      // save notes to file after every change
      NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());
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
