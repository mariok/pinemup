package net.sourceforge.pinemup.core.model;

import java.util.EventListener;

public interface CategoryChangedEventListener extends EventListener {
   void categoryChanged(CategoryChangedEvent event);
}
