package Solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PowerSetIterator<E> implements Iterator<Set<E>> {

    private  ArrayList<E> elements;
    private boolean[] current;

    public PowerSetIterator(Set<E> set){
        this.elements = new ArrayList<E>(set);
        current = new boolean[elements.size()];
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Set<E> next() {
       Set<E> nextSubset = getSetFromPowerSetCounter();
       incrementPowerSetCounter();
       return nextSubset;


    }

    private Set<E> getSetFromPowerSetCounter(){
        Set<E> result = new HashSet<>();
        for(int i = 0; i < current.length; i ++){
            if(current[i]){
                result.add(elements.get(i));
            }
        }
        return result;
    }

    private void incrementPowerSetCounter(){
        int counter = 0;
        do{
            if(counter == current.length){
                current = null;
                return;
            }
            current[counter] = !current[counter];
            counter++;
        } while (current[counter-1] == false);
    }


}
