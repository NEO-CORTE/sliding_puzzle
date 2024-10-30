package ai.slidingpuzzle;

import sac.State;
import sac.StateFunction;

public class ManhattanHeuristic extends StateFunction{
    @Override
    public double calculate(State state) {
        SlidingPuzzle slidingPuzzle = (SlidingPuzzle) state;
        double heuristic = 0;
        for (int i = 0; i < slidingPuzzle.board.length; i++) {
            for (int j = 0; j < slidingPuzzle.board[i].length; j++) {
                int value = slidingPuzzle.board[i][j];

                if (value != 0) { // Ignore the blank tile (0)
                    // Find the goal position of the current tile value in the solution board
                    int[] targetPosition = findPosition(SlidingPuzzle.solution, value);
                    assert targetPosition != null;
                    int targetRow = targetPosition[0];
                    int targetCol = targetPosition[1];

                    // Calculate Manhattan distance
                    int distance = Math.abs(i - targetRow) + Math.abs(j - targetCol);

                    // Add distance to the heuristic
                    heuristic += distance;
                }
            }
        }
        return heuristic;

    }

    private static int[] findPosition(short[][] board, int value) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == value) {
                    return new int[] { row, col };
                }
            }
        }
        return null;
    }
}