package SudokuGame;

import java.util.Random;

/**
 * The game board is a sudoku board that has a certain number of values given to you.
 * The generated board is guaranteed to be solvable.
 */

public class GameBoard extends Board{

    private GameBoard(int[][] values){
        super(values);
    }
    private GameBoard(byte[][] values){
        super(values);
    }
    private GameBoard(){
        super();
    }
    public static GameBoard generateEmpty(){
        return new GameBoard();
    }
    //Generates a "random" solvable game board (not solvable yet necessarily, TBI)
    public static GameBoard generate(long seed){
        return generate(seed, 40);
    }
    public static GameBoard fromBoard(int[][] b){
        return new GameBoard(b);
    }
    public static GameBoard generate(long seed, int numGivens){
        GameBoard b = new GameBoard();
        Random r  = new Random(seed);
        for(int i  = 0; i < numGivens; i++){
            int val = r.nextInt(9) + 1;
            int count = 0;
            do{
                count++;
                int newrow = r.nextInt(0, 9);
                int newcol = r.nextInt(0, 9);
                BoardCoord bc = new BoardCoord(newrow, newcol);
                if(!b.getTile(bc).hasValue()) {
                    b.setTile(bc, val);
                    if (b.isValid()) {
                        b.getTile(bc).makeIntrinsic();
                        System.out.printf("Making : %s intrinsic with value %d (took %d attempts)%n", bc.toString(), val, count);
                        break;
                    } else {
                        b.clearCell(bc);
                    }
                }

                if(count == 50){
                    System.out.printf("Failed to place value %d (%d attempts)%n", val, count);
                }
            }while(count < 50);

        }
        return b;
    }
    public static GameBoard generate(){
        return generate(new Random().nextLong());
    }
}
