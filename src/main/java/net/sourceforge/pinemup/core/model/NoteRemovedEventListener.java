package net.sourceforge.pinemup.core.model;

import java.util.EventListener;

public interface NoteRemovedEventListener extends EventListener {
   void noteRemoved(NoteRemovedEvent event);
}
