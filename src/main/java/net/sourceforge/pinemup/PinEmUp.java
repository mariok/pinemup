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

package net.sourceforge.pinemup;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.notes.file.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.notes.stream.NotesReader;
import net.sourceforge.pinemup.core.io.notes.stream.NotesWriter;
import net.sourceforge.pinemup.core.io.notes.stream.xml.NotesXmlReader;
import net.sourceforge.pinemup.core.io.notes.stream.xml.NotesXmlWriter;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckThread;
import net.sourceforge.pinemup.core.settings.UserSettings;
import net.sourceforge.pinemup.ui.PinEmUpUi;
import net.sourceforge.pinemup.ui.swing.SwingUI;
import net.sourceforge.pinemup.ui.swing.SwingUpdateCheckResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PinEmUp {
   private static final int STARTUP_SLEEP_TIME = 1000;

   private static final Logger LOG = LoggerFactory.getLogger(PinEmUp.class);

   private PinEmUp() {
      super();
   }

   public static void main(String[] args) {
      // wait for a moment for SystemTray to be initialized (to prevent problems
      // with autostart on some systems)
      try {
         LOG.info("pin 'em up is starting up. Waiting a few seconds before initializing the tray icon...");
         Thread.sleep(STARTUP_SLEEP_TIME);
      } catch (InterruptedException e) {
         LOG.error("Startup sleep time has been interrupted.", e);
      }

      // set locale
      I18N.getInstance().setLocale(UserSettings.getInstance().getLocale());

      // initialize IO
      NotesReader notesReader = new NotesXmlReader();
      NotesWriter notesWriter = new NotesXmlWriter();
      NotesFileReader notesFileReader = new NotesFileReader(notesReader);
      NotesFileWriter notesFileWriter = new NotesFileWriter(notesWriter);
      NotesSaveTrigger notesSaveTrigger = new NotesSaveTrigger(notesFileWriter);

      // initialize UI
      PinEmUpUi pinEmUpUi = new SwingUI(notesReader, notesWriter, notesFileReader, notesFileWriter, notesSaveTrigger);
      pinEmUpUi.initialize();

      // make sure the currently saved notesfile is valid
      UserSettings.getInstance().setNotesFile(
            pinEmUpUi.makeSureNotesFileIsValid(UserSettings.getInstance().getNotesFile()));

      // add NotesFileSaveTrigger as default listeners for notes / categories
      CategoryManager.getInstance().registerDefaultCategoryChangedEventListener(notesSaveTrigger);
      CategoryManager.getInstance().registerDefaultCategoryAddedEventListener(notesSaveTrigger);
      CategoryManager.getInstance().registerDefaultCategoryRemovedEventListener(notesSaveTrigger);
      CategoryManager.getInstance().registerDefaultNoteChangedEventListener(notesSaveTrigger);
      CategoryManager.getInstance().registerDefaultNoteAddedEventListener(notesSaveTrigger);
      CategoryManager.getInstance().registerDefaultNoteRemovedEventListener(notesSaveTrigger);

      // load notes from xml
      CategoryManager.getInstance().replaceWithNewCategories(
            notesFileReader.readCategoriesFromFile(UserSettings.getInstance().getNotesFile()));

      // update check
      if (UserSettings.getInstance().isUpdateCheckEnabled()) {
         new UpdateCheckThread(new SwingUpdateCheckResultHandler(false));
      }
   }
}
