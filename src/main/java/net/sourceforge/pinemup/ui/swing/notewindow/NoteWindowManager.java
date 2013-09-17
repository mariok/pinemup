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

package net.sourceforge.pinemup.ui.swing.notewindow;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.core.NoteAddedEvent;
import net.sourceforge.pinemup.core.NoteAddedEventListener;
import net.sourceforge.pinemup.core.NoteChangedEvent;
import net.sourceforge.pinemup.core.NoteChangedEventListener;
import net.sourceforge.pinemup.core.NoteRemovedEvent;
import net.sourceforge.pinemup.core.NoteRemovedEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NoteWindowManager implements NoteChangedEventListener, NoteAddedEventListener, NoteRemovedEventListener {
   private static final Logger LOG = LoggerFactory.getLogger(NoteWindowManager.class);

   private Map<Note, NoteWindow> noteWindows;

   public NoteWindowManager() {
      noteWindows = new HashMap<>();
   }

   public void bringAllWindowsToFront() {
      for (NoteWindow window : noteWindows.values()) {
         window.toFront();
      }
   }

   @Override
   public void noteChanged(NoteChangedEvent event) {
      LOG.debug("Received NoteChangedEvent.");
      Note n = event.getSource();
      if (n.isHidden()) {
         closeAndRemoveNoteWindow(n);
      } else {
         NoteWindow window = noteWindows.get(n);
         if (window == null) {
            NoteWindow noteWindow = createNoteWindowForNote(n);
            noteWindow.jumpIntoTextArea();
         }
      }
   }

   @Override
   public void noteRemoved(NoteRemovedEvent event) {
      LOG.debug("Received NoteRemovedEvent.");
      Note n = event.getRemovedNote();
      Category category = event.getSource();
      category.removeCategoryChangedEventListener(noteWindows.get(n));
      closeAndRemoveNoteWindow(n);
   }

   @Override
   public void noteAdded(NoteAddedEvent event) {
      LOG.debug("Received NoteAddedEvent.");
      Note n = event.getAddedNote();
      if (!n.isHidden()) {
         NoteWindow noteWindow = createNoteWindowForNote(n);
         noteWindow.jumpIntoTextArea();
      }
   }

   private void closeAndRemoveNoteWindow(Note n) {
      NoteWindow window = noteWindows.get(n);
      if (window != null) {
         noteWindows.remove(n);
         n.removeNoteChangedEventListener(window);
         window.setVisible(false);
         window.dispose();
      }
   }

   private NoteWindow createNoteWindowForNote(Note note) {
      NoteWindow window = new NoteWindow(note);
      noteWindows.put(note, window);
      note.addNoteChangedEventListener(window);
      Category category = CategoryManager.getInstance().findCategoryForNote(note);
      category.addCategoryChangedEventListener(window);
      return window;
   }
}
