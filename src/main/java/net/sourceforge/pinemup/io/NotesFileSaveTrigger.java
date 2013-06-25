package net.sourceforge.pinemup.io;

import java.util.Observable;
import java.util.Observer;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.UserSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NotesFileSaveTrigger implements Observer {
   private static final int SAVE_DELAY_MILLIS = 5000;

   private static final Logger LOG = LoggerFactory.getLogger(NotesFileSaveTrigger.class);

   private FileSaveThread fileSaveThread;
   private boolean disabled;

   private static class Holder {
      private static final NotesFileSaveTrigger INSTANCE = new NotesFileSaveTrigger();
   }

   public static NotesFileSaveTrigger getInstance() {
      return Holder.INSTANCE;
   }

   private NotesFileSaveTrigger() {

   }

   @Override
   public synchronized void update(Observable o, Object arg) {
      if (!disabled && (fileSaveThread == null || !fileSaveThread.isAlive())) {
         fileSaveThread = new FileSaveThread();
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

   private static class FileSaveThread extends Thread {
      @Override
      public void run() {
         try {
            Thread.sleep(SAVE_DELAY_MILLIS);
            NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
                  UserSettings.getInstance().getNotesFile());
         } catch (InterruptedException e) {
            LOG.error("Error while waiting for notesfile save thread.", e);
         }
      }
   }
}
