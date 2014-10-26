package net.sourceforge.pinemup.ui.swing.tray.fallback;

import net.sourceforge.pinemup.core.io.resources.ResourceLoader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.event.MouseAdapter;

public class FallbackDialog extends JFrame {
   public FallbackDialog(String title, MouseAdapter mouseAdapter, ResourceLoader resourceLoader) {
      super(title);

      JLabel iconLabel = new JLabel(new ImageIcon(resourceLoader.getTrayIcon(32)));
      iconLabel.addMouseListener(mouseAdapter);

      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setContentPane(iconLabel);
      pack();
   }
}
