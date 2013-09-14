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

package net.sourceforge.pinemup.core;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class CategoryManager {
   private List<Category> categories = new LinkedList<>();

   /* Listeners, which will be added per default to new notes. */
   private Collection<NoteChangedEventListener> defaultNoteChangedEventListeners = new LinkedList<>();

   /* Listeners, which will be added per default to new categories. */
   private Collection<CategoryChangedEventListener> defaultCategoryChangedEventlisteners = new LinkedList<>();
   private Collection<NoteAddedEventListener> defaultNoteAddedEventListeners = new LinkedList<>();
   private Collection<NoteRemovedEventListener> defaultNoteRemovedEventListeners = new LinkedList<>();

   private Collection<CategoryAddedEventListener> categoryAddedEventListeners = new LinkedList<>();

   private Collection<CategoryRemovedEventListener> categoryRemovedEventListeners = new LinkedList<>();

   private static class Holder {
      private static final CategoryManager INSTANCE = new CategoryManager();
   }

   public static CategoryManager getInstance() {
      return Holder.INSTANCE;
   }

   private CategoryManager() {
      super();
   }

   public void addCategory(Category c) {
      categories.add(c);
      addDefaultCategoryEventListeners(c);
      fireCategoryAddedEvent(c);
   }

   public void removeCategory(Category c) {
      categories.remove(c);
      fireCategoryRemovedEvent(c);
   }

   public void removeNote(Note note) {
      findCategoryForNote(note).removeNote(note);
   }

   public void hideAllNotes() {
      for (Category cat : categories) {
         cat.hideAllNotes();
      }
   }

   public void unhideAllNotes() {
      for (Category cat : categories) {
         cat.unhideAllNotes();
      }
   }

   public String[] getCategoryNames() {
      int n = getNumberOfCategories();
      String[] s = new String[n];
      int ni = 0;
      for (Category cat : categories) {
         s[ni] = cat.getName();
         ni++;
      }
      return s;
   }

   public int getNumberOfCategories() {
      return categories.size();
   }

   public Category getDefaultCategory() {
      for (Category cat : categories) {
         if (cat.isDefaultCategory()) {
            return cat;
         }
      }
      return null;
   }

   public void setDefaultCategory(Category c) {
      c.setDefault(true);
      for (Category cat : categories) {
         if (cat != c) {
            cat.setDefault(false);
         }
      }
   }

   public void showOnlyNotesOfCategory(Category c) {
      c.unhideAllNotes();
      for (Category cat : categories) {
         if (cat != c) {
            cat.hideAllNotes();
         }
      }
   }

   public void moveCategoryUp(Category c) {
      int index = categories.indexOf(c);
      if (index > 0) {
         categories.set(index, categories.get(index - 1));
         categories.set(index - 1, c);
         fireCategoryRemovedEvent(c);
         fireCategoryAddedEvent(c);
      }
   }

   public void moveCategoryDown(Category c) {
      int index = categories.indexOf(c);
      if (index < categories.size() - 1) {
         categories.set(index, categories.get(index + 1));
         categories.set(index + 1, c);
         fireCategoryRemovedEvent(c);
         fireCategoryAddedEvent(c);
      }
   }

   public Category getCategoryByNumber(int n) {
      if (n < 0 || n >= categories.size()) {
         return null;
      }
      return categories.get(n);
   }

   public List<Category> getCategories() {
      return categories;
   }

   public void replaceWithNewCategories(List<Category> newCategories) {
      hideAllNotes();
      categories.clear();
      categories.addAll(newCategories);

      for (Category category : newCategories) {
         addDefaultCategoryEventListeners(category);

         for (Note note : category.getNotes()) {
            addDefaultNoteEventListeners(note);
         }
      }

      for (Category category : newCategories) {
         fireCategoryAddedEvent(category);

         for (Note note : category.getNotes()) {
            category.fireNoteAddedEvent(note);
         }
      }
   }

   public void moveNoteToCategory(Note note, int catNumber) {
      Category newCat = getCategoryByNumber(catNumber);
      if (newCat != null) {
         // remove from old category
         Category oldCat = findCategoryForNote(note);
         if (oldCat != null) {
            oldCat.removeNote(note);
         }
         // add to new category
         newCat.addNote(note);
         // set note color to default color of the new category
         note.setColor(newCat.getDefaultNoteColor());
      }
   }

   public Category findCategoryForNote(Note note) {
      Category categoryForNote = null;
      for (Category category : categories) {
         if (category.containsNote(note)) {
            categoryForNote = category;
            break;
         }
      }
      return categoryForNote;
   }

   public Note createNoteAndAddToDefaultCategory() {
      Category defCat = getDefaultCategory();
      Note newNote = new Note();
      defCat.addNote(newNote);
      newNote.setColor(defCat.getDefaultNoteColor());
      addDefaultNoteEventListeners(newNote);

      return newNote;
   }

   private void addDefaultNoteEventListeners(Note note) {
      for (NoteChangedEventListener listener : defaultNoteChangedEventListeners) {
         note.addNoteChangedEventListener(listener);
      }
   }

   private void addDefaultCategoryEventListeners(Category category) {
      for (CategoryChangedEventListener listener : defaultCategoryChangedEventlisteners) {
         category.addCategoryChangedEventListener(listener);
      }
      for (NoteAddedEventListener listener : defaultNoteAddedEventListeners) {
         category.addNoteAddedEventListener(listener);
      }
      for (NoteRemovedEventListener listener : defaultNoteRemovedEventListeners) {
         category.addNoteRemovedEventListener(listener);
      }
   }

   public void registerDefaultNoteChangedEventListener(NoteChangedEventListener listener) {
      defaultNoteChangedEventListeners.add(listener);
   }

   public void registerDefaultCategoryChangedEventListener(CategoryChangedEventListener listener) {
      defaultCategoryChangedEventlisteners.add(listener);
   }

   public void registerDefaultNoteAddedEventListener(NoteAddedEventListener listener) {
      defaultNoteAddedEventListeners.add(listener);
   }

   public void registerDefaultNoteRemovedEventListener(NoteRemovedEventListener listener) {
      defaultNoteRemovedEventListeners.add(listener);
   }

   public void registerCategoryAddedEventListener(CategoryAddedEventListener listener) {
      categoryAddedEventListeners.add(listener);
   }

   public void registerCategoryRemovedEventListener(CategoryRemovedEventListener listener) {
      categoryRemovedEventListeners.add(listener);
   }

   private void fireCategoryAddedEvent(Category category) {
      for (CategoryAddedEventListener listener : categoryAddedEventListeners) {
         listener.categoryAdded(new CategoryAddedEvent(this, category));
      }
   }

   private void fireCategoryRemovedEvent(Category category) {
      for (CategoryRemovedEventListener listener : categoryRemovedEventListeners) {
         listener.categoryRemoved(new CategoryRemovedEvent(this, category));
      }
   }
}
