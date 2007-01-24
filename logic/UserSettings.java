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

package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UserSettings implements Serializable {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private short defaultWindowWidth;

   private short defaultWindowHeight;

   private short defaultWindowXPosition;

   private short defaultWindowYPosition;

   private String ftpServer;

   private String ftpUser;

   private char[] ftpPasswd;

   private String ftpDir;

   private String[] category;
   
   private transient byte tempDef = 0;
   
   public byte getTempDef() {
      return tempDef;
   }
   
   public void setTempDef(byte td) {
      tempDef = td;
   }

   public String[] getCategoryNames() {
      return category;
   }

   public void setCategoryName(short n, String name) {
      if (n >= 0 && n <= 4) {
         category[n] = name;
      }
   }

   public short getDefaultWindowWidth() {
      return defaultWindowWidth;
   }

   public void setDefaultWindowWidth(short x) {
      defaultWindowWidth = x;
   }

   public short getDefaultWindowHeight() {
      return defaultWindowHeight;
   }

   public void setDefaultWindowHeight(short y) {
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

   public static UserSettings loadSettings(String filename) {
      UserSettings s = null;
      try {
         FileInputStream fs = new FileInputStream(filename);
         ObjectInputStream is = new ObjectInputStream(fs);
         s = (UserSettings) is.readObject();
         is.close();
      } catch (ClassNotFoundException e) {
         // Do Nothing
      } catch (IOException e) {
         // Do Nothing
      }

      if (s == null) {
         s = new UserSettings();
         UserSettings.saveSettings(s, "config.dat");
      }

      return s;
   }

   public static void saveSettings(UserSettings s, String filename) {
      try {
         FileOutputStream fs = new FileOutputStream(filename);
         ObjectOutputStream os = new ObjectOutputStream(fs);
         os.writeObject(s);
         os.close();
      } catch (IOException e) {
         System.err.println(e.toString());
      }
   }

   public UserSettings() {
      // default values
      defaultWindowWidth = 170;
      defaultWindowHeight = 150;
      defaultWindowXPosition = 0;
      defaultWindowYPosition = 0;
      ftpServer = "ftp.example.com";
      ftpUser = "anonymous";
      ftpPasswd = null;
      ftpDir = "/";
      category = new String[5];
      category[0] = "home";
      category[1] = "office";
      category[2] = "";
      category[3] = "";
      category[4] = "";
   }
}
