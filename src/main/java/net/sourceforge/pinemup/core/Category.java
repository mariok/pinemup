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

public class Category {
   private String name;
   private List<Note> notes;
   private boolean defaultCategory;
   private byte defaultNoteColor;

   public Category(String name, boolean def, byte defNoteColor) {
      this.name = name;
      this.defaultCategory = def;
      this.defaultNoteColor = defNoteColor;
      notes = new LinkedList<Note>();
   }

   public String getName() {
      return name;
   }

   public void rename(String s) {
      name = s;
      notifyNoteObservers();
   }

   public boolean isDefaultCategory() {
      return defaultCategory;
   }

   public void setDefault(boolean b) {
      defaultCategory = b;
   }

   public void setDefaultNoteColor(byte c) {
      defaultNoteColor = c;
   }

   public byte getDefaultNoteColor() {
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
   }

   public void removeNote(Note n) {
      n.setCategory(null);
      notes.remove(n);
   }

   public void unhideAndShowAllNotes() {
      for (Note note : notes) {
         note.setHidden(false);
      }
   }

   private void notifyNoteObservers() {
      for (Note note : notes) {
         note.notifyObservers();
      }
   }
}
