package net.sourceforge.pinemup.core.io.notes.stream.export;

import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.notes.stream.NotesWriter;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.core.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

public final class NotesTextExportWriter implements NotesWriter {
   private static final Logger LOG = LoggerFactory.getLogger(NotesTextExportWriter.class);

   private static final String EXPORT_FILE_ENCODING = "UTF-8";

   private static final String NOTE_SEPARATOR_CHAR = "-";
   private static final int NOTE_SEPARATOR_LENGTH = 30;

   private static final String CATEGORY_SEPARATOR_CHAR = "#";
   private static final int CATEGORY_SEPARATOR_LENGTH = 60;
   private static final int CATEGORY_PREFIX_SUFFIX_LENGTH = 3;

   @Override
   public boolean writeCategoriesToOutputStream(List<Category> categories, OutputStream outputStream) {
      try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, Charset.forName(EXPORT_FILE_ENCODING)
            .newEncoder()))) {
         for (Category cat : categories) {
            writer.println();
            printCategorySeparatorLine(writer);
            printChars(writer, CATEGORY_SEPARATOR_CHAR, CATEGORY_PREFIX_SUFFIX_LENGTH);
            writer.print(" ");
            String categoryString = I18N.getInstance().getString("category") + ": " + cat.getName();
            writer.print(categoryString);
            int numberOfWhiteSpaces = CATEGORY_SEPARATOR_LENGTH - categoryString.length() - (2 * CATEGORY_PREFIX_SUFFIX_LENGTH) - 1;
            printChars(writer, " ", numberOfWhiteSpaces);
            printChars(writer, CATEGORY_SEPARATOR_CHAR, CATEGORY_PREFIX_SUFFIX_LENGTH);
            writer.println();
            printCategorySeparatorLine(writer);

            writer.println();

            boolean firstNote = true;
            for (Note n : cat.getNotes()) {
               if (n.getText() != null && !n.getText().trim().equals("")) {
                  if (!firstNote) {
                     writer.println();
                     printNoteSeparatorLine(writer);
                     writer.println();
                  } else {
                     firstNote = false;
                  }
                  writer.println(n.getText().replaceAll("\n", "\r\n"));
               }
            }
         }
         writer.flush();
      }

      return true;
   }

   private void printCategorySeparatorLine(PrintWriter writer) {
      printChars(writer, CATEGORY_SEPARATOR_CHAR, CATEGORY_SEPARATOR_LENGTH);
      writer.println();
   }

   private void printNoteSeparatorLine(PrintWriter writer) {
      printChars(writer, NOTE_SEPARATOR_CHAR, NOTE_SEPARATOR_LENGTH);
      writer.println();
   }

   private void printChars(PrintWriter writer, String charToPrint, int numberOfTimes) {
      for (int i = 0; i < numberOfTimes; i++) {
         writer.print(charToPrint);
      }
   }
}
