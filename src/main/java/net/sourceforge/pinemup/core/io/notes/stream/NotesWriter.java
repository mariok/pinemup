package net.sourceforge.pinemup.core.io.notes.stream;

import net.sourceforge.pinemup.core.model.Category;

import java.io.OutputStream;
import java.util.List;

public interface NotesWriter {
   boolean writeCategoriesToOutputStream(List<Category> categories, OutputStream outputStream);
}
