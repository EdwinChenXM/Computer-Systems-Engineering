package com.example.edwin.multithread;

/**
 * Created by edwin on 15/2/18.
 */

//notes: made the meanMultiThread class static
//side notes: in android studio, use "Edit Configuration to pick number of threads"





import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MeanThread {

    static ArrayList<Double> means = new ArrayList<>();
    //a function to partition an array to N subArrays (no need to consider conditions with)
    public static int[][] partition(int[] arr, int n){

        int length = arr.length/n;
        int[] temp = new int[length];
        int[][] result = new int[n][length];
        for(int i = 0; i < arr.length; i++) {
            if(i%length == 0) {
                if(i != 0) {
                    result[i/length-1] = temp;
                    //System.out.print(i + ", ");
                    //System.out.println(i/n-1);
                }
                temp = new int[length];
            }
            if(i == arr.length-1){
                result[i/length] = temp;
            }



                temp[i%length] = arr[i];
        }
        return result;


    }


    //a function to convert arraylists of Integer to array of ints
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }


    //a function to print two dimensional arrays
    public static void printArr(int[][] arr){
        for(int i = 0; i < arr.length; i++){
            System.out.print("[");
            for(int j = 0; j < arr[i].length; j++){
                System.out.print(arr[i][j]);
                System.out.print(", ");
            }
            System.out.println("]");
        }


    }

    // Function to compute the mean of an arraylist
    public static double computeMean(ArrayList<Integer> arrList){
        double sum = 0;
        for(int i = 0; i < arrList.size(); i++){
            //System.out.println(arrList.get(i));
            //System.out.println(sum);
            sum += (double)arrList.get(i)/arrList.size();
        }
        return sum;
    }

    // Function to box an int[] to Integer[]

    public static Integer[] box(int[] arr){
        Integer[] arrNew = new Integer[arr.length];
        int j = 0;
        for(int i:arr){
            arrNew[j++] = i;
        }

        return arrNew;
    }












    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        // TODO: read data from external file and store it in an array
        // Note: you should pass the file as a first command line argument at runtime.


        ArrayList<Integer> arrList = new ArrayList<>();
        ArrayList<Integer> arrTest = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27
                , 28, 29, 30, 31, 32}));
        //"Sources/input.txt"
        BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
        int NumOfThread = Integer.valueOf(args[1]);// this way, you can pass number of threads as


        // a second command line argument at runtime.
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                Scanner scanner = new Scanner(line);

                while (scanner.hasNextInt()) {
                    int nextInt = scanner.nextInt();
                    arrList.add(nextInt);
                }


            }
            //System.out.println(arr.toString());
            reader.close();

            // TODO: partition the array list into N subArrays, where N is the number of threads

            int[] arr = convertIntegers(arrList);

            int[][] partitioned = partition(arr, NumOfThread);


            //printArr(partitioned);

            // TODO: start recording time
            final long startTime = System.currentTimeMillis();



            // TODO: create N threads and assign subArrays to the threads so that each thread computes mean of
            // its repective subarray. For example,
            ArrayList<MeanMultiThread> threads = new ArrayList<>();

            for (int i = 0; i < partitioned.length; i++){
                threads.add(new MeanThread.MeanMultiThread(new ArrayList<Integer>(Arrays.asList(box(partitioned[i])))));
                //System.out.println(threads.get(i).list);
            }



            // TODO: start each thread to execute your computeMean() function defined under the run() method
            //so that the N mean values can be computed. for example,
            for(int j = 0; j < threads.size(); j++){
                threads.get(j).start();
            }
            for(int k = 0; k < threads.size(); k++){
                threads.get(k).join();
            }


            // TODO: show the N mean values
            for(int l = 0; l < means.size(); l++){
                System.out.println("Temporal mean value of thread " + l + " is " + means.get(l) + ".");
            }


            // TODO: compute the global mean value from N mean values.
            double globMean = 0.0;
            int count = 0;
            for(int i = 0; i < means.size(); i++){
                //how could means.get(i) ever become null????
                if(means.get(i)!=null){
                    globMean += means.get(i);
                    count++;
                }




            }
            globMean = globMean/count;

            System.out.println("The global mean value is " + globMean + ".");


            // TODO: stop recording time and compute the elapsed time

            final long endTime = System.currentTimeMillis();
            System.out.println("Time Spent: " + (endTime-startTime) + "ms");





        } catch (java.io.IOException e) {

            e.printStackTrace();

        }


        // define number of threads






//
//
//
//
//











//        // TODO: store the temporal mean values in a new array so that you can use that
//        /// array to compute the global mean.
//
//
//
//
//



//
//
//

//
//    }
//}
////Extend the Thread class
    }
static class MeanMultiThread extends Thread {
    private ArrayList<Integer> list;
    private double mean;
    MeanMultiThread(ArrayList<Integer> array) {
        list = array;
    }
    public double getMean() {
        return mean;
    }
    public void run() {
        // TODO: implement your actions here, e.g., computeMean(...)
        mean = computeMean(list);
        //System.out.println(mean);
        means.add(mean);



    }
}


}