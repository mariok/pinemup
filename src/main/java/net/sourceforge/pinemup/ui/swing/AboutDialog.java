/*
 * pin 'em up
 *
 * Copyright (C) 2007-2012 by Mario Ködding
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.sourceforge.pinemup.ui.swing;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.PinEmUp;
import net.sourceforge.pinemup.io.ResourceLoader;

public class AboutDialog extends JFrame implements ActionListener, HyperlinkListener {
   private static final long serialVersionUID = -6786897911342420374L;

   private static final int DIALOG_WIDTH = 600;
   private static final int DIALOG_HEIGHT = 350;

   private JButton okButton;

   private JEditorPane makeAboutTab() {
      String msg = "";
      msg += "<html>";
      msg += "<p>pin 'em up</p>";
      msg += "<p>version " + PinEmUp.VERSION + "</p>";
      msg += "<p>(C) 2007 - " + Calendar.getInstance().get(Calendar.YEAR) + " Mario Ködding</p>";
      msg += "<p><a href=\"http://pinemup.sourceforge.net\" target=\"blank\">http://pinemup.sourceforge.net</a></p>";
      msg += "</html>";
      JEditorPane p = new JEditorPane("text/html", msg);
      p.addHyperlinkListener(this);
      p.setEditable(false);
      return p;
   }

   private JEditorPane makeAuthorsTab() {
      String msg = "";
      msg += "<html>";
      msg += "<h1>Developers</h1>";
      msg += "<p>Mario Ködding<br />";
      msg += "<a href=\"mailto:mario.koedding@web.de\">mario.koedding@web.de</a><br />";
      msg += "developer &amp; founder of the project</p>";
      msg += "<p>&nbsp;</p>";
      msg += "<h1>Translators:</h1>";
      msg += "<p>Petr Mašek (Czech)</p>";
      msg += "</html>";
      JEditorPane p = new JEditorPane("text/html", msg);
      p.addHyperlinkListener(this);
      p.setEditable(false);
      return p;
   }

   private JScrollPane makeLicenseTab() {
      String msg = "";
      msg += "(C) 2007 - " + Calendar.getInstance().get(Calendar.YEAR) + " Mario Ködding\r\n\r\n";
      msg += "This program is licensed under the terms of the GNU GPL V3 or any later version.\r\n\r\n\r\n";
      msg += ResourceLoader.getInstance().getLicense();
      JEditorPane p = new JEditorPane("text/plain", msg);
      p.setEditable(false);
      p.addHyperlinkListener(this);
      JScrollPane myScrollPane = new JScrollPane(p);
      p.setCaretPosition(0); // scroll back to the top
      return myScrollPane;
   }

   public AboutDialog() {
      super(I18N.getInstance().getString("aboutdialog.title"));
      setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

      // PREPARE ALL PANELS
      // ---------------------
      JPanel mainPanel = new JPanel(new BorderLayout());

      // tabbed pane and tabs
      JTabbedPane tpane = new JTabbedPane();
      tpane.addTab(I18N.getInstance().getString("aboutdialog.abouttab"), null, makeAboutTab(),
            I18N.getInstance().getString("aboutdialog.abouttab"));
      tpane.addTab(I18N.getInstance().getString("aboutdialog.authorstab"), null, makeAuthorsTab(),
            I18N.getInstance().getString("aboutdialog.authorstab"));
      tpane.addTab(I18N.getInstance().getString("aboutdialog.licensetab"), null, makeLicenseTab(),
            I18N.getInstance().getString("aboutdialog.licensetab"));
      mainPanel.add(tpane, BorderLayout.CENTER);

      // PANEL WITH BUTTONS
      okButton = new JButton(I18N.getInstance().getString("closebutton"));
      okButton.addActionListener(this);
      okButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      okButton.setEnabled(true);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      buttonPanel.add(okButton);
      mainPanel.add(buttonPanel, BorderLayout.SOUTH);

      setContentPane(mainPanel);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);

      // center on screen
      int screenHeight = (int)getToolkit().getScreenSize().getHeight();
      int screenWidth = (int)getToolkit().getScreenSize().getWidth();
      int x = (screenWidth - getWidth()) / 2;
      int y = (screenHeight - getHeight()) / 2;
      setLocation(x, y);

      setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == okButton) {
         setVisible(false);
         dispose();
      }
   }

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
