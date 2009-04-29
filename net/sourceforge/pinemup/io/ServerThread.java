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

package net.sourceforge.pinemup.io;

import java.util.List;

import net.sourceforge.pinemup.logic.Category;
import net.sourceforge.pinemup.logic.CategoryManager;
import net.sourceforge.pinemup.logic.NoteIO;
import net.sourceforge.pinemup.logic.UserSettings;

public class ServerThread extends Thread {
   public static final boolean UPLOAD = true;
   public static final boolean DOWNLOAD = false;
   
   private boolean upload;
   
   public ServerThread(boolean upload) {
      super("Server Up-/Download Thread");
      this.upload = upload;
      this.start();
   }
   
   public void run() {
      if (upload == ServerThread.UPLOAD) { // upload notes
         ServerConnection.createServerConnection(UserSettings.getInstance().getServerType()).exportNotesToServer();
      } else { // download notes
         // download Notes
         ServerConnection.createServerConnection(UserSettings.getInstance().getServerType()).importNotesFromServer();
         //load new file
         List<Category> newCats = NoteIO.readCategoriesFromFile();
         // If successfull downloaded, replace:
         // hide notes
         CategoryManager.getInstance().hideAllNotes();
         
         // link and save new notes
         CategoryManager.getInstance().removeAllCategories();
         CategoryManager.getInstance().append(newCats);
         
         // show all notes which are not hidden
         CategoryManager.getInstance().showAllNotesNotHidden();
         
         //save to file
         NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());
      }
   }

}
