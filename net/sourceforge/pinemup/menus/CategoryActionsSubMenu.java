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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import net.sourceforge.pinemup.logic.*;

public class CategoryActionsSubMenu extends JMenu implements ActionListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   private JMenuItem hideCategoryItem, showCategoryItem, showOnlyCategoryItem, setTempDefItem, renameCategoryItem, removeCategoryItem,moveCategoryUpItem;

   private Category myCategory;
   
   private CategoryList categories;
   
   public CategoryActionsSubMenu(String title, Category c, CategoryList cl) {
      super(title);
      myCategory = c;
      categories = cl;
      
      hideCategoryItem = new JMenuItem("hide notes of this category");
      showCategoryItem = new JMenuItem("show all notes of this category");
      showOnlyCategoryItem = new JMenuItem("show only notes of this category");
      setTempDefItem = new JMenuItem("set as default");
      moveCategoryUpItem = new JMenuItem("move up in list");
      renameCategoryItem = new JMenuItem("rename this category");
      removeCategoryItem = new JMenuItem("remove this category");

      hideCategoryItem.addActionListener(this);
      showOnlyCategoryItem.addActionListener(this);
      showCategoryItem.addActionListener(this);
      setTempDefItem.addActionListener(this);
      moveCategoryUpItem.addActionListener(this);
      renameCategoryItem.addActionListener(this);
      removeCategoryItem.addActionListener(this);
      
      add(hideCategoryItem);
      add(showCategoryItem);
      add(showOnlyCategoryItem);
      addSeparator();
      add(setTempDefItem);
      add(moveCategoryUpItem);
      add(renameCategoryItem);
      addSeparator();
      add(removeCategoryItem);
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == hideCategoryItem) {
         myCategory.getNotes().hideAllNotes();
      } else if (src == showCategoryItem) {
         myCategory.getNotes().unhideAndShowAllNotes();
      } else if (src == showOnlyCategoryItem) {
         categories.showOnlyNotesOfCategory(myCategory);
      } else if (src == setTempDefItem) {
         categories.setDefaultCategory(myCategory);
      } else if (src == moveCategoryUpItem) {
         categories.moveCategoryUp(myCategory);
      } else if (src == renameCategoryItem) {
         String newname = JOptionPane.showInputDialog(this, "Category name:");
         if (newname != null) {
            myCategory.rename(newname);
         }
      } else if (src == removeCategoryItem) {
         boolean confirmed = true;
         if (categories.getNumberOfCategories() == 1) {
            confirmed = false;
            JOptionPane.showMessageDialog(this, "The last remaining category cannot be removed!","pin 'em up - error",JOptionPane.WARNING_MESSAGE);
         } else if (myCategory.getNotes().getNumberOfNotes() > 0) {
            confirmed = JOptionPane.showConfirmDialog(this, "all " + myCategory.getNotes().getNumberOfNotes() + " notes in this category will be deleted! Proceed?","Remove Category",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
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
