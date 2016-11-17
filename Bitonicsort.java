
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Darren on 11/9/2016.
 */
public class Bitonicsort {

    ExecutorService threadpool;

    public Bitonicsort(int numThreads) {
        threadpool = Executors.newFixedThreadPool(numThreads);
    }

    public void bitonicsort(ArrayList<Integer> list, int logn){
        for(int i =0; i < logn; i++){
            for(int j = 0; j <= i; j++){
                kernel(list, i, j);
                System.out.println(list);
            }
        }
        threadpool.shutdown();
    }

    void kernel(ArrayList<Integer> list, int p, int q){
        int d = 1 << (p-q);

        for(int i = 0; i < list.size(); i++){
            threadpool.submit(new BitonicTask(i, p, d, list));
        }
    }

    public class BitonicTask implements Runnable{

        int i, p, d;
        ArrayList<Integer> list;

        public BitonicTask(int i, int p, int d, ArrayList<Integer> list) {
            this.i = i;
            this.p = p;
            this.d = d;
            this.list = list;
        }

        @Override
        public void run() {
            boolean up = ((i >> p) & 2) == 0;

            if((i & d) == 0 && (list.get(i) > list.get(i|d)) == up){
                int t = list.get(i);
                list.set(i, list.get(i|d));
                list.set(i|d, t);
            }
        }
    }
}
