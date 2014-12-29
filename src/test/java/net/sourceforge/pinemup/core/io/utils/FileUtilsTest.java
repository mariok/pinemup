package net.sourceforge.pinemup.core.io.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

   @Test
       public void testCheckAndAddExtensionNoActionRequired() {
      assertEquals("notes.xml", FileUtils.checkAndAddExtension("notes.xml", ".xml"));
   }

   @Test
   public void testCheckAndAddExtensionWithMissingExtension() {
      assertEquals("notes.xml", FileUtils.checkAndAddExtension("notes", ".xml"));
   }

   @Test
   public void testCheckAndAddExtensionWithWrongExtension() {
      assertEquals("notes.txt.xml", FileUtils.checkAndAddExtension("notes.txt", ".xml"));
   }
}