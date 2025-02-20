package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.idealized.Javascript;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;

	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("web Driver   Instance  is created ");
	}

	// Method to click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			applyBorder(by, "green");
			ExtentManager.logStep("Clicked an element--->" + elementDescription);
			driver.findElement(by).click();
			logger.info("Clicked an element--->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click the element",
					elementDescription + "_unable to click");
			logger.error("Unable to click the element " + e.getMessage());

		}
	}

	// Scroll to an element
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to locate elements" + e.getMessage());
		}
	}

	// Method to pass value to input field
	public void enterValue(By by, String value) {
		try {
			waitForElementToBevisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			/*
			 * To avoid this create a webElement so driver.findElement(by).clear();
			 * driver.findElement(by).sendKeys(message);
			 */
			element.clear();

			element.sendKeys(value);
			logger.info("Entered value on the " + getElementDescription(by) + "-- " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("\"Unable to enter the text\" + e.getMessage()");
		}
	}

	// Method to get value to input field
	public String getValue(By by) {
		try {
			waitForElementToBevisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();
		} catch (Exception e) {

			applyBorder(by, "red");
			logger.error("Unable to get the text" + e.getMessage());
		}
		return "";
	}

	// Method to compare the two text ...changing the return type from void to
	// boolean
	public boolean compareText(By by, String expectedText) {
		try {
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text Verified successfully" + actualText + " equals " + expectedText);
				logger.info("Text values- Actual text:" + actualText + " equals the Expected Text - " + expectedText
						+ " - Matching");
				return true;
			} else {
				applyBorder(by, "red");
				ExtentManager.logFailure(BaseClass.getDriver(), "Compare Text",
						"Text comparison failed" + actualText + " not equals " + expectedText);
				logger.error("Text values- Actual text:" + actualText + " equals the Expected Text - " + expectedText
						+ " - not matching");
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to compare text " + e.getMessage());
			return false;
		}
	}

	// Method to check if an element is displayed
	public boolean isDisplayed(By by) {

		/*
		 * Instead of having a variable and if conditions we can directly implement the
		 * logic waitForElementToBevisible(by);
		 *
		 * try { boolean isDisplayed = driver.findElement(by).isDisplayed();
		 * if(isDisplayed) { System.out.println("Element is displayed"); return
		 * isDisplayed; } else { return isDisplayed; } } catch (Exception e) {
		 * System.out.println("Element not displayed"+e.getMessage()); return false; }
		 */

		try {
			waitForElementToBevisible(by);
			applyBorder(by, "green");
			logger.info("Element is  displayed " + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is  displayed ",
					"Element located using " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();

		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed ",
					"Element is not displayed " + getElementDescription(by));
			logger.error("Element is not displayed " + e.getMessage());
			return false;
		}

	}

	// wait for the expected element to be clickable
	private void waitForElementToBeClickable(By by) {

		try {

			wait.until(ExpectedConditions.elementToBeClickable(by));
			// ExtentManager.logStep("Element is displayed");
		} catch (Exception e) {
			logger.error("Element not clickable" + e.getMessage());
		}
	}

	// wait for element to be visible
	private void waitForElementToBevisible(By by) {

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			// ExtentManager.logStep("Element is visible");
		} catch (Exception e) {
			logger.error("Element not visible" + e.getMessage());
		}
	}

	// wait for page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readystate").equals("complete"));
			logger.info("Page Loaded successfully");
		} catch (Exception e) {
			logger.error("Page not Loaded within" + timeOutInSec + "seconds . Exception" + e.getMessage());
		}
	}

	// Method to get the description of an element using by locator
	public String getElementDescription(By locator) {
		// check for null driver or locator to avoid null pointer exception
		if (driver == null)
			return "Driver is null ";

		if (locator == null)
			return "Locator is null ";

		// find element using the locator
		WebElement element = driver.findElement(locator);

		try {
			// Get Element Attributes
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			// Return the description based on element attributes
			if (isNotEmpty(name)) {
				return "Element with name : " + name;
			} else if (isNotEmpty(id)) {
				return "Element with id : " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text : " + truncateText(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with classname : " + className;
			} else if (isNotEmpty(placeHolder)) {
				return "Element with placeholder : " + placeHolder;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("unable to describe the element" + e.getMessage());
		}
		// returns the xpath/locator if not other description found;
		return locator.toString();
	}

	// utility method to check the string is not null or empty
	private boolean isNotEmpty(String value) {

		return value != null && !value.isEmpty();

	}

	// utility method to truncate long strings
	private String truncateText(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";

	}

	// utility method to border an element
	public void applyBorder(By by, String color) {

		try {
			// Locate the element
			WebElement element = driver.findElement(by);
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color " + color + "to element" + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element " + getElementDescription(by), e.getMessage());
		}

	}

	// ----SelectMethods
	// Method to select a dropdown by visible text
	public void selectByVisibleText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by, "green");
			logger.info("Selected drop down by visible text :" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to select drop down by visible text :" + value, e);
		}

	}

	// Method to select a drop down by value

	public void selectByValue(By by, String value) {

		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByValue(value);
			applyBorder(by, "green");
			logger.info("Selected drop down by actual value:" + value);

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to select drop down by value:" + value, e);

		}

	}

//Method to select a drop down by index
	public void selectByVisibleText(By by, int index) {

		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByIndex(index);
			applyBorder(by, "green");
			logger.info("Selected drop down by index:" + index);

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to select drop down by index:" + index, e);

		}

	}

	// Methods to get all the options form drop down

	public List<String> getDropdownOptions(By by) {
		List<String> dropdowOptions = new ArrayList<>();
		try {
			WebElement dropdownElement = driver.findElement(by);
			Select select = new Select(dropdownElement);
			for (WebElement option : select.getOptions()) {
				dropdowOptions.add(option.getText());

			}
			applyBorder(by, "green");
			logger.info("Retrieved drop down options for" + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to retrieved drop down options " + e.getMessage());

		}
		return dropdowOptions;

	}

	// -------- Java Script utility Methods

	public void clickUsingJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("argument[0].click();", element);
			applyBorder(by, "green");
			logger.info("Clicked an element using Java script executor " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to click using JavaScript executor " + e.getMessage());

		}

	}

	// Method to scroll to the bottom of the page

	public void scrollToBottom(By by) {

		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
		logger.info("Scrolled to the bottom of the page");

	}

	// Method to Highlight an element using JavaScript

	public void highlightElementJS(By by) {
		try {

		} catch (Exception e) {

		}
	}

	// ---Windows and Frame Handling

	// Method to Switch between browser windows
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					logger.info("Switched to window :" + windowTitle);
					return;
				}
			}
			logger.error("Window with title " + windowTitle + " not found");
		} catch (Exception e) {
			logger.error("unale to switch window " + e.getMessage());
		}

	}

	// Method to switch to an iframe

	public void switchToFrame(By by) {
		try {

			driver.switchTo().frame(driver.findElement(by));
			logger.info("Switched to iFrame:" + getElementDescription(by));

		} catch (Exception e) {
			logger.error("unable to switch to iframe", e);
		}
	}

	// Method to switch to default content

	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
		logger.info("Switched back to default content");
	}

	// -------Alert Handling----

	// Method to accept an alert pop up
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert Accepted");
		} catch (Exception e) {
			logger.error("No alert found to accept", e);
		}
	}

	// Method to dismiss alert pop up

	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert Dismissed ");
		} catch (Exception e) {
			logger.error("No alert found to dismiss", e);
		}
	}

	// Method to get Alert Text

	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();

		} catch (Exception e) {
			logger.error("No alert text found", e);
			return "";

		}
	}

	// ----------Browser Actions ------

	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentManager.logStep("Page refreshed successfully");
			logger.info("Page refreshed successfully ");
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "unable to refresh page ", "refresh error");
			logger.error("unable to refresh page" + e.getMessage());
		}
	}

	public String getCurrentURL() {
		try {
			String url = driver.getCurrentUrl();
			ExtentManager.logStep("Current URL fetched" + url);
			logger.info("Current URL fetched" + url);
			return url;
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to fetch url ", "URL error ");
			logger.info("Unable to fetch url " + e.getMessage());
			return null;
		}
	}

	public void maximizeWindow() {
		try {
			driver.manage().window().maximize();
			ExtentManager.logStep("Browser window maximised");
			logger.info("Browser window maximised");
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to maximize window", "Maximize window error");
			logger.info("Unable to maximize window" + e.getMessage());
		}
	}

	// ------Advanced Web Element ------

	public void moveToElement(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(by)).perform();
			ExtentManager.logStep("Moved to and element" + elementDescription);
			logger.info("Moved to and element --- " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to an element",
					"unable to move to an element");
			logger.error("Unable to move to an element" + e.getMessage());
		}

	}

	public void dragAndDrop(By source, By target) {

		String sourceDescription = getElementDescription(source);
		String targetDescription = getElementDescription(target);
		try {
			Actions actions = new Actions(driver);
			actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
			ExtentManager.logStep("Dragged Element :" + sourceDescription + " and Dropped :" + targetDescription);
			logger.info("Dragged Element :" + sourceDescription + " and Dropped :" + targetDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "unable to drag and drop", "source and target");
			logger.error("unable to drag an drop :" + e.getMessage());
		}

	}

	public void doubleClick(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.doubleClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Double Clicked on  element :" + elementDescription);
			logger.info("Double clicked on element --- " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to double click element", "unable to double click");
			logger.error("Unable to double click  element" + e.getMessage());
		}

	}

	public void rightClick(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.contextClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Right-Clicked on  element :" + elementDescription);
			logger.info("Right-clicked on element --- " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to Right- click element", "unable to Right- click");
			logger.error("Unable to Right-click  element" + e.getMessage());
		}

	}

	public void sendKeysWithActions(By by, String value) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.sendKeys(driver.findElement(by), value).perform();
			ExtentManager.logStep("sent keys to element--->" + elementDescription + "|value: " + value);
			logger.info("sent keys to element--->" + elementDescription + "|value: " + value);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to send keys to element", elementDescription);
			logger.error("Unable to send keys to element" + e.getMessage());
		}

	}

	public void clearText(By by) {
		String elementDescription = getElementDescription(by);
		try {
			driver.findElement(by).clear();
			ExtentManager.logStep("Cleared text in element: " + elementDescription);
			logger.info("Cleared text in element: " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "unable to clear the text in element", elementDescription);
			logger.error("unable to clear the text in element" + e.getMessage());
		}

	}

	// Method to upload a file
	public void uploadFile(By by, String filePath) {
		try {
			driver.findElement(by).sendKeys(filePath);
			applyBorder(by, "green");
			logger.info("Uploaded File :" + filePath);

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to upload file " + e.getMessage());
		}

	}
}
