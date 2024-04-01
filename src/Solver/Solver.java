package Solver;

import SudokuGame.Board;
import SudokuGame.BoardCoord;
import SudokuGame.BoardCoordType;
import SudokuGame.Tile;

import java.util.*;

public class Solver{
    Solution s;
    long delay;
    public static final int SLOW_DELAY = 200;
    volatile boolean paused;
    long seed;
    public static void main(String[] args){
        Board b = new Board();
        System.out.println(b);
        b.HORIZONTAL_SPACING = 3;
        b.VERTICAL_SPACING = 1;
        System.out.println(b);

        for(int i = 0; i <2 ;i++) {
            Random r = new Random();
            r.setSeed(12);

            List<Integer> vals = new ArrayList<>();

            for(int j = 0; j < 10; j++){
                vals.add(r.nextInt());
            }
            System.out.println(vals);

        }

    }

    public void setDelay(long millis){
        this.delay = millis;
    }
    public void setSlow(){
        this.delay = SLOW_DELAY;
    }

    public void setSeed(long seed){
        this.seed = seed;
    }

    public boolean solve(Board b, SolutionMethod method){
        boolean solved;
        if(this.seed ==  0){
            seed = -8332444702885254801L; //new Random().nextLong();
        }
        switch(method){
            case GUESS_AND_CHECK -> solved = solveByGuessAndCheck(b, seed, false, true);
            case GUESS_AND_CHECK_SMART_SELECTION -> solved = solveByGuessAndCheck(b, seed, true,  true);
            default -> {
                System.out.println("Desired method not supported");
                return false;
            }
        }

        return solved;
    }

    private boolean revertBoardAndFail(Board b){
        b.setBoardTo(s.getInitialBoard());
        return false;
    }
    public boolean solveByGuessAndCheck(Board b, long seed, boolean smartSelection,  boolean lookForContradictionTiles){
        System.out.println("Solving via guess and check (seed=" + seed + ")");
        s = new Solution(b);
        Random r = new Random(seed);
        b.turnAllTileNotesOn();
        solveAllWithSingletonNotes(b);
        while(!b.isSolved()) {
            if(paused){
                continue;
            }
            Tile t;
            if(smartSelection){
                t = getATileWithLeastNotes(r,b);
            }else{
                t = getRandomEmptyTile(r, b);
            }

            if (t == null) {
                return revertBoardAndFail(b);
            }


            byte guess = chooseRandomFromNotes(r, t);
            if (guess == 0 || hasTileWithNoNotes(b) || !partitionsHaveEveryNumberPossible(b)) {
                TileSolution ts = s.getLastDecision();
                if(ts == null){
                   return revertBoardAndFail(b);
                }
                b.setBoardTo(s.revertHighestDecisionLevel());
                b.getTile(ts.getBc()).toggleNote(ts.getVal());
                //togglePause();
            } else {
                if (b.tileIsValid(t.getCoordinates(), guess)) {
                    //System.out.println("Guessing " +  guess + " at " + t.getCoordinates());
                    s.addDecisionLevel(b, new TileSolution(t.getCoordinates(), guess));

                } else {
                    //System.out.print(t.getCoordinates() +  " " + guess + "was invalid, marking wrong " +
                            //b.getTile(t.getCoordinates()).getNotesList() + "->");
                    b.getTile(t.getCoordinates()).toggleNote(guess);
                    delay();
                    //System.out.println(b.getTile(t.getCoordinates()).getNotesList());
                }
            }


            deducePropagations(b);
            delay();
        }
        return true;
    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }
    public boolean isPaused(){
        return paused;
    }
    public void togglePause(){
        setPaused(!paused);
    }
    public boolean hasTileWithNoNotes(Board b){
        for(int i = 0 ; i < 9; i++){
            for(int j = 0; j <  9; j++){
                Tile t = b.getTile(new BoardCoord(i,j));
                if(!t.hasValue() &&  t.getNotesList().isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean partitionsHaveEveryNumberPossible(Board b){

        for(int i = 0; i < 3; i++){
            for( int j = 0; j < 3; j++){
                BoardCoord bc = new BoardCoord(i,j, BoardCoordType.PartitionCoord);
                Tile[] partition = b.getPartitionOf(bc);
                Set<Integer> valsThatExist =  new HashSet<>();

                for(Tile t: partition){
                    if(t.hasValue()){
                        valsThatExist.add((int) t.getValue());
                    } else{
                        valsThatExist.addAll(t.getNotesList());
                    }
                }
                if(valsThatExist.size() != 9){
                    return false;
                }
            }
        }
        return true;
    }
    public void deducePropagations(Board b){
        while(solveAllWithSingletonNotes(b) || solveAllLastPossibleSquare(b));
    }
    public boolean solveAllLastPossibleSquare(Board b){
        boolean changesMade = false;
        for(int i = 0; i < 9; i++){
            changesMade = changesMade ||
                    solveLastPossibleInCollection(b, Arrays.asList(b.getColumnOf(new BoardCoord(0,i)))) ||
                    solveLastPossibleInCollection(b, Arrays.asList(b.getRowOf(new BoardCoord(i,0)))) ||
                    solveLastPossibleInCollection(b, Arrays.asList(b.getRowOf(new BoardCoord(i/3,i%3, BoardCoordType.PartitionCoord))));
        }
        return changesMade;
    }
    public boolean solveLastPossibleInCollection(Board b, Iterable<Tile> tiles){
        boolean changesMade = false;
        Map<Integer, Pair<Integer, Tile>> counts = new HashMap<>();
        for(int i = 1; i <=9 ; i++){
            counts.put(i, new Pair<Integer, Tile>(0, null));
        }

        for(Tile t: tiles){
            if(!t.hasValue()){
                for(Integer i: t.getNotesList()){
                    counts.put(i, new Pair<>(counts.get(i).first + 1, t));
                }
            }
        }
        for(Map.Entry<Integer, Pair<Integer, Tile>> entry : counts.entrySet()){
            if(entry.getValue().first == 1){
                b.setTile(new TileSolution(entry.getValue().second.getCoordinates(), entry.getKey().byteValue()));
                changesMade = true;
            }
        }
        return changesMade;
    }


    public boolean solveAllWithSingletonNotes(Board b){
        boolean changesMade = false;
        for(int i  = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                BoardCoord bc = new BoardCoord(i,j);
                List<Integer> notes = b.getTile(bc).getNotesList();
                if(notes.size() == 1){
                    TileSolution ts = new TileSolution(bc, notes.get(0).byteValue());
                    s.addToLastDecisionLevel(ts);
                    b.setTile(ts);
                    changesMade = true;
                    delay(delay/2);
                }
            }
        }
        return changesMade;
    }
    public void delay(){
        if(this.delay == 0){
            return;
        }
       delay(delay);
    }

    public void delay(long millis){
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e){
            //do nothing
        }
    }

    public Tile getRandomEmptyTile(Random r, Board b){
        Tile[] tiles = b.getEmptyTiles();
        if(tiles.length == 0){
            //System.out.println("Expected to find an empty tile, but didn't, probably a bad solution");
            return null;
        }
        return tiles[r.nextInt(tiles.length)];
    }

    public byte chooseRandomFromNotes(Random r, Tile t){
        List<Integer> notes = t.getNotesList();
        if(notes.size() == 0){
            return 0;
        }
        //System.out.println("notes are " + notes);
        byte guess = (notes.get(r.nextInt(notes.size())).byteValue());
        //System.out.println("Guessing "  + guess);
        //return guess;
        return notes.get(0).byteValue();

    }

    public Tile getATileWithLeastNotes(Random r, Board b){
        ArrayList<Tile> smallest = new ArrayList<>();
        for(int i  = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                BoardCoord bc = new BoardCoord(i,j);
                Tile t = b.getTile(bc);
                if(t.hasValue()){
                    continue;
                }
                if(smallest.size() == 0){
                    smallest.add(t);
                } else{
                    byte smallestVal = smallest.get(0).getValue();
                    byte currentVal = t.getValue();
                    if(smallestVal > currentVal){
                        smallest.clear();
                        smallest.add(t);
                    } else if(smallestVal == currentVal){
                        smallest.add(t);
                    }
                }
            }
        }
        if(smallest.size() == 0){
            return null;
        }
        return smallest.get(r.nextInt(smallest.size()));

    }

    public Solution getSolution(){
        if(s == null){
            return null;
        }
        return s;
    }

    public Tile[][] getBoardFromDecisionLevel(int level){
        return s.solution.get(level).getStartingBoard();
    }


}

class Pair<F,S>{
    F first;
    S second;
    public Pair(F first, S second){
        this.first =  first;
        this.second = second;
    }
}