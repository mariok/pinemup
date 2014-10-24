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

package net.sourceforge.pinemup.core.io.server;

import java.io.IOException;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;
import javax.xml.stream.XMLStreamException;

import net.sourceforge.pinemup.core.io.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.file.NotesFileWriter;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.settings.UserSettings;
import net.sourceforge.pinemup.core.io.NotesSaveTrigger;
import net.sourceforge.pinemup.core.UserInputRetriever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerThread extends Thread {
   public enum Direction {
      UPLOAD,
      DOWNLOAD
   }

   private static final Logger LOG = LoggerFactory.getLogger(ServerThread.class);

   private final Direction direction;
   private final UserInputRetriever userInputRetriever;
   private final NotesFileReader notesFileReader;
   private final NotesFileWriter notesFileWriter;
   private final NotesSaveTrigger notesSaveTrigger;

   public ServerThread(Direction direction, UserInputRetriever userInputRetriever,
         NotesFileReader notesFileReader, NotesFileWriter notesFileWriter, NotesSaveTrigger notesSaveTrigger) {
      super("Server Up-/Download Thread");
      this.direction = direction;
      this.userInputRetriever = userInputRetriever;
      this.notesFileReader = notesFileReader;
      this.notesFileWriter = notesFileWriter;
      this.notesSaveTrigger = notesSaveTrigger;
      this.start();
   }

   public void run() {
      if (direction == Direction.UPLOAD) {
         boolean uploadSuccessful;
         try {
            uploadSuccessful = ServerConnectionFactory.createServerConnection(
                  UserSettings.getInstance().getServerType(), getServerPassword(), notesFileReader, notesFileWriter)
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
            categoriesFromServer = ServerConnectionFactory.createServerConnection(
                  UserSettings.getInstance().getServerType(), getServerPassword(), notesFileReader, notesFileWriter)
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
            notesSaveTrigger.setDisabled(true);
            CategoryManager.getInstance().replaceWithNewCategories(categoriesFromServer);
            notesSaveTrigger.setDisabled(false);
            try {
               notesFileWriter.writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
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
