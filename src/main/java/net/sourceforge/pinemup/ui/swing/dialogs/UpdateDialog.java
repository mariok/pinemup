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

package net.sourceforge.pinemup.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.ui.swing.utils.SwingUtils;

public class UpdateDialog extends JFrame implements ActionListener {
   private static final long serialVersionUID = 1L;

   private static final int DIALOG_WIDTH = 400;
   private static final int DIALOG_HEIGHT = 350;

   private JButton closeButton;

   public UpdateDialog(String updateText) {
      super(I18N.getInstance().getString("updatedialog.title"));
      setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

      // PREPARE ALL PANELS
      // ---------------------
      JPanel mainPanel = new JPanel(new BorderLayout());
      JEditorPane p = new JEditorPane("text/html", updateText);
      p.setEditable(false);
      p.addHyperlinkListener(new DefaultHyperLinkListener());
      JScrollPane myScrollPane = new JScrollPane(p);
      // scroll back to the top
      p.setCaretPosition(0);

      mainPanel.add(myScrollPane, BorderLayout.CENTER);

      // PANEL WITH BUTTONS
      closeButton = new JButton(I18N.getInstance().getString("closebutton"));
      closeButton.addActionListener(this);
      closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      closeButton.setEnabled(true);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      buttonPanel.add(closeButton);
      mainPanel.add(buttonPanel, BorderLayout.SOUTH);

      setContentPane(mainPanel);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      SwingUtils.centerWindowOnScreen(this);

      setVisible(true);
      toFront();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == closeButton) {
         setVisible(false);
         dispose();
      }
   }
}
