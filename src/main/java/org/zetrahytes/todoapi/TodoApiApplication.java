package org.zetrahytes.todoapi;

import org.zetrahytes.todoapi.resources.StatusResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TodoApiApplication extends Application<TodoApiConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TodoApiApplication().run(args);
    }

    @Override
    public String getName() {
        return "TodoApi";
    }

    @Override
    public void initialize(final Bootstrap<TodoApiConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final TodoApiConfiguration configuration, final Environment environment) {
        environment.jersey().register(new StatusResource());
    }

}
