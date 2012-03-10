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

package net.sourceforge.pinemup.ui.swing;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class I18N {
   private static I18N instance = new I18N();
   private ResourceBundle res;

   public static final String[] LOCALES = {"cs_CZ", "de_DE", "en_US"

   };

   public static final String[] LOCALE_NAMES = {"Česky", "Deutsch", "English"};

   public void setLocale(String locale) {
      String language = locale.substring(0, locale.indexOf("_"));
      String country = locale.substring(locale.indexOf("_") + 1);
      Locale myLocale = new Locale(language, country);
      res = ResourceBundle.getBundle("i18n.messages", myLocale);
      Locale.setDefault(myLocale);
   }

   public static I18N getInstance() {
      return I18N.instance;
   }

   private I18N() {
      res = ResourceBundle.getBundle("i18n.messages", new Locale("en", "US"));
   }

   public String getString(String key) {
      String s;
      try {
         s = res.getString(key);
      } catch (MissingResourceException e) {
         s = key;
         e.printStackTrace();
      }
      return s;
   }
}
