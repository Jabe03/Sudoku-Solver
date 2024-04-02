package SudokuGame;

import java.util.*;

public class Tile {

    public static final Set<Integer> sudokuValues = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
    private byte value;
    private boolean[] notes;
    private boolean isIntrinsic;
    private BoardCoord bc;
    public Tile(){
        this( null, (byte) 0 );
    }
    public Tile(BoardCoord bc, byte b){
        this.bc = bc;
        isIntrinsic = false;
        value = b;
        notes = new boolean[9];
    }

    public Tile(Tile t){
        this.bc = new BoardCoord(t.bc);
        this.isIntrinsic = t.isIntrinsic;
        this.notes = Arrays.copyOf(t.notes, t.notes.length);
        this.value = t.value;
    }
    public byte getValue(){
        return this.value;
    }
    public boolean hasValue(){
        return value != 0;
    }
    public void clear(){
        clearNotes();
        this.value = 0;
    }
    public void turnOnAllNotes(){
        Arrays.fill(notes, true);
    }
    public void clearNotes(){
        this.notes = new boolean[9];
    }
    public void removeNote(byte note){
        notes[note-1] = false;
    }
    public void toggleNote(byte note){
        notes[note-1] = !notes[note-1];
    }
    public boolean[] getNotes(){
        return notes;
    }
    public List<Integer> getNotesList(){
        List<Integer> result = new ArrayList<Integer>();
        for(int i = 0; i < notes.length; i++){
            if(notes[i]){
                result.add(i+1);
            }
        }
        return result;
    }
    public BoardCoord getCoordinates(){
        return this.bc;
    }

    public void setValue(byte value){
        if(isSudokuValue(value) && !isIntrinsic){
            this.value = value;
            clearNotes();
        }
    }

    public void manualValueChange(byte value){
        this.value = value;
    }
    public static boolean isSudokuValue(int val){
        return sudokuValues.contains(val);
    }
    public static boolean isSudokuValue(byte val){
        return sudokuValues.contains((int)val);
    }
    public void makeIntrinsic(){
        isIntrinsic = true;
    }
    public boolean isIntrinsic(){
        return isIntrinsic;
    }


    @Override
    public boolean equals(Object o){
        if(o instanceof Tile t){
            return this.value == t.getValue();
        } else return false;
    }

    @Override
    public String toString(){
        if(this.hasValue()){
            return "~"; //String.format("Tile[row=%d,col=%d,val=%d]", bc.row, bc.col, value);
        }
        return String.format("Tile[row=%d,col=%d,notes=%s]", bc.row, bc.col, getNotesList());

    }
}
