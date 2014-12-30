package net.sourceforge.pinemup.ui.swing;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.notes.file.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.notes.file.NotesFileWriterResultHandler;
import net.sourceforge.pinemup.core.io.notes.stream.NotesReader;
import net.sourceforge.pinemup.core.io.notes.stream.NotesWriter;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;
import net.sourceforge.pinemup.core.settings.UserSettings;
import net.sourceforge.pinemup.ui.PinEmUpUi;
import net.sourceforge.pinemup.ui.swing.dialogs.DialogFactory;
import net.sourceforge.pinemup.ui.swing.notewindow.NoteWindowManager;
import net.sourceforge.pinemup.ui.swing.tray.PinEmUpTrayIcon;
import net.sourceforge.pinemup.ui.swing.tray.TrayMenu;
import net.sourceforge.pinemup.ui.swing.tray.TrayMenuUpdater;
import net.sourceforge.pinemup.ui.swing.utils.DialogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JOptionPane;
import java.awt.AWTException;
import java.awt.SystemTray;

public final class SwingUI implements PinEmUpUi {
   private static final Logger LOG = LoggerFactory.getLogger(SwingUI.class);

   private NotesReader notesReader;

   private NotesWriter notesWriter;

   private NotesFileReader notesFileReader;

   private NotesFileWriter notesFileWriter;

   private NotesSaveTrigger notesSaveTrigger;

   public SwingUI(NotesReader notesReader, NotesWriter notesWriter, NotesFileReader notesFileReader, NotesFileWriter notesFileWriter, NotesSaveTrigger notesSaveTrigger) {
      super();
      this.notesReader = notesReader;
      this.notesWriter = notesWriter;
      this.notesFileReader = notesFileReader;
      this.notesFileWriter = notesFileWriter;
      this.notesSaveTrigger = notesSaveTrigger;
   }

   @Override
   public void initialize() {
      if (SystemTray.isSupported()) {
         // add trayicon
         SystemTray tray = SystemTray.getSystemTray();
         try {
            UpdateCheckResultHandler updateCheckResultHandler = new SwingUpdateCheckResultHandler(true);
            DialogFactory dialogFactory = new DialogFactory(updateCheckResultHandler,
                  notesFileReader, notesFileWriter, notesSaveTrigger);

            NoteWindowManager noteWindowManager = new NoteWindowManager();

            notesFileWriter.setNotesFileWriterResultHandler(new NotesFileWriterResultHandler() {
               @Override
               public void onFileWrittenSuccessfully() {
                  // do nothing
               }

               @Override
               public void onFileWriteError() {
                  DialogUtils.showErrorMessageToUser(I18N.getInstance().getString("error.title"),
                        I18N.getInstance().getString("error.notesfilenotsaved"));
               }
            });

            TrayMenu trayMenu = new TrayMenu(dialogFactory, updateCheckResultHandler,
                  notesReader, notesWriter, notesFileWriter, notesSaveTrigger);
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

   @Override
   public String makeSureNotesFileIsValid(String notesFilePath) {
      return DialogUtils.makeSureNotesFileIsValid(notesFilePath, notesFileReader);
   }
}
