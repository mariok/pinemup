package net.sourceforge.pinemup.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JFileChooser;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.ui.swing.FileDialogCreator;

public class ExportFileManager {
   private static ExportFileManager instance = new ExportFileManager();

   private ExportFileManager() {

   }

   public static ExportFileManager getInstance() {
      return ExportFileManager.instance;
   }

   public void exportCategoriesToTextFile(List<Category> l) {
      File f = null;
      if (FileDialogCreator.getExportFileDialogInstance().showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
         String name = NotesFileManager.checkAndAddExtension(FileDialogCreator.getExportFileDialogInstance().getSelectedFile()
               .getAbsolutePath(), ".txt");
         f = new File(name);
      }
      if (f != null) {
         try {
            PrintWriter ostream = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            // write text of notes to file
            for (Category cat : l) {
               ostream.println(I18N.getInstance().getString("category") + ": " + cat.getName());
               ostream.println();
               for (Note n : cat.getNotes()) {
                  ostream.println(n.getText());
                  ostream.println();
                  ostream.println("---------------------");
                  ostream.println();
               }
               ostream.println();
               ostream.println("################################################################");
               ostream.println();
            }
            ostream.flush();
            ostream.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}
