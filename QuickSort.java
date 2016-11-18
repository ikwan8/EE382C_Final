import java.lang.reflect.Executable;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickSort {
    private static int N_THREADS;
    private static ExecutorService pool;

    public QuickSort(int numThreads){
        N_THREADS = numThreads;
    }

    public void quicksort(ArrayList<Integer> input) {
        pool = Executors.newFixedThreadPool(N_THREADS);
        List<Callable<Object>> isDone = new ArrayList<Callable<Object>>();
        isDone.add(Executors.callable(new QuicksortRunnable(input, 0, input.size() - 1)));
        try {
            pool.invokeAll(isDone);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }

    private static class QuicksortRunnable implements Runnable {
        private ArrayList<Integer> values;
        private int left;
        private int right;

        public QuicksortRunnable(ArrayList<Integer> values, int left, int right) {
            this.values = values;
            this.left = left;
            this.right = right;
        }

        private int partition(int left, int right) {
            int tempLeft = left, tempRight = right;
            int tmp;
            int pivot = values.get((left + right) / 2);

            while (tempLeft <= tempRight) {
                while (values.get(tempLeft) < pivot)
                    tempLeft++;
                while (values.get(tempRight) > pivot)
                    tempRight--;
                if (tempLeft <= tempRight) {
                    tmp = values.get(tempLeft);
                    values.set(tempLeft, values.get(tempRight));
                    values.set(tempRight, tmp);
                    tempLeft++;
                    tempRight--;
                }
            }
            return tempLeft;
        }

        @Override
        public void run() {
            List<Callable<Object>> isDone = new ArrayList<Callable<Object>>();
            int index = partition(left, right);
            if(left < index-1){
                isDone.add(Executors.callable(new QuicksortRunnable(values, left, index-1)));
            }
            if(index < right){
                isDone.add(Executors.callable(new QuicksortRunnable(values, index, right)));
            }
            try {
                pool.invokeAll(isDone);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}