
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Darren on 11/9/2016.
 */
public class Bricksort {

    ExecutorService threadpool;
    boolean isSorted;

    public Bricksort(int numThreads) {
        threadpool = Executors.newFixedThreadPool(numThreads);
    }


    //TODO: need n/2 comparators
    public void bricksort(ArrayList<Integer> list){
        isSorted = false;
        while(!isSorted){
            isSorted = true;

            List<Callable<Object>> callsOdd = new ArrayList<Callable<Object>>();
            for(int i = 1; i < list.size() - 1; i += 2){
                callsOdd.add(Executors.callable(new BricksortTask(list, i)));
            }
            try {
                threadpool.invokeAll(callsOdd);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("odd  : " + list + " " + isSorted);

            List<Callable<Object>> callsEven = new ArrayList<Callable<Object>>();
            for(int i = 0; i < list.size() - 1; i += 2){
                callsEven.add(Executors.callable(new BricksortTask(list, i)));
            }
            try {
                threadpool.invokeAll(callsEven);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("even  : " + list + " " + isSorted);

        }
        threadpool.shutdown();
    }

    public class BricksortTask implements Runnable{

        ArrayList<Integer> list;
        int i;

        public BricksortTask(ArrayList<Integer> list, int i) {
            this.list = list;
            this.i = i;
        }

        @Override
        public void run() {
            if(list.get(i) > list.get(i+1)) {
                swap(list, i, i + 1);
                isSorted = false;
            }
        }
    }

    public void swap(ArrayList<Integer> list, int i, int j){
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
