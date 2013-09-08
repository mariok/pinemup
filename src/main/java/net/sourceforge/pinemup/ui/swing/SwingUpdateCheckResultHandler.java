package net.sourceforge.pinemup.ui.swing;

import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.io.UpdateCheckResultHandler;
import net.sourceforge.pinemup.ui.swing.dialogs.UpdateDialog;

public class SwingUpdateCheckResultHandler implements UpdateCheckResultHandler {
   private boolean showUpToDateMessage;

   public SwingUpdateCheckResultHandler(boolean showUpToDateMessage) {
      super();
      this.showUpToDateMessage = showUpToDateMessage;
   }

   @Override
   public void handleUpdateFound(String updateMessage) {
      new UpdateDialog(updateMessage);
   }

   @Override
   public void handleNoUpdateFound() {
      if (showUpToDateMessage) {
         SwingUI.getUserInputRetriever().showInfoMessageToUser(I18N.getInstance().getString("info.title"),
               I18N.getInstance().getString("info.versionuptodate"));
      }
   }
}
