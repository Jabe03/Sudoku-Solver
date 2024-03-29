package Solver;

import SudokuGame.BoardCoord;

public class TileSolution {
    private final BoardCoord bc;
    private final byte val;

    public TileSolution(BoardCoord bc, byte val){
        this.bc = bc;
        this.val = val;

    }



    public BoardCoord getBc() {
        return bc;
    }

    public byte getVal() {
        return val;
    }



    public String toString(){
        return String.format("TSol(%d,%d)=%d", bc.row+1,bc.col+1, val);
    }
}

