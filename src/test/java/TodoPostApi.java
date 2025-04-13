import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.models.Todo;
import org.example.models.TodoBuilder;
import org.example.requests.TodoRequest;
import org.example.requests.ValidatedTodoRequest;
import org.example.specs.request.RequestSpec;
import org.example.specs.response.IncorrectDataResponse;
import org.junit.jupiter.api.*;


import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class TodoPostApi extends BaseTest {

    @Test
    public void testCreateTodoWithNegativeIDAndCheck400StatusCode() {
        Todo todo1 = new TodoBuilder()
                .setId(-4)
                .setText("test")
                .setCompleted(false)
                .build();
//        Todo todo1 = new Todo(-4, "test", false);

//        createTodo(todo1);

//        todoRequest.create(todo1)
//                .then().log().all()
//                .statusCode(201)
//                .extract().response();

        todoRequester.getRequest()
                .create(todo1)
                .then()
                .spec(IncorrectDataResponse.negativeIdInBody(todo1.getId()));

//        todoRequest.create(todo2)
//                .then().log().all()
//                .statusCode(400)
//                .extract().response();
    }

    @Test
    public void testCreateTodoWithValidDataAndCheckInTODOInList() {
//        Todo todo = new Todo(24, "test1", false);
        Todo todo = new TodoBuilder()
                .setId(24)
                .setText("test1")
                .setCompleted(false)
                        .build();

                todoRequester.getRequest().create(todo)
                .then().log().all()
                .statusCode(201)
                .body(is(emptyOrNullString()));

        Response getResponse = given()
                .contentType(ContentType.JSON)
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
    public void testCreateTodoWithMaxLengthText() {
        // Предполагаем, что максимальная длина поля 'text' составляет 255 символов
        String maxLengthText = "A".repeat(255);
        Todo newTodo = new Todo(3, maxLengthText, false);

//        ValidatedTodoRequest authValReq = new ValidatedTodoRequest(RequestSpec.authSpec());
        // Отправляем POST запрос для создания нового TODO
        todoRequester.getValidatedRequest().create(newTodo);
//        given()
//                .filter(new AllureRestAssured())
//                .contentType(ContentType.JSON)
//                .body(newTodo)
//                .when()
//                .post("/todos")
//                .then()
//                .statusCode(201)
//                .body(is(emptyOrNullString())); // Проверяем, что тело ответа пустое




        // Проверяем, что TODO было успешно создано
        List<Todo> todos = todoRequester.getValidatedRequest().readAll();
//        Todo[] todos = given()
//                .when()
//                .get("/todos")
//                .then()
//                .statusCode(200)
//                .extract()
//                .as(Todo[].class);

        // Ищем созданную задачу в списке
        boolean found = false;
        for (Todo todo : todos) {
            if (todo.getId() == newTodo.getId()) {
                Assertions.assertEquals(newTodo.getText(), todo.getText());
                Assertions.assertEquals(newTodo.isCompleted(), todo.isCompleted());
                found = true;
                break;
            }
        }
        Assertions.assertTrue(found, "Созданная задача не найдена в списке TODO");
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
//        String emptyJson = "";
        Todo emptyJson = new TodoBuilder()
                .build();

//        given()
//                .contentType(ContentType.JSON)
//                .body(emptyJson)
//                .when()
//                .post("/todos")
        todoRequester.getRequest().create(emptyJson)
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
                .contentType(ContentType.TEXT)
                .body(notNullValue());
    }

    @Test
    public void testCreateTodoWithExistedIDAndCheck400StatusCode() {
        Todo todo1 = new TodoBuilder()
                .setId(4)
                .setText("test")
                .setCompleted(false)
                .build();
//        Todo todo1 = new Todo(4, "test", false);

        Todo todo2 = new TodoBuilder()
                .setId(4)
                .setText("test2")
                .setCompleted(true)
                .build();
//        Todo todo2 = new Todo(4, "test2", true);

//        createTodo(todo1);

//        todoRequest.create(todo1)
//                .then().log().all()
//                .statusCode(201)
//                .extract().response();

        todoRequester.getRequest()
        .create(todo2)
        .then()
        .spec(new IncorrectDataResponse().sameId(todo1.getId()));

//        todoRequest.create(todo2)
//                .then().log().all()
//                .statusCode(400)
//                .extract().response();
    }


}
