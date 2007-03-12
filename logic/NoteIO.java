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
   public static void writeNotesToFile(Note n, String filename) {
      try {
         FileOutputStream fs = new FileOutputStream(filename);
         ObjectOutputStream os = new ObjectOutputStream(fs);
         os.writeObject(n);
         os.close();
      } catch (IOException e) {
         JOptionPane.showMessageDialog(null, "Could save notes to file! Please check file-settings and free disk-space!", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
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
      }
      return n;
   }

   public static void writeNotesToFTP(Note n, String filename) {
      boolean uploaded = true;
      try {
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
      }
   }
   
   public static void ExportNotesToTextFile(Note n, boolean[] catExport) {
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
