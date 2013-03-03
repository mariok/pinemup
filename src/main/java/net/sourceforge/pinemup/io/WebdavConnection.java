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

package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.net.ssl.SSLHandshakeException;
import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;

class WebdavConnection extends ServerConnection {
   private static final String PROTOCOL_HTTP = "http";
   private static final String PROTOCOL_HTTPS = "https";

   private String protocol;

   public WebdavConnection(boolean sslEnabled) {
      if (sslEnabled) {
         protocol = PROTOCOL_HTTPS;
      } else {
         protocol = PROTOCOL_HTTP;
      }
   }

   public WebdavConnection() {
      protocol = PROTOCOL_HTTP;
   }

   private void setDefaultAuthenticator() {
      Authenticator.setDefault(new Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(UserSettings.getInstance().getServerUser(), UserSettings.getInstance()
                  .getServerPasswd().toCharArray());
         }
      });
   }

   public void importNotesFromServer() {
      boolean downloaded = true;

      FileOutputStream fos = null;
      InputStream is = null;
      try {
         makeBackupFile();
         File f = new File(UserSettings.getInstance().getNotesFile());
         fos = new FileOutputStream(f);
         String urlString = protocol + "://" + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir()
               + f.getName();
         setDefaultAuthenticator();
         URL url = new URL(urlString);
         HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
         is = urlc.getInputStream();
         int nextByte = is.read();
         while (nextByte != -1) {
            fos.write(nextByte);
            nextByte = is.read();
         }
         if (urlc.getResponseCode() != HttpURLConnection.HTTP_OK) {
            downloaded = false;
         }
      } catch (SSLHandshakeException e) { // Certificate error (self-signed?)
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.sslcertificateerror"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
         downloaded = false;
      } catch (Exception e) {
         downloaded = false;
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if (fos != null) {
            try {
               fos.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      if (downloaded) {
         deleteBackupFile();
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfiledownloaded"),
               I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
      } else {
         restoreFileFromBackup();
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotdownloaded"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      }
   }

   public void exportNotesToServer() {
      boolean uploaded = true;

      FileInputStream fis = null;
      OutputStream os = null;
      try {
         File f = new File(UserSettings.getInstance().getNotesFile());
         fis = new FileInputStream(f);
         String urlString = protocol + "://" + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir()
               + f.getName();
         setDefaultAuthenticator();
         URL url = new URL(urlString);
         HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
         urlc.setDoOutput(true);
         urlc.setRequestMethod("PUT");
         os = urlc.getOutputStream();
         int nextByte = fis.read();
         while (nextByte != -1) {
            os.write(nextByte);
            nextByte = fis.read();
         }
         if (urlc.getResponseCode() != HttpURLConnection.HTTP_CREATED && urlc.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
            uploaded = false;
         }
      } catch (SSLHandshakeException e) { // Certificate error (self-signed?)
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.sslcertificateerror"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
         uploaded = false;
      } catch (Exception e) {
         uploaded = false;
      } finally {
         if (os != null) {
            try {
               os.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if (fis != null) {
            try {
               fis.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      if (uploaded) {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfileuploaded"),
               I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotuploaded"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      }
   }
}
