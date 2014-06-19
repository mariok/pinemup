package net.sourceforge.pinemup.core.settings;

/**
 * Stores common configuration information, which is cannot be modified by the
 * user.
 */
public class CommonConfiguration {
   private static final String APPLICATION_VERSION;

   static {
      if (CommonConfiguration.class.getPackage().getImplementationVersion() != null) {
         APPLICATION_VERSION = CommonConfiguration.class.getPackage().getImplementationVersion();
      } else {
         APPLICATION_VERSION = "dev-SNAPSHOT";
      }
   }

   /**
    * Private constructor for utility class.
    */
   private CommonConfiguration() {
      super();
   }

   public static String getApplicationVersion() {
      return APPLICATION_VERSION;
   }
}
