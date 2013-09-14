package net.sourceforge.pinemup.core;

import java.util.EventListener;

public interface UserSettingsChangedEventListener extends EventListener {
   void settingsChanged(UserSettingsChangedEvent event);
}
