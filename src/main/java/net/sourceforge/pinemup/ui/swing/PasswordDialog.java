package net.sourceforge.pinemup.ui.swing;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import net.sourceforge.pinemup.ui.UserPasswordRetriever;

public class PasswordDialog implements UserPasswordRetriever {
   private static final int PASSWORD_FIELD_LENGTH = 12;

   @Override
   public String retrievePasswordFromUser(String message) {
      JPasswordField p = new JPasswordField(PASSWORD_FIELD_LENGTH);
      JOptionPane.showMessageDialog(null, p, message, JOptionPane.PLAIN_MESSAGE);
      return String.valueOf(p.getPassword());
   }
}
