package net.sourceforge.pinemup.ui.swing.menus.logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;

public class CategoryMenuLogic implements ActionListener {
   public enum CategoryAction {
      HIDE_ALL_NOTES("menu.categorymenu.hidenotes"),
      SHOW_ALL_NOTES("menu.categorymenu.shownotes"),
      SHOW_ONLY_NOTES_OF_CATEGORY("menu.categorymenu.showonlynotes"),
      SET_AS_DEFAULT_CATEGORY("menu.categorymenu.setasdefault");

      private String i18nKey;

      private CategoryAction(String i18nKey) {
         this.i18nKey = i18nKey;
      }

      public String getI18nKey() {
         return i18nKey;
      }
   };

   private Category category;

   public CategoryMenuLogic(Category category) {
      this.category = category;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      CategoryAction action = CategoryAction.valueOf(e.getActionCommand());

      switch (action) {
      case HIDE_ALL_NOTES:
         category.hideAllNotes();
         break;
      case SHOW_ALL_NOTES:
         category.unhideAllNotes();
         break;
      case SHOW_ONLY_NOTES_OF_CATEGORY:
         CategoryManager.getInstance().showOnlyNotesOfCategory(category);
         break;
      case SET_AS_DEFAULT_CATEGORY:
         CategoryManager.getInstance().setDefaultCategory(category);
         break;
      default:
         break;
      }
   }
}
