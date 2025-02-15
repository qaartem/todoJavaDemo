import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.example.Todo;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class todoApiTests extends BaseTest {

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    public void testCreateTodoWithValidDataAndCheckInTODOInList() {
        Todo todo = new Todo(4, "test", false);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .post("/todos")
                .then().log().all()
                .statusCode(201)
                .extract().response();

        Response getResponse = given()
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .get("/todos")
                .then().log().all()
                .statusCode(200)
                .extract().response();

        Assertions.assertEquals(todo.getId(), todo.getId());
        Assertions.assertEquals(todo.getText(), todo.getText());
        Assertions.assertEquals(todo.isCompleted(), todo.isCompleted());
    }
}
