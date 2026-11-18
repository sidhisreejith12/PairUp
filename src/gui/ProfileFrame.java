
package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;

public class ProfileFrame extends JFrame {

    private String name;
    private String email;

    private JLabel nameValue;
    private JLabel emailValue;
    private JLabel ageValue;
    private JLabel genderValue;

    private AvatarPanel avatarPanel;

    private int userAge;
    private String userGender;

    private final Color DARK_ROSE = new Color(155, 57, 91);
    private final Color ROSE = new Color(204, 100, 137);
    private final Color BLUSH = new Color(255, 232, 239);
    private final Color CREAM = new Color(255, 249, 247);
    private final Color TEXT = new Color(91, 55, 69);

    public ProfileFrame(String name, String email) {

        this.name = name;
        this.email = email;

        setTitle("PairUp | My Profile");
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CREAM);

        // HEADER

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(
            new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );

        headerPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 18, 20)
        );

        JLabel titleLabel = new JLabel("MY PROFILE");
        titleLabel.setFont(
            new Font("Serif", Font.BOLD, 31)
        );
        titleLabel.setForeground(DARK_ROSE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
            "A little introduction about you ♡"
        );

        subtitleLabel.setFont(
            new Font("SansSerif", Font.PLAIN, 13)
        );
        subtitleLabel.setForeground(new Color(145, 117, 128));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(subtitleLabel);

        // PROFILE CONTENT

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(CREAM);

        JPanel profileCard = new JPanel();
        profileCard.setBackground(Color.WHITE);
        profileCard.setLayout(
            new BoxLayout(profileCard, BoxLayout.Y_AXIS)
        );

        profileCard.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(
                    new Color(241, 210, 221),
                    1,
                    true
                ),
                BorderFactory.createEmptyBorder(
                    25, 45, 25, 45
                )
            )
        );

        profileCard.setPreferredSize(
            new Dimension(480, 390)
        );

        // PROFILE PHOTO

        avatarPanel = new AvatarPanel();
        avatarPanel.setPreferredSize(
            new Dimension(125, 125)
        );
        avatarPanel.setMaximumSize(
            new Dimension(125, 125)
        );
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileCard.add(avatarPanel);
        profileCard.add(Box.createVerticalStrut(12));

        JLabel nameHeading = new JLabel("YOUR PROFILE");
        nameHeading.setFont(
            new Font("SansSerif", Font.BOLD, 11)
        );
        nameHeading.setForeground(ROSE);
        nameHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileCard.add(nameHeading);
        profileCard.add(Box.createVerticalStrut(20));

        // PROFILE DETAILS

        JPanel detailsPanel = new JPanel(
            new GridLayout(4, 2, 15, 18)
        );

        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailsPanel.setMaximumSize(
            new Dimension(390, 180)
        );

        nameValue = createValueLabel();
        emailValue = createValueLabel();
        ageValue = createValueLabel();
        genderValue = createValueLabel();

        detailsPanel.add(createDetailLabel("NAME"));
        detailsPanel.add(nameValue);

        detailsPanel.add(createDetailLabel("EMAIL"));
        detailsPanel.add(emailValue);

        detailsPanel.add(createDetailLabel("AGE"));
        detailsPanel.add(ageValue);

        detailsPanel.add(createDetailLabel("GENDER"));
        detailsPanel.add(genderValue);

        profileCard.add(detailsPanel);

        contentPanel.add(profileCard);

        // BOTTOM BUTTONS

        JPanel bottomPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 20, 15)
        );

        bottomPanel.setBackground(CREAM);

        JButton editButton = new JButton("EDIT PROFILE");
        JButton backButton = new JButton("BACK TO HOME");

        stylePrimaryButton(editButton);
        styleSecondaryButton(backButton);

        bottomPanel.add(editButton);
        bottomPanel.add(backButton);

        // ADD COMPONENTS

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // EDIT PROFILE NAVIGATION

        editButton.addActionListener(e -> {

            dispose();

            new EditProfileFrame(
                nameValue.getText(),
                email,
                userAge,
                userGender
            );
        });

        // HOME NAVIGATION

        backButton.addActionListener(e -> {
            dispose();
            new HomeFrame(name, email);
        });

        loadProfile();

        setVisible(true);
    }

    // DETAIL LABEL

    private JLabel createDetailLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("SansSerif", Font.BOLD, 11)
        );

        label.setForeground(ROSE);

        return label;
    }

    // VALUE LABEL

    private JLabel createValueLabel() {

        JLabel label = new JLabel();

        label.setFont(
            new Font("SansSerif", Font.PLAIN, 13)
        );

        label.setForeground(TEXT);

        return label;
    }

    // PRIMARY BUTTON

    private void stylePrimaryButton(JButton button) {

        button.setFont(
            new Font("SansSerif", Font.BOLD, 12)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(ROSE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        button.setPreferredSize(
            new Dimension(160, 43)
        );

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
            new Font("SansSerif", Font.BOLD, 12)
        );

        button.setForeground(DARK_ROSE);
        button.setBackground(BLUSH);
        button.setFocusPainted(false);
        button.setBorder(
            new LineBorder(
                new Color(231, 177, 196),
                1,
                true
            )
        );

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        button.setPreferredSize(
            new Dimension(160, 43)
        );
    }

    // LOAD PROFILE FROM DATABASE

    private void loadProfile() {

        String sql =
            "SELECT name, email, age, gender, profile_picture " +
            "FROM users WHERE email = ?";

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
                PreparedStatement statement =
                    connection.prepareStatement(sql)
            ) {

                statement.setString(1, email);

                try (
                    ResultSet result =
                        statement.executeQuery()
                ) {

                    if (result.next()) {

                        name = result.getString("name");

                        nameValue.setText(name);
                        emailValue.setText(
                            result.getString("email")
                        );

                        userAge = result.getInt("age");
                        userGender = result.getString("gender");

                        ageValue.setText(
                            String.valueOf(userAge)
                        );

                        genderValue.setText(userGender);

                        // LOAD PROFILE PICTURE

                        String imagePath =
                            result.getString("profile_picture");

                        avatarPanel.loadImage(imagePath);

                    } else {

                        JOptionPane.showMessageDialog(
                            this,
                            "Profile not found."
                        );
                    }
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Unable to load profile!\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // CIRCULAR PROFILE PHOTO PANEL

    class AvatarPanel extends JPanel {

        private Image profileImage;

        AvatarPanel() {

            setOpaque(false);
        }

        void loadImage(String imagePath) {

            profileImage = null;

            if (imagePath != null &&
                !imagePath.trim().isEmpty()) {

                File imageFile = new File(imagePath);

                if (imageFile.exists()) {

                    ImageIcon icon = new ImageIcon(imagePath);

                    profileImage = icon.getImage();
                }
            }

            repaint();
        }

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size = Math.min(
                getWidth(), getHeight()
            );

            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            // Pink circular background

            g2.setColor(BLUSH);

            g2.fillOval(x, y, size, size);

            if (profileImage != null) {

                // Clip image into a circle

                Shape circle = new Ellipse2D.Float(
                    x, y, size, size
                );

                g2.setClip(circle);

                int imageWidth = profileImage.getWidth(this);
                int imageHeight = profileImage.getHeight(this);

                if (imageWidth > 0 && imageHeight > 0) {

                    double scale = Math.max(
                        (double) size / imageWidth,
                        (double) size / imageHeight
                    );

                    int scaledWidth =
                        (int) (imageWidth * scale);

                    int scaledHeight =
                        (int) (imageHeight * scale);

                    int imageX =
                        x + (size - scaledWidth) / 2;

                    int imageY =
                        y + (size - scaledHeight) / 2;

                    g2.drawImage(
                        profileImage,
                        imageX,
                        imageY,
                        scaledWidth,
                        scaledHeight,
                        this
                    );
                }

                g2.setClip(null);

            } else {

                // Default avatar when no photo is uploaded

                g2.setColor(ROSE);

                g2.setFont(
                    new Font("Serif", Font.BOLD, 55)
                );

                FontMetrics fm = g2.getFontMetrics();

                String symbol = "♥";

                int textX =
                    x + (size - fm.stringWidth(symbol)) / 2;

                int textY =
                    y + ((size - fm.getHeight()) / 2)
                    + fm.getAscent();

                g2.drawString(symbol, textX, textY);
            }

            // Circular outline

            g2.setColor(new Color(225, 155, 180));
            g2.setStroke(new BasicStroke(3));

            g2.drawOval(
                x + 1, y + 1,
                size - 2, size - 2
            );

            g2.dispose();
        }
    }
}
