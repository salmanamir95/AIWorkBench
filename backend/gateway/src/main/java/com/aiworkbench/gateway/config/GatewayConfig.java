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
                        .path("/user/**") // Matches /user/users, /user/udr, etc.
                        .filters(f -> f
                                .stripPrefix(1) // Removes '/user', passes '/users/...' to service
                                .circuitBreaker(config -> config
                                        .setName("userServiceCB")
                                        .setFallbackUri("forward:/fallback/user")
                                )
                        )
                        .uri("lb://user-service")
                )
                .build();
    }
}