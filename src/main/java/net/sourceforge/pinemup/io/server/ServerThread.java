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

package net.sourceforge.pinemup.io.server;

import java.util.List;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;

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
      if (upload == ServerThread.UPLOAD) {
         boolean uploadSuccessful = ServerConnection.createServerConnection(UserSettings.getInstance().getServerType())
               .exportNotesToServer(CategoryManager.getInstance().getCategories());

         if (uploadSuccessful) {
            UserSettings
                  .getInstance()
                  .getUserInputRetriever()
                  .showInfoMessageToUser(I18N.getInstance().getString("info.title"), I18N.getInstance().getString("info.notesfileuploaded"));
         } else {
            UserSettings
                  .getInstance()
                  .getUserInputRetriever()
                  .showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                        I18N.getInstance().getString("error.notesfilenotuploaded"));
         }
      } else {
         List<Category> categoriesFromServer = ServerConnection.createServerConnection(UserSettings.getInstance().getServerType())
               .importCategoriesFromServer();

         if (categoriesFromServer != null) {
            CategoryManager.getInstance().replaceWithNewCategories(categoriesFromServer);
            NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
                  UserSettings.getInstance().getNotesFile());
            UserSettings
                  .getInstance()
                  .getUserInputRetriever()
                  .showInfoMessageToUser(I18N.getInstance().getString("info.title"),
                        I18N.getInstance().getString("info.notesfiledownloaded"));
         } else {
            UserSettings
                  .getInstance()
                  .getUserInputRetriever()
                  .showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                        I18N.getInstance().getString("error.notesfilenotdownloaded"));
         }
      }
   }

}
