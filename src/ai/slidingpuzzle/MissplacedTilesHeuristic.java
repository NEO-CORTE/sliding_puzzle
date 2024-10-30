package ai.slidingpuzzle;

import sac.State;
import sac.StateFunction;

public class MissplacedTilesHeuristic extends StateFunction{

    @Override
    public double calculate(State state) {
        SlidingPuzzle slidingPuzzle = (SlidingPuzzle) state;

        double cnt = 0;
        int n = SlidingPuzzle.getN();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(slidingPuzzle.board[i][j] == 0){
                    continue;
                }
                if(slidingPuzzle.board[i][j] != SlidingPuzzle.solution[i][j]){
                    cnt++;
                }
            }
        }
        return cnt;
    }
}