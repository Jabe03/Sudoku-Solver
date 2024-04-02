package Solver;

import SudokuGame.BoardCoord;
import SudokuGame.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;

public class PropagationPath {

    private final Tile[][] startingBoard;

    private TileSolution initialDecision;
    private ArrayList<TileSolution> path;
    private int decisionLevel;


    public PropagationPath(Tile[][] startingBoard, int decisionLevel, TileSolution initialDecision){
        this.startingBoard = startingBoard;

        this.decisionLevel = decisionLevel;
        this.path = new ArrayList<TileSolution>();
        this.initialDecision = initialDecision;
    }
    public PropagationPath(Tile[][] startingBoard,int decisionLevel){
        this(startingBoard, decisionLevel, null);
    }

    public ArrayList<TileSolution> getPath(){
        return path;
    }

    public TileSolution getInitialDecision() {
        return initialDecision;
    }

    public Tile[][] getStartingBoard(){
        return startingBoard;
    }
    public void add(BoardCoord bc, byte val){
        add(new TileSolution(bc, val));
    }
    public void add(TileSolution s){
        path.add(s);
    }
    public int getDecisionLevel() {
        return decisionLevel;
    }

    public void setDecisionLevel(int decisionLevel) {
        this.decisionLevel = decisionLevel;
    }

    public String toString(){

        try {
            return String.format("<DL:%d, inital = [%s], path = %s>", decisionLevel, initialDecision, path);
        } catch (ConcurrentModificationException e){
            return "";
        }
    }


}
