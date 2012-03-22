package net.sourceforge.pinemup.io;

import java.util.Observable;
import java.util.Observer;

import net.sourceforge.pinemup.core.CategoryManager;

public class NotesFileSaveTrigger implements Observer {
   private static final int SAVE_DELAY_MILLIS = 5000;

   private static NotesFileSaveTrigger instance = new NotesFileSaveTrigger();

   private FileSaveThread fileSaveThread;
   private boolean disabled;

   private NotesFileSaveTrigger() {

   }

   public static NotesFileSaveTrigger getInstance() {
      return instance;
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
            e.printStackTrace();
         }
      }
      this.disabled = disabled;
   }

   class FileSaveThread extends Thread {
      @Override
      public void run() {
         try {
            Thread.sleep(SAVE_DELAY_MILLIS);
            NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories());
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }
}
