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

package net.sourceforge.pinemup.core;

import net.sourceforge.pinemup.ui.swing.NoteWindow;

public class Note {
   private String text;

   private boolean hidden, alwaysOnTop;

   private NoteWindow window;

   private short xpos, ypos, xsize, ysize, fontsize;

   private byte bgColor;

   private Category category;

   public void setAlwaysOnTop(boolean b) {
      alwaysOnTop = b;
      if (window != null) {
         window.setAlwaysOnTop(alwaysOnTop);
      }
   }

   public boolean isAlwaysOnTop() {
      return alwaysOnTop;
   }

   public void setFontSize(short size) {
      fontsize = size;
   }

   public short getFontSize() {
      return fontsize;
   }

   public Note() { // for failnote
      window = null;
      hidden = true;
      text = "";
   }

   public void setHidden(boolean b) {
      hidden = b;
   }

   public Note(String text, byte bgColor) {
      this.text = text;
      hidden = false;
      window = null;
      xpos = UserSettings.getInstance().getDefaultWindowXPostition();
      ypos = UserSettings.getInstance().getDefaultWindowYPostition();
      xsize = UserSettings.getInstance().getDefaultWindowWidth();
      ysize = UserSettings.getInstance().getDefaultWindowHeight();
      fontsize = UserSettings.getInstance().getDefaultFontSize();
      alwaysOnTop = UserSettings.getInstance().getDefaultAlwaysOnTop();
      this.bgColor = bgColor;
   }

   public void showIfNotHidden() {
      if (!hidden && window == null) {
         window = new NoteWindow(this);
      }
   }

   public void unhideAndShow() {
      if (window == null) {
         window = new NoteWindow(this);
      }
      hidden = false;
   }

   public void hide() {
      if (window != null) {
         window.setVisible(false);
         window.dispose();
         window = null;
      }
      hidden = true;
   }

   public void setText(String t) {
      text = t;
   }

   public String getText() {
      return text;
   }

   public void setPosition(short x, short y) {
      xpos = x;
      ypos = y;
   }

   public void setSize(short x, short y) {
      xsize = x;
      ysize = y;
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
   }

   public void jumpInto() {
      if (window != null) {
         window.jumpIntoTextArea();
      }
   }

   public void moveToCategory(Category newCat) {
      if (newCat != null) {
         //remove from old category
         if (category != null) {
            category.removeNote(this);
         }
         //add to new category
         newCat.addNote(this);
         //set color to default color of the new category
         setBGColor(newCat.getDefaultNoteColor());
         //update Category name and color in Window
         updateCategoryInWindow();
      }
   }

   public void updateCategoryInWindow() {
      if (!hidden && window != null) {
         window.updateCategory();
      }
   }

   public boolean isHidden() {
      return hidden;
   }

   public void setWindow(NoteWindow w) {
      window = w;
   }

   public void setBGColor(byte c) {
      bgColor = c;
   }

   public byte getBGColor() {
      return bgColor;
   }
}
