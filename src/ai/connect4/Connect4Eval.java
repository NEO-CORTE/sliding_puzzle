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
            return 0.0;
        }
    }
}
