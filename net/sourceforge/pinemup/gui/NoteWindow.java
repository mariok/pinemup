/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
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

import net.sourceforge.pinemup.logic.*;
import net.sourceforge.pinemup.menus.*;

import java.awt.event.*;
import java.awt.*;

public class NoteWindow extends JDialog implements FocusListener, WindowListener, ActionListener, MouseListener, MouseMotionListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JScrollPane textPanel;

   private JPanel topPanel, mainPanel, catPanel;

   private JTextArea textArea;

   private Note parentNote;

   private JButton closeButton, catButton;

   private int dx, dy;

   private boolean dragging; // required to make the window movable

   private boolean resizeCursor, resizing; // required to make window resizable
   
   private JLabel bgLabel;
   
   private UserSettings settings;
   
   private CategoryList categories;
   

   public NoteWindow(Note pn, CategoryList c, UserSettings s) {
      super(new JFrame());
      parentNote = pn;
      categories = c;
      settings = s;
      textPanel = new JScrollPane();
      textPanel.setOpaque(false);
      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setOpaque(false);
      topPanel = new JPanel(new BorderLayout());
      topPanel.setOpaque(false);
      mainPanel.add(textPanel, BorderLayout.CENTER);
      mainPanel.add(topPanel, BorderLayout.NORTH);
      dragging = false;
      catButton = null;
      
      //create category-label, if option is enabled
      if (settings.getShowCategory()) {
         Category cat = categories.getCategoryForNote(parentNote);
         if (cat != null) {
            catButton = new JButton(cat.getName());
            //catButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
            catButton.setRolloverEnabled(false);
            catButton.setEnabled(false);
            catButton.setFocusable(false);
            catButton.setPreferredSize(new Dimension(100,15));
            catButton.setMargin(new Insets(0, 0, 0, 0));
            catButton.setBackground(new Color(255,255,255,0));
            
            catPanel = new JPanel(new FlowLayout());
            catPanel.add(catButton);
            catPanel.setOpaque(false);
            catButton.addMouseListener(this);
            catButton.addMouseMotionListener(this);
            catButton.addFocusListener(this);
            topPanel.add(catPanel, BorderLayout.CENTER);
         }
      }

      // create and adjust TextArea
      textArea = new JTextArea(parentNote.getText(), 1, 1);
      textArea.setOpaque(false);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, 1, parentNote.getFontSize()));
      textArea.addFocusListener(this);
      textArea.setMargin(new Insets(0, 3, 3, 3));
      textPanel.setViewportView(textArea);
      textPanel.getViewport().setOpaque(false);      
      textPanel.setBorder(null);
      mainPanel.add(textPanel, BorderLayout.CENTER);
      
      // adjust and add buttons to the topPanel
      topPanel.addMouseListener(this);
      topPanel.addMouseMotionListener(this);
      topPanel.addFocusListener(this);
      
      ImageIcon closeIcon = new ImageIcon(ResourceLoader.getCloseIcon(settings.getCloseIcon()));
      closeButton = new JButton(closeIcon);
      closeButton.setBackground(new Color(255,255,255,0));
      closeButton.setRolloverEnabled(false);
      
      closeButton.setToolTipText("hide note");
      closeButton.addActionListener(this);
      closeButton.addMouseListener(this);
      closeButton.setFocusable(false);
      closeButton.setBorderPainted(false);
      closeButton.setHorizontalAlignment(SwingConstants.CENTER);
      closeButton.setPreferredSize(new java.awt.Dimension(20, 20));
      closeButton.setMargin(new Insets(4, 0, 0, 3));
      topPanel.add(closeButton, BorderLayout.EAST);
      updateCategory();
      
      setUndecorated(true);
      setLocation(parentNote.getXPos(),parentNote.getYPos());
      setSize(parentNote.getXSize(),parentNote.getYSize());

      // menu and doubleclick
      topPanel.addMouseListener(this);

      // rezeize listener
      resizing = false;
      resizeCursor = false;
      textArea.addMouseListener(this);
      textArea.addMouseMotionListener(this);
      
      setAlwaysOnTop(parentNote.isAlwaysOnTop());
      setContentPane(mainPanel);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      addWindowListener(this);
      textArea.setFocusable(false);
      
      bgLabel = new BackgroundLabel(this);
     
      getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
      setVisible(true);
   }

   public void focusGained(FocusEvent arg0) {
      //do nothing
   }

   public void focusLost(FocusEvent arg0) {
      parentNote.setText(textArea.getText());
      parentNote.setPosition((short)getX(), (short)getY());
      parentNote.setSize((short)getWidth(), (short)getHeight());
      textArea.setFocusable(false);
      
      // write notes to file after every change
      NoteIO.writeCategoriesToFile(categories, settings);
   }

   public void windowActivated(WindowEvent arg0) {
      // do nothing
   }

   public void windowClosed(WindowEvent arg0) {
      parentNote.hide();
   }

   public void windowClosing(WindowEvent arg0) {
      // do nothing
   }

   public void windowDeactivated(WindowEvent arg0) {
      // do nothing
   }

   public void windowDeiconified(WindowEvent arg0) {
      // do nothing
   }

   public void windowIconified(WindowEvent arg0) {
      // do nothing
   }

   public void windowOpened(WindowEvent arg0) {
      // do nothing
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == closeButton) {
         parentNote.hide();
         
         // write notes to file after every change
         NoteIO.writeCategoriesToFile(categories, settings);
      }

   }

   public void mouseClicked(MouseEvent e) {
      if (e.getSource() == topPanel || e.getSource() == catButton) {
         if (e.getClickCount() == 2) { // doubleclick on topPanel
            autoSizeY();
            // write notes to file after every change
            NoteIO.writeCategoriesToFile(categories, settings);
         }
         textArea.setFocusable(false);
      } else if (e.getSource() == textArea) {
         textArea.setFocusable(true);
         textArea.requestFocus();
      }
   }
   
   private void autoSizeY() {
      setSize(getWidth(),30);
      new Thread(new Runnable() {
         public void run() {
            try {
               Thread.sleep(5); // must wait for new settings (size) to be applied
            } catch (Exception e) {
               // do nothing
            }
            int sizeX = getWidth();
            int sizeY = textArea.getHeight();
            setSize(sizeX,sizeY+25);
            parentNote.setSize((short)sizeX, (short)(sizeY+25));
         }
      }).start();
   }

   public void mouseEntered(MouseEvent e) {
      // do nothing
   }

   public void mouseExited(MouseEvent e) {
      // do nothing

   }

   public void mousePressed(MouseEvent e) {
      if (e.getSource() == topPanel || e.getSource() == catButton) {
         checkPopupMenu(e);
      }
      if ((e.getSource() == topPanel || e.getSource() == catButton) && e.getButton() == MouseEvent.BUTTON1) {
         // Position on Panel
         dx = e.getXOnScreen() - getX();
         dy = e.getYOnScreen() - getY();

         dragging = true;
      }

      else if (e.getButton() == MouseEvent.BUTTON1 && resizeCursor
            && e.getSource() == textArea) {
         dx = getX() + getWidth() - e.getXOnScreen();
         dy = getY() + getHeight() - e.getYOnScreen();
         resizing = true;
      }

   }

   public void mouseReleased(MouseEvent e) {
      if (e.getSource() == topPanel || e.getSource() == catButton) {
         checkPopupMenu(e);
      }

      if (dragging && e.getButton() == MouseEvent.BUTTON1) {
         dragging = false;
      } else if (resizing && e.getButton() == MouseEvent.BUTTON1) {
         resizing = false;
      } else if (e.getSource() == closeButton) {
         // restore button backgorund if not pressed
         repaint();
      }
      parentNote.setSize((short)getWidth(), (short)getHeight());
   }

   private void checkPopupMenu(MouseEvent event) {
      if (event.isPopupTrigger()) {
         RightClickMenu popup = new RightClickMenu(this, categories, settings);
         popup.show(event.getComponent(), event.getX(), event.getY());
      }
   }

   public Note getParentNote() {
      return parentNote;
   }

   public void mouseDragged(MouseEvent e) {
      if (dragging) {
         setLocation(e.getXOnScreen() - dx, e.getYOnScreen() - dy);
      } else if (resizing) {
         int sx = e.getXOnScreen() - getX() + dx;
         int sy = e.getYOnScreen() - getY() + dy;
         if (sx < 30) {
            sx = 30;
         }
         if (sy < 30) {
            sy = 30;
         }
         setSize(sx, sy);
      }
   }
   
   public void updateCategory() {
      Category cat = categories.getCategoryForNote(parentNote);
      if (cat != null) {
         topPanel.setToolTipText("Category: " + cat.getName());
         if (catButton != null) {
            catButton.setText(cat.getName());
            repaint();
         }
      }
   }

   public void mouseMoved(MouseEvent e) {
      if (e.getSource() == textArea && !resizing) {
         // if in lower right corner, start resizing or change cursor
         if (e.getX() >= textArea.getWidth() - 10 && e.getY() >= textPanel.getHeight() - 10) { // height from panel because of vertical scrolling
            if (!resizeCursor) {
               resizeCursor = true;
               textArea.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
            }
         } else {
            if (resizeCursor) {
               resizeCursor = false;
               textArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
            }
         }
      }
   }
   
   public void refreshView() {
      textArea.setFont(new java.awt.Font("SERIF", 1, parentNote.getFontSize()));
   }
   
   public void jumpIntoTextArea() {
      textArea.setFocusable(true);
      textArea.requestFocus();
   }
}
