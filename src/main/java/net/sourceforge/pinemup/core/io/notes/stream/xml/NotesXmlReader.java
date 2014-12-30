package net.sourceforge.pinemup.core.io.notes.stream.xml;

import net.sourceforge.pinemup.core.io.notes.stream.NotesReader;
import net.sourceforge.pinemup.core.io.resources.ResourceLoader;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.core.model.Note;
import net.sourceforge.pinemup.core.model.NoteColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class NotesXmlReader implements NotesReader {
   private static final Logger LOG = LoggerFactory.getLogger(NotesXmlReader.class);

   @Override
   public List<Category> readCategoriesFromInputStream(InputStream inputStream) {
      List<Category> categories = new LinkedList<>();

      XMLStreamReader parser = null;
      try {
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         parser = myFactory.createXMLStreamReader(inputStream, NotesXmlMetaData.NOTESFILE_ENCODING);

         Category currentCategory = null;
         Note currentNote = null;
         boolean defaultCategoryAdded = false;

         while (parser.hasNext()) {
            int event = parser.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
               switch (parser.getLocalName()) {
               case "category":
                  currentCategory = handleCategory(parser);
                  if (currentCategory.isDefaultCategory()) {
                     if (defaultCategoryAdded) {
                        // there must be only one default category
                        currentCategory.setDefault(false);
                     } else {
                        defaultCategoryAdded = true;
                     }
                  }
                  categories.add(currentCategory);
                  break;
               case "note":
                  currentNote = handleNote(parser);
                  if (currentCategory != null) {
                     currentCategory.addNote(currentNote);
                  }
                  break;
               case "text":
                  for (int i = 0; i < parser.getAttributeCount(); i++) {
                     if (parser.getAttributeLocalName(i).equals("size")) {
                        short fontSize = Short.parseShort(parser.getAttributeValue(i));
                        currentNote.setFontSize(fontSize);
                     }
                  }
                  break;
               case "newline":
                  currentNote.setText(currentNote.getText() + "\n");
                  break;
               default:
                  // unkown element name, do nothing
                  break;
               }
               break;
            case XMLStreamConstants.CHARACTERS:
               if (!parser.isWhiteSpace()) {
                  if (currentNote != null) {
                     String str = parser.getText();
                     currentNote.setText(currentNote.getText() + str);
                  }
               }
               break;
            default:
               // do nothing for all other listeners
               break;
            }
         }
      } catch (XMLStreamException e) {
         categories = null;
      } finally {
         if (parser != null) {
            try {
               parser.close();
            } catch (XMLStreamException e) {
               LOG.error("Error during attempt to close stream.", e);
            }
         }
      }

      return categories;
   }

   private Category handleCategory(XMLStreamReader parser) {
      String name = "";
      boolean isDefaultCategory = false;
      NoteColor defNoteColor = NoteColor.DEFAULT_COLOR;
      for (int i = 0; i < parser.getAttributeCount(); i++) {
         if (parser.getAttributeLocalName(i).equals("name")) {
            name = parser.getAttributeValue(i);
         } else if (parser.getAttributeLocalName(i).equals("default")) {
            isDefaultCategory = (parser.getAttributeValue(i).equals("true"));
         } else if (parser.getAttributeLocalName(i).equals("defaultnotecolor")) {
            defNoteColor = NoteColor.getNoteColorByCode(Byte.parseByte(parser.getAttributeValue(i)));
         }
      }
      return new Category(name, isDefaultCategory, defNoteColor);
   }

   private Note handleNote(XMLStreamReader parser) {
      Note currentNote = new Note();

      for (int i = 0; i < parser.getAttributeCount(); i++) {
         if (parser.getAttributeLocalName(i).equals("hidden")) {
            boolean h = parser.getAttributeValue(i).equals("true");
            currentNote.setHidden(h);
         } else if (parser.getAttributeLocalName(i).equals("xposition")) {
            short x = Short.parseShort(parser.getAttributeValue(i));
            currentNote.setPosition(x, currentNote.getYPos());
         } else if (parser.getAttributeLocalName(i).equals("yposition")) {
            short y = Short.parseShort(parser.getAttributeValue(i));
            currentNote.setPosition(currentNote.getXPos(), y);
         } else if (parser.getAttributeLocalName(i).equals("width")) {
            short w = Short.parseShort(parser.getAttributeValue(i));
            currentNote.setSize(w, currentNote.getYSize());
         } else if (parser.getAttributeLocalName(i).equals("height")) {
            short h = Short.parseShort(parser.getAttributeValue(i));
            currentNote.setSize(currentNote.getXSize(), h);
         } else if (parser.getAttributeLocalName(i).equals("alwaysontop")) {
            boolean a = parser.getAttributeValue(i).equals("true");
            currentNote.setAlwaysOnTop(a);
         } else if (parser.getAttributeLocalName(i).equals("color")) {
            NoteColor color = NoteColor.getNoteColorByCode(Byte.parseByte(parser.getAttributeValue(i)));
            currentNote.setColor(color);
         }
      }

      return currentNote;
   }

   @Override
   public boolean dataIsValid(InputStream inputStream) {
      try {
         SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
         URL schemaLocation = ResourceLoader.getInstance().getSchemaFile(NotesXmlMetaData.LATEST_NOTESFILE_VERSION);
         Schema schema = factory.newSchema(schemaLocation);
         Validator validator = schema.newValidator();
         Source source = new StreamSource(inputStream);
         validator.validate(source);

         return true;
      } catch (IOException e) {
         return false;
      } catch (SAXException e) {
         return false;
      }
   }
}
