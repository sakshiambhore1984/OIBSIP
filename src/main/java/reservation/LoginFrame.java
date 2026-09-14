package reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

    public class LoginFrame extends JFrame {

        JTextField usernameField;
        JPasswordField passwordField;
        JButton loginButton;

        public LoginFrame() {

            setTitle("Online Reservation System - Login");
            setSize(400, 250);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            JLabel usernameLabel = new JLabel("Username:");
            JLabel passwordLabel = new JLabel("Password:");

            usernameField = new JTextField();
            passwordField = new JPasswordField();

            loginButton = new JButton("LOGIN");

            panel.add(usernameLabel);
            panel.add(usernameField);

            panel.add(passwordLabel);
            panel.add(passwordField);

            panel.add(new JLabel());
            panel.add(loginButton);

            add(panel);

            // Login button event
            loginButton.addActionListener(e -> login());
        }

        private void login() {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter username and password."
                );
                return;
            }

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Login Successful!"
                    );

                    dispose();

                    new ReservationFrame();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Access Denied! Invalid username or password."
                    );
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database Error: " + e.getMessage()
                );
            }
        }

        public static void main(String[] args) {

            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }

