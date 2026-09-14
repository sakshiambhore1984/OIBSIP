package reservation;

import java.sql.Connection;
import java.sql.Statement;

    public class DatabaseSetup {

        public static void main(String[] args) {

            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement()) {

                // Users table
                String usersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    )
                    """;

                // Trains table
                String trainsTable = """
                    CREATE TABLE IF NOT EXISTS trains (
                        train_number INTEGER PRIMARY KEY,
                        train_name TEXT NOT NULL
                    )
                    """;

                // Reservations table
                String reservationsTable = """
                    CREATE TABLE IF NOT EXISTS reservations (
                        pnr INTEGER PRIMARY KEY AUTOINCREMENT,
                        passenger_name TEXT NOT NULL,
                        train_number INTEGER NOT NULL,
                        train_name TEXT NOT NULL,
                        class_type TEXT NOT NULL,
                        journey_date TEXT NOT NULL,
                        source TEXT NOT NULL,
                        destination TEXT NOT NULL
                    )
                    """;

                statement.execute(usersTable);
                statement.execute(trainsTable);
                statement.execute(reservationsTable);

                // Default login
                statement.execute(
                        "INSERT OR IGNORE INTO users(username, password) " +
                                "VALUES('admin', 'admin123')"
                );

                statement.execute(
                        "INSERT OR IGNORE INTO trains(train_number, train_name) " +
                                "VALUES(10101, 'Deccan Express')"
                );

                statement.execute(
                        "INSERT OR IGNORE INTO trains(train_number, train_name) " +
                                "VALUES(12110, 'Panchavati Express')"
                );

                statement.execute(
                        "INSERT OR IGNORE INTO trains(train_number, train_name) " +
                                "VALUES(12124, 'Deccan Queen')"
                );


                System.out.println("Database setup completed successfully!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

