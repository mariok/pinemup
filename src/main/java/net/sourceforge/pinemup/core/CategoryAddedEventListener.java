package net.sourceforge.pinemup.core;

import java.util.EventListener;

public interface CategoryAddedEventListener extends EventListener {
   void categoryAdded(CategoryAddedEvent event);
}
