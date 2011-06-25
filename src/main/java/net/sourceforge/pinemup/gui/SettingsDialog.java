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

package net.sourceforge.pinemup.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sourceforge.pinemup.io.NoteIO;
import net.sourceforge.pinemup.io.ServerConnection;
import net.sourceforge.pinemup.logic.Category;
import net.sourceforge.pinemup.logic.CategoryManager;
import net.sourceforge.pinemup.logic.PinEmUpTrayIcon;
import net.sourceforge.pinemup.logic.ResourceLoader;
import net.sourceforge.pinemup.logic.UpdateCheckThread;
import net.sourceforge.pinemup.logic.UserSettings;
import net.sourceforge.pinemup.menus.TrayMenu;

public class SettingsDialog extends JFrame implements ActionListener, DocumentListener, ChangeListener {
   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private static final int DIALOG_WIDTH = 640;
   private static final int DIALOG_HEIGHT = 480;

   private static SettingsDialog instance;

   private JButton okButton, cancelButton, applyButton, browseButton;
   private JTextField defaultWidthField, defaultHeightField, defaultXPositionField, defaultYPositionField, serverAddressField, serverUserField, serverDirField, notesFileField;
   private JPasswordField serverPasswdField;
   private JCheckBox alwaysOnTopBox, showCatBox, confirmDeleteBox, storeServerPassBox, updateCheckBox, confirmUpDownloadBox;
   private JSpinner defaultFontSizeSpinner;
   private JRadioButton closeIcon1Button, closeIcon2Button;
   private ButtonGroup closeIconGroup;
   private JButton updateCheckButton;
   private JComboBox serverTypeBox, languageBox;
   private TitledBorder updateCheckBorder, languageBorder, titleBarBorder, behaviorBorder, sizeBorder, fontBorder, visibilityBorder, notesFileBorder, serverBorder;
   private JLabel updateCheckLabel, languageLabel, closeIconLabel, showCatLabel, confirmDeleteLabel, defaultWidthLabel, defaultHeightLabel, defaultXPositionLabel, defaultYPositionLabel, defaultFontSizeLabel, alwaysOnTopLabel, serverTypeLabel, serverAddressLabel, serverUserLabel, serverPasswdLabel, serverDirLabel;
   private JTabbedPane tpane;

   public static void showInstance() {
      if (SettingsDialog.instance == null || !SettingsDialog.instance.isVisible()) {
         instance = new SettingsDialog();
      }
   }

   private JPanel makeGeneralTab() {
      JPanel generalPanel = new JPanel();

      // GENERAL SETTINGS
      GridBagLayout gbl = new GridBagLayout();
      generalPanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.fill = GridBagConstraints.BOTH;

      //Add Panel for update check
      JPanel updateCheckPanel = makeUpdateCheckPanel();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weighty = 0;
      generalPanel.add(updateCheckPanel, gbc);

      //Add Panel for language
      JPanel languagePanel = makeLanguagePanel();
      gbc.gridy = 1;
      generalPanel.add(languagePanel, gbc);

      //Add empty panel to tab
      JPanel emptyPanel = new JPanel();
      gbc.gridy = 2;
      gbc.weighty = 100;
      generalPanel.add(emptyPanel, gbc);

      return generalPanel;
   }

   private JPanel makeLookAndFeelTab() {
      JPanel lookAndFeelPanel = new JPanel();
      // TAB WITH LOOK AND FEEL SETTINGS
      GridBagLayout gbl = new GridBagLayout();
      lookAndFeelPanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
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
      lookAndFeelPanel.add(titleBarPanel, gbc);

      //Add Panel for behavior
      JPanel behaviorPanel = makeBehaviorPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      lookAndFeelPanel.add(behaviorPanel, gbc);

      //Add empty panel to tab
      JPanel emptyPanel = new JPanel();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      lookAndFeelPanel.add(emptyPanel, gbc);

      return lookAndFeelPanel;
   }

   private JPanel makeDefaultsTab() {
      // TAB WITH DEFAULT SETTINGS
      JPanel defaultsPanel = new JPanel();
      GridBagLayout gbl = new GridBagLayout();
      defaultsPanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
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
      defaultsPanel.add(sizePosPanel, gbc);

      //Add Panel for Font
      JPanel fontPanel = makeFontPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      defaultsPanel.add(fontPanel, gbc);

      //Add Panel for Visibility Settings
      JPanel visibilityPanel = makeVisibilityPanel();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      defaultsPanel.add(visibilityPanel, gbc);

      //Add empty panel to tab
      JPanel emptyPanel = new JPanel();
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      defaultsPanel.add(emptyPanel, gbc);

      return defaultsPanel;
   }

   private JPanel makeLoadSaveTab() {
      JPanel loadSavePanel = new JPanel();

      GridBagLayout gbl = new GridBagLayout();
      loadSavePanel.setLayout(gbl);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
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
      loadSavePanel.add(notesFilePanel, gbc);

      //Add Panel for FTP settings
      JPanel serverPanel = makeServerPanel();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      loadSavePanel.add(serverPanel, gbc);

      //Add empty panel to tab
      JPanel emptyPanel = new JPanel();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.fill = GridBagConstraints.BOTH;
      loadSavePanel.add(emptyPanel, gbc);

      return loadSavePanel;
   }

   private JPanel makeUpdateCheckPanel() {
      // PANEL FOR UPDATE CHECK
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.WEST;
      JPanel updateCheckPanel = new JPanel(gbl);
      updateCheckBorder = new TitledBorder("");
      updateCheckPanel.setBorder(updateCheckBorder);

      //create all Labels
      updateCheckLabel = new JLabel();
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
      updateCheckPanel.add(updateCheckLabel, gbc);
      gbc.weightx = 100;
      gbc.gridx = 2;
      gbc.gridy = 0;
      updateCheckPanel.add(emptyLabel1, gbc);
      gbc.weightx = 0;
      gbc.gridx = 0;
      gbc.gridy = 1;
      updateCheckPanel.add(emptyLabel2, gbc);
      gbc.weightx = 0;
      gbc.gridx = 1;
      gbc.gridy = 1;
      updateCheckPanel.add(emptyLabel3, gbc);
      gbc.weightx = 100;
      gbc.gridx = 2;
      gbc.gridy = 1;
      updateCheckPanel.add(emptyLabel4, gbc);
      gbc.weightx = 100;
      gbc.gridx = 1;
      gbc.gridy = 2;
      updateCheckPanel.add(emptyLabel5, gbc);
      //Add fields
      updateCheckBox = new JCheckBox("");
      updateCheckBox.addActionListener(this);
      updateCheckButton = new JButton();
      updateCheckButton.addActionListener(this);


      //Set settings for all fields
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;

      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      updateCheckPanel.add(updateCheckBox, gbc);

      gbc.gridx = 0;
      gbc.gridy = 2;
      updateCheckPanel.add(updateCheckButton, gbc);

      return updateCheckPanel;
   }

   private JPanel makeLanguagePanel() { //panel for language selection
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.WEST;
      JPanel languagePanel = new JPanel(gbl);
      languageBorder = new TitledBorder("");
      languagePanel.setBorder(languageBorder);

      //Create labels
      languageLabel = new JLabel();
      JLabel emptyLabel = new JLabel(" ");

      //Set settings for all labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;

      //Add labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      languagePanel.add(languageLabel, gbc);
      gbc.weightx = 100;
      gbc.gridx = 1;
      languagePanel.add(emptyLabel, gbc);

      //Create fields
      languageBox = new JComboBox(I18N.LOCALE_NAMES);
      languageBox.addActionListener(this);

      //Set settings for all fields
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;

      //Add fields with their positions
      gbc.gridx = 1;
      gbc.gridy = 0;
      languagePanel.add(languageBox, gbc);

      return languagePanel;
   }

   private JPanel makeTitleBarPanel() {
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.WEST;
      JPanel titleBarPanel = new JPanel(gbl);
      titleBarBorder = new TitledBorder("");
      titleBarPanel.setBorder(titleBarBorder);
      //Add all Labels
      closeIconLabel = new JLabel();
      showCatLabel = new JLabel();
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
      titleBarPanel.add(closeIconLabel, gbc);
      gbc.weightx = 100;
      gbc.gridx = 3;
      gbc.gridy = 0;
      titleBarPanel.add(emptyLabel, gbc);
      gbc.gridx = 3;
      gbc.gridy = 1;
      titleBarPanel.add(emptyLabel, gbc);
      gbc.weightx = 0;
      gbc.gridx = 0;
      gbc.gridy = 2;
      titleBarPanel.add(emptyLabel2, gbc);
      gbc.gridwidth = 3;
      gbc.gridx = 0;
      gbc.gridy = 3;
      titleBarPanel.add(showCatLabel, gbc);

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
      titleBarPanel.add(closeIcon1Button, gbc);
      gbc.gridx = 1;
      gbc.gridy = 1;
      titleBarPanel.add(closeIcon2Button, gbc);
      gbc.gridx = 2;
      gbc.gridy = 0;
      titleBarPanel.add(closeIcon1Label, gbc);
      gbc.gridx = 2;
      gbc.gridy = 1;
      titleBarPanel.add(closeIcon2Label, gbc);
      gbc.weightx = 100;
      gbc.gridx = 3;
      gbc.gridy = 3;
      titleBarPanel.add(showCatBox, gbc);

      return titleBarPanel;
   }

   private JPanel makeBehaviorPanel() {
      // PANEL FOR SIZE AND POSITIONS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.WEST;
      JPanel behaviorPanel = new JPanel(gbl);
      behaviorBorder = new TitledBorder("");
      behaviorPanel.setBorder(behaviorBorder);
      //Add all Labels
      confirmDeleteLabel = new JLabel();
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
      behaviorPanel.add(confirmDeleteLabel, gbc);
      gbc.weightx = 100;
      gbc.gridx = 2;
      gbc.gridy = 0;
      behaviorPanel.add(emptyLabel, gbc);

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
      behaviorPanel.add(confirmDeleteBox, gbc);

      return behaviorPanel;
   }

   private JPanel makeSizePosPanel() {
      // PANEL FOR SIZE AND POSITIONS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel sizePanel = new JPanel(gbl);
      sizeBorder = new TitledBorder("");
      sizePanel.setBorder(sizeBorder);

      //create labels
      defaultWidthLabel = new JLabel();
      defaultHeightLabel = new JLabel();
      defaultXPositionLabel = new JLabel();
      defaultYPositionLabel = new JLabel();

      //Set settings for all labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;
      //Add labels with their positions
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
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel fontPanel = new JPanel(gbl);
      fontBorder = new TitledBorder("");
      fontPanel.setBorder(fontBorder);
      //Create labels
      defaultFontSizeLabel = new JLabel();

      //Set settings for all labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;

      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      fontPanel.add(defaultFontSizeLabel, gbc);

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
      fontPanel.add(defaultFontSizeSpinner, gbc);

      return fontPanel;
   }

   private JPanel makeVisibilityPanel() {
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel visibilityPanel = new JPanel(gbl);
      visibilityBorder = new TitledBorder("");
      visibilityPanel.setBorder(visibilityBorder);
      //create labels
      alwaysOnTopLabel = new JLabel();

      //Set settings for all Labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;

      //Add Labels with their positions
      gbc.gridx = 0;
      gbc.gridy = 0;
      visibilityPanel.add(alwaysOnTopLabel, gbc);

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
      visibilityPanel.add(alwaysOnTopBox, gbc);

      return visibilityPanel;
   }

   private JPanel makeNotesFilePanel() {
      // PANEL FOR NOTES FILE SETTINGS
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel notesFilePanel = new JPanel(gbl);
      notesFileBorder = new TitledBorder("");
      notesFilePanel.setBorder(notesFileBorder);
      //Add fields
      notesFileField = new JTextField(20);
      notesFileField.getDocument().addDocumentListener(this);
      browseButton = new JButton();
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
      notesFilePanel.add(notesFileField, gbc);
      gbc.weightx = 100;
      gbc.gridx = 1;
      gbc.gridy = 0;
      notesFilePanel.add(browseButton, gbc);

      return notesFilePanel;
   }

   private JPanel makeServerPanel() {
      // PANEL FOR IM-/EXPORT SERVER
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(1, 1, 1, 1);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      JPanel serverPanel = new JPanel(gbl);
      serverBorder = new TitledBorder("");
      serverPanel.setBorder(serverBorder);

      //create all labels
      serverTypeLabel = new JLabel();
      serverAddressLabel = new JLabel();
      serverUserLabel = new JLabel();
      serverPasswdLabel = new JLabel();
      serverDirLabel = new JLabel();

      //set settings for all labels
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = GridBagConstraints.NONE;

      //add labels with their positions
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
      storeServerPassBox = new JCheckBox();
      storeServerPassBox.addActionListener(this);
      serverDirField = new JTextField(20);
      serverDirField.getDocument().addDocumentListener(this);
      confirmUpDownloadBox = new JCheckBox();

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
      serverPanel.add(serverDirField, gbc);
      gbc.gridy = 6;
      serverPanel.add(new JLabel(" "), gbc);
      gbc.gridy = 7;
      serverPanel.add(confirmUpDownloadBox, gbc);

      return serverPanel;
   }

   private SettingsDialog() {
      super();
      setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

      // PREPARE ALL PANELS
      // ---------------------
      JPanel mainPanel = new JPanel(new BorderLayout());

      //tabbed pane and tabs
      tpane = new JTabbedPane();
      JPanel generalTab = makeGeneralTab();
      JPanel lookAndFeelTab = makeLookAndFeelTab();
      JPanel defaultsTab = makeDefaultsTab();
      JPanel loadSaveTab = makeLoadSaveTab();
      tpane.addTab("", null, generalTab, "");
      tpane.addTab("", null, lookAndFeelTab, "");
      tpane.addTab("", null, defaultsTab, "");
      tpane.addTab("", null, loadSaveTab, "");
      mainPanel.add(tpane, BorderLayout.CENTER);

      // PANEL WITH BUTTONS
      okButton = new JButton();
      okButton.addActionListener(this);
      cancelButton = new JButton();
      cancelButton.addActionListener(this);
      applyButton = new JButton();
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

      //Load language-specific labels
      loadLocaleTexts();

      // Load Settings Into Fields
      loadSettings();

      applyButton.setEnabled(false);
      setContentPane(mainPanel);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);

      // center on screen
      int screenHeight = (int) getToolkit().getScreenSize().getHeight();
      int screenWidth = (int) getToolkit().getScreenSize().getWidth();
      int x = (screenWidth - getWidth()) / 2;
      int y = (screenHeight - getHeight()) / 2;
      setLocation(x, y);

      setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();

      if (src == okButton) {
         saveSettings();
         setVisible(false);
         dispose();
      } else if (src == applyButton) {
         saveSettings();
         loadLocaleTexts();
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
            notesFileField.setText(NoteIO.checkAndAddExtension(f.getAbsolutePath(), ".xml"));
         }
      } else if (src == updateCheckButton) {
         new UpdateCheckThread(true);
      } else if (src == updateCheckBox || src == closeIcon1Button || src == closeIcon2Button || src == alwaysOnTopBox || src == showCatBox || src == confirmDeleteBox || src == serverTypeBox || src == languageBox) {
         applyButton.setEnabled(true);
      } else if (src == storeServerPassBox) {
         applyButton.setEnabled(true);
         serverPasswdField.setEnabled(storeServerPassBox.isSelected());
      }
   }

   private int getIndexForLocale(String locale) {
      int index = 0;
      for (int i=0; i<I18N.LOCALES.length; i++) {
         if (I18N.LOCALES[i].equals(locale)) {
            index = i;
            break;
         }
      }
      return index;
   }

   private void loadSettings() {
      // write settings from object into fields
      updateCheckBox.setSelected(UserSettings.getInstance().isUpdateCheckEnabled());
      languageBox.setSelectedIndex(getIndexForLocale(UserSettings.getInstance().getLocale()));

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
      confirmUpDownloadBox.setSelected(UserSettings.getInstance().getConfirmUpDownload());
   }

   private void saveSettings() {
      //save old notesfile
      NoteIO.writeCategoriesToFile(CategoryManager.getInstance().getListIterator());

      // load settings from fields
      boolean updateCheckEnabled = updateCheckBox.isSelected();
      String locale = I18N.LOCALES[languageBox.getSelectedIndex()];
      short defaultWidth = Short.parseShort(defaultWidthField.getText());
      short defaultHeight = Short.parseShort(defaultHeightField.getText());
      short defaultXPosition = Short.parseShort(defaultXPositionField.getText());
      short defaultYPosition = Short.parseShort(defaultYPositionField.getText());
      short defaultFontSize = ((Integer) ((SpinnerNumberModel) defaultFontSizeSpinner.getModel()).getNumber()).shortValue();
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
      short serverType = (short) serverTypeBox.getSelectedIndex();
      String serverAddress = serverAddressField.getText();
      String serverUser = serverUserField.getText();
      char[] serverPasswd = serverPasswdField.getPassword();
      boolean storeServerPass = storeServerPassBox.isSelected();
      String serverDir = serverDirField.getText();
      boolean confirmUpDownload = confirmUpDownloadBox.isSelected();

      // write settings into object
      UserSettings.getInstance().setUpdateCheckEnabled(updateCheckEnabled);
      UserSettings.getInstance().setLocale(locale);
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
      UserSettings.getInstance().setConfirmUpDownload(confirmUpDownload);

      //set new locale
      I18N.getInstance().setLocale(UserSettings.getInstance().getLocale());

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

   @Override
   public void changedUpdate(DocumentEvent arg0) {
      applyButton.setEnabled(true);
   }

   @Override
   public void insertUpdate(DocumentEvent arg0) {
      applyButton.setEnabled(true);
   }

   @Override
   public void removeUpdate(DocumentEvent arg0) {
      applyButton.setEnabled(true);
   }

   @Override
   public void stateChanged(ChangeEvent arg0) {
      applyButton.setEnabled(true);
   }

   private void loadLocaleTexts() {
      setTitle(I18N.getInstance().getString("settingsdialog.title"));
      tpane.setTitleAt(0, I18N.getInstance().getString("settingsdialog.generaltab.title"));
      tpane.setToolTipTextAt(0, I18N.getInstance().getString("settingsdialog.generaltab.tooltip"));
      tpane.setTitleAt(1, I18N.getInstance().getString("settingsdialog.lookandfeeltab.title"));
      tpane.setToolTipTextAt(1, I18N.getInstance().getString("settingsdialog.lookandfeeltab.tooltip"));
      tpane.setTitleAt(2, I18N.getInstance().getString("settingsdialog.defaultsettingstab.title"));
      tpane.setToolTipTextAt(2, I18N.getInstance().getString("settingsdialog.defaultnotesettingstab.tooltip"));
      tpane.setTitleAt(3, I18N.getInstance().getString("settingsdialog.loadsavetab.title"));
      tpane.setToolTipTextAt(3, I18N.getInstance().getString("settingsdialog.loadsavetab.tooltip"));
      okButton.setText(I18N.getInstance().getString("okbutton"));
      cancelButton.setText(I18N.getInstance().getString("cancelbutton"));
      applyButton.setText(I18N.getInstance().getString("applybutton"));

      //general tab
      updateCheckBorder.setTitle(I18N.getInstance().getString("settingsdialog.update.border"));
      updateCheckLabel.setText(I18N.getInstance().getString("settingsdialog.update.checkbox") + ": ");
      updateCheckButton.setText(I18N.getInstance().getString("settingsdialog.update.button"));
      languageBorder.setTitle(I18N.getInstance().getString("settingsdialog.language.border"));
      languageLabel.setText(I18N.getInstance().getString("settingsdialog.language.language") + ": ");

      //look and feel tab
      titleBarBorder.setTitle(I18N.getInstance().getString("settingsdialog.notetitlebar.border"));
      closeIconLabel.setText(I18N.getInstance().getString("settingsdialog.notetitlebar.closeicon") + ": ");
      showCatLabel.setText(I18N.getInstance().getString("settingsdialog.notetitlebar.showcategory") + ": ");
      behaviorBorder.setTitle(I18N.getInstance().getString("settingsdialog.behavior.border"));
      confirmDeleteLabel.setText(I18N.getInstance().getString("settingsdialog.behavior.confirmnotdeletion") + ": ");
      sizeBorder.setTitle(I18N.getInstance().getString("settingsdialog.sizepos.border"));

      //default settings tab
      defaultWidthLabel.setText(I18N.getInstance().getString("settingsdialog.sizepos.defaultnotewidth") + ": ");
      defaultHeightLabel.setText(I18N.getInstance().getString("settingsdialog.sizepos.defaultnoteheight") + ": ");
      defaultXPositionLabel.setText(I18N.getInstance().getString("settingsdialog.sizepos.defaultnotexpos") + ": ");
      defaultYPositionLabel.setText(I18N.getInstance().getString("settingsdialog.sizepos.defaultnoteypos") + ": ");
      fontBorder.setTitle(I18N.getInstance().getString("settingsdialog.font.border"));
      defaultFontSizeLabel.setText(I18N.getInstance().getString("settingsdialog.font.defaultfontsize") + ": ");
      visibilityBorder.setTitle(I18N.getInstance().getString("settingsdialog.visibility.border"));
      alwaysOnTopLabel.setText(I18N.getInstance().getString("settingsdialog.visibility.defaultalwaysontop") + ": ");

      //load/save tab
      notesFileBorder.setTitle(I18N.getInstance().getString("settingsdialog.notesfile.border"));
      browseButton.setText(I18N.getInstance().getString("settingsdialog.notesfile.browsebutton"));
      serverBorder.setTitle(I18N.getInstance().getString("settingsdialog.server.border"));
      serverTypeLabel.setText(I18N.getInstance().getString("settingsdialog.server.type") + ":");
      serverAddressLabel.setText(I18N.getInstance().getString("settingsdialog.server.address") + ":");
      serverUserLabel.setText(I18N.getInstance().getString("settingsdialog.server.user") + ":");
      serverPasswdLabel.setText(I18N.getInstance().getString("settingsdialog.server.password") + ":");
      serverDirLabel.setText(I18N.getInstance().getString("settingsdialog.server.directory") + ":");
      storeServerPassBox.setText(I18N.getInstance().getString("settingsdialog.server.storepwcheckbox"));
      confirmUpDownloadBox.setText(I18N.getInstance().getString("settingsdialog.server.confirmupdownloadcheckbox"));
   }
}
