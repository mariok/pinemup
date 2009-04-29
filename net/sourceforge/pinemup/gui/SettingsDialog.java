/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2009 by Mario Ködding
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.File;
import java.util.List;

import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import net.sourceforge.pinemup.io.ServerConnection;
import net.sourceforge.pinemup.logic.*;
import net.sourceforge.pinemup.menus.TrayMenu;

public class SettingsDialog extends JFrame implements ActionListener, DocumentListener, ChangeListener {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private JButton okButton, cancelButton, applyButton, browseButton;

   private JTextField defaultWidthField, defaultHeightField, defaultXPositionField, defaultYPositionField, serverAddressField, serverUserField, serverDirField, notesFileField;

   private JPasswordField serverPasswdField;

   private JCheckBox alwaysOnTopBox, showCatBox, confirmDeleteBox, storeServerPassBox, checkForUpdatesBox;

   private JSpinner defaultFontSizeSpinner;
   
   private JRadioButton closeIcon1Button, closeIcon2Button;
   
   private ButtonGroup closeIconGroup;
   
   private JButton checkForUpdatesButton;
   
   private JComboBox serverTypeBox;
   
   private JPanel makeGeneralTab() {
      JPanel generalPanel = new JPanel();

      // GENERAL SETTINGS
      GridBagLayout gbl = new GridBagLayout();
      generalPanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;

      //Add Panel for update check
      JPanel updateCheckPanel = makeUpdateCheckPanel();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(updateCheckPanel, gbc);
      generalPanel.add(updateCheckPanel);
      
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
      generalPanel.add(emptyPanel); 
      
      return generalPanel;
   }
   
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

      //Add Panel for behavior
      JPanel behaviorPanel = makeBehaviorPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(behaviorPanel, gbc);
      lookAndFeelPanel.add(behaviorPanel);
      
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
      JPanel serverPanel = makeServerPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(serverPanel, gbc);
      loadSavePanel.add(serverPanel);
      
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

   private JPanel makeUpdateCheckPanel() {
      // PANEL FOR SIZE AND POSITIONS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.WEST;
      JPanel updateCheckPanel = new JPanel(gbl);
      updateCheckPanel.setBorder(new TitledBorder(I18N.getInstance().getString("settingsdialog.update.border")));
      //Add all Labels
      JLabel checkForUpdatesLabel = new JLabel(I18N.getInstance().getString("settingsdialog.update.checkbox") + ": ");
      JLabel emptyLabel1 = new JLabel(" ");
      JLabel emptyLabel2 = new JLabel(" ");
      JLabel emptyLabel3 = new JLabel(" ");
      JLabel emptyLabel4 = new JLabel(" ");
      JLabel emptyLabel5 = new JLabel(" ");
      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      
      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(checkForUpdatesLabel, gbc);
      updateCheckPanel.add(checkForUpdatesLabel);
      gbc.weightx = 100;
      gbc.gridx = 2;
      gbc.gridy = 0;
      gbl.setConstraints(emptyLabel1, gbc);
      updateCheckPanel.add(emptyLabel1);
      gbc.weightx = 0;
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbl.setConstraints(emptyLabel2, gbc);
      updateCheckPanel.add(emptyLabel2);
      gbc.weightx = 0;
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbl.setConstraints(emptyLabel3, gbc);
      updateCheckPanel.add(emptyLabel3);
      gbc.weightx = 100;
      gbc.gridx = 2;
      gbc.gridy = 1;
      gbl.setConstraints(emptyLabel4, gbc);
      updateCheckPanel.add(emptyLabel4);
      gbc.weightx = 100;
      gbc.gridx = 1;
      gbc.gridy = 2;
      gbl.setConstraints(emptyLabel5, gbc);
      updateCheckPanel.add(emptyLabel5);      
      //Add fields
      checkForUpdatesBox = new JCheckBox("");
      checkForUpdatesBox.addActionListener(this);
      checkForUpdatesButton = new JButton(I18N.getInstance().getString("settingsdialog.update.button"));
      checkForUpdatesButton.addActionListener(this);

      
      //Set settings for all fields
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      //Add fields with their positions
      gbc.weightx = 0;
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(checkForUpdatesBox, gbc);
      updateCheckPanel.add(checkForUpdatesBox);

      gbc.weightx = 0;
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbl.setConstraints(checkForUpdatesButton, gbc);
      updateCheckPanel.add(checkForUpdatesButton);

      return updateCheckPanel;
   }
   
   private JPanel makeTitleBarPanel() {
      // PANEL FOR SIZE AND POSITIONS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.WEST;
      JPanel titleBarPanel = new JPanel(gbl);
      titleBarPanel.setBorder(new TitledBorder(I18N.getInstance().getString("settingsdialog.notetitlebar.border")));
      //Add all Labels
      JLabel closeIconLabel = new JLabel(I18N.getInstance().getString("settingsdialog.notetitlebar.closeicon") + ": ");
      JLabel showCatLabel = new JLabel(I18N.getInstance().getString("settingsdialog.notetitlebar.showcategory") + ": ");
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
      ImageIcon closeIcon1 = new ImageIcon(ResourceLoader.getInstance().getCloseIcon(1));
      ImageIcon closeIcon2 = new ImageIcon(ResourceLoader.getInstance().getCloseIcon(2));
      closeIcon1Button = new JRadioButton();
      closeIcon1Button.addActionListener(this);
      closeIcon2Button = new JRadioButton();
      closeIcon2Button.addActionListener(this);
      closeIconGroup.add(closeIcon1Button);
      closeIconGroup.add(closeIcon2Button);
      JLabel closeIcon1Label = new JLabel(closeIcon1);
      JLabel closeIcon2Label = new JLabel(closeIcon2);
      showCatBox = new JCheckBox("");
      showCatBox.addActionListener(this);
      
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

   private JPanel makeBehaviorPanel() {
      // PANEL FOR SIZE AND POSITIONS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.WEST;
      JPanel behaviorPanel = new JPanel(gbl);
      behaviorPanel.setBorder(new TitledBorder(I18N.getInstance().getString("settingsdialog.behavior.border")));
      //Add all Labels
      JLabel confirmDeleteLabel = new JLabel(I18N.getInstance().getString("settingsdialog.behavior.confirmnotdeletion") + ": ");
      JLabel emptyLabel = new JLabel(" ");
      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      
      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbl.setConstraints(confirmDeleteLabel, gbc);
      behaviorPanel.add(confirmDeleteLabel);
      gbc.weightx = 100;
      gbc.gridx = 2;
      gbc.gridy = 0;
      gbl.setConstraints(emptyLabel, gbc);
      behaviorPanel.add(emptyLabel);
      
      //Add fields
      confirmDeleteBox = new JCheckBox("");
      confirmDeleteBox.addActionListener(this);
      
      //Set settings for all fields
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      //Add fields with their positions
      gbc.weightx = 100;
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbl.setConstraints(confirmDeleteBox, gbc);
      behaviorPanel.add(confirmDeleteBox);
      
      return behaviorPanel;
   }
   
   private JPanel makeSizePosPanel() {
      // PANEL FOR SIZE AND POSITIONS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel sizePanel = new JPanel(gbl);
      sizePanel.setBorder(new TitledBorder(I18N.getInstance().getString("settingsdialog.sizepos.border")));
      //Add all Labels
      JLabel defaultWidthLabel = new JLabel(I18N.getInstance().getString("settingsdialog.sizepos.defaultnotewidth") + ": ");
      JLabel defaultHeightLabel = new JLabel(I18N.getInstance().getString("settingsdialog.sizepos.defaultnoteheight") + ": ");
      JLabel defaultXPositionLabel = new JLabel(I18N.getInstance().getString("settingsdialog.sizepos.defaultnotexpos") + ": ");
      JLabel defaultYPositionLabel = new JLabel(I18N.getInstance().getString("settingsdialog.sizepos.defaultnoteypos") + ": ");
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
      fontPanel.setBorder(new TitledBorder(I18N.getInstance().getString("settingsdialog.font.border")));
      //Add all Labels
      JLabel defaultFontSizeLabel = new JLabel(I18N.getInstance().getString("settingsdialog.font.defaultfontsize") + ": ");

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
      defaultFontSizeSpinner.addChangeListener(this);

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
      visibilityPanel.setBorder(new TitledBorder(I18N.getInstance().getString("settingsdialog.visibility.border")));
      //Add all Labels
      JLabel alwaysOnTopLabel = new JLabel(I18N.getInstance().getString("settingsdialog.visibility.defaultalwaysontop") + ": ");

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
      alwaysOnTopBox.addActionListener(this);

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
      notesFilePanel.setBorder(new TitledBorder(I18N.getInstance().getString("settingsdialog.notesfile.border")));
      //Add fields
      notesFileField = new JTextField(20);
      notesFileField.getDocument().addDocumentListener(this);
      browseButton = new JButton(I18N.getInstance().getString("settingsdialog.notesfile.browsebutton"));
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
   
   private JPanel makeServerPanel() {
      // PANEL FOR IM-/EXPORT SERVER
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel serverPanel = new JPanel(gbl);
      serverPanel.setBorder(new TitledBorder(I18N.getInstance().getString("settingsdialog.server.border")));
      //Create all Labels
      JLabel serverTypeLabel = new JLabel(I18N.getInstance().getString("settingsdialog.server.type") + ":");
      JLabel serverAddressLabel = new JLabel(I18N.getInstance().getString("settingsdialog.server.address") + ":");
      JLabel serverUserLabel = new JLabel(I18N.getInstance().getString("settingsdialog.server.user") + ":");
      JLabel serverPasswdLabel = new JLabel(I18N.getInstance().getString("settingsdialog.server.password") + ":");
      JLabel serverDirLabel = new JLabel(I18N.getInstance().getString("settingsdialog.server.directory") + ":");

      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;      

      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      serverPanel.add(serverTypeLabel, gbc);
      gbc.gridy = 1;
      serverPanel.add(serverAddressLabel, gbc);
      gbc.gridy = 2;
      serverPanel.add(serverUserLabel, gbc);
      gbc.gridy = 3;
      serverPanel.add(serverPasswdLabel, gbc);
      gbc.gridy = 5;
      serverPanel.add(serverDirLabel, gbc);

      
      //Create fields
      serverTypeBox = new JComboBox(ServerConnection.SERVERTYPE_NAMES);
      serverTypeBox.addActionListener(this);
      serverAddressField = new JTextField(20);
      serverAddressField.getDocument().addDocumentListener(this);
      serverUserField = new JTextField(20);
      serverUserField.getDocument().addDocumentListener(this);
      serverPasswdField = new JPasswordField(20);
      serverPasswdField.getDocument().addDocumentListener(this);
      storeServerPassBox = new JCheckBox(I18N.getInstance().getString("settingsdialog.server.storepwcheckbox"));
      storeServerPassBox.addActionListener(this);
      serverDirField = new JTextField(20);
      serverDirField.getDocument().addDocumentListener(this);
      
      //Set settings for all fields
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      
      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      serverPanel.add(serverTypeBox, gbc);
      gbc.gridy = 1;
      serverPanel.add(serverAddressField, gbc);
      gbc.gridy = 2;
      serverPanel.add(serverUserField, gbc);
      gbc.gridy = 3;
      serverPanel.add(serverPasswdField, gbc);
      gbc.gridy = 4;
      serverPanel.add(storeServerPassBox, gbc);
      gbc.gridy = 5;
      serverPanel.add(serverDirField,gbc);

      return serverPanel;
   }
   
   public SettingsDialog() {
      super(I18N.getInstance().getString("settingsdialog.title"));
      setSize(new Dimension(640,480));

      // PREPARE ALL PANELS
      // ---------------------
      JPanel mainPanel = new JPanel(new BorderLayout());
      
      //tabbed pane and tabs
      JTabbedPane tpane = new JTabbedPane();
      JPanel generalTab = makeGeneralTab();
      JPanel lookAndFeelTab = makeLookAndFeelTab();
      JPanel defaultsTab = makeDefaultsTab();
      JPanel loadSaveTab = makeLoadSaveTab();
      tpane.addTab(I18N.getInstance().getString("settingsdialog.generaltab.title"), null, generalTab, I18N.getInstance().getString("settingsdialog.generaltab.tooltip"));
      tpane.addTab(I18N.getInstance().getString("settingsdialog.lookandfeeltab.title"), null, lookAndFeelTab, I18N.getInstance().getString("settingsdialog.lookandfeeltab.tooltip"));
      tpane.addTab(I18N.getInstance().getString("settingsdialog.defaultsettingstab.title"), null, defaultsTab, I18N.getInstance().getString("settingsdialog.defaultnotesettingstab.tooltip"));
      tpane.addTab(I18N.getInstance().getString("settingsdialog.loadsavetab.title"), null, loadSaveTab, I18N.getInstance().getString("settingsdialog.loadsavetab.tooltip"));
      mainPanel.add(tpane, BorderLayout.CENTER);

      // PANEL WITH BUTTONS
      okButton = new JButton(I18N.getInstance().getString("okbutton"));
      okButton.addActionListener(this);
      cancelButton = new JButton(I18N.getInstance().getString("cancelbutton"));
      cancelButton.addActionListener(this);
      applyButton = new JButton(I18N.getInstance().getString("applybutton"));
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
         if (FileDialogCreator.getFileDialogInstance().showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            f = FileDialogCreator.getFileDialogInstance().getSelectedFile();
         }
         if (f != null) {
            notesFileField.setText(NoteIO.checkAndAddExtension(f.getAbsolutePath(),".xml"));
         }
      } else if (src == checkForUpdatesButton) {
         new UpdateCheckThread(true);
      } else if (src == checkForUpdatesBox || src == closeIcon1Button || src == closeIcon2Button || src == alwaysOnTopBox || src == showCatBox || src == confirmDeleteBox || src == serverTypeBox) {
         applyButton.setEnabled(true);
      } else if (src == storeServerPassBox) {
         applyButton.setEnabled(true);
         serverPasswdField.setEnabled(storeServerPassBox.isSelected());
      }
   }

   private void loadSettings() {
      // write settings from object into fields
      
      checkForUpdatesBox.setSelected(UserSettings.getInstance().isUpdateCheckEnabled());
      
      defaultWidthField.setText(String.valueOf(UserSettings.getInstance().getDefaultWindowWidth()));
      defaultHeightField.setText(String.valueOf(UserSettings.getInstance().getDefaultWindowHeight()));
      defaultXPositionField.setText(String.valueOf(UserSettings.getInstance().getDefaultWindowXPostition()));
      defaultYPositionField.setText(String.valueOf(UserSettings.getInstance().getDefaultWindowYPostition()));
      defaultFontSizeSpinner.getModel().setValue(new Integer(UserSettings.getInstance().getDefaultFontSize()));
      if (UserSettings.getInstance().getCloseIcon() == 1) {
         closeIcon1Button.setSelected(true);
      } else if (UserSettings.getInstance().getCloseIcon() == 2) {
         closeIcon2Button.setSelected(true);
      }
      alwaysOnTopBox.setSelected(UserSettings.getInstance().getDefaultAlwaysOnTop());
      showCatBox.setSelected(UserSettings.getInstance().getShowCategory());
      confirmDeleteBox.setSelected(UserSettings.getInstance().getConfirmDeletion());
            
      notesFileField.setText(UserSettings.getInstance().getNotesFile());
      serverTypeBox.setSelectedIndex(UserSettings.getInstance().getServerType());
      serverAddressField.setText(UserSettings.getInstance().getServerAddress());
      serverUserField.setText(UserSettings.getInstance().getServerUser());
      storeServerPassBox.setSelected(UserSettings.getInstance().getStoreServerPass());
      if (UserSettings.getInstance().getStoreServerPass() && UserSettings.getInstance().getServerPasswd() != null) {
            serverPasswdField.setText(String.copyValueOf(UserSettings.getInstance().getServerPasswd()));
      }
      serverPasswdField.setEnabled(UserSettings.getInstance().getStoreServerPass());
      serverDirField.setText(UserSettings.getInstance().getServerDir());
   }

   private void saveSettings() {
      //save old notesfile
      NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());
      
      // load settings from fields
      boolean updateCheckEnabled = checkForUpdatesBox.isSelected(); 
      short defaultWidth = Short.parseShort(defaultWidthField.getText());
      short defaultHeight = Short.parseShort(defaultHeightField.getText());
      short defaultXPosition = Short.parseShort(defaultXPositionField.getText());
      short defaultYPosition = Short.parseShort(defaultYPositionField.getText());
      short defaultFontSize = ((Integer)((SpinnerNumberModel)defaultFontSizeSpinner.getModel()).getNumber()).shortValue();
      boolean defaultAlwaysOnTop = alwaysOnTopBox.isSelected();
      boolean showCat = showCatBox.isSelected();
      boolean confirmDel = confirmDeleteBox.isSelected();
      byte ci = 1;
      if (closeIcon1Button.isSelected()) {
         ci = 1;
      } else if (closeIcon2Button.isSelected()) {
         ci = 2;
      }
      String notesFile = notesFileField.getText();
      short serverType = (short)serverTypeBox.getSelectedIndex();
      String serverAddress = serverAddressField.getText();
      String serverUser = serverUserField.getText();
      char[] serverPasswd = serverPasswdField.getPassword();
      boolean storeServerPass = storeServerPassBox.isSelected();
      String serverDir = serverDirField.getText();
      
      // write settings into object
      UserSettings.getInstance().setUpdateCheckEnabled(updateCheckEnabled);
      UserSettings.getInstance().setDefaultWindowHeight(defaultHeight);
      UserSettings.getInstance().setDefaultWindowWidth(defaultWidth);
      UserSettings.getInstance().setDefaultWindowXPosition(defaultXPosition);
      UserSettings.getInstance().setDefaultWindowYPosition(defaultYPosition);
      UserSettings.getInstance().setDefaultFontSize(defaultFontSize);
      UserSettings.getInstance().setDefaultAlwaysOnTop(defaultAlwaysOnTop);
      UserSettings.getInstance().setCloseIcon(ci);
      UserSettings.getInstance().setShowCategory(showCat);
      UserSettings.getInstance().setConfirmDeletion(confirmDel);
      UserSettings.getInstance().setNotesFile(notesFile);
      UserSettings.getInstance().setServerType(serverType);
      UserSettings.getInstance().setServerAddress(serverAddress);
      UserSettings.getInstance().setServerUser(serverUser);
      UserSettings.getInstance().setStoreServerPass(storeServerPass);
      if (storeServerPass) {
         UserSettings.getInstance().setServerPasswd(serverPasswd);
      } else {
         UserSettings.getInstance().setServerPasswd(null);
      }
      UserSettings.getInstance().setServerDir(serverDir);
      
      // load new notes from file
      CategoryManager.getInstance().hideAllNotes();
      CategoryManager.getInstance().removeAllCategories();
      List<Category> cl = NoteIO.readCategoriesFromFile();
      notesFileField.setText(UserSettings.getInstance().getNotesFile()); //if file has not been valid and new one has been selected
      CategoryManager.getInstance().append(cl);
      
      // show all visible notes
      CategoryManager.getInstance().showAllNotesNotHidden();
      
      // replace Traymenu (because of new categories)
      PinEmUpTrayIcon.getInstance().setPopupMenu(new TrayMenu());
      
      // save settings permanentely
      UserSettings.getInstance().saveSettings();
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

   public void stateChanged(ChangeEvent arg0) {
      applyButton.setEnabled(true);
   }
}
