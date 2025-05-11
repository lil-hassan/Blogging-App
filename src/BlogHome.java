import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.formdev.flatlaf.FlatLightLaf;

public class BlogHome extends JFrame
{

    public BlogHome()
    {
        try
        {
            UIManager.setLookAndFeel(new FlatLightLaf());
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }

        setTitle("BlogsVerse");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("blogg.png")));
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents()
    {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(null)
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint shadow = new GradientPaint(
                        0, getHeight() - 5, new Color(0, 0, 0, 50),
                        0, getHeight(), new Color(0, 0, 0, 0)
                );
                g2.setPaint(shadow);
                g2.fillRect(0, getHeight() - 5, getWidth(), 5);
            }
        };
        headerPanel.setPreferredSize(new Dimension(600, 100));
        headerPanel.setBackground(Color.WHITE);

        JLabel logoLabel = new JLabel();
        logoLabel.setBounds(20, 20, 50, 50);
        URL logoPath = getClass().getResource("/blogg.png");
        if (logoPath != null) {
            logoLabel.setIcon(new ImageIcon(new ImageIcon(logoPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        }
        headerPanel.add(logoLabel);

        String username = UserSession.getUsername();
        JLabel welcomeLabel = new JLabel("Welcome " + (username != null ? username : "User") + "!");
        welcomeLabel.setFont(new Font("Open Sans", Font.BOLD, 20));
        welcomeLabel.setBounds(80, 30, 300, 30);
        headerPanel.add(welcomeLabel);

        JButton postButton = new JButton("Post Blog");
        postButton.setFont(new Font("Open Sans", Font.BOLD, 14));
        postButton.setBackground(new Color(0, 150, 0));
        postButton.setForeground(Color.WHITE);
        postButton.setBounds(440, 30, 120, 35);
        postButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //postButton.addActionListener(e -> new BlogPost().setVisible(true));
        postButton.addActionListener(e -> {
            new BlogPost().setVisible(true); // Open the new window
            dispose();                       // Close the current one
        });

        headerPanel.add(postButton);

        add(headerPanel, BorderLayout.NORTH);

        JPanel blogPanel = new JPanel();
        blogPanel.setLayout(new BoxLayout(blogPanel, BoxLayout.Y_AXIS));
        blogPanel.setBackground(new Color(245, 245, 245));

        List<String> titles = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        List<String> contents = new ArrayList<>();
        List<Blob> images = new ArrayList<>();
        List<Integer> blogIDs = new ArrayList<>();
        List<Integer> userIDs = new ArrayList<>();


        try
        {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/blogging_app", "root", "1234");
            String sql = "SELECT b.title, b.blog_id, u.user_id, b.content, b.image, u.username FROM blogs b JOIN users u ON b.user_id = u.user_id ORDER BY blog_id DESC";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                titles.add(rs.getString("title"));
                authors.add(rs.getString("username"));
                contents.add(rs.getString("content"));
                images.add(rs.getBlob("image")) ;
                blogIDs.add(rs.getInt("blog_id"));
                userIDs.add(rs.getInt("user_id"));
            }

            rs.close();
            stmt.close();
            con.close();
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }

        for (int i = 0; i < titles.size(); i++)
        {
            String blogTitle = titles.get(i);
            String blogAuthor = authors.get(i);
            String blogContent = contents.get(i);
            Blob blogImage = images.get(i);
            int blogID = blogIDs.get(i);
            int userID = userIDs.get(i);

            RoundedPanel blogCard = new RoundedPanel(20);
            blogCard.setLayout(new BorderLayout());
            blogCard.setMaximumSize(new Dimension(550, 120));
            blogCard.setBackground(Color.WHITE);
            blogCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            blogCard.setAlignmentX(Component.CENTER_ALIGNMENT);
            blogCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

            Color normalColor = Color.WHITE;
            Color hoverColor = new Color(230, 245, 255);

            blogCard.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseEntered(MouseEvent e)
                {
                    blogCard.setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e)
                {
                    blogCard.setBackground(normalColor);
                }

                @Override
                public void mouseClicked(MouseEvent e)
                {
                    new BlogRead(blogID, userID, blogTitle, blogContent, blogAuthor, blogImage).setVisible(true);
                }
            });

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            JLabel title = new JLabel(blogTitle);
            title.setFont(new Font("Open Sans", Font.BOLD, 15));
            title.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));

            JLabel meta = new JLabel("by " + blogAuthor + " • 2 minutes ago");
            meta.setFont(new Font("Open Sans", Font.PLAIN, 12));
            meta.setForeground(Color.GRAY);
            meta.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));

            textPanel.add(title);
            textPanel.add(meta);
            blogCard.add(textPanel, BorderLayout.CENTER);

            blogPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            blogPanel.add(blogCard);
        }

        JScrollPane scrollPane = new JScrollPane(blogPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);


    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new BlogHome().setVisible(true));
    }
}

class RoundedPanel extends JPanel
{
    private int radius;

    public RoundedPanel(int radius)
    {
        super();
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}
