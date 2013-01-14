package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.StartJetty;
import fi.om.municipalityinitiative.conf.PropertyNames;
import fi.om.municipalityinitiative.conf.WebTestConfiguration;
import fi.om.municipalityinitiative.dao.NEWTestHelper;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebTestConfiguration.class})
public abstract class NEWWebTestBase {

    protected static final int PORT = 8445; // NOTE: must match port in test.properties/baseUrl

    @Resource
    protected NEWTestHelper newTestHelper;
    @Resource
    protected MessageSource messageSource;

    protected Urls urls;

    protected WebDriver driver;

    @Inject
    protected Environment env;

    private static Server jettyServer;

    @BeforeClass
    public static synchronized void initialize() {
        if (jettyServer == null) {
            jettyServer = StartJetty.startService(PORT, "test");
            try {
                while (!jettyServer.isStarted()) {
                    Thread.sleep(250);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Before
    public void init() {
        if (urls == null) {
            Urls.initUrls(env.getRequiredProperty(PropertyNames.baseURL));
            urls = Urls.FI;
        }

        String driverType = env.getProperty("test.web-driver", "hu");
        System.out.println("*** driverType = " + driverType);
        switch (driverType) {
            case "ie":
                driver = new InternetExplorerDriver();
                driver.get(urls.frontpage());
                driver.navigate().to("javascript:document.getElementById('overridelink').click()"); // to skip security certificate problem page
                break;
            case "ff":
                driver = new FirefoxDriver();
                break;
            default:
                driver = new HtmlUnitDriver(true);
                break;
        }

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // default is 0!!!
        driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS); // default is 0!!!

        if (urls == null) {
            Urls.initUrls("https://localhost:" + PORT);
            urls = Urls.FI;
        }
        newTestHelper.dbCleanup();
    }
    
    /* TODO: Enable
    @After
    public void endTest() {
        if (driver != null) {
            driver.quit(); // Quits this driver, closing every associated window.
            driver = null;
        }
    }
    
    @AfterClass
    public static void destroy() {
        try {
            jettyServer.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    // Helpers

    protected void open(String href) {
        driver.get(href);
    }

    protected void assertTextByTag(String tag, String text) {
        List<WebElement> elements = driver.findElements(By.tagName(tag));
        for (WebElement element : elements) {
            assertNotNull(element);
            if (text.equals(element.getText().trim())) {
                return;
            }
        }
        System.out.println("--- assertTextByTag --------------- " + tag + ": " + text);
        for (WebElement element : elements) {
            System.out.println("*** '" + element.getText().trim() + "'");
        }
        fail(tag + " tag with text " + text + " not found");
    }
    
    protected void assertTextContainedByXPath(String xpathExpression, String text) {
        List<WebElement> elements = driver.findElements(By.xpath(xpathExpression));
        for (WebElement element : elements) {
            assertNotNull(element); 
            String elementText = element.getText().trim();
            if (elementText.contains(text)) {
                return;
            }
        }
        System.out.println("--- assertTextContainedByXPath --------------- " + xpathExpression + ": " + text);
        for (WebElement element : elements) {
            System.out.println("*** '" + element.getText().trim() + "'");
        }
        fail(xpathExpression + " xpath with text " + text + " not found");
    }

    protected String pageTitle() {
        return driver.getTitle();
    }
    protected void assertTitle(String text) {
        String title = driver.getTitle();

        System.out.println("--- assertTitle --------------- : " + text);
        System.out.println("*** '" + title.trim() + "'");
        assertThat(title, is(text));
    }

    protected void inputText(String fieldName, String text) {
        driver.findElement(By.name(fieldName)).sendKeys(text);
    }

    protected void inputTextByCSS(String css, String text) {
        driver.findElement(By.cssSelector(css)).sendKeys(text);
    }

    protected void clickByName(String name) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.name(name)));
        link.click();
    }

    protected void clickById(String id) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        link.click();
    }

    protected void clickLinkContaining(String text) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(text)));
        link.click();
    }

    protected String getPageUrl() {
        return driver.getCurrentUrl();
    }

    protected void assertValue(String fieldName, String value) {
//        WebDriverWait wait = new WebDriverWait(driver,10);
//        wait.until(pageContainsElement(By.name(fieldName)));

        assertEquals(value, driver.findElement(By.name(fieldName)).getAttribute("value"));
    }

    protected void wait100() {
        waitms(100);
    }
    protected synchronized void waitms(int timeout) {
        try {
            wait(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
