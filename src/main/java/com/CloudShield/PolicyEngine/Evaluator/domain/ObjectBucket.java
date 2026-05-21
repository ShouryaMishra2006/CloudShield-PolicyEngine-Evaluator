package com.CloudShield.PolicyEngine.Evaluator.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record ObjectBucket(String id, String compartment, List<String> allowedIpCidrs, boolean publicAccessAllowed) implements CloudResource {
    public ObjectBucket {
        Objects.requireNonNull(id, "Bucket ID cannot be null");
        allowedIpCidrs = new ArrayList<>(allowedIpCidrs);
    }
}
