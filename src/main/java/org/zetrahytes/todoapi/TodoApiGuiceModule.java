package org.zetrahytes.todoapi;

import org.hibernate.SessionFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.dropwizard.hibernate.HibernateBundle;

public class TodoApiGuiceModule extends AbstractModule {

    private final HibernateBundle<TodoApiConfiguration> hibernateBundle;

    public TodoApiGuiceModule(HibernateBundle<TodoApiConfiguration> hibernateBundle) {
        this.hibernateBundle = hibernateBundle;
    }

    @Override
    protected void configure() { }

    @Provides
    public SessionFactory sessionFactory() {
        return hibernateBundle.getSessionFactory();
    }
}
