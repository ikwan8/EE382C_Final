package EE382C_Final;

/**
 * Created by Ian on 11/15/2016.
 */

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.concurrent.*;
import java.util.Collections;
import java.lang.Math;


public class Radixsort {
    ExecutorService threadpool;

    public Radixsort(int numThreads) {
        threadpool = Executors.newFixedThreadPool(numThreads);
    }

    public ArrayList<Integer> radixsort(ArrayList<Integer> list) throws ExecutionException, InterruptedException {
        Future<ArrayList<Integer>> sorted = threadpool.submit(new RadixsortTask(list, getMaxExp(list)));

        ArrayList<Integer> sortedArray = sorted.get();

        threadpool.shutdown();
        return sortedArray;

    }

    public static int getMaxExp(ArrayList<Integer> list){
        int max = Collections.max(list);
        int i = 1;
        while(i < max){
            i*=10;
        }
        return (int) Math.log10(i);
    }

    public static int digitAt(int input, int pos){
        int i =(int) (input%(Math.pow(10, pos)));
        int j = (int) (i/Math.pow(10, pos-1));
        return Math.abs(j); // abs handles negative input
    }
    public class RadixsortTask implements Callable{
        int exp;
        ArrayList<Integer> zero;
        ArrayList<Integer> one;
        ArrayList<Integer> two;
        ArrayList<Integer> three;
        ArrayList<Integer> four;
        ArrayList<Integer> five;
        ArrayList<Integer> six;
        ArrayList<Integer> seven;
        ArrayList<Integer> eight;
        ArrayList<Integer> nine;

        ArrayList<Integer> list;


        public RadixsortTask(ArrayList<Integer> list, int exp) {
            this.list = list;
            this.exp = exp;
            this.zero = new ArrayList<Integer>();
            this.one  = new ArrayList<Integer>();
            this.two = new ArrayList<Integer>();
            this.three = new ArrayList<Integer>();
            this.four = new ArrayList<Integer>();
            this.five = new ArrayList<Integer>();
            this.six = new ArrayList<Integer>();
            this.seven = new ArrayList<Integer>();
            this.eight = new ArrayList<Integer>();
            this.nine = new ArrayList<Integer>();

            for(Integer i: list){
                switch (digitAt(i, exp)){
                    case 0: zero.add(i);
                        break;
                    case 1: one.add(i);
                        break;
                    case 2: two.add(i);
                        break;
                    case 3: three.add(i);
                        break;
                    case 4: four.add(i);
                        break;
                    case 5: five.add(i);
                        break;
                    case 6: six.add(i);
                        break;
                    case 7: seven.add(i);
                        break;
                    case 8: eight.add(i);
                        break;
                    case 9: nine.add(i);
                        break;
                }
            }
        }

        @Override
        public ArrayList<Integer> call() throws ExecutionException, InterruptedException {
            if(isSorted() || onlyDuplicates(list) || exp < 0){
                return combined();
            }
            else {
                Future<ArrayList<Integer>> zeroSort =threadpool.submit(new RadixsortTask(zero, exp - 1));
                Future<ArrayList<Integer>> oneSort = threadpool.submit(new RadixsortTask(one, exp - 1));
                Future<ArrayList<Integer>> twoSort = threadpool.submit(new RadixsortTask(two, exp - 1));
                Future<ArrayList<Integer>> threeSort = threadpool.submit(new RadixsortTask(three, exp - 1));
                Future<ArrayList<Integer>> fourSort = threadpool.submit(new RadixsortTask(four, exp - 1));
                Future<ArrayList<Integer>> fiveSort = threadpool.submit(new RadixsortTask(five, exp - 1));
                Future<ArrayList<Integer>> sixSort = threadpool.submit(new RadixsortTask(six, exp - 1));
                Future<ArrayList<Integer>> sevenSort = threadpool.submit(new RadixsortTask(seven, exp - 1));
                Future<ArrayList<Integer>> eightSort = threadpool.submit(new RadixsortTask(eight, exp - 1));
                Future<ArrayList<Integer>> nineSort = threadpool.submit(new RadixsortTask(nine, exp - 1));

                try {
                    zero = zeroSort.get();
                    one = oneSort.get();
                    two = twoSort.get();
                    three = threeSort.get();
                    four = fourSort.get();
                    five = fiveSort.get();
                    six = sixSort.get();
                    seven = sevenSort.get();
                    eight = eightSort.get();
                    nine = nineSort.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //System.out.println("done calling");
                return combined();
            }


        }

        public boolean bucketIsSorted(ArrayList<Integer> list){
            return list.size() == 0 || list.size() == 1;
        }

        public boolean isSorted(){
            return bucketIsSorted(zero) && bucketIsSorted(one) && bucketIsSorted(two) && bucketIsSorted(three) && bucketIsSorted(four) && bucketIsSorted(five) && bucketIsSorted(six) && bucketIsSorted(seven) && bucketIsSorted(eight) && bucketIsSorted(nine);
        }

        public ArrayList<Integer> combined(){
            ArrayList<Integer> combinedLists = new ArrayList<Integer>();
            combinedLists.addAll(zero);
            combinedLists.addAll(one);
            combinedLists.addAll(two);
            combinedLists.addAll(three);
            combinedLists.addAll(four);
            combinedLists.addAll(five);
            combinedLists.addAll(six);
            combinedLists.addAll(seven);
            combinedLists.addAll(eight);
            combinedLists.addAll(nine);
            //System.out.println(combinedLists);
            return combinedLists;
        }

        public boolean onlyDuplicates(ArrayList<Integer> input){
            for(Integer i: input){
                if (i != input.get(0)){
                    return false;
                }
            }
            return true;
        }

    }
}