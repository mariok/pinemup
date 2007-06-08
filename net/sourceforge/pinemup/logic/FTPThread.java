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

package net.sourceforge.pinemup.logic;

public class FTPThread extends Thread {
   private boolean upload;
   
   public FTPThread(boolean upload) {
      super("FTP-Up-/Download Thread");
      this.upload = upload;      
      this.start();
   }
   
   public void run() {
      if (upload) { // upload notes
         NoteIO.writeNotesToFTP(PinEmUp.getMainApp().getNotes(), "notes.dat");
      } else { // download notes
         // download Notes
         Note newNotes = NoteIO.getNotesFileFromFTP("notes.dat");
         // If successfull downloaded, replace:
         // hide notes
         if (PinEmUp.getMainApp().getNotes() != null) {
            PinEmUp.getMainApp().getNotes().hideAll();
         }
         // link and save new notes
         PinEmUp.getMainApp().setNotes(newNotes);
         // show all visible notes
         if (PinEmUp.getMainApp().getNotes() != null) {
            PinEmUp.getMainApp().getNotes().showAllVisible();
         }         
      }
   }

}
