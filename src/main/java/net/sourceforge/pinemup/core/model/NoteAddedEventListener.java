package net.sourceforge.pinemup.core.model;

import java.util.EventListener;

public interface NoteAddedEventListener extends EventListener {
   void noteAdded(NoteAddedEvent event);
}
