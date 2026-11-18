
package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField ageField;
    private JComboBox<String> genderBox;

    private final Color DARK_ROSE = new Color(155, 57, 91);
    private final Color ROSE = new Color(204, 100, 137);
    private final Color CREAM = new Color(255, 249, 247);
    private final Color TEXT = new Color(91, 55, 69);

    public RegisterFrame() {

        setTitle("PairUp | Create Account");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(CREAM);

        // LEFT SIDE - BRAND PANEL

        JPanel brandPanel = new JPanel() {

            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(242, 183, 202),
                    getWidth(), getHeight(),
                    new Color(184, 87, 126)
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        };

        brandPanel.setLayout(
            new BoxLayout(brandPanel, BoxLayout.Y_AXIS)
        );

        brandPanel.setBorder(
            BorderFactory.createEmptyBorder(65, 30, 50, 30)
        );

        JLabel heart = new JLabel("♥");
        heart.setFont(new Font("Serif", Font.PLAIN, 65));
        heart.setForeground(Color.WHITE);
        heart.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandTitle = new JLabel("PAIR UP");
        brandTitle.setFont(new Font("Serif", Font.BOLD, 43));
        brandTitle.setForeground(Color.WHITE);
        brandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Every great connection<br>"
            + "starts somewhere."
            + "</div></html>"
        );

        tagline.setFont(new Font("SansSerif", Font.PLAIN, 20));
        tagline.setForeground(Color.WHITE);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel decoration = new JLabel("♡  Meet. Connect. Belong.  ♡");
        decoration.setFont(
            new Font("SansSerif", Font.ITALIC, 14)
        );
        decoration.setForeground(new Color(255, 240, 245));
        decoration.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandPanel.add(Box.createVerticalGlue());
        brandPanel.add(heart);
        brandPanel.add(Box.createVerticalStrut(12));
        brandPanel.add(brandTitle);
        brandPanel.add(Box.createVerticalStrut(18));
        brandPanel.add(tagline);
        brandPanel.add(Box.createVerticalStrut(35));
        brandPanel.add(decoration);
        brandPanel.add(Box.createVerticalGlue());

        // RIGHT SIDE - REGISTRATION FORM

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(CREAM);

        RoundedPanel formCard = new RoundedPanel(
            25, Color.WHITE
        );

        formCard.setLayout(
            new BoxLayout(formCard, BoxLayout.Y_AXIS)
        );

        formCard.setBorder(
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        );

        formCard.setPreferredSize(
            new Dimension(365, 565)
        );

        JLabel heading = new JLabel("Create Account");
        heading.setFont(
            new Font("SansSerif", Font.BOLD, 27)
        );
        heading.setForeground(DARK_ROSE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "Join PairUp and start connecting ♡"
        );
        subtitle.setFont(
            new Font("SansSerif", Font.PLAIN, 13)
        );
        subtitle.setForeground(new Color(145, 117, 128));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        formCard.add(heading);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(subtitle);
        formCard.add(Box.createVerticalStrut(23));

        // NAME

        nameField = createTextField();
        addField(formCard, "FULL NAME", nameField);

        // EMAIL

        emailField = createTextField();
        addField(formCard, "EMAIL ADDRESS", emailField);

        // PASSWORD

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        addField(formCard, "PASSWORD", passwordField);

        // AGE

        ageField = createTextField();
        addField(formCard, "AGE", ageField);

        // GENDER

        JLabel genderLabel = createLabel("GENDER");

        String[] genders = {
            "Select Gender",
            "Male",
            "Female",
            "Other"
        };

        genderBox = new JComboBox<>(genders);
        genderBox.setFont(
            new Font("SansSerif", Font.PLAIN, 13)
        );
        genderBox.setForeground(TEXT);
        genderBox.setBackground(
            new Color(255, 247, 249)
        );
        genderBox.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38)
        );
        genderBox.setPreferredSize(
            new Dimension(280, 38)
        );
        genderBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(genderLabel);
        formCard.add(Box.createVerticalStrut(6));
        formCard.add(genderBox);
        formCard.add(Box.createVerticalStrut(20));

        // REGISTER BUTTON

        JButton registerButton = new JButton("CREATE ACCOUNT  ♥");
        stylePrimaryButton(registerButton);

        registerButton.addActionListener(
            e -> registerUser()
        );

        formCard.add(registerButton);
        formCard.add(Box.createVerticalStrut(13));

        // LOGIN BUTTON

        JButton loginButton = new JButton(
            "Already have an account? Login"
        );

        styleSecondaryButton(loginButton);

        loginButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        formCard.add(loginButton);

        rightPanel.add(formCard);

        mainPanel.add(brandPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);

        getRootPane().setDefaultButton(registerButton);

        setVisible(true);
    }

    // CREATE TEXT FIELD

    private JTextField createTextField() {

        JTextField field = new JTextField();

        styleTextField(field);

        return field;
    }

    // STYLE TEXT FIELDS

    private void styleTextField(JTextField field) {

        field.setFont(
            new Font("SansSerif", Font.PLAIN, 13)
        );

        field.setForeground(TEXT);
        field.setBackground(
            new Color(255, 247, 249)
        );

        field.setCaretColor(DARK_ROSE);

        field.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(
                    new Color(239, 205, 216),
                    1,
                    true
                ),
                BorderFactory.createEmptyBorder(
                    8, 12, 8, 12
                )
            )
        );

        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38)
        );

        field.setPreferredSize(
            new Dimension(280, 38)
        );

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // CREATE LABEL

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("SansSerif", Font.BOLD, 10)
        );

        label.setForeground(DARK_ROSE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    // ADD LABEL AND FIELD

    private void addField(
        JPanel panel,
        String labelText,
        JTextField field
    ) {

        JLabel label = createLabel(labelText);

        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(field);
        panel.add(Box.createVerticalStrut(14));
    }

    // PRIMARY BUTTON

    private void stylePrimaryButton(JButton button) {

        button.setFont(
            new Font("SansSerif", Font.BOLD, 13)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(ROSE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        button.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 43)
        );

        button.setPreferredSize(
            new Dimension(280, 43)
        );

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(
            new java.awt.event.MouseAdapter() {

                public void mouseEntered(
                    java.awt.event.MouseEvent e
                ) {
                    button.setBackground(DARK_ROSE);
                }

                public void mouseExited(
                    java.awt.event.MouseEvent e
                ) {
                    button.setBackground(ROSE);
                }
            }
        );
    }

    // SECONDARY BUTTON

    private void styleSecondaryButton(JButton button) {

        button.setFont(
            new Font("SansSerif", Font.PLAIN, 12)
        );

        button.setForeground(DARK_ROSE);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        button.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 35)
        );

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // ROUNDED CARD

    class RoundedPanel extends JPanel {

        private int radius;
        private Color backgroundColor;

        RoundedPanel(int radius, Color backgroundColor) {

            this.radius = radius;
            this.backgroundColor = backgroundColor;

            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(100, 50, 70, 20));

            g2.fill(
                new RoundRectangle2D.Float(
                    3, 5,
                    getWidth() - 6,
                    getHeight() - 8,
                    radius, radius
                )
            );

            g2.setColor(backgroundColor);

            g2.fill(
                new RoundRectangle2D.Float(
                    0, 0,
                    getWidth() - 6,
                    getHeight() - 10,
                    radius, radius
                )
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // REGISTER USER

    private void registerUser() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        String password = new String(
            passwordField.getPassword()
        );

        String age = ageField.getText().trim();

        String gender = (String) genderBox.getSelectedItem();

        // CHECK EMPTY FIELDS

        if (name.isEmpty() ||
            email.isEmpty() ||
            password.isEmpty() ||
            age.isEmpty() ||
            gender.equals("Select Gender")) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill all fields."
            );

            return;
        }

        // CHECK EMAIL

        if (!email.matches(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid email address."
            );

            return;
        }

        // CHECK AGE

        int userAge;

        try {

            userAge = Integer.parseInt(age);

            if (userAge < 18 || userAge > 100) {

                JOptionPane.showMessageDialog(
                    this,
                    "Age must be between 18 and 100."
                );

                return;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Age must be a valid number."
            );

            return;
        }

        // CHECK DUPLICATE EMAIL

        String checkSql =
            "SELECT email FROM users WHERE email = ?";

        String insertSql =
            "INSERT INTO users " +
            "(name, email, password, age, gender) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection connection =
                DatabaseConnection.getConnection()
        ) {

            if (connection == null) {

                throw new SQLException(
                    "Database connection failed."
                );
            }

            try (
                PreparedStatement checkStatement =
                    connection.prepareStatement(checkSql)
            ) {

                checkStatement.setString(1, email);

                try (
                    ResultSet result =
                        checkStatement.executeQuery()
                ) {

                    if (result.next()) {

                        JOptionPane.showMessageDialog(
                            this,
                            "This email is already registered."
                        );

                        return;
                    }
                }
            }

            try (
                PreparedStatement statement =
                    connection.prepareStatement(insertSql)
            ) {

                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, password);
                statement.setInt(4, userAge);
                statement.setString(5, gender);

                statement.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                this,
                "Registration successful! Please login."
            );

            dispose();
            new LoginFrame();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Registration failed!\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new RegisterFrame();
    }
}
