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

package net.sourceforge.pinemup.ui.swing.notewindow;

import javax.swing.JFrame;

class DummyFrame extends JFrame {
   private static final long serialVersionUID = -5528849950380262389L;

   @Override
   public boolean isShowing() {
      return true;
   }

   public DummyFrame() {
      super("pin 'em up");
      this.setUndecorated(true);
      this.setBounds(-1, -1, 1, 1);
      String osName = System.getProperty("os.name");
      if (osName == null || !(osName.toLowerCase().indexOf("win") >= 0)) {
         this.setVisible(true);
      }
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }
}
