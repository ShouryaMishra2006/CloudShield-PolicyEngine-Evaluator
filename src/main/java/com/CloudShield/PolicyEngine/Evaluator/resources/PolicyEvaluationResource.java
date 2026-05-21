package com.CloudShield.PolicyEngine.Evaluator.resources;
import com.CloudShield.PolicyEngine.Evaluator.domain.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;

@Path("/v1/evaluator")
@Produces(MediaType.APPLICATION_JSON)
public class PolicyEvaluationResource {
    
    private final String activeZone;

    public PolicyEvaluationResource(String activeZone) {
        this.activeZone = activeZone;
    }

    @GET
    @Path("/meta")
    public Response getMetadata() {
        String jsonMetaBlueprint = """
            {
                "service": "OCI-Guardrails-Engine",
                "rulesEnforced": ["BLOCK_PUBLIC_BUCKETS", "RESTRICT_HIGH_RISK_PORTS"],
                "complianceStandard": "CIS-Oracle-Cloud-Benchmarks-v1.1"
            }
            """;
        return Response.ok(jsonMetaBlueprint).build();
    }

    @POST
    @Path("/verify-compute")
    public Response checkCompute(
            @QueryParam("id") String id, 
            @QueryParam("compartment") String compartment,
            @QueryParam("ports") int ports,
            @QueryParam("encrypted") boolean encrypted) {
        
        CloudResource instance = new ComputeInstance(id, compartment, ports, encrypted);
        return runCompliancePipeline(instance);
    }

    @POST
    @Path("/verify-bucket")
    public Response checkBucket(
            @QueryParam("id") String id,
            @QueryParam("compartment") String compartment,
            @QueryParam("allowPublic") boolean allowPublic) {
        
        CloudResource bucket = new ObjectBucket(id, compartment, List.of("10.0.0.1/32"), allowPublic);
        return runCompliancePipeline(bucket);
    }

    //Using Pattern Matching in JAVA 17
    private Response runCompliancePipeline(CloudResource resource) {
        ComplianceReport report = switch (resource) {
            case ComputeInstance ci && ci.openPortsCount() > 22 -> 
                new ComplianceReport(ci.id(), "NON_COMPLIANT", "Critical structural risk: High count of open network ports detected", activeZone, Instant.now());
            
            case ComputeInstance ci && !ci.isEncrypted() -> 
                new ComplianceReport(ci.id(), "NON_COMPLIANT", "Encryption-at-rest constraint violated on target block storage", activeZone, Instant.now());
            
            case ComputeInstance ci -> 
                new ComplianceReport(ci.id(), "COMPLIANT", "Compute attributes match default OCI security baselines", activeZone, Instant.now());
            
            case ObjectBucket ob && ob.publicAccessAllowed() -> 
                new ComplianceReport(ob.id(), "NON_COMPLIANT", "Data leakage alert: Storage bucket is configured with public access permissions", activeZone, Instant.now());
            
            case ObjectBucket ob -> 
                new ComplianceReport(ob.id(), "COMPLIANT", "Private storage layout safely restricted from external networks", activeZone, Instant.now());
        };


        //By adding this security header, we ensure that the compliance report is not cached by any intermediary routers or browsers
        return Response.ok(report)
                .header("Cache-Control", "no-store, private")
                .build();
    }
}
