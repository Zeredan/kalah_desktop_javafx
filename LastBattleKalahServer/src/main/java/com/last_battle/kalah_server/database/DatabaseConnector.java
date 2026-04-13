package com.last_battle.kalah_server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5433/kalah";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ PostgreSQL JDBC Driver loaded");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        }
        initDatabase();
    }

    private static void initDatabase() {
        String createTable = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                wins_vs_player INTEGER DEFAULT 0,
                wins_vs_bot INTEGER DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
            System.out.println("✅ Database initialized");
        } catch (SQLException e) {
            System.err.println("❌ Database init failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}