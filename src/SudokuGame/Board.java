package SudokuGame;

import Solver.TileSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A Sudoku board is made up of a 9x9 grid of values 1 through 9.
 * The board is partitioned into a 3x3 grid of blocks, each 3x3 values in size.
 *
 */

public class Board {
    //public static final SudokuGame.BoardColor[] colorOrder = SudokuGame.BoardColor.getAlternatingOrder(SudokuGame.BoardColor.CYAN, SudokuGame.BoardColor.PINK);
    public static final BoardColor[] colorOrder = BoardColor.getUniformColor(BoardColor.CYAN);

    public static final BoardColor intrinsicTileColor = BoardColor.INDIGO;
    public static final BoardColor blankTileColor = BoardColor.DARK_GRAY;
    public static final BoardColor invalidTileColor = BoardColor.RED;
    public static final int BOARD_WIDTH = 9;
    public static final int BOARD_HEIGHT = 9;
    public int HORIZONTAL_SPACING = 1;
    public int VERTICAL_SPACING = 0;
    private Tile[][] values;

    public Board(int[][] values){
        byte[][] newVals = new byte[9][9];
        for(int i = 0; i < values.length; i++){
            for(int j = 0; j < values[i].length; j++){
                newVals[i][j] = b(values[i][j]);
            }
        }
        this.values = new Tile[9][9];
        initializeValuesWithArray(newVals);
    }
    public Board(byte[][] values){
        this.values = new Tile[9][9];
        initializeValuesWithArray(values);
    }
    public Board(){
        this(new byte[9][9]);
    }
    private void initializeValuesWithArray(byte[][] initials){
        for(int i = 0; i < values.length; i++){
            for(int j = 0; j < values[i].length; j++){
                values[i][j] = new Tile(new BoardCoord(i,j), initials[i][j]);
            }
        }
    }

    public void setTile(BoardCoord bc, int newVal){
            values[bc.row][bc.col].setValue(b(newVal));
            updateNotesRelevantTo(bc);
    }

    public void setTile(TileSolution s){
        setTile(s.getBc(), s.getVal());
    }

    public void clearCell(BoardCoord bc){
        values[bc.row][bc.col].clear();
    }
    public Tile getTile(BoardCoord bc){
        return values[bc.row][bc.col];
    }
    public Tile[] getEmptyTiles(){
        ArrayList<Tile> tiles = new ArrayList<>();
        for(Tile[] row: values){
            for(Tile t: row){
                if(!t.hasValue()){
                    tiles.add(t);
                }
            }
        }
        Tile[] result = new Tile[tiles.size()];
        return tiles.toArray(result);
    }
    public void makeAllCurrentValuesIntrinsic(){
        for(int i  = 0; i < values.length; i++){
            for(int j = 0; j < values[i].length; j++){
                Tile t = values[i][j];
                if(t.hasValue()){
                    t.makeIntrinsic();
                }
            }
        }
    }
    public Tile[][] getValuesCopy(){
        Tile[][] newTiles = new Tile[values.length][values[0].length];
        for(int i = 0; i < values.length; i++){
            for( int j = 0; j < values[i].length; j++){
                newTiles[i][j] = new Tile(values[i][j]);
            }
        }
        return newTiles;
    }

    public void setBoardTo(Tile[][] newBoard){
        this.values = newBoard;
    }



    static byte b(int a){
        byte b = (byte) a;
        if(b == a){
            return b;
        }
        throw new IndexOutOfBoundsException();
    }
    public boolean isSolved(){
        return isValid() && (getEmptyTiles().length == 0);
    }
    public boolean isValid(BoardCoord hypotheticalbc, byte hypotheticalVal){
        Tile temp = getTile(hypotheticalbc);
        values[hypotheticalbc.row][hypotheticalbc.col] = new Tile(hypotheticalbc, hypotheticalVal);
        boolean result = isValid();
        values[hypotheticalbc.row][hypotheticalbc.col] = temp;
        return result;
    }

    public boolean isValid(){

        return !(rowsHasError() || colsHasError() || partionsHasError());
//        for(int i = 0; i < values.length; i++){
//            for(int j = 0; j < values[i].length ; j++){
//                if(!tileIsValid(new SudokuGame.BoardCoord(i,j))){
//                    return false;
//                }
//            }
//        }
//        return true;
    }

    private boolean partionsHasError() {
        for(int i  = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(hasDuplicateIn(Arrays.asList(getPartitionOf(new BoardCoord(i,j,BoardCoordType.PartitionCoord))))){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean colsHasError() {
        for(int i  = 0; i < values.length; i++) {
            if (hasDuplicateIn(Arrays.asList(getColumnOf(new BoardCoord(0,i))))) {
                return true;
            }
        }
        return false;
    }

    private boolean rowsHasError(){
        for(int i  = 0; i < values.length; i++){
            if(hasDuplicateIn(Arrays.asList(values[i]))){
                return true;
            }
        }
        return false;
    }

    private boolean hasDuplicateIn(Iterable<Tile> collection){
        boolean[] numbers = new boolean[9];
        for(Tile t: collection){
            if(t.hasValue()){
                if(numbers[t.getValue()-1]){
                    return true;
                } else{
                    numbers[t.getValue()-1] = true;
                }
            }
        }
        return false;
    }

    public boolean tileIsValid(BoardCoord bc, byte hypothetical){
        Tile t = getTile(bc);
        byte savedVal = t.getValue();
        t.manualValueChange(hypothetical);
        boolean result = tileIsValid(bc);
        t.manualValueChange(savedVal);
        //System.out.println("Tiles are same: " + (t == values[bc.row][bc.col]));
        //System.out.println("TileVal after checking: " + t.getValue());
        return result;
    }

    public void turnAllTileNotesOn(){
        for(int i  = 0; i < values.length; i++){
            for(int j = 0; j < values[i].length; j++){
                Tile t = values[i][j];
                if(!t.hasValue()){
                    t.turnOnAllNotes();
                }
            }
        }
        correctAllNotes();
    }

    public void correctAllNotes(){
        for(int i  = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                if(values[i][j].hasValue()){
                    updateNotesRelevantTo(new BoardCoord(i,j));
                }
            }
        }

    }

    public void updateNotesRelevantTo(BoardCoord bc){
        byte val = getTile(bc).getValue();
        for(Tile t : getAllRelevantTiles(bc)){
            t.removeNote((byte)val);
        }
    }

    public boolean tileIsValid(BoardCoord bc){
        Tile t = getTile(bc);
        if(t.hasValue()){

            return (!hasColumnConflict(bc)) && (!hasRowConflict(bc)) &&(!hasPartitionConflict(bc));
        }
        return true; //if tile is blank then no possible conflicts
    }

    public int countOccurrencesOf(byte val, Tile[] collection){

        int count = 0;
        for (Tile tile : collection) {
            if (tile.getValue() == val) {
                count++;
            }
        }
        return count;
    }
    public boolean hasRowConflict(BoardCoord bc){
        Tile[] boardRow = values[bc.row];
        int count = countOccurrencesOf(getTile(bc).getValue(), boardRow);
        if(count == 0){
            throw new RuntimeException("Number was not counted in its own row, fix this.");
        }
        return count > 1;
    }

    public boolean hasColumnConflict(BoardCoord bc){
        Tile[] col = getColumnOf(bc);

        int count = countOccurrencesOf(getTile(bc).getValue(),col);
        if(count == 0){
            throw new RuntimeException("Number was not counted in its own column, fix this.");
        }
        return count > 1;
    }

    public boolean hasPartitionConflict(BoardCoord bc){
        int count = countOccurrencesOf(getTile(bc).getValue(), getPartitionOf(bc));
        if(count == 0){
            throw new RuntimeException("Number was not counted in its own partition, fix this.");
        }
        return count > 1;
    }
    public Iterable<Tile> getAllRelevantTiles(BoardCoord bc){

        Set<Tile> tiles = new HashSet<Tile>();
        tiles.addAll(Arrays.asList(getColumnOf(bc)));
        tiles.addAll(Arrays.asList(getRowOf(bc)));
        tiles.addAll(Arrays.asList(getPartitionOf(bc)));

        return tiles;

    }

    public Tile[] getRowOf(BoardCoord bc){
        return values[bc.row];
    }
    public Tile[] getColumnOf(BoardCoord bc){
        Tile[] col = new Tile[9];
        for(int row = 0; row < values.length; row++){
            col[row] = values[row][bc.col];
        }
        return col;
    }

    public Tile[] getPartitionOf(BoardCoord bc){
        Tile[] partition = new Tile[9];
        if(bc.type == BoardCoordType.CellCoord)
            bc = bc.getPartitionCoords();
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 3; col++){
                partition[row*3 + col] = values[bc.row*3 + row][bc.col*3 + col];
            }
        }
        return partition;
    }


    public String toString(){
        StringBuilder b = new StringBuilder();
        for(int partitionRow = 0; partitionRow < BOARD_HEIGHT/3; partitionRow++){
            for(int partitionSubRow = 0; partitionSubRow<BOARD_HEIGHT/3; partitionSubRow++){
                for(int partitionCol = 0; partitionCol < BOARD_WIDTH/3 ; partitionCol++){
                    for(int partitionSubCol = 0; partitionSubCol < BOARD_WIDTH/3 ; partitionSubCol++){
                        BoardCoord bc = new BoardCoord(3*partitionRow + partitionSubRow, 3*partitionCol + partitionSubCol);
                        BoardCoord partitionCoord = bc.getPartitionCoords();
                        int colorNum = partitionCoord.row * 3 + partitionCoord.col;
                        Tile t = this.getTile(bc);
                        BoardColor c = t.isIntrinsic() ? intrinsicTileColor :  (t.hasValue() ?  colorOrder[colorNum] : blankTileColor);
                        b.append(BoardColor.inColor(Byte.toString(t.getValue()), c));
                        if(partitionSubCol < BOARD_WIDTH/3-1 || partitionCol < BOARD_WIDTH/3 -1) {
                            b.append(" ".repeat(HORIZONTAL_SPACING));
                        }
                    }
                    if( partitionCol < BOARD_WIDTH/3 -1 )
                        b.append("|").append(" ".repeat(HORIZONTAL_SPACING));
                }
                b.append("\n".repeat(VERTICAL_SPACING + 1));
            }
            if(partitionRow < BOARD_HEIGHT/3 - 1){
                int numrepeats = (BOARD_WIDTH + (BOARD_WIDTH / 3) - 1)*(HORIZONTAL_SPACING+1) - HORIZONTAL_SPACING;
                b.append("-".repeat(numrepeats)).append("\n".repeat(VERTICAL_SPACING + 1));
            }

        }
        return b.toString();
    }

    public String toShortString(){
        StringBuilder b = new StringBuilder();

        for(Tile[] row: values){
            for(Tile t: row){
                b.append(t.getValue()).append(" ");
            }
            b.append("\n");
        }
        return b.toString();
    }


}



