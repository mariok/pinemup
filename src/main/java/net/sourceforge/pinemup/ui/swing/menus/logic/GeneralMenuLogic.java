package net.sourceforge.pinemup.ui.swing.menus.logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sourceforge.pinemup.core.CategoryManager;

public class GeneralMenuLogic implements ActionListener {
   public enum GeneralAction {
      ADD_NOTE("menu.addnoteitem"),
      SHOW_ALL_NOTES("menu.showallnotesitem"),
      HIDE_ALL_NOTES("menu.hideallnotesitem");

      private final String i18nKey;

      private GeneralAction(String i18nKey) {
         this.i18nKey = i18nKey;
      }

      public String getI18nKey() {
         return i18nKey;
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      GeneralAction action = GeneralAction.valueOf(e.getActionCommand());

      switch (action) {
      case ADD_NOTE:
         CategoryManager.getInstance().createNoteAndAddToDefaultCategory();
         break;
      case SHOW_ALL_NOTES:
         CategoryManager.getInstance().unhideAllNotes();
         break;
      case HIDE_ALL_NOTES:
         CategoryManager.getInstance().hideAllNotes();
         break;
      default:
         break;
      }
   }
}
