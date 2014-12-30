package net.sourceforge.pinemup.core.io.notes.stream.xml;

import net.sourceforge.pinemup.core.io.notes.stream.NotesWriter;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.core.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.util.List;

public class NotesXmlWriter implements NotesWriter {
   private static final Logger LOG = LoggerFactory.getLogger(NotesXmlWriter.class);

   public NotesXmlWriter() {
      super();
   }

   @Override
   public boolean writeCategoriesToOutputStream(List<Category> categories, OutputStream out) {
      boolean writtenSuccessfully = true;

      XMLStreamWriter writer = null;
      try {
         XMLOutputFactory myFactory = XMLOutputFactory.newInstance();
         writer = myFactory.createXMLStreamWriter(out, NotesXmlMetaData.NOTESFILE_ENCODING);

         writer.writeStartDocument(NotesXmlMetaData.NOTESFILE_ENCODING, "1.0");
         writer.writeStartElement("notesfile");
         writer.writeAttribute("version", NotesXmlMetaData.LATEST_NOTESFILE_VERSION);

         for (Category cat : categories) {
            writer.writeStartElement("category");
            writer.writeAttribute("name", cat.getName());
            writer.writeAttribute("default", String.valueOf(cat.isDefaultCategory()));
            writer.writeAttribute("defaultnotecolor", String.valueOf(cat.getDefaultNoteColor().getCode()));

            for (Note n : cat.getNotes()) {
               writer.writeStartElement("note");
               writer.writeAttribute("hidden", String.valueOf(n.isHidden()));
               writer.writeAttribute("alwaysontop", String.valueOf(n.isAlwaysOnTop()));
               writer.writeAttribute("xposition", String.valueOf(n.getXPos()));
               writer.writeAttribute("yposition", String.valueOf(n.getYPos()));
               writer.writeAttribute("width", String.valueOf(n.getXSize()));
               writer.writeAttribute("height", String.valueOf(n.getYSize()));
               writer.writeAttribute("color", String.valueOf(n.getColor().getCode()));
               writer.writeStartElement("text");
               writer.writeAttribute("size", String.valueOf(n.getFontSize()));
               String noteText = n.getText();
               String[] textParts = noteText.split("\n");
               for (int i = 0; i < textParts.length; i++) {
                  writer.writeCharacters(textParts[i]);
                  if (i < textParts.length - 1) {
                     writer.writeEmptyElement("newline");
                  }
               }
               // newlines at the end
               while (noteText.endsWith("\n")) {
                  writer.writeEmptyElement("newline");
                  noteText = noteText.substring(0, noteText.length() - 1);
               }
               writer.writeEndElement();
               writer.writeEndElement();
            }
            writer.writeEndElement();
         }
         writer.writeEndElement();
         writer.writeEndDocument();
      } catch (XMLStreamException e) {
         writtenSuccessfully  = false;
      } finally {
         if (writer != null) {
            try {
               writer.close();
            } catch (XMLStreamException e) {
               LOG.error("Error during attempt to close stream.", e);
            }
         }
      }

      return writtenSuccessfully;
   }
}
