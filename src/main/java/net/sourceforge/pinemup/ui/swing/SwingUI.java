package net.sourceforge.pinemup.ui.swing;

import java.awt.AWTException;
import java.awt.SystemTray;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.UpdateCheckResultHandler;
import net.sourceforge.pinemup.ui.UserInputRetriever;
import net.sourceforge.pinemup.ui.swing.dialogs.DialogFactory;
import net.sourceforge.pinemup.ui.swing.notewindow.NoteWindowManager;
import net.sourceforge.pinemup.ui.swing.tray.PinEmUpTrayIcon;
import net.sourceforge.pinemup.ui.swing.tray.TrayMenu;
import net.sourceforge.pinemup.ui.swing.tray.TrayMenuUpdater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingUI {
   private static final Logger LOG = LoggerFactory.getLogger(SwingUI.class);

   private SwingUI() {
      super();
   }

   public static void initialize() {
      if (SystemTray.isSupported()) {
         // add trayicon
         SystemTray tray = SystemTray.getSystemTray();
         try {
            UpdateCheckResultHandler updateCheckResultHandler = new SwingUpdateCheckResultHandler(true);
            UserInputRetriever userInputRetriever = new SwingUserInputRetreiver();
            DialogFactory dialogFactory = new DialogFactory();

            NoteWindowManager noteWindowManager = new NoteWindowManager();

            TrayMenu trayMenu = new TrayMenu(dialogFactory, userInputRetriever, updateCheckResultHandler);
            tray.add(new PinEmUpTrayIcon(trayMenu, noteWindowManager));

            TrayMenuUpdater trayMenuUpdater = new TrayMenuUpdater(trayMenu);
            CategoryManager.getInstance().registerDefaultCategoryChangedEventListener(trayMenuUpdater);
            CategoryManager.getInstance().registerCategoryAddedEventListener(trayMenuUpdater);
            CategoryManager.getInstance().registerCategoryRemovedEventListener(trayMenuUpdater);
            UserSettings.getInstance().addUserSettingsChangedEventListener(trayMenuUpdater);

            CategoryManager.getInstance().registerDefaultNoteChangedEventListener(noteWindowManager);
            CategoryManager.getInstance().registerDefaultNoteAddedEventListener(noteWindowManager);
            CategoryManager.getInstance().registerDefaultNoteRemovedEventListener(noteWindowManager);
         } catch (AWTException e) {
            LOG.error("Error during initialization of tray icon.", e);
         }
      } else {
         // tray icon not supported
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.trayiconnotsupported"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }

   public static UserInputRetriever getUserInputRetriever() {
      return new SwingUserInputRetreiver();
   }
}
