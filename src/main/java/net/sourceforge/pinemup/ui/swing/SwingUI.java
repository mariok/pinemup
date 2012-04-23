package net.sourceforge.pinemup.ui.swing;

import java.awt.AWTException;
import java.awt.SystemTray;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.ui.PinEmUpUI;
import net.sourceforge.pinemup.ui.swing.menus.TrayMenu;

public class SwingUI extends PinEmUpUI {
   private TrayMenu trayMenu;

   @Override
   public void initialize() {
      if (SystemTray.isSupported()) {
         // add trayicon
         SystemTray tray = SystemTray.getSystemTray();
         try {
            trayMenu = new TrayMenu();
            tray.add(new PinEmUpTrayIcon(trayMenu));
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

   @Override
   public void refreshCategories() {
      trayMenu.createCategoriesMenu();
   }

   @Override
   public void refreshI18NStrings() {
      trayMenu.init();
   }
}
