package net.sourceforge.pinemup.ui.swing.tray;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.notes.file.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.notes.server.ServerCommunicationResultHandler;
import net.sourceforge.pinemup.core.io.notes.server.ServerThread;
import net.sourceforge.pinemup.core.io.notes.stream.NotesReader;
import net.sourceforge.pinemup.core.io.notes.stream.NotesWriter;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckThread;
import net.sourceforge.pinemup.core.settings.UserSettings;
import net.sourceforge.pinemup.ui.swing.dialogs.DialogFactory;
import net.sourceforge.pinemup.ui.swing.utils.DialogUtils;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrayMenuLogic implements ActionListener {
   public static final String ACTION_SHOW_ABOUT_DIALOG = "SHOW_ABOUT_DIALOG";
   public static final String ACTION_SHOW_SETTINGS_DIALOG = "SHOW_SETTINGS_DIALOG";
   public static final String ACTION_EXIT_APPLICATION = "EXIT_APPLICATION";
   public static final String ACTION_UPLOAD_TO_SERVER = "UPLOAD_TO_SERVER";
   public static final String ACTION_DOWNLOAD_FROM_SERVER = "DOWNLOAD_FROM_SERVER";
   public static final String ACTION_EXPORT = "EXPORT";
   public static final String ACTION_MANAGE_CATEGORIES = "MANAGE_CATEGORIES";
   public static final String ACTION_CHECK_FOR_UPDATES = "CHECK_FOR_UPDATES";

   private final DialogFactory dialogFactory;
   private final UpdateCheckResultHandler updateCheckResultHandler;
   private final NotesReader notesReader;
   private final NotesWriter notesWriter;
   private final NotesFileWriter notesFileWriter;
   private final NotesSaveTrigger notesSaveTrigger;

   public TrayMenuLogic(DialogFactory dialogFactory, UpdateCheckResultHandler updateCheckResultHandler, NotesReader notesReader,
         NotesWriter notesWriter, NotesFileWriter notesFileWriter, NotesSaveTrigger notesSaveTrigger) {
      super();
      this.dialogFactory = dialogFactory;
      this.updateCheckResultHandler = updateCheckResultHandler;
      this.notesReader = notesReader;
      this.notesWriter = notesWriter;
      this.notesFileWriter = notesFileWriter;
      this.notesSaveTrigger = notesSaveTrigger;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      switch (e.getActionCommand()) {
      case ACTION_SHOW_ABOUT_DIALOG:
         dialogFactory.showAboutDialog();
         break;
      case ACTION_SHOW_SETTINGS_DIALOG:
         dialogFactory.showSettingsDialog();
         break;
      case ACTION_EXIT_APPLICATION:
         // save notes to xml and exit
         writeCategoriesToFile();
         System.exit(0);
      case ACTION_UPLOAD_TO_SERVER:
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacefileonserver"), I18N.getInstance()
               .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // save notes to xml
            writeCategoriesToFile();

            String serverPassword = getServerPassword();

            // copy xml to server
            new ServerThread(ServerThread.Direction.UPLOAD, notesReader, notesWriter, notesFileWriter, notesSaveTrigger,
                  new SwingServerCommunicationResultHandler(), serverPassword);
         }
         break;
      case ACTION_DOWNLOAD_FROM_SERVER:
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacelocalfile"), I18N.getInstance()
               .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            String serverPassword = getServerPassword();

            new ServerThread(ServerThread.Direction.DOWNLOAD, notesReader, notesWriter, notesFileWriter, notesSaveTrigger,
                  new SwingServerCommunicationResultHandler(), serverPassword);
         }
         break;
      case ACTION_EXPORT:
         dialogFactory.showExportDialog();
         break;
      case ACTION_MANAGE_CATEGORIES:
         dialogFactory.showCategoryDialog();
         break;
      case ACTION_CHECK_FOR_UPDATES:
         new UpdateCheckThread(updateCheckResultHandler);
         break;
      default:
         // unknown action, do nothing
         break;
      }
   }

   private void writeCategoriesToFile() {
      notesFileWriter.writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
            UserSettings.getInstance().getNotesFile());
   }

   private String getServerPassword() {
      String password = "";
      if (UserSettings.getInstance().getStoreServerPass()) {
         password = UserSettings.getInstance().getServerPasswd();
      } else {
         password = DialogUtils.retrievePasswordFromUser();
      }
      return password;
   }

   private static class SwingServerCommunicationResultHandler implements ServerCommunicationResultHandler {
      @Override
      public void onSslError() {
         DialogUtils.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
               I18N.getInstance().getString("error.sslcertificateerror"));
      }

      @Override
      public void onUploadSuccess() {
         DialogUtils.showInfoMessageToUser(I18N.getInstance().getString("info.title"),
               I18N.getInstance().getString("info.notesfileuploaded"));
      }

      @Override
      public void onUploadFailure() {
         DialogUtils.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                I18N.getInstance().getString("error.notesfilenotuploaded"));
      }

      @Override
      public void onDownloadSuccess() {
         DialogUtils.showInfoMessageToUser(I18N.getInstance().getString("info.title"),
               I18N.getInstance().getString("info.notesfiledownloaded"));
      }

      @Override
      public void onDownloadFailure() {
         DialogUtils.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
               I18N.getInstance().getString("error.notesfilenotdownloaded"));
      }
   }
}
