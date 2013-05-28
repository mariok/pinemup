package net.sourceforge.pinemup.ui.swing;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.ui.UserInputRetriever;

class SwingUserInputRetreiver implements UserInputRetriever {
   private static final int PASSWORD_FIELD_LENGTH = 12;

   @Override
   public String retrieveTextInputFromUser(String message) {
      // TODO Auto-generated method stub
      return "";
   }

   @Override
   public String retrievePasswordFromUser() {
      JPasswordField p = new JPasswordField(PASSWORD_FIELD_LENGTH);
      JOptionPane.showMessageDialog(null, p, I18N.getInstance().getString("confirm.enterserverpassword"), JOptionPane.PLAIN_MESSAGE);
      return String.valueOf(p.getPassword());
   }

   @Override
   public File retieveFileChoiceFromUser() {
      File f = null;
      if (FileDialogCreator.getFileDialogInstance().showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
         f = FileDialogCreator.getFileDialogInstance().getSelectedFile();
      }
      return f;
   }

   @Override
   public boolean retrieveUserConfirmation(String title, String message) {
      return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
   }

   @Override
   public void showErrorMessageToUser(String title, String message) {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
   }

   @Override
   public void showInfoMessageToUser(String title, String message) {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
   }
}
