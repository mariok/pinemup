package net.sourceforge.pinemup.logic;

import java.awt.TrayIcon;

import net.sourceforge.pinemup.menus.TrayMenu;

public class PinEmUpTrayIcon extends TrayIcon {
   private static PinEmUpTrayIcon instance;
   
   private PinEmUpTrayIcon() {
      super(ResourceLoader.getTrayIcon(), "pin 'em up", new TrayMenu());
      setImageAutoSize(false);
      IconClickLogic myIconListener = new IconClickLogic();
      // add actionlistener for doubleclick on icon
      addActionListener(myIconListener);
      // add mouselistener for traymenu
      addMouseListener(myIconListener);
   }
   
   public static PinEmUpTrayIcon getInstance() {
      if (PinEmUpTrayIcon.instance == null) {
         PinEmUpTrayIcon.instance = new PinEmUpTrayIcon();
      }
      return PinEmUpTrayIcon.instance;
   }
}
