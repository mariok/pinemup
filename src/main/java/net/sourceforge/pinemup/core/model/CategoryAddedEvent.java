package net.sourceforge.pinemup.core.model;

import java.util.EventObject;

public class CategoryAddedEvent extends EventObject {
   private static final long serialVersionUID = 3366814253028410097L;

   private final Category addedCategory;

   public CategoryAddedEvent(PinBoard source, Category addedCategory) {
      super(source);
      this.addedCategory = addedCategory;
   }

   @Override
   public PinBoard getSource() {
      return (PinBoard)source;
   }

   public Category getAddedCategory() {
      return addedCategory;
   }
}
