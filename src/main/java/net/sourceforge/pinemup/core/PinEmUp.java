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

import net.sourceforge.pinemup.io.NotesFileManager;
import net.sourceforge.pinemup.ui.PinEmUpUI;
import net.sourceforge.pinemup.ui.swing.SwingUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PinEmUp {
   public static final String VERSION;
   private static final int STARTUP_SLEEP_TIME = 1000;

   private static final Logger LOG = LoggerFactory.getLogger(PinEmUp.class);

   static {
      if (PinEmUp.class.getPackage().getImplementationVersion() != null) {
         VERSION = PinEmUp.class.getPackage().getImplementationVersion();
      } else {
         VERSION = "dev-SNAPSHOT";
      }
   }

   private PinEmUp() {

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

      // create Swing UI
      PinEmUpUI.setUI(new SwingUI());
      PinEmUpUI.getUI().initialize();

      // make sure the currently saved notesfile is not invalid
      UserSettings.getInstance().makeSureNotesFileIsValid();

      // load notes from file
      CategoryManager.getInstance().replaceWithNewCategories(
            NotesFileManager.getInstance().readCategoriesFromFile(UserSettings.getInstance().getNotesFile()));

      // update check
      if (UserSettings.getInstance().isUpdateCheckEnabled()) {
         new UpdateCheckThread(false);
      }
   }
}
