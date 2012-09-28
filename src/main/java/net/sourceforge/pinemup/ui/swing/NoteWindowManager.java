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

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.sourceforge.pinemup.core.Note;

public class NoteWindowManager implements Observer {
   private Map<Note, NoteWindow> noteWindows;

   private static class Holder {
      private static final NoteWindowManager INSTANCE = new NoteWindowManager();
   }

   public static NoteWindowManager getInstance() {
      return Holder.INSTANCE;
   }

   private NoteWindowManager() {
      noteWindows = new HashMap<Note, NoteWindow>();
   }

   public NoteWindow createNoteWindowForNote(Note note) {
      NoteWindow window = new NoteWindow(note);
      noteWindows.put(note, window);
      return window;
   }

   public void removeNoteWindow(NoteWindow window) {
      if (window != null) {
         noteWindows.remove(window.getParentNote());
      }
   }

   public void bringAllWindowsToFront() {
      for (NoteWindow window : noteWindows.values()) {
         window.toFront();
      }
   }

   @Override
   public void update(Observable o, Object arg) {
      if (o instanceof Note) {
         Note n = (Note)o;
         if (!n.isHidden()) {
            n.deleteObserver(this);
            createNoteWindowForNote(n);
         }
      }
   }
}
