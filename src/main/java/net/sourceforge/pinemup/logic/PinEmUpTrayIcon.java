/*
 * pin 'em up
 *
 * Copyright (C) 2007-2011 by Mario Ködding
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

package net.sourceforge.pinemup.logic;

import java.awt.TrayIcon;

import net.sourceforge.pinemup.menus.TrayMenu;

public final class PinEmUpTrayIcon extends TrayIcon {
   private static PinEmUpTrayIcon instance = new PinEmUpTrayIcon();

   private PinEmUpTrayIcon() {
      super(ResourceLoader.getInstance().getTrayIcon(), "pin 'em up", new TrayMenu());
      setImageAutoSize(false);
      IconClickLogic myIconListener = new IconClickLogic();
      // add actionlistener for doubleclick on icon
      addActionListener(myIconListener);
      // add mouselistener for traymenu
      addMouseListener(myIconListener);
   }

   public static PinEmUpTrayIcon getInstance() {
      return PinEmUpTrayIcon.instance;
   }
}
