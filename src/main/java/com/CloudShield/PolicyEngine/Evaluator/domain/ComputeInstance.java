package com.CloudShield.PolicyEngine.Evaluator.domain;

import java.util.Objects;

public record ComputeInstance(String id, String compartment, int openPortsCount, boolean isEncrypted) implements CloudResource {
    public ComputeInstance {
        Objects.requireNonNull(id, "Instance ID cannot be null");
        if (openPortsCount < 0) {
            throw new IllegalArgumentException("Ports count cannot be negative");
        }
    }
}
