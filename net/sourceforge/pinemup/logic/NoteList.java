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

package net.sourceforge.pinemup.logic;

public class NoteList {
  private Note note;
  private NoteList next;
  
  public Note getNote() {
     return note;
  }
  
  public NoteList getNext() {
     return next;
  }
  
  public NoteList() {
     note = null;
     next = null;
  }
    
  public void add(Note n) {
     if (note == null) { // list is empty
        note = n;
     }
     else {
        if (next == null) {
           next = new NoteList();
        }
        next.add(n);
     }
  }
  
  public void removeWithoutHiding(Note n) {
     if (note == n) { //current note shall be removed
        if (next != null) {
           note = next.getNote();
           next = next.getNext();           
        } else {
           note = null;
        }
     } else {
        if (next != null) {
           next.removeWithoutHiding(n);
        }
     }
  }
  
  public void remove(Note n) {
     if (note == n) { //current note shall be removed
        note.hide();
        if (next != null) {
           note = next.getNote();
           next = next.getNext();           
        } else {
           note = null;
        }
     } else {
        if (next != null) {
           next.remove(n);
        }
     }
  }
  
  public void unhideAndShowAllNotes() {
     if (note != null) {
        note.unhideAndShow();
     }
     if (next != null) {
        next.unhideAndShowAllNotes();
     }
  }
  
  public void showAllNotesNotHidden() {
     if (note != null) {
        note.showIfNotHidden();
     }
     if (next != null) {
        next.showAllNotesNotHidden();
     }
  }
  
  public void hideAllNotes() {
     if (note != null) {
        note.hide();
     }
     if (next != null) {
        next.hideAllNotes();
     }
  }
  
  public boolean noteInList(Note n) {
     boolean b = false;
     if (note == n) {
        b = true;
     } else {
        if (next != null) {
           b = next.noteInList(n);
        }
     }
     return b;
  }
  
  public void updateAllCategoryNamesInWindows() {
     if (note != null) {
        note.updateCategoryNameInWindow();
     }
     if (next != null) {
        next.updateAllCategoryNamesInWindows();
     }
  }
  
  public int getNumberOfNotes() {
     int n = 0;
     NoteList temp = this;
     while (temp != null) {
        if (temp.getNote() != null) {
           n++;
        }
        temp = temp.getNext();
     }
     return n;
  }
  
}
