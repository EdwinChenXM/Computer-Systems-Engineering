// package Week5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class FileOperation {
    private static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws java.io.IOException {

        String commandLine;

        BufferedReader console = new BufferedReader
                (new InputStreamReader(System.in));

        while (true) {
            // read what the user entered
            System.out.print("jsh>");
            commandLine = console.readLine();

            // clear the space before and after the command line
            commandLine = commandLine.trim();

            // if the user entered a return, just loop again
            if (commandLine.equals("")) {
                continue;
            }
            // if exit or quit
            else if (commandLine.equalsIgnoreCase("exit") | commandLine.equalsIgnoreCase("quit")) {
                System.exit(0);
            }

            // check the command line, separate the words
            String[] commandStr = commandLine.split(" ");
            ArrayList<String> command = new ArrayList<String>();
            for (int i = 0; i < commandStr.length; i++) {
                command.add(commandStr[i]);
            }

            // TODO: implement code to handle create here
            switch (command.get(0)) {
                case "create":
                    File file = new File(currentDirectory, command.get(1));
                    file.createNewFile();
                    break;
                case "delete":
                    File file1 = new File(currentDirectory, command.get(1));
                    file1.delete();
                    break;
                case "display":
                    Java_cat(currentDirectory,command.get(1));
                    break;
                case "list":
                    // side notes: we are doing this apparently because my computer is giving me errors if I don't add a "java"
                    // argument in front of all arguments

                    switch(command.size()){
                        case 1: Java_ls(currentDirectory,"","");
                        break;
                        case 2: Java_ls(currentDirectory,command.get(1),"");
                        break;
                        case 3: Java_ls(currentDirectory,command.get(1),command.get(2));
                        break;
                    }
                break;
                case "find":
                    Java_find(currentDirectory,command.get(1));
                break;
                case "tree" :
                    switch(command.size()){
                        case 1: Java_tree(currentDirectory,-1,"",-1);
                        break;
                        case 2: Java_tree(currentDirectory,Integer.parseInt(command.get(1)),"",Integer.parseInt(command.get(1)));
                        break;
                        case 3: Java_tree(currentDirectory,Integer.parseInt(command.get(1)),command.get(2),Integer.parseInt(command.get(1)));
                        break;
                    }



            }
            // TODO: implement code to handle delete here

            // TODO: implement code to handle display here

            // TODO: implement code to handle list here

            // TODO: implement code to handle find here

            // TODO: implement code to handle tree here

            // other commands
            ProcessBuilder pBuilder = new ProcessBuilder();


            switch(command.size()){
                case 1: {pBuilder.command("java",command.get(0));}
                break;
                case 2: {pBuilder.command("java",command.get(0),command.get(1));}
                break;
                case 3: {pBuilder.command("java",command.get(0),command.get(1),command.get(2));}
                break;

            }



            pBuilder.directory(currentDirectory);
            try {
                Process process = pBuilder.start();
                // obtain the input stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                // read what is returned by the command
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);

                // close BufferedReader
                br.close();
            }
            // catch the IOexception and resume waiting for commands
            catch (IOException ex) {
                System.out.println(ex);
                continue;
            }
        }
    }

    /**
     * Create a file
     *
     * @param dir  - current working directory
     * @param name - name of the file to be created
     */
    public static void Java_create(File dir, String name) {
        // TODO: create a file
        try{
            File file = new File(dir,name);
            file.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Delete a file
     *
     * @param dir  - current working directory
     * @param name - name of the file to be deleted
     */
    public static void Java_delete(File dir, String name) {
        // TODO: delete a file

            File file = new File(dir,name);
            file.delete();

    }

    /**
     * Display the file
     *
     * @param dir  - current working directory
     * @param name - name of the file to be displayed
     */
    public static void Java_cat(File dir, String name) {
        // TODO: display a file
        File file2 = new File(dir, name);
        try {
            FileReader fileReader = new FileReader(file2);
            BufferedReader in = new BufferedReader(fileReader);
            String line;
            while ((line = in.readLine()) != null) {
              System.out.println(line);
            }
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Function to sort the file list
     *
     * @param list        - file list to be sorted
     * @param sort_method - control the sort type
     * @return sorted list - the sorted file list
     */
    private static File[] sortFileList(File[] list, String sort_method) {
        // sort the file list based on sort_method
        // if sort based on name
        if (sort_method.equalsIgnoreCase("name")) {
            Arrays.sort(list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return (f1.getName()).compareTo(f2.getName());
                }
            });
        } else if (sort_method.equalsIgnoreCase("size")) {
            Arrays.sort(list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.length()).compareTo(f2.length());
                }
            });
        } else if (sort_method.equalsIgnoreCase("time")) {
            Arrays.sort(list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                }
            });
        }
        return list;
    }

    /**
     * List the files under directory
     *
     * @param dir            - current directory
     * @param display_method - control the list type
     * @param sort_method    - control the sort type
     */
    public static void Java_ls(File dir, String display_method, String sort_method) {
        // TODO: list files
        //get file list
        if(!sort_method.equals("")) {

            File[] myFiles = sortFileList(dir.listFiles(), sort_method);
            for (File file : myFiles) {
                System.out.print(file.getName());
                System.out.print("    ");
                System.out.print(file.length());
                System.out.print("    ");
                System.out.println(new Date(file.lastModified()));
            }
        }else{
            if(display_method.equals("property")) {
                File[] list = dir.listFiles();

                for (File file : list) {
                    System.out.print(file.getName());
                    System.out.print("    ");
                    System.out.print(file.length());
                    System.out.print("    ");
                    System.out.println(new Date(file.lastModified()));
                }
            }else{
                File[] list = dir.listFiles();

                for (File file : list) {
                    System.out.println(file.getName());
                }
            }
        }
    }

    /**
     * Find files based on input string
     *
     * @param dir  - current working directory
     * @param name - input string to find in file's name
     * @return flag - whether the input string is found in this directory and its subdirectories
     */
    public static boolean Java_find(File dir, String name) {
        boolean flag = false;
        // TODO: find files
        File[] allFiles = dir.listFiles();
        for(File file:allFiles){

            if(file.isDirectory()){
                Java_find(file,name);
            }else if(file.getName().toLowerCase().contains(name.toLowerCase())){
                String path = file.getAbsolutePath();
                System.out.println(path);
                flag = true;
            }



        }




        return flag;
    }








    /**
     * Print file structure under current directory in a tree structure
     *
     * @param dir         - current working directory
     * @param depth       - maximum sub-level file to be displayed
     * @param sort_method - control the sort type
     * @param depthConstant - self added constant to keep track of level of each file, in order to print out the tree
     */
    public static void Java_tree(File dir, int depth, String sort_method, int depthConstant) {
        // TODO: print file tree


        int level = depthConstant - depth;


        // this is a little trick here, depthConstant equals -1 is the case we use to represent no input of depth
        //Execution Logic: if depthConstant == -1 we'll never need any other things but just to print everything,
        //therefore this piece of code is put on the very top to prevent other executions
        if(depthConstant == -1){

            File[] allFiles = dir.listFiles();
            for(File file : allFiles){
                if(file.isDirectory()){
                    printTreeHelper(level);
                    System.out.println(file.getName());
                    Java_tree(file,depth-1, sort_method,depthConstant);
                }else {
                    printTreeHelper(level);
                    System.out.println(file.getName());
                }
            }
        }else {
            //depth == 0 certainly means to return and this is on top of everything else
            if (depth == 0) {
                return;
            } else {
                //SORT at every level if needed
                File[] allFiles = dir.listFiles();
                if (!sort_method.equals("")) {
                    allFiles = sortFileList(allFiles, sort_method);
                }

                for (File file : allFiles) {
                    if (file.isDirectory()) {
                        printTreeHelper(level);
                        System.out.println(file.getName());
                        Java_tree(file, depth - 1, sort_method, depthConstant);
                    } else {
                        printTreeHelper(level);
                        System.out.println(file.getName());
                    }
                }
            }
        }
    }


    // TODO: define other functions if necessary for the above functions
    // Helper Method to help us print the "|-" tree symbols
    public static void printTreeHelper(int level){
        if(level != 0){
            for(int i = 0; i < level; i++){
                System.out.print("  ");
            }
            System.out.print("|-");
        }
    }

}