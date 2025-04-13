import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.assertj.core.api.SoftAssertions;
import org.example.models.User;
import org.example.requests.TodoRequest;
import org.example.requests.TodoRequester;
import org.example.specs.request.RequestSpec;
import org.example.storages.TestDataStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.example.models.Todo;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected SoftAssertions softly;
    protected TodoRequester todoRequester;
    @BeforeAll
    public static void setup() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9090;
    }

    @BeforeEach
//    public void setupTest(){
//        todoRequester = new TodoRequester(RequestSpec.authSpecAsAdmin());
//        softly = new SoftAssertions();
//    }
//
//    protected void createTodo(Todo todo) {
//        given()
//                .contentType("application/json")
//                .body(todo)
//                .when()
//                .post("/todos")
//                .then()
//                .statusCode(201);
//    }
//
//    protected void deleteAllTodos() {
//
//        Todo[] todos = given()
//                .when()
//                .get("/todos")
//                .then()
//                .statusCode(200)
//                .extract()
//                .as(Todo[].class);
//
//        for (Todo todo : todos) {
//            given()
//                    .auth()
//                    .preemptive()
//                    .basic("admin", "admin")
//                    .when()
//                    .delete("/todos/" + todo.getId())
//                    .then()
//                    .statusCode(204);
//        }
//    }
//
//    protected Todo[] getAllTodos() {
//        return given()
//                .contentType("application/json")
//                .when()
//                .get("/todos")
//                .then()
//                .statusCode(200)
//                .extract().as(Todo[].class);
//    }

    public void setupTest() {
        todoRequester = new TodoRequester(RequestSpec.authSpecAsAdmin());
        softly = new SoftAssertions();
    }

    @AfterEach
    public void clean(){
        TestDataStorage.getInstance().getStorage()
                .forEach((k, v) -> {
                    new TodoRequest(RequestSpec.authSpec(new User("admin", "admin")))
                            .delete(k);
        });
        TestDataStorage.getInstance().clean();
    }

    @AfterEach
    public void assertAll() {
        softly.assertAll();
    }
}
