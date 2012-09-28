package net.sourceforge.pinemup.ui.swing.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.core.UpdateCheckThread;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;
import net.sourceforge.pinemup.io.ServerThread;
import net.sourceforge.pinemup.ui.swing.AboutDialog;
import net.sourceforge.pinemup.ui.swing.CategoryDialog;
import net.sourceforge.pinemup.ui.swing.ExportDialog;
import net.sourceforge.pinemup.ui.swing.NoteWindow;
import net.sourceforge.pinemup.ui.swing.NoteWindowManager;
import net.sourceforge.pinemup.ui.swing.SettingsDialog;

class GeneralMenuLogic implements ActionListener {
   public static final String ACTION_ADD_NOTE = "ADD_NOTE";
   public static final String ACTION_SHOW_ALL_NOTES = "SHOW_ALL_NOTES";
   public static final String ACTION_HIDE_ALL_NOTES = "HIDE_ALL_NOTES";

   public static final String ACTION_SHOW_ABOUT_DIALOG = "SHOW_ABOUT_DIALOG";
   public static final String ACTION_SHOW_SETTINGS_DIALOG = "SHOW_SETTINGS_DIALOG";
   public static final String ACTION_EXIT_APPLICATION = "EXIT_APPLICATION";
   public static final String ACTION_UPLOAD_TO_SERVER = "UPLOAD_TO_SERVER";
   public static final String ACTION_DOWNLOAD_FROM_SERVER = "DOWNLOAD_FROM_SERVER";
   public static final String ACTION_EXPORT = "EXPORT";
   public static final String ACTION_MANAGE_CATEGORIES = "MANAGE_CATEGORIES";
   public static final String ACTION_CHECK_FOR_UPDATES = "CHECK_FOR_UPDATES";

   @Override
   public void actionPerformed(ActionEvent e) {
      String action = e.getActionCommand();
      if (ACTION_ADD_NOTE.equals(action)) {
         Note newNote = CategoryManager.getInstance().createNoteAndAddToDefaultCategory();
         NoteWindow window = NoteWindowManager.getInstance().createNoteWindowForNote(newNote);
         window.jumpIntoTextArea();
      } else if (ACTION_SHOW_ALL_NOTES.equals(action)) {
         CategoryManager.getInstance().unhideAllNotes();
      } else if (ACTION_HIDE_ALL_NOTES.equals(action)) {
         CategoryManager.getInstance().hideAllNotes();
      } else if (ACTION_SHOW_ABOUT_DIALOG.equals(action)) {
         new AboutDialog();
      } else if (ACTION_SHOW_SETTINGS_DIALOG.equals(action)) {
         SettingsDialog.showInstance();
      } else if (ACTION_EXIT_APPLICATION.equals(action)) {
         // save notes to file and exit
         NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories());
         System.exit(0);
      } else if (ACTION_UPLOAD_TO_SERVER.equals(action)) {
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacefileonserver"), I18N.getInstance()
                     .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // save notes to file
            NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories());
            // copy file to server
            new ServerThread(ServerThread.UPLOAD);
         }
      } else if (ACTION_DOWNLOAD_FROM_SERVER.equals(action)) {
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacelocalfile"), I18N.getInstance()
                     .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            new ServerThread(ServerThread.DOWNLOAD);
         }
      } else if (ACTION_EXPORT.equals(action)) {
         new ExportDialog();
      } else if (ACTION_MANAGE_CATEGORIES.equals(action)) {
         CategoryDialog.showInstance();
      } else if (ACTION_CHECK_FOR_UPDATES.equals(action)) {
         new UpdateCheckThread(true);
      }
   }
}
