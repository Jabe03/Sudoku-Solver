import Solver.Solver;
import Solver.SolutionMethod;
import SudokuGame.Board;
import SudokuGame.BoardCoord;
import SudokuGame.GameBoard;
import SudokuGame.Tile;

import java.util.*;

public class UserInputTests {

    public static void main(String[] args){
//        Solver s = new Solver();
//        Tile t = new Tile(new BoardCoord(0,0), (byte) 1);
//        Random r = new Random(123);
//        for(int i = 0; i < 10; i++)
//            System.out.print(s.chooseRandomFromNotes(r, t));
//        System.out.println();
//        Random r2 = new Random(123);
//        for(int i = 0; i < 100; i++)
//            System.out.print((byte)(r2.nextInt(9) + 1));
        Board defaultBoard = BoardTests.getDefaultBoard();
        System.out.println(defaultBoard);
        System.out.println(defaultBoard.tileIsValid(new BoardCoord(0,0), (byte) 8));
        System.out.println(defaultBoard);
        setAndGetFromTerminalTest();
    }
    public static void setAndGetFromTerminalTest(){
        Board b = new Board();
        Scanner tsm = new Scanner(System.in);
        String index = "s i j v (set row col value), g i j (get row col) \n" +
                "q (quit) pb (print board), c i j (clear row col)\n" +
                "gn i j (getnotes row col), tn i j v (setnote row col val)\n" +
                "cv i j (checkValidity row col), cb (checkBoardValidity)\n" +
                "gb ?s (generateBoard seed(optional) gempt (get empty tiles)\n" +
                "san (set all notes)\n" +
                "db (defaultBoard) mb (mediumBoard) hb (hardBoard)\n" +
                "sol m (solve method[ gc (guesAndCheck)]";
        System.out.println(index);
        label:
        while(true){
            String command = tsm.nextLine();
            String[] terms = command.split(" ");

            switch (terms[0]) {
                case "gn": {
                    if(terms.length < 3){
                        break;
                    }
                    BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]), Integer.parseInt(terms[2]));
                    System.out.println(b.getTile(bc).getNotesList());
                    break;
                }
                case "tn":{
                    if(terms.length < 4){
                        break;
                    }
                    BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]), Integer.parseInt(terms[2]));
                    int value = Integer.parseInt(terms[3]);
                    b.getTile(bc).toggleNote((byte)value);
                    break;
                }
                case "san":{
                    b.turnAllTileNotesOn();

                }

                case "s": {
                    if(terms.length < 4){
                        break;
                    }
                    BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]), Integer.parseInt(terms[2]));
                    int value = Integer.parseInt(terms[3]);
                    b.setTile(bc, value);
                    break;
                }
                case "g": {
                    if(terms.length < 3){
                        break;
                    }
                    BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]), Integer.parseInt(terms[2]));
                    System.out.println(b.getTile(bc));
                    break;
                }
                case "q": {
                    System.exit(0);
                }
                case "pb": {
                    System.out.println(b);
                    break;
                }
                case "c": {
                    if(terms.length < 3){
                        break;
                    }
                    BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]), Integer.parseInt(terms[2]));
                    b.clearCell((bc));
                    break;
                }
                case "help": {
                    System.out.println(index);
                    break;
                }
                case "cv": {
                    if(terms.length < 3){
                        break;
                    }
                    BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]), Integer.parseInt(terms[2]));
                    System.out.println(b.tileIsValid(bc));
                    break;
                }
                case "cb": {
                    System.out.println(b.isValid());
                    break;
                }
                case "gb": {
                    if(terms.length == 1){
                        b = GameBoard.generate();
                    } else if (terms.length == 2){
                        b = GameBoard.generate(Long.parseLong(terms[1]));
                    } else if (terms.length == 3){
                        b = GameBoard.generate(Long.parseLong(terms[1]), Integer.parseInt(terms[2]));
                    }
                    break;
                }
                case "gempt": {
                    System.out.println(Arrays.toString(b.getEmptyTiles()));
                    break;
                }
                case "sol": {
                    if(terms.length < 2){
                        break;
                    } else if(terms.length == 2){
                        Solver s = new Solver();
                        boolean solved = false;
                        SolutionMethod sm;
                        switch(terms[1]){
                            case "gc" -> sm = SolutionMethod.GUESS_AND_CHECK;
                            default -> sm = SolutionMethod.GUESS_AND_CHECK;
                        }
                        long start = System.currentTimeMillis();
                        solved = s.solve(b, sm,1234);
                        long elapsed = System.currentTimeMillis() - start;
                        System.out.printf("%s with solution: %s (%dms)%n",
                                (solved? "Solved successfully" : "Solution failed"),  s.getSolution(), elapsed);

                    }
                    break;
                }
                case "db":{
                    b = BoardTests.getDefaultBoard();
                    break;
                }
                case "mb":{
                    b = BoardTests.getMediumBoard();
                    break;
                }
                case "hb":{
                    b = BoardTests.getHardBoard();
                    break;
                }

            }
        }
    }
}
