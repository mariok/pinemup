package net.sourceforge.pinemup.ui.swing.dialogs;

import net.sourceforge.pinemup.io.UpdateCheckResultHandler;
import net.sourceforge.pinemup.ui.UserInputRetriever;

public class DialogFactory {
   private UserInputRetriever userInputRetriever;
   private UpdateCheckResultHandler updateCheckResultHandler;

   private SettingsDialog settingsDialogInstance;

   public void showSettingsDialog() {
      if (settingsDialogInstance == null || !settingsDialogInstance.isVisible()) {
         settingsDialogInstance = new SettingsDialog(userInputRetriever, updateCheckResultHandler);
      }
   }
}
