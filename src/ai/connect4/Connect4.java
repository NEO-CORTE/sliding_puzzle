package ai.connect4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import sac.game.*;

public class Connect4 extends GameStateImpl{

    public static final boolean IS_MAX_AI = true;
    public static final boolean IS_MIN_AI = true;

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

    private int moveIndex = 0;

    Connect4(){
        board = new byte[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    Connect4(Connect4 parent){
        board = new byte[m][n];
        this.iLast = parent.iLast;
        this.jLast = parent.jLast;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = parent.board[i][j];
            }
        }
        this.moveIndex = parent.moveIndex;
        this.setMaximizingTurnNow(parent.isMaximizingTurnNow());
    }

    public byte[][] getBoard(){
        return this.board;
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

        moveIndex++;

        return true;
    }

    public boolean isTie(){
        return moveIndex == m*n;
    }

    public boolean isWin(){
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
        if(cnt >= 3){
            return true;
        }

//        Up = Down
        cnt = 0;
        for (int k = 1; k < 4; k++) {
            if((this.iLast + k == m) || (board[this.iLast + k][jLast] != symbol)){
                break;
            }
            cnt++;
        }
        for (int k = 1; k < 4; k++) {
            if((this.iLast - k == -1) || (board[this.iLast - k][jLast] != symbol)){
                break;
            }
            cnt++;
        }
        if(cnt >= 3) {
            return true;
        }

        cnt = 0;
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
        if(cnt >= 3){
            return true;
        }

//      SLASH
        cnt = 0;
        for (int k = 1; k < 4; k++) {
            if((this.iLast - k == -1) || (this.jLast + k == n) || (board[this.iLast - k][jLast + k] != symbol)){
                break;
            }
            cnt++;
        }
        for (int k = 1; k < 4; k++) {
            if((this.iLast + k == m) || (this.jLast - k == -1) || (board[this.iLast + k][jLast - k] != symbol)){
                break;
            }
            cnt++;
        }
        if(cnt >= 3){
            return true;
        }

        cnt = 0;
        for (int k = 1; k < 4; k++) {
            if((this.iLast - k == -1) || (this.jLast - k == -1) || (board[this.iLast - k][jLast - k] != symbol)){
                break;
            }
            cnt++;
        }
        for (int k = 1; k < 4; k++) {
            if((this.iLast + k == m) || (this.jLast + k == n) || (board[this.iLast + k][jLast + k] != symbol)){
                break;
            }
            cnt++;
        }
        if(cnt >= 3){
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
        List<GameState> children = new ArrayList<>();
        for(int j = 0; j < m; j++){
            Connect4 child = new Connect4(this);
            if(child.move(j)){
                children.add(child);
                child.setMoveName(Integer.toString(j));
            }
        }
        return children;
    }

    @Override
    public int hashCode() {
        byte[] flat = new byte[n*m];
        int k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                flat[k++] = board[i][j];
            }
        }
        return Arrays.hashCode(flat);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameSearchConfigurator conf1 = new GameSearchConfigurator();
        GameSearchConfigurator conf2 = new GameSearchConfigurator();

        conf1.setDepthLimit(3.5);
        conf2.setDepthLimit(5.5);

        GameSearchAlgorithm algo1 = new AlphaBetaPruning();
        GameSearchAlgorithm algo2 = new AlphaBetaPruning();

        algo1.setConfigurator(conf1);
        algo2.setConfigurator(conf2);

        Connect4 connect4 = new Connect4();
        Connect4.setHFunction(new Connect4Eval());
        System.out.println(connect4);

        while(true){
            //  max
            if(!Connect4.IS_MAX_AI){
                System.out.print("MAX PLAYER 'O' YOUR MOVE:\t");
                while(!connect4.move(Integer.parseInt(scanner.nextLine()))){
                    System.out.println("ILLEGAL MOVE, 'O' MAKE A MOVE: ");
                }
            }
            else{
                System.out.println("MAX AI");
                algo1.setInitial(connect4);
                algo1.execute();
                System.out.println("MOVES SCORES: " + algo1.getMovesScores());
                System.out.println("TIME: " + algo1.getDurationTime());
                System.out.println("STATES: " + algo1.getClosedStatesCount());
                System.out.println("DEPTH REACHED: " + algo1.getDepthReached() + "\n\n");
                int bestMove = Integer.parseInt(algo1.getFirstBestMove());
                connect4.move(bestMove);
            }
            System.out.println(connect4);
            if(connect4.isWin()){
                System.out.println("Max player wins");
                break;
            }
            if(connect4.isTie()){
                System.out.println("TIE");
                break;
            }


            //  min
            if(!Connect4.IS_MIN_AI){
                System.out.print("MIN PLAYER 'X' YOUR MOVE:\t");
                while(!connect4.move(Integer.parseInt(scanner.nextLine()))){
                    System.out.println("ILLEGAL MOVE, 'X' MAKE A MOVE: ");
                }
            }
            else{
                System.out.println("MIN AI");
                algo2.setInitial(connect4);
                algo2.execute();
                System.out.println("MOVES SCORES: " + algo2.getMovesScores());
                System.out.println("TIME: " + algo2.getDurationTime());
                System.out.println("STATES: " + algo2.getClosedStatesCount());
                System.out.println("DEPTH REACHED: " + algo2.getDepthReached() + "\n\n");
                int bestMove = Integer.parseInt(algo2.getFirstBestMove());
                connect4.move(bestMove);
            }
            System.out.println(connect4);
            if(connect4.isWin()){
                System.out.println("Min player wins");
                break;
            }
            if(connect4.isTie()){
                System.out.println("TIE");
                break;
            }
        }
        scanner.close();
    }
}
