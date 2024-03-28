package SudokuGame;

public class BoardCoord{
    public final byte row;
    public final byte col;
    public final BoardCoordType type;

    public BoardCoord(byte row, byte col, BoardCoordType type){
        this.row = row;
        this.col = col;
        this.type = type;
        checkBounds();
    }
    public BoardCoord(BoardCoord bc){
        this.row = bc.row;
        this.col = bc.col;
        this.type = bc.type;
    }

    public BoardCoord(byte row, byte col){
        this(row, col, BoardCoordType.CellCoord);
    }
    public BoardCoord(int row, int col, BoardCoordType t){

        this(Board.b(row), Board.b(col), t);
    }
    public BoardCoord(int row, int col){

        this(row, col, BoardCoordType.CellCoord);
    }



    private void checkBounds(){
        if(!coordinatesAreInBounds()){
            throw new IndexOutOfBoundsException("Coordinate was not within bounds of the board for: " + this);
        }
    }
    private boolean coordinatesAreInBounds(){
        return coordinatesAreInBounds(row, col, type);
    }
    private static boolean coordinatesAreInBounds(int row, int col, BoardCoordType type){
        int upper;
        int lower = 0;
        if(type == BoardCoordType.CellCoord){
            upper = 8;
        } else if(type == BoardCoordType.PartitionCoord){
            upper = 2;
        } else {
            //throw new WTFException?!?!?!??!?
            throw new IllegalArgumentException("The type of the board is completely wrong or null");
        }

        return lower <= row && row <= upper && lower <= col && col <= upper;
    }



    public BoardCoord getPartitionCoords(){
        return new BoardCoord((byte)(((int)row)/3), (byte)(((int)col)/3), BoardCoordType.PartitionCoord);
    }




    @Override
    public boolean equals(Object o){
        if(o instanceof BoardCoord b){
            return (this.type == b.type) && (this.row == b.row) && (this.col == b.col);
        }
        return false;
    }

    @Override
    public String toString(){
        return "{SudokuGame.BoardCoord: row=" + row + ", col=" + col + ", type=" + type + "}";
    }

}



