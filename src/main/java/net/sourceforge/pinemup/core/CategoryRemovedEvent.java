package net.sourceforge.pinemup.core;

import java.util.EventObject;

public class CategoryRemovedEvent extends EventObject {
   private static final long serialVersionUID = 5340998352321719112L;

   private Category removedCategory;

   public CategoryRemovedEvent(CategoryManager source, Category removedCategory) {
      super(source);
      this.removedCategory = removedCategory;
   }

   @Override
   public CategoryManager getSource() {
      return (CategoryManager)source;
   }

   public Category getAddedCategory() {
      return removedCategory;
   }
}
