import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.annotations.BeforeEachExtension;
import org.example.annotations.Mobile;
import org.example.annotations.PrepareTodo;
import org.example.specs.response.IncorrectDataResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.example.models.Todo;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.example.generators.TestDataGenerator.generateTestData;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(BeforeEachExtension.class)
public class TodoGetApi extends BaseTest{
//    @BeforeEach
//    public void setupEach() {
//        deleteAllTodos();
//    }

//    @Test
//    public void testGetTodosWhenDatabaseIsEmpty() {
//        given()
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/todos")
//                .then().log().all()
//                .statusCode(200)
//                .body("", hasSize(0));
//    }

    @Test
    @PrepareTodo(2)
    public void testGetTodosWithExistingIDs() {
//        for (int i = 1; i <= 2; i++) {
//            Todo todo = new Todo(i, "Task " + i, i % 2 == 0);
//            createTodo(todo);
//        }
//        Todo todo1 = generateTestData(Todo.class);
//
//        todo1.setText("arabic symbols");

        var createdTodos = todoRequester.getValidatedRequest().readAll();

        softly.assertThat(createdTodos).hasSize(5);

//        Response response =
//                given()
//                        .when()
//                        .get("/todos")
//                        .then().log().all()
//                        .statusCode(200)
//                        .contentType("application/json")
//                        .body("", hasSize(2))
//                        .extract().response();
//
//        Todo[] todos = response.getBody().as(Todo[].class);
//
//        assertEquals(1, todos[0].getId());
//        assertEquals("Task 1", todos[0].getText());
//        Assertions.assertFalse(todos[0].isCompleted());
//
//        assertEquals(2, todos[1].getId());
//        assertEquals("Task 2", todos[1].getText());
//        Assertions.assertTrue(todos[1].isCompleted());
    }


    @Test
//    @Mobile
    @PrepareTodo(5)
    public void testGetTodosWithLimitAndOffsetPaginationForFirstTwoPagesAndCheckThatIDsOnDifferentPagesDoNotOverlap() {
        var createdTodos = todoRequester.getValidatedRequest().readAll(2, 2);
        softly.assertThat(createdTodos).hasSize(2);
//        for (int i = 1; i <= 10; i++) {
//            Todo todo = new Todo(i, "Task " + i, i % 2 == 0);
////            createTodo(todo);
//        }
//        Response responsePage1 = given()
//                .queryParam("limit", 5)
//                .queryParam("offset", 0)
//                .when()
//                .get("/todos")
//                .then().log().all()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .extract().response();
//        Todo[] todosPage1 = responsePage1.as(Todo[].class);
//        assertEquals(5, todosPage1.length, "Ожидается 5 записей на первой странице");
//
//        Response responsePage2 = given()
//                .queryParam("limit", 5)
//                .queryParam("offset", 5)
//                .when()
//                .get("/todos")
//                .then().log().all()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .extract().response();
//        Todo[] todosPage2 = responsePage2.as(Todo[].class);
//        assertEquals(5, todosPage2.length, "Ожидается 5 записей на второй странице");
//
//        for (Todo t1 : todosPage1) {
//            for (Todo t2 : todosPage2) {
//                assertNotEquals(t1.getId(), t2.getId(), "Записи на разных страницах не должны пересекаться");
//            }
//        }
    }

    @Test
    public void testGetTodosWithInvalidOffsetAndLimit() {
        todoRequester.getRequest().readAll(-1,-1)
                .then().assertThat().spec(IncorrectDataResponse.offsetOrLimitHaveIncorrectValues());
    }

    @Test
    public void testGetTodosWithExcessiveLimit() {
        var paginatedTodos = todoRequester.getValidatedRequest().readAll(1, 1000);
        var allTodos = todoRequester.getValidatedRequest().readAll();

        softly.assertThat(paginatedTodos).isEqualTo(allTodos);
    }
}
