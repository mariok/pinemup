/*
 * pin 'em up
 *
 * Copyright (C) 2007-2011 by Mario Ködding
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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

import net.sourceforge.pinemup.gui.FileDialogCreator;
import net.sourceforge.pinemup.gui.I18N;
import net.sourceforge.pinemup.logic.Category;
import net.sourceforge.pinemup.logic.Note;
import net.sourceforge.pinemup.logic.ResourceLoader;
import net.sourceforge.pinemup.logic.UserSettings;

import org.xml.sax.SAXException;

public final class NoteIO {
   public static final String LATEST_NOTESFILE_VERSION = "0.2";

   public static void writeCategoriesToFile(ListIterator<Category> l) {
      //write notes to xml file
      try {
         XMLOutputFactory myFactory = XMLOutputFactory.newInstance();
         FileOutputStream f = new FileOutputStream(UserSettings.getInstance().getNotesFile());
         XMLStreamWriter writer = myFactory.createXMLStreamWriter(f, "UTF-8");

         writer.writeStartDocument("UTF-8", "1.0");
         writer.writeStartElement("notesfile");
         writer.writeAttribute("version", LATEST_NOTESFILE_VERSION);

         Category tc;
         while (l.hasNext()) {
            tc = l.next();
            writer.writeStartElement("category");
            writer.writeAttribute("name", tc.getName());
            writer.writeAttribute("default", String.valueOf(tc.isDefaultCategory()));
            writer.writeAttribute("defaultnotecolor", String.valueOf(tc.getDefaultNoteColor()));

            ListIterator<Note> nl = tc.getListIterator();
            Note n;
            while (nl.hasNext()) {
               n = nl.next();
               writer.writeStartElement("note");
               writer.writeAttribute("hidden", String.valueOf(n.isHidden()));
               writer.writeAttribute("alwaysontop", String.valueOf(n.isAlwaysOnTop()));
               writer.writeAttribute("xposition", String.valueOf(n.getXPos()));
               writer.writeAttribute("yposition", String.valueOf(n.getYPos()));
               writer.writeAttribute("width", String.valueOf(n.getXSize()));
               writer.writeAttribute("height", String.valueOf(n.getYSize()));
               writer.writeAttribute("color", String.valueOf(n.getBGColor()));
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
               //newlines at the end
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
         writer.close();
         f.close();
      } catch (XMLStreamException e) {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotsaved"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      } catch (FileNotFoundException e) {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotsaved"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static List<Category> readCategoriesFromFile() {
      List<Category> c = new LinkedList<Category>();
      Category currentCategory = null;
      Note currentNote = null;
      boolean defaultNotAdded = true;
      File nfile = new File(UserSettings.getInstance().getNotesFile());
      while (nfile.exists() && !fileIsValid(UserSettings.getInstance().getNotesFile())) {
         if (JOptionPane.showConfirmDialog(null,  I18N.getInstance().getString("error.notesfilenotvalid"), I18N.getInstance().getString("error.title"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            System.exit(0);
         }

         File f = null;
         if (FileDialogCreator.getFileDialogInstance().showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            f = FileDialogCreator.getFileDialogInstance().getSelectedFile();
         }
         if (f != null) {
            UserSettings.getInstance().setNotesFile((NoteIO.checkAndAddExtension(f.getAbsolutePath(), ".xml")));
         }
         nfile = new File(UserSettings.getInstance().getNotesFile());
      }
      UserSettings.getInstance().saveSettings();

      try {
         InputStream in = new FileInputStream(UserSettings.getInstance().getNotesFile());
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         XMLStreamReader parser = myFactory.createXMLStreamReader(in, "UTF-8");

         int event;
         while (parser.hasNext()) {
            event = parser.next();
            switch(event) {
            case XMLStreamConstants.START_DOCUMENT:
               // do nothing
               break;
            case XMLStreamConstants.END_DOCUMENT:
               parser.close();
               break;
            case XMLStreamConstants.NAMESPACE:
               // do nothing
               break;
            case XMLStreamConstants.START_ELEMENT:
               String ename = parser.getLocalName();
               if (ename.equals("notesfile")) {
                  //do nothing yet
               } else if (ename.equals("category")) {
                  String name = "";
                  boolean def = false;
                  byte defNoteColor = 0;
                  for (int i = 0; i < parser.getAttributeCount(); i++) {
                     if (parser.getAttributeLocalName(i).equals("name")) {
                        name = parser.getAttributeValue(i);
                     } else if (parser.getAttributeLocalName(i).equals("default")) {
                        def = (parser.getAttributeValue(i).equals("true")) && defaultNotAdded;
                        if (def) {
                           defaultNotAdded = false;
                        }
                     } else if (parser.getAttributeLocalName(i).equals("defaultnotecolor")) {
                        defNoteColor = Byte.parseByte(parser.getAttributeValue(i));
                     }
                  }
                  currentCategory = new Category(name, def, defNoteColor);
                  c.add(currentCategory);
               } else if (ename.equals("note")) {
                  currentNote = new Note("", (byte) 0);
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
                        byte clr = Byte.parseByte(parser.getAttributeValue(i));
                        currentNote.setBGColor(clr);
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
            case XMLStreamConstants.END_ELEMENT:
               // do nothing
               break;
            default:
               break;
            }
         }
         parser.close();
         in.close();
      } catch (FileNotFoundException e) {
         //create default categories
         c.add(new Category("Home", true, (byte) 0));
         c.add(new Category("Office", false, (byte) 0));
      } catch (XMLStreamException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return c;
   }

   public static void exportCategoriesToTextFile(ListIterator<Category> l) {
      File f = null;
      if (FileDialogCreator.getExportFileDialogInstance().showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
         String name = NoteIO.checkAndAddExtension(FileDialogCreator.getExportFileDialogInstance().getSelectedFile().getAbsolutePath(), ".txt");
         f = new File(name);
      }
      if (f != null) {
         try {
            PrintWriter ostream = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            // write text of notes to file
            Category tc;
            while (l.hasNext()) {
               tc = l.next();
               ostream.println(I18N.getInstance().getString("category") + ": " + tc.getName());
               ostream.println();
               ListIterator<Note> nl = tc.getListIterator();
               Note n;
               while (nl.hasNext()) {
                  n = nl.next();
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

   public static String checkAndAddExtension(String s, String xt) {
      int len = s.length();
      String ext = s.substring(len - 4, len);
      if (!ext.toLowerCase().equals(xt.toLowerCase())) {
         s = s + xt.toLowerCase();
      }
      return s;
   }

   private static boolean fileIsValid(String filename) {
      try {
      // 1. Lookup a factory for the W3C XML Schema language
      SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

      // 2. Compile the schema.
      // Here the schema is loaded from a java.io.File, but you could use
      // a java.net.URL or a javax.xml.transform.Source instead.
      //File schemaLocation = ResourceLoader.getSchemaFile();
      String version = getNotesFileVersion(filename);
      if (version == null) {
         return false;
      }
      URL schemaLocation = ResourceLoader.getInstance().getSchemaFile(version);
      Schema schema;
      schema = factory.newSchema(schemaLocation);

      // 3. Get a validator from the schema.
      Validator validator = schema.newValidator();

      // 4. Parse the document you want to check.
      Source source = new StreamSource(new FileInputStream(filename));

      // 5. Check the document
      validator.validate(source);

      return true;
      } catch (IOException e) {
         //e.printStackTrace();
         return false;
      } catch (SAXException e) {
         //System.out.println(filename + " is not valid!");
         //System.out.println(e.getMessage());
         return false;
      }
   }

   private static String getNotesFileVersion(String filename) {
      String version = null;
      try {
         InputStream in = new FileInputStream(filename);
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         XMLStreamReader parser = myFactory.createXMLStreamReader(in, "UTF-8");

         int event;
         while (parser.hasNext()) {
            event = parser.next();
            switch(event) {
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
         parser.close();
         in.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (XMLStreamException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return version;
   }

   private NoteIO() {

   }
}
