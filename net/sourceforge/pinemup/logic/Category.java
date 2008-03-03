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

public class Category {
  private String name;
  private NoteList notes;
  private boolean defaultCategory;
  private byte defaultNoteColor;
  
  public Category(String name, NoteList nl, boolean def, byte defNoteColor) {
     this.name = name;
     this.notes = nl;
     this.defaultCategory = def;
     this.defaultNoteColor = defNoteColor;
  }
  
  public NoteList getNotes() {
     return notes;
  }
  
  public String getName() {
     return name;
  }
  
  public void setName(String s) {
     name = s;
  }
  
  public void rename(String s) {
     name = s;
     notes.updateAllCategoriesInWindows();
  }
  
  public boolean isDefaultCategory() {
     return defaultCategory;
  }
  
  public void setDefault(boolean b) {
     defaultCategory = b;
  }
  
  public void setDefaultNoteColor(byte c) {
     defaultNoteColor = c;
  }
  
  public byte getDefaultNoteColor() {
     return defaultNoteColor;
  }
}
