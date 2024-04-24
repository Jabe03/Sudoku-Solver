import Solver.PowerSetIterator;

import java.util.HashSet;
import java.util.Set;

public class PowerSetIteratorTests {

    public static void main(String[] args){
        Set<Integer> myset = new HashSet<>();
        for(int i = 1; i <= 30; i++){
            myset.add(i);
        }
        PowerSetIterator<Integer> iter = new PowerSetIterator<>(myset);
        int counter = 0;
        long start = System.currentTimeMillis();
        while(iter.hasNext()){
            iter.next();
            //System.out.println(iter.next());
            counter++;
            if(counter%2000000 == 0){
                double percentCompletion = counter/(Math.pow(2,(myset.size())));
                System.out.println(percentCompletion*100 + "%");
            }
        }
        System.out.println("Total nummber of elements in powerset: " + counter + " (" + (System.currentTimeMillis()-start) + "ms)" );

    }


}
