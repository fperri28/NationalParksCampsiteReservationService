package com.techelevator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;


public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] objects) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(objects);
			
			out.print("\nPlease choose an option >>> ");
			out.flush();
			
			choice = getChoiceFromUserInput(objects);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] objects) {
		Object choice = null;
		String userInput = in.nextLine();
		System.out.println(userInput);
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption > 0 && selectedOption <= objects.length) {
				choice = objects[selectedOption - 1];
			} 
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] objects) { // MAIN MENU METHOD EXCLUDES THE HIDDEN OPTION
		out.println();
		for(int i = 0; i < objects.length; i++) {
			int optionNum = i+1;
					out.println( optionNum + ") " + objects[i]);;
					
		}

	}
}
