package net.sourceforge.pinemup.core;

import java.util.EventListener;

public interface NoteAddedEventListener extends EventListener {
   void noteAdded(NoteAddedEvent event);
}
