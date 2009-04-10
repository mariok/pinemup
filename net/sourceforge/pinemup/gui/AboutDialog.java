/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2008 by Mario Koedding
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

package net.sourceforge.pinemup.gui;


import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.sourceforge.pinemup.logic.*;

public class AboutDialog extends JFrame implements ActionListener, HyperlinkListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JButton okButton;
   
   private JEditorPane makeAboutTab() {
      String msg = "";
      msg += "<html>";
      msg += "<p>pin 'em up</p>";
      msg += "<p>version " + PinEmUp.getVersion() + "</p>";
      msg += "<p>(C) 2007 Mario Koedding</p>";
      msg += "<p><a href=\"http://pinemup.sourceforge.net\" target=\"blank\">http://pinemup.sourceforge.net</a></p>";
      msg += "</html>";
      JEditorPane p = new JEditorPane("text/html",msg);
      p.addHyperlinkListener(this);
      p.setEditable(false);
      return p;
   }

   private JEditorPane makeAuthorsTab() {
      String msg = "";
      msg += "<html>";
      msg += "<p><strong>Mario Koedding</strong><br />";
      msg += "<a href=\"mailto:mario@koedding.net\">mario@koedding.net</a><br />";
      msg += "developer &amp; founder of the project</p>";
      msg += "</html>";
      JEditorPane p = new JEditorPane("text/html",msg);
      p.addHyperlinkListener(this);
      p.setEditable(false);
      return p;
   }

   private JScrollPane makeLicenseTab() {
      String msg = "";
      msg += "(C) 2007 Mario Koedding\r\n\r\n";
      msg += "This program is licensed under the terms of the GNU GPL V3 or any later version.\r\n\r\n\r\n";
      msg += ResourceLoader.getInstance().getLicense();
      JEditorPane p = new JEditorPane("text/plain",msg);
      p.setEditable(false);
      p.addHyperlinkListener(this);
      JScrollPane myScrollPane = new JScrollPane(p);
      p.setCaretPosition(0); //scroll back to the top
      return myScrollPane;
   }
   
   public AboutDialog() {
      super("About pin 'em up");
      setSize(new Dimension(600,350));

      // PREPARE ALL PANELS
      // ---------------------
      JPanel mainPanel = new JPanel(new BorderLayout());
      
      //tabbed pane and tabs
      JTabbedPane tpane = new JTabbedPane();
      tpane.addTab("about", null, makeAboutTab(), "about");
      tpane.addTab("authors", null, makeAuthorsTab(), "authors");
      tpane.addTab("license", null, makeLicenseTab(), "license");
      mainPanel.add(tpane, BorderLayout.CENTER);

      // PANEL WITH BUTTONS
      okButton = new JButton("close");
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

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == okButton) {
         setVisible(false);
         dispose();
      }
   }

   public void hyperlinkUpdate(HyperlinkEvent e) {
      if (e.getEventType().toString().equals("ACTIVATED")) { 
         if (e.getURL().toString().startsWith("mailto:")) {
            try {
               URI mailURI = new URI("mailto",e.getURL().toString().substring(7),null);
               Desktop.getDesktop().mail(mailURI);
            } catch (URISyntaxException err1) {
               //do nothing
            } catch (IOException err2) {
               //do nothing
            }
         } else {
            try {
               Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (IOException ioe) {
               //do nothing
            } catch (URISyntaxException urie) {
               //do nothing
            }            
         }
      }
   }
}

