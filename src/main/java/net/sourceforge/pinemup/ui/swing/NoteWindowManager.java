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

package net.sourceforge.pinemup.ui.swing;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.Note;

public class NoteWindowManager {
   private static NoteWindowManager instance = new NoteWindowManager();

   private List<NoteWindow> noteWindows;

   private NoteWindowManager() {

   }

   public static NoteWindowManager getInstance() {
      return NoteWindowManager.instance;
   }

   public void createNoteWindowsForAllVisibleNotes() {
      noteWindows = new LinkedList<NoteWindow>();

      List<Note> visibleNotes = CategoryManager.getInstance().getAllVisibleNotes();
      for (Note note : visibleNotes) {
         createNoteWindowForNote(note);
      }
   }

   public NoteWindow createNoteWindowForNote(Note note) {
      NoteWindow window = new NoteWindow(note);
      noteWindows.add(window);
      return window;
   }

   public void removeNoteWindow(NoteWindow noteWindow) {
      noteWindows.remove(noteWindow);
   }
}
