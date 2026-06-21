package com.aiworkbench.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("user-service", r -> r
                        .path("/user/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .addResponseHeader("X-Gateway", "active"))
                        .uri("http://localhost:8081"))

                .build();
    }

}
