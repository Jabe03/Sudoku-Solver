import SudokuGame.Board;
import SudokuGame.GameBoard;
import org.junit.Test;

import java.util.GregorianCalendar;

public class BoardTests {
    public static final int[][] DEFAULT_BOARD = new int[][]{
            {0,0,0,0,0,7,0,4,0},
            {0,8,2,6,0,0,5,0,0},
            {7,3,0,5,0,1,0,0,0},
            {0,0,0,0,3,8,0,1,0},
            {1,9,8,0,6,5,3,0,7},
            {6,7,0,0,2,9,4,8,5},
            {0,1,7,0,0,0,0,6,4},
            {2,0,0,8,0,0,0,0,0},
            {0,0,0,0,0,6,7,0,2}
    };


    public static final int[][] HARD_BOARD = new int[][]{
            {6,0,0,5,2,0,1,0,7},
            {0,8,0,9,0,0,0,0,0},
            {0,0,0,0,0,0,0,5,0},
            {1,0,0,0,7,0,2,0,4},
            {0,0,6,0,0,1,0,0,0},
            {0,0,0,0,0,0,3,0,0},
            {4,0,0,0,0,0,0,3,0},
            {0,0,9,2,0,0,4,0,5},
            {0,0,0,0,5,0,0,6,0}
    };

    public static final int[][] MEDIUM_BOARD = new int[][]{
            {0,8,5,7,2,0,1,0,0},
            {7,0,0,4,0,5,0,0,6},
            {0,0,0,0,0,3,7,0,0},
            {4,5,9,1,6,7,0,0,0},
            {0,6,0,0,0,0,0,1,7},
            {0,7,0,0,0,0,0,0,0},
            {6,0,8,0,0,0,5,0,0},
            {0,0,0,0,5,9,8,0,4},
            {5,9,0,3,0,8,0,0,0},

    };

    public static void getDefaultBoard(Board b) {
        getBoardFrom(DEFAULT_BOARD,b);
    }
    public static void getBoardFrom(int[][] template, Board b){
        b.setBoardTo(GameBoard.fromBoard(template).getValuesCopy());
    }
    public static void getHardBoard(Board b){
        getBoardFrom(HARD_BOARD,b);
    }
    public static void getMediumBoard(Board b){
        getBoardFrom(MEDIUM_BOARD,b);
    }


}
