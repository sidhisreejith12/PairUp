
package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import database.DatabaseConnection;

public class FindMatchesFrame extends JFrame {

    private String email;

    private int currentUserId;
    private int candidateUserId;

    private JLabel nameLabel;
    private JLabel ageLabel;
    private JLabel genderLabel;
    private JLabel statusLabel;
    private JLabel pictureLabel;
    private JLabel compatibilityLabel;

    private JButton likeButton;
    private JButton passButton;

    private final Color darkRose = new Color(160, 91, 119);
    private final Color pink = new Color(221, 139, 166);
    private final Color backgroundPink = new Color(255, 241, 245);
    private final Color textColor = new Color(85, 58, 68);

    public FindMatchesFrame(String email) {

        this.email = email;

        setTitle("PairUp - Find Matches");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundPink);
        mainPanel.setBorder(new EmptyBorder(25, 45, 25, 45));

        // HEADER
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(backgroundPink);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("DISCOVER PEOPLE");
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(darkRose);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Someone interesting might be waiting for you...");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitle);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // PROFILE CARD
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(240, 205, 216), 2, true),
            new EmptyBorder(22, 35, 22, 35)
        ));

        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setBackground(backgroundPink);
        cardWrapper.add(card);

        // PROFILE PICTURE
        pictureLabel = new JLabel();
        pictureLabel.setPreferredSize(new Dimension(155, 155));
        pictureLabel.setMinimumSize(new Dimension(155, 155));
        pictureLabel.setMaximumSize(new Dimension(155, 155));
        pictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pictureLabel.setVerticalAlignment(SwingConstants.CENTER);
        pictureLabel.setOpaque(true);
        pictureLabel.setBackground(new Color(255, 226, 235));
        pictureLabel.setBorder(new LineBorder(pink, 3, true));
        pictureLabel.setText("♥");
        pictureLabel.setFont(new Font("Serif", Font.BOLD, 65));
        pictureLabel.setForeground(darkRose);
        pictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(pictureLabel);
        card.add(Box.createVerticalStrut(15));

        // NAME
        nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("Serif", Font.BOLD, 27));
        nameLabel.setForeground(darkRose);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(12));

        // AGE AND GENDER
        ageLabel = createDetailLabel("");
        genderLabel = createDetailLabel("");

        card.add(ageLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(genderLabel);
        card.add(Box.createVerticalStrut(15));

        // COMPATIBILITY
        compatibilityLabel = new JLabel("");
        compatibilityLabel.setFont(new Font("Arial", Font.BOLD, 15));
        compatibilityLabel.setForeground(darkRose);
        compatibilityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(compatibilityLabel);
        card.add(Box.createVerticalStrut(12));

        // STATUS
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(textColor);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(statusLabel);
        card.add(Box.createVerticalStrut(22));

        // LIKE AND PASS BUTTONS
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonPanel.setBackground(Color.WHITE);

        likeButton = createButton("♥  LIKE", darkRose);
        passButton = createButton("PASS", new Color(130, 120, 125));

        buttonPanel.add(likeButton);
        buttonPanel.add(passButton);

        card.add(buttonPanel);

        mainPanel.add(cardWrapper, BorderLayout.CENTER);

        // BACK BUTTON
        JButton backButton = createButton("BACK TO HOME", new Color(190, 130, 150));
        backButton.setPreferredSize(new Dimension(170, 38));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(backgroundPink);
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        likeButton.addActionListener(e -> saveAction("LIKE"));
        passButton.addActionListener(e -> saveAction("PASS"));

        backButton.addActionListener(e -> openHome());

        add(mainPanel);
        setVisible(true);

        loadNextProfile();
    }

    private JLabel createDetailLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        return button;
    }

    // LOAD THE NEXT SUITABLE PROFILE
    private void loadNextProfile() {

        String sql =
            "SELECT me.user_id AS current_user_id, " +
            "u.user_id, u.name, u.age, u.gender, u.profile_picture " +
            "FROM users me " +
            "JOIN users u ON u.user_id <> me.user_id " +
            "WHERE me.email = ? " +

            "AND (me.preferred_gender = 'Any' " +
            "OR me.preferred_gender = u.gender) " +

            "AND (u.preferred_gender = 'Any' " +
            "OR u.preferred_gender = me.gender) " +

            "AND u.age BETWEEN me.min_preferred_age " +
            "AND me.max_preferred_age " +

            "AND me.age BETWEEN u.min_preferred_age " +
            "AND u.max_preferred_age " +

            "AND NOT EXISTS (" +
            "SELECT 1 FROM user_actions a " +
            "WHERE a.from_user_id = me.user_id " +
            "AND a.to_user_id = u.user_id) " +

            "ORDER BY u.user_id";

        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
            return;
        }

        try (connection;
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    currentUserId = result.getInt("current_user_id");
                    candidateUserId = result.getInt("user_id");

                    nameLabel.setText(result.getString("name"));
                    ageLabel.setText("Age: " + result.getInt("age"));
                    genderLabel.setText("Gender: " + result.getString("gender"));

                    loadProfilePicture(result.getString("profile_picture"));
                    loadCompatibility();

                    statusLabel.setText("");

                    likeButton.setEnabled(true);
                    passButton.setEnabled(true);

                } else {

                    candidateUserId = 0;

                    nameLabel.setText("No more profiles!");
                    ageLabel.setText("");
                    genderLabel.setText("");

                    pictureLabel.setIcon(null);
                    pictureLabel.setText("♥");

                    compatibilityLabel.setText("");
                    statusLabel.setText("Check back later for new people!");

                    likeButton.setEnabled(false);
                    passButton.setEnabled(false);
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this, "Error loading profiles: " + ex.getMessage()
            );
        }
    }

    // LOAD CANDIDATE PROFILE PICTURE
    private void loadProfilePicture(String imagePath) {

        pictureLabel.setIcon(null);
        pictureLabel.setText("♥");

        if (imagePath == null || imagePath.trim().isEmpty()) {
            return;
        }

        try {

            File imageFile = new File(imagePath);

            if (!imageFile.isAbsolute()) {
                imageFile = new File(System.getProperty("user.dir"), imagePath);
            }

            if (imageFile.exists()) {

                ImageIcon original = new ImageIcon(imageFile.getAbsolutePath());

                Image scaled = original.getImage().getScaledInstance(
                    145, 145, Image.SCALE_SMOOTH
                );

                pictureLabel.setText("");
                pictureLabel.setIcon(new ImageIcon(scaled));
            }

        } catch (Exception ex) {

            pictureLabel.setIcon(null);
            pictureLabel.setText("♥");
        }
    }

    // CALCULATE INTEREST COMPATIBILITY
    private void loadCompatibility() {

        Set<String> myInterests = getInterests(currentUserId);
        Set<String> candidateInterests = getInterests(candidateUserId);

        if (myInterests.isEmpty() || candidateInterests.isEmpty()) {

            compatibilityLabel.setText(
                "Add interests to see compatibility"
            );

            return;
        }

        Set<String> common = new HashSet<>(myInterests);
        common.retainAll(candidateInterests);

        Set<String> combined = new HashSet<>(myInterests);
        combined.addAll(candidateInterests);

        int percentage = (int) Math.round(
            (common.size() * 100.0) / combined.size()
        );

        compatibilityLabel.setText(
            "♡ " + percentage + "% shared-interest compatibility"
        );
    }

    // GET INTERESTS FROM DATABASE
    private Set<String> getInterests(int userId) {

        Set<String> interests = new HashSet<>();

        String sql =
            "SELECT interest_name FROM user_interests WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection == null) {
                return interests;
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, userId);

                try (ResultSet result = statement.executeQuery()) {

                    while (result.next()) {
                        interests.add(result.getString("interest_name"));
                    }
                }
            }

        } catch (SQLException ex) {

            System.out.println("Could not load interests: " + ex.getMessage());
        }

        return interests;
    }

    // SAVE LIKE OR PASS
    private void saveAction(String action) {

        if (candidateUserId == 0) {
            return;
        }

        String insertSQL =
            "INSERT INTO user_actions " +
            "(from_user_id, to_user_id, action) " +
            "VALUES (?, ?, ?)";

        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
            return;
        }

        try (connection) {

            String message = "Your action has been saved!";

            try (PreparedStatement statement =
                     connection.prepareStatement(insertSQL)) {

                statement.setInt(1, currentUserId);
                statement.setInt(2, candidateUserId);
                statement.setString(3, action);

                statement.executeUpdate();
            }

            if (action.equals("LIKE")) {

                String checkSQL =
                    "SELECT * FROM user_actions " +
                    "WHERE from_user_id = ? " +
                    "AND to_user_id = ? " +
                    "AND action = 'LIKE'";

                try (PreparedStatement checkStatement =
                         connection.prepareStatement(checkSQL)) {

                    checkStatement.setInt(1, candidateUserId);
                    checkStatement.setInt(2, currentUserId);

                    try (ResultSet result = checkStatement.executeQuery()) {

                        if (result.next()) {
                            message = "IT'S A MATCH! ❤️";
                        }
                    }
                }
            }

            JOptionPane.showMessageDialog(this, message);

            loadNextProfile();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this, "Error saving action: " + ex.getMessage()
            );
        }
    }

    // RETURN TO HOME
    private void openHome() {

        String name = "";

        String sql = "SELECT name FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection != null) {

                try (PreparedStatement statement = connection.prepareStatement(sql)) {

                    statement.setString(1, email);

                    try (ResultSet result = statement.executeQuery()) {

                        if (result.next()) {
                            name = result.getString("name");
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            System.out.println("Could not load user name.");
        }

        dispose();
        new HomeFrame(name, email);
    }
}
