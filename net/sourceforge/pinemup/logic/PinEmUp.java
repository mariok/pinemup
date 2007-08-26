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

import java.awt.*;

import javax.swing.*;
import net.sourceforge.pinemup.menus.TrayMenu;

public class PinEmUp {
   private static final String VERSION = "0.3";
   
   private Note failNote;
   
   private static PinEmUp main;

   private static JFileChooser fileDialog, exportFileDialog;
   
   private UserSettings settings;

   private TrayIcon icon;
   
   private CategoryList categories;

   public static JFileChooser getFileDialog() {
      return fileDialog;
   }

   public static JFileChooser getExportFileDialog() {
      return exportFileDialog;
   }
   
   public TrayIcon getTrayIcon() {
      return icon;
   }

   public static void setPinEmUp(PinEmUp m) {
      main = m;
   }

   public static PinEmUp getMainApp() {
      return main;
   }

   public Note getFailNote() {
      return failNote;
   }

   public void exit() {
      //save notes to file
      NoteIO.writeCategoriesToFile(categories, settings);
      
      System.exit(0);
   }

   public PinEmUp() {
      if (SystemTray.isSupported()) {
         
         // for FTP issues
         failNote = new Note();
         
         PinEmUp.setPinEmUp(this);

         // load user settings
         settings = new UserSettings();

         SystemTray tray = SystemTray.getSystemTray();

         Image img = ResourceLoader.getTrayIcon();
         
         // create File-Dialog for notesfile
         fileDialog = new JFileChooser();
         fileDialog.setDialogTitle("select notes file");
         fileDialog.removeChoosableFileFilter(fileDialog.getChoosableFileFilters()[0]);
         fileDialog.setFileFilter(new MyFileFilter("XML"));
         fileDialog.setMultiSelectionEnabled(false);

         // create File-Dialog for export
         exportFileDialog = new JFileChooser();
         exportFileDialog.setDialogTitle("select text-file for export");
         exportFileDialog.removeChoosableFileFilter(exportFileDialog.getChoosableFileFilters()[0]);
         exportFileDialog.setFileFilter(new MyFileFilter("TXT"));
         exportFileDialog.setMultiSelectionEnabled(false);
         
         //load notes from file
         categories = NoteIO.readCategoriesFromFile(settings);
         
         // create trayicon
         icon = new TrayIcon(img, "pin 'em up", new TrayMenu(categories,settings));
         icon.setImageAutoSize(false);
         
         IconClickLogic myIconListener = new IconClickLogic(categories,settings);
         // add actionlistener for doubleclick on icon
         icon.addActionListener(myIconListener);
         // add mouselistener for traymenu
         icon.addMouseListener(myIconListener);

         // add trayicon
         try {
            tray.add(icon);
         } catch (AWTException e) {
            System.err.println(e);
         }

         //show all visible notes
         categories.showAllNotesNotHidden();
         
         //udate check
         if (settings.isUpdateCheckEnabled()) {
            new UpdateCheckThread(false);
         }          
      } else {
         JOptionPane.showMessageDialog(null, "Error! TrayIcon not supported by your system. Exiting...", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }

   public static void main(String args[]) {
      new PinEmUp();
   }
   
   public static String getVersion() {
      return VERSION;
   }
}
