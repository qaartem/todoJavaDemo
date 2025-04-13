package org.example.annotations;

import org.example.conf.Configuration;
import org.example.models.Todo;
import org.example.requests.TodoRequest;

import org.example.specs.request.RequestSpec;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

import static org.example.generators.TestDataGenerator.generateTestData;

public class BeforeEachExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        var testMethod = extensionContext.getRequiredTestMethod();

        mobileExecutionExtension(testMethod);
        prepareTodoExtension(testMethod);
    }

    private void mobileExecutionExtension(Method testMethod) {
        var mobile = testMethod.getAnnotation(Mobile.class);

        if (mobile != null) {
            Configuration.setProperty("version", "mobile");
        }
    }

    private void prepareTodoExtension(Method testMethod) {
        var prepareTodo = testMethod.getAnnotation(PrepareTodo.class);

        if (prepareTodo != null) {
            for (int i = 0; i < prepareTodo.value(); i++) {
                new TodoRequest(RequestSpec.authSpecAsAdmin())
                        .create(generateTestData(Todo.class));
            }
        }
    }
}
