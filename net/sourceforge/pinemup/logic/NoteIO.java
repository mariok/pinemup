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

public class NoteIO {
   public static void writeCategoriesToFile(CategoryList c, String filename) {
      //TODO: write to file
      /*try {
         FileOutputStream fs = new FileOutputStream(filename);
         ObjectOutputStream os = new ObjectOutputStream(fs);
         os.writeObject(n);
         os.close();
      } catch (IOException e) {
         JOptionPane.showMessageDialog(null, "Could save notes to file! Please check file-settings and free disk-space!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
      }*/
   }

   public static CategoryList readCategoriesFromFile(String filename) {
      CategoryList c = new CategoryList();
      c.add(new Category("TEST1",new NoteList(),true));
      //TODO:read from file
      return c;
   }

   public static CategoryList getCategoriesFromFTP(String filename) {
      CategoryList c = new CategoryList();
      //TODO: load from FTP
      /*try {
         UserSettings us = PinEmUp.getUserSettings();
         String ftpString = "ftp://" + us.getFtpUser() + ":"
               + us.getFtpPasswdString() + "@" + us.getFtpServer()
               + us.getFtpDir() + filename + ";type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         InputStream is = urlc.getInputStream(); // to download
         ObjectInputStream ois = new ObjectInputStream(is);
         n = (Note) ois.readObject();
         ois.close();
      } catch (Exception e) {
         n = PinEmUp.getMainApp().getFailNote();
         JOptionPane.showMessageDialog(null, "Could not download file from FTP server!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
      }*/
      return c;
   }

   public static void writeCategoriesToFTP(CategoryList c, String filename) {
      boolean uploaded = true;
      //TODO: upload
      /*try {
         UserSettings us = PinEmUp.getUserSettings();
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
      }*/
   }
   
   public static void exportCategoriesToTextFile(CategoryList c, boolean[] catExport) {
      //TODO: rewrite function
      /*
      File f = null;
      PinEmUp.getFileDialog().setDialogTitle("Export notes to text-file");
      PinEmUp.getFileDialog().setFileFilter(new MyFileFilter("TXT"));
      if (PinEmUp.getFileDialog().showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
         //f = PinEmUp.getFileDialog().getSelectedFile();
         String name = NoteIO.checkAndAddExtension(PinEmUp.getFileDialog().getSelectedFile().getAbsolutePath(), ".txt");
         f = new File(name);
      }
      if (f != null) {
         try {
            PrintWriter ostream = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            // write text of notes to file
            Note head = n;
            for (int i=0; i<5; i++) {
               n = head;
               if (catExport[i]) {
                  ostream.println("Category: "+PinEmUp.getUserSettings().getCategoryNames()[i]);
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
      */
   }
   
   public static String checkAndAddExtension(String s, String xt) {
      int len = s.length();
      String ext = s.substring(len-4, len);
      if (!ext.toLowerCase().equals(xt.toLowerCase())) {
         s = s + xt.toLowerCase();
      }
      return s;
   }

}
