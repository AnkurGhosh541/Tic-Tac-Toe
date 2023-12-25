import javax.swing.*;
import java.awt.*;

public class GameLogic {
    private Player currentPlayer;
    private Player opponent;
    private final String opponentType;
    private final Player[][] board;

    public GameLogic(String opponentType) {
        board = new Player[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = null;
            }
        }
        currentPlayer = Player.PLAYER;
        opponent = Player.OPPONENT;
        this.opponentType = opponentType;
    }

    public int makeHumanMove(int[] position) {
        board[position[0]][position[1]] = currentPlayer;

        if (isGameOver()) {
            int option = JOptionPane.showConfirmDialog(null, "Do you want to play again ?",
                    "Reset game or Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            return option;
        }
        return 1;
    }

    public int makeAIMove() {
        int[] move = findBestMove(board);
        board[move[0]][move[1]] = currentPlayer;
        GameArea.buttonArr[move[0]][move[1]].setText("O");

        if (isGameOver()) {
            int option = JOptionPane.showConfirmDialog(null, "Do you want to play again ?",
                    "Reset game or Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            return option;
        }

        return 1;
    }


    private int[] findBestMove(Player[][] board) {
        int bestVal = Integer.MIN_VALUE;
        int bestI = -1;
        int bestJ = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null) {
                    board[i][j] = currentPlayer;
                    int curVal = minimax(board, 0, false);
                    board[i][j] = null;

                    if (curVal > bestVal) {
                        bestI = i;
                        bestJ = j;
                        bestVal = curVal;
                    }
                }
            }
        }

        return new int[]{bestI, bestJ};
    }

    private boolean isWinner() {
        // check horizontal
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                GameArea.buttonArr[i][0].setForeground(Color.RED);
                GameArea.buttonArr[i][1].setForeground(Color.RED);
                GameArea.buttonArr[i][2].setForeground(Color.RED);
                System.out.println("Winning combination: ");
                printBoard(board);
                return true;
            }
        }

        // check vertical
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                GameArea.buttonArr[0][i].setForeground(Color.RED);
                GameArea.buttonArr[1][i].setForeground(Color.RED);
                GameArea.buttonArr[2][i].setForeground(Color.RED);
                System.out.println("Winning combination: ");
                printBoard(board);
                return true;
            }
        }

        // check diagonal
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            GameArea.buttonArr[0][0].setForeground(Color.RED);
            GameArea.buttonArr[1][1].setForeground(Color.RED);
            GameArea.buttonArr[2][2].setForeground(Color.RED);
            System.out.println("Winning combination: ");
            printBoard(board);
            return true;
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            GameArea.buttonArr[0][2].setForeground(Color.RED);
            GameArea.buttonArr[1][1].setForeground(Color.RED);
            GameArea.buttonArr[2][0].setForeground(Color.RED);
            System.out.println("Winning combination: ");
            printBoard(board);
            return true;
        }

        return false;
    }

    private int evaluate(Player[][] board) {
        // check horizontal
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == currentPlayer) return 10;
                else if (board[i][0] == opponent) return -10;
            }
        }

        // check vertical
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if (board[0][i] == currentPlayer) return 10;
                else if (board[0][i] == opponent) return -10;
            }
        }

        // check diagonal
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == currentPlayer) return 10;
            else if (board[0][0] == opponent) return -10;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == currentPlayer) return 10;
            else if (board[0][2] == opponent) return -10;
        }

        return 0;
    }

    private int minimax(Player[][] board, int depth, boolean isMaximizing) {
        int score = evaluate(board);

        if (score == 10)
            return score - depth;

        if (score == -10)
            return score + depth;

        if (isBoardFull(board))
            return 0;

        int bestScore;
        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == null) {
                        board[i][j] = currentPlayer;
                        int curScore = minimax(board, depth + 1, false);
                        board[i][j] = null;
                        bestScore = Math.max(bestScore, curScore);
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == null) {
                        board[i][j] = opponent;
                        int curScore = minimax(board, depth + 1, true);
                        board[i][j] = null;
                        bestScore = Math.min(bestScore, curScore);
                    }
                }
            }

        }
        return bestScore;
    }

    private boolean isBoardFull(Player[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null) return false;
            }
        }
        return true;
    }

    private boolean isGameOver() {
        if (isWinner()) {
            if (opponentType.equalsIgnoreCase("HUMAN")) {
                if (currentPlayer == Player.PLAYER) {
                    JOptionPane.showMessageDialog(null, "Player 1 has won");
                } else {
                    JOptionPane.showMessageDialog(null, "Player 2 has won");
                }
            } else {
                if (currentPlayer == Player.PLAYER) {
                    JOptionPane.showMessageDialog(null, "Player has won");
                } else {
                    JOptionPane.showMessageDialog(null, "Computer has won");
                }
            }
            System.out.println(currentPlayer + " has won");
            return true;
        }

        if (isBoardFull(board)) {
            JOptionPane.showMessageDialog(null, "Match Draw");
            System.out.println("Match Draw");
            return true;
        }

        return false;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.PLAYER) ? Player.OPPONENT : Player.PLAYER;
        opponent = (currentPlayer == Player.PLAYER) ? Player.OPPONENT : Player.PLAYER;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getOpponentType() {
        return opponentType;
    }

    private static void printBoard(Player[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}


