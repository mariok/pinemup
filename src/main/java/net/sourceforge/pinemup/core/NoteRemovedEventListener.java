package net.sourceforge.pinemup.core;

import java.util.EventListener;

public interface NoteRemovedEventListener extends EventListener {
   void noteRemoved(NoteRemovedEvent event);
}
