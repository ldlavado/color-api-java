package com.example.colorapi;

import com.example.colorapi.config.DatabaseUrlParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseUrlParserTest {

    @Test
    void convierteUrlDeRender() {
        DatabaseUrlParser.Resolved resolved = DatabaseUrlParser.resolve(
                "postgres://colorapi:secreto@dpg-abc.ohio-postgres.render.com/colorapi");

        assertEquals("jdbc:postgresql://dpg-abc.ohio-postgres.render.com:5432/colorapi", resolved.jdbcUrl());
        assertEquals("colorapi", resolved.username());
        assertEquals("secreto", resolved.password());
    }

    @Test
    void conservaSslModeExterno() {
        DatabaseUrlParser.Resolved resolved = DatabaseUrlParser.resolve(
                "postgres://colorapi:secreto@dpg-abc.ohio-postgres.render.com:5432/colorapi?sslmode=require");

        assertEquals(
                "jdbc:postgresql://dpg-abc.ohio-postgres.render.com:5432/colorapi?sslmode=require",
                resolved.jdbcUrl());
    }
}
