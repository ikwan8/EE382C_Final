
import EE382C_Final.MergeSort;
import EE382C_Final.Radixsort;
import EE382C_Final.QuickSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by Darren on 11/9/2016.
 */
public class Main {

    static int listSize = 8;  //must be power of 2
    static int numThreads = 8;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ArrayList<Integer> mergeList = new ArrayList();
        ArrayList<Integer> quickList = new ArrayList();
        ArrayList<Integer> radixList = new ArrayList();
        ArrayList<Integer> bitonicList = new ArrayList();
        ArrayList<Integer> brickList = new ArrayList();
        Random generator = new Random(50);

        for(int i = 0; i < listSize; i++){
            int randNum = (int) Math.ceil(generator.nextDouble() * 100);
            mergeList.add(randNum);
            quickList.add(randNum);
            radixList.add(randNum);
            bitonicList.add(randNum);
            brickList.add(randNum);
        }

        System.out.println("Test List:\n" + mergeList + "\n");

        System.out.println("Running Merge Sort");

        MergeSort mergesorter = new MergeSort(numThreads);
        try {
            mergesorter.mergesort(mergeList);
            System.out.println("Merge Sort Results:\n"+mergeList + "\n");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Running quick sort");
        QuickSort quicksorter = new QuickSort(numThreads);
        try {
            quicksorter.quicksort(quickList, numThreads);
            Thread.sleep(1000);
            System.out.println("quick Sort Results:\n"+quickList + "\n");
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Running Radix Sort");
        Radixsort radixsorter = new Radixsort(numThreads);
        try {
            ArrayList<Integer> radixResults = radixsorter.radixsort(radixList);
            System.out.println("Radix sort Results:\n"+ radixResults + "\n");

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Running Brick Sort");
        Bricksort bricksorter = new Bricksort(numThreads);
        bricksorter.bricksort(brickList);
        System.out.println("Brick Result:\n" + brickList + "\n");



        System.out.println("Running Bitonic Sort");
        Bitonicsort bitonicsorter = new Bitonicsort(numThreads);
        bitonicsorter.bitonicsort(bitonicList, simpleLogn(listSize));
        System.out.println("Bitonic Result:\n" + bitonicList + "\n");



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
