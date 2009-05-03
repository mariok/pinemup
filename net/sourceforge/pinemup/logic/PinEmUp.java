/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2009 by Mario Ködding
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

import java.awt.*;
import javax.swing.*;

import net.sourceforge.pinemup.gui.I18N;

public class PinEmUp {
   private static final String VERSION = "0.5-svn";

   public PinEmUp() {
      if (SystemTray.isSupported()) {
         //set locale
         I18N.getInstance().setLocale(UserSettings.getInstance().getLocale());
         
         //load notes from file
         CategoryManager.getInstance().append(NoteIO.readCategoriesFromFile());
         
         // add trayicon
         SystemTray tray = SystemTray.getSystemTray();
         try {
            tray.add(PinEmUpTrayIcon.getInstance());
         } catch (AWTException e) {
            System.err.println(e);
         }

         //show all notes that are set visible
         CategoryManager.getInstance().showAllNotesNotHidden();
         
         //udate check
         if (UserSettings.getInstance().isUpdateCheckEnabled()) {
            new UpdateCheckThread(false);
         }
      } else { //tray icon not supported
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.trayiconnotsupported"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }

   public static void main(String args[]) {
      new PinEmUp();
   }
   
   public static String getVersion() {
      return VERSION;
   }
}
