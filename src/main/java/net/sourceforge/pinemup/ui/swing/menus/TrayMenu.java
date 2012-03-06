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

package net.sourceforge.pinemup.ui.swing.menus;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.UpdateCheckThread;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NotesFileManager;
import net.sourceforge.pinemup.io.ServerThread;
import net.sourceforge.pinemup.ui.swing.AboutDialog;
import net.sourceforge.pinemup.ui.swing.CategoryDialog;
import net.sourceforge.pinemup.ui.swing.ExportDialog;
import net.sourceforge.pinemup.ui.swing.I18N;
import net.sourceforge.pinemup.ui.swing.SettingsDialog;

public class TrayMenu extends PopupMenu implements ActionListener {
   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private MenuItem manageCategoriesItem, exportItem, aboutItem, updateItem, closeItem, showSettingsDialogItem, serverUploadItem,
         serverDownloadItem;

   public TrayMenu() {
      super("pin 'em up");

      // add basic items
      MenuItem[] basicItems = (new MenuCreator()).getBasicMenuItems();
      for (int i = 0; i < basicItems.length; i++) {
         add(basicItems[i]);
      }
      addSeparator();

      // categories menus
      Menu categoriesMenu = new Menu(I18N.getInstance().getString("menu.categorymenu"));
      add(categoriesMenu);
      Menu[] catMenu = new Menu[CategoryManager.getInstance().getNumberOfCategories()];

      // Category menu items
      int i = 0;
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         catMenu[i] = (new MenuCreator()).getCategoryActionsMenu((i + 1) + " " + CategoryManager.getInstance().getCategoryNames()[i], cat);
         categoriesMenu.add(catMenu[i]);
         i++;
      }

      // other category actions
      categoriesMenu.addSeparator();
      manageCategoriesItem = new MenuItem(I18N.getInstance().getString("menu.categorymenu.managecategoriesitem"));
      manageCategoriesItem.addActionListener(this);
      categoriesMenu.add(manageCategoriesItem);

      // im-/export menu
      addSeparator();
      Menu imExMenu = new Menu(I18N.getInstance().getString("menu.notesimexport"));
      serverUploadItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.serveruploaditem"));
      serverUploadItem.addActionListener(this);
      imExMenu.add(serverUploadItem);
      serverDownloadItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.serverdownloaditem"));
      serverDownloadItem.addActionListener(this);
      imExMenu.add(serverDownloadItem);
      imExMenu.addSeparator();
      exportItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.textexportitem"));
      exportItem.addActionListener(this);
      imExMenu.add(exportItem);
      add(imExMenu);

      // other items
      addSeparator();
      showSettingsDialogItem = new MenuItem(I18N.getInstance().getString("menu.settingsitem"));
      showSettingsDialogItem.addActionListener(this);
      add(showSettingsDialogItem);

      // help menu
      Menu helpMenu = new Menu(I18N.getInstance().getString("menu.help"));
      updateItem = new MenuItem(I18N.getInstance().getString("menu.help.updatecheckitem"));
      updateItem.addActionListener(this);
      helpMenu.add(updateItem);
      helpMenu.addSeparator();
      aboutItem = new MenuItem(I18N.getInstance().getString("menu.help.aboutitem"));
      aboutItem.addActionListener(this);
      helpMenu.add(aboutItem);
      add(helpMenu);
      addSeparator();

      // close item
      closeItem = new MenuItem(I18N.getInstance().getString("menu.exititem"));
      closeItem.addActionListener(this);
      add(closeItem);
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == aboutItem) {
         new AboutDialog();
      } else if (src == showSettingsDialogItem) {
         SettingsDialog.showInstance();
      } else if (src == closeItem) {
         // save notes to file and exit
         NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories());
         System.exit(0);
      } else if (src == serverUploadItem) {
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacefileonserver"), I18N.getInstance()
                     .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // save notes to file
            NotesFileManager.getInstance().writeCategoriesToFile(CategoryManager.getInstance().getCategories());
            // copy file to server
            new ServerThread(ServerThread.UPLOAD);
         }
      } else if (src == serverDownloadItem) {
         if (!UserSettings.getInstance().getConfirmUpDownload()
               || JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.replacelocalfile"), I18N.getInstance()
                     .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            new ServerThread(ServerThread.DOWNLOAD);
         }
      } else if (src == exportItem) {
         new ExportDialog();
      } else if (src == manageCategoriesItem) {
         CategoryDialog.showInstance();
      } else if (src == updateItem) {
         new UpdateCheckThread(true);
      }
   }
}
