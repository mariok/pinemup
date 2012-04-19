package net.sourceforge.pinemup.ui.swing;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class DefaultHyperLinkListener implements HyperlinkListener {
   @Override
   public void hyperlinkUpdate(HyperlinkEvent e) {
      if (e.getEventType().toString().equals("ACTIVATED")) {
         if (e.getURL().toString().startsWith("mailto:")) {
            try {
               URI mailURI = new URI("mailto", e.getURL().toString().substring("mailto:".length()), null);
               Desktop.getDesktop().mail(mailURI);
            } catch (URISyntaxException err1) {
               // do nothing
            } catch (IOException err2) {
               // do nothing
            }
         } else {
            try {
               Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (IOException ioe) {
               // do nothing
            } catch (URISyntaxException urie) {
               // do nothing
            }
         }
      }
   }
}
