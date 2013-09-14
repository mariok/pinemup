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

package net.sourceforge.pinemup.core;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Note {
   public static final short MIN_FONT_SIZE = 5;
   public static final short MAX_FONT_SIZE = 30;

   private String text;

   private boolean hidden;
   private boolean alwaysOnTop;

   private short xpos, ypos, xsize, ysize, fontSize;

   private NoteColor color;

   private Collection<NoteChangedEventListener> noteChangedEventListeners = new ConcurrentLinkedDeque<>();

   public void addNoteChangedEventListener(NoteChangedEventListener listener) {
      noteChangedEventListeners.add(listener);
   }

   public void removeNoteChangedEventListener(NoteChangedEventListener listener) {
      noteChangedEventListeners.remove(listener);
   }

   public void setAlwaysOnTop(boolean alwaysOnTop) {
      if (alwaysOnTop != this.alwaysOnTop) {
         this.alwaysOnTop = alwaysOnTop;
         fireNoteChangedEvent();
      }
   }

   public boolean isAlwaysOnTop() {
      return alwaysOnTop;
   }

   public void setFontSize(short fontSize) {
      if (fontSize != this.fontSize && fontSize >= MIN_FONT_SIZE && fontSize <= MAX_FONT_SIZE) {
         this.fontSize = fontSize;
         fireNoteChangedEvent();
      }
   }

   public short getFontSize() {
      return fontSize;
   }

   public void setHidden(boolean hidden) {
      if (hidden != this.hidden) {
         this.hidden = hidden;
         fireNoteChangedEvent();
      }
   }

   public boolean isHidden() {
      return hidden;
   }

   public Note() {
      text = "";
      xpos = UserSettings.getInstance().getDefaultWindowXPostition();
      ypos = UserSettings.getInstance().getDefaultWindowYPostition();
      xsize = UserSettings.getInstance().getDefaultWindowWidth();
      ysize = UserSettings.getInstance().getDefaultWindowHeight();
      fontSize = UserSettings.getInstance().getDefaultFontSize();
      alwaysOnTop = UserSettings.getInstance().getDefaultAlwaysOnTop();
      color = NoteColor.DEFAULT_COLOR;
   }

   public void setText(String text) {
      if (!text.equals(this.text)) {
         this.text = text;
         fireNoteChangedEvent();
      }
   }

   public String getText() {
      return text;
   }

   public void setPosition(short x, short y) {
      if (x != xpos || y != ypos) {
         xpos = x;
         ypos = y;
         fireNoteChangedEvent();
      }
   }

   public short getXPos() {
      return xpos;
   }

   public short getYPos() {
      return ypos;
   }

   public void setSize(short x, short y) {
      if (x != xsize || y != ysize) {
         xsize = x;
         ysize = y;
         fireNoteChangedEvent();
      }
   }

   public short getXSize() {
      return xsize;
   }

   public short getYSize() {
      return ysize;
   }

   public void setColor(NoteColor color) {
      this.color = color;
      fireNoteChangedEvent();
   }

   public NoteColor getColor() {
      return color;
   }

   public void fireNoteChangedEvent() {
      for (NoteChangedEventListener listener : noteChangedEventListeners) {
         listener.noteChanged(new NoteChangedEvent(this));
      }
   }
}
