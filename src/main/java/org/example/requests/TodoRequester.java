package org.example.requests;

import io.restassured.specification.RequestSpecification;
import org.example.requests.interfaces.SearchInterface;

public class TodoRequester {
    private TodoRequest request;
    private ValidatedTodoRequest validatedRequest;

    public TodoRequester(RequestSpecification requestSpecification) {
        this.request = new TodoRequest(requestSpecification);
        this.validatedRequest = new ValidatedTodoRequest(requestSpecification);
    }

    public TodoRequest getRequest() {
        return request;
    }

    public ValidatedTodoRequest getValidatedRequest() {
        return validatedRequest;
    }
}
