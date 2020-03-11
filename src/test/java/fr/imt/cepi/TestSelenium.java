package fr.imt.cepi;


import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

public class TestSelenium {
    private WebDriver driver;

    @BeforeClass
    public static void setupClass() {

        FirefoxDriverManager.firefoxdriver().setup();
//        ChromeDriverManager.getInstance().setup();
    }

    @Before
    public void setUp() {

        driver = new FirefoxDriver();
//        driver = new ChromeDriver();
//        driver = new SafariDriver();
    }

    @After
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }

    @Test
    public void connectOK() throws Exception {
        driver.get("http://localhost:8080/MyServlet/");

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("connect")));

        driver.findElement(By.name("login")).sendKeys("bertin");
        driver.findElement(By.name("password")).sendKeys("dominique");
        driver.findElement(By.name("connect")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.jumbotron")));

        assertEquals("Accueil", driver.getTitle());
        assertEquals("Bonjour BERTIN Dominique", driver.findElement(By.tagName("h1")).getText());
    }
}

