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

package net.sourceforge.pinemup.ui.swing.dialogs.file;

import javax.swing.JFileChooser;

import net.sourceforge.pinemup.core.i18n.I18N;

public final class FileDialogCreator {
   private static JFileChooser fileDialog;
   private static JFileChooser exportFileDialog;

   private FileDialogCreator() {

   }

   public static JFileChooser getFileDialogInstance() {
      if (FileDialogCreator.fileDialog == null) {
         FileDialogCreator.fileDialog = new JFileChooser();
         FileDialogCreator.fileDialog.setDialogTitle(I18N.getInstance().getString("filedialog.title"));
         FileDialogCreator.fileDialog.removeChoosableFileFilter(fileDialog.getChoosableFileFilters()[0]);
         FileDialogCreator.fileDialog.setFileFilter(new MyFileFilter("XML"));
         FileDialogCreator.fileDialog.setMultiSelectionEnabled(false);
      }
      return FileDialogCreator.fileDialog;
   }

   public static JFileChooser getExportFileDialogInstance() {
      if (FileDialogCreator.exportFileDialog == null) {
         FileDialogCreator.exportFileDialog = new JFileChooser();
         FileDialogCreator.exportFileDialog.setDialogTitle(I18N.getInstance().getString("exportfiledialog.title"));
         FileDialogCreator.exportFileDialog.removeChoosableFileFilter(exportFileDialog.getChoosableFileFilters()[0]);
         FileDialogCreator.exportFileDialog.setFileFilter(new MyFileFilter("TXT"));
         FileDialogCreator.exportFileDialog.setMultiSelectionEnabled(false);
      }
      return FileDialogCreator.exportFileDialog;
   }
}
