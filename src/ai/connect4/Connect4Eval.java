package ai.connect4;

import sac.State;
import sac.StateFunction;

public class Connect4Eval extends StateFunction {
    @Override
    public double calculate(State state) {
        Connect4 connect4 = (Connect4)state;
        if(connect4.isWin()){
            return (connect4.isMaximizingTurnNow()) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        else{
            double score = 0;
            byte[][] board = connect4.getBoard();
            int rows = board.length;
            int cols = board[0].length;

            // Check all directions: horizontal, vertical, diagonal (\ and /)
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (connect4.isMaximizingTurnNow()) {
                        // Maximizing player logic
                        score += evaluatePlayerSequence(1, board, row, col, rows, cols);
                    } else if (board[row][col] == -1) {
                        // Minimizing player logic
                        score -= evaluatePlayerSequence(-1, board, row, col, rows, cols);
                    }
                }
            }

            return score;
        }
    }

    private double evaluatePlayerSequence(int player, byte[][] board, int row, int col, int rows, int cols) {
        double totalScore = 0;

        // Evaluate horizontal
        if (col + 3 < cols) {
            totalScore += evaluateSequence(player, board[row][col + 1], board[row][col + 2], board[row][col + 3]);
        }

        // Evaluate vertical
        if (row + 3 < rows) {
            totalScore += evaluateSequence(player, board[row + 1][col], board[row + 2][col], board[row + 3][col]);
        }

        // Evaluate diagonal (\)
        if (row + 3 < rows && col + 3 < cols) {
            totalScore += evaluateSequence(player, board[row + 1][col + 1], board[row + 2][col + 2], board[row + 3][col + 3]) * 2;
        }

        // Evaluate diagonal (/)
        if (row + 3 < rows && col - 3 >= 0) {
            totalScore += evaluateSequence(player, board[row + 1][col - 1], board[row + 2][col - 2], board[row + 3][col - 3]) * 2;
        }

        return totalScore;
    }

    // Helper to evaluate a single sequence
    private double evaluateSequence(int player, int... sequence) {
        int count = 0;
        int opponent = (player == 1) ? -1 : 1;

        for (int piece : sequence) {
            if (piece == player) count++;
            else if (piece == opponent) return 0; // Blocked sequence, no score
        }

        return switch (count) {
            case 3 -> 10 * count;   // Almost complete sequence
            case 2 -> 5 * count;    // Moderate potential
            case 1 -> count;    // Weak potential
            default -> 0;   // Empty or blocked sequence
        };
    }
}
