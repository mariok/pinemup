package net.sourceforge.pinemup.core;

public class UserSettingsChangedEvent extends ObjectChangedEvent<UserSettings> {
   private static final long serialVersionUID = -1625574299332502650L;

   public UserSettingsChangedEvent(UserSettings source) {
      super(source);
   }
}
