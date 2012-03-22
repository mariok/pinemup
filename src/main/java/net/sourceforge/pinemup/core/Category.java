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

public class Category extends Observable {
   private String name;
   private List<Note> notes;
   private boolean defaultCategory;
   private NoteColor defaultNoteColor;

   public Category(String name, boolean def, NoteColor defNoteColor) {
      this.name = name;
      this.defaultCategory = def;
      this.defaultNoteColor = defNoteColor;
      notes = new LinkedList<Note>();
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      if (!name.equals(this.name)) {
         this.name = name;
         notifyNotes();
      }
   }

   public boolean isDefaultCategory() {
      return defaultCategory;
   }

   public void setDefault(boolean b) {
      if (b != defaultCategory) {
         defaultCategory = b;
         setChanged();
         notifyObservers();
      }
   }

   public void setDefaultNoteColor(NoteColor c) {
      if (!c.equals(defaultNoteColor)) {
         defaultNoteColor = c;
         setChanged();
         notifyObservers();
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

   public List<Note> getNotes() {
      return notes;
   }

   public List<Note> getVisibleNotes() {
      List<Note> visibleNotes = new LinkedList<Note>();
      for (Note note : notes) {
         if (!note.isHidden()) {
            visibleNotes.add(note);
         }
      }
      return visibleNotes;
   }

   public void addNote(Note n) {
      n.setCategory(this);
      notes.add(n);
      setChanged();
      notifyObservers();
   }

   public void removeNote(Note n) {
      n.setCategory(null);
      notes.remove(n);
      setChanged();
      notifyObservers();
   }

   public void unhideAllNotes() {
      for (Note note : notes) {
         note.setHidden(false);
      }
   }

   private void notifyNotes() {
      for (Note note : notes) {
         note.refreshCategoryInfo();
      }
   }
}
