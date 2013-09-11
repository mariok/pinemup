package net.sourceforge.pinemup.core;

import java.util.EventListener;

public interface NoteChangedEventListener extends EventListener {
   void noteChanged(NoteChangedEvent event);
}
