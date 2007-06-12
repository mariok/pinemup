package net.sourceforge.pinemup.logic;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.gui.ExportDialog;
import net.sourceforge.pinemup.gui.SettingsDialog;

public class TrayMenuLogic extends MenuLogic {
   public void checkCommand(String cmd) {
      super.checkCommand(cmd);
      
      if (cmd.equals("ShowAboutDialog")) {
         String msg = "";
         msg += "pin 'em up\n";
         msg += "version " + PinEmUp.getVersion() + "\n\n";
         msg += "(C) 2007 Mario Koedding\n";
         msg += "mario.koedding@web.de\n\n";
         msg += "This program is licensed under the terms of the GNU GPL V2.\n";
         msg += "Read \"COPYING\" file for details.\n\n";
         msg += "visit http://pinemup.sourceforge.net";
         JOptionPane.showMessageDialog(null, msg, "about pin 'em up", JOptionPane.INFORMATION_MESSAGE);
      } else if (cmd.equals("ShowSettings")) {
         new SettingsDialog();
      } else if (cmd.equals("Exit")) {
         PinEmUp.getMainApp().exit();
      } else if (cmd.equals("UploadNotesToFTP")) {
         new FTPThread(true);
      } else if (cmd.equals("DownloadNotesFromFTP")) {
         new FTPThread(false);
      } else if (cmd.equals("ExportToTextFile")) {
         new ExportDialog();
      }
   }
}
