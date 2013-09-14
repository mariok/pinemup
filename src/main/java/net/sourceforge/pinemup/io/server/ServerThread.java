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

package net.sourceforge.pinemup.io.server;

import java.io.IOException;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;
import javax.xml.stream.XMLStreamException;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;
import net.sourceforge.pinemup.io.NotesFileSaveTrigger;
import net.sourceforge.pinemup.ui.UserInputRetriever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerThread extends Thread {
   private static final Logger LOG = LoggerFactory.getLogger(ServerThread.class);

   public static final boolean UPLOAD = true;
   public static final boolean DOWNLOAD = false;

   private boolean upload;
   private UserInputRetriever userInputRetriever;

   public ServerThread(boolean upload, UserInputRetriever userInputRetriever) {
      super("Server Up-/Download Thread");
      this.upload = upload;
      this.userInputRetriever = userInputRetriever;
      this.start();
   }

   public void run() {
      if (upload == ServerThread.UPLOAD) {

         boolean uploadSuccessful;
         try {
            uploadSuccessful = ServerConnection.createServerConnection(UserSettings.getInstance().getServerType(), getServerPassword())
                  .exportNotesToServer(CategoryManager.getInstance().getCategories());
         } catch (SSLHandshakeException e) {
            LOG.error("SSL exception occured. Probably a self-signed certificate?", e);

            userInputRetriever.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                  I18N.getInstance().getString("error.sslcertificateerror"));
            uploadSuccessful = false;
         } catch (IOException | XMLStreamException e) {
            uploadSuccessful = false;
         }

         if (uploadSuccessful) {
            userInputRetriever.showInfoMessageToUser(I18N.getInstance().getString("info.title"),
                  I18N.getInstance().getString("info.notesfileuploaded"));
         } else {
            userInputRetriever.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                  I18N.getInstance().getString("error.notesfilenotuploaded"));
         }
      } else {
         List<Category> categoriesFromServer;

         try {
            categoriesFromServer = ServerConnection.createServerConnection(UserSettings.getInstance().getServerType(), getServerPassword())
                  .importCategoriesFromServer();
         } catch (SSLHandshakeException e) {
            // certificate error (self-signed?)
            userInputRetriever.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                  I18N.getInstance().getString("error.sslcertificateerror"));
            categoriesFromServer = null;
         } catch (IOException e) {
            categoriesFromServer = null;
         }

         if (categoriesFromServer != null) {
            NotesFileSaveTrigger.getInstance().setDisabled(true);
            CategoryManager.getInstance().replaceWithNewCategories(categoriesFromServer);
            NotesFileSaveTrigger.getInstance().setDisabled(false);
            try {
               NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
                     UserSettings.getInstance().getNotesFile());
            } catch (IOException | XMLStreamException e) {
               userInputRetriever.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.notesfilenotsaved"));
            }
            userInputRetriever.showInfoMessageToUser(I18N.getInstance().getString("info.title"),
                  I18N.getInstance().getString("info.notesfiledownloaded"));
         } else {
            userInputRetriever.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                  I18N.getInstance().getString("error.notesfilenotdownloaded"));
         }
      }
   }

   private String getServerPassword() {
      String password = "";
      if (UserSettings.getInstance().getStoreServerPass()) {
         password = UserSettings.getInstance().getServerPasswd();
      } else {
         if (userInputRetriever != null) {
            password = userInputRetriever.retrievePasswordFromUser();
         }
      }
      return password;
   }

}
