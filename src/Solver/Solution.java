package Solver;

import SudokuGame.BoardCoord;
import SudokuGame.Tile;

import java.util.ArrayList;

public class Solution {

    ArrayList<PropagationPath> solution;

    public Solution(){
        solution = new ArrayList<PropagationPath>();
        solution.add(new PropagationPath(0));
    }

    public Tile[][] revertToLowestDecisionLevel(){
        if(solution.size() <2){
            System.out.println("tried to lower decision level below 0");
        }

        ArrayList<PropagationPath> newSol = new ArrayList<PropagationPath>();
        newSol.add(solution.get(0));
        solution = newSol;
        return null;
    }

    public void addDecisionLevel(){
        solution.add(new PropagationPath(solution.size()));
    }
    public void addToLastDecisionLevel(BoardCoord bc, byte val){
        solution.get(solution.size()-1).add(bc,val);
    }


}




