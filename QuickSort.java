/**
 * Created by Ian on 11/14/2016.
 *
 *
 */

package EE382C_Final;


import java.util.ArrayList;
import java.util.Random;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickSort {
    /**
     * Number of threads to use for sorting.
     */
    private static int N_THREADS;

    /**
     * Thread pool used for executing sorting Runnables.
     */
    private static ExecutorService pool;


    public QuickSort(int numThreads){
        N_THREADS = numThreads;
    }

    public static void printArr(ArrayList<Integer> a){
        for(Integer i: a){
            System.out.print(i + ", ");
        }
        System.out.println();
    }
    /**
     * Main method used for sorting from clients. Input is sorted in place using multiple threads.
     *
     * @param input The array to sort.
     */
    public void quicksort(ArrayList<Integer> input, int threads) {
        pool=Executors.newFixedThreadPool(N_THREADS);
        pool.execute(new QuicksortRunnable(input, 0, input.size() - 1));
    }

    /**
     * Sorts a section of an array using quicksort. The method used is not technically recursive as it just creates new
     * runnables and hands them off to the ThreadPoolExecutor.
     *
     *
     */
    private static class QuicksortRunnable implements Runnable {
        /**
         * The array being sorted.
         */
        private ArrayList<Integer> values;
        /**
         * The starting index of the section of the array to be sorted.
         */
        private int left;
        /**
         * The ending index of the section of the array to be sorted.
         */
        private int right;


        /**
         * Default constructor. Sets up the runnable object for execution.
         * @param values The array to sort.
         * @param left   The starting index of the section of the array to be sorted.
         * @param right  The ending index of the section of the array to be sorted.
         */
        public QuicksortRunnable(ArrayList<Integer> values, int left, int right) {
            this.values = values;
            this.left = left;
            this.right = right;
        }



        /**
         * Method which actually does the sorting. Falls back on recursion if there are a certain number of queued /
         * running tasks.
         *
         * @param pLeft  The left index of the sub array to sort.
         * @param pRight The right index of the sub array to sort.
         */
        private void quicksort(int pLeft, int pRight) {
            pool.execute(new QuicksortRunnable(values, 0, values.size()-1));
        }

        /**
         * Partitions the portion of the array between indexes left and right, inclusively, by moving all elements less
         * than values[pivotIndex] before the pivot, and the equal or greater elements after it.
         *
         * @param left
         * @param right
         * @return The final index of the pivot value.
         */
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
            int index = partition(left, right);
            if(left < index-1){
                pool.execute(new QuicksortRunnable(values, left, index-1));
            }
            if(index < right){
                pool.execute(new QuicksortRunnable(values, index, right));
            }
        }
    }
}