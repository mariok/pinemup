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

package net.sourceforge.pinemup.menus;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.sourceforge.pinemup.gui.*;
import net.sourceforge.pinemup.logic.*;

public class TrayMenu extends PopupMenu {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private MenuItem addCategoryItem, exportItem, aboutItem, closeItem, showSettingsDialogItem, ftpUploadItem, ftpDownloadItem;
   
   private UserSettings settings;
   
   private CategoryList categories;

   public TrayMenu(CategoryList c, UserSettings s) {
      // title and notes actions
      categories = c;
      settings = s;
      
      //create MenuCreator
      MenuCreator myMenuCreator = new MenuCreator(categories,settings);
      
      //add basic items
      MenuItem[] basicItems = myMenuCreator.getBasicMenuItems();
      for (int i=0; i<basicItems.length;i++) {
         add(basicItems[i]);
      }
      addSeparator();
      
      // categories menus
      Menu categoriesMenu = new Menu("category actions");
      add(categoriesMenu);
      Menu[] catMenu = new Menu[categories.getNumberOfCategories()];
      
      //Category menu items
      CategoryList tempCL = categories;
      Category myCat = null;
      for (int i=0; i<catMenu.length; i++) {
         if (tempCL != null) {
            myCat = tempCL.getCategory();
            tempCL = tempCL.getNext();
         }
         catMenu[i] = myMenuCreator.getCategoryActionsMenu((i+1) + " " + categories.getNames()[i]);
         categoriesMenu.add(catMenu[i]);
      }
      
      //other category actions
      addCategoryItem = new JMenuItem("add new category");
      addCategoryItem.addActionListener(this);
      categoriesMenu.addSeparator();
      categoriesMenu.add(addCategoryItem);
      
      // im-/export menu      
      addSeparator();
      JMenu imExMenu = new JMenu("notes im-/export");
      JMenu ftpMenu = new JMenu("ftp");
      ftpUploadItem = new JMenuItem("upload to ftp server");
      ftpUploadItem.addActionListener(this);
      ftpMenu.add(ftpUploadItem);
      ftpDownloadItem = new JMenuItem("download from ftp server");
      ftpDownloadItem.addActionListener(this);
      ftpMenu.add(ftpDownloadItem);
      imExMenu.add(ftpMenu);
      imExMenu.addSeparator();
      exportItem = new JMenuItem("export to textfile");
      exportItem.addActionListener(this);
      imExMenu.add(exportItem);
      add(imExMenu);
      
      // other items
      addSeparator();
      showSettingsDialogItem = new JMenuItem("settings");
      showSettingsDialogItem.addActionListener(this);
      add(showSettingsDialogItem);
      JMenu helpMenu = new JMenu("help");
      aboutItem = new JMenuItem("about pin 'em up");
      aboutItem.addActionListener(this);
      helpMenu.add(aboutItem);
      add(helpMenu);
      addSeparator();
      closeItem = new JMenuItem("exit");
      closeItem.addActionListener(this);
      add(closeItem);
   }
   
   public boolean checkActionEvent(ActionEvent e) {
      boolean actionFound = super.checkActionEvent(e);
      if (!actionFound) {
         Object src = e.getSource();
         if (src == aboutItem) {
            new AboutDialog();
         } else if (src == showSettingsDialogItem) {
            new SettingsDialog(settings,categories);
         } else if (src == closeItem) {
            PinEmUp.getMainApp().exit();
         } else if (src == ftpUploadItem) {
            if (JOptionPane.showConfirmDialog(this, "Notesfile on server will be replaced, if it already exists! Proceed?","Upload Notesfile",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
               //save notes to file
               NoteIO.writeCategoriesToFile(categories, settings);
               //copy file to ftp
               new FTPThread(true,categories,settings);
            }
         } else if (src == ftpDownloadItem) {
            if (JOptionPane.showConfirmDialog(this, "Your current notesfile will be replaced by the version on the FTP server! Proceed?","Download Notesfile",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
               new FTPThread(false,categories, settings);
            }
         } else if (src == exportItem) {
            new ExportDialog(categories);
         } else if (src == addCategoryItem) {
            if (categories.getNumberOfCategories()<1000) {
               String cname = JOptionPane.showInputDialog(this, "Category name:");
               if (cname != null) {
                  categories.add(new Category(cname,new NoteList(),false));
               }               
            }
         }
         
         // save notes to file after every change
         NoteIO.writeCategoriesToFile(categories, settings);
      }
      return actionFound;
   }

   public void focusGained(FocusEvent arg0) {
      // do nothing
      
   }

   public void focusLost(FocusEvent arg0) {
      setVisible(false);
   }
}
