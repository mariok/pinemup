/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
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

import java.io.*;

import net.sourceforge.pinemup.gui.*;

public class Note implements Serializable {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private String text;

   private boolean hidden, alwaysOnTop;

   private transient NoteWindow window;

   private short xpos, ypos, xsize, ysize, fontsize;
   
   private byte bgColor;
   
   private transient CategoryList categories;
   
   private transient UserSettings settings;
   
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

   public Note(String text, UserSettings s, CategoryList cl, byte bgColor) {
      this.text = text;
      hidden = false;
      window = null;
      settings = s;
      xpos = settings.getDefaultWindowXPostition();
      ypos = settings.getDefaultWindowYPostition();
      xsize = settings.getDefaultWindowWidth();
      ysize = settings.getDefaultWindowHeight();
      fontsize = settings.getDefaultFontSize();
      alwaysOnTop = settings.getDefaultAlwaysOnTop();
      this.bgColor = bgColor;
      categories = cl;
   }

   public void showIfNotHidden() {
      if (hidden == false && window == null) {
         window = new NoteWindow(this, categories, settings);
      }
   }
   
   public void unhideAndShow() {
      if (window == null) {
         window = new NoteWindow(this, categories, settings);
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
   
   public void jumpInto() {
      if (window != null) {
         window.jumpIntoTextArea();
      }
   }
   
   public void moveToCategory(Category newCat) {
      if (newCat != null) {
         //get old category
         Category myCategory = categories.getCategoryForNote(this);
         //add to new category
         newCat.getNotes().add(this);
         //remove from old category
         if (myCategory != null) {
            //remove from current category
            myCategory.getNotes().removeWithoutHiding(this);
         }
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
