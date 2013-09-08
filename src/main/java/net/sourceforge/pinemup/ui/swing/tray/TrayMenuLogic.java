package net.sourceforge.pinemup.ui.swing.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;
import net.sourceforge.pinemup.io.UpdateCheckResultHandler;
import net.sourceforge.pinemup.io.UpdateCheckThread;
import net.sourceforge.pinemup.io.server.ServerThread;
import net.sourceforge.pinemup.ui.UserInputRetriever;
import net.sourceforge.pinemup.ui.swing.dialogs.AboutDialog;
import net.sourceforge.pinemup.ui.swing.dialogs.CategoryDialog;
import net.sourceforge.pinemup.ui.swing.dialogs.DialogFactory;
import net.sourceforge.pinemup.ui.swing.dialogs.ExportDialog;

public class TrayMenuLogic implements ActionListener {
   public static final String ACTION_SHOW_ABOUT_DIALOG = "SHOW_ABOUT_DIALOG";
   public static final String ACTION_SHOW_SETTINGS_DIALOG = "SHOW_SETTINGS_DIALOG";
   public static final String ACTION_EXIT_APPLICATION = "EXIT_APPLICATION";
   public static final String ACTION_UPLOAD_TO_SERVER = "UPLOAD_TO_SERVER";
   public static final String ACTION_DOWNLOAD_FROM_SERVER = "DOWNLOAD_FROM_SERVER";
   public static final String ACTION_EXPORT = "EXPORT";
   public static final String ACTION_MANAGE_CATEGORIES = "MANAGE_CATEGORIES";
   public static final String ACTION_CHECK_FOR_UPDATES = "CHECK_FOR_UPDATES";

   private DialogFactory dialogFactory;
   private UserInputRetriever userInputRetriever;
   private UpdateCheckResultHandler updateCheckResultHandler;

   public TrayMenuLogic(DialogFactory dialogFactory, UserInputRetriever userInputRetriever,
         UpdateCheckResultHandler updateCheckResultHandler) {
      super();
      this.dialogFactory = dialogFactory;
      this.userInputRetriever = userInputRetriever;
      this.updateCheckResultHandler = updateCheckResultHandler;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      String action = e.getActionCommand();
      if (ACTION_SHOW_ABOUT_DIALOG.equals(action)) {
         new AboutDialog();
      } else if (ACTION_SHOW_SETTINGS_DIALOG.equals(action)) {
         dialogFactory.showSettingsDialog();
      } else if (ACTION_EXIT_APPLICATION.equals(action)) {
         // save notes to file and exit
         writeCategoriesToFile();
         System.exit(0);
      } else if (ACTION_UPLOAD_TO_SERVER.equals(action)) {
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacefileonserver"), I18N.getInstance()
                     .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // save notes to file
            writeCategoriesToFile();
            // copy file to server
            new ServerThread(ServerThread.UPLOAD, userInputRetriever);
         }
      } else if (ACTION_DOWNLOAD_FROM_SERVER.equals(action)) {
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacelocalfile"), I18N.getInstance()
                     .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            new ServerThread(ServerThread.DOWNLOAD, userInputRetriever);
         }
      } else if (ACTION_EXPORT.equals(action)) {
         new ExportDialog();
      } else if (ACTION_MANAGE_CATEGORIES.equals(action)) {
         CategoryDialog.showInstance();
      } else if (ACTION_CHECK_FOR_UPDATES.equals(action)) {
         new UpdateCheckThread(updateCheckResultHandler);
      }
   }

   private void writeCategoriesToFile() {
      try {
         NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
               UserSettings.getInstance().getNotesFile());
      } catch (IOException | XMLStreamException e) {
         userInputRetriever.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
               I18N.getInstance().getString("error.notesfilenotsaved"));
      }
   }
}
