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

public class UpdateDialog extends JFrame implements ActionListener, HyperlinkListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JButton closeButton;
      public UpdateDialog(String updateText) {
      super("Update available for pin 'em up");
      setSize(new Dimension(400,350));

      // PREPARE ALL PANELS
      // ---------------------
      JPanel mainPanel = new JPanel(new BorderLayout());
      JEditorPane p = new JEditorPane("text/html",updateText);
      p.setEditable(false);
      p.addHyperlinkListener(this);
      JScrollPane myScrollPane = new JScrollPane(p);
      p.setCaretPosition(0); //scroll back to the top
      
      mainPanel.add(myScrollPane, BorderLayout.CENTER);

      // PANEL WITH BUTTONS
      closeButton = new JButton("close");
      closeButton.addActionListener(this);
      closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      closeButton.setEnabled(true);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      buttonPanel.add(closeButton);
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
      if (src == closeButton) {
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

