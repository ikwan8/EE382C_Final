/**
 * Created by Ian on 11/10/2016.
 */
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.*;
import java.util.concurrent.Future;

public class MergeSort{
    ExecutorService threadpool;

    public MergeSort(int numThreads) {
        threadpool = Executors.newFixedThreadPool(numThreads);
    }

    public void mergesort(ArrayList<Integer> whole) throws ExecutionException, InterruptedException {
        Future<ArrayList<Integer>> sorted = threadpool.submit(new MergeSortTask(whole));
        try {
            whole = sorted.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        threadpool.shutdown();

    }

    public static void printArr(ArrayList<Integer> a){
        for(Integer i: a){
            System.out.print(i + ", ");
        }
        System.out.println();
    }

    public class MergeSortTask implements Callable {

        private ArrayList<Integer> whole;

        public MergeSortTask(ArrayList<Integer> whole) {
            this.whole = whole;
        }

        @Override
        public ArrayList<Integer> call() throws ExecutionException, InterruptedException {
            //System.out.println(whole);
            ArrayList<Integer> left = new ArrayList<Integer>();
            ArrayList<Integer> right = new ArrayList<Integer>();
            int center;

            if (whole.size() == 1) {
                return whole;
            } else {
                center = whole.size() / 2;
                // copy the left half of whole into the left.
                for (int i = 0; i < center; i++) {
                    left.add(whole.get(i));
                }

                //copy the right half of whole into the new arraylist.
                for (int i = center; i < whole.size(); i++) {
                    right.add(whole.get(i));
                }

                // Sort the left and right halves of the arraylist.
                Future<ArrayList<Integer>> leftFuture = threadpool.submit(new MergeSortTask(left));
                Future<ArrayList<Integer>> rightFuture = threadpool.submit(new MergeSortTask(right));
                left = leftFuture.get();
                right = rightFuture.get();

                // Merge the results back together.
                merge(left, right, whole);
            }
            return whole;
        }

        private void merge(ArrayList<Integer> left, ArrayList<Integer> right, ArrayList<Integer> whole) {
            int leftIndex = 0;
            int rightIndex = 0;
            int wholeIndex = 0;

            // As long as neither the left nor the right ArrayList has
            // been used up, keep taking the smaller of left.get(leftIndex)
            // or right.get(rightIndex) and adding it at both.get(bothIndex).
            while (leftIndex < left.size() && rightIndex < right.size()) {
                if ((left.get(leftIndex).compareTo(right.get(rightIndex))) < 0) {
                    whole.set(wholeIndex, left.get(leftIndex));
                    leftIndex++;
                } else {
                    whole.set(wholeIndex, right.get(rightIndex));
                    rightIndex++;
                }
                wholeIndex++;
            }

            ArrayList<Integer> rest;
            int restIndex;
            if (leftIndex >= left.size()) {
                // The left ArrayList has been use up...
                rest = right;
                restIndex = rightIndex;
            } else {
                // The right ArrayList has been used up...
                rest = left;
                restIndex = leftIndex;
            }

            // Copy the rest of whichever ArrayList (left or right) was not used up.
            for (int i = restIndex; i < rest.size(); i++) {
                whole.set(wholeIndex, rest.get(i));
                wholeIndex++;
            }
        }
    }
}