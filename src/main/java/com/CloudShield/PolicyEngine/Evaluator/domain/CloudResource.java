package com.CloudShield.PolicyEngine.Evaluator.domain;

public sealed interface CloudResource permits ComputeInstance, ObjectBucket {
    String id();
    String compartment();
}
