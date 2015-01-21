package net.sourceforge.pinemup.ui.swing.dialogs;

import net.sourceforge.pinemup.core.io.notes.file.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;

public class DialogFactory {
   private UpdateCheckResultHandler updateCheckResultHandler;

   /** All of these dialogs must only exist once. */
   private SettingsDialog settingsDialogInstance;
   private CategoryDialog categoryDialogInstance;
   private AboutDialog aboutDialogInstance;
   private ExportDialog exportDialogInstance;

   private final NotesFileReader notesFileReader;
   private final NotesFileWriter notesFileWriter;
   private final NotesSaveTrigger notesSaveTrigger;

   public DialogFactory(UpdateCheckResultHandler updateCheckResultHandler, NotesFileReader notesFileReader,
         NotesFileWriter notesFileWriter, NotesSaveTrigger notesSaveTrigger) {
      this.updateCheckResultHandler = updateCheckResultHandler;
      this.notesFileReader = notesFileReader;
      this.notesFileWriter = notesFileWriter;
      this.notesSaveTrigger = notesSaveTrigger;
   }

   public void showSettingsDialog() {
      if (settingsDialogInstance == null || !settingsDialogInstance.isVisible()) {
         settingsDialogInstance = new SettingsDialog(updateCheckResultHandler,
               notesFileReader, notesFileWriter, notesSaveTrigger);
      }
   }

   public void showCategoryDialog() {
      if (categoryDialogInstance == null || !categoryDialogInstance.isVisible()) {
         categoryDialogInstance = new CategoryDialog();
      }
   }

   public void showAboutDialog() {
      if (aboutDialogInstance == null || !aboutDialogInstance.isVisible()) {
         aboutDialogInstance = new AboutDialog();
      }
   }

   public void showExportDialog() {
      if (exportDialogInstance == null || !exportDialogInstance.isVisible()) {
         exportDialogInstance = new ExportDialog();
      }
   }
}
