package org.zetrahytes.todoapi;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.zetrahytes.todoapi.auth.ApiAuthenticator;
import org.zetrahytes.todoapi.auth.ApiAuthorizer;
import org.zetrahytes.todoapi.db.ElasticsearchDAO;
import org.zetrahytes.todoapi.entity.Todo;
import org.zetrahytes.todoapi.entity.User;
import org.zetrahytes.todoapi.resources.NotesResource;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
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

    private GuiceBundle<TodoApiConfiguration> guiceBundle;

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

        guiceBundle = GuiceBundle.<TodoApiConfiguration>newBuilder()
                .addModule(new TodoApiGuiceModule(hibernateBundle))
                .enableAutoConfig(getClass().getPackage().getName())
                .setConfigClass(TodoApiConfiguration.class)
                .build();

        bootstrap.addBundle(guiceBundle);

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

        // initializing apache shiro
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new ApiAuthenticator())
                .setAuthorizer(new ApiAuthorizer())
                .setRealm("TodoApiRealm")
                .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        // Notes resource
        final ManagedEsClient managedClient = new ManagedEsClient(configuration.getEsConfiguration());
        final ElasticsearchDAO elasticsearchDAO = new ElasticsearchDAO(managedClient.getClient());
        environment.lifecycle().manage(managedClient);
        environment.healthChecks().register("ES cluster health", new EsClusterHealthCheck(managedClient.getClient()));
        environment.jersey().register(
                new NotesResource(elasticsearchDAO, configuration.getIndex(), configuration.getType()));
    }

}
