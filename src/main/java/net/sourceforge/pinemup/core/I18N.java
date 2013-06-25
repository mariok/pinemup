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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class I18N {
   public enum SupportedLocale {
      de_DE("de", "DE"),
      cs_CZ("cs", "CZ"),
      en_US("en", "US"),
      ru("ru"),
      uk_UA("uk", "UA");

      private Locale locale;

      private SupportedLocale(String language) {
         locale = new Locale(language);
      }

      private SupportedLocale(String language, String country) {
         locale = new Locale(language, country);
      }

      public Locale getLocale() {
         return locale;
      }

      @Override
      public String toString() {
         return locale.getDisplayName();
      }

      public static SupportedLocale fromLocaleString(String localeString) {
         String[] localeParts = localeString.split("_");
         String language = localeParts[0];
         String country = "";
         if (localeParts.length > 1) {
            country = localeParts[1];
         }

         SupportedLocale locale = FALLBACK_LOCALE;
         for (SupportedLocale l : SupportedLocale.values()) {
            if (language.equals(l.getLocale().getLanguage()) && country.equals(l.getLocale().getCountry())) {
               locale = l;
               break;
            }
         }
         return locale;
      }
   }

   private static final SupportedLocale FALLBACK_LOCALE = SupportedLocale.en_US;

   private static final Logger LOG = LoggerFactory.getLogger(I18N.class);

   private ResourceBundle res;

   private static class Holder {
      private static final I18N INSTANCE = new I18N();
   }

   public static I18N getInstance() {
      return Holder.INSTANCE;
   }

   public void setLocale(Locale locale) {
      res = ResourceBundle.getBundle("i18n.messages", locale);
      Locale.setDefault(locale);
   }

   private I18N() {
      res = ResourceBundle.getBundle("i18n.messages", FALLBACK_LOCALE.getLocale());
   }

   public String getString(String key) {
      String s;
      try {
         s = res.getString(key);
      } catch (MissingResourceException e) {
         s = key;
         LOG.error("The resource with the key '" + key + "' could not be found.", e);
      }
      return s;
   }

   public String getString(String key, Object... args) {
      String message = getString(key).replace("'", "''");
      return MessageFormat.format(message, args);
   }
}
