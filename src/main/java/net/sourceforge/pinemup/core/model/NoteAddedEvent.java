package net.sourceforge.pinemup.core.model;

import java.util.EventObject;

public class NoteAddedEvent extends EventObject {
   private static final long serialVersionUID = 7367726368345518686L;

   private final Note addedNote;

   public NoteAddedEvent(Category source, Note addedNote) {
      super(source);
      this.addedNote = addedNote;
   }

   @Override
   public Category getSource() {
      return (Category)source;
   }

   public Note getAddedNote() {
      return addedNote;
   }
}
