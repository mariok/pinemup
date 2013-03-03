/*
 * pin 'em up
 *
 * Copyright (C) 2007-2012 by Mario Ködding
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
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
import net.sourceforge.pinemup.ui.swing.FileDialogCreator;

import org.xml.sax.SAXException;

public class NotesFileManager {
   private static final String LATEST_NOTESFILE_VERSION = "0.2";

   private static class Holder {
      private static final NotesFileManager INSTANCE = new NotesFileManager();
   }

   public static NotesFileManager getInstance() {
      return Holder.INSTANCE;
   }

   private NotesFileManager() {

   }

   public void writeCategoriesToFile(List<Category> l) {
      System.out.println(new Date() + " writing notes to file...");
      FileOutputStream f = null;
      XMLStreamWriter writer = null;
      try {
         XMLOutputFactory myFactory = XMLOutputFactory.newInstance();
         f = new FileOutputStream(UserSettings.getInstance().getNotesFile());
         writer = myFactory.createXMLStreamWriter(f, "UTF-8");

         writer.writeStartDocument("UTF-8", "1.0");
         writer.writeStartElement("notesfile");
         writer.writeAttribute("version", LATEST_NOTESFILE_VERSION);

         for (Category cat : l) {
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
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotsaved"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      } catch (FileNotFoundException e) {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotsaved"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      } finally {
         if (writer != null) {
            try {
               writer.close();
            } catch (XMLStreamException e) {
               e.printStackTrace();
            }
         }
         if (f != null) {
            try {
               f.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }

   public List<Category> readCategoriesFromFile() {
      List<Category> categories = new LinkedList<Category>();
      Category currentCategory = null;
      Note currentNote = null;
      boolean defaultNotAdded = true;
      File nfile = new File(UserSettings.getInstance().getNotesFile());
      while (nfile.exists() && !fileIsValid(UserSettings.getInstance().getNotesFile())) {
         if (JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("error.notesfilenotvalid"),
               I18N.getInstance().getString("error.title"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            System.exit(0);
         }

         File f = null;
         if (FileDialogCreator.getFileDialogInstance().showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            f = FileDialogCreator.getFileDialogInstance().getSelectedFile();
         }
         if (f != null) {
            UserSettings.getInstance().setNotesFile((NotesFileManager.checkAndAddExtension(f.getAbsolutePath(), ".xml")));
         }
         nfile = new File(UserSettings.getInstance().getNotesFile());
      }
      UserSettings.getInstance().saveSettings();

      InputStream in = null;
      XMLStreamReader parser = null;
      try {
         in = new FileInputStream(UserSettings.getInstance().getNotesFile());
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         parser = myFactory.createXMLStreamReader(in, "UTF-8");

         int event;
         while (parser.hasNext()) {
            event = parser.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
               String ename = parser.getLocalName();
               if (ename.equals("category")) {
                  String name = "";
                  boolean def = false;
                  NoteColor defNoteColor = NoteColor.DEFAULT_COLOR;
                  for (int i = 0; i < parser.getAttributeCount(); i++) {
                     if (parser.getAttributeLocalName(i).equals("name")) {
                        name = parser.getAttributeValue(i);
                     } else if (parser.getAttributeLocalName(i).equals("default")) {
                        def = (parser.getAttributeValue(i).equals("true")) && defaultNotAdded;
                        if (def) {
                           defaultNotAdded = false;
                        }
                     } else if (parser.getAttributeLocalName(i).equals("defaultnotecolor")) {
                        defNoteColor = NoteColor.getNoteColorByCode(Byte.parseByte(parser.getAttributeValue(i)));
                     }
                  }
                  currentCategory = new Category(name, def, defNoteColor);
                  categories.add(currentCategory);
               } else if (ename.equals("note")) {
                  currentNote = new Note();
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
                  if (currentCategory != null) {
                     currentCategory.addNote(currentNote);
                  }
               } else if (ename.equals("text")) {
                  for (int i = 0; i < parser.getAttributeCount(); i++) {
                     if (parser.getAttributeLocalName(i).equals("size")) {
                        short fontSize = Short.parseShort(parser.getAttributeValue(i));
                        currentNote.setFontSize(fontSize);
                     }
                  }
               } else if (ename.equals("newline")) {
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
      } catch (FileNotFoundException e) {
         // create default categories
         categories.add(new Category("Home", true, NoteColor.YELLOW));
         categories.add(new Category("Office", false, NoteColor.GREEN));
      } catch (XMLStreamException e) {
         e.printStackTrace();
      } finally {
         if (parser != null) {
            try {
               parser.close();
            } catch (XMLStreamException e) {
               e.printStackTrace();
            }
         }
         if (in != null) {
            try {
               in.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      observeAllNotesAndCategories(categories);
      return categories;
   }

   public static String checkAndAddExtension(String s, String xt) {
      int len = s.length();
      String ext = s.substring(len - 4, len);
      if (!ext.toLowerCase().equals(xt.toLowerCase())) {
         s = s + xt.toLowerCase();
      }
      return s;
   }

   private boolean fileIsValid(String filename) {
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
         e.printStackTrace();
      } catch (XMLStreamException e) {
         e.printStackTrace();
      } finally {
         if (parser != null) {
            try {
               parser.close();
            } catch (XMLStreamException e) {
               e.printStackTrace();
            }
         }
         if (in != null) {
            try {
               in.close();
            } catch (IOException e) {
               e.printStackTrace();
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
