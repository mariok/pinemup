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

import java.awt.event.ActionEvent;

import javax.swing.*;

import net.sourceforge.pinemup.gui.NoteWindow;
import net.sourceforge.pinemup.logic.*;

public class RightClickMenu extends BasicMenu {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private NoteWindow parentWindow;
   
   private Category myCat;
   
   private JMenuItem deleteNoteItem, alwaysOnTopOnItem, alwaysOnTopOffItem;
   
   private JMenuItem[] setFontSizeItem, setCategoryItem;
   
   public RightClickMenu(NoteWindow w, CategoryList c, UserSettings s) {
      super(c,s);
      parentWindow = w;
      myCat = categories.getCategoryForNote(parentWindow.getParentNote());

      deleteNoteItem = new JMenuItem("delete this note");
      deleteNoteItem.addActionListener(this);
      add(deleteNoteItem);
      addSeparator();
      
      // settings menu
      JMenu settingsMenu = new JMenu("note settings");
      JMenu setFontSizeMenu = new JMenu("font size");
      setFontSizeItem = new JMenuItem[26];
      for (int i=0; i<26; i++) {
         setFontSizeItem[i] = new JMenuItem(String.valueOf(i+5));
         if (i+5 == parentWindow.getParentNote().getFontSize()) {
            setFontSizeItem[i].setText("# "+setFontSizeItem[i].getText());
         } else {
            setFontSizeItem[i].setText("  "+setFontSizeItem[i].getText());
         }
         setFontSizeItem[i].addActionListener(this);
         setFontSizeMenu.add(setFontSizeItem[i]);
      }
      JMenu setCategoryMenu = new JMenu("category");
      
      setCategoryItem = new JMenuItem[categories.getNumberOfCategories()];
      for (int i=0; i<setCategoryItem.length; i++) {
         setCategoryItem[i] = new JMenuItem((i+1) + " " + categories.getNames()[i]);
         setCategoryItem[i].addActionListener(this);
         setCategoryMenu.add(setCategoryItem[i]);
      }

      JMenu alwaysOnTopMenu = new JMenu("always on top");
      String[] aot = {"  ","  "};
      if(parentWindow.getParentNote().isAlwaysOnTop()) {
         aot[0] = "# ";
      } else {
         aot[1] = "# ";
      }
      alwaysOnTopOnItem = new JMenuItem(aot[0]+"enabled");
      alwaysOnTopOffItem = new JMenuItem(aot[1]+"disabled");
      alwaysOnTopOnItem.addActionListener(this);
      alwaysOnTopOffItem.addActionListener(this);
      alwaysOnTopMenu.add(alwaysOnTopOnItem);
      alwaysOnTopMenu.add(alwaysOnTopOffItem);
                 
      settingsMenu.add(alwaysOnTopMenu);
      settingsMenu.add(setCategoryMenu);
      settingsMenu.add(setFontSizeMenu);
      add(settingsMenu);
      addSeparator();
      
      // category menu
      CategoryActionsSubMenu categoryMenu = new CategoryActionsSubMenu("category actions",myCat,categories);
      add(categoryMenu);
   }
   
   public boolean checkActionEvent(ActionEvent e) {
      boolean actionFound = super.checkActionEvent(e);
      if (!actionFound) {
         Object src = e.getSource();
         if (src == deleteNoteItem) {
            actionFound = true;
            if (myCat != null) {
               myCat.getNotes().remove(parentWindow.getParentNote());
            }
         } else if (src == alwaysOnTopOnItem) {
            actionFound = true;
            parentWindow.getParentNote().setAlwaysOnTop(true);
         } else if (src == alwaysOnTopOffItem) {
            actionFound = true;
            parentWindow.getParentNote().setAlwaysOnTop(false);
         } else {
            CategoryList tempCat = categories;
            for (int i=0; i<setCategoryItem.length; i++) {
               if (src == setCategoryItem[i]) {
                  actionFound = true;
                  parentWindow.getParentNote().moveToCategory(tempCat.getCategory());
               }
               tempCat = tempCat.getNext();
            }
            if (!actionFound) {
               for (int i=0; i<setFontSizeItem.length; i++) {
                  if (src == setFontSizeItem[i]) {
                     actionFound = true;
                     parentWindow.getParentNote().setFontSize((short)(i+5));
                     parentWindow.refreshView();
                  }
               }
            }
         }
         
         // save notes to file after every change
         NoteIO.writeCategoriesToFile(categories, settings);
      }
      return actionFound;
   }

}
