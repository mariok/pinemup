package net.sourceforge.pinemup.core.io.updatecheck;

public interface UpdateCheckResultHandler {
   void handleUpdateFound(String updateMessage);

   void handleNoUpdateFound();
}
