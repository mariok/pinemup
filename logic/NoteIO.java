/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
 *
 *
 * pin 'em up is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * pin 'em up is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with pin 'em up; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package logic;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.*;

import javax.swing.JOptionPane;

public class NoteIO {
   private static final String FILENAME = "notes.dat";
   
   public static String getFileName() {
      return FILENAME;
   }
   
   public static void writeNotesToFile(Note n, String filename) {
      try {
         FileOutputStream fs = new FileOutputStream(filename);
         ObjectOutputStream os = new ObjectOutputStream(fs);
         os.writeObject(n);
         os.close();
      } catch (IOException e) {
         System.err.println(e.toString());
      }
   }

   public static Note readNotesFromFile(String filename) {
      Note n = null;
      try {
         FileInputStream fs = new FileInputStream(filename);
         ObjectInputStream is = new ObjectInputStream(fs);
         n = (Note) is.readObject();
         is.close();
      } catch (ClassNotFoundException e) {
         // do nothing
      } catch (IOException e) {
         // do nothing
      }
      return n;
   }

   public static Note getNotesFileFromFTP(String filename) {
      Note n = null;
      try {
         UserSettings us = MainApp.getUserSettings();
         String ftpString = "ftp://" + us.getFtpUser() + ":"
               + us.getFtpPasswdString() + "@" + us.getFtpServer()
               + us.getFtpDir() + "notes.dat;type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         InputStream is = urlc.getInputStream(); // to download
         ObjectInputStream ois = new ObjectInputStream(is);
         n = (Note) ois.readObject();
         ois.close();
      } catch (Exception e) {
         n = MainApp.getMainApp().getFailNote();
         JOptionPane.showMessageDialog(null, "Could not download file from FTP server!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
      }
      return n;
   }

   public static void writeNotesToFTP(Note n, String filename) {
      boolean uploaded = true;
      try {
         UserSettings us = MainApp.getUserSettings();
         String ftpString = "ftp://" + us.getFtpUser() + ":"
               + us.getFtpPasswdString() + "@" + us.getFtpServer()
               + us.getFtpDir() + filename + ";type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         OutputStream os = urlc.getOutputStream(); // To upload
         ObjectOutputStream oos = new ObjectOutputStream(os);
         oos.writeObject(n);
         oos.close();
      } catch (Exception e) {
         uploaded = false;
         JOptionPane.showMessageDialog(null, "Error! Notes could not be uploaded to FTP server!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
      }
      if (uploaded) {
         JOptionPane.showMessageDialog(null, "Notes successfully uploaded to FTP server!", "pin 'em up - information", JOptionPane.INFORMATION_MESSAGE);
      }
   }
   
   public static void ExportNotesToTextFile(Note n, boolean[] catExport) {
      File f = null;
      MainApp.getFileDialog().setDialogTitle("Export notes to text-file");
      if (MainApp.getFileDialog().showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
         f = MainApp.getFileDialog().getSelectedFile();
      }
      
      if (f != null) {         
         try {
            PrintWriter ostream = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            // write text of notes to file
            Note head = n;
            for (int i=0; i<5; i++) {
               n = head;
               if (catExport[i]) {
                  ostream.println("Category: "+MainApp.getUserSettings().getCategoryNames()[i]);
                  ostream.println();
                  while (n != null) {
                     if (n.getCategory() == i) {
                        ostream.println(n.getText());
                        ostream.println();
                        ostream.println("---------------------");
                        ostream.println();
                     }
                     n = n.getNext();
                  }
                  ostream.println();
                  ostream.println("################################################################");
                  ostream.println();
               }
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

}
