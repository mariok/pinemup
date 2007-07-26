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
   private static final String VERSION = "0.3-svn";
   
   private Note failNote;
   
   private static PinEmUp main;

   private static JFileChooser fileDialog;
   
   private UserSettings settings;

   private TrayMenu menu;
   
   private CategoryList categories;

   public static JFileChooser getFileDialog() {
      return fileDialog;
   }
   
   public TrayMenu getTrayMenu() {
      return menu;
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
      TrayIcon icon = null;

      if (SystemTray.isSupported()) {
         // for FTP issues
         failNote = new Note();
         
         PinEmUp.setPinEmUp(this);

         // load user settings
         settings = new UserSettings();

         SystemTray tray = SystemTray.getSystemTray();

         Image img = ResourceLoader.getTrayIcon();

         //load notes from file
         categories = NoteIO.readCategoriesFromFile(settings);

         // create trayicon
         icon = new TrayIcon(img, "pin 'em up", new TrayMenu(categories,settings));
         icon.setImageAutoSize(true);
         
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
                  
         // create File-Dialog
         fileDialog = new JFileChooser();
         fileDialog.removeChoosableFileFilter(fileDialog.getChoosableFileFilters()[0]);
         fileDialog.setFileFilter(new MyFileFilter("TXT"));
         fileDialog.setMultiSelectionEnabled(false);
         
         //set focus to icon
         
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
