package net.sourceforge.pinemup.ui.swing;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

class DefaultHyperLinkListener implements HyperlinkListener {
   @Override
   public void hyperlinkUpdate(HyperlinkEvent e) {
      if (e.getEventType().toString().equals("ACTIVATED")) {
         if (e.getURL().toString().startsWith("mailto:")) {
            try {
               URI mailURI = new URI("mailto", e.getURL().toString().substring("mailto:".length()), null);
               Desktop.getDesktop().mail(mailURI);
            } catch (URISyntaxException err1) {
               printHyperlinkError();
            } catch (IOException err2) {
               printHyperlinkError();
            }
         } else {
            try {
               Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (IOException ioe) {
               printHyperlinkError();
            } catch (URISyntaxException urie) {
               printHyperlinkError();
            }
         }
      }
   }

   private void printHyperlinkError() {
      System.err.println("Error while accessing hyperlink.");
   }
}
