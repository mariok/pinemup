package net.sourceforge.pinemup.ui.swing.tray;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.UserInputRetriever;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.server.ServerThread;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckThread;
import net.sourceforge.pinemup.core.settings.UserSettings;
import net.sourceforge.pinemup.ui.swing.dialogs.AboutDialog;
import net.sourceforge.pinemup.ui.swing.dialogs.CategoryDialog;
import net.sourceforge.pinemup.ui.swing.dialogs.DialogFactory;
import net.sourceforge.pinemup.ui.swing.dialogs.ExportDialog;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
   private final UserInputRetriever userInputRetriever;
   private final UpdateCheckResultHandler updateCheckResultHandler;
   private final NotesFileReader notesFileReader;
   private final NotesFileWriter notesFileWriter;
   private final NotesSaveTrigger notesSaveTrigger;

   public TrayMenuLogic(DialogFactory dialogFactory, UserInputRetriever userInputRetriever,
         UpdateCheckResultHandler updateCheckResultHandler, NotesFileReader notesFileReader,
         NotesFileWriter notesFileWriter, NotesSaveTrigger notesSaveTrigger) {
      super();
      this.dialogFactory = dialogFactory;
      this.userInputRetriever = userInputRetriever;
      this.updateCheckResultHandler = updateCheckResultHandler;
      this.notesFileReader = notesFileReader;
      this.notesFileWriter = notesFileWriter;
      this.notesSaveTrigger = notesSaveTrigger;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      switch (e.getActionCommand()) {
      case ACTION_SHOW_ABOUT_DIALOG:
         new AboutDialog();
         break;
      case ACTION_SHOW_SETTINGS_DIALOG:
         dialogFactory.showSettingsDialog();
         break;
      case ACTION_EXIT_APPLICATION:
         // save notes to file and exit
         writeCategoriesToFile();
         System.exit(0);
      case ACTION_UPLOAD_TO_SERVER:
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacefileonserver"), I18N.getInstance()
               .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // save notes to file
            writeCategoriesToFile();
            // copy file to server
            new ServerThread(ServerThread.Direction.UPLOAD, userInputRetriever, notesFileReader, notesFileWriter, notesSaveTrigger);
         }
         break;
      case ACTION_DOWNLOAD_FROM_SERVER:
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacelocalfile"), I18N.getInstance()
               .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            new ServerThread(ServerThread.Direction.DOWNLOAD, userInputRetriever, notesFileReader, notesFileWriter, notesSaveTrigger);
         }
         break;
      case ACTION_EXPORT:
         new ExportDialog();
         break;
      case ACTION_MANAGE_CATEGORIES:
         CategoryDialog.showInstance();
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
      try {
         notesFileWriter.writeCategoriesToFile(CategoryManager.getInstance().getCategories(),
               UserSettings.getInstance().getNotesFile());
      } catch (IOException | XMLStreamException e) {
         userInputRetriever.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
               I18N.getInstance().getString("error.notesfilenotsaved"));
      }
   }
}
