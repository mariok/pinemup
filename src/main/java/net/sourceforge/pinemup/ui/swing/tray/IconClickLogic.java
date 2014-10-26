/*
 * pin 'em up
 *
 * Copyright (C) 2007-2013 by Mario Ködding
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.sourceforge.pinemup.ui.swing.tray;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.ui.swing.notewindow.NoteWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPopupMenu;
import javax.swing.Timer;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconClickLogic extends MouseAdapter implements ActionListener {
   private static final Logger LOG = LoggerFactory.getLogger(IconClickLogic.class);

   private static final int CLICK_INTERVAL = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");

   private Timer timer;

   private final JPopupMenu trayMenu;

   private final NoteWindowManager noteWindowManager;

   public IconClickLogic(JPopupMenu trayMenu, NoteWindowManager noteWindowManager) {
      super();
      this.trayMenu = trayMenu;
      this.noteWindowManager = noteWindowManager;
   }

   @Override
   public void actionPerformed(ActionEvent arg0) {
      timer.stop();
      singleClick();
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      LOG.debug("Mouse click on icon (button {}).", e.getButton());

      if (e.getButton() == MouseEvent.BUTTON1) {
         if (e.getClickCount() == 1) {
            timer = new Timer(CLICK_INTERVAL, this);
            timer.start();
         } else {
            timer.stop();
            if (e.getClickCount() == 2) {
               doubleClick();
            }
         }
      }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if (e.isPopupTrigger()) {
         trayMenu.show(e.getComponent(), e.getX(), e.getY());
      }
   }

   @Override
   public void mousePressed(MouseEvent e) {
      if (e.isPopupTrigger()) {
         trayMenu.show(e.getComponent(), e.getX(), e.getY());
      }
   }

   private void singleClick() {
      noteWindowManager.bringAllWindowsToFront();
   }

   private void doubleClick() {
      CategoryManager.getInstance().createNoteAndAddToDefaultCategory();
   }
}
