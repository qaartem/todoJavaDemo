import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.http.ContentType;
import org.example.models.Todo;
import org.example.models.TodoBuilder;
import org.example.specs.response.IncorrectDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TodoPutApi extends BaseTest{

//    @BeforeEach
//    public void setupEach() {
//        deleteAllTodos();
//    }

    private Todo getTodoById(int id) {
        Todo[] todos = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .extract().as(Todo[].class);
        return Arrays.stream(todos)
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Test
    public void testUpdateTodoWithValidData() {
        // Создаем Todo с id=1
        Todo todo = new Todo(1, "Task 1", false);
//        createTodo(todo);

        // Обновляем Todo с id = 1
        Todo updatedTodo = new Todo(1, "Task 1 updated", true);
        given()
                .contentType(ContentType.JSON)
                .body(updatedTodo)
                .when()
                .put("/todos/1")
                .then()
                .statusCode(200);

        // Получаем обновленный объект и проверяем его поля
        Todo fetchedTodo = getTodoById(1);

        assertEquals(updatedTodo.getId(), fetchedTodo.getId(), "ID должен совпадать");
        assertEquals(updatedTodo.getText(), fetchedTodo.getText(), "Текст задачи должен быть обновлен");
        assertEquals(updatedTodo.isCompleted(), fetchedTodo.isCompleted(), "Статус выполнения должен быть обновлен");
    }

    @Test
    public void testUpdateTodoNonExistent() {
        // Пытаемся обновить несуществующий Todo с id=9999
        Todo updatedTodo = new Todo(9999, "Non-existent Task", false);
        given()
                .contentType(ContentType.JSON)
                .body(updatedTodo)
                .when()
                .put("/todos/9999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdateTodoWithInvalidData() {
        // Создаем корректный Todo с id=2
        Todo todo = new Todo(2, "Task 2", false);
//        createTodo(todo);

        // Обновляем с некорректными данными: поле "completed" имеет неверный тип (целое число вместо boolean)
        String invalidUpdateJson = "{\"id\": 2, \"text\": \"Task 2 updated\", \"completed\": 1}";
        given()
                .contentType(ContentType.JSON)
                .body(invalidUpdateJson)
                .when()
                .put("/todos/2")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    public void testUpdateTodoWithoutIDInBody() {
        // Создаем Todo с id=3
        Todo todo = new Todo(3, "Task 3", false);
//        createTodo(todo);

        // Пытаемся обновить Todo с id=3, но тело запроса не содержит поля "id"
        String invalidUpdateJson = "{\"text\": \"Task 3 updated\", \"completed\": true}";
        given()
                .contentType(ContentType.JSON)
                .body(invalidUpdateJson)
                .when()
                .put("/todos/3")
                .then()
                .statusCode(401);
    }

    @Test
    public void testUpdateTodoWithIdMismatch() {
        // Создаем Todo с id = 6
        Todo todo = new Todo(6, "Task 6", false);
//        createTodo(todo);

        // Обновляем Todo, отправляя те же данные, без изменений
        given()
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .put("/todos/6")
                .then()
                .statusCode(200);

        // Получаем список всех Todo и фильтруем по id = 6
        Todo fetchedTodo = getTodoById(6);

        // Проверяем, что данные не изменились
        assertEquals(todo.getId(), fetchedTodo.getId(), "ID должен совпадать");
        assertEquals(todo.getText(), fetchedTodo.getText(), "Текст задачи не должен измениться");
        assertEquals(todo.isCompleted(), fetchedTodo.isCompleted(), "Статус выполнения не должен измениться");
    }
}
