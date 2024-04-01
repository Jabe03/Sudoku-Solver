package Display;

import Solver.Solver;
import SudokuGame.Board;
import SudokuGame.BoardColor;
import SudokuGame.BoardCoord;
import SudokuGame.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BoardView extends JPanel {

    JFrame window;
    Board b;

    Solver s;
    public static final int PADDING = 100;
    public static final int BOX_SIZE = 45;
    public static final Color backgroundColor = new Color(187, 187, 187);
    public static final Color notesColor = new Color(79, 79, 79);
    public static final Color majorGridlineColor = new Color(0, 0, 0);
    public static final Color minorGridlineColor = new Color(168, 168, 168);
    public static final Font defaultFont = new Font("Ariel", Font.PLAIN, 10);
    public static final Font valueFont = defaultFont.deriveFont(30.0F);
    public static final Font notesFont = defaultFont.deriveFont(10.0F);

    public BoardView(Board b){
        super();
        this.b = b;
        this.setSize(new Dimension(BOX_SIZE * 9 + PADDING,BOX_SIZE * 9 + PADDING));
        initWindow();


    }

    private void initWindow(){
        this.window = new JFrame("Sudoku Board");
        window.add(this);
        window.setContentPane(this);
        window.setSize(new Dimension(600, 600));
        window.setAlwaysOnTop(true);
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_SPACE -> {
                        if(s != null){
                            s.togglePause();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void setSolver(Solver s){
        this.s = s;
    }


    public void toggleVisible(){
        window.setVisible(!window.isVisible());
    }

    public void updateFrame(){

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        drawBackground(g);
        drawGridlines(g);
        drawBoard(g);
        drawSolverInfo(g);
    }

    private void drawSolverInfo(Graphics g){
        if(s == null){
            return;
        }
        g.setFont(defaultFont);
        g.drawString("Decision level: " + s.getSolution().getCurrentDecisionLevel(),PADDING, PADDING);

        g.drawString("SPACE = pause solver",  PADDING + 9*BOX_SIZE, PADDING + 9*BOX_SIZE);
    }

    private void drawBackground(Graphics g){
        g.setColor(backgroundColor);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
    private void drawGridlines(Graphics g){
        g.setColor(Color.BLACK);
        g.drawLine(PADDING, PADDING, PADDING + 9*BOX_SIZE, PADDING); //top edge
        g.drawLine(PADDING, PADDING, PADDING , PADDING + 9*BOX_SIZE); //left edge
        g.drawLine(PADDING + 9*BOX_SIZE, PADDING, PADDING + 9*BOX_SIZE, PADDING +  + 9*BOX_SIZE); // right edge
        g.drawLine(PADDING, PADDING + 9*BOX_SIZE, PADDING + 9*BOX_SIZE, PADDING + 9*BOX_SIZE); //bottom edge
        g.setColor(minorGridlineColor);
        for(int x = 0; x <= 9 ; x++){
                g.drawLine(PADDING + x*BOX_SIZE, PADDING, PADDING + x*BOX_SIZE, PADDING + 9*BOX_SIZE);
        }
        for(int y = 0; y <= 9; y++){
            g.drawLine( PADDING,PADDING + y*BOX_SIZE,  PADDING + 9*BOX_SIZE,PADDING + y*BOX_SIZE);
        }
        g.setColor(majorGridlineColor);
        for(int x = 0; x <= 3 ; x++){
            g.drawLine(PADDING + 3*x*BOX_SIZE, PADDING, PADDING + 3*x*BOX_SIZE, PADDING + 9*BOX_SIZE);
        }
        for(int y = 0; y <= 3; y++){
            g.drawLine( PADDING,PADDING + 3*y*BOX_SIZE,  PADDING + 9*BOX_SIZE,PADDING + 3*y*BOX_SIZE);
        }

    }
    private void drawBoard(Graphics g){
        g.setColor(new Color(0,0,0));



        for(int i = 0; i < 9; i++){
            for(int j= 0 ; j < 9 ; j++){
                BoardCoord bc = new BoardCoord(i,j);
                BoardCoord partition = bc.getPartitionCoords();
                int partitionNum = partition.row*3 + partition.col;
                Tile t = b.getTile(bc);
                if(t.hasValue()){
                    g.setFont(valueFont);
                    String value = Byte.toString(t.getValue());
                    int centeringY = -(BOX_SIZE - g.getFontMetrics().getAscent())/2 - 5;
                    int centeringX = (BOX_SIZE - g.getFontMetrics().stringWidth(value) )/2;
                    g.setColor(BoardColor.getRGBColor(BoardColor.getDefaultOrder()[partitionNum]));
                    g.drawString(value, PADDING + j*BOX_SIZE + centeringX, PADDING + (i+1)*BOX_SIZE + centeringY);
                } else {
                    g.setFont(notesFont);
                    g.setColor(notesColor);
                    int noteHeight = g.getFontMetrics().getHeight();
                    //g.drawString("NOTES", PADDING + j*BOX_SIZE , PADDING + i*BOX_SIZE);
                    boolean[] notes = t.getNotes();
                    for(int k = 0; k < notes.length; k++){
                        int centeringY = (BOX_SIZE - ((2*BOX_SIZE/3) - 5))/2;
                        int centeringX = (BOX_SIZE - ((2*BOX_SIZE/3) + g.getFontMetrics().stringWidth("5")) )/2;
                        if(notes[k]) {
                            int xOffset = k % 3 * (BOX_SIZE / 3);
                            int yOffset = k / 3 * (BOX_SIZE / 3);
                            g.drawString(Integer.toString(k+1), PADDING + j*BOX_SIZE + xOffset + centeringX, PADDING + i*BOX_SIZE + yOffset + centeringY);
                        }
                    }
                }

            }
        }
    }
}
