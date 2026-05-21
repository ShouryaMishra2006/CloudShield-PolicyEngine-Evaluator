package com.CloudShield.PolicyEngine.Evaluator;

import com.CloudShield.PolicyEngine.Evaluator.resources.PolicyEvaluationResource;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class GuardrailsApplication extends Application<GuardrailsConfiguration> {

    public static void main(String[] args) throws Exception {
        new GuardrailsApplication().run("server", "config.yml");
    }

    @Override
    public void initialize(Bootstrap<GuardrailsConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
    }

    @Override
    public void run(GuardrailsConfiguration configuration, Environment environment) {
        environment.jersey().register(new PolicyEvaluationResource(configuration.getEnvironmentZone()));
    }
}
