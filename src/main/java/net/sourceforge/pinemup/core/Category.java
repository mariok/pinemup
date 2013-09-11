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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Category {
   private String name;
   private Set<Note> notes;
   private boolean defaultCategory;
   private NoteColor defaultNoteColor;

   private List<CategoryChangedEventListener> categoryChangedEventListeners = new LinkedList<>();
   private List<NoteAddedEventListener> noteAddedEventListeners = new LinkedList<>();
   private List<NoteRemovedEventListener> noteRemovedEventListeners = new LinkedList<>();

   public void addCategoryChangedEventListener(CategoryChangedEventListener listener) {
      categoryChangedEventListeners.add(listener);
   }

   public void removeCategoryChangedEventListener(CategoryChangedEventListener listener) {
      categoryChangedEventListeners.remove(listener);
   }

   public void addNoteAddedEventListener(NoteAddedEventListener listener) {
      noteAddedEventListeners.add(listener);
   }

   public void removeNoteAddedEventListener(NoteAddedEventListener listener) {
      noteAddedEventListeners.remove(listener);
   }

   public void addNoteRemovedEventListener(NoteRemovedEventListener listener) {
      noteRemovedEventListeners.add(listener);
   }

   public void removeNoteRemovedEventListener(NoteRemovedEventListener listener) {
      noteRemovedEventListeners.remove(listener);
   }

   public Category(String name, boolean def, NoteColor defNoteColor) {
      this.name = name;
      this.defaultCategory = def;
      this.defaultNoteColor = defNoteColor;
      notes = new HashSet<>();
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      if (!name.equals(this.name)) {
         this.name = name;
         fireCategoryChangedEvent();
      }
   }

   public boolean isDefaultCategory() {
      return defaultCategory;
   }

   public void setDefault(boolean b) {
      if (b != defaultCategory) {
         defaultCategory = b;
         fireCategoryChangedEvent();
      }
   }

   public void setDefaultNoteColor(NoteColor c) {
      if (!c.equals(defaultNoteColor)) {
         defaultNoteColor = c;
         fireCategoryChangedEvent();
      }
   }

   public NoteColor getDefaultNoteColor() {
      return defaultNoteColor;
   }

   public int getNumberOfNotes() {
      return notes.size();
   }

   public void hideAllNotes() {
      for (Note note : notes) {
         note.setHidden(true);
      }
   }

   public Set<Note> getNotes() {
      return notes;
   }

   public void addNote(Note n) {
      notes.add(n);
      fireNoteAddedEvent(n);
   }

   public void removeNote(Note n) {
      notes.remove(n);
      fireNoteRemovedEvent(n);
   }

   public void unhideAllNotes() {
      for (Note note : notes) {
         note.setHidden(false);
      }
   }

   public boolean containsNote(Note note) {
      return notes.contains(note);
   }

   public void fireCategoryChangedEvent() {
      for (CategoryChangedEventListener listener : categoryChangedEventListeners) {
         listener.categoryChanged(new CategoryChangedEvent(this));
      }
   }

   public void fireNoteAddedEvent(Note note) {
      for (NoteAddedEventListener listener : noteAddedEventListeners) {
         listener.noteAdded(new NoteAddedEvent(this, note));
      }
   }

   public void fireNoteRemovedEvent(Note note) {
      for (NoteRemovedEventListener listener : noteRemovedEventListeners) {
         listener.noteRemoved(new NoteRemovedEvent(this, note));
      }
   }
}
