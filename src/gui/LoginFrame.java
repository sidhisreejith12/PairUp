
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

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private final Color DARK_ROSE = new Color(155, 57, 91);
    private final Color ROSE = new Color(204, 100, 137);
    private final Color BLUSH = new Color(255, 232, 239);
    private final Color CREAM = new Color(255, 249, 247);
    private final Color TEXT = new Color(91, 55, 69);

    public LoginFrame() {

        setTitle("PairUp | Welcome Back");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(CREAM);

        // LEFT SIDE - BRAND SECTION

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

        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(
            BorderFactory.createEmptyBorder(65, 35, 50, 35)
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
            + "Where meaningful<br>connections begin."
            + "</div></html>"
        );

        tagline.setFont(new Font("SansSerif", Font.PLAIN, 20));
        tagline.setForeground(Color.WHITE);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel decoration = new JLabel(
            "<html><div style='text-align:center;'>"
            + "♡ &nbsp; Discover &nbsp; Connect &nbsp; Belong &nbsp; ♡"
            + "</div></html>"
        );

        decoration.setFont(new Font("SansSerif", Font.ITALIC, 13));
        decoration.setForeground(new Color(255, 240, 245));
        decoration.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandPanel.add(Box.createVerticalGlue());
        brandPanel.add(heart);
        brandPanel.add(Box.createVerticalStrut(12));
        brandPanel.add(brandTitle);
        brandPanel.add(Box.createVerticalStrut(15));
        brandPanel.add(tagline);
        brandPanel.add(Box.createVerticalStrut(35));
        brandPanel.add(decoration);
        brandPanel.add(Box.createVerticalGlue());

        // RIGHT SIDE - LOGIN FORM

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(CREAM);

        JPanel formCard = new RoundedPanel(25, Color.WHITE);

        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(
            BorderFactory.createEmptyBorder(35, 35, 35, 35)
        );

        formCard.setPreferredSize(new Dimension(340, 410));

        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 27));
        welcomeLabel.setForeground(DARK_ROSE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Your next connection awaits ♡");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(145, 117, 128));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formCard.add(welcomeLabel);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(subtitleLabel);
        formCard.add(Box.createVerticalStrut(35));

        // EMAIL

        JLabel emailLabel = createLabel("EMAIL ADDRESS");

        emailField = createTextField();

        formCard.add(emailLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(emailField);
        formCard.add(Box.createVerticalStrut(22));

        // PASSWORD

        JLabel passwordLabel = createLabel("PASSWORD");

        passwordField = new JPasswordField();
        styleTextField(passwordField);

        formCard.add(passwordLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(passwordField);
        formCard.add(Box.createVerticalStrut(30));

        // LOGIN BUTTON

        loginButton = new JButton("LOGIN  ♥");
        stylePrimaryButton(loginButton);

        loginButton.addActionListener(e -> loginUser());

        formCard.add(loginButton);
        formCard.add(Box.createVerticalStrut(20));

        // REGISTER SECTION

        JLabel registerLabel = new JLabel(
            "New to PairUp? Create your account"
        );

        registerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        registerLabel.setForeground(TEXT);
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton = new JButton("CREATE ACCOUNT");
        styleSecondaryButton(registerButton);

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        formCard.add(registerLabel);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(registerButton);

        rightPanel.add(formCard);

        mainPanel.add(brandPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);

        getRootPane().setDefaultButton(loginButton);

        setVisible(true);
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(DARK_ROSE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private JTextField createTextField() {

        JTextField field = new JTextField();

        styleTextField(field);

        return field;
    }

    private void styleTextField(JTextField field) {

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(new Color(255, 247, 249));
        field.setCaretColor(DARK_ROSE);

        field.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(new Color(239, 205, 216), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            )
        );

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        field.setPreferredSize(new Dimension(260, 43));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void stylePrimaryButton(JButton button) {

        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(ROSE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        button.setPreferredSize(new Dimension(260, 46));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(DARK_ROSE);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(ROSE);
            }
        });
    }

    private void styleSecondaryButton(JButton button) {

        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(DARK_ROSE);
        button.setBackground(new Color(255, 238, 243));
        button.setFocusPainted(false);
        button.setBorder(
            new LineBorder(new Color(231, 177, 196), 1, true)
        );
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setPreferredSize(new Dimension(260, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // Rounded white login card

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
            g2.fill(new RoundRectangle2D.Float(
                3, 5, getWidth() - 6, getHeight() - 8,
                radius, radius
            ));

            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Float(
                0, 0, getWidth() - 6, getHeight() - 10,
                radius, radius
            ));

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private void loginUser() {

        String email = emailField.getText().trim();

        String password = new String(
            passwordField.getPassword()
        );

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter both email and password."
            );

            return;
        }

        String sql =
            "SELECT name FROM users WHERE email = ? AND password = ?";

        try (
            Connection connection = DatabaseConnection.getConnection()
        ) {

            if (connection == null) {
                throw new SQLException("Unable to connect to database.");
            }

            try (
                PreparedStatement statement =
                    connection.prepareStatement(sql)
            ) {

                statement.setString(1, email);
                statement.setString(2, password);

                try (ResultSet result = statement.executeQuery()) {

                    if (result.next()) {

                        String name = result.getString("name");
                        String userEmail = email;

                        JOptionPane.showMessageDialog(
                            this,
                            "Login successful! Welcome " + name
                        );

                        dispose();
                        new HomeFrame(name, userEmail);

                    } else {

                        JOptionPane.showMessageDialog(
                            this,
                            "Invalid email or password."
                        );
                    }
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Login failed!\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
