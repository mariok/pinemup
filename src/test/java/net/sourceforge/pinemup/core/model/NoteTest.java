package net.sourceforge.pinemup.core.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class NoteTest {
   private Note note;

   @Before
   public void setup() {
      note = new Note();
   }

   @Test
   public void testNoteChangedEventListenersCalledForAllAttributes() {
      NoteChangedEventListener noteChangedEventListenerMock1 = Mockito.mock(NoteChangedEventListener.class);
      note.addNoteChangedEventListener(noteChangedEventListenerMock1);

      note.setText("Test");
      note.setAlwaysOnTop(true);
      note.setColor(NoteColor.BLUE);
      note.setFontSize(Note.MIN_FONT_SIZE);
      note.setHidden(true);
      note.setPosition((short)20, (short)20);
      note.setSize((short) 240, (short) 240);
      Mockito.verify(noteChangedEventListenerMock1, Mockito.times(7)).noteChanged(Mockito.any(NoteChangedEvent.class));

      note.setText("Test");
      note.setAlwaysOnTop(true);
      note.setColor(NoteColor.BLUE);
      note.setFontSize(Note.MIN_FONT_SIZE);
      note.setHidden(true);
      note.setPosition((short)20, (short)20);
      note.setSize((short) 240, (short) 240);
      // there should be no additional calls, because no new values were set
      Mockito.verify(noteChangedEventListenerMock1, Mockito.times(7)).noteChanged(Mockito.any(NoteChangedEvent.class));
   }


   @Test
   public void testSetInvalidFontSize() {
      int initialFontSize = note.getFontSize();
      note.setFontSize((short)(Note.MIN_FONT_SIZE - 1));
      assertEquals(initialFontSize, note.getFontSize());
      note.setFontSize((short)(Note.MAX_FONT_SIZE + 1));
      assertEquals(initialFontSize, note.getFontSize());
   }
}