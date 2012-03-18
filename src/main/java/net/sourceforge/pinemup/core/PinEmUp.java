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

import net.sourceforge.pinemup.ui.PinEmUpUI;
import net.sourceforge.pinemup.ui.swing.SwingUI;

public final class PinEmUp {
   public static final String VERSION;
   private static final int STARTUP_SLEEP_TIME = 1000;

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
      // wait for a moment for SystemTray to be initialized
      // (to prevent problems with autostart on some systems)
      try {
         Thread.sleep(STARTUP_SLEEP_TIME);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      // create Swing UI
      PinEmUpUI pinEmUpUi = new SwingUI();
      pinEmUpUi.initializeUI();
   }
}
