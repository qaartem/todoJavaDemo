import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.Todo;
import org.junit.jupiter.api.*;


import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class TodoPostApi extends BaseTest {

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    public void testCreateTodoWithValidDataAndCheckInTODOInList() {
        Todo todo = new Todo(24, "test1", false);
        given()
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .post("/todos")
                .then().log().all()
                .statusCode(201)
                .body(is(emptyOrNullString()));

        Response getResponse = given()
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .get("/todos")
                .then().log().all()
                .statusCode(200)
                .extract().response();

        // Десериализуем ответ в массив объектов Todo
        Todo[] todos = getResponse.as(Todo[].class);

        // Ищем созданный элемент по id
        Todo createdTodo = Arrays.stream(todos)
                .filter(t -> t.getId() == todo.getId())
                .findFirst()
                .orElse(null);

        assertNotNull(createdTodo, "Созданный элемент должен присутствовать в списке");
        assertEquals(todo.getId(), createdTodo.getId());
        assertEquals(todo.getText(), createdTodo.getText());
        assertEquals(todo.isCompleted(), createdTodo.isCompleted());
    }

    @Test
    public void testCreateTodoWithInvalidDataAndCheck400StatusCode() {
        String invalidJson = "{\"id\": 1, \"text\": \"test\", \"completed\": 1}";

        given()
                .contentType(ContentType.JSON)
                .body(invalidJson)
                .when()
                .post("/todos")
                .then().log().all()
                .statusCode(400)
                .extract().response();
    }

    @Test
    public void testCreateTodoWithoutBodyAndCheck400StatusCode() {
        String emptyJson = "";

        given()
                .contentType(ContentType.JSON)
                .body(emptyJson)
                .when()
                .post("/todos")
                .then().log().all()
                .statusCode(400)
                .extract().response();
    }

    @Test
    public void testCreateTodoWithMissingFieldInBodyAndCheck400StatusCode() {
        String missingFieldJson = "{\"id\": 1, \"completed\": 1}";

        given()
                .contentType(ContentType.JSON)
                .body(missingFieldJson)
                .when()
                .post("/todos")
                .then().log().all()
                .statusCode(400)
                .extract().response();
    }

    @Test
    public void testCreateTodoWithExistedIDAndCheck400StatusCode() {
        Todo todo1 = new Todo(4, "test", false);
        Todo todo2 = new Todo(4, "test2", true);
        given()
                .contentType(ContentType.JSON)
                .body(todo1)
                .when()
                .post("/todos")
                .then().log().all()
                .statusCode(201)
                .extract().response();

        given()
                .contentType(ContentType.JSON)
                .body(todo2)
                .when()
                .post("/todos")
                .then().log().all()
                .statusCode(400)
                .extract().response();
    }


}
