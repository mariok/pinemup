package net.sourceforge.pinemup.core.io.notes.server;

public interface ServerCommunicationResultHandler {
   void onSslError();

   void onUploadSuccess();

   void onUploadFailure();

   void onDownloadSuccess();

   void onDownloadFailure();
}
