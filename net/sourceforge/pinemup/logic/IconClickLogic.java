package net.sourceforge.pinemup.logic;

import java.awt.event.*;

public class IconClickLogic extends MouseAdapter implements ActionListener {
   private CategoryList categories;
   private UserSettings settings;
   
   public void actionPerformed(ActionEvent arg0) {
      Category defCat = categories.getDefaultCategory();
      if (defCat != null) {
         Note newNote = new Note("",settings,categories);
         defCat.getNotes().add(newNote);
         newNote.showIfNotHidden();
         newNote.jumpInto();
      }
   }
   
   public IconClickLogic(CategoryList c, UserSettings s) {
      categories = c;
      settings = s;
   }
}
