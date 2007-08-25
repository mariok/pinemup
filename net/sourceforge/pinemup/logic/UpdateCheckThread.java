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

import java.io.*;
import java.net.*;
import javax.swing.*;

import net.sourceforge.pinemup.gui.*;




public class UpdateCheckThread extends Thread {
   private static final String updateURL = "http://pinemup.sourceforge.net/version.php?version=" + PinEmUp.getVersion();
   private boolean showUpToDateMessage;
   
   public UpdateCheckThread(boolean showUpToDateMessage) {
      super("Update-Check Thread");
      this.showUpToDateMessage = showUpToDateMessage;
      this.start();
   }
   
   public void run() {
      try {
         URL url = new URL(updateURL);
         URLConnection urlc = url.openConnection();
         BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
         
         String versionString = br.readLine();
         if (!versionString.equals(PinEmUp.getVersion())) {
            String changelogString = "<html>"; 
            changelogString += "<p>THERE IS AN UPDATE AVAILABLE FOR PIN 'EM UP!</p>";
            changelogString += "<p>Your version of pin 'em up is " + PinEmUp.getVersion() + "<br />";
            changelogString += "The latest version available is " + versionString + "</p>";
            changelogString += "<p>The new version can be downloaded at <a href=\"http://pinemup.sourceforge.net\">http://pinemup.sourceforge.net</a></p>";
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
            JOptionPane.showMessageDialog(null, "Your version of pin 'em up is up to date!", "pin 'em up - information", JOptionPane.INFORMATION_MESSAGE);
         }
         br.close();
      } catch (IOException e) {
         //do nothing
      }

   }

}