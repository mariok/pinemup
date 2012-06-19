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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;

class MenuCreator {
   private String[] getBasicItemTexts() {
      String[] s = {I18N.getInstance().getString("menu.addnoteitem"), I18N.getInstance().getString("menu.showallnotesitem"),
            I18N.getInstance().getString("menu.hideallnotesitem")};
      return s;
   }

   private String[] getBasicActionCommands() {
      String[] s = {GeneralMenuLogic.ACTION_ADD_NOTE, GeneralMenuLogic.ACTION_SHOW_ALL_NOTES, GeneralMenuLogic.ACTION_HIDE_ALL_NOTES};
      return s;
   }

   private String[] getCategoryItemTexts() {
      String[] s = {I18N.getInstance().getString("menu.categorymenu.hidenotes"),
            I18N.getInstance().getString("menu.categorymenu.shownotes"), I18N.getInstance().getString("menu.categorymenu.showonlynotes"),
            I18N.getInstance().getString("menu.categorymenu.setasdefault")};
      return s;
   }

   private String[] getCategoryActionCommands() {
      String[] s = {CategoryMenuLogic.ACTION_HIDE_ALL_NOTES, CategoryMenuLogic.ACTION_SHOW_ALL_NOTES,
            CategoryMenuLogic.ACTION_SHOW_ONLY_NOTES_OF_CATEGORY, CategoryMenuLogic.ACTION_SET_AS_DEFAULT_CATEGORY};
      return s;
   }

   public List<JMenuItem> getBasicJMenuItems() {
      String[] texts = getBasicItemTexts();
      String[] actions = getBasicActionCommands();
      GeneralMenuLogic basicMenuLogic = new GeneralMenuLogic();
      List<JMenuItem> menuItems = new ArrayList<JMenuItem>();
      for (int i = 0; i < texts.length; i++) {
         JMenuItem menuItem = new JMenuItem(texts[i]);
         menuItem.setActionCommand(actions[i]);
         menuItem.addActionListener(basicMenuLogic);
         menuItems.add(menuItem);
      }
      return menuItems;
   }

   public List<MenuItem> getBasicMenuItems() {
      String[] texts = getBasicItemTexts();
      String[] actions = getBasicActionCommands();
      GeneralMenuLogic basicMenuLogic = new GeneralMenuLogic();
      List<MenuItem> menuItems = new ArrayList<MenuItem>();
      for (int i = 0; i < texts.length; i++) {
         MenuItem menuItem = new MenuItem(texts[i]);
         menuItem.setActionCommand(actions[i]);
         menuItem.addActionListener(basicMenuLogic);
         menuItems.add(menuItem);
      }
      return menuItems;
   }

   public JMenu getCategoryActionsJMenu(String title, Category c) {
      JMenu menu = new JMenu(title);
      String[] texts = getCategoryItemTexts();
      String[] actions = getCategoryActionCommands();
      CategoryMenuLogic catMenuLogic = new CategoryMenuLogic(c);
      for (int i = 0; i < texts.length; i++) {
         JMenuItem menuItem = new JMenuItem(texts[i]);
         menuItem.setActionCommand(actions[i]);
         menuItem.addActionListener(catMenuLogic);
         menu.add(menuItem);
         if (i == 2) {
            menu.addSeparator();
         }
      }
      return menu;
   }

   private Menu getCategoryActionsMenu(String title, Category c) {
      Menu menu = new Menu(title);
      String[] texts = getCategoryItemTexts();
      String[] actions = getCategoryActionCommands();
      CategoryMenuLogic catMenuLogic = new CategoryMenuLogic(c);
      for (int i = 0; i < texts.length; i++) {
         MenuItem menuItem = new MenuItem(texts[i]);
         menuItem.setActionCommand(actions[i]);
         menuItem.addActionListener(catMenuLogic);
         menu.add(menuItem);
         if (i == 2) {
            menu.addSeparator();
         }
      }
      return menu;
   }

   public List<Menu> getCategoryMenus() {
      List<Menu> categoryMenus = new ArrayList<Menu>();
      for (Category cat : CategoryManager.getInstance().getCategories()) {
         Menu catMenu = getCategoryActionsMenu(cat.getName(), cat);
         categoryMenus.add(catMenu);
      }
      return categoryMenus;
   }
}
