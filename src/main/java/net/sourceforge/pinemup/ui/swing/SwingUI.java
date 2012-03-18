package net.sourceforge.pinemup.ui.swing;

import java.awt.AWTException;
import java.awt.SystemTray;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UpdateCheckThread;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;
import net.sourceforge.pinemup.ui.PinEmUpUI;

public class SwingUI implements PinEmUpUI {
   @Override
   public void initializeUI() {
      if (SystemTray.isSupported()) {
         // set locale
         I18N.getInstance().setLocale(UserSettings.getInstance().getLocale());

         // load notes from file
         CategoryManager.getInstance().append(NotesFileManager.getInstance().readCategoriesFromFile());

         // add trayicon
         SystemTray tray = SystemTray.getSystemTray();
         try {
            tray.add(PinEmUpTrayIcon.getInstance());
         } catch (AWTException e) {
            e.printStackTrace();
         }

         // define user interface for retrieving the server password
         UserSettings.getInstance().setUserPasswordRetriever(new PasswordDialog());

         // show all notes that are set visible
         NoteWindowManager.getInstance().createNoteWindowsForAllVisibleNotes();

         // udate check
         if (UserSettings.getInstance().isUpdateCheckEnabled()) {
            new UpdateCheckThread(false);
         }
      } else { // tray icon not supported
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.trayiconnotsupported"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }
}
