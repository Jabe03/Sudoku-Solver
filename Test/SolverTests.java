import Solver.Solver;
import SudokuGame.BoardCoord;
import SudokuGame.GameBoard;
import SudokuGame.Tile;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class SolverTests {


    @Test
    public void GuessAndCheckSolver(){


        GameBoard gb = BoardTests.getDefaultBoard();
        assertTrue(gb.isValid());

    }

}
