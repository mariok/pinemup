package net.sourceforge.pinemup.core.io.notes.file;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.model.*;
import net.sourceforge.pinemup.core.settings.UserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NotesSaveTrigger implements CategoryChangedEventListener, NoteChangedEventListener, NoteAddedEventListener,
      NoteRemovedEventListener, CategoryAddedEventListener, CategoryRemovedEventListener {
   private static final Logger LOG = LoggerFactory.getLogger(NotesSaveTrigger.class);

   private static final int SAVE_DELAY_MILLIS = 5000;

   private FileSaveThread fileSaveThread;
   private boolean disabled;

   private final NotesFileWriter notesFileWriter;

   public NotesSaveTrigger(NotesFileWriter notesFileWriter) {
      super();
      this.notesFileWriter = notesFileWriter;
   }

   @Override
   public void noteChanged(NoteChangedEvent event) {
      LOG.debug("Received NoteChangedEvent.");
      triggerSave();
   }

   @Override
   public void categoryChanged(CategoryChangedEvent event) {
      LOG.debug("Received CategoryChangedEvent.");
      triggerSave();
   }

   @Override
   public void categoryRemoved(CategoryRemovedEvent event) {
      LOG.debug("Received CategoryRemovedEvent.");
      triggerSave();
   }

   @Override
   public void categoryAdded(CategoryAddedEvent event) {
      LOG.debug("Received CategoryAddedEvent.");
      triggerSave();
   }

   @Override
   public void noteRemoved(NoteRemovedEvent event) {
      LOG.debug("Received NoteRemovedEvent.");
      triggerSave();
   }

   @Override
   public void noteAdded(NoteAddedEvent event) {
      LOG.debug("Received NoteAddedEvent.");
      triggerSave();
   }

   private synchronized void triggerSave() {
      if (!disabled && (fileSaveThread == null || !fileSaveThread.isAlive())) {
         fileSaveThread = new FileSaveThread(notesFileWriter);
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
      private final NotesFileWriter notesFileWriter;

      public FileSaveThread(NotesFileWriter notesFileWriter) {
         this.notesFileWriter = notesFileWriter;
      }

      @Override
      public void run() {
         try {
            Thread.sleep(SAVE_DELAY_MILLIS);
            notesFileWriter.writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
                  UserSettings.getInstance().getNotesFile());
         } catch (InterruptedException e) {
            LOG.error("Error while waiting for notesfile save thread.", e);
         }
      }
   }
}
