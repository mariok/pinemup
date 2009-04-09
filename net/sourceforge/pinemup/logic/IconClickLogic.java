/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2008 by Mario Koedding
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

import java.awt.event.*;

public class IconClickLogic extends MouseAdapter implements ActionListener {
   private CategoryManager categories;
   
   public void actionPerformed(ActionEvent arg0) {
      Category defCat = categories.getDefaultCategory();
      if (defCat != null) {
         Note newNote = new Note("",categories,defCat.getDefaultNoteColor());
         defCat.addNote(newNote);
         newNote.showIfNotHidden();
         newNote.jumpInto();
      }
   }
   
   public IconClickLogic(CategoryManager c) {
      categories = c;
   }
}
