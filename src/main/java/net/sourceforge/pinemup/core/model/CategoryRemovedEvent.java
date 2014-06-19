package net.sourceforge.pinemup.core.model;

import java.util.EventObject;

public class CategoryRemovedEvent extends EventObject {
   private static final long serialVersionUID = 5340998352321719112L;

   private final Category removedCategory;

   public CategoryRemovedEvent(PinBoard source, Category removedCategory) {
      super(source);
      this.removedCategory = removedCategory;
   }

   @Override
   public PinBoard getSource() {
      return (PinBoard)source;
   }

   public Category getAddedCategory() {
      return removedCategory;
   }
}
