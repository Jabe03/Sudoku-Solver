package Solver;

import SudokuGame.Board;
import SudokuGame.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver{

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
        Solution s = new Solution();
        Random r = new Random(seed);
        while(!b.isSolved()){
            s.addDecisionLevel();
            Tile t = getRandomEmptyTile(r, b);
            if (t == null){
                return false;
            }
            byte guess = chooseRandomFromNotes(r,t);
            if(b.tileIsValid(t.getCoordinates(), guess)){
                b.setTile(t.getCoordinates(), guess);
                s.addToLastDecisionLevel(t.getCoordinates(), guess);
            }
        }
        return true;
    }

    public Tile getRandomEmptyTile(Random r, Board b){
        Tile[] tiles = b.getEmptyTiles();
        if(tiles.length == 0){
            System.out.println("Expected to find an empty tile, but didn't, probably a bad solution");
            return null;
        }
        return tiles[r.nextInt(tiles.length)];
    }

    public byte chooseRandomFromNotes(Random r, Tile t){
        List<Integer> notes = t.getNotesList();
        if(notes.size() == 0){
            System.out.println("size of notes is zero, generating from 9");
            return (byte) (r.nextInt(9) + 1);
        }
        System.out.println("notes are " + notes);
        return  (byte) (notes.get(r.nextInt(notes.size())) + 1);

    }


}