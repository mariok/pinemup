package net.sourceforge.pinemup.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import net.sourceforge.pinemup.logic.*;


public abstract class BasicMenu extends JPopupMenu implements ActionListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   protected CategoryList categories;
   protected UserSettings settings; 
   
   private JMenuItem addNoteItem, showAllItem, hideAllItem;
   
   public BasicMenu(CategoryList cl, UserSettings s) {
      categories = cl;
      settings = s;
      addNoteItem = new JMenuItem("add note");
      addNoteItem.addActionListener(this);
      add(addNoteItem);
      showAllItem = new JMenuItem("show all notes");
      showAllItem.addActionListener(this);
      add(showAllItem);
      hideAllItem = new JMenuItem("hide all notes");
      hideAllItem.addActionListener(this);
      add(hideAllItem);
      addSeparator();
   }

   public void actionPerformed(ActionEvent e) {
      checkActionEvent(e);
   }
   
   public boolean checkActionEvent(ActionEvent e) {
      boolean actionFound = false;
      Object src = e.getSource();
      if (src == addNoteItem) {
         actionFound = true;
         Category defCat = categories.getDefaultCategory();
         if (defCat != null) {
            Note newNote = new Note("",settings,categories);
            defCat.getNotes().add(newNote);
            newNote.showIfNotHidden();
            newNote.jumpInto();
         }
      } else if (src == showAllItem) {
         actionFound = true;
         categories.unhideAndShowAllNotes();
      } else if (src == hideAllItem) {
         actionFound = true;
         categories.hideAllNotes();
      }
      return actionFound;
   }
}
