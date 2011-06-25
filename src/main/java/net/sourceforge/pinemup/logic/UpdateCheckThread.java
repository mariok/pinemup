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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.gui.I18N;
import net.sourceforge.pinemup.gui.UpdateDialog;

public class UpdateCheckThread extends Thread {
   private static final String UPDATE_URL = "http://pinemup.sourceforge.net/version.php?version=" + PinEmUp.VERSION;
   private boolean showUpToDateMessage;

   public UpdateCheckThread(boolean showUpToDateMessage) {
      super("Update-Check Thread");
      this.showUpToDateMessage = showUpToDateMessage;
      this.start();
   }

   public void run() {
      try {
         URL url = new URL(UPDATE_URL);
         URLConnection urlc = url.openConnection();
         BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

         String versionString = br.readLine();
         if (!versionString.equals(PinEmUp.VERSION)) {
            String changelogString = "<html>";
            changelogString += "<p>" + I18N.getInstance().getString("info.updateavailable.part1") + "</p>";
            changelogString += "<p>" + I18N.getInstance().getString("info.updateavailable.part2") + " " + PinEmUp.VERSION + "<br />";
            changelogString += I18N.getInstance().getString("info.updateavailable.part3") + " " + versionString + "</p>";
            changelogString += "<p>" + I18N.getInstance().getString("info.updateavailable.part4") + " <a href=\"http://pinemup.sourceforge.net\">http://pinemup.sourceforge.net</a></p>";
            changelogString += "<p>&nbsp;</p>";
            changelogString += "<p>Changelog:<br />";
            changelogString += "--------------------------------</p><p>";
            boolean firstList = true;
            String nextLine;
            do {
               nextLine = br.readLine();
               if (nextLine != null) {
                  if (nextLine.startsWith("-")) {
                     changelogString += "<li>" + nextLine.substring(2) + "</li>";
                  } else {
                     if (!firstList) {
                        changelogString += "</ul>";
                     } else {
                        firstList = false;
                     }
                     changelogString += nextLine + "<ul>";
                  }

               }
            } while (nextLine != null);
            changelogString += "</p></html>";

            new UpdateDialog(changelogString);
         } else if (showUpToDateMessage) {
            JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.versionuptodate"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
         }
         br.close();
      } catch (IOException e) {
         //do nothing
      }

   }

}