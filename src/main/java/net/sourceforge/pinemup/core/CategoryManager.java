/*
 * pin 'em up
 *
 * Copyright (C) 2007-2013 by Mario Ködding
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

import net.sourceforge.pinemup.core.model.*;
import net.sourceforge.pinemup.core.settings.UserSettings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class CategoryManager {
   /** The pinboard, which contains all notes. **/
   private PinBoard pinBoard = new PinBoard();

   /** Listeners, which will be added per default to new notes. **/
   private final Collection<NoteChangedEventListener> defaultNoteChangedEventListeners = new LinkedList<>();

   /** Listeners, which will be added per default to new categories. **/
   private final Collection<CategoryChangedEventListener> defaultCategoryChangedEventlisteners = new LinkedList<>();
   private final Collection<NoteAddedEventListener> defaultNoteAddedEventListeners = new LinkedList<>();
   private final Collection<NoteRemovedEventListener> defaultNoteRemovedEventListeners = new LinkedList<>();

   /** Listeners, which will be added per default to new pinboards. **/
   private final Collection<CategoryAddedEventListener> defaultCategoryAddedEventListeners = new LinkedList<>();
   private final Collection<CategoryRemovedEventListener> defaultCategoryRemovedEventListeners = new LinkedList<>();

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
      addDefaultCategoryEventListeners(c);
      pinBoard.addCategory(c);
   }

   public void removeCategory(Category c) {
      pinBoard.removeCategory(c);
   }

   public void removeNote(Note note) {
      findCategoryForNote(note).removeNote(note);
   }

   public void hideAllNotes() {
      for (Category cat : pinBoard.getCategories()) {
         cat.hideAllNotes();
      }
   }

   public void unhideAllNotes() {
      for (Category cat : pinBoard.getCategories()) {
         cat.unhideAllNotes();
      }
   }

   public String[] getCategoryNames() {
      int n = getNumberOfCategories();
      String[] s = new String[n];
      int ni = 0;
      for (Category cat : pinBoard.getCategories()) {
         s[ni] = cat.getName();
         ni++;
      }
      return s;
   }

   public int getNumberOfCategories() {
      return pinBoard.getCategories().size();
   }

   public Category getDefaultCategory() {
      for (Category cat : pinBoard.getCategories()) {
         if (cat.isDefaultCategory()) {
            return cat;
         }
      }
      return null;
   }

   public void setDefaultCategory(Category c) {
      c.setDefault(true);
      for (Category cat : pinBoard.getCategories()) {
         if (cat != c) {
            cat.setDefault(false);
         }
      }
   }

   public void showOnlyNotesOfCategory(Category c) {
      c.unhideAllNotes();
      for (Category cat : pinBoard.getCategories()) {
         if (cat != c) {
            cat.hideAllNotes();
         }
      }
   }

   public void moveCategoryUp(Category c) {
      pinBoard.moveCategoryUp(c);
   }

   public void moveCategoryDown(Category c) {
      pinBoard.moveCategoryDown(c);
   }

   public Category getCategoryByNumber(int n) {
      return pinBoard.getCategoryByNumber(n);
   }

   public List<Category> getCategories() {
      return pinBoard.getCategories();
   }

   public void replaceWithNewCategories(List<Category> newCategories) {
      hideAllNotes();

      for (Category category : newCategories) {
         addDefaultCategoryEventListeners(category);

         for (Note note : category.getNotes()) {
            addDefaultNoteEventListeners(note);
         }
      }

      pinBoard = new PinBoard();
      addDefaultPinBoardEventListeners(pinBoard);

      for (Category category : newCategories) {
         pinBoard.addCategory(category);
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
      for (Category category : pinBoard.getCategories()) {
         if (category.containsNote(note)) {
            categoryForNote = category;
            break;
         }
      }
      return categoryForNote;
   }

   public Note createNoteAndAddToDefaultCategory() {
      Category defCat = getDefaultCategory();
      Note newNote = createNoteWithDefaultUserSettings();
      defCat.addNote(newNote);
      newNote.setColor(defCat.getDefaultNoteColor());
      addDefaultNoteEventListeners(newNote);

      return newNote;
   }

   private Note createNoteWithDefaultUserSettings() {
      UserSettings userSettings = UserSettings.getInstance();
      Note newNote = new Note();
      newNote.setSize(userSettings.getDefaultWindowWidth(), userSettings.getDefaultWindowHeight());
      newNote.setPosition(userSettings.getDefaultWindowXPostition(), userSettings.getDefaultWindowYPostition());
      newNote.setFontSize(userSettings.getDefaultFontSize());
      newNote.setAlwaysOnTop(userSettings.getDefaultAlwaysOnTop());
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

   private void addDefaultPinBoardEventListeners(PinBoard pinBoard) {
      for (CategoryAddedEventListener listener : defaultCategoryAddedEventListeners) {
         pinBoard.addCategoryAddedEventListener(listener);
      }
      for (CategoryRemovedEventListener listener : defaultCategoryRemovedEventListeners) {
         pinBoard.addCategoryRemovedEventListener(listener);
      }
   }

   public void registerDefaultNoteChangedEventListener(NoteChangedEventListener listener) {
      defaultNoteChangedEventListeners.add(listener);
   }

   public void registerDefaultNoteAddedEventListener(NoteAddedEventListener listener) {
      defaultNoteAddedEventListeners.add(listener);
   }

   public void registerDefaultNoteRemovedEventListener(NoteRemovedEventListener listener) {
      defaultNoteRemovedEventListeners.add(listener);
   }

   public void registerDefaultCategoryChangedEventListener(CategoryChangedEventListener listener) {
      defaultCategoryChangedEventlisteners.add(listener);
   }

   public void registerDefaultCategoryAddedEventListener(CategoryAddedEventListener listener) {
      defaultCategoryAddedEventListeners.add(listener);
   }

   public void registerDefaultCategoryRemovedEventListener(CategoryRemovedEventListener listener) {
      defaultCategoryRemovedEventListeners.add(listener);
   }
}
