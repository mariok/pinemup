/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
 *
 *
 * pin 'em up is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * pin 'em up is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with pin 'em up; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package gui;

import logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class SettingsDialog extends JFrame implements ActionListener, DocumentListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JButton okButton, cancelButton, applyButton;

   private JPanel guiPanel, buttonPanel, lsPanel, mainPanel, categoryPanel;

   private JTabbedPane tpane;

   private JTextField defaultWidthField, defaultHeightField, defaultXPositionField, defaultYPositionField, ftpServerField, ftpUserField, ftpDirField, cat1Field, cat2Field, cat3Field, cat4Field, cat5Field;

   private JPasswordField ftpPasswdField;

   private JLabel ftpServerLabel, ftpUserLabel, ftpPasswdLabel, ftpDirLabel;

   public SettingsDialog() {
      super("Settings");

      // prepare panels
      mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());
      guiPanel = new JPanel();
      lsPanel = new JPanel();
      categoryPanel = new JPanel();
      tpane = new JTabbedPane();
      tpane.addTab("GUI", null, guiPanel, "GUI Settings");
      tpane.addTab("Load/Save", null, lsPanel, "Load / Save Settings");
      tpane.addTab("Categories", null, categoryPanel, "Names of the categories");
      mainPanel.add(tpane, BorderLayout.CENTER);

      // panel with buttons
      okButton = new JButton("OK");
      okButton.addActionListener(this);
      cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(this);
      applyButton = new JButton("Apply");
      applyButton.addActionListener(this);
      okButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      applyButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      okButton.setEnabled(true);
      cancelButton.setEnabled(true);
      buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);
      buttonPanel.add(applyButton);
      mainPanel.add(buttonPanel, BorderLayout.SOUTH);

      // GUI settings panel
      guiPanel.setLayout(new GridLayout(4, 2));
      JPanel[] guiSubPanel = new JPanel[8];
      for (int i=0;i<8;i++) {
         guiSubPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
         guiPanel.add(guiSubPanel[i]);
      }
      guiSubPanel[0].add(new JLabel("Default Note Width:"));
      defaultWidthField = new JTextField(4);
      defaultWidthField.getDocument().addDocumentListener(this);
      defaultWidthField.setMaximumSize(new Dimension(50, 20));
      guiSubPanel[1].add(defaultWidthField);
      guiSubPanel[2].add(new JLabel("Default Note Height:"));
      defaultHeightField = new JTextField(4);
      defaultHeightField.getDocument().addDocumentListener(this);
      guiSubPanel[3].add(defaultHeightField);
      guiSubPanel[4].add(new JLabel("Default Note X Position:"));
      defaultXPositionField = new JTextField(4);
      defaultXPositionField.getDocument().addDocumentListener(this);
      guiSubPanel[5].add(defaultXPositionField);
      guiSubPanel[6].add(new JLabel("Default Note Y Position:"));
      defaultYPositionField = new JTextField(4);
      defaultYPositionField.getDocument().addDocumentListener(this);
      guiSubPanel[7].add(defaultYPositionField);

      // load/save settings panel
      lsPanel.setLayout(new GridLayout(8, 2)); // Save-Method-Panel
      JPanel[] lsSubPanel = new JPanel[16];
      for (int i=0; i<16; i++) {
         lsSubPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
         lsPanel.add(lsSubPanel[i]);
      }
      lsSubPanel[0].add(new JLabel("FTP-Settings"));
      ftpServerLabel = new JLabel("FTP-Server:");
      lsSubPanel[2].add(ftpServerLabel);
      ftpServerField = new JTextField(20);
      ftpServerField.getDocument().addDocumentListener(this);
      lsSubPanel[3].add(ftpServerField);
      ftpUserLabel = new JLabel("FTP-User:");
      lsSubPanel[4].add(ftpUserLabel);
      ftpUserField = new JTextField(20);
      ftpUserField.getDocument().addDocumentListener(this);
      lsSubPanel[5].add(ftpUserField);
      ftpPasswdLabel = new JLabel("FTP-Password:");
      lsSubPanel[6].add(ftpPasswdLabel);
      ftpPasswdField = new JPasswordField(20);
      ftpPasswdField.getDocument().addDocumentListener(this);
      lsSubPanel[7].add(ftpPasswdField);
      ftpDirLabel = new JLabel("FTP-Directory:");
      lsSubPanel[8].add(ftpDirLabel);
      ftpDirField = new JTextField(20);
      ftpDirField.getDocument().addDocumentListener(this);
      lsSubPanel[9].add(ftpDirField);

      // category settings panel
      categoryPanel.setLayout(new GridLayout(6, 2));
      JPanel[] categorySubPanel = new JPanel[12];
      for (int i=0; i<12; i++) {
         categorySubPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
         categoryPanel.add(categorySubPanel[i]);
      }
      categorySubPanel[0].add(new JLabel("Category names"));
      categorySubPanel[2].add(new JLabel("Category 1:"));
      cat1Field = new JTextField(20);
      cat1Field.getDocument().addDocumentListener(this);
      categorySubPanel[3].add(cat1Field);
      categorySubPanel[4].add(new JLabel("Category 2:"));
      cat2Field = new JTextField(20);
      cat2Field.getDocument().addDocumentListener(this);
      categorySubPanel[5].add(cat2Field);
      categorySubPanel[6].add(new JLabel("Category 3:"));
      cat3Field = new JTextField(20);
      cat3Field.getDocument().addDocumentListener(this);
      categorySubPanel[7].add(cat3Field);
      categorySubPanel[8].add(new JLabel("Category 4:"));
      cat4Field = new JTextField(20);
      cat4Field.getDocument().addDocumentListener(this);
      categorySubPanel[9].add(cat4Field);
      categorySubPanel[10].add(new JLabel("Category 5:"));
      cat5Field = new JTextField(20);
      cat5Field.getDocument().addDocumentListener(this);
      categorySubPanel[11].add(cat5Field);
      
      // Load Settings Into Fields
      loadSettings();

      applyButton.setEnabled(false);
      setContentPane(mainPanel);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      pack();
      setVisible(true);
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();

      if (src == okButton) {
         saveSettings();
         setVisible(false);
         dispose();
      } else if (src == applyButton) {
         saveSettings();
         applyButton.setEnabled(false);
      } else if (src == cancelButton) {
         setVisible(false);
         dispose();
      }

   }

   private void loadSettings() {
      // write settings from object into fields
      
      // GUI panel
      defaultWidthField.setText(String.valueOf(MainApp.getUserSettings().getDefaultWindowWidth()));
      defaultHeightField.setText(String.valueOf(MainApp.getUserSettings().getDefaultWindowHeight()));
      defaultXPositionField.setText(String.valueOf(MainApp.getUserSettings().getDefaultWindowXPostition()));
      defaultYPositionField.setText(String.valueOf(MainApp.getUserSettings().getDefaultWindowYPostition()));
      
      // load/save panel
      ftpServerField.setText(MainApp.getUserSettings().getFtpServer());
      ftpUserField.setText(MainApp.getUserSettings().getFtpUser());
      if (MainApp.getUserSettings().getFtpPasswd() != null) {
         ftpPasswdField.setText(String.copyValueOf(MainApp.getUserSettings()
               .getFtpPasswd()));
      } else {
         ftpPasswdField.setText("");
      }
      ftpDirField.setText(MainApp.getUserSettings().getFtpDir());
      
      // category panel
      cat1Field.setText(MainApp.getUserSettings().getCategoryNames()[0]);
      cat2Field.setText(MainApp.getUserSettings().getCategoryNames()[1]);
      cat3Field.setText(MainApp.getUserSettings().getCategoryNames()[2]);
      cat4Field.setText(MainApp.getUserSettings().getCategoryNames()[3]);
      cat5Field.setText(MainApp.getUserSettings().getCategoryNames()[4]);
   }

   private void saveSettings() {
      // load settings from fields
      short defaultWidth = Short.parseShort(defaultWidthField.getText());
      short defaultHeight = Short.parseShort(defaultHeightField.getText());
      short defaultXPosition = Short.parseShort(defaultXPositionField.getText());
      short defaultYPosition = Short.parseShort(defaultYPositionField.getText());
      String ftpServer = ftpServerField.getText();
      String ftpUser = ftpUserField.getText();
      char[] ftpPasswd = ftpPasswdField.getPassword();
      String ftpDir = ftpDirField.getText();
      String cat1 = cat1Field.getText();
      String cat2 = cat2Field.getText();
      String cat3 = cat3Field.getText();
      String cat4 = cat4Field.getText();
      String cat5 = cat5Field.getText();
      
      // write settings into object
      MainApp.getUserSettings().setDefaultWindowHeight(defaultHeight);
      MainApp.getUserSettings().setDefaultWindowWidth(defaultWidth);
      MainApp.getUserSettings().setDefaultWindowXPosition(defaultXPosition);
      MainApp.getUserSettings().setDefaultWindowYPosition(defaultYPosition);
      MainApp.getUserSettings().setFtpServer(ftpServer);
      MainApp.getUserSettings().setFtpUser(ftpUser);
      MainApp.getUserSettings().setFtpPasswd(ftpPasswd);
      MainApp.getUserSettings().setFtpDir(ftpDir);
      MainApp.getUserSettings().setCategoryName((byte)0, cat1);
      MainApp.getUserSettings().setCategoryName((byte)1, cat2);
      MainApp.getUserSettings().setCategoryName((byte)2, cat3);
      MainApp.getUserSettings().setCategoryName((byte)3, cat4);
      MainApp.getUserSettings().setCategoryName((byte)4, cat5);
      // update all tooltips with new category names
      Note n = MainApp.getMainApp().getNotes();
      while (n!=null && n.getPrev() != null) { // Back to the Beginning
         n = n.getPrev();
      }
      while (n != null) {
         if (n.getWindow() != null) {
            n.getWindow().updateToolTip();
         }
         n = n.getNext();
      }
      // update categories in traymenu
      MainApp.getMainApp().getTrayMenu().updateCategories();
      
      // save object to disk
      UserSettings.saveSettings(MainApp.getUserSettings(), "config.dat");
   }

   public void changedUpdate(DocumentEvent arg0) {
      applyButton.setEnabled(true);
   }

   public void insertUpdate(DocumentEvent arg0) {
      applyButton.setEnabled(true);      
   }

   public void removeUpdate(DocumentEvent arg0) {
      applyButton.setEnabled(true);  
   }
}
