package org.zetrahytes.todoapi.resources;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.zetrahytes.todoapi.db.TodoDAO;
import org.zetrahytes.todoapi.entity.Todo;

import com.google.common.collect.ImmutableList;

import io.dropwizard.testing.junit.ResourceTestRule;
import jersey.repackaged.com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodoResourceTest {

    private static final TodoDAO todoDAO = mock(TodoDAO.class);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TodoResource(todoDAO))
            .build();
    @Captor
    private ArgumentCaptor<Todo> todoCaptor;
    private Todo todo;

    @Before
    public void setUp() {
        todo = new Todo(1, "Todo Name", true, new Date());
    }

    @After
    public void tearDown() {
        reset(todoDAO);
    }

    @Test
    public void getAllTodos() {
        final List<Todo> todoList = ImmutableList.of(todo);
        when(todoDAO.findAllTodos()).thenReturn(todoList);
        final List<Todo> response = resources.client().target("/todos")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Todo>>() {});
        verify(todoDAO).findAllTodos();
        assertThat(response).containsAll(todoList);
    }

    @Test
    public void addTodo() {
        when(todoDAO.addTodo(any(Todo.class))).thenReturn(todo);
        final Response response = resources.client().target("/todos")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(todo, MediaType.APPLICATION_JSON));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(todoDAO).addTodo(todoCaptor.capture());
        assertThat(todoCaptor.getValue()).isEqualTo(todo);
    }
}
