/*
 * To change this license header, choose License Headers in Project Settings.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmv.solutions.IT2JZ.GUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author David Koller XMV Solutions GmbH
 */
public class Settings extends java.util.Properties {

    public enum Keys {
        Browser,
        SummaryFieldMapping,
        DescriptionFieldMapping,
        TestStepNameFieldMapping,
        TestDataFieldMapping,
        ExpectedResultFieldMapping,
        WaitForUserInteractions,
        WaitForSitesToLoad,
        WaitForAjax,
        ExcelFilePath
    }
    
    /**
     * Static singelton;
     */
    private static Settings settings;

    /**
     * Path to the settings File
     */
    private static final String SETTINGS_PATH = 
            new File(
                     System.getProperty("java.io.tmpdir"),
                    ".IT2JZ.properties"
            ).getAbsolutePath();

    /**
     * Singelton Delivery of Settings Instance
     *
     * @return Settings Object
     */
    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }

    private Settings() {
        
        try {
            FileInputStream fis = new FileInputStream(SETTINGS_PATH);
            super.loadFromXML(fis);
            fis.close();
        } catch (Exception e) {
            System.err.println("Settingspath " + SETTINGS_PATH + " not writeable");
        }

    }
    
    /**
     * Stores changed properties
     */
    public void store() {
            
        try (FileOutputStream fos = new FileOutputStream(SETTINGS_PATH)){
            super.storeToXML(
                    fos, 
                    new SimpleDateFormat("yyyyMMdd_HHmmss").
                            format(Calendar.getInstance().getTime()));
            fos.close();
        } catch (Exception e) {
            System.err.println("Settingspath " + SETTINGS_PATH + " not writeable");
        }
        
    }

}
