/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2008 by Mario Koedding
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

package net.sourceforge.pinemup.logic;

public class CategoryList {
   private Category category;
   private CategoryList next;
   
   public CategoryList getNext() {
      return next;
   }
   
   public Category getCategory() {
      return category;
   }
   
   public void setCategory(Category c) {
      category = c;
   }
   
   public CategoryList() {
      category = null;
      next = null;
   }
   
   public void add(Category c) {
      if (category == null) { // list is empty
         category = c;
      }
      else {
         if (next == null) {
            next = new CategoryList();
         }
         next.add(c);
      }
   }
   
   public void remove(Category c) {
      if (category == c) { //first category shall be removed
         if (next != null) {
            category = next.getCategory();
            next = next.getNext();
         } else {
            category = null;
         }
      } else if (next != null && next.getCategory() == c) { //category in the middle
         next = next.getNext();
      } else {
         if (next != null) {
            next.remove(c);
         }
      }
   }
   
   public Category getCategoryForNote(Note n) {
      Category c = null;
      if (category.containsNote(n)) {
         c = category;
      } else {
         if (next != null) {
            c = next.getCategoryForNote(n);
         }
      }
      return c;
   }
   
   public void removeAll() {
      category = null;
      next = null;
   }
   
   public void attach(CategoryList cl) {
      if (category == null) { //list is empty
         if (cl != null) {
            category = cl.getCategory();
            next = cl.getNext();
         }
      } else if (next == null) {
         next = cl;
      } else {
         next.attach(cl);
      }
   }
   
   public void hideAllNotes() {
      if (category != null) { //list not empty
         category.hideAllNotes();
         if (next != null) {
            next.hideAllNotes();
         }
      }
   }
   
   public void showAllNotesNotHidden() {
      if (category != null) { //list not empty
         category.showAllNotesNotHidden();
         if (next != null) {
            next.showAllNotesNotHidden();
         }
      }
   }
   
   public void unhideAndShowAllNotes() {
      if (category != null) {
         category.unhideAndShowAllNotes();
         if (next != null) {
            next.unhideAndShowAllNotes();
         }
      }
   }
   
   public void updateAllCategoriesInWindows() {
      if (category != null) {
         category.updateCategoryInWindows();
         if (next != null) {
            next.updateAllCategoriesInWindows();
         }
      }
   }
   
   public String[] getNames() {
      short n = getNumberOfCategories();
      String[] s = new String[n];
      CategoryList cl = this;
      for (int i=0;i<n;i++) {
         if (cl != null) {
            if (cl.getCategory() != null) {
               s[i] = cl.getCategory().getName();
            }
            cl = cl.getNext();
         }
      }
      return s;
   }
   
   public short getNumberOfCategories() {
      short s = 0;
      CategoryList cl = this;
      while (cl != null) {
         s++;
         cl = cl.getNext();
      }
      return s;
   }
   
   public Category getDefaultCategory() {
      Category c = null;
      if (category.isDefaultCategory()) {
         c = category;
      } else {
         if (next != null) {
            c = next.getDefaultCategory();
         }
      }
      return c;
   }
   
   public void setDefaultCategory(Category c) {
      if (category == c) {
         category.setDefault(true);
      } else {
         category.setDefault(false);
      }
      if (next != null) {
         next.setDefaultCategory(c);
      }
   }
   
   public void showOnlyNotesOfCategory(Category c) {
      if (category == c) {
         category.unhideAndShowAllNotes();
      } else {
         category.hideAllNotes();
      }
      if (next != null) {
         next.showOnlyNotesOfCategory(c);
      }
   }
   
   public void moveCategoryUp(Category c) {
      if (category != c) { //not the first category
         if (next != null && next.getCategory() == c) {
            Category temp = category;
            category = next.getCategory();
            next.setCategory(temp);
         } else if (next != null) {
            next.moveCategoryUp(c);
         }
      }
   }
   
   public void moveCategoryDown(Category c) {
      if (next != null) {
         if (category == c) { //current category
            Category temp = category;
            category = next.getCategory();
            next.setCategory(temp);
         } else {
            next.moveCategoryDown(c);
         }
      }
   }
   
   public Category getCategoryByNumber(int n) {
      CategoryList cl = this;
      for (int i=0; i<n; i++) {
         cl = cl.next;
      }
      return cl.category;
   }
}
