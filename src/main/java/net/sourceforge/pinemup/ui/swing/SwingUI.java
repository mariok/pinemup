package net.sourceforge.pinemup.ui.swing;

import java.awt.AWTException;
import java.awt.SystemTray;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.file.NotesFileWriter;
import net.sourceforge.pinemup.core.settings.UserSettings;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;
import net.sourceforge.pinemup.core.UserInputRetriever;
import net.sourceforge.pinemup.ui.swing.dialogs.DialogFactory;
import net.sourceforge.pinemup.ui.swing.notewindow.NoteWindowManager;
import net.sourceforge.pinemup.ui.swing.tray.PinEmUpTrayIcon;
import net.sourceforge.pinemup.ui.swing.tray.TrayMenu;
import net.sourceforge.pinemup.ui.swing.tray.TrayMenuUpdater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SwingUI {
   private static final Logger LOG = LoggerFactory.getLogger(SwingUI.class);

   private SwingUI() {
      super();
   }

   public static void initialize(NotesFileReader notesFileReader, NotesFileWriter notesFileWriter, NotesSaveTrigger notesSaveTrigger) {
      if (SystemTray.isSupported()) {
         // add trayicon
         SystemTray tray = SystemTray.getSystemTray();
         try {
            UpdateCheckResultHandler updateCheckResultHandler = new SwingUpdateCheckResultHandler(true);
            UserInputRetriever userInputRetriever = new SwingUserInputRetreiver();
            DialogFactory dialogFactory = new DialogFactory(userInputRetriever, updateCheckResultHandler,
                  notesFileReader, notesFileWriter, notesSaveTrigger);

            NoteWindowManager noteWindowManager = new NoteWindowManager();

            TrayMenu trayMenu = new TrayMenu(dialogFactory, userInputRetriever, updateCheckResultHandler,
                  notesFileReader, notesFileWriter, notesSaveTrigger);
            tray.add(new PinEmUpTrayIcon(trayMenu, noteWindowManager));

            TrayMenuUpdater trayMenuUpdater = new TrayMenuUpdater(trayMenu);
            CategoryManager.getInstance().registerDefaultCategoryChangedEventListener(trayMenuUpdater);
            CategoryManager.getInstance().registerDefaultCategoryAddedEventListener(trayMenuUpdater);
            CategoryManager.getInstance().registerDefaultCategoryRemovedEventListener(trayMenuUpdater);
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
