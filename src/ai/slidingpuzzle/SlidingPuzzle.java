package ai.slidingpuzzle;

import sac.IdentifierType;
import sac.graph.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SlidingPuzzle extends sac.graph.GraphStateImpl{
    public static final int n = 3;

    public enum moves{U, D, L, R}

    public short[][] board;

    public static short[][] solution;

    public SlidingPuzzle(){
        board = new short[n][n];
        solution = new short[n][n];
        short value = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                solution[i][j] = board[i][j] = value++;
            }
        }
    }

    public SlidingPuzzle(SlidingPuzzle other){
        board = new short[n][n];
        for(int i = 0; i < n; i++){
            System.arraycopy(other.board[i], 0, this.board[i],0, n);
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                output.append(board[i][j]);
                if(j < n - 1){
                    output.append(",");
                }
            }
            output.append("\n");
        }

        return output.toString();
    }

    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList<>();

        SlidingPuzzle childU = new SlidingPuzzle(this);
        SlidingPuzzle childD = new SlidingPuzzle(this);
        SlidingPuzzle childL = new SlidingPuzzle(this);
        SlidingPuzzle childR = new SlidingPuzzle(this);

        if(childU.makeMove(moves.U)){
            childU.setMoveName(String.valueOf(moves.U));
            children.add(childU);
        }
        if(childD.makeMove(moves.D)){
            childD.setMoveName(String.valueOf(moves.D));
            children.add(childD);
        }
        if(childL.makeMove(moves.L)){
            childL.setMoveName(String.valueOf(moves.L));
            children.add(childL);
        }
        if(childR.makeMove(moves.R)){
            childR.setMoveName(String.valueOf(moves.R));
            children.add(childR);
        }

        return children;
    }

    @Override
    public boolean isSolution() {
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(board[i][j] != solution[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Deprecated
    public String getSolutionBoard(){
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                output.append(solution[i][j]);
                if(j < n - 1){
                    output.append(",");
                }
            }
            output.append("\n");
        }

        return output.toString();
    }

    @Deprecated
    public static void testCase3(){
        SlidingPuzzle puzzle = new SlidingPuzzle();
        puzzle.fromString("152036784");

        GraphSearchConfigurator conf = new GraphSearchConfigurator();
        conf.setIdentifierType(IdentifierType.HASH_CODE);

        GraphSearchAlgorithm alg = new AStar(puzzle, conf);
        SlidingPuzzle.setHFunction(new ManhattanHeuristic());
        alg.execute();

        System.out.println(alg.getBestSoFar().getMovesAlongPath());
        System.out.println(alg.getBestSoFar().getMovesAlongPath().size());
    }

    public static int getN() {
        return n;
    }

    public boolean isValidMove(int row, int col, moves move){

        if(row == 0 && move == moves.U){
            return false;
        }

        if(col == 0 && move == moves.L){
            return false;
        }

        if(row == (n - 1) && move == moves.D){
            return false;
        }

        if(col == (n - 1) && move == moves.R){
            return false;
        }

        return true;
    }

    private void _makeMove(int row, int col, moves move){
        short tmp;

        if(move == moves.U){
            tmp = this.board[row-1][col];
            this.board[row-1][col] = 0;
            this.board[row][col] = tmp;

        }
        if(move == moves.D){
            tmp = this.board[row+1][col];
            this.board[row+1][col] = 0;
            this.board[row][col] = tmp;
        }

        if(move == moves.L){
            tmp = this.board[row][col-1];
            this.board[row][col-1] = 0;
            this.board[row][col] = tmp;
        }
        if(move == moves.R){
            tmp = this.board[row][col+1];
            this.board[row][col+1] = 0;
            this.board[row][col] = tmp;
        }

    }

    public boolean makeMove(moves move){
        int pos_i = -1, pos_j = -1;

        outer_loop:
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(board[i][j] == 0){
                    pos_i = i;
                    pos_j = j;
                    break outer_loop;
                }
            }
        }

        if(isValidMove(pos_i, pos_j, move)) {
            _makeMove(pos_i, pos_j, move);
            return true;
        }
        return false;
    }

    public void randomize(int cnt, long seed, int option) {
        if (option == 1) {
            Random random = new Random(seed);
            for (int i = 0; i < cnt; i++) {
                while (true) {
                    if (this.makeMove(moves.values()[random.nextInt(0, 128)%4])) break;
                }
            }
        } else {
            for (int i = 0; i < cnt; i++) {
                while (true) {
                    if (this.makeMove(moves.values()[ThreadLocalRandom.current().nextInt(0, 1024)%4])) break;
                }
            }
        }
    }

    @Override
    public int hashCode() {
        short[] flat = new short[n*n];
        int k = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++) {
                flat[k++] = this.board[i][j];
            }
        }
        return Arrays.hashCode(flat);
    }

    public void fromString(String slidingString){
        int k = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                board[i][j] = Short.parseShort(slidingString.substring(k, k+1));
                k++;
            }
        }
    }

    public static void testCase_MisplacedTiles(int moveNumber, int caseNumber, int option, int disp, boolean debug, boolean times){
        System.out.println("Missplaced Tiles:");

        long avgTime = 0;
        long cStateCount = 0;
        long oStateCount = 0;
        long pathLength = 0;


        GraphSearchConfigurator conf = new GraphSearchConfigurator();
        conf.setIdentifierType(IdentifierType.HASH_CODE);
        SlidingPuzzle.setHFunction(new MissplacedTilesHeuristic());


        for(int i = 0; i < caseNumber; i++){
            SlidingPuzzle puzzle = new SlidingPuzzle();

            puzzle.randomize(moveNumber, i + disp, option);

            GraphSearchAlgorithm alg = new AStar(puzzle,conf);

            alg.execute();

            avgTime += alg.getDurationTime();
            cStateCount += alg.getClosedStatesCount();
            oStateCount += alg.getOpenSet().size();
            pathLength += alg.getBestSoFar().getMovesAlongPath().size();

            if(times){
                System.out.println(alg.getDurationTime());
            }

            if(debug){
                System.out.println(puzzle);
                System.out.println("Path:");
                System.out.println(alg.getBestSoFar().getMovesAlongPath());
                System.out.println(alg.getBestSoFar().getMovesAlongPath().size());
                System.out.println();
            }

            if(i == caseNumber - 1){
                System.out.println(alg.getBestSoFar().getMovesAlongPath());
                System.out.println(alg.getBestSoFar().getMovesAlongPath().size());
            }
        }

        System.out.println("Avg time in ms: " + (double)avgTime/(double)caseNumber);
        System.out.println("Avg closed: " + (double)cStateCount/(double)caseNumber);
        System.out.println("Avg open: " + (double)oStateCount/(double)caseNumber);
        System.out.println("Avg path length: " + (double)pathLength/(double)caseNumber);
    }

    public static void testCase_Manhattan(int moveNumber, int caseNumber, int option, int disp, boolean debug, boolean times){
        System.out.println("Manhattan:");

        long avgTime = 0;
        long cStateCount = 0;
        long oStateCount = 0;
        long pathLength = 0;


        GraphSearchConfigurator conf = new GraphSearchConfigurator();
        conf.setIdentifierType(IdentifierType.HASH_CODE);
        SlidingPuzzle.setHFunction(new ManhattanHeuristic());


        for(int i = 0; i < caseNumber; i++){
            SlidingPuzzle puzzle = new SlidingPuzzle();

            puzzle.randomize(moveNumber, i + disp, option);

            GraphSearchAlgorithm alg = new AStar(puzzle,conf);

            alg.execute();

            avgTime += alg.getDurationTime();
            cStateCount += alg.getClosedStatesCount();
            oStateCount += alg.getOpenSet().size();
            pathLength += alg.getBestSoFar().getMovesAlongPath().size();

            if(times){
                System.out.println(alg.getDurationTime());
            }

            if(debug){
                System.out.println(puzzle);
                System.out.println("Path:");
                System.out.println(alg.getBestSoFar().getMovesAlongPath());
                System.out.println(alg.getBestSoFar().getMovesAlongPath().size());
                System.out.println();
            }

            if(i == caseNumber - 1){
                System.out.println(alg.getBestSoFar().getMovesAlongPath());
                System.out.println(alg.getBestSoFar().getMovesAlongPath().size());
            }
        }



        System.out.println("Avg time in ms: " + (double)avgTime/(double)caseNumber);
        System.out.println("Avg closed: " + (double)cStateCount/(double)caseNumber);
        System.out.println("Avg open: " + (double)oStateCount/(double)caseNumber);
        System.out.println("Avg path length: " + (double)pathLength/(double)caseNumber);
    }

    public static void main(String[] args){
        int moveNumber = 1000;
        int caseNumber = 100;
        int option = 1;
        int disp = 2137;

        boolean debug = false;
        boolean times = false;

        SlidingPuzzle.testCase_MisplacedTiles(moveNumber, caseNumber, option, disp, debug, times);
        System.out.println();
        SlidingPuzzle.testCase_Manhattan(moveNumber, caseNumber, option, disp, debug, times);

    }
}