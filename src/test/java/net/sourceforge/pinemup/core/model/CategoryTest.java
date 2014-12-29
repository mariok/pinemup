package net.sourceforge.pinemup.core.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CategoryTest {
   private Category category;

   @Before
   public void setup() {
      this.category = new Category("test-category", true, NoteColor.DEFAULT_COLOR);
   }

   @Test
   public void testCategoryChangedEventListenersCalledForAllAttributes() {
      CategoryChangedEventListener categoryChangedEventListenerMock1 = Mockito.mock(CategoryChangedEventListener.class);
      category.addCategoryChangedEventListener(categoryChangedEventListenerMock1);

      category.setDefault(false);
      category.setDefaultNoteColor(NoteColor.BLUE);
      category.setName("new-name");
      Mockito.verify(categoryChangedEventListenerMock1, Mockito.times(3)).categoryChanged(Mockito.any(CategoryChangedEvent.class));

      category.setDefault(false);
      category.setDefaultNoteColor(NoteColor.BLUE);
      category.setName("new-name");
      // there should be no additional calls, because no new values were set
      Mockito.verify(categoryChangedEventListenerMock1, Mockito.times(3)).categoryChanged(Mockito.any(CategoryChangedEvent.class));
   }

   @Test
   public void testNoteAddedEventListenersCalled() {
      NoteAddedEventListener noteAddedEventListenerMock1 = Mockito.mock(NoteAddedEventListener.class);
      NoteAddedEventListener noteAddedEventListenerMock2 = Mockito.mock(NoteAddedEventListener.class);

      category.addNoteAddedEventListener(noteAddedEventListenerMock1);
      category.addNoteAddedEventListener(noteAddedEventListenerMock2);

      category.addNote(new Note());
      category.addNote(new Note());
      category.addNote(new Note());

      Mockito.verify(noteAddedEventListenerMock1, Mockito.times(3)).noteAdded(Mockito.any(NoteAddedEvent.class));
      Mockito.verify(noteAddedEventListenerMock2, Mockito.times(3)).noteAdded(Mockito.any(NoteAddedEvent.class));
   }

   @Test
   public void testNoteRemovedEventListenersCalled() {
      NoteRemovedEventListener noteRemovedEventListenerMock1 = Mockito.mock(NoteRemovedEventListener.class);
      NoteRemovedEventListener noteRemovedEventListenerMock2 = Mockito.mock(NoteRemovedEventListener.class);

      category.addNoteRemovedEventListener(noteRemovedEventListenerMock1);
      category.addNoteRemovedEventListener(noteRemovedEventListenerMock2);

      Note note1 = new Note();
      Note note2 = new Note();
      Note note3 = new Note();
      category.addNote(note1);
      category.addNote(note2);
      category.addNote(note3);

      category.removeNote(note1);
      category.removeNote(note2);
      category.removeNote(note3);

      Mockito.verify(noteRemovedEventListenerMock1, Mockito.times(3)).noteRemoved(Mockito.any(NoteRemovedEvent.class));
      Mockito.verify(noteRemovedEventListenerMock2, Mockito.times(3)).noteRemoved(Mockito.any(NoteRemovedEvent.class));
   }

   @Test
   public void testHideAndUnhideAllNotes() {
      Note note1 = new Note();
      Note note2 = new Note();
      Note note3 = new Note();
      Note note4 = new Note();

      category.addNote(note1);
      category.addNote(note2);
      category.addNote(note3);
      category.addNote(note4);

      category.hideAllNotes();

      assertTrue(note1.isHidden());
      assertTrue(note2.isHidden());
      assertTrue(note3.isHidden());
      assertTrue(note4.isHidden());

      category.unhideAllNotes();

      assertFalse(note1.isHidden());
      assertFalse(note2.isHidden());
      assertFalse(note3.isHidden());
      assertFalse(note4.isHidden());
   }

   @Test
   public void testAddNote() {
      Note note1 = new Note();
      category.addNote(note1);
      assertEquals(1, category.getNumberOfNotes());
      assertTrue(category.getNotes().contains(note1));

      Note note2 = new Note();
      category.addNote(note2);
      assertEquals(2, category.getNumberOfNotes());
      assertTrue(category.getNotes().contains(note2));
   }

   @Test
   public void testRemoveNote() {
      Note note1 = new Note();
      category.addNote(note1);
      assertTrue(category.getNotes().contains(note1));
      category.removeNote(note1);
      assertFalse(category.getNotes().contains(note1));
      assertTrue(category.getNotes().isEmpty());
   }
}