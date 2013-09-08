package net.sourceforge.pinemup.ui.swing.tray;

import java.util.Observable;
import java.util.Observer;

public class TrayMenuUpdater implements Observer {
   private TrayMenu trayMenu;

   public TrayMenuUpdater(TrayMenu trayMenu) {
      this.trayMenu = trayMenu;
   }

   @Override
   public void update(Observable o, Object arg) {
      trayMenu.createCategoriesMenu();
   }
}
