package net.sourceforge.pinemup.ui.swing.tray;

import net.sourceforge.pinemup.core.model.CategoryAddedEvent;
import net.sourceforge.pinemup.core.model.CategoryAddedEventListener;
import net.sourceforge.pinemup.core.model.CategoryChangedEvent;
import net.sourceforge.pinemup.core.model.CategoryChangedEventListener;
import net.sourceforge.pinemup.core.model.CategoryRemovedEvent;
import net.sourceforge.pinemup.core.model.CategoryRemovedEventListener;
import net.sourceforge.pinemup.core.settings.UserSettingsChangedEvent;
import net.sourceforge.pinemup.core.settings.UserSettingsChangedEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrayMenuUpdater implements CategoryChangedEventListener, UserSettingsChangedEventListener, CategoryAddedEventListener,
      CategoryRemovedEventListener {
   private static final Logger LOG = LoggerFactory.getLogger(TrayMenuUpdater.class);

   private final TrayMenu trayMenu;

   public TrayMenuUpdater(TrayMenu trayMenu) {
      this.trayMenu = trayMenu;
   }

   @Override
   public void settingsChanged(UserSettingsChangedEvent event) {
      trayMenu.initWithNewLanguage();
   }

   @Override
   public void categoryChanged(CategoryChangedEvent event) {
      LOG.debug("Received CategoryChangedEvent.");
      trayMenu.createCategoriesMenu();
   }

   @Override
   public void categoryRemoved(CategoryRemovedEvent event) {
      LOG.debug("Received CategoryRemovedEvent.");
      trayMenu.createCategoriesMenu();
   }

   @Override
   public void categoryAdded(CategoryAddedEvent event) {
      LOG.debug("Received CategoryAddedEvent.");
      trayMenu.createCategoriesMenu();
   }
}
