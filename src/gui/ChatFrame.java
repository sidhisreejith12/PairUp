
package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import database.DatabaseConnection;

public class ChatFrame extends JFrame {

    private String email;
    private int matchId;
    private String matchName;

    private int currentUserId;
    private boolean canChat = false;

    private JPanel messagesPanel;
    private JScrollPane scrollPane;

    private JTextField messageField;
    private JButton sendButton;

    private Timer refreshTimer;

    private final Color darkRose = new Color(160, 91, 119);
    private final Color pink = new Color(221, 139, 166);
    private final Color backgroundPink = new Color(255, 241, 245);
    private final Color lightPink = new Color(255, 226, 235);
    private final Color textColor = new Color(85, 58, 68);

    public ChatFrame(String email, int matchId, String matchName) {

        this.email = email;
        this.matchId = matchId;
        this.matchName = matchName;

        setTitle("PairUp - Chat with " + matchName);
        setSize(650, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(backgroundPink);

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 18, 15, 18));

        JButton backButton = createButton("← BACK", new Color(190, 130, 150));
        backButton.setPreferredSize(new Dimension(110, 38));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(matchName);
        title.setFont(new Font("Serif", Font.BOLD, 25));
        title.setForeground(darkRose);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Your PairUp connection ♥");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitle);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // CHAT MESSAGE AREA
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(backgroundPink);
        messagesPanel.setBorder(new EmptyBorder(15, 18, 15, 18));

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(backgroundPink);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // MESSAGE INPUT AREA
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(15, 18, 15, 18));

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 15));
        messageField.setForeground(textColor);
        messageField.setBackground(backgroundPink);
        messageField.setBorder(new CompoundBorder(
            new LineBorder(new Color(240, 205, 216), 1, true),
            new EmptyBorder(12, 12, 12, 12)
        ));

        sendButton = createButton("SEND ♥", darkRose);
        sendButton.setPreferredSize(new Dimension(110, 43));

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // BUTTON ACTIONS
        backButton.addActionListener(e -> goBack());

        sendButton.addActionListener(e -> sendMessage());

        messageField.addActionListener(e -> sendMessage());

        add(mainPanel);

        loadCurrentUser();

        if (currentUserId != 0 && isMutualMatch()) {

            canChat = true;

            loadMessages(true);

            refreshTimer = new Timer(3000, e -> loadMessages(false));
            refreshTimer.start();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "You can only chat with mutual matches!"
            );

            messageField.setEnabled(false);
            sendButton.setEnabled(false);

            addSystemMessage("Chat is unavailable because this is not a mutual match.");
        }

        // STOP AUTOMATIC REFRESH WHEN WINDOW CLOSES
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                stopRefreshTimer();
            }

            public void windowClosed(WindowEvent e) {
                stopRefreshTimer();
            }
        });

        setVisible(true);
    }

    // BUTTON DESIGN
    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        return button;
    }

    // LOAD LOGGED-IN USER ID
    private void loadCurrentUser() {

        String sql = "SELECT user_id FROM users WHERE email = ?";

        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {
            JOptionPane.showMessageDialog(
                this, "Database connection failed!"
            );
            return;
        }

        try (connection;
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    currentUserId = result.getInt("user_id");
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this, "Error loading user: " + ex.getMessage()
            );
        }
    }

    // VERIFY MUTUAL MATCH
    private boolean isMutualMatch() {

        String sql =
            "SELECT COUNT(*) FROM user_actions a " +
            "JOIN user_actions b " +
            "ON a.from_user_id = b.to_user_id " +
            "AND a.to_user_id = b.from_user_id " +
            "WHERE a.from_user_id = ? " +
            "AND a.to_user_id = ? " +
            "AND a.action = 'LIKE' " +
            "AND b.action = 'LIKE'";

        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {
            return false;
        }

        try (connection;
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currentUserId);
            statement.setInt(2, matchId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt(1) > 0;
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this, "Match verification failed: " + ex.getMessage()
            );
        }

        return false;
    }

    // LOAD MESSAGES FROM DATABASE
    private void loadMessages(boolean showError) {

        if (!canChat) {
            return;
        }

        String sql =
            "SELECT sender_id, message_text " +
            "FROM messages " +
            "WHERE (sender_id = ? AND receiver_id = ?) " +
            "OR (sender_id = ? AND receiver_id = ?) " +
            "ORDER BY sent_at, message_id";

        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {

            if (showError) {
                JOptionPane.showMessageDialog(
                    this, "Database connection failed!"
                );
            }

            return;
        }

        try (connection;
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currentUserId);
            statement.setInt(2, matchId);
            statement.setInt(3, matchId);
            statement.setInt(4, currentUserId);

            try (ResultSet result = statement.executeQuery()) {

                messagesPanel.removeAll();

                boolean hasMessages = false;

                while (result.next()) {

                    hasMessages = true;

                    int sender = result.getInt("sender_id");
                    String message = result.getString("message_text");

                    if (sender == currentUserId) {
                        addMessageBubble(message, true);
                    } else {
                        addMessageBubble(message, false);
                    }
                }

                if (!hasMessages) {
                    addSystemMessage(
                        "Say hello! Start your conversation here. ♥"
                    );
                }

                messagesPanel.revalidate();
                messagesPanel.repaint();

                scrollToBottom();
            }

        } catch (SQLException ex) {

            if (showError) {
                JOptionPane.showMessageDialog(
                    this, "Error loading messages: " + ex.getMessage()
                );
            }
        }
    }

    // CREATE MESSAGE BUBBLES
    private void addMessageBubble(String message, boolean sentByMe) {

        JPanel row = new JPanel(new FlowLayout(
            sentByMe ? FlowLayout.RIGHT : FlowLayout.LEFT,
            5, 5
        ));

        row.setBackground(backgroundPink);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel bubble = new JPanel(new BorderLayout(0, 5));

        bubble.setBackground(sentByMe ? lightPink : Color.WHITE);
        bubble.setBorder(new CompoundBorder(
            new LineBorder(
                sentByMe ? new Color(240, 190, 207)
                         : new Color(235, 215, 222),
                1, true
            ),
            new EmptyBorder(9, 13, 9, 13)
        ));

        JLabel senderLabel = new JLabel(
            sentByMe ? "YOU" : matchName.toUpperCase()
        );

        senderLabel.setFont(new Font("Arial", Font.BOLD, 10));
        senderLabel.setForeground(darkRose);

        JTextArea messageText = new JTextArea(message);
        messageText.setFont(new Font("Arial", Font.PLAIN, 14));
        messageText.setForeground(textColor);
        messageText.setBackground(sentByMe ? lightPink : Color.WHITE);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setEditable(false);
        messageText.setFocusable(false);
        messageText.setBorder(null);

        int width = Math.min(300, Math.max(100, message.length() * 8));
        messageText.setSize(new Dimension(width, Integer.MAX_VALUE));

        Dimension textSize = messageText.getPreferredSize();
        messageText.setPreferredSize(new Dimension(
            Math.min(300, Math.max(100, textSize.width)),
            textSize.height
        ));

        bubble.add(senderLabel, BorderLayout.NORTH);
        bubble.add(messageText, BorderLayout.CENTER);

        row.add(bubble);

        messagesPanel.add(row);
        messagesPanel.add(Box.createVerticalStrut(3));
    }

    // SYSTEM MESSAGE
    private void addSystemMessage(String message) {

        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.ITALIC, 13));
        label.setForeground(darkRose);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        messagesPanel.add(Box.createVerticalStrut(20));
        messagesPanel.add(label);
    }

    // SCROLL TO THE LATEST MESSAGE
    private void scrollToBottom() {

        SwingUtilities.invokeLater(() -> {

            JScrollBar verticalBar =
                scrollPane.getVerticalScrollBar();

            verticalBar.setValue(verticalBar.getMaximum());
        });
    }

    // SEND MESSAGE
    private void sendMessage() {

        if (!canChat) {
            return;
        }

        String message = messageField.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        String sql =
            "INSERT INTO messages (sender_id, receiver_id, message_text) " +
            "VALUES (?, ?, ?)";

        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {

            JOptionPane.showMessageDialog(
                this, "Database connection failed!"
            );

            return;
        }

        try (connection;
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currentUserId);
            statement.setInt(2, matchId);
            statement.setString(3, message);

            statement.executeUpdate();

            messageField.setText("");

            loadMessages(false);

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this, "Error sending message: " + ex.getMessage()
            );
        }
    }

    // RETURN TO MY MATCHES
    private void goBack() {

        stopRefreshTimer();

        dispose();

        new MyMatchesFrame(email);
    }

    // STOP AUTOMATIC MESSAGE REFRESH
    private void stopRefreshTimer() {

        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}
