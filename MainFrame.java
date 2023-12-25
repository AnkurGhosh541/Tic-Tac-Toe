import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener, ResetGameListener {
    private GameArea gameArea = null;
    public MainFrame(){
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        init();
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem newItem = new JMenuItem("New Game");
        newItem.addActionListener(this);
        menuBar.add(newItem);
        setJMenuBar(menuBar);

        setNewGame();
    }

    public void setNewGame() {
        int gameType = JOptionPane.showOptionDialog(this, "Choose game mode",
                "Choose Mode", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{
                        "Player vs Player", "Player vs Computer"
                }, "Player vs Player");

        String opponentType = "HUMAN";
        if (gameType == JOptionPane.CLOSED_OPTION) {
            System.exit(1);
        } else if (gameType == 0) {
            opponentType = "HUMAN";
        } else if (gameType == 1) {
            opponentType = "AI";
        }

        if(gameArea != null)
            remove(gameArea);
        gameArea = new GameArea(opponentType);
        gameArea.addResetGameListener(this);
        add(gameArea);


        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if(cmd.equalsIgnoreCase("new game")) {
            setNewGame();
        }
    }

    @Override
    public void resetGameRequested(ResetGameEvent event) {
        setNewGame();
    }
}
