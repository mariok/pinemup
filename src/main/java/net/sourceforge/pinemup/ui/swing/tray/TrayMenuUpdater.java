package net.sourceforge.pinemup.ui.swing.tray;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrayMenuUpdater implements Observer {
   private static final Logger LOG = LoggerFactory.getLogger(TrayMenuUpdater.class);

   private TrayMenu trayMenu;

   public TrayMenuUpdater(TrayMenu trayMenu) {
      this.trayMenu = trayMenu;
   }

   @Override
   public void update(Observable o, Object arg) {
      LOG.debug("Received update event.");
      trayMenu.createCategoriesMenu();
   }
}
