package net.sourceforge.pinemup.core.model;

import java.util.EventListener;

public interface NoteChangedEventListener extends EventListener {
   void noteChanged(NoteChangedEvent event);
}
