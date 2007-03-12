/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
 *
 *
 * pin 'em up is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * pin 'em up is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with pin 'em up; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import gui.*;

public class MenuLogic implements ActionListener {

   public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();

      if (cmd.equals("AddNote")) {
         PinEmUp.getMainApp().setNotes(Note.add(PinEmUp.getMainApp().getNotes(), ""));
         PinEmUp.getMainApp().getNotes().showAllVisible();
      } else if (cmd.equals("ShowAllNotes")) {
         if (PinEmUp.getMainApp().getNotes() != null) {
            PinEmUp.getMainApp().getNotes().showAll();
         }
      } else if (cmd.equals("HideAllNotes")) {
         if (PinEmUp.getMainApp().getNotes() != null) {
            PinEmUp.getMainApp().getNotes().hideAll();
         }
      } else if (cmd.equals("ShowSettings")) {
         new SettingsDialog();
      } else if (cmd.equals("Exit")) {
         PinEmUp.getMainApp().exit();
      } else if (cmd.equals("UploadNotesToFTP")) {
         new FTPThread(true);
      } else if (cmd.equals("DownloadNotesFromFTP")) {
         new FTPThread(false);
      } else if (cmd.equals("ShowAboutDialog")) {
         String msg = "pin 'em up\nversion " + PinEmUp.getVersion() + "\n\n(C) 2007 Mario Koedding\nmario.koedding@web.de\n\nThis program is licensed under the terms of the GNU GPL V2.\nRead \"COPYING\" file for details.\n\nvisit http://pin-em-up.sourceforge.net";
         JOptionPane.showMessageDialog(null, msg, "about pin 'em up", JOptionPane.INFORMATION_MESSAGE);
      } else if (cmd.equals("ShowOnlyCategory0")) {
         PinEmUp.getMainApp().getNotes().showOnlyCategory((byte)0);
      } else if (cmd.equals("ShowOnlyCategory1")) {
         PinEmUp.getMainApp().getNotes().showOnlyCategory((byte)1);
      } else if (cmd.equals("ShowOnlyCategory2")) {
         PinEmUp.getMainApp().getNotes().showOnlyCategory((byte)2);
      } else if (cmd.equals("ShowOnlyCategory3")) {
         PinEmUp.getMainApp().getNotes().showOnlyCategory((byte)3);
      } else if (cmd.equals("ShowOnlyCategory4")) {
         PinEmUp.getMainApp().getNotes().showOnlyCategory((byte)4);
      } else if (cmd.equals("HideCategory0")) {
         PinEmUp.getMainApp().getNotes().hideCategory((byte)0);
      } else if (cmd.equals("HideCategory1")) {
         PinEmUp.getMainApp().getNotes().hideCategory((byte)1);
      } else if (cmd.equals("HideCategory2")) {
         PinEmUp.getMainApp().getNotes().hideCategory((byte)2);
      } else if (cmd.equals("HideCategory3")) {
         PinEmUp.getMainApp().getNotes().hideCategory((byte)3);
      } else if (cmd.equals("HideCategory4")) {
         PinEmUp.getMainApp().getNotes().hideCategory((byte)4);
      } else if (cmd.equals("ShowCategory0")) {
         PinEmUp.getMainApp().getNotes().showCategory((byte)0);
      } else if (cmd.equals("ShowCategory1")) {
         PinEmUp.getMainApp().getNotes().showCategory((byte)1);
      } else if (cmd.equals("ShowCategory2")) {
         PinEmUp.getMainApp().getNotes().showCategory((byte)2);
      } else if (cmd.equals("ShowCategory3")) {
         PinEmUp.getMainApp().getNotes().showCategory((byte)3);
      } else if (cmd.equals("ShowCategory4")) {
         PinEmUp.getMainApp().getNotes().showCategory((byte)4);
      } else if (cmd.equals("SetTempDef0")) {
         PinEmUp.getUserSettings().setTempDef((byte)0);
      } else if (cmd.equals("SetTempDef1")) {
         PinEmUp.getUserSettings().setTempDef((byte)1);
      } else if (cmd.equals("SetTempDef2")) {
         PinEmUp.getUserSettings().setTempDef((byte)2);
      } else if (cmd.equals("SetTempDef3")) {
         PinEmUp.getUserSettings().setTempDef((byte)3);
      } else if (cmd.equals("SetTempDef4")) {
         PinEmUp.getUserSettings().setTempDef((byte)4);
      } else if (cmd.equals("ExportToTextFile")) {
         new ExportDialog();
      }
      
      if (cmd.equals("ShowCategory0") || cmd.equals("ShowCategory1") || cmd.equals("ShowCategory2") || cmd.equals("ShowCategory3") || cmd.equals("ShowCategory4") || cmd.equals("HideCategory0") || cmd.equals("HideCategory1") || cmd.equals("HideCategory2") || cmd.equals("HideCategory3") || cmd.equals("HideCategory4") || cmd.equals("ShowOnlyCategory0") || cmd.equals("ShowOnlyCategory1") || cmd.equals("ShowOnlyCategory2") || cmd.equals("ShowOnlyCategory3") || cmd.equals("ShowOnlyCategory4") || cmd.equals("AddNote") || cmd.equals("ShowAllNotes") || cmd.equals("HideAllNotes") || cmd.equals("DownloadNotesFromFTP")) {
         // save notes to file after every change
         NoteIO.writeNotesToFile(PinEmUp.getMainApp().getNotes(), PinEmUp.getUserSettings().getNotesFile());  
      }
   }

   public MenuLogic() {
      // do nothing
   }

}
