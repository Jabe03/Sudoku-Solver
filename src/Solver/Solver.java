package Solver;

import SudokuGame.Board;
import SudokuGame.BoardCoord;
import SudokuGame.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver{
    Solution s;
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


    public boolean solve(Board b, SolutionMethod method, long seed){
        boolean solved;
        switch(method){
            case GUESS_AND_CHECK -> solved = solveByGuessAndCheck(b, seed);
            default -> {
                System.out.println("Desired method not supported");
                return false;
            }
        }

        return solved;
    }

    public boolean solveByGuessAndCheck(Board b, long seed){
        System.out.println("Solving via guess and check");
        s = new Solution(b);
        Random r = new Random(seed);
        b.turnAllTileNotesOn();
        solveAllWithSingletonNotes(b);
        while(!b.isSolved()) {

            Tile t = getRandomEmptyTile(r, b);
            //Tile t = getATileWithLeastNotes(r,b);
            if (t == null) {
                return false;
            }

            byte guess = chooseRandomFromNotes(r, t);
            if (guess == 0) {
                TileSolution ts = s.getLastDecision();
                b.setBoardTo(s.revertHighestDecisionLevel());
                b.getTile(ts.getBc()).toggleNote(ts.getVal());
            } else {
                if (b.tileIsValid(t.getCoordinates(), guess)) {
                    //System.out.println("Guessing " +  guess + " at " + t.getCoordinates());
                    s.addDecisionLevel(b, new TileSolution(t.getCoordinates(), guess));

                } else {
                    //System.out.print(t.getCoordinates() +  " " + guess + "was invalid, marking wrong " +
                            //b.getTile(t.getCoordinates()).getNotesList() + "->");
                    b.getTile(t.getCoordinates()).toggleNote(guess);
                    //System.out.println(b.getTile(t.getCoordinates()).getNotesList());
                }
            }
            solveAllWithSingletonNotes(b);
        }
        return true;
    }

    public void solveAllWithSingletonNotes(Board b){
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
                }
            }
        }
        if(changesMade){
            solveAllWithSingletonNotes(b);
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
        //return smallest.get(r.nextInt(smallest.size()));
        return smallest.get(0);
    }

    public Solution getSolution(){
        if(s == null){
            return null;
        }
        return s;
    }


}