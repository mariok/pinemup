package net.sourceforge.pinemup.core.model;

public class CategoryChangedEvent extends ObjectChangedEvent<Category> {
   private static final long serialVersionUID = -1625574299332502650L;

   public CategoryChangedEvent(Category source) {
      super(source);
   }
}
