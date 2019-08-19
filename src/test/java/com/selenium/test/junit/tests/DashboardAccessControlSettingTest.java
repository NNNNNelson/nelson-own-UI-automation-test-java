package com.selenium.test.junit.tests;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.selenium.test.junit.rules.ScreenShotOnFailRule;
import com.selenium.test.webtestsbase.WebDriverFactory;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sidelnikov Mikhail on 19.09.14.
 * Uses JUnit test framework
 * Test demonstrates simple webdriver functions : how to start browser, open url, insert some text and check that this text was inserted
 */
public class DashboardAccessControlSettingTest {

    @Rule
    public ScreenShotOnFailRule screenShotOnFailRule = new ScreenShotOnFailRule();

    @Before
    public void beforeTest() {
        System.setProperty("webdriver.chrome.driver", "/Users/nnnnnelson/my_tools/chromedriver");
        WebDriverFactory.startBrowser(true);
    }


    @Test
    public void testDashboardAccessControlSettingsPageModificationWillReflectToViewAllowedRolesPropertyInDefinition() throws InterruptedException {
        WebDriver driver = WebDriverFactory.getDriver();

        //Login Foglight
        driver.get("http://10.30.168.150:8080");
        WebElement userInput = driver.findElement(By.cssSelector("input#user"));
        userInput.sendKeys("foglight");
        WebElement passwordInput = driver.findElement(By.cssSelector("input#password"));
        passwordInput.sendKeys("foglight");
        passwordInput.submit();

        //Define a driver wait
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //Go to "User & Security" dashboard
        WebElement administrationNodeInNavigationPanel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@nodeid='system:administration']/span")));
        administrationNodeInNavigationPanel.click();
        WebElement usersNSecurityLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()=' Users & Security']")));
        usersNSecurityLink.click();
        WebElement dashboardAccessControlSettingsLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Dashboard Access Control Settings']/parent::a")));
        dashboardAccessControlSettingsLink.click();

        //For "Alarms Tab" view, set "Administrator" role access as "Disable"
        //Expand "Alarms" module
        WebElement alarmNodeExpandImg = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a/span[text()='Alarms']/parent::a/parent::td/preceding-sibling::td/img")));
        alarmNodeExpandImg.click();
        //Click "Alarms Tab" view "Adminitrator" role icon
        WebElement alarmsTabView_AdministratorRole_Icon = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Alarms Tab']/ancestor::td/following-sibling::td[1]/descendant::img")));
        alarmsTabView_AdministratorRole_Icon.click();
        //In popup form window, set select value to "Disable" then click "Update" button to submit change
        WebElement selectOf_alarmsTabView_AdministratorRole_Icon = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select")));
        selectOf_alarmsTabView_AdministratorRole_Icon.sendKeys("Disable");
        selectOf_alarmsTabView_AdministratorRole_Icon.submit();

        //Go to "Definition" dashboard to check "Alarm Tab" view's "Allowed Roles" property's values has removed "Administrator"
        //Expand "Configuration" node in navigation panel
        //Wait for 1 second for the popup window to disappear
        Thread.sleep(1000);
        WebElement expandIcon_Of_ConfigurationNodeInNavigationPanel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Configuration']/preceding-sibling::img")));
        expandIcon_Of_ConfigurationNodeInNavigationPanel.click();
        //Click "Definitions" node to go to "Definition" dashboard
        WebElement definitionDashboardNodeInNavigationPanel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Definitions']")));
        definitionDashboardNodeInNavigationPanel.click();
        //Choose "Alarms" node in "Definitions" panel
        WebElement alarmsNodeInDefinitionsPanel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='leftPanel']/descendant::div[@nodeid='system:core_alarms']/span[text()='Alarms']")));
        alarmsNodeInDefinitionsPanel.click();
        //Use "Search Definitions" search bar to search "Alarms Tab" view
        //Show the "Use Regular Expressions" option block
        //Wait for 1 second for "Alarms" finishes showing its views
        Thread.sleep(1000);
        //Use javascript to check (enable) the "Search Definitions" search bar "Use Regular Expressions" select
        ((JavascriptExecutor)WebDriverFactory.getDriver()).executeScript("document.querySelector(\"input[name='searchField_regex']\").checked = true;");
        //Input "Alarms Tab" string to "Search Definitions" search bar and press "ENTER" key to execute the search
        WebElement searchDefinitionsSearchBar = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Search Definitions']")));
        searchDefinitionsSearchBar.sendKeys("Alarms\\sTab");
        searchDefinitionsSearchBar.sendKeys(Keys.ENTER);
        //Find and click "Alarms Tab" view
        WebElement alarmsTabViewInSearchResult = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id=\"searchResults\"]/descendant::div[text()=\"Alarms Tab\"]")));
        alarmsTabViewInSearchResult.click();
        //In editor panel, check the "Allowed Role(s)" doesn't include "Administrator" role
        WebElement allowedRulesValueTd = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[text()='Allowed Role(s)']/following::td[1]")));
        System.out.println(allowedRulesValueTd.getText());
//        Assert.assertThat(allowedRulesValueTd.getText(), CoreMatchers.containsString("Administrator"));
        Assert.assertFalse(allowedRulesValueTd.getText().contains("Administrator"));


//        String toSearch = "Selenium";
//        WebDriverFactory.getDriver().get("http://www.youtube.com");
//        WebElement searchString = WebDriverFactory.getDriver().findElement(By.cssSelector("#masthead-search-term"));
//        searchString.sendKeys(toSearch);
//        String searchStringText = searchString.getAttribute("value");
//        assertTrue("Text from page(" + searchStringText + ") not equals to text from test(" + toSearch + ")",
//                searchStringText.equals(toSearch));
    }


//    @After
//    public void afterTest() {
//        WebDriverFactory.finishBrowser();
//    }

}
