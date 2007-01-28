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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainApp {
   private static final String VERSION = "0.2-svn";
   
   public Note notes, failNote;

   private static MainApp main;

   private static UserSettings settings;

   private static MenuLogic menuListener;

   private TrayMenu menu;

   public TrayMenu getTrayMenu() {
      return menu;
   }

   public static void setMainApp(MainApp m) {
      main = m;
   }

   public static MainApp getMainApp() {
      return main;
   }

   public Note getFailNote() {
      return failNote;
   }

   public static void setUserSettings(UserSettings s) {
      settings = s;
   }

   public static UserSettings getUserSettings() {
      return settings;
   }

   public static MenuLogic getMenuListener() {
      return menuListener;
   }

   public static void setMenuListener(MenuLogic m) {
      menuListener = m;
   }

   public void exit() {
      // save notes to file
      NoteIO.writeNotesToFile(getNotes(), NoteIO.getFileName());
      
      System.exit(0);
   }

   public MainApp() {
      TrayIcon icon = null;

      if (SystemTray.isSupported()) {
         // for FTP issues
         failNote = new Note();
         
         MainApp.setMainApp(this);
         MainApp.setMenuListener(new MenuLogic());

         // load user settings
         MainApp.setUserSettings(UserSettings.loadSettings("config.dat"));

         SystemTray tray = SystemTray.getSystemTray();

         Image img = ResourceLoader.loadImage("resources", "icon.gif");

         // create popup menu
         menu = new TrayMenu();

         // create trayicon
         icon = new TrayIcon(img, "pin 'em up", menu);
         icon.setImageAutoSize(true);
         
         // add actionlistener for doubleclick on icon
         icon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               MainApp.getMainApp().setNotes(Note.add(MainApp.getMainApp().getNotes(), ""));
               MainApp.getMainApp().getNotes().showAllVisible();
            }
         });

         // add trayicon
         try {
            tray.add(icon);
         } catch (AWTException e) {
            System.err.println(e);
         }

         // load notes from file
         notes = NoteIO.readNotesFromFile(NoteIO.getFileName());

         // show all visible notes
         if (notes != null) {
            notes.showAllVisible();
         }
         
      } else {
         JOptionPane.showMessageDialog(null, "Error! TrayIcon not supported by your system. Exiting...", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }

   public Note getNotes() {
      return notes;
   }

   public void setNotes(Note n) {
      notes = n;
   }

   public static void main(String args[]) {
      new MainApp();
   }
   
   public static String getVersion() {
      return VERSION;
   }
}
