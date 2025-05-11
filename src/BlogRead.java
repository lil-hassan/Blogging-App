import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.*;
import javax.imageio.ImageIO;
import com.formdev.flatlaf.FlatLightLaf;

public class BlogRead extends JFrame {

    private JPanel commentDisplayPanel;
    private JScrollPane commentDisplayScroll;
    private int blogId;
    private int userId;

    public BlogRead(int blogId, int userId, String titleText, String contentText, String author, Blob imageBlob) {
        this.blogId = blogId;
        this.userId = userId;

        try
        {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }

        setTitle("Read Blog - " + titleText);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("blogg.png")));
        setSize(700, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents(titleText, contentText, imageBlob);
    }

    private void initComponents(String titleText, String contentText, Blob imageBlob)
    {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" + titleText + "</div></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        container.add(titleLabel);

        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (imageBlob != null) {
            ImageIcon imageIcon = convertBlobToImageIcon(imageBlob, 600, 300);
            if (imageIcon != null) {
                imageLabel.setIcon(imageIcon);
                imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            }
        }
        container.add(imageLabel);

        JTextArea contentArea = new JTextArea(contentText);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Serif", Font.PLAIN, 16));
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(null);
        contentArea.setMargin(new Insets(10, 10, 10, 10));
        contentArea.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        container.add(contentArea);

        container.add(Box.createVerticalStrut(20));

        JTextArea commentInput = new JTextArea();
        commentInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        commentInput.setLineWrap(true);
        commentInput.setWrapStyleWord(true);

        JScrollPane commentInputScroll = new JScrollPane(commentInput);
        commentInputScroll.setBorder(BorderFactory.createTitledBorder("Leave a Comment"));
        commentInputScroll.setPreferredSize(new Dimension(600, 100));
        commentInputScroll.setMaximumSize(new Dimension(600, 100));
        commentInputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        container.add(commentInputScroll);

        container.add(Box.createVerticalStrut(10));

        JButton submitComment = new JButton("Submit Comment");
        submitComment.setBackground(new Color(0, 128, 0));
        submitComment.setForeground(Color.WHITE);
        submitComment.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitComment.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(submitComment);

        container.add(Box.createVerticalStrut(20));

        commentDisplayPanel = new JPanel();
        commentDisplayPanel.setLayout(new BoxLayout(commentDisplayPanel, BoxLayout.Y_AXIS));
        commentDisplayPanel.setBackground(Color.WHITE);

        commentDisplayScroll = new JScrollPane(commentDisplayPanel);
        commentDisplayScroll.setBorder(BorderFactory.createTitledBorder("Comments"));
        commentDisplayScroll.setPreferredSize(new Dimension(600, 100));
        commentDisplayScroll.setMaximumSize(new Dimension(600, 100));
        commentDisplayScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        container.add(commentDisplayScroll);

        submitComment.addActionListener(e -> {
            String comment = commentInput.getText().trim();
            if (!comment.isEmpty()) {
                saveCommentToDatabase(blogId, UserSession.getUserId(), comment);
                loadCommentsFromDatabase();
                commentInput.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Comment cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JScrollPane mainScroll = new JScrollPane(container);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(mainScroll);

        loadCommentsFromDatabase();
    }

    private void addComment(String username, String comment) {
        JLabel commentLabel = new JLabel("<html><b>" + username + ":</b> <span style='width:500px'>" + comment + "</span></html>");
        commentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        commentLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        commentDisplayPanel.add(commentLabel);
        commentDisplayPanel.revalidate();
        commentDisplayPanel.repaint();
    }

    private void loadCommentsFromDatabase() {
        commentDisplayPanel.removeAll();

        String url = "jdbc:mysql://localhost:3306/blogging_app";
        String dbUser = "root";  // replace with your DB username
        String dbPass = "1234";  // replace with your DB password

        String sql = "SELECT u.username, c.comments FROM comments c JOIN users u ON c.user_id = u.user_id WHERE c.blog_id = ? ORDER BY c.comment_id DESC ";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, blogId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                String comment = rs.getString("comments");
                addComment(username, comment);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load comments.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCommentToDatabase(int blogId, int userId, String comment) {
        String url = "jdbc:mysql://localhost:3306/blogging_app";
        String dbUser = "root";  // replace with your DB username
        String dbPass = "1234";  // replace with your DB password

        String sql = "INSERT INTO comments (blog_id, user_id, comments) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, blogId);
            stmt.setInt(2, userId);
            stmt.setString(3, comment);
            stmt.executeUpdate();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save comment to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon convertBlobToImageIcon(Blob blob, int width, int height)
    {
        try {
            InputStream is = blob.getBinaryStream();
            BufferedImage originalImage = ImageIO.read(is);
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
