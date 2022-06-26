package gmail.asteroster.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.CredentialsConfig;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.aeonbits.owner.ConfigFactory;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    @BeforeAll
    public static void setUp() {
        CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class);
        String  authCookieName = "NOPCOMMERCE.AUTH";

        String selenoid = System.getProperty("selenoidUrl", "selenoid.autotests.cloud/wd/hub");

        Configuration.baseUrl = System.getProperty("baseUrl", "http://demowebshop.tricentis.com");
        RestAssured.baseURI = System.getProperty("baseURI", "http://demowebshop.tricentis.com");
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.remote = "https://" + config.login() + ":" + config.password() + "@" + selenoid;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    public void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }
}
