import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.Todo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TodoGetApi extends BaseTest{
    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    public void testGetTodosWhenDatabaseIsEmpty() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .body("", hasSize(0));
    }

    @Test
    public void testGetTodosWithExistingIDs() {
        Todo todo1 = new Todo(1, "Task 1", false);
        Todo todo2 = new Todo(2, "Task 2", true);

        createTodo(todo1);
        createTodo(todo2);

        Response response =
                given()
                        .when()
                        .get("/todos")
                        .then()
                        .statusCode(200)
                        .contentType("application/json")
                        .body("", hasSize(2))
                        .extract().response();

        Todo[] todos = response.getBody().as(Todo[].class);

        assertEquals(1, todos[0].getId());
        assertEquals("Task 1", todos[0].getText());
        Assertions.assertFalse(todos[0].isCompleted());

        assertEquals(2, todos[1].getId());
        assertEquals("Task 2", todos[1].getText());
        Assertions.assertTrue(todos[1].isCompleted());
    }

    @Test
    public void testGetTodosWithLimitAndOffsetPaginationForFirstTwoPagesAndCheckThatIDsOnDifferentPagesDoNotOverlap() {
        for (int i = 1; i <= 10; i++) {
            Todo todo = new Todo(i, "Task " + i, i % 2 == 0);
            createTodo(todo);
        }
        Response responsePage1 = given()
                .queryParam("limit", 5)
                .queryParam("offset", 0)
                .when()
                .get("/todos")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();
        Todo[] todosPage1 = responsePage1.as(Todo[].class);
        assertEquals(5, todosPage1.length, "Ожидается 5 записей на первой странице");

        Response responsePage2 = given()
                .queryParam("limit", 5)
                .queryParam("offset", 5)
                .when()
                .get("/todos")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();
        Todo[] todosPage2 = responsePage2.as(Todo[].class);
        assertEquals(5, todosPage2.length, "Ожидается 5 записей на второй странице");

        for (Todo t1 : todosPage1) {
            for (Todo t2 : todosPage2) {
                assertNotEquals(t1.getId(), t2.getId(), "Записи на разных страницах не должны пересекаться");
            }
        }
    }
}
