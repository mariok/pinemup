package net.sourceforge.pinemup.core.io.server;

public interface ServerCommunicationResultHandler {
   void onSslError();

   void onUploadSuccess();

   void onUploadFailure();

   void onDownloadSuccess();

   void onDownloadFailure();
}
