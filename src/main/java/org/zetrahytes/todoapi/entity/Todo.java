package org.zetrahytes.todoapi.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "todo")
@NamedQueries({
    @NamedQuery(
            name = "org.zetrahytes.todoapi.entity.Todo.findAllTodos",
            query = "SELECT t FROM Todo t"
    )
})
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private boolean done;
    private Date created;
    
    public Todo() {}

    public Todo(long id, String name, boolean done, Date created) {
        this.id = id;
        this.name = name;
        this.done = done;
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
