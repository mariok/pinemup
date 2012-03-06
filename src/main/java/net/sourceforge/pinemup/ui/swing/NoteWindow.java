/*
 * pin 'em up
 *
 * Copyright (C) 2007-2011 by Mario Ködding
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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.View;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.Note;
import net.sourceforge.pinemup.core.ResourceLoader;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.io.NoteIO;
import net.sourceforge.pinemup.ui.swing.menus.RightClickMenu;

public class NoteWindow extends JDialog implements FocusListener, WindowListener, ActionListener, MouseListener, MouseMotionListener, KeyListener {
   private static final int MIN_WINDOW_HEIGHT = 40;
   private static final int MIN_WINDOW_WIDTH = 30;

   private static final int SHORT_TEXT_LENGTH = 30;
   private static final int RESIZE_AREA_SIZE = 10;

   private static final Dimension SCROLLBUTTON_SIZE = new Dimension(10, 5);

   private static final long serialVersionUID = 1L;

   private static final int OFFSET = 35;

   private static final Color COLOR_TRANSPARENT = new Color(255, 255, 255, 0);

   private static final Dimension CLOSEBUTTON_SIZE = new Dimension(20, 20);

   private static final Dimension CATBUTTON_SIZE = new Dimension(100, 15);

   private static final int TOPPANEL_HEIGHT = 26;

   private JScrollPane textPanel;

   private JPanel topPanel, catPanel, mainPanel;

   private JTextArea textArea;

   private Note parentNote;

   private JButton closeButton, catButton, scrollButton;

   private int dx, dy;

   private boolean dragging; // required to make the window movable

   private boolean resizeCursor, resizing; // required to make window resizable

   private BackgroundLabel bgLabel;

   public NoteWindow(Note pn) {
      super(
         (new JFrame() {
            /**
            *
            */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isShowing() {
               return true;
            }
         }),
         "note:"
      );
      parentNote = pn;
      textPanel = new JScrollPane();
      textPanel.setBorder(null);
      textPanel.setOpaque(false);
      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setOpaque(false);
      topPanel = new JPanel(new BorderLayout());
      topPanel.setPreferredSize(new Dimension(UserSettings.getInstance().getDefaultWindowWidth(), TOPPANEL_HEIGHT));
      topPanel.setBorder(null);
      topPanel.setOpaque(false);
      mainPanel.add(textPanel, BorderLayout.CENTER);
      mainPanel.add(topPanel, BorderLayout.NORTH);
      dragging = false;
      catButton = null;

      //create category-label, if option is enabled
      if (UserSettings.getInstance().getShowCategory()) {
         Category cat = parentNote.getCategory();
         if (cat != null) {
            catButton = new JButton(cat.getName());
            catButton.setRolloverEnabled(false);
            catButton.setEnabled(false);
            catButton.setFocusable(false);
            catButton.setPreferredSize(CATBUTTON_SIZE);
            catButton.setMargin(new Insets(0, 0, 0, 0));
            catButton.setBackground(COLOR_TRANSPARENT);

            catPanel = new JPanel(new FlowLayout());
            catPanel.add(catButton);
            catPanel.setOpaque(false);
            catButton.addMouseListener(this);
            catButton.addMouseMotionListener(this);
            topPanel.add(catPanel, BorderLayout.CENTER);
         }
      }

      // create and adjust TextArea
      textArea = new JTextArea(parentNote.getText(), 1, 1);
      textArea.setEditable(true);
      textArea.setOpaque(false);
      textArea.addFocusListener(this);
      textArea.setMargin(new Insets(0, 10, 3, 0));
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      updateFontSize();
      textPanel.setViewportView(textArea);
      textPanel.getViewport().setOpaque(false);
      textPanel.setBorder(null);
      textPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      mainPanel.add(textPanel, BorderLayout.CENTER);

      //add area to show if note is scrollable
      ImageIcon scrollImage = new ImageIcon(ResourceLoader.getInstance().getScrollImage());
      scrollButton = new JButton(scrollImage);
      mainPanel.add(scrollButton, BorderLayout.SOUTH);
      scrollButton.setRolloverEnabled(false);
      scrollButton.setEnabled(false);
      scrollButton.setFocusable(false);
      scrollButton.setMargin(new Insets(0, 0, 0, 0));
      scrollButton.setPreferredSize(SCROLLBUTTON_SIZE);
      scrollButton.setBorder(null);
      scrollButton.setBackground(COLOR_TRANSPARENT);
      scrollButton.setVisible(false);
      scrollButton.setDisabledIcon(new ImageIcon(ResourceLoader.getInstance().getScrollImage()));

      // adjust and add buttons to the topPanel
      topPanel.addMouseListener(this);
      topPanel.addMouseMotionListener(this);
      topPanel.setFocusable(true);

      ImageIcon closeIcon = new ImageIcon(ResourceLoader.getInstance().getCloseIcon(UserSettings.getInstance().getCloseIcon()));
      closeButton = new JButton(closeIcon);
      closeButton.setBackground(COLOR_TRANSPARENT);
      closeButton.setRolloverEnabled(false);

      closeButton.setToolTipText(I18N.getInstance().getString("notewindow.hidebuttontooltip"));
      closeButton.addActionListener(this);
      closeButton.addMouseListener(this);
      closeButton.setFocusable(false);
      closeButton.setBorderPainted(false);
      closeButton.setHorizontalAlignment(SwingConstants.CENTER);
      closeButton.setPreferredSize(CLOSEBUTTON_SIZE);
      closeButton.setMargin(new Insets(3, 0, 0, 3));
      topPanel.add(closeButton, BorderLayout.EAST);

      setUndecorated(true);
      setLocation(parentNote.getXPos(), parentNote.getYPos());
      setSize(parentNote.getXSize(), parentNote.getYSize());

      // menu and doubleclick
      topPanel.addMouseListener(this);

      // resize listener
      resizing = false;
      resizeCursor = false;
      textArea.addMouseListener(this);
      textArea.addMouseMotionListener(this);

      setAlwaysOnTop(parentNote.isAlwaysOnTop());
      setContentPane(mainPanel);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      addWindowListener(this);
      textArea.setFocusable(false);

      bgLabel = new BackgroundLabel(this, parentNote.getBGColor());

      getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));

      textPanel.getVerticalScrollBar().setOpaque(false);

      //add keylisteners (for keyboard shortcuts)
      topPanel.addKeyListener(this);
      textArea.addKeyListener(this);

      updateCategory();
      setVisible(true);
      toBack();
      showScrollButtonIfNeeded();
      setTitle(I18N.getInstance().getString("note") + ": " + getShortText());
   }

   @Override
   public void focusGained(FocusEvent e) {
      if (e.getSource() == textArea) {
         showScrollBarIfNeeded();
      }
   }

   @Override
   public void focusLost(FocusEvent e) {
      if (e.getSource() == textArea) {
         if (!resizing) { //resizing would call showScrollBarIfNeeded() and thus revert the effect
            showScrollButtonIfNeeded();
         }
         setTitle("note: " + getShortText());
         parentNote.setText(textArea.getText());
         textArea.setFocusable(false);

         // write notes to file after every change
         NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());
      }
   }

   @Override
   public void windowActivated(WindowEvent arg0) {
      // do nothing
   }

   @Override
   public void windowClosed(WindowEvent arg0) {
      parentNote.hide();
   }

   @Override
   public void windowClosing(WindowEvent arg0) {
      // do nothing
   }

   @Override
   public void windowDeactivated(WindowEvent arg0) {
      // do nothing
   }

   @Override
   public void windowDeiconified(WindowEvent arg0) {
      // do nothing
   }

   @Override
   public void windowIconified(WindowEvent arg0) {
      // do nothing
   }

   @Override
   public void windowOpened(WindowEvent arg0) {
      // do nothing
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == closeButton) {
         parentNote.hide();

         // write notes to file after every change
         NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());
      }

   }

   @Override
   public void mouseClicked(MouseEvent e) {
      if (e.getSource() == topPanel || e.getSource() == catButton) {
         if (e.getClickCount() == 2) { // doubleclick on topPanel
            autoSizeY();
            // write notes to file after every change
            NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());
         } else {
            textArea.setFocusable(false);
         }
      } else if (e.getSource() == textArea) {
         textArea.setFocusable(true);
         textArea.requestFocus();
      }
   }

   private void autoSizeY() {
      int sizeX = getWidth();
      int sizeY = OFFSET;

      //get number of lines (incl. wrapped lines)
      int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();
      View view = textArea.getUI().getRootView(textArea).getView(0);
      int prefHeight = (int) view.getPreferredSpan(View.Y_AXIS);
      int lines = prefHeight / lineHeight;

      //calculate new height
      sizeY += lineHeight * lines;

      //apply new size
      setSize(sizeX, sizeY);
      parentNote.setSize((short) sizeX, (short) sizeY);
      scrollButton.setVisible(false);
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      // do nothing
   }

   @Override
   public void mouseExited(MouseEvent e) {
      // do nothing

   }

   @Override
   public void mousePressed(MouseEvent e) {
      Object src = e.getSource();
      if (src == topPanel || src == catButton) {
         checkPopupMenu(e);
         textArea.setFocusable(false);
         if (e.getButton() == MouseEvent.BUTTON1) {
            // Position on Panel
            dx = e.getXOnScreen() - getX();
            dy = e.getYOnScreen() - getY();
            dragging = true;
         }
      } else if (src == textArea) {
         if (e.getButton() == MouseEvent.BUTTON1 && resizeCursor) {
            dx = getX() + getWidth() - e.getXOnScreen();
            dy = getY() + getHeight() - e.getYOnScreen();
            resizing = true;
            textArea.setFocusable(false);
            showScrollBarIfNeeded();
         } else {
            textArea.setFocusable(true);
            textArea.requestFocus();
         }
      }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if (e.getSource() == topPanel || e.getSource() == catButton) {
         checkPopupMenu(e);
      }
      boolean changeMade = false;

      if (dragging && e.getButton() == MouseEvent.BUTTON1) {
         // stop moving and save position
         dragging = false;
         parentNote.setPosition((short) getX(), (short) getY());
         changeMade = true;
      } else if (resizing && e.getButton() == MouseEvent.BUTTON1) {
         // stop resizing and save size
         resizing = false;
         parentNote.setSize((short) getWidth(), (short) getHeight());
         showScrollButtonIfNeeded();
         changeMade = true;
      } else if (e.getSource() == closeButton) {
         // restore button background if not pressed
         repaint();
      } else if (e.getSource() == textArea) {
         textArea.setFocusable(true);
         textArea.requestFocus();
      }

      if (changeMade) {
         // write notes to file after every change
         NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());
      }
   }

   private void checkPopupMenu(MouseEvent event) {
      if (event.isPopupTrigger()) {
         RightClickMenu popup = new RightClickMenu(this);
         popup.show(event.getComponent(), event.getX(), event.getY());
      }
   }

   public Note getParentNote() {
      return parentNote;
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      if (dragging) {
         setLocation(e.getXOnScreen() - dx, e.getYOnScreen() - dy);
      } else if (resizing) {
         int sx = e.getXOnScreen() - getX() + dx;
         int sy = e.getYOnScreen() - getY() + dy;
         if (sx < MIN_WINDOW_WIDTH) {
            sx = MIN_WINDOW_WIDTH;
         }
         if (sy < MIN_WINDOW_HEIGHT) {
            sy = MIN_WINDOW_HEIGHT;
         }
         setSize(sx, sy);
      }
   }

   public void updateCategory() {
      Category cat = parentNote.getCategory();
      if (cat != null) {
         topPanel.setToolTipText(I18N.getInstance().getString("category") + ": " + cat.getName());
         if (catButton != null) {
            catButton.setText(cat.getName());
            repaint();
         }
         setBGColor(parentNote.getBGColor());
      }
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      if (!resizing) {
         // if in lower right corner, start resizing or change cursor
         if ((e.getSource() == textArea && (e.getX() >= textArea.getWidth() - RESIZE_AREA_SIZE && e.getY() >= textPanel.getHeight() - RESIZE_AREA_SIZE)) || (e.getSource() == scrollButton && (e.getX() >= scrollButton.getWidth() - RESIZE_AREA_SIZE))) { // height from panel because of vertical scrolling
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

   public void updateFontSize() {
      textArea.setFont(new java.awt.Font("SANSSERIF", 1, parentNote.getFontSize()));
   }

   public void jumpIntoTextArea() {
      toFront();
      requestFocus();
      textArea.setFocusable(true);
      textArea.requestFocusInWindow();
   }

   private void showScrollButtonIfNeeded() {
      textPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      if (textPanel.getHeight() != textArea.getHeight()) {
         scrollButton.setVisible(true);
      }
   }

   private void showScrollBarIfNeeded() {
      scrollButton.setVisible(false);
      textPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
   }

   public void setBGColor(byte c) {
      bgLabel.setMyColor(c);
      repaint();
   }

   private String getShortText() { //returns short text for the window titles
      int l = textArea.getText().length();
      String dots = "";
      if (l > SHORT_TEXT_LENGTH) {
         l = SHORT_TEXT_LENGTH;
         dots = "...";
      }
      String t = textArea.getText().substring(0, l);
      t = t.replace("\n", " ") + dots;
      return t;
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.isControlDown()) {
         switch (e.getKeyCode()) {
         case KeyEvent.VK_0:
         case KeyEvent.VK_1:
         case KeyEvent.VK_2:
         case KeyEvent.VK_3:
         case KeyEvent.VK_4:
         case KeyEvent.VK_5:
         case KeyEvent.VK_6:
         case KeyEvent.VK_7:
         case KeyEvent.VK_8:
         case KeyEvent.VK_9:
            int catNumber = Integer.parseInt(String.valueOf(e.getKeyChar()));
            if (catNumber == 0) {
               catNumber = 9;
            } else {
               catNumber--;
            }
            if (CategoryManager.getInstance().getCategoryByNumber(catNumber) != null) {
               parentNote.moveToCategory(CategoryManager.getInstance().getCategoryByNumber(catNumber));
            }
            break;
         default:
            break;
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      //do nothing
   }

   @Override
   public void keyTyped(KeyEvent e) {
      //do nothing
   }
}
