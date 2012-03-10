/*
 * pin 'em up
 *
 * Copyright (C) 2007-2012 by Mario Ködding
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

package net.sourceforge.pinemup.core;

import java.util.Observable;

public class Note extends Observable {
   private String text;

   private boolean hidden;
   private boolean alwaysOnTop;

   private short xpos, ypos, xsize, ysize, fontsize;

   private NoteColor color;

   private Category category;

   public void setAlwaysOnTop(boolean b) {
      alwaysOnTop = b;
      setChanged();
      notifyObservers();
   }

   public boolean isAlwaysOnTop() {
      return alwaysOnTop;
   }

   public void setFontSize(short size) {
      fontsize = size;
      setChanged();
      notifyObservers();
   }

   public short getFontSize() {
      return fontsize;
   }

   public Note() { // for failnote
      hidden = true;
      text = "";
   }

   public void setHidden(boolean b) {
      hidden = b;
      setChanged();
      notifyObservers();
   }

   public boolean isHidden() {
      return hidden;
   }

   public Note(String text, NoteColor color) {
      this.text = text;
      hidden = false;
      xpos = UserSettings.getInstance().getDefaultWindowXPostition();
      ypos = UserSettings.getInstance().getDefaultWindowYPostition();
      xsize = UserSettings.getInstance().getDefaultWindowWidth();
      ysize = UserSettings.getInstance().getDefaultWindowHeight();
      fontsize = UserSettings.getInstance().getDefaultFontSize();
      alwaysOnTop = UserSettings.getInstance().getDefaultAlwaysOnTop();
      this.color = color;
   }

   public void setText(String t) {
      text = t;
      setChanged();
      notifyObservers();
   }

   public String getText() {
      return text;
   }

   public void setPosition(short x, short y) {
      xpos = x;
      ypos = y;
      setChanged();
      notifyObservers();
   }

   public void setSize(short x, short y) {
      xsize = x;
      ysize = y;
      setChanged();
      notifyObservers();
   }

   public short getXPos() {
      return xpos;
   }

   public short getYPos() {
      return ypos;
   }

   public short getXSize() {
      return xsize;
   }

   public short getYSize() {
      return ysize;
   }

   public Category getCategory() {
      return category;
   }

   public void setCategory(Category c) {
      category = c;
      setChanged();
      notifyObservers();
   }

   public void moveToCategory(Category newCat) {
      if (newCat != null) {
         // remove from old category
         if (category != null) {
            category.removeNote(this);
         }
         // add to new category
         newCat.addNote(this);
         // set color to default color of the new category
         setColor(newCat.getDefaultNoteColor());
         // update Category name and color in Window
         setChanged();
         notifyObservers();
      }
   }

   public void setColor(NoteColor color) {
      this.color = color;
      setChanged();
      notifyObservers();
   }

   public NoteColor getColor() {
      return color;
   }
}
