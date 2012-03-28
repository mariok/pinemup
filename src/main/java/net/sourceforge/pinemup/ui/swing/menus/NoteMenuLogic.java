package net.sourceforge.pinemup.ui.swing.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.core.NoteColor;
import net.sourceforge.pinemup.core.UserSettings;

public class NoteMenuLogic implements ActionListener {
   public static final String ACTION_DELETE_NOTE = "DELETE_NOTE";
   public static final String ACTION_TOGGLE_ALWAYS_ON_TOP = "TOGGLE_ALWAYS_ON_TOP";
   public static final String ACTION_MOVE_NOTE_TO_CATEGORY = "MOVE_NOTE_TO_CATEGORY";
   public static final String ACTION_SET_NOTE_COLOR = "SET_NOTE_COLOR";
   public static final String ACTION_SET_NOTE_FONT_SIZE = "SET_NOTE_FONT_SIZE";

   private Note note;

   NoteMenuLogic(Note note) {
      this.note = note;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      String action = e.getActionCommand();
      if (ACTION_DELETE_NOTE.equals(action)) {
         boolean confirmed = true;
         if (UserSettings.getInstance().getConfirmDeletion()) {
            confirmed = JOptionPane.showConfirmDialog(null, I18N.getInstance().getString("confirm.deletenote"), I18N.getInstance()
                  .getString("confirm.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
         }
         Category currentCat = note.getCategory();
         if (confirmed && currentCat != null) {
            note.setHidden(true);
            currentCat.removeNote(note);
         }
      } else if (ACTION_TOGGLE_ALWAYS_ON_TOP.equals(action)) {
         note.setAlwaysOnTop(!note.isAlwaysOnTop());
      } else if (action != null && action.startsWith(ACTION_MOVE_NOTE_TO_CATEGORY)) {
         int catNumber = Integer.parseInt(action.substring(ACTION_MOVE_NOTE_TO_CATEGORY.length() + 1));
         CategoryManager.getInstance().moveNoteToCategory(note, catNumber);
      } else if (action != null && action.startsWith(ACTION_SET_NOTE_FONT_SIZE)) {
         short fontSize = Short.parseShort(action.substring(ACTION_SET_NOTE_FONT_SIZE.length() + 1));
         note.setFontSize(fontSize);
      } else if (action != null && action.startsWith(ACTION_SET_NOTE_COLOR)) {
         byte colorCode = Byte.parseByte(action.substring(ACTION_SET_NOTE_COLOR.length() + 1));
         note.setColor(NoteColor.getNoteColorByCode(colorCode));
      }
   }
}
