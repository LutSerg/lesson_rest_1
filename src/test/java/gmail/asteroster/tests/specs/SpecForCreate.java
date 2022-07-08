package gmail.asteroster.tests.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class SpecForCreate {

    static String createRq = "{\n" + " \"name\": \"morpheus\",\n" + " \"job\": \"leader\"\n" + "}";
    public static RequestSpecification createRequest = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON)
            .body(createRq);

    public static ResponseSpecification responseCreateSpec = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(LogDetail.ALL)
//            .expectBody(containsString("success"))
            .build();
}
