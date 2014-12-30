package net.sourceforge.pinemup.core.io.notes.file;

public interface NotesFileWriterResultHandler {
   void onFileWrittenSuccessfully();

   void onFileWriteError();
}
