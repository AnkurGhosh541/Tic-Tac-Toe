import javax.swing.*;
import java.awt.*;

public class PlayingSide extends JPanel {

    private final JLabel playerName;
    public PlayingSide(String name) {
        setLayout(new GridBagLayout());
        playerName = new JLabel(name);
        playerName.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        add(playerName);
    }

    public void setActivePlayer() {
        playerName.setForeground(Color.RED);
    }

    public void setInactivePlayer() {
        playerName.setForeground(Color.BLACK);
    }

}
