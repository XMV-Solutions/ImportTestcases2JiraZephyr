/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmv.solutions.IT2JZ.Jira;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xmv.solutions.commons.ExcelFile;

/**
 *
 * @author David Koller XMV Solutions GmbH
 */
public class Excel2TestcaseConverter {

    /**
     * Array of Testcases
     */
    private List<JiraTestcase> testcases;

    public List<JiraTestcase> getTestcases() {
        return testcases;
    }
    
    /**
     * FieldMappings
     */
    private String summaryFM;
    private String descriptionFM;
    private String testStepNameFM;
    private String testStepDataFM;
    private String testStepExpectedResultFM;

    /**
     * Excel File
     */
    ExcelFile excelFile;

    /**
     * Constructor
     * @param summaryFM
     * @param descriptionFM
     * @param testStepNameFM
     * @param testStepDataFM
     * @param testStepExpectedResultFM
     * @param excelFile 
     */
    public Excel2TestcaseConverter(String summaryFM, String descriptionFM, String testStepNameFM, String testStepDataFM, String testStepExpectedResultFM, ExcelFile excelFile) {
        this.summaryFM = summaryFM;
        this.descriptionFM = descriptionFM;
        this.testStepNameFM = testStepNameFM;
        this.testStepDataFM = testStepDataFM;
        this.testStepExpectedResultFM = testStepExpectedResultFM;
        this.excelFile = excelFile;
    }    

    public List<JiraTestcase> parse() {

        // Reset first
        testcases = new ArrayList<JiraTestcase>();
        
        // Check Excel File
        if(excelFile == null || !excelFile.testExcelFile()) {
            return null;
        }
        
        try {

            // Get the workbook instance for XLS file
            Workbook workbook = excelFile.getWorkbook();

            for(Sheet sheet : workbook){
            
                String Sheetname = "";

                try {

                    // Take Sheet next sheet
                    Sheetname = sheet.getSheetName();

                } catch (Exception e) {

                    // Problem with Excelfile
                    break;
                }

                // Initiate TestSet
                String summary = null;
                String description = null;
                List<JiraTestcaseStep> steps = new ArrayList<JiraTestcaseStep>();
                JiraTestcaseStep step = null;

                for (Row row : sheet) {

                    // Omitt first line or empty ones
                    if (row.getRowNum() == 0 || row == null || row.getLastCellNum() < 1) {
                        continue;
                    }

                    try {

                        // Scan Columns
                        String A = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
                        String B = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
                        String C = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
                        String D = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";
                        String E = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
                        String F = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";

                        // if the First coulumn isn't empty, we start a new step
                        if (!A.isEmpty()) {

                            // finnish last testcase if we can, respctl all
                            // attributes are ready
                            if (summary != null && description != null && !steps.isEmpty()) {

                                // Generate testcase and add to List
                                testcases.add(new JiraTestcase(summary, description, steps));

                                // Reset
                                summary = null;
                                description = null;
                                step = null;
                                steps = new ArrayList<JiraTestcaseStep>();

                            }

                            // Start new Testcase
                            summary = parseMapping(summaryFM, A, B, C, D, E, F, Sheetname);
                            description = parseMapping(descriptionFM, A, B, C, D, E, F, Sheetname);;

                        }

                        step = new JiraTestcaseStep();
                        step.setStepName(parseMapping(testStepNameFM, A, B, C, D, E, F, Sheetname));
                        step.setTestData(parseMapping(testStepDataFM, A, B, C, D, E, F, Sheetname));
                        step.setExpectedResult(parseMapping(testStepExpectedResultFM, A, B, C, D, E, F, Sheetname));

                        steps.add(step);

                    } catch (Exception e) {

                        e.printStackTrace();
                        break;

                    }

                }

                // Generate very last testcase and add to List
                if (summary != null && description != null && !steps.isEmpty()) {
                    testcases.add(new JiraTestcase(summary, description, steps));
                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return testcases;

    }

    /**
     * Fill string according to mapping patterns
     * @param mapping
     * @param A
     * @param B
     * @param C
     * @param D
     * @param E
     * @param F
     * @param Sheetname
     * @return String with replaced patterns
     */
    private String parseMapping(String mapping, String A, String B, String C, String D, String E, String F, String Sheetname) {

        // To reduce risk of replacing "$*" in the source strings
        String unique = UUID.randomUUID().toString();
        mapping = mapping.replace("$", unique + "__");
        
        // Replace Patterns
        mapping = mapping.replace(unique + "__A", A);
        mapping = mapping.replace(unique + "__B", B);
        mapping = mapping.replace(unique + "__C", C);
        mapping = mapping.replace(unique + "__D", D);
        mapping = mapping.replace(unique + "__E", E);
        mapping = mapping.replace(unique + "__F", F);
        mapping = mapping.replace(unique + "__SHEET", Sheetname);

        return mapping;
        
    }

}
