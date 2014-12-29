package net.sourceforge.pinemup.ui.swing;

import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;
import net.sourceforge.pinemup.ui.swing.dialogs.UpdateDialog;
import net.sourceforge.pinemup.ui.swing.utils.DialogUtils;

public class SwingUpdateCheckResultHandler implements UpdateCheckResultHandler {
   private final boolean showUpToDateMessage;

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
         DialogUtils.showInfoMessageToUser(I18N.getInstance().getString("info.title"),
               I18N.getInstance().getString("info.versionuptodate"));
      }
   }
}
