import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import org.assertj.core.api.SoftAssertions;
import org.example.annotations.PrepareTodo;
import org.example.models.Todo;
import org.example.requests.TodoRequest;
import org.example.specs.response.IncorrectDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;

public class TodoDeleteApi extends BaseTest{

//    @BeforeEach
//    public void setupEach() {
//        deleteAllTodos();
//    }

//    private Todo[] getTodos() {
//        return given()
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/todos")
//                .then().log().all()
//                .statusCode(200)
//                .extract()
//                .as(Todo[].class);
//    }
    // Тест 1: Успешное удаление существующего Todo
    @Test
    @PrepareTodo(1)
    @Description("TC1: Авторизированный юзер может удалить todo")
    public void testDeleteExistingTodo() {

        // Создаем Todo с id = 7
//        Todo todo = new Todo(7, "Task 7", false);
//        createTodo(todo);

        // Удаляем Todo с id = 7 с аутентификацией, ожидаем статус 204 (No Content)
//        given()
//                .auth().preemptive().basic("admin", "admin")
//                .when()
//                .delete("/todos/" + todo.getId())
//                .then().log().all()
//                .statusCode(204);

        Todo createdTodo = todoRequester.getValidatedRequest().readAll().getFirst();

        todoRequester.getValidatedRequest().delete(createdTodo.getId());

        softly.assertThat(todoRequester.getValidatedRequest().readAll()).hasSize(0);

        // Получаем список всех Todo и убеждаемся, что Todo с id = 7 отсутствует
//        Todo[] todos = getTodos();
//        boolean exists = Arrays.stream(todos)
//                .anyMatch(t -> t.getId() == todo.getId());
//        assertFalse(exists, "Todo с id 7 должен быть удалён");
    }

    // Тест 2: Попытка удаления несуществующего Todo (ожидается статус 404)
    @Test
    @Description("TC4: Авторизованный юзер не может удалить todo с несуществующим id")
    public void testDeleteNonExistingTodo() {
        var nonExistingId = new Random().nextLong();

        todoRequester.getRequest().delete(nonExistingId)
                .then().assertThat().spec(IncorrectDataResponse.nonExistingId(nonExistingId));
//        given()
//                .auth().preemptive().basic("admin", "admin")
//                .when()
//                .delete("/todos/9999")
//                .then()
//                .statusCode(404);

    }

    // Тест 3: Удаление одного и того же Todo дважды
    @Test
    public void testDeleteTodoTwice() {
        // Создаем Todo с id = 8
//        Todo todo = new Todo(8, "Task 8", true);
//        createTodo(todo);

        // Первый запрос на удаление: ожидаем статус 204
//        given()
//                .auth().preemptive().basic("admin", "admin")
//                .when()
//                .delete("/todos/" + todo.getId())
//                .then().log().all()
//                .statusCode(204);
//
//        // Второй запрос: ресурс уже удален, ожидаем статус 404
//        given()
//                .auth().preemptive().basic("admin", "admin")
//                .when()
//                .delete("/todos/" + todo.getId())
//                .then().log().all()
//                .statusCode(404);
    }

    // Тест 4: Попытка удаления Todo с некорректным идентификатором (например, нечисловой id)
    @Test
    public void testDeleteTodoWithInvalidId() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .when()
                .delete("/todos/abc")
                .then()
                .statusCode(404);
    }

    // Тест 5: Проверка уменьшения количества элементов после удаления Todo
    @Test
    public void testDeleteTodoAndCheckListCountDecrease() {
        // Создаем два Todo с id = 10 и id = 11
        Todo todo10 = new Todo(10, "Task 10", false);
        Todo todo11 = new Todo(11, "Task 11", true);
//        createTodo(todo10);
//        createTodo(todo11);

//        int countBefore = getTodos().length;

        // Удаляем Todo с id = 10 с аутентификацией
        given()
                .auth().preemptive().basic("admin", "admin")
                .when()
                .delete("/todos/" + todo10.getId())
                .then()
                .statusCode(204);

        // Получаем количество элементов после удаления
//        int countAfter = getTodos().length;

        // Проверяем, что количество уменьшилось на 1
//        assertEquals(countBefore - 1, countAfter, "Количество Todo должно уменьшиться на 1 после удаления");
    }
}
