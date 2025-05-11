import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import utils.DatabaseConnection;
import com.formdev.flatlaf.FlatLightLaf;

public class BlogLogin extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel iconLabel;

    public BlogLogin() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        initComponents();
        setTitle("BlogsVerse");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("blogg.png")));
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Login to BlogsVerse");
        heading.setFont(new Font("Open Sans", Font.BOLD, 20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBounds(80, 20, 250, 30);
        mainPanel.add(heading);

        iconLabel = new JLabel();
        iconLabel.setBounds(170, 60, 60, 60);
        URL iconPath = getClass().getResource("/login.png");
        if (iconPath != null) {
            iconLabel.setIcon(new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        }
        mainPanel.add(iconLabel);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Open Sans", Font.PLAIN, 14));
        lblUsername.setBounds(60, 140, 80, 25);
        mainPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Open Sans", Font.PLAIN, 14));
        txtUsername.setBounds(150, 140, 180, 25);
        mainPanel.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Open Sans", Font.PLAIN, 14));
        lblPassword.setBounds(60, 180, 80, 25);
        mainPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Open Sans", Font.PLAIN, 14));
        txtPassword.setBounds(150, 180, 180, 25);
        mainPanel.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Open Sans", Font.BOLD, 14));
        btnLogin.setBackground(new Color(0, 128, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(80, 230, 100, 35);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> login());
        mainPanel.add(btnLogin);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Open Sans", Font.BOLD, 14));
        btnBack.setBounds(210, 230, 100, 35);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {new BlogChoice().setVisible(true);
            SwingUtilities.getWindowAncestor(btnBack).dispose();});
        mainPanel.add(btnBack);

        setContentPane(mainPanel);
    }

    private void login()
    {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        try
        {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE username = ? AND passwords = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");

                // ✅ Set the session
                UserSession.setUser(userId, username);
                //UserSession.setUserId(userIdFromDatabase);


                //JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + username + "!");
                new BlogHome().setVisible(true);
                UserSession.setUserId(userId);
                dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }

            rs.close();
            stmt.close();
        }

        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new BlogLogin().setVisible(true));
    }
}
