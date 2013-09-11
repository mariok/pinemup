package net.sourceforge.pinemup.core;

import java.util.EventListener;

public interface CategoryChangedEventListener extends EventListener {
   void categoryChanged(CategoryChangedEvent event);
}
