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

import java.util.prefs.Preferences;

public class UserSettings {
   /**
    * 
    */
   
   private Preferences prefs;
   
   private static final long serialVersionUID = 1L;

   private short defaultWindowWidth;
   private short defaultWindowHeight;
   private short defaultWindowXPosition;
   private short defaultWindowYPosition;
   private short defaultFontSize;
   private String notesFile;
   private String ftpServer;
   private String ftpUser;
   private char[] ftpPasswd;
   private String ftpDir;
   private boolean defaultAlwaysOnTop;
   private byte tempDef = 0;
   private byte closeicon;
   private boolean showCategory;
   private boolean confirmDeletion;
   
   public void setConfirmDeletion(boolean b) {
      confirmDeletion = b;
   }
   
   public boolean getConfirmDeletion() {
      return confirmDeletion;
   }
   
   public void setShowCategory(boolean sc) {
      showCategory = sc;
   }
   
   public boolean getShowCategory() {
      return showCategory;
   }
   
   public byte getCloseIcon() {
      return closeicon;
   }
   
   public void setCloseIcon(byte ci) {
      closeicon = ci;
   }
   
   public String getNotesFile() {
      return notesFile;
   }
   
   public void setNotesFile(String s) {
      notesFile = s;
   }
   
   public boolean getDefaultAlwaysOnTop() {
      return defaultAlwaysOnTop;
   }
   
   public void setDefaultAlwaysOnTop(boolean b) {
      defaultAlwaysOnTop = b;
   }
   
   public short getDefaultFontSize() {
      return defaultFontSize;
   }
   
   public void setDefaultFontSize(short size) {
      defaultFontSize = size;
   }
   
   public byte getTempDef() {
      return tempDef;
   }
   
   public void setTempDef(byte td) {
      tempDef = td;
   }

   public short getDefaultWindowWidth() {
      return defaultWindowWidth;
   }

   public void setDefaultWindowWidth(short x) {
      if (x < 30) {
         x = 30;
      }
      defaultWindowWidth = x;
   }

   public short getDefaultWindowHeight() {
      return defaultWindowHeight;
   }

   public void setDefaultWindowHeight(short y) {
      if (y < 30) {
         y = 30;
      }
      defaultWindowHeight = y;
   }

   public short getDefaultWindowXPostition() {
      return defaultWindowXPosition;
   }

   public void setDefaultWindowXPosition(short x) {
      defaultWindowXPosition = x;
   }

   public short getDefaultWindowYPostition() {
      return defaultWindowYPosition;
   }

   public void setDefaultWindowYPosition(short y) {
      defaultWindowYPosition = y;
   }

   public String getFtpServer() {
      return ftpServer;
   }

   public void setFtpServer(String s) {
      ftpServer = s;
   }

   public String getFtpUser() {
      return ftpUser;
   }

   public void setFtpUser(String u) {
      ftpUser = u;
   }

   public char[] getFtpPasswd() {
      return ftpPasswd;
   }

   public String getFtpPasswdString() {
      String tempString = "";
      for (int i = 0; i < ftpPasswd.length; i++) {
         tempString += ftpPasswd[i];
      }
      return tempString;
   }

   public void setFtpPasswd(char[] p) {
      ftpPasswd = p;
   }

   public String getFtpDir() {
      return ftpDir;
   }

   public void setFtpDir(String d) {
      ftpDir = d;
   }

   public void saveSettings() {
      // save preferences
      prefs.putInt("peu_defaultWindowWidth", defaultWindowWidth);
      prefs.putInt("peu_defaultWindowHeight", defaultWindowHeight);
      prefs.putInt("peu_defaultWindowXPosition", defaultWindowXPosition);
      prefs.putInt("peu_defaultWindowYPosition", defaultWindowYPosition);
      prefs.putInt("peu_defaultFontSize", defaultFontSize);
      prefs.putBoolean("peu_defaultAlwaysOnTop", defaultAlwaysOnTop);
      prefs.put("peu_notesFile", notesFile);
      prefs.put("peu_ftpServer", ftpServer);
      prefs.put("peu_ftpUser", ftpUser);
      prefs.put("peu_ftpPasswd", getFtpPasswdString());
      prefs.put("peu_ftpDir", ftpDir);
      prefs.putInt("peu_closeicon", closeicon);
      prefs.putBoolean("peu_showCategory", showCategory);
      prefs.putBoolean("peu_confirmDeletion", confirmDeletion);
   }

   public UserSettings() {
      prefs = Preferences.userNodeForPackage(PinEmUp.class);

      String homeDir = System.getProperty("user.home");
      if (homeDir.charAt(homeDir.length()-1) != '\\' && homeDir.charAt(homeDir.length()-1) != '/') {
         homeDir = homeDir + "/";
      }
      
      defaultWindowWidth = Short.parseShort(prefs.get("peu_defaultWindowWidth", "170"));
      defaultWindowHeight = Short.parseShort(prefs.get("peu_defaultWindowHeight", "150"));
      defaultWindowXPosition = Short.parseShort(prefs.get("peu_defaultWindowXPosition", "0"));
      defaultWindowYPosition = Short.parseShort(prefs.get("peu_defaultWindowYPosition", "0"));
      defaultFontSize = Short.parseShort(prefs.get("peu_defaultFontSize", "14"));
      defaultAlwaysOnTop = prefs.getBoolean("peu_defaultAlwaysOnTop", false);
      notesFile = prefs.get("peu_notesFile", homeDir + "pinemup.xml");
      ftpServer = prefs.get("peu_ftpServer", "ftp.example.com");
      ftpUser = prefs.get("peu_ftpUser", "anonymous");
      ftpPasswd = prefs.get("peu_ftpPasswd", "").toCharArray();
      ftpDir = prefs.get("peu_ftpDir", "/");
      closeicon = Byte.parseByte(prefs.get("peu_closeicon", "1"));
      showCategory = prefs.getBoolean("peu_showCategory", false);
      confirmDeletion = prefs.getBoolean("peu_confirmDeletion", true);
   }
}
