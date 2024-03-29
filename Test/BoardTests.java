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

    public static void getDefaultBoard(Board b){
        b.setBoardTo(GameBoard.fromBoard(DEFAULT_BOARD).getValuesCopy());
    }



}
