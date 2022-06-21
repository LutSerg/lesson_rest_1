package gmail.asteroster.tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresApiTests {

    @Test
    void singleUserTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().body()
                .log().status()
                .statusCode(200)
                .body("data.first_name", is("Janet"));
    }

    @Test
    void userNotFound() {
        get("https://reqres.in/api/users/23")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    void createUserTest() {
        String createRq = "{\n" + " \"name\": \"morpheus\",\n" + " \"job\": \"leader\"\n" + "}";

        given()
                .log().uri()
                .log().body()
                .body(createRq)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"));
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
}
