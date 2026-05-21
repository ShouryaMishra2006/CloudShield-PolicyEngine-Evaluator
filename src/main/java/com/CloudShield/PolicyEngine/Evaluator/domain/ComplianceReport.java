package com.CloudShield.PolicyEngine.Evaluator.domain;

import java.time.Instant;

public record ComplianceReport(
    String resourceId,
    String status,
    String evaluationReason,
    String evaluatedZone,
    Instant timestamp
) {}
