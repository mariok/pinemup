package gui;

import java.awt.*;
import javax.swing.JLabel;

public class BackgroundLabel extends JLabel {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   private static final Color MYSTARTCOLOR = new Color(255, 255, 185);
   private static final Color MYENDCOLOR = new Color(255, 235, 70);
   private NoteWindow parentWindow;
   
   public void paintComponent(Graphics g) {
       setBounds(0,0,parentWindow.getWidth(),parentWindow.getHeight());
       int h = getHeight();
       int w = getWidth();
       Graphics2D g2 = (Graphics2D)g;
       
       for (int i=1; i<=w; i++) {
          int startX = i;
          int startY = 0;
          int endX = startX+1;
          int endY = startY+h;
          GradientPaint gradient = new GradientPaint(startX, startY, MYSTARTCOLOR, endX, endY, MYENDCOLOR);
          g2.setPaint(gradient);
       }
       g2.fillRect(0,0,w,h);
   }
   
   public BackgroundLabel(NoteWindow w) {
      super();
      parentWindow = w;
      setBounds(0,0,parentWindow.getWidth(),parentWindow.getHeight());
      setFocusable(false);
      setOpaque(false);
   }
}
