package ai.missplacedtiles;

import ai.slidingpuzzle.SlidingPuzzle;
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
                if(slidingPuzzle.board[i][j] != 0){
                    continue;
                }
                if(slidingPuzzle.board[i][j] != i * n + j){
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
