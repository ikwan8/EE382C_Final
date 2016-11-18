import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by Darren on 11/9/2016.
 */
public class Main {

    static int listSize = 1;  //must be power of 2
    static int numThreads = 1;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ArrayList<Long> mergeResult = new ArrayList();
        ArrayList<Long> quickResult = new ArrayList();
        ArrayList<Long> radixResult = new ArrayList();
        ArrayList<Long> bitonicResult = new ArrayList();
        ArrayList<Long> brickResult = new ArrayList();

        while(listSize <= 16384) {
            ArrayList<Integer> mergeList = new ArrayList();
            ArrayList<Integer> quickList = new ArrayList();
            ArrayList<Integer> radixList = new ArrayList();
            ArrayList<Integer> bitonicList = new ArrayList();
            ArrayList<Integer> brickList = new ArrayList();
            Random generator = new Random(50);

            for (int i = 0; i < listSize; i++) {
                int randNum = (int) Math.ceil(generator.nextDouble() * 10000);
                mergeList.add(randNum);
                quickList.add(randNum);
                radixList.add(randNum);
                bitonicList.add(randNum);
                brickList.add(randNum);
            }

            System.out.println("Array size: " + listSize);

            System.out.println("Running Merge Sort");

            MergeSort mergesorter = new MergeSort(numThreads);
            try {
                long startTime = System.nanoTime();
                mergesorter.mergesort(mergeList);
                long endTime = System.nanoTime();
                mergeResult.add(endTime - startTime);
//                System.out.println("Merge Sort timing (ns): " + (endTime - startTime) + "\n");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println("Running quick sort");
            QuickSort quicksorter = new QuickSort(numThreads);
            long startTime = System.nanoTime();
            quicksorter.quicksort(quickList);
            long endTime = System.nanoTime();
            quickResult.add(endTime - startTime);
//            System.out.println("quick Sort timing (ns): " + (endTime - startTime) + "\n");


            System.out.println("Running Radix Sort");
            Radixsort radixsorter = new Radixsort(numThreads);
            try {
                startTime = System.nanoTime();
                ArrayList<Integer> radixResults = radixsorter.radixsort(radixList);
                endTime = System.nanoTime();
                radixResult.add(endTime - startTime);
//                System.out.println("Radix sort timing (ns):" + (endTime - startTime) + "\n");

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Running Brick Sort");
            Bricksort bricksorter = new Bricksort(numThreads);
            startTime = System.nanoTime();
            bricksorter.bricksort(brickList);
            endTime = System.nanoTime();
            brickResult.add(endTime - startTime);
//            System.out.println("Brick timing (ns): " + (endTime - startTime) + "\n");


            System.out.println("Running Bitonic Sort");
            Bitonicsort bitonicsorter = new Bitonicsort(numThreads);
            startTime = System.nanoTime();
            bitonicsorter.bitonicsort(bitonicList, simpleLogn(listSize));
            endTime = System.nanoTime();
            bitonicResult.add(endTime - startTime);
//            System.out.println("Bitonic timing (ns): " + (endTime - startTime) + "\n");

            listSize*=2;
            numThreads*=2;
        }

        System.out.println("Results in order of: merge, quick, radix, bitonic, brick");
        System.out.println(mergeResult);
        System.out.println(quickResult);
        System.out.println(radixResult);
        System.out.println(bitonicResult);
        System.out.println(brickResult);
    }

    static int simpleLogn(int listSize){
        int counter = 0;
        while(listSize != 1){
            listSize = listSize >> 1;
            counter++;
        }
        return counter;
    }
}
