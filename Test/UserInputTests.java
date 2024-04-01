import Display.BoardView;
import Solver.Solver;
import Solver.SolutionMethod;
import SudokuGame.Board;
import SudokuGame.BoardCoord;
import SudokuGame.GameBoard;
import SudokuGame.Tile;

import java.util.*;

public class UserInputTests {
    //-8332444702885254801 giving huge problems review this
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
        setAndGetFromTerminalTest();
    }
    public static void setAndGetFromTerminalTest(){
        Board b = new Board();
        Scanner tsm = new Scanner(System.in);

        BoardView view = new BoardView(b);
        startUpdatingWindow(view);
        Solver s = new Solver();
        String index = """
                s i j v (set row col value), g i j (get row col)\s
                q (quit) pb (print board), c i j (clear row col)
                gn i j (getnotes row col), tn i j v (setnote row col val)
                cv i j (checkValidity row col), chb (checkBoardValidity)
                gb ?s (generateBoard seed(optional) gempt (get empty tiles)
                gan  (generate all possible notes)
                db (defaultBoard) mb hb clb
                sol m (solve method) ss (set solver seed)
                methods: [ gc (guesAndCheck) gcs (smart guess and check(picks tile with least notes to guess about))]
                tw (toggle window)
                ttgen n (timeTrialsSolver numBoards)
                """;
        System.out.println(index);
        while(true){
            String command = tsm.nextLine();
            String[] terms = command.split(" ");
            try {
                switch (terms[0]) {
                    case "gan", "san" -> {
                        b.turnAllTileNotesOn();
                    }
                    case "gn" -> {
                        if (terms.length < 3) {
                            break;
                        }
                        BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]) - 1, Integer.parseInt(terms[2]) - 1);
                        System.out.println(b.getTile(bc).getNotesList());
                    }
                    case "tn" -> {
                        if (terms.length < 4) {
                            break;
                        }
                        BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]) - 1, Integer.parseInt(terms[2]) - 1);
                        int value = Integer.parseInt(terms[3]);
                        b.getTile(bc).toggleNote((byte) (value + 1));
                    }

                    case "s" -> {
                        if (terms.length < 4) {

                            break;
                        }
                        BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]) - 1, Integer.parseInt(terms[2]) - 1);
                        int value = Integer.parseInt(terms[3]);
                        b.setTile(bc, value);
                    }
                    case "g" -> {
                        if (terms.length < 3) {
                            break;
                        }
                        BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]) - 1, Integer.parseInt(terms[2]) - 1);
                        System.out.println(b.getTile(bc));
                    }

                    case "q" -> {
                        System.exit(0);
                    }
                    case "pb" -> {
                        System.out.println(b);
                    }
                    case "c" -> {
                        if (terms.length < 3) {
                            break;
                        }
                        BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]) - 1, Integer.parseInt(terms[2]) - 1);
                        b.clearCell((bc));
                    }
                    case "help" -> {
                        System.out.println(index);
                    }
                    case "cv" -> {
                        if (terms.length < 3) {
                            break;
                        }
                        BoardCoord bc = new BoardCoord(Integer.parseInt(terms[1]) - 1, Integer.parseInt(terms[2]) - 1);
                        System.out.println(b.tileIsValid(bc));
                    }
                    case "chb" -> {
                        System.out.println(b.isValid());
                    }
                    case "gb" -> {
                        if (terms.length == 1) {
                            b = GameBoard.generate();
                        } else if (terms.length == 2) {
                            b = GameBoard.generate(Long.parseLong(terms[1]));
                        } else if (terms.length == 3) {
                            b = GameBoard.generate(Long.parseLong(terms[1]), Integer.parseInt(terms[2]));
                        }
                    }
                    case "gempt" -> {
                        System.out.println(Arrays.toString(b.getEmptyTiles()));
                    }
                    case "ss" ->{
                        if (terms.length < 2) {
                            break;
                        }
                        s.setSeed(Long.parseLong(terms[1]));
                    }
                    case "sol" -> {

                        boolean solved;
                        if (terms.length == 1 || terms.length > 3) {
                            break;
                        }
                        if (terms.length == 3) {
                            s.setDelay(Long.parseLong(terms[2]));
                        }
                        SolutionMethod sm;
                        switch (terms[1]) {
                            case "gc" -> sm = SolutionMethod.GUESS_AND_CHECK;
                            case "gcs" -> sm = SolutionMethod.GUESS_AND_CHECK_SMART_SELECTION;
                            default -> sm = SolutionMethod.GUESS_AND_CHECK;
                        }
                        view.setSolver(s);
                        long start = System.currentTimeMillis();
                        solved = s.solve(b, sm);
                        long elapsed = System.currentTimeMillis() - start;
                        System.out.printf("%s with solution: %s (%dms)%n",
                                (solved ? "Solved successfully" : "Solution failed"), s.getSolution(), elapsed);

                    }


                    case "mb" -> {
                        BoardTests.getMediumBoard(b);
                    }
                    case "hb" -> {
                        BoardTests.getHardBoard(b);
                    }
                    case "db" -> {
                        BoardTests.getDefaultBoard(b);
                    }
                    case "cb" -> {
                        BoardTests.getClearBoard(b);
                    }
                    case "tw" -> {
                        view.toggleVisible();

                    }
                    case "ttgen" -> {
                        if (terms.length < 2) {
                            break;
                        }
                        runTimeTrialGeneratingBoards(Integer.parseInt(terms[1]), b, s);
                    }
                }
            } catch (NumberFormatException e){
                System.out.println("Command was wrong, try again");
            }
        }


    }

    public static void runTimeTrialGeneratingBoards(int numBoards, Board b, Solver s){
        Random seedGenerator = new Random();
        long slowestTime = 0;
        long slowestSeed = 0;
        long cumElapsed = 0;
        for(int i  = 0; i < numBoards; i++){
            long seed = seedGenerator.nextLong();
            s.setSeed(seed);
            BoardTests.getClearBoard(b);
            long now = System.currentTimeMillis();
            System.out.println("Solving board#" + i);
            s.solve(b,SolutionMethod.GUESS_AND_CHECK_SMART_SELECTION);
            long elapsed = System.currentTimeMillis() - now;
            if(elapsed > slowestTime){
                slowestTime = elapsed;
                slowestSeed = seed;
            }
            cumElapsed += elapsed;
        }

        System.out.printf("Longest generation time: %dms, seed: %d.(Avg. %fms)%n", slowestTime, slowestSeed, cumElapsed/(double) numBoards);
    }
    public static void startUpdatingWindow(BoardView b) {
        Thread t = new Thread(() -> {
            int updatesPerSecond = 30;
            double interval = 1000.0 / updatesPerSecond;
            long now = System.currentTimeMillis();
            while (true) {
                if (System.currentTimeMillis() - now >= interval) {
                    now += interval;
                    b.repaint();
                }
            }
        });
        t.start();
    }
}
