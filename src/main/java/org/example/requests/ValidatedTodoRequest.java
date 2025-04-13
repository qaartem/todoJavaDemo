package org.example.requests;


import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.example.models.Todo;
import org.example.requests.interfaces.CrudInterface;
import org.example.requests.interfaces.SearchInterface;
import org.example.storages.TestDataStorage;

import java.util.List;

public class ValidatedTodoRequest extends Request implements CrudInterface<Todo>, SearchInterface<Todo> {
//    private static final String TODO_ENDPOINT = "/todos";

    private TodoRequest todoRequest;

    public ValidatedTodoRequest(RequestSpecification reqSpec) {
        super(reqSpec);
        todoRequest = new TodoRequest(reqSpec);
    }

    @Override
    @Step("Create {entity}")
    public String create(Todo entity) {
        var response = todoRequest.create(entity)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED).extract().asString();
        TestDataStorage.getInstance().addData(entity);
        return response;
    }

    @Override
    public Todo update(long id, Todo entity) {
        return todoRequest.update(id, entity)
                .then()
                .statusCode(HttpStatus.SC_OK).extract().as(Todo.class);
    }

    @Override
    public String delete(long id) {
        return todoRequest.delete(id)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().body()
                .asString();
    }

    @Override
    public List<Todo> readAll(int offset, int limit) {
        Todo[] todos = todoRequest.readAll(offset, limit)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Todo[].class);
        return List.of(todos);
    }

    public List<Todo> readAll() {
        Todo[] todos = todoRequest.readAll()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Todo[].class);
        return List.of(todos);
    }
}
