
package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import database.DatabaseConnection;

public class MyMatchesFrame extends JFrame {

    private String email;

    private JPanel matchesPanel;
    private JLabel countLabel;

    private final Color darkRose = new Color(160, 91, 119);
    private final Color backgroundPink = new Color(255, 241, 245);
    private final Color textColor = new Color(85, 58, 68);
    private final Color lightPink = new Color(255, 226, 235);

    public MyMatchesFrame(String email) {

        this.email = email;

        setTitle("PairUp - My Matches");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundPink);
        mainPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        // HEADER
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(backgroundPink);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("MY MATCHES ♥");
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(darkRose);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "Your connections, all in one place"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        countLabel = new JLabel("Loading your matches...");
        countLabel.setFont(new Font("Arial", Font.BOLD, 14));
        countLabel.setForeground(darkRose);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(7));
        headerPanel.add(subtitle);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(countLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // MATCH CARDS PANEL
        matchesPanel = new JPanel();
        matchesPanel.setBackground(backgroundPink);
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(matchesPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(backgroundPink);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // BACK BUTTON
        JButton backButton = createButton(
            "BACK TO HOME", new Color(190, 130, 150)
        );

        backButton.setPreferredSize(new Dimension(180, 40));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(backgroundPink);
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> openHome());

        add(mainPanel);

        loadMatches();

        setVisible(true);
    }

    // BUTTON DESIGN
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

    // LOAD MUTUAL MATCHES
    private void loadMatches() {

        String sql =
            "SELECT u.user_id, u.name, u.age, u.gender, u.profile_picture " +
            "FROM users me " +
            "JOIN user_actions a " +
            "ON a.from_user_id = me.user_id " +
            "AND a.action = 'LIKE' " +
            "JOIN user_actions b " +
            "ON b.from_user_id = a.to_user_id " +
            "AND b.to_user_id = me.user_id " +
            "AND b.action = 'LIKE' " +
            "JOIN users u ON u.user_id = a.to_user_id " +
            "WHERE me.email = ? " +
            "ORDER BY u.name";

        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {
            JOptionPane.showMessageDialog(
                this, "Database connection failed."
            );
            countLabel.setText("Unable to load matches.");
            return;
        }

        try (connection;
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {

                int count = 0;

                while (result.next()) {

                    int id = result.getInt("user_id");
                    String name = result.getString("name");
                    int age = result.getInt("age");
                    String gender = result.getString("gender");
                    String picture = result.getString("profile_picture");

                    int compatibility = getCompatibility(id);

                    addMatchCard(
                        id, name, age, gender, picture, compatibility
                    );

                    count++;
                }

                countLabel.setText(
                    count + (count == 1 ? " mutual match" : " mutual matches")
                );

                if (count == 0) {
                    showEmptyMessage();
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this, "Error loading matches: " + ex.getMessage()
            );
        }
    }

    // CREATE A MATCH CARD
    private void addMatchCard(
        int matchId,
        String name,
        int age,
        String gender,
        String picture,
        int compatibility
    ) {

        JButton card = new JButton();
        card.setLayout(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(240, 205, 216), 2, true),
            new EmptyBorder(12, 18, 12, 18)
        ));
        card.setFocusPainted(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(650, 125));
        card.setPreferredSize(new Dimension(650, 125));

        // PROFILE PICTURE
        JLabel pictureLabel = new JLabel();
        pictureLabel.setPreferredSize(new Dimension(90, 90));
        pictureLabel.setMinimumSize(new Dimension(90, 90));
        pictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pictureLabel.setVerticalAlignment(SwingConstants.CENTER);
        pictureLabel.setOpaque(true);
        pictureLabel.setBackground(lightPink);
        pictureLabel.setBorder(new LineBorder(new Color(221, 139, 166), 2, true));
        pictureLabel.setText("♥");
        pictureLabel.setFont(new Font("Serif", Font.BOLD, 42));
        pictureLabel.setForeground(darkRose);

        loadPicture(picture, pictureLabel);

        card.add(pictureLabel, BorderLayout.WEST);

        // MATCH DETAILS
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 23));
        nameLabel.setForeground(darkRose);

        JLabel detailsLabel = new JLabel(
            "Age: " + age + "     |     Gender: " + gender
        );
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsLabel.setForeground(textColor);

        JLabel compatibilityLabel = new JLabel(
            "♡ " + compatibility + "% shared-interest compatibility"
        );
        compatibilityLabel.setFont(new Font("Arial", Font.BOLD, 13));
        compatibilityLabel.setForeground(darkRose);

        JLabel chatLabel = new JLabel("Click to start chatting →");
        chatLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        chatLabel.setForeground(new Color(150, 120, 130));

        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(7));
        detailsPanel.add(detailsLabel);
        detailsPanel.add(Box.createVerticalStrut(7));
        detailsPanel.add(compatibilityLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(chatLabel);

        card.add(detailsPanel, BorderLayout.CENTER);

        // OPEN CHAT WHEN CARD IS CLICKED
        card.addActionListener(e -> {

            dispose();
            new ChatFrame(email, matchId, name);
        });

        matchesPanel.add(Box.createVerticalStrut(12));
        matchesPanel.add(card);
        matchesPanel.add(Box.createVerticalStrut(5));
    }

    // LOAD PROFILE PICTURE
    private void loadPicture(String imagePath, JLabel pictureLabel) {

        if (imagePath == null || imagePath.trim().isEmpty()) {
            return;
        }

        try {

            File imageFile = new File(imagePath);

            if (!imageFile.isAbsolute()) {
                imageFile = new File(
                    System.getProperty("user.dir"), imagePath
                );
            }

            if (imageFile.exists()) {

                ImageIcon original = new ImageIcon(
                    imageFile.getAbsolutePath()
                );

                Image scaled = original.getImage().getScaledInstance(
                    85, 85, Image.SCALE_SMOOTH
                );

                pictureLabel.setText("");
                pictureLabel.setIcon(new ImageIcon(scaled));
            }

        } catch (Exception ex) {

            pictureLabel.setIcon(null);
            pictureLabel.setText("♥");
        }
    }

    // CALCULATE SHARED-INTEREST COMPATIBILITY
    private int getCompatibility(int matchId) {

        int currentUserId = getCurrentUserId();

        if (currentUserId == -1) {
            return 0;
        }

        Set<String> myInterests = getInterests(currentUserId);
        Set<String> matchInterests = getInterests(matchId);

        if (myInterests.isEmpty() || matchInterests.isEmpty()) {
            return 0;
        }

        Set<String> common = new HashSet<>(myInterests);
        common.retainAll(matchInterests);

        Set<String> combined = new HashSet<>(myInterests);
        combined.addAll(matchInterests);

        return (int) Math.round(
            (common.size() * 100.0) / combined.size()
        );
    }

    // GET CURRENT USER ID
    private int getCurrentUserId() {

        String sql = "SELECT user_id FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection == null) {
                return -1;
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, email);

                try (ResultSet result = statement.executeQuery()) {

                    if (result.next()) {
                        return result.getInt("user_id");
                    }
                }
            }

        } catch (SQLException ex) {

            System.out.println("Could not find current user.");
        }

        return -1;
    }

    // GET USER INTERESTS
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

            System.out.println("Could not load interests.");
        }

        return interests;
    }

    // MESSAGE WHEN THERE ARE NO MATCHES
    private void showEmptyMessage() {

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(240, 205, 216), 2, true),
            new EmptyBorder(35, 25, 35, 25)
        ));

        JLabel heart = new JLabel("♥");
        heart.setFont(new Font("Serif", Font.BOLD, 45));
        heart.setForeground(darkRose);
        heart.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel("No mutual matches yet!");
        message.setFont(new Font("Serif", Font.BOLD, 23));
        message.setForeground(darkRose);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "Keep discovering people. Your connection might be next!"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        emptyPanel.add(heart);
        emptyPanel.add(Box.createVerticalStrut(10));
        emptyPanel.add(message);
        emptyPanel.add(Box.createVerticalStrut(8));
        emptyPanel.add(subtitle);

        emptyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyPanel.setMaximumSize(new Dimension(650, 200));

        matchesPanel.add(Box.createVerticalStrut(30));
        matchesPanel.add(emptyPanel);
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
