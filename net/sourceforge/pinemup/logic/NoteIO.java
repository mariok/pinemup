/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
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

package net.sourceforge.pinemup.logic;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.*;

import javax.swing.JOptionPane;
import javax.xml.stream.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.xml.sax.SAXException;

public class NoteIO {
   
   private static final String LATEST_NOTESFILE_VERSION = "0.2";
   
   public static void writeCategoriesToFile(CategoryList c, UserSettings s) {
      //write notes to xml file
      try {
         XMLOutputFactory myFactory = XMLOutputFactory.newInstance();
         FileOutputStream f = new FileOutputStream(s.getNotesFile());
         XMLStreamWriter writer = myFactory.createXMLStreamWriter(f,"UTF-8");
         
         writer.writeStartDocument("UTF-8","1.0");
         writer.writeStartElement("notesfile");
         writer.writeAttribute("version",LATEST_NOTESFILE_VERSION);
         
         CategoryList cl = c;
         while (cl != null) {
            if (cl.getCategory() != null) {
               writer.writeStartElement("category");
               writer.writeAttribute("name", cl.getCategory().getName());
               writer.writeAttribute("default",String.valueOf(cl.getCategory().isDefaultCategory()));
               writer.writeAttribute("defaultnotecolor",String.valueOf(cl.getCategory().getDefaultNoteColor()));

               NoteList nl = cl.getCategory().getNotes();
               while (nl != null) {
                  if (nl.getNote() != null) {
                     writer.writeStartElement("note");
                     writer.writeAttribute("hidden", String.valueOf(nl.getNote().isHidden()));
                     writer.writeAttribute("alwaysontop", String.valueOf(nl.getNote().isAlwaysOnTop()));
                     writer.writeAttribute("xposition", String.valueOf(nl.getNote().getXPos()));
                     writer.writeAttribute("yposition", String.valueOf(nl.getNote().getYPos()));
                     writer.writeAttribute("width", String.valueOf(nl.getNote().getXSize()));
                     writer.writeAttribute("height", String.valueOf(nl.getNote().getYSize()));
                     writer.writeAttribute("color", String.valueOf(nl.getNote().getBGColor()));
                     writer.writeStartElement("text");
                     writer.writeAttribute("size", String.valueOf(nl.getNote().getFontSize()));
                     String noteText = nl.getNote().getText(); 
                     String[] textParts = noteText.split("\n");
                     for (int i = 0; i<textParts.length; i++) {
                        writer.writeCharacters(textParts[i]);
                        if (i < textParts.length-1) {
                           writer.writeEmptyElement("newline");
                        }
                     }
                     //newlines at the end
                     while (noteText.endsWith("\n")) {
                        writer.writeEmptyElement("newline");
                        noteText = noteText.substring(0, noteText.length()-1);
                     }
                     writer.writeEndElement();
                     writer.writeEndElement();
                  }
                  nl = nl.getNext();
               }
               
               writer.writeEndElement();
            }
            cl = cl.getNext();
         }
         writer.writeEndElement();
         writer.writeEndDocument();
         writer.close();
         f.close();
      } catch (XMLStreamException e) {
         JOptionPane.showMessageDialog(null, "Could save notes to file! Please check file-settings and free disk-space!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
      } catch (FileNotFoundException e) {
         JOptionPane.showMessageDialog(null, "Could save notes to file! Please check file-settings and free disk-space!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
      } catch (IOException e) {
         System.out.println("IO Error");
      }
   }

   public static CategoryList readCategoriesFromFile(UserSettings s) {
      CategoryList c = new CategoryList();
      Category currentCategory = null;
      Note currentNote = null;
      boolean defaultNotAdded = true;
      File nfile = new File(s.getNotesFile());
      while (nfile.exists() && !fileIsValid(s.getNotesFile())) {
         if (JOptionPane.showConfirmDialog(null, "Notefile is not valid. Do you want to select a new one? (Click NO to exit)","Invalid notesfile",JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            System.exit(0);
         }
         
         File f = null;
         if (PinEmUp.getFileDialog().showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            f = PinEmUp.getFileDialog().getSelectedFile();
         }
         if (f != null) {
            s.setNotesFile((NoteIO.checkAndAddExtension(f.getAbsolutePath(),".xml")));
         }
         nfile = new File(s.getNotesFile());
      }
      s.saveSettings();
      
      try {
         InputStream in = new FileInputStream(s.getNotesFile());
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         XMLStreamReader parser = myFactory.createXMLStreamReader(in,"UTF-8");
        
         int event;
         while(parser.hasNext()) {
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
                  for (int i=0; i<parser.getAttributeCount(); i++) {
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
                  currentCategory = new Category(name,new NoteList(),def,defNoteColor);
                  c.add(currentCategory);
               } else if (ename.equals("note")) {
                  currentNote = new Note("",s,c,(byte)0);
                  for (int i=0; i<parser.getAttributeCount(); i++) {
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
                     currentCategory.getNotes().add(currentNote);
                  }
               } else if (ename.equals("text")) {
                  for (int i=0; i<parser.getAttributeCount(); i++) {
                     if (parser.getAttributeLocalName(i).equals("size")) {
                        short fontSize = Short.parseShort(parser.getAttributeValue(i));
                        currentNote.setFontSize(fontSize);
                     }
                  }
               } else if (ename.equals("newline")) {
                  currentNote.setText(currentNote.getText()+"\n");
               }
               break;
            case XMLStreamConstants.CHARACTERS:
               if(!parser.isWhiteSpace()) {
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
         //neu erstellen
         c.add(new Category("Home",new NoteList(),true,(byte)0));
         c.add(new Category("Office",new NoteList(),false,(byte)0));
      } catch (XMLStreamException e) {
         //Meldung ausgeben
         System.out.println("XML Error");
      } catch (IOException e) {
         System.out.println("IO Error");
      }
      return c;
   }

   public static void getCategoriesFromFTP(UserSettings us) {
      boolean downloaded = true;
      try {
         File f = new File(us.getNotesFile());
         FileOutputStream fos = new FileOutputStream(f);
         String filename = f.getName();
         String ftpString = "ftp://" + us.getFtpUser() + ":"
               + us.getFtpPasswdString() + "@" + us.getFtpServer()
               + us.getFtpDir() + filename + ";type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         InputStream is = urlc.getInputStream();
         int nextByte = is.read();
         while(nextByte != -1) {
            fos.write(nextByte);
            nextByte = is.read();
         }
         fos.close();
      } catch (Exception e) {
         downloaded = false;
         JOptionPane.showMessageDialog(null, "Could not download file from FTP server!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
      }
      if (downloaded) {
         JOptionPane.showMessageDialog(null, "Notes successfully downloaded from FTP server!", "pin 'em up - information", JOptionPane.INFORMATION_MESSAGE);
      }
   }

   public static void writeCategoriesToFTP(UserSettings us) {
      boolean uploaded = true;
      try {
         String completeFilename = us.getNotesFile();
         File f = new File(completeFilename);
         String filename = f.getName();
         FileInputStream fis = new FileInputStream(f);
         String ftpString = "ftp://" + us.getFtpUser() + ":"
         + us.getFtpPasswdString() + "@" + us.getFtpServer()
         + us.getFtpDir() + filename + ";type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         OutputStream  os = urlc.getOutputStream();
         
         int nextByte = fis.read();
         while (nextByte != -1) {
            os.write(nextByte);
            nextByte = fis.read();
         }
         os.close();
      } catch (Exception e) {
         uploaded = false;
         JOptionPane.showMessageDialog(null, "Error! Notes could not be uploaded to FTP server!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
      }
      if (uploaded) {
         JOptionPane.showMessageDialog(null, "Notes successfully uploaded to FTP server!", "pin 'em up - information", JOptionPane.INFORMATION_MESSAGE);
      }
   }
   
   public static void exportCategoriesToTextFile(CategoryList c) {
      File f = null;
      if (PinEmUp.getExportFileDialog().showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
         String name = NoteIO.checkAndAddExtension(PinEmUp.getExportFileDialog().getSelectedFile().getAbsolutePath(), ".txt");
         f = new File(name);
      }
      if (f != null) {
         try {
            PrintWriter ostream = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            // write text of notes to file
            while (c != null) {
               ostream.println("Category: " + c.getCategory().getName());
               ostream.println();
               NoteList n = c.getCategory().getNotes();
               while (n != null) {
                  if (n.getNote() != null) {
                     ostream.println(n.getNote().getText());
                     ostream.println();
                     ostream.println("---------------------");
                     ostream.println();
                  }
                  n = n.getNext();
               }
               ostream.println();
               ostream.println("################################################################");
               ostream.println();
               c = c.getNext();
            }
            ostream.flush();
            ostream.close();
         }
         catch ( IOException e ) {
            System.out.println("IOERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
         }
      }
   }
   
   public static String checkAndAddExtension(String s, String xt) {
      int len = s.length();
      String ext = s.substring(len-4, len);
      if (!ext.toLowerCase().equals(xt.toLowerCase())) {
         s = s + xt.toLowerCase();
      }
      return s;
   }
   
   public static boolean fileIsValid(String filename) {
      try {
      // 1. Lookup a factory for the W3C XML Schema language
      SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      
      // 2. Compile the schema. 
      // Here the schema is loaded from a java.io.File, but you could use 
      // a java.net.URL or a javax.xml.transform.Source instead.
      //File schemaLocation = ResourceLoader.getSchemaFile();
      String version = getNotesFileVersion(filename);
      URL schemaLocation = ResourceLoader.getSchemaFile(version);
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
      String version = "";
      try {
         InputStream in = new FileInputStream(filename);
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         XMLStreamReader parser = myFactory.createXMLStreamReader(in,"UTF-8");
        
         int event;
         while(parser.hasNext()) {
            event = parser.next();
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
               String ename = parser.getLocalName();
               if (ename.equals("notesfile")) {
                  for (int i=0; i<parser.getAttributeCount(); i++) {
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
         // do nothing
      } catch (XMLStreamException e) {
         System.out.println("XML Error");
      } catch (IOException e) {
         System.out.println("IO Error");
      }
      return version;
   }
   
   public static String getLatestNotesfileVersion() {
      return LATEST_NOTESFILE_VERSION;
   }

}
