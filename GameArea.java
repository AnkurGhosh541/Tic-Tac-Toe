import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameArea extends JPanel implements ActionListener {
    private final Map<JButton, int[]> buttonMap;
    private final GameLogic gameLogic;
    private final GridBagConstraints c;

    static JButton[][] buttonArr = new JButton[3][3];

    private PlayingSide player;
    private PlayingSide computer;

    private final ArrayList<ResetGameListener> resetGameListeners = new ArrayList<>();

    public GameArea(String opponentType) {
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        gameLogic = new GameLogic(opponentType);
        buttonMap = new HashMap<>(9);
        paintGrid();
        paintPlayerPanels(opponentType);
    }

    private void paintGrid() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(50, 50));
                b.setFont(new Font(Font.MONOSPACED, Font.BOLD, 256));
                b.setFocusable(false);
                b.addActionListener(this);
                mainPanel.add(b);
                buttonMap.put(b, new int[]{i, j});
                buttonArr[i][j] = b;
            }
        }
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        add(mainPanel, c);
    }

    private void paintPlayerPanels(String opponentType) {
        String p1, p2;

        if(opponentType.equalsIgnoreCase("HUMAN")) {
            p1 = "Player 1";
            p2 = "Player 2";
        } else {
            p1 = "Player  ";
            p2 = "Computer";
        }

        p1 = String.format("%10s", p1);
        player = new PlayingSide(p1);
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx = 0.35;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        add(player, c);

        p2 = String.format("%10s", p2);
        computer = new PlayingSide(p2);
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx = 0.35;
        c.weighty = 1;
        c.gridx = 2;
        c.gridy = 0;
        add(computer, c);

        player.setActivePlayer();
    }

    public void addResetGameListener(ResetGameListener listener) {
        resetGameListeners.add(listener);
    }

    private void notifyResetGameListeners() {
        ResetGameEvent event = new ResetGameEvent(this);
        for (ResetGameListener listener : resetGameListeners) {
            listener.resetGameRequested(event);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        int isOver;
        if(gameLogic.getOpponentType().equalsIgnoreCase("AI")) {
            if(gameLogic.getCurrentPlayer() == Player.PLAYER) {
                b.setText("X");
                b.removeActionListener(this);
                isOver = gameLogic.makeHumanMove(buttonMap.get(b));
                if (isOver == 1) {
                    gameLogic.switchPlayer();
                    computer.setActivePlayer();
                    player.setInactivePlayer();
                } else if (isOver == 0) {
                    notifyResetGameListeners();
                    return;
                } else {
                    System.exit(1);
                }

                isOver = gameLogic.makeAIMove();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (buttonArr[i][j].getText().equalsIgnoreCase("O")) {
                            buttonArr[i][j].removeActionListener(this);
                        }
                    }
                }

                if (isOver == 1) {
                    gameLogic.switchPlayer();
                    player.setActivePlayer();
                    computer.setInactivePlayer();
                } else if (isOver == 0) {
                    notifyResetGameListeners();
                } else {
                    System.exit(1);
                }
            }
        } else {
            if (gameLogic.getCurrentPlayer() == Player.PLAYER) {
                b.setText("X");
                b.removeActionListener(this);
                isOver = gameLogic.makeHumanMove(buttonMap.get(b));
                if (isOver == 1) {
                    gameLogic.switchPlayer();
                    computer.setActivePlayer();
                    player.setInactivePlayer();
                } else if (isOver == 0) {
                    notifyResetGameListeners();
                } else {
                    System.exit(1);
                }
            } else {
                b.setText("O");
                b.removeActionListener(this);
                isOver = gameLogic.makeHumanMove(buttonMap.get(b));
                if (isOver == 1) {
                    gameLogic.switchPlayer();
                    player.setActivePlayer();
                    computer.setInactivePlayer();
                } else if (isOver == 0) {
                    notifyResetGameListeners();
                } else {
                    System.exit(1);
                }
            }
        }
    }
}
