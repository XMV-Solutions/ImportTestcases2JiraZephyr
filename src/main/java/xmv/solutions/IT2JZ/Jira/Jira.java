/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmv.solutions.IT2JZ.Jira;

import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author David Koller XMV Solutions GmbH
 */
public class Jira {

    /**
     * Standard wait time in seconds
     */
    static final int STANARD_WAIT_TIMEOUT_LENGTH = 5;

    /**
     * Length to wait for Timeouts
     */
    private int waitTimoutLength;

    /**
     * ID of span with create issue type
     */
    static final String CREATE_ISSUE_TYPE__ID = "issue-create-issue-type";
    /**
     * Expected Content in create issue type
     */
    static final String CREATE_ISSUE_TYPE__EXPECTED_CONTENT = "Test";

    private URL urlTestCreatePage;

    public URL getUrlTestCreatePage() {
        return urlTestCreatePage;
    }

    /**
     * Creates an Issue in the current Window
     *
     * @param testcase
     */
    public void createIssue(JiraTestcase testcase) {

        if (urlTestCreatePage != null) {
            testcase.setStartURL(urlTestCreatePage.toString());
            testcase.create(browserSession, waitTimoutLength);
        }

    }

    /**
     * Possible Browsers to use for import
     */
    public enum Browser {
        /**
         * Use Mozilla Firefox
         */
        Firefox,
        /**
         * Use Google Chrome
         */
        Chrome,
        /**
         * Use Apple Safari
         */
        Safari,
        /**
         * Use MS Edge
         */
        Edge,
        /**
         * Use MS Internet Explorer
         */
        InternetExplorer,
        /**
         * Use Built in HTML Unit
         */
        HtmlUnitDriver
    }

    /**
     * Browser to use
     */
    private Browser browserToUse;

    /**
     * Handle for the browserToUse
     */
    private WebDriver browserSession;

    /**
     * open Browser
     *
     * @param toUse select Browser toUse from Enum
     */
    private void openBrowser() {

        try {

            switch (browserToUse) {

                case Chrome:
                    browserSession = new ChromeDriver();
                    break;

                case Edge:
                    browserSession = new EdgeDriver();
                    break;

                case InternetExplorer:
                    browserSession = new InternetExplorerDriver();
                    break;

                case Safari:
                    browserSession = new SafariDriver();
                    break;

                case Firefox:
                    browserSession = new FirefoxDriver();
                    break;

                case HtmlUnitDriver:
                default:
                    browserSession = new HtmlUnitDriver();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Construct a jira instance providing just the browser to use for
     *
     * @param browserToUse select witch browser to use
     */
    public Jira(Browser browserToUse) {
        this.browserToUse = browserToUse;
        openBrowser();
        waitTimoutLength = STANARD_WAIT_TIMEOUT_LENGTH;
    }

    /**
     * Construct a jira instance providing using built in browser
     *
     */
    public Jira() {
        this(Browser.HtmlUnitDriver);
    }

    /**
     * Test if browser handle is working
     */
    public void testBrowserOpenURL(URL url) {
        browserSession.get(url.toString());
    }

    /**
     * Tests if browser ist usable
     *
     * @return
     */
    public boolean testBrowserOpen() {

        // Open URL
        try {
            URL url = new URL("http://xmv.solutions");
            testBrowserOpenURL(url);
        } catch (Exception e) {
            return false;
        }

        try {

            return browserSession.getTitle().contains("</internet>");

        } catch (Exception e) {

            return false;
        }

    }

    /**
     * Tests if the Test Create Page is opened
     *
     * @return result of test
     */
    public boolean testCreatePageOpened() {
        try {

            // Wait until CreateIssueLayerIsReady
            (new WebDriverWait(browserSession, waitTimoutLength)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver browserSession) {
                    return browserSession.findElement(By.id(CREATE_ISSUE_TYPE__ID)).getText().contains(CREATE_ISSUE_TYPE__EXPECTED_CONTENT);
                }
            });

            // Save URL
            try {
                urlTestCreatePage = new URL(browserSession.getCurrentUrl());
            } catch (Exception e) {
                return false;
            }

            return true;

        } catch (Exception e) {

            urlTestCreatePage = null;
            return false;
        }
    }

}
