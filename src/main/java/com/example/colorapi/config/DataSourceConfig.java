package com.example.colorapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        String raw = firstNonBlank(System.getenv("DATABASE_URL"), System.getenv("SPRING_DATASOURCE_URL"));
        DatabaseUrlParser.Resolved db = DatabaseUrlParser.resolve(raw);

        HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl(db.jdbcUrl());
        hikari.setUsername(db.username());
        hikari.setPassword(db.password());
        hikari.setMaximumPoolSize(5);
        return new HikariDataSource(hikari);
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        return second;
    }
}
