package net.sourceforge.pinemup.core.io;

public interface UpdateCheckResultHandler {
   void handleUpdateFound(String updateMessage);

   void handleNoUpdateFound();
}
