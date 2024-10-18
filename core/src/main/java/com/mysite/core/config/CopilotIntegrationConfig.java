package com.mysite.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Copilot Integration Config")
public @interface CopilotIntegrationConfig {

    @AttributeDefinition(name = "API Key", description = "API Key for authorization")
    String apiKey() default "";

    @AttributeDefinition(name = "API URL", description = "URL for the Copilot API")
    String apiUrl() default "";
}
