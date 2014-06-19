package net.sourceforge.pinemup.core.settings;

import net.sourceforge.pinemup.core.model.ObjectChangedEvent;

public class UserSettingsChangedEvent extends ObjectChangedEvent<UserSettings> {
   private static final long serialVersionUID = -1625574299332502650L;

   public UserSettingsChangedEvent(UserSettings source) {
      super(source);
   }
}
