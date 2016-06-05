package org.zetrahytes.todoapi;

import org.zetrahytes.todoapi.db.ElasticsearchDAO;
import org.zetrahytes.todoapi.db.TodoDAO;
import org.zetrahytes.todoapi.entity.Todo;
import org.zetrahytes.todoapi.resources.NotesResource;
import org.zetrahytes.todoapi.resources.StatusResource;
import org.zetrahytes.todoapi.resources.TodoResource;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.elasticsearch.health.EsClusterHealthCheck;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TodoApiApplication extends Application<TodoApiConfiguration> {

    private final HibernateBundle<TodoApiConfiguration> hibernateBundle =
            new HibernateBundle<TodoApiConfiguration>(Todo.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(TodoApiConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };
        
    public static void main(final String[] args) throws Exception {
        new TodoApiApplication().run(args);
    }

    @Override
    public String getName() {
        return "TodoApi";
    }

    @Override
    public void initialize(final Bootstrap<TodoApiConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        
        bootstrap.addBundle(new MigrationsBundle<TodoApiConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TodoApiConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final TodoApiConfiguration configuration, final Environment environment) {
        
        // Status resource
        environment.jersey().register(new StatusResource());
        
        // Todo resource
        final TodoDAO todoDAO = new TodoDAO(hibernateBundle.getSessionFactory());
        environment.jersey().register(new TodoResource(todoDAO));
        
        // Notes resource
        final ManagedEsClient managedClient = new ManagedEsClient(configuration.getEsConfiguration());
        final ElasticsearchDAO elasticsearchDAO = new ElasticsearchDAO(managedClient.getClient());
        environment.lifecycle().manage(managedClient);
        environment.healthChecks().register("ES cluster health", new EsClusterHealthCheck(managedClient.getClient()));
        environment.jersey().register(new NotesResource(elasticsearchDAO, configuration.getIndex(), configuration.getType()));
    }

}
