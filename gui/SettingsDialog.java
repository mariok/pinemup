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
import java.io.File;

import javax.swing.border.TitledBorder;
import javax.swing.event.*;

public class SettingsDialog extends JFrame implements ActionListener, DocumentListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JButton okButton, cancelButton, applyButton, browseButton;

   private JPanel guiPanel, buttonPanel, lsPanel, mainPanel, categoryPanel;

   private JTabbedPane tpane;

   private JTextField defaultWidthField, defaultHeightField, defaultXPositionField, defaultYPositionField, ftpServerField, ftpUserField, ftpDirField, cat1Field, cat2Field, cat3Field, cat4Field, cat5Field, notesFileField;

   private JPasswordField ftpPasswdField;

   private JLabel ftpServerLabel, ftpUserLabel, ftpPasswdLabel, ftpDirLabel;
   
   private JCheckBox alwaysOnTopBox;

   private JSpinner defaultFontSizeSpinner;
   
   public SettingsDialog() {
      super("Settings");

      // prepare panels
      mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());
      guiPanel = new JPanel();
      lsPanel = new JPanel(new BorderLayout());
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

      // notes default settings panel
      final int ROWS = 6;
      guiPanel.setLayout(new GridLayout(ROWS, 2));
      JPanel[] guiSubPanel = new JPanel[ROWS*2];
      for (int i=0;i<ROWS*2;i++) {
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
      guiSubPanel[8].add(new JLabel("Default Font Size:"));
      defaultFontSizeSpinner = new JSpinner(new SpinnerNumberModel(5, 5, 30, 1));
      guiSubPanel[9].add(defaultFontSizeSpinner);
      alwaysOnTopBox = new JCheckBox("Always On Top By Default");
      guiSubPanel[10].add(alwaysOnTopBox);

      // load/save settings panel
      JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      savePanel.setBorder(new TitledBorder("notes-file"));
      notesFileField = new JTextField(20);
      notesFileField.getDocument().addDocumentListener(this);
      savePanel.add(notesFileField);
      browseButton = new JButton("browse");
      browseButton.addActionListener(this);
      savePanel.add(browseButton);
      lsPanel.add(savePanel);
      JPanel[] ftpSubPanel = new JPanel[8];
      JPanel ftpPanel = new JPanel(new GridLayout(4,2));
      ftpPanel.setBorder(new TitledBorder("FTP settings"));
      lsPanel.add(ftpPanel,BorderLayout.SOUTH);
      for (int i=0; i<8; i++) {
         ftpSubPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
         ftpPanel.add(ftpSubPanel[i]);
      }
      ftpServerLabel = new JLabel("FTP-Server:");
      ftpSubPanel[0].add(ftpServerLabel);
      ftpServerField = new JTextField(20);
      ftpServerField.getDocument().addDocumentListener(this);
      ftpSubPanel[1].add(ftpServerField);
      ftpUserLabel = new JLabel("FTP-User:");
      ftpSubPanel[2].add(ftpUserLabel);
      ftpUserField = new JTextField(20);
      ftpUserField.getDocument().addDocumentListener(this);
      ftpSubPanel[3].add(ftpUserField);
      ftpPasswdLabel = new JLabel("FTP-Password:");
      ftpSubPanel[4].add(ftpPasswdLabel);
      ftpPasswdField = new JPasswordField(20);
      ftpPasswdField.getDocument().addDocumentListener(this);
      ftpSubPanel[5].add(ftpPasswdField);
      ftpDirLabel = new JLabel("FTP-Directory:");
      ftpSubPanel[6].add(ftpDirLabel);
      ftpDirField = new JTextField(20);
      ftpDirField.getDocument().addDocumentListener(this);
      ftpSubPanel[7].add(ftpDirField);

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
         saveSettings();
         setVisible(false);
         dispose();
      } else if (src == applyButton) {
         saveSettings();
         applyButton.setEnabled(false);
      } else if (src == cancelButton) {
         setVisible(false);
         dispose();
      } else if (src == browseButton) {
         File f = null;
         PinEmUp.getFileDialog().setDialogTitle("select notes file");
         PinEmUp.getFileDialog().setFileFilter(new MyFileFilter("DAT"));
         if (PinEmUp.getFileDialog().showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            f = PinEmUp.getFileDialog().getSelectedFile();
         }
         if (f != null) {
            notesFileField.setText(NoteIO.checkAndAddExtension(f.getAbsolutePath(),".dat"));
         }
      }
   }

   private void loadSettings() {
      // write settings from object into fields
      
      // default note settings panel
      defaultWidthField.setText(String.valueOf(PinEmUp.getUserSettings().getDefaultWindowWidth()));
      defaultHeightField.setText(String.valueOf(PinEmUp.getUserSettings().getDefaultWindowHeight()));
      defaultXPositionField.setText(String.valueOf(PinEmUp.getUserSettings().getDefaultWindowXPostition()));
      defaultYPositionField.setText(String.valueOf(PinEmUp.getUserSettings().getDefaultWindowYPostition()));
      defaultFontSizeSpinner.getModel().setValue(new Integer(PinEmUp.getUserSettings().getDefaultFontSize()));
      alwaysOnTopBox.setSelected(PinEmUp.getUserSettings().getDefaultAlwaysOnTop());
      
      // load/save panel
      notesFileField.setText(PinEmUp.getUserSettings().getNotesFile());
      ftpServerField.setText(PinEmUp.getUserSettings().getFtpServer());
      ftpUserField.setText(PinEmUp.getUserSettings().getFtpUser());
      if (PinEmUp.getUserSettings().getFtpPasswd() != null) {
         ftpPasswdField.setText(String.copyValueOf(PinEmUp.getUserSettings()
               .getFtpPasswd()));
      } else {
         ftpPasswdField.setText("");
      }
      ftpDirField.setText(PinEmUp.getUserSettings().getFtpDir());
      
      // category panel
      cat1Field.setText(PinEmUp.getUserSettings().getCategoryNames()[0]);
      cat2Field.setText(PinEmUp.getUserSettings().getCategoryNames()[1]);
      cat3Field.setText(PinEmUp.getUserSettings().getCategoryNames()[2]);
      cat4Field.setText(PinEmUp.getUserSettings().getCategoryNames()[3]);
      cat5Field.setText(PinEmUp.getUserSettings().getCategoryNames()[4]);
   }

   private void saveSettings() {
      // load settings from fields
      short defaultWidth = Short.parseShort(defaultWidthField.getText());
      short defaultHeight = Short.parseShort(defaultHeightField.getText());
      short defaultXPosition = Short.parseShort(defaultXPositionField.getText());
      short defaultYPosition = Short.parseShort(defaultYPositionField.getText());
      short defaultFontSize = ((Integer)((SpinnerNumberModel)defaultFontSizeSpinner.getModel()).getNumber()).shortValue();
      boolean defaultAlwaysOnTop = alwaysOnTopBox.isSelected();
      String notesFile = notesFileField.getText();
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
      PinEmUp.getUserSettings().setDefaultWindowHeight(defaultHeight);
      PinEmUp.getUserSettings().setDefaultWindowWidth(defaultWidth);
      PinEmUp.getUserSettings().setDefaultWindowXPosition(defaultXPosition);
      PinEmUp.getUserSettings().setDefaultWindowYPosition(defaultYPosition);
      PinEmUp.getUserSettings().setDefaultFontSize(defaultFontSize);
      PinEmUp.getUserSettings().setDefaultAlwaysOnTop(defaultAlwaysOnTop);
      PinEmUp.getUserSettings().setNotesFile(notesFile);
      PinEmUp.getUserSettings().setFtpServer(ftpServer);
      PinEmUp.getUserSettings().setFtpUser(ftpUser);
      PinEmUp.getUserSettings().setFtpPasswd(ftpPasswd);
      PinEmUp.getUserSettings().setFtpDir(ftpDir);
      PinEmUp.getUserSettings().setCategoryName((byte)0, cat1);
      PinEmUp.getUserSettings().setCategoryName((byte)1, cat2);
      PinEmUp.getUserSettings().setCategoryName((byte)2, cat3);
      PinEmUp.getUserSettings().setCategoryName((byte)3, cat4);
      PinEmUp.getUserSettings().setCategoryName((byte)4, cat5);
      // update all tooltips with new category names
      Note n = PinEmUp.getMainApp().getNotes();
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
      PinEmUp.getMainApp().getTrayMenu().updateCategories();
      
      // load new notes from file
      if (PinEmUp.getMainApp().getNotes() != null) {
         PinEmUp.getMainApp().getNotes().tempHideAll();
      }
      PinEmUp.getMainApp().setNotes(NoteIO.readNotesFromFile(notesFile));
      
      // show all visible notes
      if (PinEmUp.getMainApp().getNotes() != null) {
         PinEmUp.getMainApp().getNotes().showAllVisible();
      }
      
      // save settings permanentely
      PinEmUp.getUserSettings().saveSettings();
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
