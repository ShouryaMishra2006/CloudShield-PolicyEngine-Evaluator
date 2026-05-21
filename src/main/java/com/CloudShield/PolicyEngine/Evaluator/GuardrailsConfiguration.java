package com.CloudShield.PolicyEngine.Evaluator;

import io.dropwizard.core.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public class GuardrailsConfiguration extends Configuration {
    @NotEmpty
    private String environmentZone;

    @JsonProperty
    public String getEnvironmentZone() { return environmentZone; }
}
