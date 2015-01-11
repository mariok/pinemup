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

public class NoteWindow extends JWindow implements NoteChangedEventListener, CategoryChangedEventListener {
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

   /** The note, which is represented by this NoteWindow. */
   private final Note parentNote;

   /** Some component references are stored here, so that the components can be updated later. */
   private final JPanel topPanel;
   private final JButton catButton;
   private final JScrollPane textPanel;
   private final JTextArea textArea;
   private final JButton scrollButton;
   private final BackgroundLabel bgLabel;

   /** state-information required to make window movable. */
   private boolean dragging;
   private int draggingOffsetX;
   private int draggingOffsetY;

   /** state-information required to make window resizable. */
   private boolean resizeCursor;
   private boolean resizing;

   /** state-information required for font size change via mousewheel. */
   private boolean controlPressed;

   NoteWindow(Frame owner, Note parentNote) {
      super(owner);
      this.parentNote = parentNote;

      //create components
      catButton = createCatButtonIfNecessary();
      topPanel = createTopPanel(catButton);
      textArea = createTextArea();
      textPanel = createTextPanel(textArea);
      scrollButton = createScrollButton();

      JButton closeButton = createCloseButton();
      topPanel.add(closeButton, BorderLayout.EAST);

      bgLabel = new BackgroundLabel();
      getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));

      JPanel mainPanel = createMainPanel();
      mainPanel.add(topPanel, BorderLayout.NORTH);
      mainPanel.add(textPanel, BorderLayout.CENTER);
      mainPanel.add(scrollButton, BorderLayout.SOUTH);
      setContentPane(mainPanel);

      // initialize component state with information from parent note
      initWindowAndComponentsFromParentNoteProperties();

      // now add behavior to components
      setupDoubleClickLogic(topPanel, catButton);
      setupRightClickMenu(topPanel, catButton);
      setupNoteMoveLogic(topPanel, catButton);
      setupTextAreaFocusLogic(topPanel, catButton, textArea);
      setupResizeLogic(textArea);
      setupCloseButtonLogic(closeButton);
      setupKeyboardShortCutLogic(textArea, topPanel);
      setupChangeFontSizeWithMouseWheelLogic(textArea, topPanel);
      setupWindowCloseLogic();

      // show the window
      setVisible(true);
      showScrollButtonIfNeeded();
   }

   private JButton createCatButtonIfNecessary() {
      JButton catButton = null;

      // create category-label, if the corresponding option is enabled
      if (UserSettings.getInstance().getShowCategory()) {
         catButton = new JButton();
         catButton.setRolloverEnabled(false);
         catButton.setEnabled(false);
         catButton.setFocusable(false);
         catButton.setPreferredSize(CATBUTTON_SIZE);
         catButton.setMargin(new Insets(0, 0, 0, 0));
         catButton.setBackground(COLOR_TRANSPARENT);
      }

      return catButton;
   }

   private JButton createCloseButton() {
      ImageIcon closeIcon = new ImageIcon(ResourceLoader.getInstance().getCloseIcon(UserSettings.getInstance().getCloseIcon()));

      JButton closeButton = new JButton(closeIcon);
      closeButton.setBackground(COLOR_TRANSPARENT);
      closeButton.setRolloverEnabled(false);
      closeButton.setToolTipText(I18N.getInstance().getString("notewindow.hidebuttontooltip"));
      closeButton.setFocusable(false);
      closeButton.setBorderPainted(false);
      closeButton.setHorizontalAlignment(SwingConstants.CENTER);
      closeButton.setPreferredSize(CLOSEBUTTON_SIZE);
      closeButton.setMargin(new Insets(3, 0, 0, 3));

      // add listener to fix problem with background-color after button has been pressed
      closeButton.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            // restore button background if not pressed
            repaint();
         }
      });

      return closeButton;
   }

   private JButton createScrollButton() {
      ImageIcon scrollImage = new ImageIcon(ResourceLoader.getInstance().getScrollImage());

      JButton scrollButton = new JButton(scrollImage);
      scrollButton.setRolloverEnabled(false);
      scrollButton.setEnabled(false);
      scrollButton.setFocusable(false);
      scrollButton.setMargin(new Insets(0, 0, 0, 0));
      scrollButton.setPreferredSize(SCROLLBUTTON_SIZE);
      scrollButton.setBorder(null);
      scrollButton.setBackground(COLOR_TRANSPARENT);
      scrollButton.setDisabledIcon(new ImageIcon(ResourceLoader.getInstance().getScrollImage()));

      // the scrollbutton will only be shown, if note is scrollable
      scrollButton.setVisible(false);

      return scrollButton;
   }

   private JScrollPane createTextPanel(JTextArea textArea) {
      JScrollPane textPanel = new JScrollPane();
      textPanel.setBorder(null);
      textPanel.setOpaque(false);
      textPanel.setViewportView(textArea);
      textPanel.getViewport().setOpaque(false);
      textPanel.getVerticalScrollBar().setOpaque(false);
      textPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

      return textPanel;
   }

   private JTextArea createTextArea() {
      JTextArea textArea = new JTextArea(parentNote.getText(), 1, 1);
      textArea.setEditable(true);
      textArea.setOpaque(false);
      textArea.setFocusable(false);
      textArea.setMargin(new Insets(0, 10, 3, 0));
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);

      return textArea;
   }

   private JPanel createMainPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setOpaque(false);

      return mainPanel;
   }

   private JPanel createTopPanel(JButton catButton) {
      JPanel tp = new JPanel(new BorderLayout());
      tp.setPreferredSize(new Dimension(UserSettings.getInstance().getDefaultWindowWidth(), TOPPANEL_HEIGHT));
      tp.setBorder(null);
      tp.setOpaque(false);
      tp.setFocusable(true);

      if (catButton != null) {
         JPanel catPanel = new JPanel(new FlowLayout());
         catPanel.add(catButton);
         catPanel.setOpaque(false);
         tp.add(catPanel, BorderLayout.CENTER);
      }

      return tp;
   }

   @Override
   public void setSize(int width, int height) {
      super.setSize(width, height);
      bgLabel.updateSize(width, height);
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

   private void checkPopupMenu(MouseEvent event) {
      if (event.isPopupTrigger()) {
         RightClickMenu popup = new RightClickMenu(parentNote);
         popup.show(event.getComponent(), event.getX(), event.getY());
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

   private void setFontSize(short fontSize) {
      textArea.setFont(new java.awt.Font("SANSSERIF", Font.BOLD, fontSize));
   }

   private void setBgColor(NoteColor c) {
      bgLabel.setMyColor(c);
      repaint();
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

   private void initWindowAndComponentsFromParentNoteProperties() {
      int width = parentNote.getXSize();
      int height = parentNote.getYSize();
      setSize(width, height);
      setLocation(parentNote.getXPos(), parentNote.getYPos());
      loadCategoryNameFromParentNote();
      updateWindowAndComponentsFromParentNoteProperties();
   }

   private void updateWindowAndComponentsFromParentNoteProperties() {
      setFontSize(parentNote.getFontSize());
      setBgColor(parentNote.getColor());
      setAlwaysOnTop(parentNote.isAlwaysOnTop());
   }

   private void setupWindowCloseLogic() {
      // for reacting on window-events
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosed(WindowEvent e) {
            getOwner().setVisible(false);
         }
      });
   }

   private void setupChangeFontSizeWithMouseWheelLogic(Component... components) {
      MouseWheelListener changeFontSizeViaMouseWheelListener = e -> {
         if (controlPressed) {
            int diff = -1 * e.getWheelRotation();
            parentNote.setFontSize((short)(parentNote.getFontSize() + diff));
         }
      };

      for(Component component : components) {
         component.addMouseWheelListener(changeFontSizeViaMouseWheelListener);
      }
   }

   private void setupKeyboardShortCutLogic(Component... components) {
      // for keyboard shortcuts
      KeyListener keyListener = new KeyAdapter() {
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
      };

      for(Component component : components) {
         component.addKeyListener(keyListener);
      }
   }

   private void setupCloseButtonLogic(JButton closeButton) {
      closeButton.addActionListener(e -> parentNote.setHidden(true));
   }

   private void setupResizeLogic(JTextArea textArea) {
      textArea.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && resizeCursor) {
               draggingOffsetX = getX() + getWidth() - e.getXOnScreen();
               draggingOffsetY = getY() + getHeight() - e.getYOnScreen();
               resizing = true;
               textArea.setFocusable(false);
               showScrollBarIfNeeded();
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            if (resizing && e.getButton() == MouseEvent.BUTTON1) {
               // stop resizing and save size
               resizing = false;
               parentNote.setSize((short)getWidth(), (short)getHeight());
               showScrollButtonIfNeeded();
            }
         }
      });

      textArea.addMouseMotionListener(new MouseMotionListener() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (resizing) {
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
               if (e.getX() >= textArea.getWidth() - RESIZE_AREA_SIZE && e.getY() >= textPanel.getHeight() - RESIZE_AREA_SIZE) {
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
      });
   }

   private void setupTextAreaFocusLogic(JPanel topPanel, JButton catButton, JTextArea textArea) {
      MouseListener releaseFocusFromTextAreaListener = new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
               textArea.setFocusable(false);
            }
         }

         @Override
         public void mousePressed(MouseEvent e) {
            textArea.setFocusable(false);
         }
      };
      topPanel.addMouseListener(releaseFocusFromTextAreaListener);
      if (catButton != null) {
         catButton.addMouseListener(releaseFocusFromTextAreaListener);
      }

      textArea.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            textArea.setFocusable(true);
            textArea.requestFocus();
         }

         @Override
         public void mousePressed(MouseEvent e) {
            if (!resizeCursor) {
               textArea.setFocusable(true);
               textArea.requestFocus();
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            if (!resizing) {
               textArea.setFocusable(true);
               textArea.requestFocus();
            }
         }
      });

      textArea.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent e) {
            showScrollBarIfNeeded();
         }

         @Override
         public void focusLost(FocusEvent e) {
            if (!resizing) {
               // resizing would call showScrollBarIfNeeded() and thus revert the
               // effect
               showScrollButtonIfNeeded();
            }
            parentNote.setText(textArea.getText());
            textArea.setFocusable(false);
         }
      });
   }

   private void setupNoteMoveLogic(Component... components) {
      for(Component component : components) {
         if (component != null) {
            component.addMouseListener(new MouseAdapter() {
               @Override
               public void mousePressed(MouseEvent e) {
                  if (e.getButton() == MouseEvent.BUTTON1) {
                     // determine position on panel
                     draggingOffsetX = e.getXOnScreen() - getX();
                     draggingOffsetY = e.getYOnScreen() - getY();
                     dragging = true;
                  }
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                  if (dragging && e.getButton() == MouseEvent.BUTTON1) {
                     // stop moving and save position
                     dragging = false;
                     parentNote.setPosition((short)getX(), (short)getY());
                  }
               }
            });

            component.addMouseMotionListener(new MouseMotionAdapter() {
               @Override
               public void mouseDragged(MouseEvent e) {
                  if (dragging) {
                     setLocation(e.getXOnScreen() - draggingOffsetX, e.getYOnScreen() - draggingOffsetY);
                  }
               }
            });
         }
      }
   }

   private void setupDoubleClickLogic(Component... components) {
      for(Component component : components) {
         if (component != null) {
            component.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 2) {
                     // doubleclick on topPanel
                     autoSizeY();
                  } else {
                     textArea.setFocusable(false);
                  }
               }

               @Override
               public void mousePressed(MouseEvent e) {
                  textArea.setFocusable(false);
               }
            });
         }
      }
   }

   private void setupRightClickMenu(Component... components) {
      for(Component component : components) {
         if (component != null) {
            component.addMouseListener(new MouseAdapter() {
               @Override
               public void mousePressed(MouseEvent e) {
                  checkPopupMenu(e);
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                  checkPopupMenu(e);
               }
            });
         }
      }
   }

   @Override
   public void noteChanged(NoteChangedEvent event) {
      LOG.debug("Received NoteChangedEvent.");
      updateWindowAndComponentsFromParentNoteProperties();
   }

   @Override
   public void categoryChanged(CategoryChangedEvent event) {
      LOG.debug("Received CategoryChangedEvent.");
      Category category = event.getSource();
      refreshCategoryName(category);
   }
}
