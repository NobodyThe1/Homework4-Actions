import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionsTest {

    String login = "otus-test@mail.ru";
    String password = "1Testtest+";

    private WebDriver driver;
    private Actions action;
    private WebDriverWait wait;

    @BeforeAll
    public static void open() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void init() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        action = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    @AfterEach
    public void close() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @Test
    public void otusLKTest() throws InterruptedException {

        otusAuthorise();

        driver.findElement(By.cssSelector(".input[name='fname']")).clear();
        driver.findElement(By.cssSelector(".input[name='fname']")).sendKeys("Иван");
        driver.findElement(By.cssSelector(".input[name='lname']")).clear();
        driver.findElement(By.cssSelector(".input[name='lname']")).sendKeys("Иванов");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".input[name='fname_latin']")));
        driver.findElement(By.cssSelector(".input[name='fname_latin']")).clear();
        driver.findElement(By.cssSelector(".input[name='fname_latin']")).sendKeys("Ivan");
        driver.findElement(By.cssSelector(".input[name='lname_latin']")).clear();
        driver.findElement(By.cssSelector(".input[name='lname_latin']")).sendKeys("Ivanov");
        driver.findElement(By.cssSelector(".input[name='blog_name']")).clear();
        driver.findElement(By.cssSelector(".input[name='blog_name']")).sendKeys("Иван");
        driver.findElement(By.cssSelector(".input[name='date_of_birth']")).clear();
        driver.findElement(By.cssSelector(".input[name='date_of_birth']")).sendKeys("11.11.1991");
        action.moveToElement(driver.findElement(By.cssSelector(".js-lk-cv-dependent-master"))).click().perform();
        driver.findElement(By.cssSelector("button.lk-cv-block__select-option[title='Россия']")).click();
        wait.until(ExpectedConditions.attributeContains(By.cssSelector(".js-lk-cv-dependent-master > div"), "class", "hide"));
        action.moveToElement(driver.findElement(By.cssSelector(".js-lk-cv-dependent-slave-city"))).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By.cssSelector
                (".js-lk-cv-dependent-slave-city > div"), "class", "hide")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".js-custom-select-option[title='Москва']")));
        driver.findElement(By.cssSelector(".js-custom-select-option[title='Москва']")).click();
        action.moveToElement(driver.findElement(By.cssSelector("[name='english_level'] ~ div"))).click().perform();
        driver.findElement(By.cssSelector("button.lk-cv-block__select-option[title='Средний (Intermediate)']")).click();

        action.moveToElement(driver.findElement(By.cssSelector("input[name='contact-0-service']~div"))).click().perform();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#id_contact-0-value")));
        driver.findElement(By.cssSelector("#id_contact-0-value")).clear();
        driver.findElement(By.cssSelector("[data-num] [data-selected-option-class] label ~ *:not(.hide) button[title='WhatsApp']")).click();
        driver.findElement(By.cssSelector("#id_contact-0-value")).sendKeys("+7 999 123 45 67");
        driver.findElement(By.cssSelector(".js-lk-cv-custom-select-add")).click();
        action.moveToElement(driver.findElement(By.cssSelector("input[name='contact-1-service']~div"))).click().perform();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#id_contact-1-value")));
        driver.findElement(By.cssSelector("#id_contact-1-value")).clear();
        driver.findElement(By.cssSelector("[data-num] [data-selected-option-class] label ~ *:not(.hide) button[data-value='telegram']")).click();
        driver.findElement(By.cssSelector("#id_contact-1-value")).sendKeys("+7 999 876 54 32");
        driver.findElement(By.cssSelector(".button_blue.lk-cv-action-buttons__button.js-disable-on-submit")).submit();

        driver.manage().deleteAllCookies();
        driver.navigate().refresh();

        otusAuthorise();

        Assertions.assertEquals("Иван", driver.findElement(By.cssSelector(".input[name='fname']")).getAttribute("value"));
        Assertions.assertEquals("Иванов", driver.findElement(By.cssSelector(".input[name='lname']")).getAttribute("value"));
        Assertions.assertEquals("Ivan", driver.findElement(By.cssSelector(".input[name='fname_latin']")).getAttribute("value"));
        Assertions.assertEquals("Ivanov", driver.findElement(By.cssSelector(".input[name='lname_latin']")).getAttribute("value"));
        Assertions.assertEquals("Иван", driver.findElement(By.cssSelector(".input[name='blog_name']")).getAttribute("value"));
        Assertions.assertEquals("11.11.1991", driver.findElement(By.cssSelector(".input[name='date_of_birth']")).getAttribute("value"));

        Assertions.assertEquals("Россия", driver.findElement(By.cssSelector(".js-lk-cv-dependent-master")).getText());
        Assertions.assertEquals("Москва", driver.findElement(By.cssSelector(".js-lk-cv-dependent-slave-city")).getText());
        Assertions.assertEquals("Средний (Intermediate)", driver.findElement(By.cssSelector("[name='english_level'] ~ div")).getText());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#id_contact-0-value")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#id_contact-1-value")));
        Assertions.assertEquals("+7 999 876 54 32", driver.findElement(By.cssSelector("#id_contact-0-value")).getAttribute("value"));
        Assertions.assertEquals("+7 999 123 45 67", driver.findElement(By.cssSelector("#id_contact-1-value")).getAttribute("value"));
    }

    public void otusAuthorise() {

        driver.get("https://otus.ru/");
        driver.findElement(By.cssSelector(".header3__button-sign-in-container")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-container[data-modal-id='new-log-reg']:not(.hide-top)")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[type='text'][name='email']:not(.hide)")));
        driver.findElement(By.cssSelector("[type='text'][name='email']:not(.hide)")).sendKeys(login);
        driver.findElement(By.cssSelector(".new-input[name='password']")).sendKeys(password);
        driver.findElement(By.cssSelector(".new-button[type='submit']")).submit();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".header3__user-info-avatar")));
        action.moveToElement(driver.findElement(By.cssSelector(".header3__user-info-avatar"))).perform();
        driver.findElement(By.cssSelector("div.header3__user-info-popup-links > a")).click();
    }
}
