/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmv.solutions.commons;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author David Koller XMV Solutions GmbH
 */
public class ExcelFile {

    private String path;

    public ExcelFile(String path) {
        this.setPath(path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Open a filechooser that finds ExcelFiles
     *
     * @return FilePath
     */
    public static ExcelFile openFileChooser() {
        return openFileChooser(System.getProperty("user.home"));
    }

    /**
     * Open a filechooser that finds ExcelFiles
     *
     * @param startPath starting with path startPath
     * @return FilePath
     */
    public static ExcelFile openFileChooser(String startPath) {

        // Create a file chooser
        final JFileChooser fc = new JFileChooser();

        fc.setCurrentDirectory(new File(startPath));

        // Remove all FileFilters
        FileFilter[] filters = fc.getChoosableFileFilters();
        for (int i = 0; i < filters.length; i++) {
            fc.removeChoosableFileFilter(filters[i]);
        }

        fc.addChoosableFileFilter(new ExcelFileFilter());

        // In response to a button click:
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String path = fc.getSelectedFile().getAbsolutePath();
            return new ExcelFile(path);

        }

        return null;

    }

    public enum Type {
        xls,
        xlsx
    }

    /**
     * Type of Excel
     */
    private Type type;

    /**
     * Get Type of Excel
     *
     * @return
     */
    public Type getType() {
        return type;
    }

    private Workbook workbook;

    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * test ExcelFile if usable and determine type
     *
     * @return true if usable
     */
    public boolean testExcelFile() {

        try {
            if (workbook != null) {
                Sheet mySheet = workbook.getSheetAt(0);
                return true;
            }
        } catch (Exception e) {
            
            // Set to null an try again below
            workbook = null;
            
        }

        try {
            FileInputStream fis = new FileInputStream(this.getPath());
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            workbook = myWorkBook;
            type = Type.xlsx;
            return true;

        } catch (Exception ex1) {

            try {
                FileInputStream fis = new FileInputStream(this.getPath());
                HSSFWorkbook myWorkBook = new HSSFWorkbook(fis);
                HSSFSheet mySheet = myWorkBook.getSheetAt(0);

                workbook = myWorkBook;
                type = Type.xls;
                return true;

            } catch (Exception ex2) {

                workbook = null;                
                return false;

            }

        }

    }

}

class ExcelFileFilter extends FileFilter {

    // Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = FilenameUtils.getExtension(f.getAbsolutePath());
        if (extension != null) {
            if (extension.equals("xls") || extension.equals("xlsx")) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    // The description of this filter
    public String getDescription() {
        return "Excel Files";
    }
}
