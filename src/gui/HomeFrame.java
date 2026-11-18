
package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class HomeFrame extends JFrame {

    private String name;
    private String email;

    private final Color DARK_ROSE = new Color(155, 57, 91);
    private final Color ROSE = new Color(204, 100, 137);
    private final Color BLUSH = new Color(255, 232, 239);
    private final Color CREAM = new Color(255, 249, 247);
    private final Color TEXT = new Color(91, 55, 69);

    public HomeFrame(String name, String email) {

        this.name = name;
        this.email = email;

        setTitle("PairUp | Home");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CREAM);

        // TOP HEADER

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(
            BorderFactory.createEmptyBorder(18, 35, 18, 35)
        );

        JLabel logoLabel = new JLabel("♥ PAIR UP");
        logoLabel.setFont(new Font("Serif", Font.BOLD, 25));
        logoLabel.setForeground(DARK_ROSE);

        JLabel taglineLabel = new JLabel(
            "Discover meaningful connections"
        );

        taglineLabel.setFont(
            new Font("SansSerif", Font.ITALIC, 13)
        );

        taglineLabel.setForeground(new Color(145, 117, 128));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));

        logoPanel.add(logoLabel);
        logoPanel.add(Box.createVerticalStrut(3));
        logoPanel.add(taglineLabel);

        JButton logoutButton = new JButton("LOG OUT");

        logoutButton.setFont(
            new Font("SansSerif", Font.BOLD, 11)
        );

        logoutButton.setForeground(DARK_ROSE);
        logoutButton.setBackground(BLUSH);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(
            new LineBorder(new Color(231, 177, 196), 1, true)
        );
        logoutButton.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // MAIN CONTENT

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(CREAM);
        contentPanel.setLayout(
            new BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        );

        contentPanel.setBorder(
            BorderFactory.createEmptyBorder(35, 55, 25, 55)
        );

        JLabel welcomeLabel = new JLabel(
            "Welcome back, " + name + " ♡"
        );

        welcomeLabel.setFont(
            new Font("Serif", Font.BOLD, 34)
        );

        welcomeLabel.setForeground(DARK_ROSE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(
            "Your next meaningful connection could be just a click away."
        );

        descriptionLabel.setFont(
            new Font("SansSerif", Font.PLAIN, 14)
        );

        descriptionLabel.setForeground(TEXT);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(descriptionLabel);
        contentPanel.add(Box.createVerticalStrut(35));

        // FEATURE CARDS

        JPanel cardPanel = new JPanel(new GridLayout(2, 2, 22, 22));
        cardPanel.setBackground(CREAM);

        JButton profileButton = createFeatureButton(
            "♡",
            "MY PROFILE",
            "View and update your profile"
        );

        JButton matchButton = createFeatureButton(
            "♥",
            "FIND MATCHES",
            "Discover people who match your preferences"
        );

        JButton likesButton = createFeatureButton(
            "✦",
            "MY LIKES",
            "View your mutual connections"
        );

        JButton logoutCardButton = createFeatureButton(
            "↗",
            "LOG OUT",
            "Sign out of your PairUp account"
        );

        cardPanel.add(profileButton);
        cardPanel.add(matchButton);
        cardPanel.add(likesButton);
        cardPanel.add(logoutCardButton);

        contentPanel.add(cardPanel);

        // BOTTOM MESSAGE

        JLabel footerLabel = new JLabel(
            "Made with love, just for meaningful connections. ♥"
        );

        footerLabel.setFont(
            new Font("SansSerif", Font.ITALIC, 12)
        );

        footerLabel.setForeground(new Color(173, 125, 144));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(footerLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        // NAVIGATION

        profileButton.addActionListener(e -> {
            dispose();
            new ProfileFrame(name, email);
        });

        matchButton.addActionListener(e -> {
            dispose();
            new FindMatchesFrame(email);
        });

        likesButton.addActionListener(e -> {
            dispose();
            new MyMatchesFrame(email);
        });

        logoutButton.addActionListener(e -> logout());

        logoutCardButton.addActionListener(e -> logout());

        setVisible(true);
    }

    // FEATURE BUTTON DESIGN

    private JButton createFeatureButton(
        String symbol,
        String title,
        String description
    ) {

        JButton button = new JButton();

        button.setLayout(
            new BoxLayout(button, BoxLayout.Y_AXIS)
        );

        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(
                    new Color(241, 210, 221),
                    1,
                    true
                ),
                BorderFactory.createEmptyBorder(
                    20, 18, 20, 18
                )
            )
        );

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        JLabel symbolLabel = new JLabel(symbol);
        symbolLabel.setFont(
            new Font("Serif", Font.PLAIN, 35)
        );
        symbolLabel.setForeground(ROSE);
        symbolLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
            new Font("SansSerif", Font.BOLD, 15)
        );
        titleLabel.setForeground(DARK_ROSE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(
            "<html><div style='text-align:center;'>"
            + description
            + "</div></html>"
        );

        descriptionLabel.setFont(
            new Font("SansSerif", Font.PLAIN, 11)
        );

        descriptionLabel.setForeground(TEXT);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.add(symbolLabel);
        button.add(Box.createVerticalStrut(12));
        button.add(titleLabel);
        button.add(Box.createVerticalStrut(8));
        button.add(descriptionLabel);

        // Hover effect

        button.addMouseListener(
            new java.awt.event.MouseAdapter() {

                public void mouseEntered(
                    java.awt.event.MouseEvent e
                ) {
                    button.setBackground(BLUSH);
                    button.setBorder(
                        new LineBorder(ROSE, 2, true)
                    );
                }

                public void mouseExited(
                    java.awt.event.MouseEvent e
                ) {
                    button.setBackground(Color.WHITE);
                    button.setBorder(
                        BorderFactory.createCompoundBorder(
                            new LineBorder(
                                new Color(241, 210, 221),
                                1,
                                true
                            ),
                            BorderFactory.createEmptyBorder(
                                20, 18, 20, 18
                            )
                        )
                    );
                }
            }
        );

        return button;
    }

    // LOGOUT FUNCTION

    private void logout() {

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to log out?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}
