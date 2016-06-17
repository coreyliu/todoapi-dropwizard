package org.zetrahytes.todoapi.entity;

import java.security.Principal;
import org.apache.shiro.subject.Subject;

public class User implements Principal {
    // represents state and security operations for a single application user
    private final Subject subject;

    public User(Subject subject) {
        this.subject = subject;
    }

    public String getName() {
        return subject.getPrincipal().toString();
    }

    public Subject getSubject() {
        return subject;
    }

}
