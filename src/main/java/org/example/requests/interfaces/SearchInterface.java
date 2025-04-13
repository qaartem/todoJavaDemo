package org.example.requests.interfaces;

public interface SearchInterface<T> {
    Object readAll(int offset, int limit);
    Object readAll();
}