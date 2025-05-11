import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import utils.DatabaseConnection;
import com.formdev.flatlaf.FlatLightLaf;

public class BlogRegister extends JFrame
{
    private JTextField userIdField, usernameField, emailField;
    private JPasswordField passwordField;

    public BlogRegister() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        initComponents();
        setTitle("BlogsVerse - Create Account");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("blogg.png")));
        setSize(450, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Create Your Account");
        heading.setFont(new Font("Open Sans", Font.BOLD, 20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBounds(100, 20, 250, 30);
        panel.add(heading);

        JLabel iconLabel = new JLabel();
        iconLabel.setBounds(190, 60, 60, 60);
        URL iconPath = getClass().getResource("/createAcc.png");
        if (iconPath != null) {
            iconLabel.setIcon(new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        }
        panel.add(iconLabel);

        JLabel lblUserId = new JLabel("User ID:");
        lblUserId.setFont(new Font("Open Sans", Font.PLAIN, 14));
        lblUserId.setBounds(60, 140, 100, 25);
        panel.add(lblUserId);

        userIdField = new JTextField();
        userIdField.setFont(new Font("Open Sans", Font.PLAIN, 14));
        userIdField.setBounds(160, 140, 200, 25);
        panel.add(userIdField);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Open Sans", Font.PLAIN, 14));
        lblUsername.setBounds(60, 180, 100, 25);
        panel.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Open Sans", Font.PLAIN, 14));
        usernameField.setBounds(160, 180, 200, 25);
        panel.add(usernameField);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Open Sans", Font.PLAIN, 14));
        lblEmail.setBounds(60, 220, 100, 25);
        panel.add(lblEmail);

        emailField = new JTextField();
        emailField.setFont(new Font("Open Sans", Font.PLAIN, 14));
        emailField.setBounds(160, 220, 200, 25);
        panel.add(emailField);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Open Sans", Font.PLAIN, 14));
        lblPassword.setBounds(60, 260, 100, 25);
        panel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Open Sans", Font.PLAIN, 14));
        passwordField.setBounds(160, 260, 200, 25);
        panel.add(passwordField);

        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Open Sans", Font.BOLD, 14));
        btnRegister.setBackground(new Color(0, 128, 0));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBounds(90, 310, 120, 35);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> register());
        panel.add(btnRegister);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Open Sans", Font.BOLD, 14));
        btnBack.setBounds(230, 310, 120, 35);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {new BlogChoice().setVisible(true);
                                    SwingUtilities.getWindowAncestor(btnBack).dispose();});
        panel.add(btnBack);

        setContentPane(panel);
    }

    private void register()
    {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@gmail.com") && !email.contains("@yahoo.com"))
        {
            JOptionPane.showMessageDialog(this, "Enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try
        {
            Connection conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO users (username, email, passwords) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);

            stmt.executeUpdate();
            stmt.close();

            JOptionPane.showMessageDialog(this, "Account successfully created for: " + username);
            new BlogLogin().setVisible(true);
            dispose();
        }
        catch (SQLIntegrityConstraintViolationException ex)
        {
            JOptionPane.showMessageDialog(this, "Email already exists. Please use a different one.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        userIdField.setText("");
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BlogRegister().setVisible(true));
    }
}
