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

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import net.sourceforge.pinemup.io.NotesFileSaveTrigger;
import net.sourceforge.pinemup.ui.PinEmUpUI;
import net.sourceforge.pinemup.ui.swing.NoteWindowManager;

public final class CategoryManager extends Observable {
   private List<Category> categories;

   private static class Holder {
      private static final CategoryManager INSTANCE = new CategoryManager();
   }

   public static CategoryManager getInstance() {
      return Holder.INSTANCE;
   }

   private CategoryManager() {
      categories = new LinkedList<Category>();
      addObserver(NotesFileSaveTrigger.getInstance());
   }

   public void addCategory(Category c) {
      categories.add(c);
      setChanged();
      notifyObservers();
   }

   public void removeCategory(Category c) {
      categories.remove(c);
      setChanged();
      notifyObservers();
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
         setChanged();
         notifyObservers();
      }
   }

   public void moveCategoryDown(Category c) {
      int index = categories.indexOf(c);
      if (index < categories.size() - 1) {
         categories.set(index, categories.get(index + 1));
         categories.set(index + 1, c);
         setChanged();
         notifyObservers();
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

   public List<Note> getAllNotes() {
      List<Note> notes = new LinkedList<Note>();
      for (Category cat : categories) {
         notes.addAll(cat.getNotes());
      }
      return notes;
   }

   public List<Note> getAllVisibleNotes() {
      List<Note> visibleNotes = new LinkedList<Note>();
      for (Category cat : categories) {
         visibleNotes.addAll(cat.getVisibleNotes());
      }
      return visibleNotes;
   }

   public void replaceWithNewCategories(List<Category> newCategories) {
      NotesFileSaveTrigger.getInstance().setDisabled(true);

      // hide all old notes
      hideAllNotes();

      // link and save new notes
      categories.clear();
      categories.addAll(newCategories);

      addNoteWindowManagerForAllNotes();
      notifyAllNotes();

      PinEmUpUI.getUI().refreshCategories();

      NotesFileSaveTrigger.getInstance().setDisabled(false);
   }

   private void addNoteWindowManagerForAllNotes() {
      for (Category cat : categories) {
         for (Note n : cat.getNotes()) {
            n.addObserver(NoteWindowManager.getInstance());
         }
      }
   }

   private void notifyAllNotes() {
      for (Category cat : categories) {
         for (Note n : cat.getNotes()) {
            n.markForObservers();
            n.notifyObservers();
         }
      }
   }

   public void moveNoteToCategory(Note note, int catNumber) {
      Category newCat = getCategoryByNumber(catNumber);
      if (newCat != null) {
         // remove from old category
         if (note.getCategory() != null) {
            note.getCategory().removeNote(note);
         }
         // add to new category
         newCat.addNote(note);
         // set note color to default color of the new category
         note.setColor(newCat.getDefaultNoteColor());
      }
   }

   public Note createNoteAndAddToDefaultCategory() {
      Category defCat = getDefaultCategory();
      Note newNote = new Note();
      defCat.addNote(newNote);
      newNote.setColor(defCat.getDefaultNoteColor());
      newNote.addObserver(NotesFileSaveTrigger.getInstance());
      return newNote;
   }
}
