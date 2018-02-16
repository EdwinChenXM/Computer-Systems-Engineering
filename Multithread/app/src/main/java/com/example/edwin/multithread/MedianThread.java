package com.example.edwin.multithread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import static com.example.edwin.multithread.MeanThread.box;
import static com.example.edwin.multithread.MeanThread.convertIntegers;
import static com.example.edwin.multithread.MeanThread.partition;


//notes: made the meanMultiThread class static
//side notes: in android studio, use "Edit Configuration to pick number of threads"



public class MedianThread {

    //function to merge two already sorted arraylists
    public static ArrayList<Integer> sortMerge(ArrayList<Integer> arr1,ArrayList<Integer> arr2){
        int i  = 0;
        int j = 0;
        ArrayList<Integer> newArr = new ArrayList<>();
        while(i < arr1.size()){

            if(i >= arr1.size()){
                //System.out.println(i);
                for(int k = j; k < arr2.size(); k++){
                    newArr.add(arr2.get(k));
                }
                break;
            }

            if(j >= arr2.size()){
                for(int l = i; l < arr1.size(); l++){
                    newArr.add(arr2.get(l));
                }
                break;

            }

            while(j < arr2.size()){
                if(i >= arr1.size()){
                    //System.out.println(i);
                    for(int k = j; k < arr2.size(); k++){
                        newArr.add(arr2.get(k));
                    }
                    break;
                }

                if(j >= arr2.size()){
                    for(int l = i; l < arr1.size(); l++){
                        newArr.add(arr2.get(l));
                    }
                    break;

                }

                if(arr1.get(i) >= arr2.get(j)){
                    newArr.add(arr2.get(j));
                    j++;
                }else{
                    newArr.add(arr1.get(i));
                    i++;
                }

            }

        }
        return newArr;

        //return arr1;
    }

    //Recursively merge arraylist into one final arraylist
    public static ArrayList<Integer> recursiveMerge(ArrayList<ArrayList<Integer>> allLists){
        if(allLists.size() == 1){
            return allLists.get(0);
        }

        ArrayList<ArrayList<Integer>> newList = new ArrayList<>();

        for(int i = 0; i < allLists.size()-1; i+=2){
            ArrayList<Integer> aList = sortMerge(allLists.get(i),allLists.get(i+1));
            //System.out.println(aList.size());
            newList.add(deepCopy(aList));
        }
        return recursiveMerge(newList);
//
       // return allLists.get(0);

    }

    public static ArrayList<ArrayList<Integer>> distribute(ArrayList<Integer> arr){
        ArrayList<Integer> arrList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> arrLists = new ArrayList<>();
        for(int i:arr){
            arrList.add(i);
            arrLists.add(deepCopy(arrList));
            arrList.clear();
        }

        return arrLists;

    }


    public static ArrayList<Integer> deepCopy(ArrayList<Integer> arr){
        ArrayList<Integer> newArr = new ArrayList<>();
        for(int i:arr){
            newArr.add(i);
        }
        return newArr;
    }





    public static void main(String[] args) throws InterruptedException, FileNotFoundException  {
        // TODO: read data from external file and store it in an array
        // Note: you should pass the file as a first command line argument at runtime.
        ArrayList<Integer> arrList = new ArrayList<>();
        int NumOfThread = Integer.valueOf(args[1]);// this way, you can pass number of threads as
        // a second command line argument at runtime.
        BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
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
            System.out.println("initial length: " +arr.length);
            int[][] partitioned = partition(arr, NumOfThread);


            //printArr(partitioned);

            // TODO: start recording time
            final long startTime = System.currentTimeMillis();



            // TODO: create N threads and assign subArrays to the threads so that each thread computes mean of
            // its repective subarray. For example,
            ArrayList<MedianMultiThread> threads = new ArrayList<>();

            for (int i = 0; i < partitioned.length; i++){
                threads.add(new MedianMultiThread(new ArrayList<Integer>(Arrays.asList(box(partitioned[i])))));
                //System.out.println(threads.get(i).list);
            }



            // TODO: start each thread to execute your mergeSort() function defined under the run() method
            //so that the N mean values can be computed. for example,
            for(int j = 0; j < threads.size(); j++){
                threads.get(j).start();
            }
            for(int k = 0; k < threads.size(); k++){
                threads.get(k).join();
            }

            // TODO: use any merge algorithm to merge the sorted subarrays and store it to another array, e.g., sortedFullArray.




            ArrayList<ArrayList<Integer>> newLists = new ArrayList<>();
            for(int i = 0; i < threads.size()-1; i+=2){
                ArrayList<Integer> newList = sortMerge(threads.get(i).list,threads.get(i+1).list);
                //System.out.println(newList.size());
                newLists.add(deepCopy(newList));
            }

            ArrayList<Integer> sortedFinal = recursiveMerge(newLists);

//            System.out.print("Size: ");
//            System.out.println(sortedFinal.size());


            //TODO: get median from sortedFullArray

            double median = computeMedian(sortedFinal);


            // TODO: printout the final sorted array
            System.out.print("final sorted array: ");
            System.out.println(sortedFinal);


            // TODO: printout median


            System.out.println("median is: " + median);


            // TODO: stop recording time and compute the elapsed time

            final long endTime = System.currentTimeMillis();
            System.out.println("Time Spent: " + (endTime-startTime) + "ms");






        } catch (java.io.IOException e) {

            e.printStackTrace();

        }
        // define number of threads
    }

    public static double computeMedian(ArrayList<Integer> inputArray) {
        //TODO: implement your function that computes median of values of an array
        return inputArray.get(inputArray.size()/2);


    }

}

// extend Thread
class MedianMultiThread extends Thread {
    public ArrayList<Integer> list;

    public ArrayList<Integer> getInternal() {
        return list;
    }

    MedianMultiThread(ArrayList<Integer> array) {
        list = array;
    }

    public void run() {
        // called by object.start()

        int[] listArr = convertIntegers(list);

        //these lines(273-274) of code are used for radix sort
//        Radix.radixsort(listArr,list.size());




        //these lines(279-280) of code is used for merge sort
        MergeSort model = new MergeSort();
        model.sort(listArr,0,listArr.length-1);

        list.clear();
        for(int i : listArr){
            list.add(i);

        }

    }

    // TODO: implement merge sort here, recursive algorithm
    //can also use radix sort instead to improve performance
}