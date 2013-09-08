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

package net.sourceforge.pinemup.ui.swing.tray;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.io.UpdateCheckResultHandler;
import net.sourceforge.pinemup.ui.UserInputRetriever;
import net.sourceforge.pinemup.ui.swing.dialogs.DialogFactory;
import net.sourceforge.pinemup.ui.swing.menus.logic.CategoryMenuLogic;
import net.sourceforge.pinemup.ui.swing.menus.logic.CategoryMenuLogic.CategoryAction;
import net.sourceforge.pinemup.ui.swing.menus.logic.GeneralMenuLogic;
import net.sourceforge.pinemup.ui.swing.menus.logic.GeneralMenuLogic.GeneralAction;

public class TrayMenu extends PopupMenu {
   private static final long serialVersionUID = 4859510599893727949L;

   private Menu categoriesMenu;
   private MenuItem manageCategoriesItem;

   public TrayMenu(DialogFactory dialogFactory, UserInputRetriever userInputRetriever, UpdateCheckResultHandler updateCheckResultHandler) {
      super("pin 'em up");

      removeAll();

      TrayMenuLogic trayMenuLogic = new TrayMenuLogic(dialogFactory, userInputRetriever, updateCheckResultHandler);

      // add basic items
      for (MenuItem item : getBasicMenuItems()) {
         add(item);
      }
      addSeparator();

      // categories menus
      categoriesMenu = new Menu(I18N.getInstance().getString("menu.categorymenu"));
      add(categoriesMenu);

      // category actions
      manageCategoriesItem = new MenuItem(I18N.getInstance().getString("menu.categorymenu.managecategoriesitem"));
      manageCategoriesItem.setActionCommand(TrayMenuLogic.ACTION_MANAGE_CATEGORIES);
      manageCategoriesItem.addActionListener(trayMenuLogic);
      createCategoriesMenu();

      // im-/export menu
      addSeparator();
      Menu imExMenu = new Menu(I18N.getInstance().getString("menu.notesimexport"));
      MenuItem serverUploadItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.serveruploaditem"));
      serverUploadItem.setActionCommand(TrayMenuLogic.ACTION_UPLOAD_TO_SERVER);
      serverUploadItem.addActionListener(trayMenuLogic);
      imExMenu.add(serverUploadItem);
      MenuItem serverDownloadItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.serverdownloaditem"));
      serverDownloadItem.setActionCommand(TrayMenuLogic.ACTION_DOWNLOAD_FROM_SERVER);
      serverDownloadItem.addActionListener(trayMenuLogic);
      imExMenu.add(serverDownloadItem);
      imExMenu.addSeparator();
      MenuItem exportItem = new MenuItem(I18N.getInstance().getString("menu.notesimexport.textexportitem"));
      exportItem.setActionCommand(TrayMenuLogic.ACTION_EXPORT);
      exportItem.addActionListener(trayMenuLogic);
      imExMenu.add(exportItem);
      add(imExMenu);

      // other items
      addSeparator();
      MenuItem showSettingsDialogItem = new MenuItem(I18N.getInstance().getString("menu.settingsitem"));
      showSettingsDialogItem.setActionCommand(TrayMenuLogic.ACTION_SHOW_SETTINGS_DIALOG);
      showSettingsDialogItem.addActionListener(trayMenuLogic);
      add(showSettingsDialogItem);

      // help menu
      Menu helpMenu = new Menu(I18N.getInstance().getString("menu.help"));
      MenuItem updateItem = new MenuItem(I18N.getInstance().getString("menu.help.updatecheckitem"));
      updateItem.setActionCommand(TrayMenuLogic.ACTION_CHECK_FOR_UPDATES);
      updateItem.addActionListener(trayMenuLogic);
      helpMenu.add(updateItem);
      helpMenu.addSeparator();
      MenuItem aboutItem = new MenuItem(I18N.getInstance().getString("menu.help.aboutitem"));
      aboutItem.setActionCommand(TrayMenuLogic.ACTION_SHOW_ABOUT_DIALOG);
      aboutItem.addActionListener(trayMenuLogic);
      helpMenu.add(aboutItem);
      add(helpMenu);
      addSeparator();

      // close item
      MenuItem closeItem = new MenuItem(I18N.getInstance().getString("menu.exititem"));
      closeItem.setActionCommand(TrayMenuLogic.ACTION_EXIT_APPLICATION);
      closeItem.addActionListener(trayMenuLogic);
      add(closeItem);
   }

   public void createCategoriesMenu() {
      categoriesMenu.removeAll();
      for (Menu m : getCategoryMenus()) {
         categoriesMenu.add(m);
      }
      categoriesMenu.addSeparator();
      categoriesMenu.add(manageCategoriesItem);
   }

   private List<Menu> getCategoryMenus() {
      List<Menu> categoryMenus = new ArrayList<Menu>();
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         Menu catMenu = getCategoryActionsMenu(cat.getName(), cat);
         categoryMenus.add(catMenu);
      }
      return categoryMenus;
   }

   private Menu getCategoryActionsMenu(String title, Category c) {
      Menu menu = new Menu(title);
      CategoryMenuLogic catMenuLogic = new CategoryMenuLogic(c);
      int i = 0;
      for (CategoryAction action : CategoryAction.values()) {
         MenuItem menuItem = new MenuItem(I18N.getInstance().getString(action.getI18nKey()));
         menuItem.setActionCommand(action.toString());
         menuItem.addActionListener(catMenuLogic);
         menu.add(menuItem);
         if (i == 2) {
            menu.addSeparator();
         }
         i++;
      }
      return menu;
   }

   private List<MenuItem> getBasicMenuItems() {
      GeneralMenuLogic basicMenuLogic = new GeneralMenuLogic();
      List<MenuItem> menuItems = new ArrayList<>();
      for (GeneralAction action : GeneralAction.values()) {
         MenuItem menuItem = new MenuItem(action.getI18nKey());
         menuItem.setActionCommand(action.toString());
         menuItem.addActionListener(basicMenuLogic);
         menuItems.add(menuItem);
      }
      return menuItems;
   }
}
