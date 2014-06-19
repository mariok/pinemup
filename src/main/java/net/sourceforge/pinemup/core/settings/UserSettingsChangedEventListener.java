package net.sourceforge.pinemup.core.settings;

import java.util.EventListener;

public interface UserSettingsChangedEventListener extends EventListener {
   void settingsChanged(UserSettingsChangedEvent event);
}
