package net.sourceforge.pinemup.logic;

import java.awt.event.*;
import net.sourceforge.pinemup.menus.*;
import javax.swing.*;

public class IconClickLogic extends MouseAdapter implements ActionListener {
   private CategoryList categories;
   private UserSettings settings;
   private TrayMenu menu;
   
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
      menu = new TrayMenu(categories,settings);
      menu.setInvoker(null);
   }

   public void mouseReleased(MouseEvent event) {
      if (event.isPopupTrigger() || event.getButton() == MouseEvent.BUTTON2 || event.getButton() == MouseEvent.BUTTON3) {
         menu.setLocation(event.getX(), event.getY());
         menu.setVisible(true);         
         SwingUtilities.windowForComponent(menu).setAlwaysOnTop(true);
      }
   }
   
   
   

}
