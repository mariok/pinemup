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

package net.sourceforge.pinemup.logic;

import javax.swing.*;

import net.sourceforge.pinemup.gui.*;

public class RightClickMenu extends JPopupMenu {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JMenuItem addNoteItem, showAllItem, hideAllItem, deleteNoteItem, hideCategoryItem, showOnlyCategoryItem, showCategoryItem, alwaysOnTopOnItem, alwaysOnTopOffItem;
   private JMenuItem[] setFontSizeItem, setCategoryItem;
   
   private NoteWindow parentWindow;
   
   private RightClickMenuLogic menuListener;

   public RightClickMenu(NoteWindow w, RightClickMenuLogic ml) {
      parentWindow = w;
      menuListener = ml;
      addNoteItem = new JMenuItem("add note");
      addNoteItem.setActionCommand("AddNote");
      addNoteItem.addActionListener(menuListener);
      add(addNoteItem);
      showAllItem = new JMenuItem("show all notes");
      showAllItem.setActionCommand("ShowAllNotes");
      showAllItem.addActionListener(menuListener);
      add(showAllItem);
      hideAllItem = new JMenuItem("hide all notes");
      hideAllItem.setActionCommand("HideAllNotes");
      hideAllItem.addActionListener(menuListener);
      add(hideAllItem);
      addSeparator();
      deleteNoteItem = new JMenuItem("delete this note");
      deleteNoteItem.setActionCommand("DeleteThisNote");
      deleteNoteItem.addActionListener(menuListener);
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
         setFontSizeItem[i].setActionCommand("SetFontSize"+(i+5));
         setFontSizeItem[i].addActionListener(menuListener);
         setFontSizeMenu.add(setFontSizeItem[i]);
      }
      JMenu setCategoryMenu = new JMenu("category");
      String[] active = new String[PinEmUp.getUserSettings().getNumberOfCategories()];
      for (int i=0; i<active.length; i++) {
         active[i] = "  ";
      }
      active[parentWindow.getParentNote().getCategory()] = "# ";
      
      setCategoryItem = new JMenuItem[PinEmUp.getUserSettings().getNumberOfCategories()];
      for (int i=0; i<setCategoryItem.length; i++) {
         setCategoryItem[i] = new JMenuItem(active[i] + i + " " + PinEmUp.getUserSettings().getCategoryNames()[i]);
         setCategoryItem[i].addActionListener(menuListener);
         setCategoryItem[i].setActionCommand("SetCategory"+(i+1));
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
      alwaysOnTopOnItem.addActionListener(menuListener);
      alwaysOnTopOffItem.addActionListener(menuListener);
      alwaysOnTopMenu.add(alwaysOnTopOnItem);
      alwaysOnTopMenu.add(alwaysOnTopOffItem);
      
      
            
      settingsMenu.add(alwaysOnTopMenu);
      settingsMenu.add(setCategoryMenu);
      settingsMenu.add(setFontSizeMenu);
      add(settingsMenu);
      addSeparator();
      
      // category menu
      JMenu categoryMenu = new JMenu("category actions");
      add(categoryMenu);
      
      // other category actions
      hideCategoryItem = new JMenuItem("hide notes of this category");
      showCategoryItem = new JMenuItem("show all notes of this category");
      showOnlyCategoryItem = new JMenuItem("show only notes of this category");
      hideCategoryItem.addActionListener(menuListener);
      showOnlyCategoryItem.addActionListener(menuListener);
      showCategoryItem.addActionListener(menuListener);
      
      categoryMenu.add(hideCategoryItem);
      categoryMenu.add(showCategoryItem);
      categoryMenu.add(showOnlyCategoryItem);
   }

}
