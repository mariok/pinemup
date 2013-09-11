package net.sourceforge.pinemup.core;

import java.util.EventListener;

public interface CategoryRemovedEventListener extends EventListener {
   void categoryRemoved(CategoryRemovedEvent event);
}
