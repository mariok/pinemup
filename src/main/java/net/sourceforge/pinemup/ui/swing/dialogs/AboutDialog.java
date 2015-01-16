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

import net.sourceforge.pinemup.core.i18n.I18N;
import net.sourceforge.pinemup.core.io.resources.ResourceLoader;
import net.sourceforge.pinemup.ui.swing.utils.SwingUtils;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Calendar;

public class AboutDialog extends JFrame {
   private static final long serialVersionUID = -6786897911342420374L;

   private static final int DIALOG_WIDTH = 600;
   private static final int DIALOG_HEIGHT = 350;

   public AboutDialog() {
      super(I18N.getInstance().getString("aboutdialog.title"));

      JTabbedPane tabbedPane = new JTabbedPane();
      tabbedPane.addTab(I18N.getInstance().getString("aboutdialog.abouttab"), null, makeAboutTab(),
            I18N.getInstance().getString("aboutdialog.abouttab"));
      tabbedPane.addTab(I18N.getInstance().getString("aboutdialog.authorstab"), null, makeAuthorsTab(),
            I18N.getInstance().getString("aboutdialog.authorstab"));
      tabbedPane.addTab(I18N.getInstance().getString("aboutdialog.licensetab"), null, makeLicenseTab(),
            I18N.getInstance().getString("aboutdialog.licensetab"));

      JButton okButton = new JButton(I18N.getInstance().getString("closebutton"));
      okButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

      JPanel buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      buttonPanel.add(okButton);

      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(tabbedPane, BorderLayout.CENTER);
      mainPanel.add(buttonPanel, BorderLayout.SOUTH);
      setContentPane(mainPanel);

      setupOkButtonLogic(okButton);

      setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      SwingUtils.centerWindowOnScreen(this);
      setVisible(true);
   }

   private JScrollPane makeAboutTab() {
      String msg = ResourceLoader.getInstance().getAboutPage();
      return makeTextPanel(msg, "text/html");
   }

   private JScrollPane makeAuthorsTab() {
      String msg = ResourceLoader.getInstance().getAuthorsPage();
      return makeTextPanel(msg, "text/html");
   }

   private JScrollPane makeLicenseTab() {
      String msg = "(C) 2007 - " + Calendar.getInstance().get(Calendar.YEAR) + " Mario Ködding\r\n\r\n";
      msg += "This program is licensed under the terms of the GNU GPL V3 or any later version.\r\n\r\n\r\n";
      msg += ResourceLoader.getInstance().getLicense();
      return makeTextPanel(msg, "text/plain");
   }

   private JScrollPane makeTextPanel(String text, String contentType) {
      JEditorPane p = new JEditorPane(contentType, text);
      p.addHyperlinkListener(new DefaultHyperLinkListener());
      p.setEditable(false);
      JScrollPane myScrollPane = new JScrollPane(p);

      // scroll back to the top
      p.setCaretPosition(0);

      return myScrollPane;
   }

   private void setupOkButtonLogic(JButton okButton) {
      okButton.addActionListener(e -> {
         setVisible(false);
         dispose();
      });
   }
}
