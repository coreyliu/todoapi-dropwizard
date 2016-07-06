package org.zetrahytes.todoapi.db;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.zetrahytes.todoapi.entity.Todo;


import io.dropwizard.hibernate.AbstractDAO;

public class TodoDAO extends AbstractDAO<Todo> {

    @Inject
    public TodoDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Todo> findAllTodos() {
        return list(namedQuery("org.zetrahytes.todoapi.entity.Todo.findAllTodos"));
    }

    public Todo addTodo(Todo todo) {
        return persist(todo);
    }

}
