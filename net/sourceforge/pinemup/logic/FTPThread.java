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

public class FTPThread extends Thread {
   private boolean upload;
   private CategoryList categories;
   private UserSettings settings;
   
   public FTPThread(boolean upload, CategoryList c, UserSettings s) {
      super("FTP-Up-/Download Thread");
      this.upload = upload;      
      this.categories = c;
      this.settings = s;
      this.start();
   }
   
   public void run() {
      if (upload) { // upload notes
         NoteIO.writeCategoriesToFTP(categories, settings.getNotesFile());
      } else { // download notes
         // download Notes
         //TODO: Dateiname übergeben
         CategoryList newCats = NoteIO.getCategoriesFromFTP(settings.getNotesFile());
         // If successfull downloaded, replace:
         // hide notes
         categories.hideAllNotes();
         
         // link and save new notes
         categories.removeAll();
         categories.attach(newCats);
         
         // show all notes which are not hidden
         categories.showAllNotesNotHidden();
         
         //save to file
         NoteIO.writeCategoriesToFile(categories, settings);
      }
   }

}
