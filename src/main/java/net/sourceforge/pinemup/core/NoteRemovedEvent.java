package net.sourceforge.pinemup.core;

import java.util.EventObject;

public class NoteRemovedEvent extends EventObject {
   private static final long serialVersionUID = 4092939602152086749L;

   private Note removedNote;

   public NoteRemovedEvent(Category source, Note removedNote) {
      super(source);
      this.removedNote = removedNote;
   }

   @Override
   public Category getSource() {
      return (Category)source;
   }

   public Note getRemovedNote() {
      return removedNote;
   }
}
