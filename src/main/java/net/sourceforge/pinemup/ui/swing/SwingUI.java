package net.sourceforge.pinemup.ui.swing;

import java.awt.AWTException;
import java.awt.SystemTray;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.ui.PinEmUpUI;

public class SwingUI extends PinEmUpUI {
   @Override
   public void initialize() {
      if (SystemTray.isSupported()) {
         // add trayicon
         SystemTray tray = SystemTray.getSystemTray();
         try {
            tray.add(PinEmUpTrayIcon.getInstance());
         } catch (AWTException e) {
            e.printStackTrace();
         }

         // define user interface for retrieving the server password
         UserSettings.getInstance().setUserPasswordRetriever(new PasswordDialog());
      } else { // tray icon not supported
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.trayiconnotsupported"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }

   @Override
   public void hideNotes() {
      NoteWindowManager.getInstance().hideAndRemoveAllNoteWindows();
   }

   @Override
   public void showNotes() {
      NoteWindowManager.getInstance().createNoteWindowsForAllVisibleNotes();
   }
}
