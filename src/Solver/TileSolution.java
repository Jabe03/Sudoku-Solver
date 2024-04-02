package Solver;

import SudokuGame.BoardCoord;

public class TileSolution {
    private final BoardCoord bc;
    private final byte val;
    private final String justification;

    public TileSolution(BoardCoord bc, byte val, String justification){
        this.bc = bc;
        this.val = val;
        this.justification = justification;
    }
    public TileSolution(BoardCoord bc, byte val){
        this(bc,val,null);
    }


    public BoardCoord getBc() {
        return bc;
    }

    public byte getVal() {
        return val;
    }



    public String toString(){
        if(justification == null)
            return String.format("TSol(%d,%d)=%d", bc.row+1,bc.col+1, val);
        return String.format("TS(%d,%d)=%d by %s", bc.row+1,bc.col+1, val, justification);
    }
}

