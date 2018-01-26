import java.io.*;
import java.lang.ProcessBuilder;
import java.util.Arrays;
import java.util.ArrayList;


public class lab1 {
	
	ArrayList history = new ArrayList();;
	
	public lab1(){
		ArrayList history = new ArrayList();
	}
	
	public static boolean contains(File[] strs, String str){
		
		for(int i = 0; i < strs.length; i++){
			if(strs[i].getAbsolutePath().equals(str)){
				return true;
			}
		}
		return false;
		
	}
	
	public void takeHistory(String cmd){
		if(this.history.size() >= 15){
			return;
		}
		if(!this.history.contains(cmd)){
			this.history.add(cmd);
		}
	}
	
	public static void reversePrint(ArrayList arr){
		for(int i = arr.size()-1; i >= 0; i--){
			System.out.println(arr.get(i));
		}		
	}
	
	
	
	
	public static void main(String[] args) throws java.io.IOException {
		lab1 context = new lab1();
		
		
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String[] command = new String[2];
		String homePath = System.getProperty("user.home");
		File homeDir = new File(homePath);
		String currentPath = System.getProperty("user.dir");
		
		
		while (true) {
			// read what the user entered
				
			System.out.print(currentPath);
			System.out.print(">");
			commandLine = console.readLine();
			command  = commandLine.split(" ");
			
			
			// TODO: adding a history feature

			// if the user entered a return, just loop again
			if (commandLine.equals("")) {
				continue;
			}else if(commandLine.equals("history")){
				reversePrint(context.history);
				continue;
			}
			
			
//			for(int i = 0; i < 2; i ++){
//				System.out.println(command[i]);
//				System.out.println("fuck");
//			}
			
			
			
			
			
			
			// TODO: modifying the shell to allow changing directories

			
			if(command[0].equals("cd")){
				String[] temp = new String[]{"ls"};
				
				
				ProcessBuilder pb = new ProcessBuilder(temp);
				File currentDir = new File(currentPath);
				if(currentDir == null) currentDir = new File("");
				
				File parentDir = new File(currentDir.getAbsolutePath()).getParentFile();
				
				try{
					if(command[1].equals("..")){
						pb.directory(parentDir);
						currentPath = pb.directory().getAbsolutePath();
						//System.out.println(parentDir);
						context.takeHistory(commandLine);
						continue;
					}else if(command[1].equals(".")){
						//System.out.println(currentDir);
						context.takeHistory(commandLine);
						continue;
					}else if(command[1].equals("~")){
						pb.directory(homeDir);
						currentPath = pb.directory().getAbsolutePath();
						//System.out.println(homeDir);
						context.takeHistory(commandLine);
						continue;
					}else{
						int i = 0;
						String[] paths = command[1].split("/");
						try{
							while(lab1.contains(currentDir.listFiles(),currentPath+"/"+paths[i])){
								currentDir = new File(currentPath+"/"+paths[i]);
								currentPath = currentDir.getAbsolutePath();
								i++;
							}
							currentDir = new File(command[1]);
							//currentPath = currentDir.getAbsolutePath(); ??? why is this wrong
							currentPath = currentDir.toString();
							System.out.println(currentPath);
							context.takeHistory(commandLine);
							continue;
						}catch(ArrayIndexOutOfBoundsException e){
							context.takeHistory(commandLine);
							continue;
						}
						
					}
					
				}catch(ArrayIndexOutOfBoundsException e){
					System.out.println("Please enter a valid directory");
					continue;
				}
				
				
				
				
			}
			
			// TODO: creating the external process and executing the command in that process
			
			
			
			ProcessBuilder pb = new java.lang.ProcessBuilder(command);
			
			
			try{
				Process p = pb.start();
				
				BufferedReader message = new BufferedReader(new InputStreamReader(p.getInputStream()));
			for(String line; (line = message.readLine()) != null;){
				System.out.println(line);
			}
				context.takeHistory(command[0]);
			}catch(java.io.IOException e){
				System.out.println("invalid input");
			}
		
		
		}
	}
}