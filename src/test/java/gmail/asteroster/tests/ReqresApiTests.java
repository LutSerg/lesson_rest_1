package gmail.asteroster.tests;

import gmail.asteroster.tests.lombok.CreateUser;
import gmail.asteroster.tests.lombok.LombokCreateUser;
import gmail.asteroster.tests.lombok.LombokUserData;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import static gmail.asteroster.tests.specs.SpecForCreate.createRequest;
import static gmail.asteroster.tests.specs.SpecForCreate.responseCreateSpec;
import static gmail.asteroster.tests.specs.Specs.request;
import static gmail.asteroster.tests.specs.Specs.responseSpec;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresApiTests {

    @Test
    void singleUserTest() {
      LombokUserData data = given()
                .spec(request)
                .when()
                .get("users/2")
                .then()
                .spec(responseSpec)
                .body("data.first_name", is("Janet"))
              .extract().as(LombokUserData.class);
        assertEquals(2, data.getUser().getId());
        assertThat(data.getUser().getFirstName(), is(equalTo("Janet")));
    }

    @Test
    void userNotFound() {
        given()
                .spec(request)
                .when()
                .get("users/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    void createUserTest() {
        String createRq = "{\n" + " \"name\": \"morpheus\",\n" + " \"job\": \"leader\"\n" + "}";

        given()
                .spec(request)
                .body(createRq)
                .when()
                .post("api/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", is("morpheus"));
    }

    @Test
    void createUserTestWithLombokModel() {
        LombokCreateUser data = given()
                .spec(createRequest)
                .when()
                .post("/users")
                .then()
                .spec(responseCreateSpec)
                .extract().as(LombokCreateUser.class);
            }

    @Test
    void deleteTest () {
        delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .statusCode(204);
    }

    @Test
    void listUsersTest () {
        Integer pages = get("https://reqres.in/api/users?page=2")

                .then()
                .extract().path("total_pages");

        System.out.println(pages);
        Integer expectedPages = 2;
        assertEquals(expectedPages, pages);
    }

        @Test
        void testListUser(){
            given()
                    .spec(request)
                    .when()
                    .get("/users?page=2")
                    .then()
                    .spec(responseSpec)
                    .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                            hasItem("byron.fields@reqres.in"));

    }
}
