package net.sourceforge.pinemup.core.model;

public class NoteChangedEvent extends ObjectChangedEvent<Note> {
   private static final long serialVersionUID = 3522570514200163056L;

   public NoteChangedEvent(Note source) {
      super(source);
   }
}
