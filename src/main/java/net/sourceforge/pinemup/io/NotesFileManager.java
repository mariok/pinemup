/*
 * pin 'em up
 *
 * Copyright (C) 2007-2013 by Mario Ködding
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.core.NoteColor;
import net.sourceforge.pinemup.core.UserSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public final class NotesFileManager {
   private static final String LATEST_NOTESFILE_VERSION = "0.2";

   private static final Logger LOG = LoggerFactory.getLogger(NotesFileManager.class);

   private static class Holder {
      private static final NotesFileManager INSTANCE = new NotesFileManager();
   }

   public static NotesFileManager getInstance() {
      return Holder.INSTANCE;
   }

   private NotesFileManager() {
   }

   public boolean writeCategoriesToOutputStream(List<Category> categories, OutputStream out) {
      boolean writtenSuccessfully = true;

      XMLStreamWriter writer = null;
      try {
         XMLOutputFactory myFactory = XMLOutputFactory.newInstance();
         writer = myFactory.createXMLStreamWriter(out, "UTF-8");

         writer.writeStartDocument("UTF-8", "1.0");
         writer.writeStartElement("notesfile");
         writer.writeAttribute("version", LATEST_NOTESFILE_VERSION);

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
         LOG.error("XMLStreamException occured during attempt to write notesfile to disk.", e);

         writtenSuccessfully = false;
         UserSettings
               .getInstance()
               .getUserInputRetriever()
               .showErrorMessageToUser(I18N.getInstance().getString("error.title"), I18N.getInstance().getString("error.notesfilenotsaved"));
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

   public boolean writeCategoriesToFile(List<Category> categories, String filePath) {
      boolean writtenSuccessfully = true;

      LOG.info("writing notes to file...");

      try (FileOutputStream f = new FileOutputStream(filePath);) {
         writtenSuccessfully = writeCategoriesToOutputStream(categories, f);
      } catch (FileNotFoundException e) {
         writtenSuccessfully = false;
         UserSettings
               .getInstance()
               .getUserInputRetriever()
               .showErrorMessageToUser(I18N.getInstance().getString("error.title"), I18N.getInstance().getString("error.notesfilenotsaved"));
      } catch (IOException e) {
         LOG.error("Error during attempt to write notes to stream.", e);
      }

      return writtenSuccessfully;
   }

   public List<Category> readCategoriesFromInputStream(InputStream inputStream) {
      List<Category> categories = new LinkedList<>();

      XMLStreamReader parser = null;
      try {
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         parser = myFactory.createXMLStreamReader(inputStream, "UTF-8");

         Category currentCategory = null;
         Note currentNote = null;
         boolean defaultCategoryAdded = false;

         while (parser.hasNext()) {
            int event = parser.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
               String elementName = parser.getLocalName();
               if (elementName.equals("category")) {
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
               } else if (elementName.equals("note")) {
                  currentNote = handleNote(parser);
                  if (currentCategory != null) {
                     currentCategory.addNote(currentNote);
                  }
               } else if (elementName.equals("text")) {
                  for (int i = 0; i < parser.getAttributeCount(); i++) {
                     if (parser.getAttributeLocalName(i).equals("size")) {
                        short fontSize = Short.parseShort(parser.getAttributeValue(i));
                        currentNote.setFontSize(fontSize);
                     }
                  }
               } else if (elementName.equals("newline")) {
                  currentNote.setText(currentNote.getText() + "\n");
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
               // do nothing for all other events
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

   public List<Category> readCategoriesFromFile(String filePath) {
      List<Category> categories = new LinkedList<>();

      if (new File(filePath).exists()) {
         try (InputStream in = new FileInputStream(filePath)) {
            categories = readCategoriesFromInputStream(in);
         } catch (IOException e) {
            LOG.error("Error during attempt to read notes from file.", e);
         }
      } else {
         // create default categories
         categories.add(new Category("Home", true, NoteColor.YELLOW));
         categories.add(new Category("Office", false, NoteColor.GREEN));
      }

      observeAllNotesAndCategories(categories);

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

   public static String checkAndAddExtension(String fileName, String requiredExtension) {
      String checkedFileName = fileName;

      int len = fileName.length();
      String actualExtension = fileName.substring(len - 4, len);

      if (!actualExtension.equalsIgnoreCase(requiredExtension)) {
         checkedFileName = fileName + requiredExtension.toLowerCase();
      }

      return checkedFileName;
   }

   public boolean fileIsValid(String filename) {
      try {
         SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

         String version = getNotesFileVersion(filename);
         if (version == null) {
            return false;
         }
         URL schemaLocation = ResourceLoader.getInstance().getSchemaFile(version);
         Schema schema;
         schema = factory.newSchema(schemaLocation);

         Validator validator = schema.newValidator();

         Source source = new StreamSource(new FileInputStream(filename));

         validator.validate(source);

         return true;
      } catch (IOException e) {
         return false;
      } catch (SAXException e) {
         return false;
      }
   }

   private String getNotesFileVersion(String filename) {
      String version = null;

      InputStream in = null;
      XMLStreamReader parser = null;
      try {
         in = new FileInputStream(filename);
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         parser = myFactory.createXMLStreamReader(in, "UTF-8");

         int event;
         while (parser.hasNext()) {
            event = parser.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
               String ename = parser.getLocalName();
               if (ename.equals("notesfile")) {
                  for (int i = 0; i < parser.getAttributeCount(); i++) {
                     if (parser.getAttributeLocalName(i).equals("version")) {
                        version = parser.getAttributeValue(i);
                     }
                  }
               }
               break;
            default:
               // do nothing
               break;
            }
         }
      } catch (FileNotFoundException e) {
         LOG.error("The file " + filename + " could not be found!", e);
      } catch (XMLStreamException e) {
         LOG.error("Error during attempt to retrieve notesfile version.", e);
      } finally {
         if (parser != null) {
            try {
               parser.close();
            } catch (XMLStreamException e) {
               LOG.error("Error during attempt to close stream.", e);
            }
         }
         if (in != null) {
            try {
               in.close();
            } catch (IOException e) {
               LOG.error("Error during attempt to close stream.", e);
            }
         }
      }
      return version;
   }

   private void observeAllNotesAndCategories(List<Category> categories) {
      for (Category cat : categories) {
         cat.addObserver(NotesFileSaveTrigger.getInstance());
         for (Note note : cat.getNotes()) {
            note.addObserver(NotesFileSaveTrigger.getInstance());
         }
      }
   }
}
