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

package net.sourceforge.pinemup.gui;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import net.sourceforge.pinemup.logic.*;

public class SettingsDialog extends JFrame implements ActionListener, DocumentListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JButton okButton, cancelButton, applyButton, browseButton;

   private JTextField defaultWidthField, defaultHeightField, defaultXPositionField, defaultYPositionField, ftpServerField, ftpUserField, ftpDirField, cat1Field, cat2Field, cat3Field, cat4Field, cat5Field, notesFileField;

   private JPasswordField ftpPasswdField;

   private JCheckBox alwaysOnTopBox, showCatBox;

   private JSpinner defaultFontSizeSpinner;
   
   private JRadioButton closeIcon1Button, closeIcon2Button;
   
   private ButtonGroup closeIconGroup;
   
   private JPanel makeLookAndFeelTab() {
      JPanel lookAndFeelPanel = new JPanel();
      // TAB WITH LOOK AND FEEL SETTINGS
      GridBagLayout gbl = new GridBagLayout();
      lookAndFeelPanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;

      //Add Panel for size and Position
      JPanel titleBarPanel = makeTitleBarPanel();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(titleBarPanel, gbc);
      lookAndFeelPanel.add(titleBarPanel);
      
      //Add empty panel to tab
      JPanel emptyPanel = new JPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(emptyPanel, gbc);
      lookAndFeelPanel.add(emptyPanel); 
      
      return lookAndFeelPanel;
   }
   
   private JPanel makeDefaultsTab() {
      // TAB WITH DEFAULT SETTINGS
      JPanel defaultsPanel = new JPanel();
      GridBagLayout gbl = new GridBagLayout();
      defaultsPanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;

      //Add Panel for size and Position
      JPanel sizePosPanel = makeSizePosPanel();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(sizePosPanel, gbc);
      defaultsPanel.add(sizePosPanel);
      
      //Add Panel for Font
      JPanel fontPanel = makeFontPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(fontPanel, gbc);
      defaultsPanel.add(fontPanel);
      
      //Add Panel for Visibility Settings
      JPanel visibilityPanel = makeVisibilityPanel();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(visibilityPanel, gbc);
      defaultsPanel.add(visibilityPanel);
      
      //Add empty panel to tab
      JPanel emptyPanel = new JPanel();
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(emptyPanel, gbc);
      defaultsPanel.add(emptyPanel); 
      
      return defaultsPanel;
   }
   
   private JPanel makeLoadSaveTab() {
      JPanel loadSavePanel = new JPanel();
      
      GridBagLayout gbl = new GridBagLayout();
      loadSavePanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;

      //Add Panel for notes file settings
      JPanel notesFilePanel = makeNotesFilePanel();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(notesFilePanel, gbc);
      loadSavePanel.add(notesFilePanel);

      //Add Panel for FTP settings
      JPanel ftpPanel = makeFtpPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(ftpPanel, gbc);
      loadSavePanel.add(ftpPanel);
      
      //Add empty panel to tab
      JPanel emptyPanel = new JPanel();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(emptyPanel, gbc);
      loadSavePanel.add(emptyPanel); 
      
      return loadSavePanel;
   }
   
   private JPanel makeCategoryTab() {
      JPanel categoryPanel = new JPanel();
      GridBagLayout gbl = new GridBagLayout();
      categoryPanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;

      //Add Panel for category names
      JPanel catNamesPanel = makeCatNamesPanel();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(catNamesPanel, gbc);
      categoryPanel.add(catNamesPanel);
      
      //Add empty panel to tab
      JPanel emptyPanel = new JPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(emptyPanel, gbc);
      categoryPanel.add(emptyPanel); 
      
      return categoryPanel;
   }
      
   
   private JPanel makeTitleBarPanel() {
      // PANEL FOR SIZE AND POSITIONS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.WEST;
      JPanel titleBarPanel = new JPanel(gbl);
      titleBarPanel.setBorder(new TitledBorder("Titlebar"));
      //Add all Labels
      JLabel closeIconLabel = new JLabel("Close-Icon: ");
      JLabel showCatLabel = new JLabel("Show category in titlebar: ");
      JLabel emptyLabel = new JLabel(" ");
      JLabel emptyLabel2 = new JLabel(" ");
      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      
      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(closeIconLabel, gbc);
      titleBarPanel.add(closeIconLabel);
      gbc.weightx = 100;
      gbc.gridx = 3;
      gbc.gridy = 0;
      gbl.setConstraints(emptyLabel, gbc);
      titleBarPanel.add(emptyLabel);
      gbc.gridx = 3;
      gbc.gridy = 1;
      gbl.setConstraints(emptyLabel, gbc);
      titleBarPanel.add(emptyLabel);
      gbc.weightx = 0;
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbl.setConstraints(emptyLabel2, gbc);
      titleBarPanel.add(emptyLabel2);
      gbc.gridwidth = 3;
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbl.setConstraints(showCatLabel, gbc);
      titleBarPanel.add(showCatLabel);
      
      //Add fields
      closeIconGroup = new ButtonGroup();
      ImageIcon closeIcon1 = new ImageIcon(ResourceLoader.getCloseIcon(1));
      ImageIcon closeIcon2 = new ImageIcon(ResourceLoader.getCloseIcon(2));
      closeIcon1Button = new JRadioButton();
      closeIcon1Button.addActionListener(this);
      closeIcon2Button = new JRadioButton();
      closeIcon2Button.addActionListener(this);
      closeIconGroup.add(closeIcon1Button);
      closeIconGroup.add(closeIcon2Button);
      JLabel closeIcon1Label = new JLabel(closeIcon1);
      JLabel closeIcon2Label = new JLabel(closeIcon2);
      showCatBox = new JCheckBox("");
      
      //Set settings for all fields
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(closeIcon1Button, gbc);
      titleBarPanel.add(closeIcon1Button);
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbl.setConstraints(closeIcon2Button, gbc);
      titleBarPanel.add(closeIcon2Button);
      gbc.gridx = 2;
      gbc.gridy = 0;
      gbl.setConstraints(closeIcon1Label, gbc);
      titleBarPanel.add(closeIcon1Label);
      gbc.gridx = 2;
      gbc.gridy = 1;
      gbl.setConstraints(closeIcon2Label, gbc);
      titleBarPanel.add(closeIcon2Label);
      gbc.weightx = 100;
      gbc.gridx = 3;
      gbc.gridy = 3;
      gbl.setConstraints(showCatBox, gbc);
      titleBarPanel.add(showCatBox);
      
      return titleBarPanel;
   }
   
   private JPanel makeSizePosPanel() {
      // PANEL FOR SIZE AND POSITIONS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel sizePanel = new JPanel(gbl);
      sizePanel.setBorder(new TitledBorder("Size and Position"));
      //Add all Labels
      JLabel defaultWidthLabel = new JLabel("Default Note Width: ");
      JLabel defaultHeightLabel = new JLabel("Default Note Height: ");
      JLabel defaultXPositionLabel = new JLabel("Default Note X Position: ");
      JLabel defaultYPositionLabel = new JLabel("Default Note Y Position: ");
      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      
      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(defaultWidthLabel, gbc);
      sizePanel.add(defaultWidthLabel);
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbl.setConstraints(defaultHeightLabel, gbc);
      sizePanel.add(defaultHeightLabel);
      gbc.gridx = 3;
      gbc.gridy = 0;
      gbl.setConstraints(defaultXPositionLabel, gbc);
      sizePanel.add(defaultXPositionLabel);
      gbc.gridx = 3;
      gbc.gridy = 1;
      gbl.setConstraints(defaultYPositionLabel, gbc);
      sizePanel.add(defaultYPositionLabel);
      
      //Add textfields      
      defaultWidthField = new JTextField(4);
      defaultWidthField.getDocument().addDocumentListener(this);
      defaultHeightField = new JTextField(4);
      defaultHeightField.getDocument().addDocumentListener(this);
      defaultXPositionField = new JTextField(4);
      defaultXPositionField.getDocument().addDocumentListener(this);
      defaultYPositionField = new JTextField(4);
      defaultYPositionField.getDocument().addDocumentListener(this);
      //Set settings for all textfields
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE; 
      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(defaultWidthField, gbc);
      sizePanel.add(defaultWidthField);
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbl.setConstraints(defaultHeightField, gbc);
      sizePanel.add(defaultHeightField);
      gbc.gridx = 4;
      gbc.gridy = 0;
      gbl.setConstraints(defaultXPositionField, gbc);
      sizePanel.add(defaultXPositionField);
      gbc.gridx = 4;
      gbc.gridy = 1;
      gbl.setConstraints(defaultYPositionField, gbc);
      sizePanel.add(defaultYPositionField);
      return sizePanel;
   }
   
   private JPanel makeFontPanel() {
      // PANEL FOR FONT SETTINGS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel fontPanel = new JPanel(gbl);
      fontPanel.setBorder(new TitledBorder("Font"));
      //Add all Labels
      JLabel defaultFontSizeLabel = new JLabel("Default Font Size: ");

      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      

      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(defaultFontSizeLabel, gbc);
      fontPanel.add(defaultFontSizeLabel);
      
      //Add fields      
      defaultFontSizeSpinner = new JSpinner(new SpinnerNumberModel(5, 5, 30, 1));

      //Set settings for all fields
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE; 
      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(defaultFontSizeSpinner, gbc);
      fontPanel.add(defaultFontSizeSpinner);

      return fontPanel;
   }

   private JPanel makeVisibilityPanel() {
      // PANEL FOR VISIBILITY SETTINGS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel visibilityPanel = new JPanel(gbl);
      visibilityPanel.setBorder(new TitledBorder("Visibility"));
      //Add all Labels
      JLabel alwaysOnTopLabel = new JLabel("Always On Top By Default: ");

      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      

      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(alwaysOnTopLabel, gbc);
      visibilityPanel.add(alwaysOnTopLabel);
      
      //Add fields
      alwaysOnTopBox = new JCheckBox("");

      //Set settings for all fields
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(alwaysOnTopBox, gbc);
      visibilityPanel.add(alwaysOnTopBox);

      return visibilityPanel;
   }
   
   private JPanel makeNotesFilePanel() {
      // PANEL FOR NOTES FILE SETTINGS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel notesFilePanel = new JPanel(gbl);
      notesFilePanel.setBorder(new TitledBorder("Notes File"));
      //Add fields
      notesFileField = new JTextField(20);
      notesFileField.getDocument().addDocumentListener(this);
      browseButton = new JButton("browse");
      browseButton.addActionListener(this);
      //Set settings for all fields
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      //Add fields with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(notesFileField, gbc);
      notesFilePanel.add(notesFileField);
      gbc.weightx = 100;
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(browseButton, gbc);
      notesFilePanel.add(browseButton);

      return notesFilePanel;
   }
   
   private JPanel makeFtpPanel() {
      // PANEL FOR CATEGORY NAMES
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel ftpPanel = new JPanel(gbl);
      ftpPanel.setBorder(new TitledBorder("FTP settings"));
      //Add all Labels
      JLabel ftpServerLabel = new JLabel("FTP-Server:");
      JLabel ftpUserLabel = new JLabel("FTP-User:");
      JLabel ftpPasswdLabel = new JLabel("FTP-Password:");
      JLabel ftpDirLabel = new JLabel("FTP-Directory:");

      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      

      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(ftpServerLabel, gbc);
      ftpPanel.add(ftpServerLabel);
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbl.setConstraints(ftpUserLabel, gbc);
      ftpPanel.add(ftpUserLabel);
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbl.setConstraints(ftpPasswdLabel, gbc);
      ftpPanel.add(ftpPasswdLabel);
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbl.setConstraints(ftpDirLabel, gbc);
      ftpPanel.add(ftpDirLabel);

      
      //Add fields
      ftpServerField = new JTextField(20);
      ftpServerField.getDocument().addDocumentListener(this);
      ftpUserField = new JTextField(20);
      ftpUserField.getDocument().addDocumentListener(this);
      ftpPasswdField = new JPasswordField(20);
      ftpPasswdField.getDocument().addDocumentListener(this);
      ftpDirField = new JTextField(20);
      ftpDirField.getDocument().addDocumentListener(this);
      
      //Set settings for all fields
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(ftpServerField, gbc);
      ftpPanel.add(ftpServerField);
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbl.setConstraints(ftpUserField, gbc);
      ftpPanel.add(ftpUserField);
      gbc.gridx = 1;
      gbc.gridy = 2;
      gbl.setConstraints(ftpPasswdField, gbc);
      ftpPanel.add(ftpPasswdField);
      gbc.gridx = 1;
      gbc.gridy = 3;
      gbl.setConstraints(ftpDirField, gbc);
      ftpPanel.add(ftpDirField);

      return ftpPanel;
   }
   
   private JPanel makeCatNamesPanel() {
      // PANEL FOR CATEGORY NAMES
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel catNamesPanel = new JPanel(gbl);
      catNamesPanel.setBorder(new TitledBorder("Category Names"));
      //Add all Labels
      JLabel cat1Label = new JLabel("Category 1: ");
      JLabel cat2Label = new JLabel("Category 2: ");
      JLabel cat3Label = new JLabel("Category 3: ");
      JLabel cat4Label = new JLabel("Category 4: ");
      JLabel cat5Label = new JLabel("Category 5: ");

      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      

      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(cat1Label, gbc);
      catNamesPanel.add(cat1Label);
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbl.setConstraints(cat2Label, gbc);
      catNamesPanel.add(cat2Label);
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbl.setConstraints(cat3Label, gbc);
      catNamesPanel.add(cat3Label);
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbl.setConstraints(cat4Label, gbc);
      catNamesPanel.add(cat4Label);
      gbc.gridx = 0;
      gbc.gridy = 4;
      gbl.setConstraints(cat5Label, gbc);
      catNamesPanel.add(cat5Label);
      
      //Add fields
      cat1Field = new JTextField(20);
      cat1Field.getDocument().addDocumentListener(this);
      cat2Field = new JTextField(20);
      cat2Field.getDocument().addDocumentListener(this);
      cat3Field = new JTextField(20);
      cat3Field.getDocument().addDocumentListener(this);
      cat4Field = new JTextField(20);
      cat4Field.getDocument().addDocumentListener(this);
      cat5Field = new JTextField(20);
      cat5Field.getDocument().addDocumentListener(this);
      //Set settings for all fields
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(cat1Field, gbc);
      catNamesPanel.add(cat1Field);
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbl.setConstraints(cat2Field, gbc);
      catNamesPanel.add(cat2Field);
      gbc.gridx = 1;
      gbc.gridy = 2;
      gbl.setConstraints(cat3Field, gbc);
      catNamesPanel.add(cat3Field);
      gbc.gridx = 1;
      gbc.gridy = 3;
      gbl.setConstraints(cat4Field, gbc);
      catNamesPanel.add(cat4Field);
      gbc.gridx = 1;
      gbc.gridy = 4;
      gbl.setConstraints(cat5Field, gbc);
      catNamesPanel.add(cat5Field);

      return catNamesPanel;
   }
   
   
   public SettingsDialog() {
      super("Settings - pin 'em up");
      setSize(new Dimension(640,480));

      // PREPARE ALL PANELS
      // ---------------------
      JPanel mainPanel = new JPanel(new BorderLayout());
      
      //tabbed pane and tabs
      JTabbedPane tpane = new JTabbedPane();
      JPanel lookAndFeelTab = makeLookAndFeelTab();
      JPanel defaultsTab = makeDefaultsTab();
      JPanel loadSaveTab = makeLoadSaveTab();
      JPanel categoryTab = makeCategoryTab();
      tpane.addTab("Notes Look&Feel", null, lookAndFeelTab, "Look&Feel of the Notes");
      tpane.addTab("Notes Default Settings", null, defaultsTab, "Default Settings for new Notes");
      tpane.addTab("Load/Save", null, loadSaveTab, "Load / Save Settings");
      tpane.addTab("Categories", null, categoryTab, "Names of the categories");
      mainPanel.add(tpane, BorderLayout.CENTER);

      // PANEL WITH BUTTONS
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
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);
      buttonPanel.add(applyButton);
      mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
      // Load Settings Into Fields
      loadSettings();

      applyButton.setEnabled(false);
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
      } else if (src == closeIcon1Button || src == closeIcon2Button) {
         applyButton.setEnabled(true);
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
      if (PinEmUp.getUserSettings().getCloseIcon() == 1) {
         closeIcon1Button.setSelected(true);
      } else if (PinEmUp.getUserSettings().getCloseIcon() == 2) {
         closeIcon2Button.setSelected(true);
      }
      alwaysOnTopBox.setSelected(PinEmUp.getUserSettings().getDefaultAlwaysOnTop());
      showCatBox.setSelected(PinEmUp.getUserSettings().getShowCategory());
            
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
      boolean showCat = showCatBox.isSelected();
      byte ci = 1;
      if (closeIcon1Button.isSelected()) {
         ci = 1;
      } else if (closeIcon2Button.isSelected()) {
         ci = 2;
      }
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
      PinEmUp.getUserSettings().setCloseIcon(ci);
      PinEmUp.getUserSettings().setShowCategory(showCat);
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
