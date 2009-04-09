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

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CategoryManager {
   private LinkedList<Category> categories;
   private static CategoryManager instance;
   
   private CategoryManager() {
      categories = new LinkedList<Category>();
   }
   
   public static CategoryManager getInstance() {
      if (CategoryManager.instance == null) {
         CategoryManager.instance = new CategoryManager();
      }
      return CategoryManager.instance;
   }
   
   public void addCategory(Category c) {
      categories.add(c);
   }
   
   public void removeCategory(Category c) {
      categories.remove(c);
   }
   
   public void hideAllNotes() {
      ListIterator<Category> l = categories.listIterator();
      while (l.hasNext()) {
         l.next().hideAllNotes();
      }
   }
   
   public void showAllNotesNotHidden() {
      ListIterator<Category> l = categories.listIterator();
      while (l.hasNext()) {
         l.next().showAllNotesNotHidden();
      }
   }
   
   public void unhideAndShowAllNotes() {
      ListIterator<Category> l = categories.listIterator();
      while (l.hasNext()) {
         l.next().unhideAndShowAllNotes();
      }
   }
   
   public void updateAllCategoriesInWindows() {
      ListIterator<Category> l = categories.listIterator();
      while (l.hasNext()) {
         l.next().updateCategoryInWindows();
      }
   }
   
   public String[] getCategoryNames() {
      int n = getNumberOfCategories();
      String[] s = new String[n];
      ListIterator<Category> l = categories.listIterator();
      int ni;
      while (l.hasNext()) {
         ni = l.nextIndex();
         s[ni] = l.next().getName();         
      }
      return s;
   }
   
   public int getNumberOfCategories() {
      return categories.size();
   }
   
   public Category getDefaultCategory() {
      Category c = null;
      ListIterator<Category> l = categories.listIterator();
      Category tc;
      while (l.hasNext() && c == null) {
         tc = l.next();
         if (tc.isDefaultCategory()) {
            c = tc;
         }
      }
      return c;
   }
   
   public void setDefaultCategory(Category c) {
      ListIterator<Category> l = categories.listIterator();
      Category tc;
      while (l.hasNext()) {
         tc = l.next();
         if (tc == c) {
            tc.setDefault(true);
         } else {
            tc.setDefault(false);
         }
      }
   }
   
   public void showOnlyNotesOfCategory(Category c) {
      ListIterator<Category> l = categories.listIterator();
      Category tc;
      while (l.hasNext()) {
         tc = l.next();
         if (tc == c) {
            tc.unhideAndShowAllNotes();
         } else {
            tc.hideAllNotes();
         }
      }
   }
   
   public void moveCategoryUp(Category c) {
      int index = categories.indexOf(c);
      if (index>0) {
         categories.set(index, categories.get(index-1));
         categories.set(index-1,c);
      }
   }
   
   public void moveCategoryDown(Category c) {
      int index = categories.indexOf(c);
      if (index<categories.size()-1) {
         categories.set(index, categories.get(index+1));
         categories.set(index+1,c);
      }
   }
   
   public Category getCategoryByNumber(int n) {
      return categories.get(n);
   }
   
   public ListIterator<Category> getListIterator() {
      return categories.listIterator();
   }
   
   public void removeAllCategories() {
      categories.clear();
   }
   
   public void append(List<Category> cl) {
      ListIterator<Category> l = cl.listIterator();
      while (l.hasNext()) {
         categories.add(l.next());
      }
   }
}
