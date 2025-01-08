package ca.bc.gov.mal.cirras.claims.api.rest.v1.jersey;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.ClaimEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.ClaimListEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.SyncClaimEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.SyncClaimRelatedDataEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.SyncCodeEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.SyncCommodityVarietyEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.SyncCoveragePerilEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.ClaimCalculationEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.ClaimCalculationListEndpointImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl.TopLevelEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints.impl.CodeTableEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints.impl.CodeTableListEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.jersey.JerseyResourceConfig;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;

public class JerseyApplication extends JerseyResourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(JerseyApplication.class);

	/**
	 * Register JAX-RS application components.
	 */
	public JerseyApplication(@Context ServletConfig servletConfig) {
		super();

		logger.debug("<JerseyApplication");
		
		register(MultiPartFeature.class);
		
		register(TopLevelEndpointsImpl.class);
		register(CodeTableEndpointsImpl.class);
		register(CodeTableListEndpointsImpl.class);

		register(ClaimListEndpointImpl.class);
		register(ClaimEndpointImpl.class);
		
		register(ClaimCalculationListEndpointImpl.class);
		register(ClaimCalculationEndpointImpl.class);
		
		register(SyncClaimEndpointImpl.class);

		register(SyncClaimRelatedDataEndpointImpl.class);
		
		register(SyncCommodityVarietyEndpointImpl.class);
		register(SyncClaimEndpointImpl.class);
		
		register(SyncCodeEndpointImpl.class);
		register(SyncCoveragePerilEndpointImpl.class);

		register(OpenApiResource.class);
		register(AcceptHeaderOpenApiResource.class);

		SwaggerConfiguration oasConfig = new SwaggerConfiguration()
			.prettyPrint(Boolean.TRUE)
			.resourcePackages(
				Stream.of(
					"ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints",
					"ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints",
					"ca.bc.gov.nrs.wfone.common.rest.endpoints"
				).collect(Collectors.toSet()));


        try {
            new JaxrsOpenApiContextBuilder<JaxrsOpenApiContextBuilder<?>>()
                    .servletConfig(servletConfig)
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

		logger.debug(">JerseyApplication");
	}
}
