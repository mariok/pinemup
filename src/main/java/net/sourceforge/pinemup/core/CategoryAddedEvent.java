package net.sourceforge.pinemup.core;

import java.util.EventObject;

public class CategoryAddedEvent extends EventObject {
   private static final long serialVersionUID = 3366814253028410097L;

   private Category addedCategory;

   public CategoryAddedEvent(CategoryManager source, Category addedCategory) {
      super(source);
      this.addedCategory = addedCategory;
   }

   @Override
   public CategoryManager getSource() {
      return (CategoryManager)source;
   }

   public Category getAddedCategory() {
      return addedCategory;
   }
}
