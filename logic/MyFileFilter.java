package logic;

import java.io.File;
import javax.swing.filechooser.*;

public class MyFileFilter extends FileFilter {
   private String ending;
   
   public boolean accept(File f) {
      return f.getName().toUpperCase().endsWith("."+ending) || f.isDirectory();
   }

   public String getDescription() {
      return "*."+ending.toLowerCase();
   }

   public MyFileFilter(String ending) {
      super();
      this.ending = ending.toUpperCase();
   }
}