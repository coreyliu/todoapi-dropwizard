package org.zetrahytes.todoapi.resources;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zetrahytes.todoapi.db.TodoDAO;
import org.zetrahytes.todoapi.entity.Todo;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/todos")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoResource.class);
    private TodoDAO todoDAO;

    @Inject
    public TodoResource(TodoDAO todoDAO) {
        this.todoDAO = todoDAO;
        LOGGER.info("TodoResource initialized");
    }

    @GET
    @UnitOfWork
    public List<Todo> getAllTodos() {
        return todoDAO.findAllTodos();
    }

    @POST
    @UnitOfWork
    public Todo addTodo(Todo todo) {
        return todoDAO.addTodo(todo);
    }
}
