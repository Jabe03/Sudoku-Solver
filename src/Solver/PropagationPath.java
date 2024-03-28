package Solver;

import SudokuGame.BoardCoord;
import SudokuGame.Tile;

import java.util.ArrayList;

class PropagationPath {

    private Tile[][] startingBoard;
    private ArrayList<TileSolution> path;
    private int decisionLevel;

    public PropagationPath(Tile[][] startingBoard, int decisionLevel){

        //write this now
        this.decisionLevel = decisionLevel;
        this.path = new ArrayList<TileSolution>();

    }

    public void add(BoardCoord bc, byte val){
        path.add(new TileSolution(bc, val, decisionLevel));
    }
    public int getDecisionLevel() {
        return decisionLevel;
    }

    public void setDecisionLevel(int decisionLevel) {
        this.decisionLevel = decisionLevel;
    }


}
