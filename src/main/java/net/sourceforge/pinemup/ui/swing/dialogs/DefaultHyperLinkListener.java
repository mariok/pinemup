package net.sourceforge.pinemup.ui.swing.dialogs;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DefaultHyperLinkListener implements HyperlinkListener {
   private static final Logger LOG = LoggerFactory.getLogger(DefaultHyperLinkListener.class);

   @Override
   public void hyperlinkUpdate(HyperlinkEvent e) {
      if (e.getEventType().toString().equals("ACTIVATED")) {
         if (e.getURL().toString().startsWith("mailto:")) {
            try {
               URI mailURI = new URI("mailto", e.getURL().toString().substring("mailto:".length()), null);
               Desktop.getDesktop().mail(mailURI);
            } catch (URISyntaxException | IOException ex) {
               LOG.error("Error while accessing email-hyperlink.", ex);
            }
         } else {
            try {
               Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (IOException | URISyntaxException ex) {
               LOG.error("Error while accessing hyperlink.", ex);
            }
         }
      }
   }
}
