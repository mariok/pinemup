package net.sourceforge.pinemup.core.io.utils;

public class FileUtils {
   public static String checkAndAddExtension(String fileName, String requiredExtension) {
      String checkedFileName = fileName;

      String actualExtension = fileName.substring(fileName.length() - requiredExtension.length());
      if (!actualExtension.equalsIgnoreCase(requiredExtension)) {
         checkedFileName = fileName + requiredExtension.toLowerCase();
      }

      return checkedFileName;
   }
}
