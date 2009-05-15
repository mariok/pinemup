package net.sourceforge.pinemup.gui;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

public class I18N {
   private static I18N instance;
   private ResourceBundle res;
   
   public static final String[] LOCALES = {
         "cs_CZ",
         "de_DE",
         "en_US"

   };
   
   public static final String[] LOCALE_NAMES = {
         "Česky",
         "Deutsch",
         "English"
   };
   
   public void setLocale(String locale) {
      String language = locale.substring(0, locale.indexOf("_"));
      String country = locale.substring(locale.indexOf("_")+1);
      Locale myLocale = new Locale(language, country);
      res = ResourceBundle.getBundle("net.sourceforge.pinemup.resources.i18n.messages", myLocale);
      Locale.setDefault(myLocale);
   }
   
   public static I18N getInstance() {
      if (I18N.instance == null) {
         I18N.instance = new I18N();
      }
      return I18N.instance;
   }
   
   private I18N() {
      res = ResourceBundle.getBundle("net.sourceforge.pinemup.resources.i18n.messages", new Locale("en","US"));
   }
   
   public String getString(String key) {
      String s;
      try {
         s = res.getString(key);
      } catch (MissingResourceException e) {
         System.err.println("Missing Resource: " + key);
         s = key;
      }
      return s;
   }
}
