/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmv.solutions.IT2JZ.Jira;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author David Koller XMV Solutions GmbH
 */
public class JiraTestcase {

    private String description;
    private String summary;
    private List<JiraTestcaseStep> steps;
    private String urlForCreatingTestcase;

    private static final String SUMMARY_FIELD__ID = "summary";
    private static final String DESCRIPTION_FIELD__ID = "description";
    private static final String ASSIGN_TO_ME_BUTTON__ID = "assign-to-me-trigger";
    
    private static final String STEP_FIELD__NAME = "step";
    private static final String DATA_FIELD__NAME = "data";
    private static final String RESULT_FIELD__NAME = "result";
    

    public JiraTestcase(String Summary, String Description, List<JiraTestcaseStep> Steps) {
        summary = Summary;
        description = Description;
        steps = Steps;
    }

    protected void create(WebDriver seleniumDriver, int timeOut) {

        try {

            // Open Create Issue Layer
            seleniumDriver.get(urlForCreatingTestcase);

            // Wait until Summary is active
            (new WebDriverWait(seleniumDriver, timeOut)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver seleniumDriver) {
                    return seleniumDriver.findElement(By.id(SUMMARY_FIELD__ID)).isEnabled();
                }
            });

            // Assign to current User
            seleniumDriver.findElement(By.id(ASSIGN_TO_ME_BUTTON__ID)).click();
            seleniumDriver.findElement(By.id(ASSIGN_TO_ME_BUTTON__ID)).click();
            seleniumDriver.findElement(By.id(ASSIGN_TO_ME_BUTTON__ID)).click();
            
            // Set Summary
            seleniumDriver.findElement(By.name(SUMMARY_FIELD__ID)).sendKeys(summary);

            // Wait until Description is active
            (new WebDriverWait(seleniumDriver, timeOut)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver seleniumDriver) {
                    return seleniumDriver.findElement(By.id(DESCRIPTION_FIELD__ID)).isEnabled();
                }
            });

            // Set Description
            seleniumDriver.findElement(By.id(DESCRIPTION_FIELD__ID)).sendKeys(description);

            // Assign to current User
            seleniumDriver.findElement(By.id(ASSIGN_TO_ME_BUTTON__ID)).click();
            seleniumDriver.findElement(By.id(ASSIGN_TO_ME_BUTTON__ID)).click();
            seleniumDriver.findElement(By.id(ASSIGN_TO_ME_BUTTON__ID)).click();

            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }

            // Create
            seleniumDriver.findElement(By.name(SUMMARY_FIELD__ID)).submit();

            // Wait until new Window is loaded --> step field is ready
            (new WebDriverWait(seleniumDriver, timeOut)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver seleniumDriver) {
                    try {
                        return seleniumDriver.findElement(By.name(STEP_FIELD__NAME)).getText().isEmpty()
                                && seleniumDriver.findElement(By.name(STEP_FIELD__NAME)).isEnabled();
                    } catch (Exception e) {
                        return false;
                    }

                }
            });

            // Set Label
            // TODO Set Label in Testcase
            int stepNo = 0;

            // Add Steps
            for (JiraTestcaseStep step : steps) {

                stepNo++;
                if (step.getStepName().isEmpty()) {
                    step.setStepName("Step " + stepNo);
                }

                // Ommits text from beeing interpreted as formattings in JIRA
                String noformatBegin = "{noformat:bgColor=transparent| borderColor=transparent}\n";
                String noformatEnd = "\n{noformat}";

                // Adding step description
                seleniumDriver.findElement(By.name(STEP_FIELD__NAME)).sendKeys(noformatBegin + step.getStepName() + noformatEnd);
                seleniumDriver.findElement(By.name(DATA_FIELD__NAME)).sendKeys(noformatBegin + step.getTestData() + noformatEnd);
                seleniumDriver.findElement(By.name(RESULT_FIELD__NAME))
                        .sendKeys(noformatBegin + step.getExpectedResult() + noformatEnd);

                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                }

                // Add
                seleniumDriver.findElement(By.name(RESULT_FIELD__NAME)).submit();

                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }

                // Wait until new Window is loaded --> step field is ready
                (new WebDriverWait(seleniumDriver, timeOut)).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver seleniumDriver) {
                        try {
                            return seleniumDriver.findElement(By.name(STEP_FIELD__NAME)).getText().isEmpty()
                                    && seleniumDriver.findElement(By.name(STEP_FIELD__NAME)).isEnabled();
                        } catch (Exception e) {
                            return false;
                        }

                    }
                });

            }

        } catch (Exception e) {

            e.printStackTrace();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
            }

        }

    }

    public void setStartURL(String UrlForCreatingTestcase) {
        urlForCreatingTestcase = UrlForCreatingTestcase;
    }

}
