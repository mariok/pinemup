package net.sourceforge.pinemup.core.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains a number of notes of different categories.
 */
public class PinBoard {
   private final List<Category> categories = new LinkedList<>();

   private final Collection<CategoryAddedEventListener> categoryAddedEventListeners = new LinkedList<>();

   private final Collection<CategoryRemovedEventListener> categoryRemovedEventListeners = new LinkedList<>();

   public void addCategory(Category c) {
      categories.add(c);
      fireCategoryAddedEvent(c);

      for (Note note : c.getNotes()) {
         c.fireNoteAddedEvent(note);
      }
   }

   public void removeCategory(Category c) {
      categories.remove(c);
      fireCategoryRemovedEvent(c);
   }

   public List<Category> getCategories() {
      return categories;
   }

   public void moveCategoryUp(Category c) {
      int index = categories.indexOf(c);
      if (index > 0) {
         categories.set(index, categories.get(index - 1));
         categories.set(index - 1, c);
         fireCategoryRemovedEvent(c);
         fireCategoryAddedEvent(c);
      }
   }

   public void moveCategoryDown(Category c) {
      int index = categories.indexOf(c);
      if (index < categories.size() - 1) {
         categories.set(index, categories.get(index + 1));
         categories.set(index + 1, c);
         fireCategoryRemovedEvent(c);
         fireCategoryAddedEvent(c);
      }
   }

   public Category getCategoryByNumber(int n) {
      if (n < 0 || n >= categories.size()) {
         return null;
      }
      return categories.get(n);
   }

   public void addCategoryAddedEventListener(CategoryAddedEventListener listener) {
      categoryAddedEventListeners.add(listener);
   }

   public void addCategoryRemovedEventListener(CategoryRemovedEventListener listener) {
      categoryRemovedEventListeners.add(listener);
   }

   private void fireCategoryAddedEvent(Category category) {
      for (CategoryAddedEventListener listener : categoryAddedEventListeners) {
         listener.categoryAdded(new CategoryAddedEvent(this, category));
      }
   }

   private void fireCategoryRemovedEvent(Category category) {
      for (CategoryRemovedEventListener listener : categoryRemovedEventListeners) {
         listener.categoryRemoved(new CategoryRemovedEvent(this, category));
      }
   }
}
