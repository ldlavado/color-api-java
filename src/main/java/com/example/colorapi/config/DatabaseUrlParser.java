package com.example.colorapi.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public final class DatabaseUrlParser {

    public record Resolved(String jdbcUrl, String username, String password) {
    }

    private DatabaseUrlParser() {
    }

    public static Resolved resolve(String raw) {
        if (raw == null || raw.isBlank()) {
            raw = "postgres://colorapi:colorapi@localhost:5432/colorapi";
        }
        raw = raw.trim();
        if (raw.startsWith("jdbc:")) {
            return new Resolved(raw, "colorapi", "colorapi");
        }

        String normalized = raw
                .replaceFirst("^postgres://", "http://")
                .replaceFirst("^postgresql://", "http://");
        URI uri = URI.create(normalized);

        String username = "colorapi";
        String password = "colorapi";
        String userInfo = uri.getUserInfo();
        if (userInfo != null && !userInfo.isBlank()) {
            int split = userInfo.indexOf(':');
            if (split < 0) {
                username = decode(userInfo);
            } else {
                username = decode(userInfo.substring(0, split));
                password = decode(userInfo.substring(split + 1));
            }
        }

        String host = uri.getHost();
        int port = uri.getPort() == -1 ? 5432 : uri.getPort();
        String database = uri.getPath() == null ? "colorapi" : uri.getPath().replaceFirst("^/", "");
        if (database.isBlank()) {
            database = "colorapi";
        }

        String jdbc = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
            jdbc += "?" + uri.getQuery();
        }
        return new Resolved(jdbc, username, password);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
