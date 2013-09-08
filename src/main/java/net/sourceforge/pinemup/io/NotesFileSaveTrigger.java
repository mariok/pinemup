package net.sourceforge.pinemup.io;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.xml.stream.XMLStreamException;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.ui.UserInputRetriever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NotesFileSaveTrigger implements Observer {
   private static final int SAVE_DELAY_MILLIS = 5000;

   private static final Logger LOG = LoggerFactory.getLogger(NotesFileSaveTrigger.class);

   private FileSaveThread fileSaveThread;
   private boolean disabled;
   private UserInputRetriever userInputRetriever;

   private static class Holder {
      private static final NotesFileSaveTrigger INSTANCE = new NotesFileSaveTrigger();
   }

   public static NotesFileSaveTrigger getInstance() {
      return Holder.INSTANCE;
   }

   private NotesFileSaveTrigger() {
      super();
   }

   @Override
   public synchronized void update(Observable o, Object arg) {
      if (!disabled && (fileSaveThread == null || !fileSaveThread.isAlive())) {
         fileSaveThread = new FileSaveThread(userInputRetriever);
         fileSaveThread.start();
      }
   }

   public synchronized boolean isDisabled() {
      return disabled;
   }

   public synchronized void setDisabled(boolean disabled) {
      if (fileSaveThread != null && fileSaveThread.isAlive()) {
         try {
            fileSaveThread.join();
         } catch (InterruptedException e) {
            LOG.error("Error while waiting for notesfile save thread.", e);
         }
      }
      this.disabled = disabled;
   }

   public void setUserInputRetriever(UserInputRetriever userInputRetriever) {
      this.userInputRetriever = userInputRetriever;
   }

   private static class FileSaveThread extends Thread {
      private UserInputRetriever userInputRetriever;

      public FileSaveThread(UserInputRetriever userInputRetriever) {
         this.userInputRetriever = userInputRetriever;
      }

      @Override
      public void run() {
         try {
            Thread.sleep(SAVE_DELAY_MILLIS);
            NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
                  UserSettings.getInstance().getNotesFile());
         } catch (InterruptedException e) {
            LOG.error("Error while waiting for notesfile save thread.", e);
         } catch (IOException | XMLStreamException e) {
            userInputRetriever.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                  I18N.getInstance().getString("error.notesfilenotsaved"));
         }
      }
   }
}
