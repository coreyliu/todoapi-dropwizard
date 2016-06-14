package org.zetrahytes.todoapi.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "todo")
@Cacheable
@NamedQueries({
        @NamedQuery(
            name = "org.zetrahytes.todoapi.entity.Todo.findAllTodos",
            query = "SELECT t FROM Todo t",
            hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
        )
    })
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private boolean done;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date();

    public Todo() { }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Todo)) {
            return false;
        }

        final Todo that = (Todo) o;

        return Objects.equals(this.id, that.id)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.done, that.done)
                && Objects.equals(this.created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, done, created);
    }

}
