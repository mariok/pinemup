package net.sourceforge.pinemup.ui.swing;

import java.awt.Window;

public class SwingUtils {
   public static void centerWindowOnScreen(Window window) {
      int screenHeight = (int)window.getToolkit().getScreenSize().getHeight();
      int screenWidth = (int)window.getToolkit().getScreenSize().getWidth();
      int x = (screenWidth - window.getWidth()) / 2;
      int y = (screenHeight - window.getHeight()) / 2;
      window.setLocation(x, y);
   }
}
