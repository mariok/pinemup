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

import net.sourceforge.pinemup.core.I18N;

public class TrayMenu extends PopupMenu {
   private static final long serialVersionUID = 4859510599893727949L;

   private Menu categoriesMenu;
   private MenuItem manageCategoriesItem;
   private MenuCreator menuCreator;
   private GeneralMenuLogic menuLogic;

   public TrayMenu() {
      super("pin 'em up");
      menuCreator = new MenuCreator();
      menuLogic = new GeneralMenuLogic();
      init();
   }

   public void init() {
      removeAll();

      // add basic items
      for (MenuItem item : menuCreator.getBasicMenuItems()) {
         add(item);
      }
      addSeparator();

      // categories menus
      categoriesMenu = new Menu(I18N.getInstance().getString("menu.categorymenu"));
      add(categoriesMenu);

      // category actions
      manageCategoriesItem = new MenuItem(I18N.getInstance().getString("menu.categorymenu.managecategoriesitem"));
      manageCategoriesItem.setActionCommand(GeneralMenuLogic.ACTION_MANAGE_CATEGORIES);
      manageCategoriesItem.addActionListener(menuLogic);
      createCategoriesMenu();

      // im-/export menu
      addSeparator();
      Menu imExMenu = new Menu(I18N.getInstance().getString("menu.notesimexport"));
      MenuItem serverUploadItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.serveruploaditem"));
      serverUploadItem.setActionCommand(GeneralMenuLogic.ACTION_UPLOAD_TO_SERVER);
      serverUploadItem.addActionListener(menuLogic);
      imExMenu.add(serverUploadItem);
      MenuItem serverDownloadItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.serverdownloaditem"));
      serverDownloadItem.setActionCommand(GeneralMenuLogic.ACTION_DOWNLOAD_FROM_SERVER);
      serverDownloadItem.addActionListener(menuLogic);
      imExMenu.add(serverDownloadItem);
      imExMenu.addSeparator();
      MenuItem exportItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.textexportitem"));
      exportItem.setActionCommand(GeneralMenuLogic.ACTION_EXPORT);
      exportItem.addActionListener(menuLogic);
      imExMenu.add(exportItem);
      add(imExMenu);

      // other items
      addSeparator();
      MenuItem showSettingsDialogItem = new MenuItem(I18N.getInstance().getString("menu.settingsitem"));
      showSettingsDialogItem.setActionCommand(GeneralMenuLogic.ACTION_SHOW_SETTINGS_DIALOG);
      showSettingsDialogItem.addActionListener(menuLogic);
      add(showSettingsDialogItem);

      // help menu
      Menu helpMenu = new Menu(I18N.getInstance().getString("menu.help"));
      MenuItem updateItem = new MenuItem(I18N.getInstance().getString("menu.help.updatecheckitem"));
      updateItem.setActionCommand(GeneralMenuLogic.ACTION_CHECK_FOR_UPDATES);
      updateItem.addActionListener(menuLogic);
      helpMenu.add(updateItem);
      helpMenu.addSeparator();
      MenuItem aboutItem = new MenuItem(I18N.getInstance().getString("menu.help.aboutitem"));
      aboutItem.setActionCommand(GeneralMenuLogic.ACTION_SHOW_ABOUT_DIALOG);
      aboutItem.addActionListener(menuLogic);
      helpMenu.add(aboutItem);
      add(helpMenu);
      addSeparator();

      // close item
      MenuItem closeItem = new MenuItem(I18N.getInstance().getString("menu.exititem"));
      closeItem.setActionCommand(GeneralMenuLogic.ACTION_EXIT_APPLICATION);
      closeItem.addActionListener(menuLogic);
      add(closeItem);
   }

   public void createCategoriesMenu() {
      categoriesMenu.removeAll();
      for (Menu m : menuCreator.getCategoryMenus()) {
         categoriesMenu.add(m);
      }
      categoriesMenu.addSeparator();
      categoriesMenu.add(manageCategoriesItem);
   }
}
