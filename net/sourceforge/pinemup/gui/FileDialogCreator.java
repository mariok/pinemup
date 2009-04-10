package net.sourceforge.pinemup.gui;

import javax.swing.JFileChooser;

import net.sourceforge.pinemup.logic.MyFileFilter;

public class FileDialogCreator {
   private static JFileChooser fileDialog;
   private static JFileChooser exportFileDialog;

   private FileDialogCreator() {}
   
   public static JFileChooser getFileDialogInstance() {
      if (FileDialogCreator.fileDialog == null) {
         FileDialogCreator.fileDialog = new JFileChooser();
         FileDialogCreator.fileDialog.setDialogTitle("select notes file");
         FileDialogCreator.fileDialog.removeChoosableFileFilter(fileDialog.getChoosableFileFilters()[0]);
         FileDialogCreator.fileDialog.setFileFilter(new MyFileFilter("XML"));
         FileDialogCreator.fileDialog.setMultiSelectionEnabled(false);
      }
      return FileDialogCreator.fileDialog;
   }
   
   public static JFileChooser getExportFileDialogInstance() {
      if (FileDialogCreator.exportFileDialog == null) {
         FileDialogCreator.exportFileDialog = new JFileChooser();
         FileDialogCreator.exportFileDialog.setDialogTitle("select text-file for export");
         FileDialogCreator.exportFileDialog.removeChoosableFileFilter(exportFileDialog.getChoosableFileFilters()[0]);
         FileDialogCreator.exportFileDialog.setFileFilter(new MyFileFilter("TXT"));
         FileDialogCreator.exportFileDialog.setMultiSelectionEnabled(false);
      }
      return FileDialogCreator.exportFileDialog;
   }
}
