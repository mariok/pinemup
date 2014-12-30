package net.sourceforge.pinemup.ui.swing.utils;

import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.utils.FileUtils;
import net.sourceforge.pinemup.ui.swing.dialogs.file.FileDialogCreator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.io.File;

public final class DialogUtils {
   private static final int PASSWORD_FIELD_LENGTH = 12;

   private DialogUtils() {
      super();
   }

   public static String retrievePasswordFromUser() {
      JPasswordField p = new JPasswordField(PASSWORD_FIELD_LENGTH);
      JOptionPane.showMessageDialog(null, p, I18N.getInstance().getString("confirm.enterserverpassword"), JOptionPane.PLAIN_MESSAGE);
      return String.valueOf(p.getPassword());
   }

   public static File retieveFileChoiceFromUser() {
      File f = null;
      if (FileDialogCreator.getFileDialogInstance().showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
         f = FileDialogCreator.getFileDialogInstance().getSelectedFile();
      }
      return f;
   }

   public static boolean retrieveUserConfirmation(String title, String message) {
      return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
   }

   public static void showErrorMessageToUser(String title, String message) {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
   }

   public static void showInfoMessageToUser(String title, String message) {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
   }

   public static String makeSureNotesFileIsValid(String notesFilePath, NotesFileReader notesFileReader) {
      String selectedFilePath = notesFilePath;
      boolean selectedFileIsValid;

      do {
         selectedFileIsValid = notesFileReader.fileIsValid(selectedFilePath);

            if (!selectedFileIsValid) {
               if (!retrieveUserConfirmation(I18N.getInstance().getString("error.title"),
                     I18N.getInstance().getString("error.notesfilenotvalid"))) {
                  System.exit(0);
               }

               File userFileChoice = retieveFileChoiceFromUser();
               if (userFileChoice != null) {
                  selectedFilePath = FileUtils.checkAndAddExtension(userFileChoice.getAbsolutePath(), ".xml");
               }
            }
       } while (!selectedFileIsValid);

      return selectedFilePath;
   }
}
