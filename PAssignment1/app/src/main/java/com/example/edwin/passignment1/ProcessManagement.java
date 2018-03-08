package com.example.edwin.passignment1;

/*Programming Assignment 1
*Authors : Chen Xiaoming (ID: 1002179) , Hao Weining (ID: 1002195?)
*Date : 08/03/2018 */

import android.annotation.TargetApi;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessManagement {

    //set the working directory
    private static File currentDirectory = new File(System.getProperty("user.dir"));


    //This function checks if the graph is possible to be run, i.e. when a process requires an input file
    // which was never created(doesn't exist), the graph would be treated as not eligible.

    public static boolean checkEligibleGraph(ArrayList<ProcessGraphNode> processGraphNodes){

        ArrayList<File> input = new ArrayList<>();
        ArrayList<File> output = new ArrayList<>();



        for(int i = 0; i < processGraphNodes.size(); i++){
            if(!processGraphNodes.get(i).getInputFile().getName().equals("stdin")){
                input.add(processGraphNodes.get(i).getInputFile());
            }
            output.add(processGraphNodes.get(i).getOutputFile());
        }

        System.out.println("testing");
        System.out.print("input files required: ");
        System.out.println(input);
        System.out.print("output files generated: ");
        System.out.println(output);

        for(File f:input){
            if(!output.contains(f)){
                File tempFile = new File("outputFiles/" + f.getName());
                if(!tempFile.exists()){
                    return false;
                }
            }
        }
        return true;


    }



    public static Object lock = new Object();
    @TargetApi(26)
    public static void main(String[] args) throws InterruptedException {
        //set the instructions file(note : moved it inside psvm so that it could take arguments rather than just letting
        // users input inside the code)
        File instructionSet = new File(args[0]);

        //parse the instruction file and construct a data structure, stored inside ProcessGraph class
        //System.out.println(currentDirectory.getAbsoluteFile());
        ParseFile.generateGraph(instructionSet);

        //Runnable keeps track of all files whose isRunnable is true but isExecuted is false
        ArrayList<Integer> Runnable = new ArrayList<>();

        // Print the graph information
        ProcessGraph.printGraph();

        if(!checkEligibleGraph(ProcessGraph.nodes)){
            System.out.println("This Graph is not able to be completed due to unresolved data dependencies, error code 1");
            return;

        }




        // TODO: WRITE YOUR CODE

        // Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run
        for(int i = 0; i < ProcessGraph.nodes.size(); i++){
            if(!ProcessGraph.nodes.get(i).isExecuted()){
                if(ProcessGraph.nodes.get(i).isRunnable()){
                    Runnable.add(ProcessGraph.nodes.get(i).getNodeId());
                }
            }
        }


        // check if all the nodes are executed
        if(Runnable.isEmpty()){
            System.out.println("File does not have a root");
            return;
        }
        // TODO: WRITE YOUR CODE


        // TODO: WRITE YOUR CODE


        // TODO: WRITE YOUR CODE
        while(!Runnable.isEmpty()) {

            //Loop through all runnable nodes, executing them and freeing up their children's dependencies.
            for (int i = 0; i < Runnable.size(); i++) {

                ProcessBuilder pb = new ProcessBuilder();
                pb.directory(currentDirectory);

                String[] Command = ProcessGraph.nodes.get(Runnable.get(i)).getCommand().split(" ");
                if(Command[0].equals("wc") | Command[0].equals("cat")){
                    for(int j = 1; j < Command.length; j++){
                        if(Command[j].charAt(0) != '-'){
                            Command[j] = "outputFiles/" + Command[j];
                        }
                    }



                }
                pb.command(Command);


                //Redirect Output and inputFiles from stdin and stdout
                if(!ProcessGraph.nodes.get(Runnable.get(i)).getInputFile().getName().equals("stdin")){
                    pb.redirectInput(new File("outputFiles/" + ProcessGraph.nodes.get(Runnable.get(i)).getInputFile().getName()));
                }
                if(!ProcessGraph.nodes.get(Runnable.get(i)).getInputFile().getName().equals("stdout")){
                    pb.redirectOutput(new File("outputFiles/" + ProcessGraph.nodes.get(Runnable.get(i)).getOutputFile().getName()));
                }

                //Execute the nodes
                try {

                    //Checking if all required files of a process are available
                    // this only resolves the issues with input files, cat command input files, and wc command input files.
                    List<String> command = pb.command();
                    if(command.get(0).equals("cat")){

                        if(command.size()>1){
                            for(int j = 1; j < command.size(); j++){
                                File tempFile = new File(command.get(j));
                                if(!tempFile.exists()){
                                    System.out.println("This Graph is not able to be completed due to unresolved data dependencies, error code 2");
                                    System.out.println("Problematic Node: " + Runnable.get(i));
                                    return;
                                }
                            }
                        }
                    }else if(command.get(0).equals("wc")){
                        if(command.size()>1){
                            for(int j = 1; j < command.size(); j++){
                                if(command.get(j).charAt(0)!='-'){
                                    File tempFile = new File(command.get(j));
                                    System.out.println(tempFile);
                                    if(!tempFile.exists()){
                                        System.out.println("This Graph is not able to be completed due to unresolved data dependencies, error code 3");
                                        return;
                                    }
                                }
                            }
                        }
                    }


                    //Finally executing the node, knowing that it fulfills all dependencies
                    Process p = pb.start();
                    p.waitFor();




                } catch (IOException e) {
                    e.printStackTrace();
                }

                //set & show executed
                ProcessGraph.nodes.get(Runnable.get(i)).setExecuted();
                System.out.println("Process " + Runnable.get(i) + " is executed");

                //mark all the runnable nodes
                for (int j = 0; j < ProcessGraph.nodes.get(Runnable.get(i)).getChildren().size(); j++) {
                    if(ProcessGraph.nodes.get(Runnable.get(i)).getChildren().get(j).allParentsExecuted()){
                        ProcessGraph.nodes.get(Runnable.get(i)).getChildren().get(j).setRunnable();
                    }
                }
                //remove node from Runnable when it is already executed
                Runnable.remove(new Integer(Runnable.get(i)));
            }



            //Add all the nodes tagged with isRunnable() returning true to Runnable
            for(int i = 0; i < ProcessGraph.nodes.size(); i++){
                if(!ProcessGraph.nodes.get(i).isExecuted()){
                    if(ProcessGraph.nodes.get(i).isRunnable()){
                        if(!Runnable.contains(i)){
                            Runnable.add(i);
                        }


                    }
                }
            }
        }
        System.out.println("All process finished successfully");
    }

}