package net.sourceforge.pinemup.core.io.notes.file;

import net.sourceforge.pinemup.core.io.notes.stream.NotesWriter;
import net.sourceforge.pinemup.core.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class NotesFileWriter {
   private static final Logger LOG = LoggerFactory.getLogger(NotesFileWriter.class);

   private final NotesWriter notesWriter;

   private NotesFileWriterResultHandler notesFileWriterResultHandler;

   public void setNotesFileWriterResultHandler(NotesFileWriterResultHandler notesFileWriterResultHandler) {
      this.notesFileWriterResultHandler = notesFileWriterResultHandler;
   }

   public NotesFileWriter(NotesWriter notesWriter) {
      this.notesWriter = notesWriter;
   }

   public boolean writeCategoriesToFile(List<Category> categories, String filePath) {
      boolean writtenSuccessfully = false;

      LOG.debug("writing notes to file...");

      try (FileOutputStream f = new FileOutputStream(filePath)) {
         writtenSuccessfully = notesWriter.writeCategoriesToOutputStream(categories, f);
         if (notesFileWriterResultHandler != null) {
            notesFileWriterResultHandler.onFileWrittenSuccessfully();
         }
      } catch (IOException e) {
         LOG.error("Exception when trying to write the notes to a file. File was NOT written.");
         if (notesFileWriterResultHandler != null) {
            notesFileWriterResultHandler.onFileWriteError();
         }
      }

      return writtenSuccessfully;
   }
}
