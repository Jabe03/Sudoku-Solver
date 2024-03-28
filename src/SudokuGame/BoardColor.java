package SudokuGame;

import java.awt.*;

public enum BoardColor{
    INDIGO,
    BLUE,
    GREEN,
    RED,
    ORANGE,
    SUNSET_ORANGE,
    YELLOW,
    DARK_GRAY,
    PINK,
    BROWN,
    CYAN;
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

//    // Regular Colors
//    public static final String BLACK = "\033[0;30m";   // BLACK
//    public static final String RED = "\033[0;31m";     // RED
//    public static final String GREEN = "\033[0;32m";   // GREEN
//    public static final String YELLOW = "\033[0;33m";  // YELLOW
//    public static final String BLUE = "\033[0;34m";    // BLUE
//    public static final String PURPLE = "\033[0;35m";  // PURPLE
//    public static final String CYAN = "\033[0;36m";    // CYAN
//    public static final String WHITE = "\033[0;37m";   // WHITE
//    // High Intensity
//    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
//    public static final String RED_BRIGHT = "\033[0;91m";    // RED
//    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
//    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
//    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
//    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
//    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
//    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    public static BoardColor[] getDefaultOrder(){
        return new BoardColor[]{INDIGO,YELLOW,PINK,BROWN,CYAN,ORANGE,YELLOW, GREEN, RED};
    }
    public static BoardColor[] getAlternatingOrder(BoardColor a, BoardColor b){
        BoardColor[] result = new BoardColor[9];
        for(int i = 0; i < 9; i++){
            result[i] = i%2 == 0 ? a : b;
        }
        return result;
    }

    public static BoardColor[] getUniformColor(BoardColor a){
        BoardColor[] result = new BoardColor[9];
        for(int i = 0; i < 9; i++){
            result[i] = a;
        }
        return result;
    }
    public static String inColor(String s, BoardColor c) {
        String colorString;
        switch (c) {
            case INDIGO -> colorString = "\033[0;35m";
            case BLUE -> colorString = "\033[0;34m";
            case GREEN -> colorString = "\033[0;32m";
            case RED -> colorString = "\033[0;31m";
            case ORANGE -> colorString = "\033[0;91m";
            case SUNSET_ORANGE -> colorString = "\033[0;93m";
            case YELLOW -> colorString = "\033[0;33m";
            case DARK_GRAY -> colorString = "\033[0;30m";
            case PINK -> colorString = "\033[0;95m";
            case BROWN -> colorString = "\033[0;90m";
            case CYAN -> colorString = "\033[0;36m";
            default -> colorString = "ERROR";
        }

        return colorString + s + "\033[0m";
    }

    public static Color getRGBColor(BoardColor c){
        switch (c){
            case INDIGO -> {
                return new Color(117, 41, 255);
            }
            case BLUE -> {
                return new Color(27, 27, 199);
            }
            case GREEN -> {
                return new Color(83, 0, 150);
            }
            case RED -> {
                return new Color(124, 0, 0);
            }
            case ORANGE -> {
                return new Color(227, 155, 0);
            }
            case SUNSET_ORANGE -> {
                return new Color(255, 106, 0);
            }
            case YELLOW -> {
                return new Color(255, 221, 0);
            }
            case DARK_GRAY -> {
                return new Color(63, 63, 63);
            }
            case PINK -> {
                return new Color(182, 0, 173);
            }
            case BROWN -> {
                return new Color(122, 63, 0);
            }
            case CYAN -> {
                return new Color(0, 247, 255);
            }
        }
        return null;
    }







}

