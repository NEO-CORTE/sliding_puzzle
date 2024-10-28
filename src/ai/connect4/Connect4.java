package ai.connect4;

import java.util.List;
import sac.game.GameState;
import sac.game.GameStateImpl;

public class Connect4 extends GameStateImpl{

    public static final int m = 6;
    public static final int n = 7;

    private static final byte O = 1;
    private static final byte EMPTY = 0;
    private static final byte X = -1;

    private int iLast = -1;
    private int jLast = -1;

    private enum Symbols{
        X, E, O
    }

    private byte[][] board;

    Connect4(){
        board = new byte[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public boolean move(int j){
        int i = m - 1;
        for (; i >= 0 ; i--) {
            if(board[i][j] == EMPTY){
                break;
            }
        }

        if(i < 0){
            return false;
        }

        board[i][j] = (this.isMaximizingTurnNow()) ? O : X;
        this.setMaximizingTurnNow(!isMaximizingTurnNow());

        this.iLast = i;
        this.jLast = j;

        return true;
    }

    private boolean isWin(){
        if(iLast < 0 || jLast < 0){
            return false;
        }

        byte symbol = board[this.iLast][this.jLast];

        int cnt = 0;
        for (int k = 1; k < 4; k++) {
            if((this.jLast + k == n) || (board[this.iLast][jLast + k] != symbol)){
                break;
            }
            cnt++;
        }
        for (int k = 1; k < 4; k++) {
            if((this.jLast - k == -1) || (board[this.iLast][jLast - k] != symbol)){
                break;
            }
            cnt++;
        }
        if(cnt == 3){
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        for (int j = 0; j < n; j++) {
            txt.append(" ");
            txt.append(j);
        }
        txt.append("\n");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                txt.append("|");
                Symbols symbol = Symbols.values()[board[i][j]+1];
                if(symbol == Symbols.E){
                    txt.append(".");
                }
                else{
                    txt.append(symbol);
                }
            }
            txt.append("|\n");
        }
        for (int j = 0; j < n; j++) {
            txt.append(" ");
            txt.append(j);
        }
        return txt.toString();
    }

    @Override
    public List<GameState> generateChildren() {
        return List.of();
    }

    public static void main(String[] args) {
        Connect4 connect4 = new Connect4();
        connect4.move(3);
        connect4.move(3);
        connect4.move(2);
        connect4.move(2);
        connect4.move(4);
        connect4.move(4);
        connect4.move(5);
        System.out.println(connect4.isWin());
        System.out.println(connect4.toString());
    }
}
