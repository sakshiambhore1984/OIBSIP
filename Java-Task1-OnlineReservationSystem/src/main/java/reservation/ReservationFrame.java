package reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReservationFrame extends JFrame {

    JTextField passengerField;
    JTextField trainNumberField;
    JTextField trainNameField;
    JTextField dateField;
    JTextField sourceField;
    JTextField destinationField;

    JComboBox<String> classBox;

    JButton bookButton;

    public ReservationFrame() {

        setTitle("Online Reservation System - Reservation");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        // Passenger Name
        panel.add(new JLabel("Passenger Name:"));
        passengerField = new JTextField();
        panel.add(passengerField);

        // Train Number
        panel.add(new JLabel("Train Number:"));
        trainNumberField = new JTextField();
        panel.add(trainNumberField);

        // Train Name
        panel.add(new JLabel("Train Name:"));
        trainNameField = new JTextField();
        trainNameField.setEditable(false);
        panel.add(trainNameField);

        // Class
        panel.add(new JLabel("Class Type:"));

        String[] classes = {
                "AC",
                "Sleeper",
                "General"
        };

        classBox = new JComboBox<>(classes);
        panel.add(classBox);

        // Date
        panel.add(new JLabel("Journey Date:"));
        dateField = new JTextField();
        panel.add(dateField);

        // Source
        panel.add(new JLabel("Source Station:"));
        sourceField = new JTextField();
        panel.add(sourceField);

        // Destination
        panel.add(new JLabel("Destination Station:"));
        destinationField = new JTextField();
        panel.add(destinationField);

        // Book button
        bookButton = new JButton("BOOK TICKET");
        panel.add(new JLabel());
        panel.add(bookButton);

        add(panel);

        // Train number listener
        trainNumberField.addActionListener(e -> findTrain());

        // Book button listener
        bookButton.addActionListener(e -> bookTicket());

        setVisible(true);
    }


    // Find train name
    private void findTrain() {

        String trainNumberText = trainNumberField.getText().trim();

        if (trainNumberText.isEmpty()) {
            return;
        }

        try {

            int trainNumber = Integer.parseInt(trainNumberText);

            String sql =
                    "SELECT train_name FROM trains WHERE train_number = ?";

            try (Connection connection =
                         DatabaseConnection.getConnection();
                 PreparedStatement ps =
                         connection.prepareStatement(sql)) {

                ps.setInt(1, trainNumber);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    trainNameField.setText(
                            rs.getString("train_name")
                    );

                } else {

                    trainNameField.setText("Train Not Found");
                }
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Train number must be numeric."
            );
        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage()
            );
        }
    }


    // Book ticket
  /*  private void bookTicket() {

        String passengerName = passengerField.getText().trim();
        String trainNumberText = trainNumberField.getText().trim();
        String trainName = trainNameField.getText().trim();
        String classType = (String) classBox.getSelectedItem();
        String journeyDate = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();


        // Validation
        if (passengerName.isEmpty()
                || trainNumberText.isEmpty()
                || journeyDate.isEmpty()
                || source.isEmpty()
                || destination.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields."
            );

            return;
        }


        int trainNumber;

        try {

            trainNumber = Integer.parseInt(trainNumberText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Train number must be numeric."
            );

            return;
        }


        if (trainName.equals("Train Not Found")
                || trainName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid train number."
            );

            return;
        }


        String sql = """
                INSERT INTO reservations
                (passenger_name, train_number, train_name,
                 class_type, journey_date, source, destination)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;


        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement(
                             sql,
                             java.sql.Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setString(1, passengerName);
            ps.setInt(2, trainNumber);
            ps.setString(3, trainName);
            ps.setString(4, classType);
            ps.setString(5, journeyDate);
            ps.setString(6, source);
            ps.setString(7, destination);

            ps.executeUpdate();


            // Get generated PNR
            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {

                int pnr = keys.getInt(1);

                JOptionPane.showMessageDialog(
                        this,
                        "Booking Successful!\n\n"
                                + "PNR: " + pnr + "\n"
                                + "Passenger: " + passengerName + "\n"
                                + "Train: " + trainName + "\n"
                                + "Class: " + classType + "\n"
                                + "Date: " + journeyDate + "\n"
                                + "From: " + source + "\n"
                                + "To: " + destination
                );
            }

            clearForm();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Booking Error: " + e.getMessage()
            );
        }
    }*/
    private void bookTicket() {

        String passengerName = passengerField.getText().trim();
        String trainNumberText = trainNumberField.getText().trim();
        String classType = (String) classBox.getSelectedItem();
        String journeyDate = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();

        // Basic validation
        if (passengerName.isEmpty()
                || trainNumberText.isEmpty()
                || journeyDate.isEmpty()
                || source.isEmpty()
                || destination.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields."
            );

            return;
        }

        int trainNumber;

        try {
            trainNumber = Integer.parseInt(trainNumberText);
        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Train number must be numeric."
            );

            return;
        }

        // Fetch train name automatically
        String trainName = "";

        String trainSql =
                "SELECT train_name FROM trains WHERE train_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement trainPs =
                     connection.prepareStatement(trainSql)) {

            trainPs.setInt(1, trainNumber);

            ResultSet rs = trainPs.executeQuery();

            if (rs.next()) {
                trainName = rs.getString("train_name");
                trainNameField.setText(trainName);
            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Train number " + trainNumber + " not found."
                );

                return;
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage()
            );

            return;
        }


        // Insert reservation
        String sql = """
            INSERT INTO reservations
            (passenger_name, train_number, train_name,
             class_type, journey_date, source, destination)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement(
                             sql,
                             java.sql.Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setString(1, passengerName);
            ps.setInt(2, trainNumber);
            ps.setString(3, trainName);
            ps.setString(4, classType);
            ps.setString(5, journeyDate);
            ps.setString(6, source);
            ps.setString(7, destination);

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {

                int pnr = keys.getInt(1);

                JOptionPane.showMessageDialog(
                        this,
                        "Booking Successful!\n\n"
                                + "PNR: " + pnr + "\n"
                                + "Passenger: " + passengerName + "\n"
                                + "Train: " + trainName + "\n"
                                + "Class: " + classType + "\n"
                                + "Date: " + journeyDate + "\n"
                                + "From: " + source + "\n"
                                + "To: " + destination
                );
            }

            clearForm();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Booking Error: " + e.getMessage()
            );
        }
    }


    // Clear form
    private void clearForm() {

        passengerField.setText("");
        trainNumberField.setText("");
        trainNameField.setText("");
        dateField.setText("");
        sourceField.setText("");
        destinationField.setText("");

        classBox.setSelectedIndex(0);
    }
}