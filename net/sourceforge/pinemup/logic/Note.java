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

import java.io.*;

import net.sourceforge.pinemup.gui.*;

public class Note implements Serializable {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private String text;

   private boolean visible, alwaysOnTop;

   private transient NoteWindow window;

   private Note next, prev;

   private short xpos, ypos, xsize, ysize, fontsize;
   
   private byte category;
   
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

   public void setCategory(byte catNr) {
      category = catNr;
      if (window != null) {
         window.updateToolTip();
      }
   }

   public byte getCategory() {
      return category;
   }

   public Note() { // for failnote
      next = null;
      prev = null;
      window = null;
      visible = false;
      text = "";
   }

   public Note(String text) {
      this.text = text;
      next = null;
      prev = null;
      visible = true;
      window = null;
      xpos = PinEmUp.getUserSettings().getDefaultWindowXPostition();
      ypos = PinEmUp.getUserSettings().getDefaultWindowYPostition();
      xsize = PinEmUp.getUserSettings().getDefaultWindowWidth();
      ysize = PinEmUp.getUserSettings().getDefaultWindowHeight();
      fontsize = PinEmUp.getUserSettings().getDefaultFontSize();
      category = PinEmUp.getUserSettings().getTempDef();
      alwaysOnTop = PinEmUp.getUserSettings().getDefaultAlwaysOnTop();
   }

   public void setVisible(boolean b) {
      visible = b;
   }

   public void showVisibleNote() {
      if (visible == true && window == null) {
         window = new NoteWindow(this);
      }
   }

   public void showNote() {
      if (window == null) {
         window = new NoteWindow(this);
         visible = true;
      }
   }

   public void showAll() {
      Note n = this;
      // back to the beginning
      while (n.getPrev() != null) {
         n = n.getPrev();
      }

      while (n != null) {
         n.showNote();
         n = n.getNext();
      }
   }

   public void showAllVisible() {
      Note n = this;
      // back to the beginning
      while (n.getPrev() != null) {
         n = n.getPrev();
      }

      while (n != null) {
         n.showVisibleNote();
         n = n.getNext();
      }
   }

   public void setNext(Note n) {
      next = n;
   }

   public void setPrev(Note n) {
      prev = n;
   }

   public Note getNext() {
      return next;
   }

   public Note getPrev() {
      return prev;
   }

   public static Note add(Note head, String s) {
      Note newNote = new Note(s);
      if (head == null) { // first note added
         head = newNote;
      }
      else {
         Note temp = head;
         while (temp.getNext() != null) {
            temp = temp.getNext();
         }
         temp.setNext(newNote);
         newNote.setPrev(temp);
      }
      return head;
   }

   public static Note remove(Note all, Note n) {
      net.sourceforge.pinemup.logic.Note head = all;
      if (head == n) { // first note
         head.hide();
         head = head.getNext();
         if (head != null) {
            head.setPrev(null);
         }
      } else {
         // search note
         while (all != n) {
            if (all != null) {
               all = all.getNext();
            }
         }
         // remove note
         all.hide();
         all.getPrev().setNext(all.getNext());            
         if (all.getNext() != null) {
            all.getNext().setPrev(all.getPrev());
         }
      }
      return head;
   }

   public void setText(String t) {
      text = t;
   }
   
   public String getText() {
      return text;
   }

   public void setWindow(NoteWindow w) {
      window = w;
   }
   
   public NoteWindow getWindow() {
      return window;
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
   
   public void tempHide() {
      if (window != null) {
         window.setVisible(false);
         window.dispose();
         window = null;   
      }
   }
   
   
   public void hide() {
      if (window != null) {
         window.setVisible(false);
         window.dispose();
         window = null;   
      }
      visible = false;
   }
   
   public void tempHideAll() {
      Note n = this;
      // back to the beginning
      while (n.getPrev() != null) {
         n = n.getPrev();
      }

      while (n != null) {
         n.tempHide();
         n = n.getNext();
      }
   }

   public void hideAll() {
      Note n = this;
      // back to the beginning
      while (n.getPrev() != null) {
         n = n.getPrev();
      }

      while (n != null) {
         n.hide();
         n = n.getNext();
      }
   }
   
   public void showOnlyCategory(byte nr) {
      Note n = this;
      // back to the beginning
      while (n.getPrev() != null) {
         n = n.getPrev();
      }

      while (n != null) {
         if (n.getCategory() != nr) {
            n.hide();            
         } else {
            n.showNote();
         }
         n = n.getNext();
      }
   }
   
   public void showCategory(byte nr) {
      Note n = this;
      // back to the beginning
      while (n.getPrev() != null) {
         n = n.getPrev();
      }

      while (n != null) {
         if (n.getCategory() == nr) {
            n.showNote();            
         }
         n = n.getNext();
      }
   }
   
   public void hideCategory(byte nr) {
      Note n = this;
      // back to the beginning
      while (n.getPrev() != null) {
         n = n.getPrev();
      }

      while (n != null) {
         if (n.getCategory() == nr) {
            n.hide();            
         }
         n = n.getNext();
      }
   }

}
