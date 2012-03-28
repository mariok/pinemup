package net.sourceforge.pinemup.ui.swing.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;

public class CategoryMenuLogic implements ActionListener {
   public static final String ACTION_HIDE_ALL_NOTES = "HIDE_ALL_NOTES";
   public static final String ACTION_SHOW_ALL_NOTES = "SHOW_ALL_NOTES";
   public static final String ACTION_SHOW_ONLY_NOTES_OF_CATEGORY = "SHOW_ONLY_NOTES_OF_CATEGORY";
   public static final String ACTION_SET_AS_DEFAULT_CATEGORY = "SET_AS_DEFAULT_CATEGORY";

   private Category category;

   public CategoryMenuLogic(Category category) {
      this.category = category;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      String action = e.getActionCommand();
      if (ACTION_HIDE_ALL_NOTES.equals(action)) {
         category.hideAllNotes();
      } else if (ACTION_SHOW_ALL_NOTES.equals(action)) {
         category.unhideAllNotes();
      } else if (ACTION_SHOW_ONLY_NOTES_OF_CATEGORY.equals(action)) {
         CategoryManager.getInstance().showOnlyNotesOfCategory(category);
      } else if (ACTION_SET_AS_DEFAULT_CATEGORY.equals(action)) {
         CategoryManager.getInstance().setDefaultCategory(category);
      }
   }
}
