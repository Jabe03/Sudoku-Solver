import SudokuGame.BoardCoord;
import SudokuGame.BoardCoordType;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BoardCoordTest {

    @Test
    public void OutOfBoundsTest(){
        assertThrows(IndexOutOfBoundsException.class, () -> new BoardCoord(9,0));
        assertThrows(IndexOutOfBoundsException.class, () -> new BoardCoord(-1,192387198));
    }

    @Test
    public void InBoundsTest(){
        BoardCoord bc = new BoardCoord(3,2);
        assertEquals(3, bc.row);
        assertEquals(2, bc.col);
        bc = new BoardCoord(8,0);
        assertEquals(8, bc.row);
        assertEquals( 0, bc.col);

    }

    @Test
    public void InBoundsPartitionTest(){
        BoardCoord bc = new BoardCoord(1,2, BoardCoordType.PartitionCoord);
        assertEquals(1, bc.row);
        assertEquals(2, bc.col);
    }
    @Test
    public void OutOfBoundsPartitionTest(){
        assertThrows(IndexOutOfBoundsException.class, () -> new BoardCoord(-1,2, BoardCoordType.PartitionCoord));
        assertThrows(IndexOutOfBoundsException.class, () -> new BoardCoord(0,3, BoardCoordType.PartitionCoord));

    }
}
