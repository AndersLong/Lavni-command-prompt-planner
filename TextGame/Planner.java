/*
	Author: Anders Long
	Last modified: 2/11/2020
	Synopsis
*/

//this is nothera test for git

import java.io.*;
import java.util.*;

class Planner implements Runnable{
	String name  = "Lavni";
	double version = 1.2;
	Scanner scan=new Scanner(System.in);
	private String[] menuChoices={"view planner",
				      "add to planner",
				      "checkbox",
				      "organize planner",
				      "manuel",
				      "quit?"};

	Thread thread;

	public static void main(String[] args){
		new Planner();
	}


	/**
		This is the constructor, it is responsible for starting the
		loop function

	*/

	public Planner(){
		printDiv(String.format("Welcome to %s version %.1f!", name, version));
		thread=new Thread(this);
		thread.start();
	}

	/*
		gets a string array from a file with a given filepath. This function
		utilizes another function getStrArrFromList with converts a dynamic
		ArrayList to a string array
	*/

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
		The loop function embodies the entirety of the programm
		by "re-offering" choices until the exit choice is chosen
	*/

	public void run(){
		int choice=getChoice(scan,"What would you like to do!",menuChoices);
		while(choice!=5){
			switch(choice){
				case -1:
					proceed(scan);
					break;
				case 0:
					printNewPage();
					printPlanner();
					proceed(scan);
					break;
				case 1:
					printNewPage();
					addToPlanner(scan);
					break;
				case 2:
					printNewPage();
					removeFromPlanner(scan);
					break;
				case 3:
					printNewPage();
					orgPlan(scan);
					break;
				case 4:
					printNewPage();
					displayMan(scan);
					break;
			}
			choice=getChoice(scan,"what would you like to do!",menuChoices);
		}
		scan.close();
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
				String catchAndRelease=scan.nextLine();
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


	/*
		this function opens up the saveFile, puts all the lines
		into an ArrayList of Strings, and returns the arrayList
	*/

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

	/*
		This function uses "getPlanner()" to get an ArrayList of strings
		which it displays on the screen
	*/

	public void printPlanner(){
		ArrayList<String> strs=getPlanner();
		if(strs.isEmpty()){
			printDiv("WOW! nothing to do! have fun on vacation!");
			return;
		}
		for(String str:strs){
			System.out.printf("%s\n",str);
		}
		return;
	}

	/*
		takes in a scanner object and uses "getPlanner()" to get an
		ArrayList<> of strings. From there the user can input
		another string, which is converted to an array and written to
		the file
	*/

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
		printToPlanner(strs);
	}
	/*
		A "jack-of-all-trades" function that basically catches the
		program when something is done incorrectly. It requires that
		the user enters any number to exit the function
		Uses:
		-pause();
		-proceed();
		-printNewPage();
	*/

	public void proceed(Scanner scan){
		pause(1000);
		printDiv("press any number to proceed");
		try{
			int catchAndRelease=scan.nextInt();
		}catch(InputMismatchException ime){
			String catchAndRelease=scan.nextLine();
			proceed(scan);
		}
		printNewPage();
		return;
	}

	/*
		This function allows the user to move one checkbox up to the
		place of another. It uses:
		-getPlanner();
		-printDiv();
		-printToPlanner();
		-getChoice();
	*/

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
		printToPlanner(strs);
	}

	/*
		converts a given ArrayList of Strings to a
		String[]
	*/

	public String[] getStrArrFromList(ArrayList<String> strs){
		Object[] objs=strs.toArray();
		String[] arr=new String[strs.size()];
		for(int i=0;i<objs.length;i++){
			arr[i]=(String)objs[i];
		}
		return arr;
	}

	/*
		displays the manual file
		uses:
		-proceed();
	*/

	public void displayMan(Scanner scan){
		try(Scanner s=new Scanner(new File("man.txt"))){
			while(s.hasNextLine()){
				System.out.println(s.nextLine());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		proceed(scan);
	}

	/*
		Allows the user to remove a checkbox from the planner it uses:
		-getPlanner();
		-printDiv();
		-getChoice();
		-printToPlanner();
		-pause();
	*/

	public void removeFromPlanner(Scanner scan){
		ArrayList<String> strs=getPlanner();
		if(strs.isEmpty()){
			printDiv("NOTHING TO DO!");
			return;
		}
		int choice=getChoice(scan,"have you done any of these?",getStrArrFromList(strs));
		if(choice==-1){
			printDiv("Sorry, that wasn't a valid choice!");
			return;
		}else{
			printDiv(String.format("Good job completing %s!", strs.get(choice)));
			strs.remove(choice);
			printToPlanner(strs);
		}
		pause(20);
		proceed(scan);

	}

	/*
		printNewPage basically resets the screen by printing what
		Computer Scientists affectionately call "a heck ton" of
		newline characters
	*/

	public void printNewPage(){
		for(int i=0;i<45;i++){
			System.out.printf("\n");
		}
	}

	/*
		given an arrayList of Strings, this function prints
		to the planner saveFile
	*/

	public void printToPlanner(ArrayList<String> strs){
		try(PrintWriter pw=new PrintWriter("saveFile.txt")){
			for(String str:strs){
				pw.write(str+"\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/*
		prints a given string with pretty, dashed formatting
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

	/*
		pause uses the thread.sleep method to insert pauses
		for aesthetic appeal
	*/

	public void pause(double time){
		try{
			thread.sleep((long)time);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}


}


