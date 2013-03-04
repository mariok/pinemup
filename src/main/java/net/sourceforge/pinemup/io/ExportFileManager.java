package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

import javax.swing.JFileChooser;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.ui.swing.FileDialogCreator;

public class ExportFileManager {
   private static final String EXPORT_FILE_ENCODING = "UTF-8";
   private static final String FILE_EXTENSION = ".txt";
   private static final String NOTE_SEPARATOR = "---------------------";
   private static final String CATEGORY_SEPARATOR = "################################################################";

   private static class Holder {
      private static final ExportFileManager INSTANCE = new ExportFileManager();
   }

   public static ExportFileManager getInstance() {
      return Holder.INSTANCE;
   }

   private ExportFileManager() {

   }

   public void exportCategoriesToTextFile(List<Category> l) {
      File f = null;
      if (FileDialogCreator.getExportFileDialogInstance().showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
         String name = NotesFileManager.checkAndAddExtension(FileDialogCreator.getExportFileDialogInstance().getSelectedFile()
               .getAbsolutePath(), FILE_EXTENSION);
         f = new File(name);
      }
      if (f != null) {
         try {
            PrintWriter ostream = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), Charset.forName(EXPORT_FILE_ENCODING)
                  .newEncoder()));

            for (Category cat : l) {
               ostream.println(CATEGORY_SEPARATOR);
               ostream.println(I18N.getInstance().getString("category") + ": " + cat.getName());
               ostream.println(CATEGORY_SEPARATOR);
               ostream.println();

               boolean firstNote = true;
               for (Note n : cat.getNotes()) {
                  if (n.getText() != null && !n.getText().trim().equals("")) {
                     if (!firstNote) {
                        ostream.println();
                        ostream.println(NOTE_SEPARATOR);
                     } else {
                        firstNote = false;
                     }
                     ostream.println(n.getText().replaceAll("\n", "\r\n"));
                     ostream.println();
                  }
               }
            }
            ostream.flush();
            ostream.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}
