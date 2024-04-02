package Solver;

import SudokuGame.Board;
import SudokuGame.BoardCoord;
import SudokuGame.Tile;

import java.util.ArrayList;

public class Solution {

    public ArrayList<PropagationPath> solution;

    public Solution(Board b){
        solution = new ArrayList<PropagationPath>();
        solution.add(new PropagationPath(b.getValuesCopy(),0));
    }

    public Tile[][] revertToLowestDecisionLevel(){
        return revertToDecisionLevel(0);
    }
    public Tile[][] revertHighestDecisionLevel(){
        return revertToDecisionLevel(solution.size() -2);
    }

    public Tile[][] revertToDecisionLevel(int level){
        if(solution.size() <= 1){
            System.out.println("tried to lower decision level below 0");
        }
        Tile[][]  revertTo =  solution.get(level+1).getStartingBoard();
        solution.subList(level + 1, solution.size()).clear();
        return revertTo;

    }

    public Tile[][] getInitialBoard(){
        return solution.get(0).getStartingBoard();
    }

    public void addDecisionLevel(Board start, TileSolution guess){
        solution.add(new PropagationPath(start.getValuesCopy(), solution.size(), guess));
        start.setTile(guess);
    }
    public void addToLastDecisionLevel(BoardCoord bc, byte val){
       addToLastDecisionLevel(new TileSolution(bc, val));
    }
    public void addToLastDecisionLevel(TileSolution s){
        solution.get(solution.size()-1).add(s);
    }

    public TileSolution getFirstDecision(){
        if(solution.size() == 1){
            return null;
        }
        return solution.get(1).getInitialDecision();

    }

    public TileSolution getLastDecision(){
        if(solution.size() == 1){
            return null;
        }
        return solution.get(solution.size()-1).getInitialDecision();
    }
    public int getCurrentDecisionLevel(){
        return solution.size()-1;
    }
    public String toString(){
        return solution.toString();
    }

    public Board getBoardFromDecisionLevel(int level){
        if(level < 0 || level >= solution.size()){
            return null;
        }
        PropagationPath currentDecision = solution.get(level);
        Board board = new Board(currentDecision.getStartingBoard());
        if(level != 0) {
            board.setTile(currentDecision.getInitialDecision());
        }
        for(TileSolution ts: currentDecision.getPath()){
            board.setTile(ts);
        }



        return board;
    }




}




