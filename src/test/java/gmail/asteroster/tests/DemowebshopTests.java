package gmail.asteroster.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static helpers.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Tag("demowebshop")
public class DemowebshopTests extends TestBase{

    static String login = "qaguru@qa.guru",
            password = "qaguru@qa.guru1",
            authCookieName = "NOPCOMMERCE.AUTH";


    @Test
    @DisplayName("Successful authorization to some demowebshop (UI)")
    void loginTest() {
        step("Open login page", () ->
                open("/login"));

        step("Fill login form", () -> {
            $("#Email").setValue(login);
            $("#Password").setValue(password)
                    .pressEnter();
        });

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithApiTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookieValue = given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
//                .body("Email=" + login + "&Password=" + password + "&RememberMe=false")
//                .body(format("Email=%s&Password=%s&RememberMe=false", login, password))
                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authCookieName);

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            step("Set cookie to to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });

        step("Open main page", () ->
                open(""));
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithApiAndAllureListenerTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookieValue = given()
                    .filter(new AllureRestAssured())
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authCookieName);

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            step("Set cookie to to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });

        step("Open main page", () ->
                open(""));
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithApiAndCustomListenerTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookieValue = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authCookieName);

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            step("Set cookie to to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });

        step("Open main page", () ->
                open(""));
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @DisplayName("")
    void addProductToCartTest() {
        /*
        curl 'http://demowebshop.tricentis.com/addproducttocart/details/72/1' -H 'Accept: *\/*' \
                -H 'Accept-Language: en-US,en;q=0.9,ru-RU;q=0.8,ru;q=0.7' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
        -H 'Cookie: ARRAffinity=92eb765899e80d8de4d490df907547e5cb10de899e8b754a4d5fa1a7122fad69; __utmc=78382081; __utmz=78382081.1654621885.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); NOPCOMMERCE.AUTH=AF92D1DFD49E790EE4CC2D0E85753DE050CB2344B20B0F115A15AEA618D6A1E25656143E241BEFA5E2618A0347D5B0D42D4669E9D47859A189847CF3921FDDF61D69B52512514D185B7DA5680BB4414B84E6007877C6ED9BC4ADCE09D9D8F2E034E8ED0D422AC960B15260B74C73DCDE10480D96CBC74322D18641CBABB3EAFA23E166B9A46E8B33AF57476AC31791CF; Nop.customer=bccc3d3b-96f1-4e02-aef5-52bd4ae25d47; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72; __RequestVerificationToken=goE8JI7wmciPGwy_5fHjIaZ2VgPu7m17jhcM5G2LWCDeIVnuvBBe-1UmRa9jYFZUd7dODRaoRLS5J-CvyMvrpYzq0Ifv2Urf-YAKFLEuISM1; __utma=78382081.1686277131.1654621885.1654625676.1654880801.3; __utmt=1; __atuvc=3%7C23; __atuvs=62a37ab038eec4e9001; __utmb=78382081.3.10.1654880801' \
        -H 'Origin: http://demowebshop.tricentis.com' \
        -H 'Referer: http://demowebshop.tricentis.com/build-your-cheap-own-computer' \
        -H 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36' \
        -H 'X-Requested-With: XMLHttpRequest' \
        --data-raw 'product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=20' \
        --compressed \
        --insecure
         */

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";
        String authCookieValue = "AF92D1DFD49E790EE4CC2D0E85753DE050CB2344B20B0F115A15AE" +
                "A618D6A1E25656143E241BEFA5E2618A0347D5B0D42D4669E9D47859A189847CF3921FDDF61D69B52512514D185B" +
                "7DA5680BB4414B84E6007877C6ED9BC4ADCE09D9D8F2E034E8ED0D422AC960B15260B74C73DCDE10480D96CBC743" +
                "22D18641CBABB3EAFA23E166B9A46E8B33AF57476AC31791CF";

        String cartSize = given()
                .filter(withCustomTemplates())
                .contentType("application/x-www-form-urlencoded")
                .body(body)
                .cookie(authCookieName, authCookieValue)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("updateflyoutcartsectionhtml", notNullValue())
                .body("updatetopcartsectionhtml", notNullValue())
                .extract()
                .path("updatetopcartsectionhtml");

        step("Open minimal content, because cookie can be set when site is opened", () ->
                open("/Themes/DefaultClean/Content/images/logo.png"));
        step("Set cookie to to browser", () -> {
            Cookie authCookie = new Cookie(authCookieName, authCookieValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });

        step("Open main page", () ->
                open(""));
        step("Verify cart size", () ->
                $("#topcartlink .cart-qty").shouldHave(text(cartSize)));
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("")
    void addProductToCartWithDynamicCookieTest() {
        String authCookieValue = given()
                .filter(withCustomTemplates())
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", login)
                .formParam("Password", password)
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract().cookie(authCookieName);

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";

        String cartSize = given()
                .filter(withCustomTemplates())
                .contentType("application/x-www-form-urlencoded")
                .body(body)
                .cookie(authCookieName, authCookieValue)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("updateflyoutcartsectionhtml", notNullValue())
                .body("updatetopcartsectionhtml", notNullValue())
                .extract()
                .path("updatetopcartsectionhtml");

        step("Open minimal content, because cookie can be set when site is opened", () ->
                open("/Themes/DefaultClean/Content/images/logo.png"));
        step("Set cookie to to browser", () -> {
            Cookie authCookie = new Cookie(authCookieName, authCookieValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });

        step("Open main page", () ->
                open(""));
        step("Verify cart size", () ->
                $("#topcartlink .cart-qty").shouldHave(text(cartSize)));
    }
}
