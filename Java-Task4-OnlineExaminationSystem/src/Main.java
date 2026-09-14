import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    String displayName = "Student";
    String currentPassword = "1234";

    String[] questions = {
            "Which language is used for Java GUI development?",
            "Which component is used for multiple choice options?",
            "Which class is used for countdown timer?",
            "Which keyword is used to create an object?",
            "Which method is the entry point of a Java program?"
    };

    String[][] options = {
            {"Swing", "HTML", "CSS", "SQL"},
            {"JRadioButton", "JTextField", "JLabel", "JFrame"},
            {"Timer", "Scanner", "Random", "ArrayList"},
            {"new", "create", "object", "make"},
            {"main()", "start()", "run()", "execute()"}
    };

    int[] correctAnswers = {0, 0, 0, 0, 0};
    int[] selectedAnswers = {-1, -1, -1, -1, -1};

    int currentQuestion = 0;
    Timer examTimer;
    int remainingSeconds = 30 * 60;
    long examStartTime;

    JRadioButton option1;
    JRadioButton option2;
    JRadioButton option3;
    JRadioButton option4;

    JLabel questionLabel;
    JLabel questionNumberLabel;

    public Main() {
        showLoginScreen();
    }

    // ================= LOGIN =================

    private void showLoginScreen() {

        getContentPane().removeAll();

        setTitle("Online Examination System - Login");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        );

        JLabel titleLabel =
                new JLabel("ONLINE EXAMINATION SYSTEM");

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("LOGIN");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel());
        panel.add(loginButton);

        add(titleLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> login());

        revalidate();
        repaint();
    }

    private void login() {

        String username = usernameField.getText();
        String password =
                new String(passwordField.getPassword());

        if (username.equals("student")
                && password.equals(currentPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login Successful!"
            );

            showProfileScreen();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Username or Password!"
            );
        }
    }

    // ================= PROFILE =================

    private void showProfileScreen() {

        getContentPane().removeAll();

        setTitle("Profile Update");
        setSize(500, 350);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        );

        JLabel titleLabel =
                new JLabel("UPDATE PROFILE");

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField nameField =
                new JTextField(displayName);

        JPasswordField newPasswordField =
                new JPasswordField(currentPassword);

        JButton updateButton =
                new JButton("UPDATE");

        JButton startExamButton =
                new JButton("START EXAM");

        panel.add(new JLabel("Display Name:"));
        panel.add(nameField);

        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);

        panel.add(updateButton);
        panel.add(startExamButton);

        add(titleLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        updateButton.addActionListener(e -> {

            displayName = nameField.getText();

            currentPassword =
                    new String(newPasswordField.getPassword());

            JOptionPane.showMessageDialog(
                    this,
                    "Profile Updated Successfully!"
            );
        });

        startExamButton.addActionListener(e -> {

            currentQuestion = 0;

            for (int i = 0; i < selectedAnswers.length; i++) {
                selectedAnswers[i] = -1;
            }

            showExamScreen();
        });

        revalidate();
        repaint();
    }

    // ================= EXAM SCREEN =================

    private void showExamScreen() {

        getContentPane().removeAll();

        setTitle("Online Examination System - Exam");
        setSize(700, 500);
        remainingSeconds = 30 * 60;
        examStartTime = System.currentTimeMillis();

        JLabel timerLabel = new JLabel("Time Left: 30:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        questionNumberLabel =
                new JLabel();

        questionNumberLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        questionLabel =
                new JLabel();

        questionLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        JPanel questionPanel =
                new JPanel(new BorderLayout());

        questionPanel.add(
                questionNumberLabel,
                BorderLayout.NORTH
        );

        questionPanel.add(
                questionLabel,
                BorderLayout.CENTER
        );

        JPanel optionsPanel =
                new JPanel(new GridLayout(4, 1, 10, 10));

        option1 = new JRadioButton();
        option2 = new JRadioButton();
        option3 = new JRadioButton();
        option4 = new JRadioButton();

        ButtonGroup group = new ButtonGroup();

        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);

        optionsPanel.add(option1);
        optionsPanel.add(option2);
        optionsPanel.add(option3);
        optionsPanel.add(option4);

        JButton previousButton =
                new JButton("Previous");

        JButton nextButton =
                new JButton("Next");

        JButton submitButton =
                new JButton("Submit Exam");

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);

        examTimer = new Timer(1000, e -> {

            remainingSeconds--;

            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;

            timerLabel.setText(
                    String.format("Time Left: %02d:%02d", minutes, seconds)
            );

            if (remainingSeconds <= 0) {

                examTimer.stop();

                JOptionPane.showMessageDialog(
                        this,
                        "Time is over! Exam will be submitted automatically."
                );

                saveAnswer();
                showResultScreen();
            }
        });

        examTimer.start();

        mainPanel.add(
                timerLabel,
                BorderLayout.EAST
        );

        mainPanel.add(
                questionPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                optionsPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        loadQuestion();

        previousButton.addActionListener(e -> {

            saveAnswer();

            if (currentQuestion > 0) {
                currentQuestion--;
                loadQuestion();
            }
        });

        nextButton.addActionListener(e -> {

            saveAnswer();

            if (currentQuestion < questions.length - 1) {
                currentQuestion++;
                loadQuestion();
            }
        });

        submitButton.addActionListener(e -> {

            saveAnswer();

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to submit?",
                    "Confirm Submission",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {

                if (examTimer != null) {
                    examTimer.stop();
                }

                showResultScreen();
            }
        });

        revalidate();
        repaint();
    }

    // ================= LOAD QUESTION =================

    private void loadQuestion() {

        questionNumberLabel.setText(
                "Question " + (currentQuestion + 1)
                        + " / " + questions.length
        );

        questionLabel.setText(
                questions[currentQuestion]
        );

        option1.setText(
                options[currentQuestion][0]
        );

        option2.setText(
                options[currentQuestion][1]
        );

        option3.setText(
                options[currentQuestion][2]
        );

        option4.setText(
                options[currentQuestion][3]
        );

        ButtonGroup group = new ButtonGroup();

        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);

        int answer = selectedAnswers[currentQuestion];

        if (answer == 0) {
            option1.setSelected(true);
        } else if (answer == 1) {
            option2.setSelected(true);
        } else if (answer == 2) {
            option3.setSelected(true);
        } else if (answer == 3) {
            option4.setSelected(true);
        }
    }

    // ================= SAVE ANSWER =================

    private void saveAnswer() {

        if (option1.isSelected()) {
            selectedAnswers[currentQuestion] = 0;
        } else if (option2.isSelected()) {
            selectedAnswers[currentQuestion] = 1;
        } else if (option3.isSelected()) {
            selectedAnswers[currentQuestion] = 2;
        } else if (option4.isSelected()) {
            selectedAnswers[currentQuestion] = 3;
        }
    }

    // ================= RESULT =================

    private void showResultScreen() {

        getContentPane().removeAll();

        int score = 0;

        for (int i = 0; i < questions.length; i++) {

            if (selectedAnswers[i] == correctAnswers[i]) {
                score++;
            }
        }

        setTitle("Exam Result");
        setSize(500, 400);

        JPanel panel = new JPanel(
                new BorderLayout(10, 10)
        );

        JLabel resultLabel =
                new JLabel(
                        "Your Score: "
                                + score
                                + " out of "
                                + questions.length,
                        SwingConstants.CENTER
                );

        resultLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        JTextArea breakdown =
                new JTextArea();

        breakdown.setEditable(false);

        for (int i = 0; i < questions.length; i++) {

            if (selectedAnswers[i] == correctAnswers[i]) {

                breakdown.append(
                        "Question "
                                + (i + 1)
                                + ": Correct\n"
                );

            } else {

                breakdown.append(
                        "Question "
                                + (i + 1)
                                + ": Incorrect\n"
                );
            }
        }

        JButton logoutButton =
                new JButton("LOGOUT");

        panel.add(
                resultLabel,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(breakdown),
                BorderLayout.CENTER
        );

        panel.add(
                logoutButton,
                BorderLayout.SOUTH
        );

        add(panel);

        logoutButton.addActionListener(e -> {

            showLoginScreen();

        });

        revalidate();
        repaint();
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Main frame = new Main();

            frame.setVisible(true);
        });
    }
}