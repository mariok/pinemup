package net.sourceforge.pinemup.core.io.notes.stream;

import net.sourceforge.pinemup.core.model.Category;

import java.io.InputStream;
import java.util.List;

public interface NotesReader {
   List<Category> readCategoriesFromInputStream(InputStream inputStream);

   boolean dataIsValid(InputStream inputStream);
}
