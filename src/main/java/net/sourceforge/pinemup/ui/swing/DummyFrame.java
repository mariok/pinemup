package net.sourceforge.pinemup.ui.swing;

import javax.swing.JFrame;

import net.sourceforge.pinemup.core.PinEmUp;

public class DummyFrame extends JFrame {
   private static final long serialVersionUID = -5528849950380262389L;

   @Override
   public boolean isShowing() {
      return true;
   }

   private DummyFrame() {
      super("pin 'em up");
      this.setUndecorated(true);
      this.setBounds(-1, -1, 1, 1);
      String osName = System.getProperty("os.name");
      if (osName == null || !(osName.toLowerCase().indexOf("win") >= 0)) {
         this.setVisible(true);
      }
   }

   public static DummyFrame getInstance() {
      return Holder.INSTANCE;
   }

   private static class Holder {
      private static final DummyFrame INSTANCE = new DummyFrame();

      private Holder() {
         // do nothing
      }
   }
}
