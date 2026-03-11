package com.example.Auth.db.config;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(
            final DataSource dataSource,
            @Value("${spring.flyway.locations:classpath:db/migration}") final String locations,
            @Value("${spring.flyway.default-schema:}") final String defaultSchema,
            @Value("${spring.flyway.schemas:}") final String schemas,
            @Value("${spring.flyway.baseline-on-migrate:false}") final boolean baselineOnMigrate,
            @Value("${spring.flyway.create-schemas:false}") final boolean createSchemas
    ) {
        final FluentConfiguration config = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(baselineOnMigrate)
                .createSchemas(createSchemas);

        final String[] locationList = splitCsv(locations);
        if (locationList.length > 0) {
            config.locations(locationList);
        }

        if (!defaultSchema.isBlank()) {
            config.defaultSchema(defaultSchema.trim());
        }

        final String[] schemaList = splitCsv(schemas);
        if (schemaList.length > 0) {
            config.schemas(schemaList);
        }

        return config.load();
    }

    @Bean
    public ApplicationRunner flywayRunner(final Flyway flyway) {
        return args -> flyway.migrate();
    }

    private static String[] splitCsv(final String raw) {
        if (raw == null || raw.isBlank()) {
            return new String[0];
        }
        final List<String> parts = Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
        return parts.toArray(String[]::new);
    }
}
