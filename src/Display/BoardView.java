package Display;

import Solver.Solver;
import SudokuGame.Board;
import SudokuGame.BoardColor;
import SudokuGame.BoardCoord;
import SudokuGame.Tile;
import Solver.PropagationPath;
import Solver.TileSolution;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BoardView extends JPanel {

    JFrame window;
    Board board;

    Solver s;

    int currentDecisionLevelView;
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
        this.board = b;
        currentDecisionLevelView = 0;
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
                            if(currentDecisionLevelView > s.getSolution().getCurrentDecisionLevel()-1){
                                currentDecisionLevelView = s.getSolution().getCurrentDecisionLevel() - 1;
                            }
                        }

                    }
                    case KeyEvent.VK_LEFT -> {
                        if(s != null && s.isPaused() && currentDecisionLevelView > 0){
                            currentDecisionLevelView--;
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if(s != null && s.isPaused() && currentDecisionLevelView < s.getSolution().solution.size()-1){
                            currentDecisionLevelView++;
                        }
                    }
                    case KeyEvent.VK_UP -> {
                        if(s != null && s.isPaused()){
                            currentDecisionLevelView = s.getSolution().getCurrentDecisionLevel();
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        if(s != null && s.isPaused()){
                            currentDecisionLevelView = 0;
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
        if(s != null && s.isPaused()){
            Board b = s.getSolution().getBoardFromDecisionLevel(currentDecisionLevelView);
            if(b != null){
                drawBoard(g,b , getColoringAtDL(b,currentDecisionLevelView));
            }

        } else {
            drawBoard(g, board);
        }

        drawSolverInfo(g);
    }

    private void drawSolverInfo(Graphics g){
        if(s == null || s.getSolution() == null){
            return;
        }
        g.setFont(defaultFont);
        g.setColor(BoardColor.getRGBColor(BoardColor.DARK_GRAY));
        g.drawString("Decision level: " + (s.isPaused() ? currentDecisionLevelView : s.getSolution().getCurrentDecisionLevel()) + "numIterations="+ s.iterNum,PADDING, PADDING);

        g.drawString("SPACE = pause solver",  PADDING + 9*BOX_SIZE, PADDING + 9*BOX_SIZE);

        int displayDecLevel = 0;
        if(s.isPaused()){
           displayDecLevel = currentDecisionLevelView;
        } else {
            displayDecLevel = s.getSolution().solution.size()-1;
        }
        wrapString(g, s.getSolution().solution.get(displayDecLevel).toString(), PADDING, PADDING + 9*BOX_SIZE, PADDING + 9*BOX_SIZE + g.getFontMetrics().getHeight());
        //g.drawString(wrapString(g, s.getSolution().solution.get(displayDecLevel).toString(), getWidth()), PADDING, PADDING + 9*BOX_SIZE + g.getFontMetrics().getHeight());
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
    private void drawBoard(Graphics g, Board b){

        drawBoard(g,b, getDefaultColoring(b));

    }

    private Color[][] getDefaultColoring(Board b){
        Color[][] colors = new Color[9][9];


        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                BoardCoord bc = new BoardCoord(i,j);
                BoardCoord partition = bc.getPartitionCoords();
                Tile t = b.getTile(bc);
                int partitionNum = partition.row*3 + partition.col;
                if(t.hasValue()){
                    if(t.isIntrinsic()){
                        colors[i][j] =BoardColor.getRGBColor(BoardColor.INDIGO); //BoardColor.getRGBColor(BoardColor.getDefaultOrder()[partitionNum]);
                    } else {
                        colors[i][j] =BoardColor.getRGBColor(BoardColor.DARK_GRAY);
                    }
                } else {
                    colors[i][j] = notesColor;
                }
            }
        }
        return colors;
    }

    public Color[][] getColoringAtDL(Board b, int decisionLevel){
        Color[][] colors = getDefaultColoring(b);
        PropagationPath p = s.getSolution().solution.get(decisionLevel);

        if(decisionLevel != 0) {
            TileSolution ts = p.getInitialDecision();
            colors[ts.getBc().row][ts.getBc().col] = BoardColor.getRGBColor(BoardColor.CYAN);
        }
        for(TileSolution t: p.getPath()){
            if(!b.getTile(t.getBc()).hasValue()){
                System.out.println("colored tile does not have a value, something is wrong");
            }
            //System.out.println("Coloring a tile");
            colors[t.getBc().row][t.getBc().col] = BoardColor.getRGBColor(BoardColor.ORANGE);
        }
        return colors;
    }

    private void drawBoard(Graphics g, Board b, Color[][] colors){
        if (!b.hasBoard()){
            return;
        }
        //g.setColor(new Color(0,0,0));



        for(int i = 0; i < 9; i++){
            for(int j= 0 ; j < 9 ; j++){
                BoardCoord bc = new BoardCoord(i,j);
                //BoardCoord partition = bc.getPartitionCoords();
                Tile t = b.getTile(bc);
                if(t.hasValue()){
                    g.setFont(valueFont);
                    String value = Byte.toString(t.getValue());
                    int centeringY = -(BOX_SIZE - g.getFontMetrics().getAscent())/2 - 5;
                    int centeringX = (BOX_SIZE - g.getFontMetrics().stringWidth(value) )/2;
                    g.setColor(colors[i][j]);
                    g.drawString(value, PADDING + j*BOX_SIZE + centeringX, PADDING + (i+1)*BOX_SIZE + centeringY);
                } else {
                    g.setFont(notesFont);
                    g.setColor(colors[i][j]);
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


    public void wrapString(Graphics g,String original, int x1, int x2, int y){
        int stringWidth = g.getFontMetrics().stringWidth(original);
        int width = x2-x1;
        int numLines = stringWidth / width + (stringWidth% width == 0 ? 0 : 1);
        int charsPerLine;
        if(numLines == 0){
            charsPerLine = original.length();
        } else {
            charsPerLine = original.length() / numLines;
        }
        for(int i = 0; i < numLines; i++){
            if(i == numLines-1){
                g.drawString(original.substring(i*charsPerLine), x1, y + i*g.getFontMetrics().getHeight());

            } else {
                g.drawString(original.substring(i*charsPerLine, (i + 1) * charsPerLine), x1, y + i*g.getFontMetrics().getHeight());

            }
        }
    }
}
