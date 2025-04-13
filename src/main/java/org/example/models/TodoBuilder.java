package org.example.models;

public class TodoBuilder {
    private long id;
    private String text;
    private boolean completed;


    public TodoBuilder setCompleted(boolean completed) {
        this.completed = completed;
        return this;
    }

    public TodoBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public TodoBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public Todo build() {
        return new Todo(
                id, text, completed
        );
    }
}
