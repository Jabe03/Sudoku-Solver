package Solver;

import SudokuGame.Board;
import SudokuGame.BoardCoord;
import SudokuGame.Tile;

import java.util.ArrayList;

public class Solution {

    ArrayList<PropagationPath> solution;

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
        return solution.get(1).initialDecision;

    }

    public TileSolution getLastDecision(){
        if(solution.size() == 1){
            return null;
        }
        return solution.get(solution.size()-1).initialDecision;
    }
    public int getCurrentDecisionLevel(){
        return solution.size();
    }
    public String toString(){
        return solution.toString();
    }


}




