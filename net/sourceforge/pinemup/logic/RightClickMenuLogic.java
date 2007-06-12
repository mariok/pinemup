package net.sourceforge.pinemup.logic;

import net.sourceforge.pinemup.gui.NoteWindow;

public class RightClickMenuLogic extends MenuLogic {
   
   private NoteWindow parentWindow;
   
   public RightClickMenuLogic(NoteWindow w) {
      parentWindow = w;
   }
   
   public void checkCommand(String cmd) {
      super.checkCommand(cmd);
      
      if (cmd.equals("DeleteThisNote")) {
         PinEmUp.getMainApp().setNotes(Note.remove(PinEmUp.getMainApp().getNotes(), parentWindow.getParentNote()));
      } else if (cmd.substring(0, 11).equals("SetCategory")) {
         byte c = Byte.parseByte(cmd.substring(11));
         parentWindow.getParentNote().setCategory(c);
      } else if (cmd.equals("HideThisCategory")) {
         parentWindow.getParentNote().hideCategory(parentWindow.getParentNote().getCategory());
      } else if (cmd.equals("ShowOnlyThisCategory")) {
         parentWindow.getParentNote().showOnlyCategory(parentWindow.getParentNote().getCategory());
      } else if (cmd.equals("ShowThisCategory")) {
         parentWindow.getParentNote().showCategory(parentWindow.getParentNote().getCategory());
      } else if (cmd.equals("AlwaysOnTopOn")) {
         parentWindow.getParentNote().setAlwaysOnTop(true);
      } else if (cmd.equals("AlwaysOnTopOff")) {
         parentWindow.getParentNote().setAlwaysOnTop(false);
      } else if (cmd.substring(0, 11).equals("SetFontSize")) {
         short s = Short.parseShort(cmd.substring(11));
         parentWindow.getParentNote().setFontSize(s);
         parentWindow.refreshView();
      }
      
      // save notes to file after every change
      NoteIO.writeNotesToFile(PinEmUp.getMainApp().getNotes(), PinEmUp.getUserSettings().getNotesFile());
   }
}
