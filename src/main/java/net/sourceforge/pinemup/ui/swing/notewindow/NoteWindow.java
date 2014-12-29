/*
 * pin 'em up
 *
 * Copyright (C) 2007-2013 by Mario Ködding
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

package net.sourceforge.pinemup.ui.swing.notewindow;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.resources.ResourceLoader;
import net.sourceforge.pinemup.core.model.*;
import net.sourceforge.pinemup.core.settings.UserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.*;

public class NoteWindow extends JWindow implements FocusListener, WindowListener, ActionListener, MouseListener, MouseMotionListener,
      MouseWheelListener, KeyListener, NoteChangedEventListener, CategoryChangedEventListener {
   private static final Logger LOG = LoggerFactory.getLogger(NoteWindow.class);

   private static final long serialVersionUID = -5228524832353948701L;

   private static final int MIN_WINDOW_HEIGHT = 40;
   private static final int MIN_WINDOW_WIDTH = 30;

   private static final int RESIZE_AREA_SIZE = 10;

   private static final int OFFSET_Y_FOR_AUTOSIZING = 35;

   private static final Dimension SCROLLBUTTON_SIZE = new Dimension(10, 5);
   private static final Color COLOR_TRANSPARENT = new Color(255, 255, 255, 0);
   private static final Dimension CLOSEBUTTON_SIZE = new Dimension(20, 20);
   private static final Dimension CATBUTTON_SIZE = new Dimension(100, 15);
   private static final int TOPPANEL_HEIGHT = 26;

   private final JScrollPane textPanel;

   private final JPanel topPanel;

   private final JTextArea textArea;

   private final Note parentNote;

   private final JButton closeButton;
   private JButton catButton;
   private final JButton scrollButton;

   private int draggingOffsetX, draggingOffsetY;

   /** required to make window movable. */
   private boolean dragging;

   /** required to make window resizable. */
   private boolean resizeCursor, resizing;

   /** required for font size change via mousewheel. */
   private boolean controlPressed;

   private final BackgroundLabel bgLabel;

   NoteWindow(Note pn) {
      super(new DummyFrame());
      parentNote = pn;
      textPanel = new JScrollPane();
      textPanel.setBorder(null);
      textPanel.setOpaque(false);
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setOpaque(false);
      topPanel = new JPanel(new BorderLayout());
      topPanel.setPreferredSize(new Dimension(UserSettings.getInstance().getDefaultWindowWidth(), TOPPANEL_HEIGHT));
      topPanel.setBorder(null);
      topPanel.setOpaque(false);
      mainPanel.add(textPanel, BorderLayout.CENTER);
      mainPanel.add(topPanel, BorderLayout.NORTH);
      dragging = false;
      catButton = null;

      // create category-label, if option is enabled
      if (UserSettings.getInstance().getShowCategory()) {
         catButton = new JButton();
         catButton.setRolloverEnabled(false);
         catButton.setEnabled(false);
         catButton.setFocusable(false);
         catButton.setPreferredSize(CATBUTTON_SIZE);
         catButton.setMargin(new Insets(0, 0, 0, 0));
         catButton.setBackground(COLOR_TRANSPARENT);

         JPanel catPanel = new JPanel(new FlowLayout());
         catPanel.add(catButton);
         catPanel.setOpaque(false);
         catButton.addMouseListener(this);
         catButton.addMouseMotionListener(this);
         topPanel.add(catPanel, BorderLayout.CENTER);
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

      // add area to show if note is scrollable
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

      int width = parentNote.getXSize();
      int height = parentNote.getYSize();
      bgLabel = new BackgroundLabel(parentNote.getColor(), width, height);
      getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));

      setSize(width, height);
      setLocation(parentNote.getXPos(), parentNote.getYPos());

      // menu and doubleclick
      topPanel.addMouseListener(this);

      // resize listener
      resizing = false;
      resizeCursor = false;
      textArea.addMouseListener(this);
      textArea.addMouseMotionListener(this);

      // change text size via mousewheel
      textArea.addMouseWheelListener(this);
      topPanel.addMouseWheelListener(this);

      setAlwaysOnTop(parentNote.isAlwaysOnTop());
      setContentPane(mainPanel);
      addWindowListener(this);
      textArea.setFocusable(false);

      textPanel.getVerticalScrollBar().setOpaque(false);

      // add keylisteners (for keyboard shortcuts)
      topPanel.addKeyListener(this);
      textArea.addKeyListener(this);

      loadCategoryNameFromParentNote();
      setVisible(true);
      showScrollButtonIfNeeded();
   }

   @Override
   public void setSize(int width, int height) {
      super.setSize(width, height);
      bgLabel.updateSize(width, height);
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
         if (!resizing) {
            // resizing would call showScrollBarIfNeeded() and thus revert the
            // effect
            showScrollButtonIfNeeded();
         }
         parentNote.setText(textArea.getText());
         textArea.setFocusable(false);
      }
   }

   @Override
   public void windowActivated(WindowEvent arg0) {
      // do nothing
   }

   @Override
   public void windowClosed(WindowEvent arg0) {
      getOwner().setVisible(false);
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
         parentNote.setHidden(true);
      }
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      if (e.getSource() == topPanel || e.getSource() == catButton) {
         if (e.getClickCount() == 2) {
            // doubleclick on topPanel
            autoSizeY();
         } else {
            textArea.setFocusable(false);
         }
      } else if (e.getSource() == textArea) {
         textArea.setFocusable(true);
         textArea.requestFocus();
      }
   }

   private void autoSizeY() {
      View view = textArea.getUI().getRootView(textArea).getView(0);
      int prefHeight = (int)view.getPreferredSpan(View.Y_AXIS);

      int currentWidth = getWidth();
      int autoHeight = prefHeight + OFFSET_Y_FOR_AUTOSIZING;

      // apply new size
      setSize(currentWidth, autoHeight);
      parentNote.setSize((short)currentWidth, (short)autoHeight);

      // hide scroll button, as all content is visible now
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
            // determine position on panel
            draggingOffsetX = e.getXOnScreen() - getX();
            draggingOffsetY = e.getYOnScreen() - getY();
            dragging = true;
         }
      } else if (src == textArea) {
         if (e.getButton() == MouseEvent.BUTTON1 && resizeCursor) {
            draggingOffsetX = getX() + getWidth() - e.getXOnScreen();
            draggingOffsetY = getY() + getHeight() - e.getYOnScreen();
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

      if (dragging && e.getButton() == MouseEvent.BUTTON1) {
         // stop moving and save position
         dragging = false;
         parentNote.setPosition((short)getX(), (short)getY());
      } else if (resizing && e.getButton() == MouseEvent.BUTTON1) {
         // stop resizing and save size
         resizing = false;
         parentNote.setSize((short)getWidth(), (short)getHeight());
         showScrollButtonIfNeeded();
      } else if (e.getSource() == closeButton) {
         // restore button background if not pressed
         repaint();
      } else if (e.getSource() == textArea) {
         textArea.setFocusable(true);
         textArea.requestFocus();
      }
   }

   private void checkPopupMenu(MouseEvent event) {
      if (event.isPopupTrigger()) {
         RightClickMenu popup = new RightClickMenu(parentNote);
         popup.show(event.getComponent(), event.getX(), event.getY());
      }
   }

   public Note getParentNote() {
      return parentNote;
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      if (dragging) {
         setLocation(e.getXOnScreen() - draggingOffsetX, e.getYOnScreen() - draggingOffsetY);
      } else if (resizing) {
         int sx = e.getXOnScreen() - getX() + draggingOffsetX;
         int sy = e.getYOnScreen() - getY() + draggingOffsetY;
         if (sx < MIN_WINDOW_WIDTH) {
            sx = MIN_WINDOW_WIDTH;
         }
         if (sy < MIN_WINDOW_HEIGHT) {
            sy = MIN_WINDOW_HEIGHT;
         }
         setSize(sx, sy);
      }
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      if (!resizing) {
         // if in lower right corner, start resizing or change cursor
         if ((e.getSource() == textArea && (e.getX() >= textArea.getWidth() - RESIZE_AREA_SIZE && e.getY() >= textPanel.getHeight()
               - RESIZE_AREA_SIZE))
               || (e.getSource() == scrollButton && (e.getX() >= scrollButton.getWidth() - RESIZE_AREA_SIZE))) {
            // height from panel because of vertical scrolling
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

   private void loadCategoryNameFromParentNote() {
      Category cat = CategoryManager.getInstance().findCategoryForNote(parentNote);
      if (cat != null) {
         refreshCategoryName(cat);
      }
   }

   private void refreshCategoryName(Category category) {
      topPanel.setToolTipText(I18N.getInstance().getString("category") + ": " + category.getName());
      if (catButton != null) {
         catButton.setText(category.getName());
         repaint();
      }
   }

   private void updateFontSize() {
      textArea.setFont(new java.awt.Font("SANSSERIF", Font.BOLD, parentNote.getFontSize()));
   }

   private void updateAlwaysOnTopState() {
      setAlwaysOnTop(parentNote.isAlwaysOnTop());
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

   public void setBGColor(NoteColor c) {
      bgLabel.setMyColor(c);
      repaint();
   }

   @Override
   public void mouseWheelMoved(MouseWheelEvent e) {
      if (controlPressed) {
         int diff = -1 * e.getWheelRotation();
         parentNote.setFontSize((short)(parentNote.getFontSize() + diff));
      }
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.isControlDown()) {
         controlPressed = true;

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
            int catNumber = e.getKeyCode() - KeyEvent.VK_0;
            if (catNumber == 0) {
               catNumber = 9;
            } else {
               catNumber--;
            }
            CategoryManager.getInstance().moveNoteToCategory(parentNote, catNumber);
            break;
         case KeyEvent.VK_MINUS:
            parentNote.setFontSize((short)(parentNote.getFontSize() - 1));
            break;
         case KeyEvent.VK_PLUS:
            parentNote.setFontSize((short)(parentNote.getFontSize() + 1));
            break;
         default:
            break;
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      if (!e.isControlDown()) {
         controlPressed = false;
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {
      // do nothing
   }

   @Override
   public void noteChanged(NoteChangedEvent event) {
      LOG.debug("Received NoteChangedEvent.");
      setBGColor(parentNote.getColor());
      updateFontSize();
      updateAlwaysOnTopState();
   }

   @Override
   public void categoryChanged(CategoryChangedEvent event) {
      LOG.debug("Received CategoryChangedEvent.");
      Category category = event.getSource();
      refreshCategoryName(category);
   }
}
