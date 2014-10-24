package net.sourceforge.pinemup.ui.swing.dialogs;

import net.sourceforge.pinemup.core.io.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.resources.ResourceLoader;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;
import net.sourceforge.pinemup.core.UserInputRetriever;

public class DialogFactory {
   private UserInputRetriever userInputRetriever;
   private UpdateCheckResultHandler updateCheckResultHandler;

   private SettingsDialog settingsDialogInstance;

   private final NotesFileReader notesFileReader;
   private final NotesFileWriter notesFileWriter;
   private final NotesSaveTrigger notesSaveTrigger;
   private final ResourceLoader resourceLoader;

   public DialogFactory(UserInputRetriever userInputRetriever,
         UpdateCheckResultHandler updateCheckResultHandler, NotesFileReader notesFileReader,
         NotesFileWriter notesFileWriter, NotesSaveTrigger notesSaveTrigger,
         ResourceLoader resourceLoader) {
      this.userInputRetriever = userInputRetriever;
      this.updateCheckResultHandler = updateCheckResultHandler;
      this.notesFileReader = notesFileReader;
      this.notesFileWriter = notesFileWriter;
      this.notesSaveTrigger = notesSaveTrigger;
      this.resourceLoader = resourceLoader;
   }

   public void showSettingsDialog() {
      if (settingsDialogInstance == null || !settingsDialogInstance.isVisible()) {
         settingsDialogInstance = new SettingsDialog(userInputRetriever, updateCheckResultHandler,
               notesFileReader, notesFileWriter, notesSaveTrigger, resourceLoader);
      }
   }
}
