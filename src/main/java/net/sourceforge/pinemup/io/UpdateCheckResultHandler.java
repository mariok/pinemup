package net.sourceforge.pinemup.io;

public interface UpdateCheckResultHandler {
   void handleUpdateFound(String updateMessage);

   void handleNoUpdateFound();
}
