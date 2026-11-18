
package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.*;
import java.sql.*;
import java.util.UUID;
import javax.imageio.ImageIO;

import database.DatabaseConnection;

public class EditProfileFrame extends JFrame {

    private JTextField nameField, ageField;
    private JTextField minAgeField, maxAgeField;

    private JComboBox<String> genderBox;
    private JComboBox<String> preferredGenderBox;

    private JCheckBox[] interestBoxes;
    private JLabel photoLabel;

    private String email;
    private String imagePath = "";
    private String selectedImagePath = null;

    private final Color DARK = new Color(91, 57, 72);
    private final Color ROSE = new Color(190, 105, 130);
    private final Color BACKGROUND = new Color(255, 244, 247);
    private final Color CARD = Color.WHITE;

    private final String[] interests = {
        "Music", "Movies", "Photography",
        "Travelling", "Reading", "Gaming",
        "Cooking", "Sports", "Fitness",
        "Technology", "Art", "Dancing",
        "Fashion", "Animals", "Food"
    };

    public EditProfileFrame(String name, String email,
                            int age, String gender) {

        this.email = email;

        setTitle("PairUp - Edit Profile");
        setSize(850, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);

        // HEADER
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(DARK);
        header.setPreferredSize(new Dimension(850, 75));

        JLabel title = new JLabel(
                "EDIT YOUR PROFILE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Make your PairUp profile more personal",
                SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(255, 220, 230));

        header.add(title);
        header.add(subtitle);

        mainPanel.add(header, BorderLayout.NORTH);

        // SCROLLABLE CONTENT
        JPanel content = new JPanel();
        content.setBackground(BACKGROUND);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 35, 20, 35));

        // PROFILE PHOTO CARD
        JPanel photoCard = createCard();
        photoCard.setLayout(new BorderLayout(20, 10));
        photoCard.setMaximumSize(new Dimension(750, 165));

        JPanel photoInfo = new JPanel();
        photoInfo.setBackground(CARD);
        photoInfo.setLayout(
                new BoxLayout(photoInfo, BoxLayout.Y_AXIS));

        JLabel photoTitle = createHeading("PROFILE PICTURE");

        JLabel photoDescription = new JLabel(
                "Choose a picture that represents you.");
        photoDescription.setFont(
                new Font("Arial", Font.PLAIN, 13));
        photoDescription.setForeground(Color.GRAY);

        JButton uploadButton = createButton("UPLOAD PHOTO");
        uploadButton.addActionListener(e -> choosePhoto());

        photoInfo.add(photoTitle);
        photoInfo.add(Box.createVerticalStrut(10));
        photoInfo.add(photoDescription);
        photoInfo.add(Box.createVerticalStrut(15));
        photoInfo.add(uploadButton);

        photoLabel = new JLabel("♡", SwingConstants.CENTER);
        photoLabel.setFont(new Font("Arial", Font.BOLD, 48));
        photoLabel.setForeground(ROSE);
        photoLabel.setOpaque(true);
        photoLabel.setBackground(new Color(255, 231, 238));
        photoLabel.setPreferredSize(new Dimension(120, 120));
        photoLabel.setBorder(
                new LineBorder(new Color(235, 190, 205), 2, true));

        photoCard.add(photoInfo, BorderLayout.CENTER);
        photoCard.add(photoLabel, BorderLayout.EAST);

        content.add(photoCard);
        content.add(Box.createVerticalStrut(18));

        // PERSONAL DETAILS CARD
        JPanel personalCard = createCard();
        personalCard.setLayout(new GridBagLayout());
        personalCard.setMaximumSize(new Dimension(750, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 12, 9, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addSectionHeading(
                personalCard, gbc, "PERSONAL DETAILS", 0);

        nameField = createField(name);
        ageField = createField(String.valueOf(age));

        genderBox = new JComboBox<>(
                new String[]{"Male", "Female", "Other"});
        genderBox.setSelectedItem(gender);
        styleCombo(genderBox);

        addFormRow(personalCard, gbc, "Name", nameField, 1);
        addFormRow(personalCard, gbc, "Age", ageField, 2);
        addFormRow(personalCard, gbc, "Gender", genderBox, 3);

        content.add(personalCard);
        content.add(Box.createVerticalStrut(18));

        // MATCHING PREFERENCES CARD
        JPanel preferenceCard = createCard();
        preferenceCard.setLayout(new GridBagLayout());
        preferenceCard.setMaximumSize(new Dimension(750, 250));

        addSectionHeading(
                preferenceCard, gbc, "MATCHING PREFERENCES", 0);

        preferredGenderBox = new JComboBox<>(
                new String[]{"Any", "Male", "Female", "Other"});
        styleCombo(preferredGenderBox);

        minAgeField = createField("18");
        maxAgeField = createField("100");

        addFormRow(preferenceCard, gbc,
                "Interested in", preferredGenderBox, 1);
        addFormRow(preferenceCard, gbc,
                "Minimum preferred age", minAgeField, 2);
        addFormRow(preferenceCard, gbc,
                "Maximum preferred age", maxAgeField, 3);

        content.add(preferenceCard);
        content.add(Box.createVerticalStrut(18));

        // INTERESTS CARD
        JPanel interestsCard = createCard();
        interestsCard.setLayout(new BorderLayout(10, 12));
        interestsCard.setMaximumSize(new Dimension(750, 280));

        JLabel interestsTitle = createHeading("YOUR INTERESTS");

        JLabel interestsDescription = new JLabel(
                "Select as many interests as you like!");
        interestsDescription.setFont(
                new Font("Arial", Font.PLAIN, 13));
        interestsDescription.setForeground(Color.GRAY);

        JPanel interestHeader = new JPanel();
        interestHeader.setBackground(CARD);
        interestHeader.setLayout(
                new BoxLayout(interestHeader, BoxLayout.Y_AXIS));

        interestHeader.add(interestsTitle);
        interestHeader.add(Box.createVerticalStrut(7));
        interestHeader.add(interestsDescription);

        JPanel checkboxPanel = new JPanel(
                new GridLayout(0, 3, 12, 10));
        checkboxPanel.setBackground(CARD);

        interestBoxes = new JCheckBox[interests.length];

        for (int i = 0; i < interests.length; i++) {

            interestBoxes[i] = new JCheckBox(interests[i]);

            interestBoxes[i].setFont(
                    new Font("Arial", Font.PLAIN, 14));

            interestBoxes[i].setForeground(DARK);
            interestBoxes[i].setBackground(CARD);
            interestBoxes[i].setFocusPainted(false);
            interestBoxes[i].setCursor(
                    new Cursor(Cursor.HAND_CURSOR));

            checkboxPanel.add(interestBoxes[i]);
        }

        interestsCard.add(interestHeader, BorderLayout.NORTH);
        interestsCard.add(checkboxPanel, BorderLayout.CENTER);

        content.add(interestsCard);
        content.add(Box.createVerticalStrut(20));

        // BUTTONS
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND);

        JButton saveButton = createButton("SAVE CHANGES");
        JButton cancelButton = createSecondaryButton("CANCEL");

        saveButton.addActionListener(e -> updateProfile());

        cancelButton.addActionListener(e -> {
            dispose();
            new ProfileFrame(name, email);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        content.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        loadPreferences();

        setVisible(true);
    }

    // CREATE CARD
    private JPanel createCard() {

        JPanel panel = new JPanel();

        panel.setBackground(CARD);

        panel.setBorder(new CompoundBorder(
                new LineBorder(
                        new Color(245, 215, 225), 1, true),
                new EmptyBorder(18, 22, 18, 22)));

        return panel;
    }

    // CURSIVE SECTION HEADINGS
    private JLabel createHeading(String text) {

    JLabel label = new JLabel(text);

    label.setFont(
            new Font("Monotype Corsiva", Font.BOLD, 25));

    label.setForeground(new Color(160, 91, 119));

    return label;
}

    // TEXT FIELD
    private JTextField createField(String value) {

        JTextField field = new JTextField(value);

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 34));

        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 195, 207)),
                new EmptyBorder(5, 10, 5, 10)));

        return field;
    }

    // COMBOBOX STYLE
    private void styleCombo(JComboBox<String> combo) {

        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(300, 34));
    }

    // MAIN BUTTON
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(ROSE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                button.setBackground(DARK);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(ROSE);
            }
        });

        return button;
    }

    // SECONDARY BUTTON
    private JButton createSecondaryButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(DARK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);

        button.setBorder(new CompoundBorder(
                new LineBorder(ROSE, 1),
                new EmptyBorder(9, 20, 9, 20)));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // SECTION HEADING LAYOUT
    private void addSectionHeading(JPanel panel,
            GridBagConstraints gbc, String text, int row) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        panel.add(createHeading(text), gbc);

        gbc.gridwidth = 1;
    }

    // FORM ROW LAYOUT
    private void addFormRow(JPanel panel,
            GridBagConstraints gbc, String label,
            JComponent field, int row) {

        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.4;

        JLabel text = new JLabel(label);
        text.setFont(new Font("Arial", Font.PLAIN, 14));
        text.setForeground(DARK);

        panel.add(text, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;

        panel.add(field, gbc);
    }

    // UPLOAD PROFILE PICTURE
    private void choosePhoto() {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Choose Profile Picture");

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            try {

                BufferedImage image = ImageIO.read(file);

                if (image == null) {

                    JOptionPane.showMessageDialog(
                            this, "Please select a valid image.");

                    return;
                }

                selectedImagePath = file.getAbsolutePath();

                displayPhoto(selectedImagePath);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this, "Unable to load selected image.");
            }
        }
    }

    // DISPLAY PROFILE PICTURE
    private void displayPhoto(String path) {

        try {

            BufferedImage image = ImageIO.read(new File(path));

            if (image == null) {
                return;
            }

            Image scaled = image.getScaledInstance(
                    115, 115, Image.SCALE_SMOOTH);

            photoLabel.setText("");
            photoLabel.setIcon(new ImageIcon(scaled));

        } catch (Exception ex) {

            photoLabel.setText("♡");
            photoLabel.setIcon(null);
        }
    }

    // LOAD EXISTING PREFERENCES, PHOTO AND INTERESTS
    private void loadPreferences() {

        String sql = "SELECT user_id, preferred_gender, "
                + "min_preferred_age, max_preferred_age, "
                + "profile_picture FROM users WHERE email = ?";

        Connection connection =
                DatabaseConnection.getConnection();

        if (connection == null) {

            JOptionPane.showMessageDialog(
                    this, "Database connection failed.");

            return;
        }

        int userId = -1;

        try (connection;
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    userId = result.getInt("user_id");

                    preferredGenderBox.setSelectedItem(
                            result.getString("preferred_gender"));

                    minAgeField.setText(String.valueOf(
                            result.getInt("min_preferred_age")));

                    maxAgeField.setText(String.valueOf(
                            result.getInt("max_preferred_age")));

                    imagePath = result.getString("profile_picture");

                    if (imagePath != null && !imagePath.isEmpty()) {

                        File photo = new File(imagePath);

                        if (photo.exists()) {
                            displayPhoto(imagePath);
                        }
                    }
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this, "Error loading profile: "
                    + ex.getMessage());

            return;
        }

        if (userId != -1) {
            loadInterests(userId);
        }
    }

    // LOAD PREVIOUSLY SELECTED CHECKBOXES
    private void loadInterests(int userId) {

        String sql = "SELECT interest_name FROM user_interests "
                + "WHERE user_id = ?";

        Connection connection =
                DatabaseConnection.getConnection();

        if (connection == null) {
            return;
        }

        try (connection;
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    String savedInterest =
                            result.getString("interest_name");

                    for (int i = 0; i < interests.length; i++) {

                        if (interests[i].equals(savedInterest)) {
                            interestBoxes[i].setSelected(true);
                        }
                    }
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this, "Error loading interests: "
                    + ex.getMessage());
        }
    }

    // SAVE PROFILE, PICTURE AND INTERESTS
    private void updateProfile() {

        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String minText = minAgeField.getText().trim();
        String maxText = maxAgeField.getText().trim();

        String gender = (String) genderBox.getSelectedItem();

        String preferredGender =
                (String) preferredGenderBox.getSelectedItem();

        if (name.isEmpty() || ageText.isEmpty()
                || minText.isEmpty() || maxText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill all required fields.");

            return;
        }

        try {

            int age = Integer.parseInt(ageText);
            int minAge = Integer.parseInt(minText);
            int maxAge = Integer.parseInt(maxText);

            if (age < 18 || age > 100) {

                JOptionPane.showMessageDialog(
                        this, "Your age must be between 18 and 100.");

                return;
            }

            if (minAge < 18 || maxAge > 100
                    || minAge > maxAge) {

                JOptionPane.showMessageDialog(
                        this, "Enter a valid preferred age range.");

                return;
            }

            // COPY SELECTED PICTURE INTO PROJECT FOLDER
            if (selectedImagePath != null) {

                Path folder = Paths.get("profile_pictures");

                Files.createDirectories(folder);

                String extension = getExtension(
                        selectedImagePath);

                String fileName = UUID.randomUUID()
                        + extension;

                Path destination = folder.resolve(fileName);

                Files.copy(
                        Paths.get(selectedImagePath),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING);

                imagePath = destination.toString();
            }

            Connection connection =
                    DatabaseConnection.getConnection();

            if (connection == null) {

                JOptionPane.showMessageDialog(
                        this, "Database connection failed.");

                return;
            }

            try (connection) {

                connection.setAutoCommit(false);

                try {

                    // UPDATE USER DETAILS
                    String updateSQL =
                            "UPDATE users SET name=?, age=?, "
                            + "gender=?, preferred_gender=?, "
                            + "min_preferred_age=?, "
                            + "max_preferred_age=?, "
                            + "profile_picture=? WHERE email=?";

                    try (PreparedStatement statement =
                            connection.prepareStatement(updateSQL)) {

                        statement.setString(1, name);
                        statement.setInt(2, age);
                        statement.setString(3, gender);
                        statement.setString(4, preferredGender);
                        statement.setInt(5, minAge);
                        statement.setInt(6, maxAge);

                        if (imagePath == null
                                || imagePath.isEmpty()) {

                            statement.setNull(7, Types.VARCHAR);

                        } else {

                            statement.setString(7, imagePath);
                        }

                        statement.setString(8, email);

                        int rows = statement.executeUpdate();

                        if (rows == 0) {

                            throw new SQLException(
                                    "User profile was not found.");
                        }
                    }

                    // GET USER ID
                    int userId;

                    String userSQL =
                            "SELECT user_id FROM users WHERE email=?";

                    try (PreparedStatement statement =
                            connection.prepareStatement(userSQL)) {

                        statement.setString(1, email);

                        try (ResultSet result =
                                statement.executeQuery()) {

                            if (!result.next()) {

                                throw new SQLException(
                                        "Unable to find user.");
                            }

                            userId = result.getInt("user_id");
                        }
                    }

                    // DELETE OLD INTERESTS
                    String deleteSQL =
                            "DELETE FROM user_interests "
                            + "WHERE user_id=?";

                    try (PreparedStatement statement =
                            connection.prepareStatement(deleteSQL)) {

                        statement.setInt(1, userId);
                        statement.executeUpdate();
                    }

                    // INSERT SELECTED INTERESTS
                    String insertSQL =
                            "INSERT INTO user_interests "
                            + "(user_id, interest_name) "
                            + "VALUES (?, ?)";

                    try (PreparedStatement statement =
                            connection.prepareStatement(insertSQL)) {

                        for (JCheckBox box : interestBoxes) {

                            if (box.isSelected()) {

                                statement.setInt(1, userId);
                                statement.setString(2, box.getText());

                                statement.addBatch();
                            }
                        }

                        statement.executeBatch();
                    }

                    connection.commit();

                    JOptionPane.showMessageDialog(
                            this, "Profile updated successfully!");

                    dispose();

                    new ProfileFrame(name, email);

                } catch (SQLException ex) {

                    connection.rollback();

                    JOptionPane.showMessageDialog(
                            this, "Error updating profile: "
                            + ex.getMessage());
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Age fields must contain numbers.");

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this, "Error saving profile: "
                    + ex.getMessage());
        }
    }

    // GET IMAGE FILE EXTENSION
    private String getExtension(String path) {

        int dot = path.lastIndexOf('.');

        if (dot == -1) {
            return ".jpg";
        }

        return path.substring(dot).toLowerCase();
    }
}
