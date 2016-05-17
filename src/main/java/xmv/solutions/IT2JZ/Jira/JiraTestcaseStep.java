/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmv.solutions.IT2JZ.Jira;

/**
 *
 * @author David Koller XMV Solutions GmbH
 */
public class JiraTestcaseStep {

	/**
	 * Name of Step (e.g. "Step 1")
	 */
	private String stepName;
	
	/**
	 * Action to give in actual step (e.g. "Press red button")
	 */
	private String testData;
	
	/**
	 * Expected result of action for current step (e.g. "Popup is shown saying 'Done!'")
	 */
	private String expectedResult;

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}
		
}