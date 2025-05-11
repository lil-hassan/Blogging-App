import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

public class BlogChoice extends javax.swing.JFrame
{
    public BlogChoice()
    {
        // Set FlatLaf Look and Feel
        try
        {
            UIManager.setLookAndFeel(new FlatLightLaf());
        }

        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }

        initComponents();
        setTitle("BlogsVerse");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("blogg.png")));

        // Set icons if available
        URL createIconPath = getClass().getResource("/createAcc.png");
        if (createIconPath != null)
            jLabel5.setIcon(new ImageIcon(new ImageIcon(createIconPath).getImage().getScaledInstance(jLabel5.getWidth(), jLabel5.getHeight(), java.awt.Image.SCALE_SMOOTH)));

        URL loginIconPath = getClass().getResource("/login.png");
        if (loginIconPath != null)
            jLabel7.setIcon(new ImageIcon(new ImageIcon(loginIconPath).getImage().getScaledInstance(jLabel7.getWidth(), jLabel7.getHeight(), java.awt.Image.SCALE_SMOOTH)));
    }

    private void initComponents()
    {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        jPanel1.setLayout(null);

        jLabel1.setText("BlogsVerse");
        jLabel1.setFont(new Font("Open Sans", Font.BOLD, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 10, 300, 30);

        jPanel2.setLayout(null);
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                new BlogRegister().setVisible(true);
                dispose();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                jPanel2.setBackground(Color.LIGHT_GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel2.setBackground(Color.WHITE);
            }
        });

        jLabel4.setText("Create Account");
        jLabel4.setFont(new Font("Open Sans", Font.PLAIN, 14));
        jPanel2.add(jLabel4);
        jLabel4.setBounds(110, 10, 120, 25);
        jPanel2.add(jLabel5);
        jLabel5.setBounds(60, 5, 40, 30);
        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 50, 300, 40);

        jPanel3.setLayout(null);
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new BlogLogin().setVisible(true);
                dispose();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel3.setBackground(Color.LIGHT_GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel3.setBackground(Color.WHITE);
            }
        });

        jLabel6.setText("Login");
        jLabel6.setFont(new Font("Open Sans", Font.PLAIN, 14));
        jPanel3.add(jLabel6);
        jLabel6.setBounds(110, 10, 120, 25);
        jPanel3.add(jLabel7);
        jLabel7.setBounds(60, 5, 40, 30);
        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 100, 300, 40);

        add(jPanel1);
        jPanel1.setBounds(0, 0, 300, 160);

        setSize(300, 225);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new BlogChoice().setVisible(true);
    }

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
}
