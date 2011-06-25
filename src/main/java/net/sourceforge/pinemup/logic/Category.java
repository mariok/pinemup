/*
 * pin 'em up
 *
 * Copyright (C) 2007-2011 by Mario Ködding
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
import java.util.ListIterator;

public class Category {
   private String name;
   private LinkedList<Note> notes;
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
      updateCategoryInWindows();
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
      ListIterator<Note> l = notes.listIterator();
      while (l.hasNext()) {
         l.next().hide();
      }
   }

   public ListIterator<Note> getListIterator() {
      return notes.listIterator();
   }

   public void addNote(Note n) {
      n.setCategory(this);
      notes.add(n);
   }

   public void removeNote(Note n) {
      n.setCategory(null);
      notes.remove(n);
   }

   public void showAllNotesNotHidden() {
      ListIterator<Note> l = notes.listIterator();
      while (l.hasNext()) {
         l.next().showIfNotHidden();
      }
   }

   public void unhideAndShowAllNotes() {
      ListIterator<Note> l = notes.listIterator();
      while (l.hasNext()) {
         l.next().unhideAndShow();
      }
   }

   public void updateCategoryInWindows() {
      ListIterator<Note> l = notes.listIterator();
      while (l.hasNext()) {
         l.next().updateCategoryInWindow();
      }
   }
}
