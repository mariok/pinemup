package net.sourceforge.pinemup.io;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import net.sourceforge.pinemup.core.CategoryAddedEvent;
import net.sourceforge.pinemup.core.CategoryAddedEventListener;
import net.sourceforge.pinemup.core.CategoryChangedEvent;
import net.sourceforge.pinemup.core.CategoryChangedEventListener;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.CategoryRemovedEvent;
import net.sourceforge.pinemup.core.CategoryRemovedEventListener;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.NoteAddedEvent;
import net.sourceforge.pinemup.core.NoteAddedEventListener;
import net.sourceforge.pinemup.core.NoteChangedEvent;
import net.sourceforge.pinemup.core.NoteChangedEventListener;
import net.sourceforge.pinemup.core.NoteRemovedEvent;
import net.sourceforge.pinemup.core.NoteRemovedEventListener;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.ui.UserInputRetriever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NotesFileSaveTrigger implements CategoryChangedEventListener, NoteChangedEventListener, NoteAddedEventListener,
      NoteRemovedEventListener, CategoryAddedEventListener, CategoryRemovedEventListener {
   private static final Logger LOG = LoggerFactory.getLogger(NotesFileSaveTrigger.class);

   private static final int SAVE_DELAY_MILLIS = 5000;

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
