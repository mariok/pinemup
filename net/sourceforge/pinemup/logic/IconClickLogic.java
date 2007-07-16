package net.sourceforge.pinemup.logic;

import java.awt.event.*;
import net.sourceforge.pinemup.menus.*;

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

   public void mouseReleased(MouseEvent event) {
      if (event.isPopupTrigger() || event.getButton() == MouseEvent.BUTTON2 || event.getButton() == MouseEvent.BUTTON3) {
         TrayMenu menu = new TrayMenu(categories,settings);
         menu.setLocation(event.getX(), event.getY());
         menu.setInvoker(menu);
         menu.setVisible(true);
      }
   }
   

}
