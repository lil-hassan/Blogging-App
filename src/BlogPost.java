import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import com.formdev.flatlaf.FlatLightLaf;

public class BlogPost extends JFrame {

    private JTextField titleField;
    private JTextArea contentArea;
    private JLabel imagePathLabel;
    private String selectedImagePath;


    public BlogPost() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("BlogsVerse - Create New Blog");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("blogg.png")));
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        logoPanel.setOpaque(false);
        setTitle("BlogsVerse - Create New Blog ");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("blogg.png")));
        ImageIcon icon = new ImageIcon(getClass().getResource("blogg.png"));
        Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));

        JLabel title = new JLabel("Create Blog Post");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoPanel.add(logoLabel);
        logoPanel.add(title);
        header.add(logoPanel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(245, 245, 245));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(550, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        titleField = new JTextField();
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        titleField.setBorder(BorderFactory.createTitledBorder("Title"));

        contentArea = new JTextArea(7, 50);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(BorderFactory.createTitledBorder("Content"));

        JButton imageButton = new JButton("Upload JPG Image");
        imageButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        imageButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        imagePathLabel = new JLabel("No image selected");
        imagePathLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        imagePathLabel.setForeground(Color.GRAY);
        imagePathLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        imageButton.addActionListener(e -> chooseImage());

        JButton postButton = new JButton("Post");
        postButton.setBackground(new Color(0, 150, 0));
        postButton.setForeground(Color.WHITE);
        postButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        postButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        postButton.setFocusPainted(false);
        postButton.setMaximumSize(new Dimension(100, 35));
        postButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        postButton.addActionListener(e -> {
            String titleText = titleField.getText().trim();
            String contentText = contentArea.getText().trim();

            if (titleText.isEmpty() || contentText.isEmpty() || selectedImagePath == null)
            {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields.",
                        "Incomplete Post",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int currentUserId = getCurrentUserId();

            if (currentUserId == -1)
            {
                JOptionPane.showMessageDialog(this,
                        "No user is currently logged in. Please log in again.",
                        "Session Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try
            {
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/blogging_app", "root", "1234");

                File imgFile = new File(selectedImagePath);
                FileInputStream fis = new FileInputStream(imgFile);

                String sql = "INSERT INTO blogs (user_id, title, content, image) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setInt(1, currentUserId);
                stmt.setString(2, titleText);
                stmt.setString(3, contentText);
                stmt.setBinaryStream(4, fis, (int) imgFile.length());

                int rows = stmt.executeUpdate();

                if (rows > 0)
                {
                    JOptionPane.showMessageDialog(this, "Blog posted successfully!");
                    new BlogHome().setVisible(true);
                    dispose();

                }

                else
                {
                    JOptionPane.showMessageDialog(this, "Failed to post blog.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                stmt.close();
                con.close();
            }

            catch (Exception ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(titleField);
        card.add(Box.createVerticalStrut(15));
        card.add(contentScroll);
        card.add(Box.createVerticalStrut(15));
        card.add(imageButton);
        card.add(Box.createVerticalStrut(5));
        card.add(imagePathLabel);
        card.add(Box.createVerticalStrut(25));
        card.add(postButton);

        container.add(card);
        add(container, BorderLayout.CENTER);
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPG Images", "jpg", "jpeg"));
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selected = chooser.getSelectedFile();
            selectedImagePath = selected.getAbsolutePath();
            imagePathLabel.setText("Selected: " + selected.getName());
        }
    }

    private int getCurrentUserId()
    {
        return UserSession.getUserId(); // Pull user ID from session
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new BlogPost().setVisible(true));
    }
}
