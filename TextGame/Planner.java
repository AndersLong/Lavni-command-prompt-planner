/*
	Author: Anders Long
	Last modified: 2/11/2020
	Synopsis
*/

//this is a test for git

import java.io.*;
import java.util.*;

class Planner{
	String name  = "Lavni";
	double version = 1.1;
	Scanner scan=new Scanner(System.in);
	private String[] menuChoices={"view planner",
				      "add to planner",
				      "checkbox",
				      "organize planner",
				      "manuel",
				      "quit?"};

	public static void main(String[] args){
		new Planner();
	}


	/**
		This is the constructor, it is responsible for starting the
		loop function

	*/

	public Planner(){
		printDiv(String.format("Welcome to %s version %.1f!", name, version));
		loop();
		scan.close();
	}

	public String[] getArrayFromFile(String path){
		ArrayList<String> strs=new ArrayList<>();
		try(Scanner s=new Scanner(new File(path))){
			while(s.hasNextLine()){
				strs.add(s.nextLine());
			}
			return getStrArrFromList(strs);
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
		The loop function embodies the entirety of the program
		by "re-offering" choices until the exit choice is chosen
	*/

	public void loop(){
		int choice=getChoice(scan,"What would you like to do!",menuChoices);
		while(choice!=5){
			switch(choice){
				case -1:
					printDiv("sorry! that's not valid input you dork!");
					break;
				case 0:
					printPlanner();
					break;
				case 1:
					addToPlanner(scan);
					break;
				case 2:
					removeFromPlanner(scan);
					break;
				case 3:
					orgPlan(scan);
					break;
				case 4:
					displayMan();
					break;
			}
			choice=getChoice(scan,"what would you like to do!",menuChoices);
			if(choice==-1){
				printDiv("sorry, not valid! exiting now");
				break;
			}
		}
	}

	/**
		Prints a prompt, and all choices. and until a valid choice is given,
		it does not return the value
	*/

	public int getChoice(Scanner scan,String prompt,String[] choices){
		printDiv(prompt);
		for(int i=0;i<choices.length;i++){
			System.out.printf("%d---->%s\n",i,choices[i]);
		}
		int result=-2;
		while(!isValid(choices.length,result)){
			try{
				result=scan.nextInt();
			}catch(InputMismatchException ime){
				return -1;
			}
		}
		return result;

	}

	/*
		Returns true if a given choice is within the allowed range
	*/

	public boolean isValid(int rangeOfChoices, int choice){
		for(int i=0;i<rangeOfChoices;i++){
			if(choice==i){
				return true;
			}
		}
		return false;
	}


	public ArrayList<String> getPlanner(){
		ArrayList<String> checkBoxes=new ArrayList<>();
		try(Scanner s=new Scanner(new File("saveFile.txt"))){
			while(s.hasNextLine()){
				String line=s.nextLine();
				checkBoxes.add(line);
			}
			return checkBoxes;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public void printPlanner(){
		ArrayList<String> strs=getPlanner();
		for(String str:strs){
			System.out.printf("%s\n",str);
		}
		return;
	}

	public void addToPlanner(Scanner scan){
		printPlanner();
		ArrayList<String> strs=getPlanner();
		String inp=scan.nextLine();
		while(inp.isBlank()){
			inp=scan.nextLine();
		}
		if(!inp.isBlank()){
			strs.add(inp);
		}
		try(PrintWriter pw=new PrintWriter(new File("saveFile.txt"))){
			for(String str:strs){
				pw.write(str+"\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void orgPlan(Scanner scan){
		ArrayList<String> strs=getPlanner();
		int choice=getChoice(scan,"Which checkbox would you like to move",getStrArrFromList(strs));
		if(choice==-1){
			printDiv("sorry. that isn't valid input");
			return;
		}
		String op=strs.get(choice);
		strs.remove(strs.indexOf(op));
		choice=getChoice(scan,"Where would you like to move this before?",getStrArrFromList(strs));
		if(choice==-1){
			printDiv("sorry. that isn't valid input");
			return;
		}
		strs.add(choice,op);
		try(PrintWriter pw=new PrintWriter(new File("saveFile.txt"))){
			for(String str:strs){
				pw.write(str+"\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public String[] getStrArrFromList(ArrayList<String> strs){
		Object[] objs=strs.toArray();
		String[] arr=new String[strs.size()];
		for(int i=0;i<objs.length;i++){
			arr[i]=(String)objs[i];
		}
		return arr;
	}

	public void displayMan(){
		try(Scanner s=new Scanner(new File("man.txt"))){
			while(s.hasNextLine()){
				System.out.println(s.nextLine());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		String[] ops={"y","n"};
		int choice=-2;
		while(choice!=0 && !isValid(ops.length,choice)){
			choice=getChoice(scan,"would you like to exit?",ops);
		}
	}

	public void removeFromPlanner(Scanner scan){
		printPlanner();
		ArrayList<String> strs=getPlanner();
		String inp=scan.nextLine();
		while(inp.isBlank()){
			inp=scan.nextLine();
		}
		if(strs.contains(inp)){
			strs.remove(strs.indexOf(inp));
			try(PrintWriter pw=new PrintWriter(new File("saveFile.txt"))){
				for(String str:strs){
					pw.write(str+"\n");
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			printDiv(String.format("good job on completing %s!",inp));
		}else{
			
			printDiv(String.format("sorry! %s isn't on the list, but good job doing it!",inp));
		}

	}
	/*
	   below are all the subclasses used in our simulation

	*/

	public void printDiv(String message){
		System.out.println();
		for(int i=0;i<=message.length();i++){
			System.out.printf("-");
		}
		System.out.println();
		System.out.printf("%s!\n",message);
		for(int i=0;i<=message.length();i++){
			System.out.printf("-");
		}
		System.out.println();
	}


}


