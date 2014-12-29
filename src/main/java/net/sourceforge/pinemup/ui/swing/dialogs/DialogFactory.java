package net.sourceforge.pinemup.ui.swing.dialogs;

import net.sourceforge.pinemup.core.io.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;

public class DialogFactory {
   private UpdateCheckResultHandler updateCheckResultHandler;

   private SettingsDialog settingsDialogInstance;

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
}
