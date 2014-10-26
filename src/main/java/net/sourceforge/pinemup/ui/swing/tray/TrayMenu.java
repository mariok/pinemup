/*
 * pin 'em up
 *
 * Copyright (C) 2007-2013 by Mario Ködding
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

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.UserInputRetriever;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.NotesSaveTrigger;
import net.sourceforge.pinemup.core.io.file.NotesFileReader;
import net.sourceforge.pinemup.core.io.file.NotesFileWriter;
import net.sourceforge.pinemup.core.io.resources.ResourceLoader;
import net.sourceforge.pinemup.core.io.updatecheck.UpdateCheckResultHandler;
import net.sourceforge.pinemup.core.model.Category;
import net.sourceforge.pinemup.ui.swing.dialogs.DialogFactory;
import net.sourceforge.pinemup.ui.swing.menus.logic.CategoryMenuLogic;
import net.sourceforge.pinemup.ui.swing.menus.logic.CategoryMenuLogic.CategoryAction;
import net.sourceforge.pinemup.ui.swing.menus.logic.GeneralMenuLogic;
import net.sourceforge.pinemup.ui.swing.menus.logic.GeneralMenuLogic.GeneralAction;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.util.ArrayList;
import java.util.List;

public class TrayMenu extends JPopupMenu {
   private static final long serialVersionUID = 4859510599893727949L;

   private JMenu categoriesMenu;
   private JMenuItem manageCategoriesItem;
   private final TrayMenuLogic trayMenuLogic;

   public TrayMenu(DialogFactory dialogFactory, UserInputRetriever userInputRetriever,
         UpdateCheckResultHandler updateCheckResultHandler, NotesFileReader notesFileReader,
         NotesFileWriter notesFileWriter, NotesSaveTrigger notesSaveTrigger, ResourceLoader resourceLoader) {
      super("pin 'em up");
      trayMenuLogic = new TrayMenuLogic(dialogFactory, userInputRetriever, updateCheckResultHandler,
            notesFileReader, notesFileWriter, notesSaveTrigger, resourceLoader);
      initWithNewLanguage();
   }

   public final void initWithNewLanguage() {
      removeAll();

      // add basic items
      for (JMenuItem item : getBasicMenuItems()) {
         add(item);
      }
      addSeparator();

      // categories menus
      categoriesMenu = new JMenu(I18N.getInstance().getString("menu.categorymenu"));
      add(categoriesMenu);

      // category actions
      manageCategoriesItem = new JMenuItem(I18N.getInstance().getString("menu.categorymenu.managecategoriesitem"));
      manageCategoriesItem.setActionCommand(TrayMenuLogic.ACTION_MANAGE_CATEGORIES);
      manageCategoriesItem.addActionListener(trayMenuLogic);
      createCategoriesMenu();

      // im-/export menu
      addSeparator();
      JMenu imExMenu = new JMenu(I18N.getInstance().getString("menu.notesimexport"));
      JMenuItem serverUploadItem = new JMenuItem(I18N.getInstance().getString("menu.notesimexport.serveruploaditem"));
      serverUploadItem.setActionCommand(TrayMenuLogic.ACTION_UPLOAD_TO_SERVER);
      serverUploadItem.addActionListener(trayMenuLogic);
      imExMenu.add(serverUploadItem);
      JMenuItem serverDownloadItem = new JMenuItem(I18N.getInstance().getString("menu.notesimexport.serverdownloaditem"));
      serverDownloadItem.setActionCommand(TrayMenuLogic.ACTION_DOWNLOAD_FROM_SERVER);
      serverDownloadItem.addActionListener(trayMenuLogic);
      imExMenu.add(serverDownloadItem);
      imExMenu.addSeparator();
      JMenuItem exportItem = new JMenuItem(I18N.getInstance().getString("menu.notesimexport.textexportitem"));
      exportItem.setActionCommand(TrayMenuLogic.ACTION_EXPORT);
      exportItem.addActionListener(trayMenuLogic);
      imExMenu.add(exportItem);
      add(imExMenu);

      // other items
      addSeparator();
      JMenuItem showSettingsDialogItem = new JMenuItem(I18N.getInstance().getString("menu.settingsitem"));
      showSettingsDialogItem.setActionCommand(TrayMenuLogic.ACTION_SHOW_SETTINGS_DIALOG);
      showSettingsDialogItem.addActionListener(trayMenuLogic);
      add(showSettingsDialogItem);

      // help menu
      JMenu helpMenu = new JMenu(I18N.getInstance().getString("menu.help"));
      JMenuItem updateItem = new JMenuItem(I18N.getInstance().getString("menu.help.updatecheckitem"));
      updateItem.setActionCommand(TrayMenuLogic.ACTION_CHECK_FOR_UPDATES);
      updateItem.addActionListener(trayMenuLogic);
      helpMenu.add(updateItem);
      helpMenu.addSeparator();
      JMenuItem aboutItem = new JMenuItem(I18N.getInstance().getString("menu.help.aboutitem"));
      aboutItem.setActionCommand(TrayMenuLogic.ACTION_SHOW_ABOUT_DIALOG);
      aboutItem.addActionListener(trayMenuLogic);
      helpMenu.add(aboutItem);
      add(helpMenu);
      addSeparator();

      // close item
      JMenuItem closeItem = new JMenuItem(I18N.getInstance().getString("menu.exititem"));
      closeItem.setActionCommand(TrayMenuLogic.ACTION_EXIT_APPLICATION);
      closeItem.addActionListener(trayMenuLogic);
      add(closeItem);
   }

   public void createCategoriesMenu() {
      categoriesMenu.removeAll();
      for (JMenu m : getCategoryMenus()) {
         categoriesMenu.add(m);
      }
      categoriesMenu.addSeparator();
      categoriesMenu.add(manageCategoriesItem);
   }

   private List<JMenu> getCategoryMenus() {
      List<JMenu> categoryMenus = new ArrayList<>();
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         categoryMenus.add(createCategoryActionsMenu(cat.getName(), cat));
      }
      return categoryMenus;
   }

   private JMenu createCategoryActionsMenu(String title, Category c) {
      JMenu menu = new JMenu(title);
      CategoryMenuLogic catMenuLogic = new CategoryMenuLogic(c);
      int i = 0;
      for (CategoryAction action : CategoryAction.values()) {
         JMenuItem menuItem = new JMenuItem(I18N.getInstance().getString(action.getI18nKey()));
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

   private List<JMenuItem> getBasicMenuItems() {
      GeneralMenuLogic basicMenuLogic = new GeneralMenuLogic();
      List<JMenuItem> menuItems = new ArrayList<>();
      for (GeneralAction action : GeneralAction.values()) {
         JMenuItem menuItem = new JMenuItem(I18N.getInstance().getString(action.getI18nKey()));
         menuItem.setActionCommand(action.toString());
         menuItem.addActionListener(basicMenuLogic);
         menuItems.add(menuItem);
      }
      return menuItems;
   }
}
