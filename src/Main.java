import javax.swing.*;

public class Main {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            BlogChoice choice = new BlogChoice();
            choice.setVisible(true);
        });
    }
}
