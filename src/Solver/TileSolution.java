package Solver;

import SudokuGame.BoardCoord;

public class TileSolution {
    private final BoardCoord bc;
    private final byte val;
    private int decisionLevel;

    public TileSolution(BoardCoord bc, byte val, int decisionLevel){
        this.bc = bc;
        this.val = val;
        this.decisionLevel = decisionLevel;
    }



    public BoardCoord getBc() {
        return bc;
    }

    public byte getVal() {
        return val;
    }

    public int getDecisionLevel() {
        return decisionLevel;
    }

    public void setDecisionLevel(int decisionLevel) {
        this.decisionLevel = decisionLevel;
    }
}

