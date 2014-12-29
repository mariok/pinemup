package net.sourceforge.pinemup.core.io.file;

public interface FileWriterResultHandler {
   void onFileWrittenSuccessfully();

   void onFileWriteError();
}
