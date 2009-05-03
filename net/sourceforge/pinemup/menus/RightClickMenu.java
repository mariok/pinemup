/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2009 by Mario Ködding
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
import java.awt.event.ActionListener;

import javax.swing.*;

import net.sourceforge.pinemup.gui.*;
import net.sourceforge.pinemup.logic.*;

public class RightClickMenu extends JPopupMenu implements ActionListener {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   private static final String ACTIVE_SYMBOL = "->";

   private NoteWindow parentWindow;
   
   private Category myCat;
   
   private JMenuItem deleteNoteItem, alwaysOnTopOnItem, alwaysOnTopOffItem;
   
   private JMenuItem[] setFontSizeItem, setCategoryItem, setBGColorItem;
   
   public RightClickMenu(NoteWindow w) {
      super();
      parentWindow = w;
      myCat = parentWindow.getParentNote().getCategory();

      //create MenuCreator
      MenuCreator myMenuCreator = new MenuCreator();      
      
      //add basic items
      JMenuItem[] basicItems = myMenuCreator.getBasicJMenuItems();
      for (int i=0; i<basicItems.length;i++) {
         add(basicItems[i]);
      }
      addSeparator();
      
      //add additional items
      deleteNoteItem = new JMenuItem(I18N.getInstance().getString("menu.deletenoteitem"));
      deleteNoteItem.addActionListener(this);
      add(deleteNoteItem);
      addSeparator();
      
      // settings menu
      JMenu settingsMenu = new JMenu(I18N.getInstance().getString("menu.notesettings"));
      
      JMenu setFontSizeMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.fontsize"));
      setFontSizeItem = new JMenuItem[26];
      for (int i=0; i<26; i++) {
         setFontSizeItem[i] = new JMenuItem(String.valueOf(i+5));
         if (i+5 == parentWindow.getParentNote().getFontSize()) {
            setFontSizeItem[i].setText(ACTIVE_SYMBOL + " " + setFontSizeItem[i].getText());
         } else {
            setFontSizeItem[i].setText("  "+setFontSizeItem[i].getText());
         }
         setFontSizeItem[i].addActionListener(this);
         setFontSizeMenu.add(setFontSizeItem[i]);
      }
      
      JMenu setBGColorMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.color"));
      setBGColorItem = new JMenuItem[BackgroundLabel.getNumberOfColors()];
      for (byte i=0; i<setBGColorItem.length; i++) {
         String prefix = " ";
         if (i == parentWindow.getParentNote().getBGColor()) {
            prefix = ACTIVE_SYMBOL + " ";
         }
         setBGColorItem[i] = new JMenuItem(prefix + BackgroundLabel.getColorName(i));
         setBGColorItem[i].setText("  "+setBGColorItem[i].getText());
         setBGColorItem[i].addActionListener(this);
         setBGColorMenu.add(setBGColorItem[i]);
      }
      
      JMenu setCategoryMenu = new JMenu(I18N.getInstance().getString("category"));
      setCategoryItem = new JMenuItem[CategoryManager.getInstance().getNumberOfCategories()];
      for (int i=0; i<setCategoryItem.length; i++) {
         setCategoryItem[i] = new JMenuItem((i+1) + " " + CategoryManager.getInstance().getCategoryNames()[i]);
         setCategoryItem[i].addActionListener(this);
         setCategoryMenu.add(setCategoryItem[i]);
      }

      JMenu alwaysOnTopMenu = new JMenu(I18N.getInstance().getString("menu.notesettings.alwaysontop"));
      String[] aot = {"  ","  "};
      if(parentWindow.getParentNote().isAlwaysOnTop()) {
         aot[0] = ACTIVE_SYMBOL + " ";
      } else {
         aot[1] = ACTIVE_SYMBOL + " ";
      }
      alwaysOnTopOnItem = new JMenuItem(aot[0]+I18N.getInstance().getString("enabled"));
      alwaysOnTopOffItem = new JMenuItem(aot[1]+I18N.getInstance().getString("disabled"));
      alwaysOnTopOnItem.addActionListener(this);
      alwaysOnTopOffItem.addActionListener(this);
      alwaysOnTopMenu.add(alwaysOnTopOnItem);
      alwaysOnTopMenu.add(alwaysOnTopOffItem);
                 
      settingsMenu.add(alwaysOnTopMenu);
      settingsMenu.add(setCategoryMenu);
      settingsMenu.add(setFontSizeMenu);
      settingsMenu.add(setBGColorMenu);
      add(settingsMenu);
      addSeparator();
      
      // category menu
      add(myMenuCreator.getCategoryActionsJMenu(I18N.getInstance().getString("menu.categorymenu"),myCat));
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == deleteNoteItem) {
         boolean confirmed = true;
         if (UserSettings.getInstance().getConfirmDeletion()) {
            confirmed = JOptionPane.showConfirmDialog(this, I18N.getInstance().getString("confirm.deletenote"), I18N.getInstance().getString("confirm.title"),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
         }
         if (confirmed && myCat != null) {
            parentWindow.getParentNote().hide();
            myCat.removeNote(parentWindow.getParentNote());
         }
      } else if (src == alwaysOnTopOnItem) {
         parentWindow.getParentNote().setAlwaysOnTop(true);
      } else if (src == alwaysOnTopOffItem) {
         parentWindow.getParentNote().setAlwaysOnTop(false);
      } else {
         for (int i=0; i<setCategoryItem.length; i++) {
            if (src == setCategoryItem[i]) {
               parentWindow.getParentNote().moveToCategory(CategoryManager.getInstance().getCategoryByNumber(i));
            }
         }
         
         for (int i=0; i<setFontSizeItem.length; i++) {
            if (src == setFontSizeItem[i]) {
               parentWindow.getParentNote().setFontSize((short)(i+5));
               parentWindow.updateFontSize();
            }
         }
         
         for (byte i=0; i<setBGColorItem.length; i++) {
            if (src == setBGColorItem[i]) {
               parentWindow.getParentNote().setBGColor(i);
               parentWindow.setBGColor(i);
            }
         }
      }
      
      // save notes to file after every change
      NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());
   }
}
