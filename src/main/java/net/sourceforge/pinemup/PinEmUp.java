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

package net.sourceforge.pinemup;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;
import net.sourceforge.pinemup.io.NotesFileSaveTrigger;
import net.sourceforge.pinemup.io.UpdateCheckThread;
import net.sourceforge.pinemup.ui.swing.SwingUI;
import net.sourceforge.pinemup.ui.swing.SwingUpdateCheckResultHandler;
import net.sourceforge.pinemup.ui.swing.notewindow.NoteWindowManager;

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

      // initialize UI
      SwingUI.initialize();
      NotesFileSaveTrigger.getInstance().setUserInputRetriever(SwingUI.getUserInputRetriever());

      // make sure the currently saved notesfile is valid
      UserSettings.getInstance().setNotesFile(
            NotesFileManager.getInstance().makeSureNotesFileIsValid(UserSettings.getInstance().getNotesFile(),
                  SwingUI.getUserInputRetriever()));

      // add NotesFileSaveTrigger as observer for notes and categories
      CategoryManager.getInstance().addCategoriesObserver(NotesFileSaveTrigger.getInstance());
      CategoryManager.getInstance().addDefaultNoteObserver(NotesFileSaveTrigger.getInstance());

      CategoryManager.getInstance().addDefaultNoteObserver(NoteWindowManager.getInstance());

      // load notes from file
      CategoryManager.getInstance().replaceWithNewCategories(
            NotesFileManager.getInstance().readCategoriesFromFile(UserSettings.getInstance().getNotesFile()));

      // update check
      if (UserSettings.getInstance().isUpdateCheckEnabled()) {
         new UpdateCheckThread(new SwingUpdateCheckResultHandler(false));
      }
   }
}
