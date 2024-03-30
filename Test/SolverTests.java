import Solver.Solver;
import SudokuGame.Board;
import SudokuGame.BoardCoord;
import SudokuGame.GameBoard;
import SudokuGame.Tile;
import jdk.dynalink.linker.GuardingDynamicLinkerExporter;
import org.junit.Test;
import Solver.Solution;
import Solver.TileSolution;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class SolverTests {

    public static  void main(String[] args){
        GuessAndCheckSolver();
    }

    public static void GuessAndCheckSolver(){


        Board b = new Board();
        BoardTests.getDefaultBoard(b);
        Solution s  = new Solution(b);
        TileSolution ts  = new TileSolution(new BoardCoord(1,1), (byte) 3);
        s.addDecisionLevel(b,ts);
        ts  = new TileSolution(new BoardCoord(3,3), (byte) 3);
        s.addDecisionLevel(b,ts);
        ts  = new TileSolution(new BoardCoord(6,6), (byte) 3);
        b.setTile(ts);
        s.addDecisionLevel(b,ts);
        ts  = new TileSolution(new BoardCoord(0,6), (byte) 3);
        b.setTile(ts);
        s.addDecisionLevel(b,ts);
        System.out.println(s);
        GameBoard gb = GameBoard.generateEmpty();
        BoardTests.getDefaultBoard(gb);
        assertTrue(gb.isValid());

    }

}
