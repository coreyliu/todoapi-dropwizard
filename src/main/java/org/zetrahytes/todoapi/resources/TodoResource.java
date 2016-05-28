package org.zetrahytes.todoapi.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zetrahytes.todoapi.db.TodoDAO;
import org.zetrahytes.todoapi.entity.Todo;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusResource.class);
    private final TodoDAO todoDAO;

    public TodoResource(TodoDAO todoDAO) {
        this.todoDAO = todoDAO;
        LOGGER.info("TodoDAO initialized");
    }

    @GET
    @UnitOfWork
    public List<Todo> getAllTodos() {
        return todoDAO.findAllTodos();
    }
}
