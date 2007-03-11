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

import gui.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RightClickMenu extends JPopupMenu implements ActionListener {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JMenuItem addNoteItem, showAllItem, hideAllItem, deleteNoteItem, setCategory1Item, setCategory2Item, setCategory3Item, setCategory4Item, setCategory5Item, hideCategoryItem, showOnlyCategoryItem, showCategoryItem, alwaysOnTopOnItem, alwaysOnTopOffItem;
   private JMenuItem[] setFontSizeItem;
   
   private NoteWindow parentWindow;

   public RightClickMenu(NoteWindow w) {
      parentWindow = w;
      deleteNoteItem = new JMenuItem("delete this note");
      deleteNoteItem.addActionListener(this);
      add(deleteNoteItem);
      addSeparator();
      addNoteItem = new JMenuItem("add note");
      addNoteItem.setActionCommand("AddNote");
      addNoteItem.addActionListener(MainApp.getMenuListener());
      add(addNoteItem);
      showAllItem = new JMenuItem("show all notes");
      showAllItem.setActionCommand("ShowAllNotes");
      showAllItem.addActionListener(MainApp.getMenuListener());
      add(showAllItem);
      hideAllItem = new JMenuItem("hide all notes");
      hideAllItem.setActionCommand("HideAllNotes");
      hideAllItem.addActionListener(MainApp.getMenuListener());
      add(hideAllItem);
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
         setFontSizeItem[i].addActionListener(this);
         setFontSizeMenu.add(setFontSizeItem[i]);
      }
      JMenu setCategoryMenu = new JMenu("category");
      String[] active = {"  ","  ","  ","  ","  "};
      active[parentWindow.getParentNote().getCategory()] = "# ";
      setCategory1Item = new JMenuItem(active[0] + "1 " + MainApp.getUserSettings().getCategoryNames()[0]);
      setCategory2Item = new JMenuItem(active[1] + "2 " + MainApp.getUserSettings().getCategoryNames()[1]);
      setCategory3Item = new JMenuItem(active[2] + "3 " + MainApp.getUserSettings().getCategoryNames()[2]);
      setCategory4Item = new JMenuItem(active[3] + "4 " + MainApp.getUserSettings().getCategoryNames()[3]);
      setCategory5Item = new JMenuItem(active[4] + "5 " + MainApp.getUserSettings().getCategoryNames()[4]);
      setCategory1Item.addActionListener(this);
      setCategory2Item.addActionListener(this);
      setCategory3Item.addActionListener(this);
      setCategory4Item.addActionListener(this);
      setCategory5Item.addActionListener(this);
      setCategoryMenu.add(setCategory1Item);
      setCategoryMenu.add(setCategory2Item);
      setCategoryMenu.add(setCategory3Item);
      setCategoryMenu.add(setCategory4Item);
      setCategoryMenu.add(setCategory5Item);
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
      JMenu categoryMenu = new JMenu("category actions");
      add(categoryMenu);
      
      // other category actions
      hideCategoryItem = new JMenuItem("hide notes of this category");
      showCategoryItem = new JMenuItem("show all notes of this category");
      showOnlyCategoryItem = new JMenuItem("show only notes of this category");
      hideCategoryItem.addActionListener(this);
      showOnlyCategoryItem.addActionListener(this);
      showCategoryItem.addActionListener(this);
      
      categoryMenu.add(hideCategoryItem);
      categoryMenu.add(showCategoryItem);
      categoryMenu.add(showOnlyCategoryItem);
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == deleteNoteItem) {
         MainApp.getMainApp().setNotes(Note.remove(MainApp.getMainApp().getNotes(), parentWindow.getParentNote()));
      } else if (src == setCategory1Item) {
         parentWindow.getParentNote().setCategory((byte)0);
      } else if (src == setCategory2Item) {
         parentWindow.getParentNote().setCategory((byte)1);
      } else if (src == setCategory3Item) {
         parentWindow.getParentNote().setCategory((byte)2);
      } else if (src == setCategory4Item) {
         parentWindow.getParentNote().setCategory((byte)3);
      } else if (src == setCategory5Item) {
         parentWindow.getParentNote().setCategory((byte)4);
      } else if (src == hideCategoryItem) {
         parentWindow.getParentNote().hideCategory(parentWindow.getParentNote().getCategory());
      } else if (src == showOnlyCategoryItem) {
         parentWindow.getParentNote().showOnlyCategory(parentWindow.getParentNote().getCategory());
      } else if (src == showCategoryItem) {
         parentWindow.getParentNote().showCategory(parentWindow.getParentNote().getCategory());
      } else if (src == alwaysOnTopOnItem) {
         parentWindow.getParentNote().setAlwaysOnTop(true);
      } else if (src == alwaysOnTopOffItem) {
         parentWindow.getParentNote().setAlwaysOnTop(false);
      } else if (e.getActionCommand().substring(0, 11).equals("SetFontSize")) {
         short s = Short.parseShort(e.getActionCommand().substring(11));
         parentWindow.getParentNote().setFontSize(s);
         parentWindow.refreshView();
      }
      
      // save notes to file after every change
      NoteIO.writeNotesToFile(MainApp.getMainApp().getNotes(), NoteIO.getFileName());
   }

}
