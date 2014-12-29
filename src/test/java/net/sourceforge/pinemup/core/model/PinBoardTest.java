package net.sourceforge.pinemup.core.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class PinBoardTest {
   private PinBoard pinBoard;

   @Before
   public void setup() {
      pinBoard = new PinBoard();
   }

   @Test
   public void testAddCategory() {
      Category cat1 = new Category("cat1", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat1);
      assertEquals(1, pinBoard.getCategories().size());
      assertEquals(cat1, pinBoard.getCategoryByNumber(0));

      Category cat2 = new Category("cat2", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat2);
      assertEquals(2, pinBoard.getCategories().size());
      assertEquals(cat2, pinBoard.getCategoryByNumber(1));
   }

   @Test
   public void testRemoveCategory() {
      Category cat1 = new Category("cat1", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat1);
      assertTrue(pinBoard.getCategories().contains(cat1));
      pinBoard.removeCategory(cat1);
      assertFalse(pinBoard.getCategories().contains(cat1));
      assertTrue(pinBoard.getCategories().isEmpty());
   }

   @Test
   public void testMoveCategoryUp() {
      Category cat1 = new Category("cat1", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat1);
      Category cat2 = new Category("cat2", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat2);
      Category cat3 = new Category("cat3", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat3);

      assertEquals(cat1, pinBoard.getCategoryByNumber(0));
      assertEquals(cat2, pinBoard.getCategoryByNumber(1));
      assertEquals(cat3, pinBoard.getCategoryByNumber(2));

      pinBoard.moveCategoryUp(cat3);
      pinBoard.moveCategoryUp(cat3);

      assertEquals(cat3, pinBoard.getCategoryByNumber(0));
      assertEquals(cat1, pinBoard.getCategoryByNumber(1));
      assertEquals(cat2, pinBoard.getCategoryByNumber(2));
   }

   @Test
   public void testMoveCategoryDown() {
      Category cat1 = new Category("cat1", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat1);
      Category cat2 = new Category("cat2", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat2);
      Category cat3 = new Category("cat3", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat3);

      assertEquals(cat1, pinBoard.getCategoryByNumber(0));
      assertEquals(cat2, pinBoard.getCategoryByNumber(1));
      assertEquals(cat3, pinBoard.getCategoryByNumber(2));

      pinBoard.moveCategoryDown(cat1);
      pinBoard.moveCategoryDown(cat1);

      assertEquals(cat2, pinBoard.getCategoryByNumber(0));
      assertEquals(cat3, pinBoard.getCategoryByNumber(1));
      assertEquals(cat1, pinBoard.getCategoryByNumber(2));
   }

   @Test
   public void testCategoryAddedEventListenersCalled() {
      CategoryAddedEventListener categoryAddedEventListenerMock1 = Mockito.mock(CategoryAddedEventListener.class);
      CategoryAddedEventListener categoryAddedEventListenerMock2 = Mockito.mock(CategoryAddedEventListener.class);

      pinBoard.addCategoryAddedEventListener(categoryAddedEventListenerMock1);
      pinBoard.addCategoryAddedEventListener(categoryAddedEventListenerMock2);

      pinBoard.addCategory(new Category("cat1", false, NoteColor.DEFAULT_COLOR));
      pinBoard.addCategory(new Category("cat2", false, NoteColor.DEFAULT_COLOR));
      pinBoard.addCategory(new Category("cat3", false, NoteColor.DEFAULT_COLOR));

      Mockito.verify(categoryAddedEventListenerMock1, Mockito.times(3)).categoryAdded(Mockito.any(CategoryAddedEvent.class));
      Mockito.verify(categoryAddedEventListenerMock2, Mockito.times(3)).categoryAdded(Mockito.any(CategoryAddedEvent.class));
   }

   @Test
   public void testCategoryRemovedEventListenersCalled() {
      CategoryRemovedEventListener categoryRemovedEventListenerMock1 = Mockito.mock(CategoryRemovedEventListener.class);
      CategoryRemovedEventListener categoryRemovedEventListenerMock2 = Mockito.mock(CategoryRemovedEventListener.class);

      pinBoard.addCategoryRemovedEventListener(categoryRemovedEventListenerMock1);
      pinBoard.addCategoryRemovedEventListener(categoryRemovedEventListenerMock2);

      Category cat1 = new Category("cat1", false, NoteColor.DEFAULT_COLOR);
      Category cat2 = new Category("cat2", false, NoteColor.DEFAULT_COLOR);
      Category cat3 = new Category("cat3", false, NoteColor.DEFAULT_COLOR);
      pinBoard.addCategory(cat1);
      pinBoard.addCategory(cat2);
      pinBoard.addCategory(cat3);

      pinBoard.removeCategory(cat1);
      pinBoard.removeCategory(cat2);
      pinBoard.removeCategory(cat3);

      Mockito.verify(categoryRemovedEventListenerMock1, Mockito.times(3)).categoryRemoved(Mockito.any(CategoryRemovedEvent.class));
      Mockito.verify(categoryRemovedEventListenerMock2, Mockito.times(3)).categoryRemoved(Mockito.any(CategoryRemovedEvent.class));
   }
}