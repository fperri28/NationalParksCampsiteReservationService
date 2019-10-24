package com.techelevator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;


public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options , int spaces, boolean showBalance) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options, spaces);
			
			if (showBalance) {
				out.println();
	//			out.println("Current balance: $" +String.format("%.2f", Vending.getCurrentBalance()));
			}
			out.print("\nPlease choose an option >>> ");
			out.flush();
			
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options, int spaces) { // MAIN MENU METHOD EXCLUDES THE HIDDEN OPTION
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
				if (optionNum<=spaces) {
					out.println(optionNum+") "+options[i]);
				}
				else {
					out.print(options[i]);
				}
		}

	}
}
