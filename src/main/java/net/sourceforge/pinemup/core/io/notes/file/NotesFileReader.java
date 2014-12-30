package net.sourceforge.pinemup.core.io.notes.file;

import net.sourceforge.pinemup.core.io.notes.stream.NotesReader;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.core.model.NoteColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class NotesFileReader {
   private static final Logger LOG = LoggerFactory.getLogger(NotesFileReader.class);

   private final NotesReader notesReader;

   public NotesFileReader(NotesReader notesReader) {
      this.notesReader = notesReader;
   }

   public List<Category> readCategoriesFromFile(String filePath) {
      List<Category> categories = new LinkedList<>();

      if (new File(filePath).exists()) {
         try (InputStream in = new FileInputStream(filePath)) {
            categories = notesReader.readCategoriesFromInputStream(in);
         } catch (IOException e) {
            LOG.error("Error during attempt to read notes from xml.", e);
         }
      } else {
         // create default categories
         categories.add(new Category("Home", true, NoteColor.YELLOW));
         categories.add(new Category("Office", false, NoteColor.GREEN));
      }

      return categories;
   }

   public boolean fileIsValid(String filePath) {
      boolean fileIsValid;

      File file = new File(filePath);

      if (file.exists()) {
         try (InputStream inputStream = new FileInputStream(file)) {
            fileIsValid = notesReader.dataIsValid(inputStream);
         } catch (IOException e) {
            fileIsValid = false;
         }
      } else {
         fileIsValid = true;
      }

      return fileIsValid;
   }
}
