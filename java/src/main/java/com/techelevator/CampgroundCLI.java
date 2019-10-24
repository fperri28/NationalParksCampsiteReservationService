package com.techelevator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.Menu;

public class CampgroundCLI {

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		
		Menu appMenu = new Menu(System.in, System.out);                		// Instantiate a menu for Vending Machine to use
		CampgroundCLI application = new CampgroundCLI(dataSource, appMenu);	// Instantiate a CampgroundCLI passing it the datasource and Menu object to use
		application.run();													// Tell the CampgrounCLI to start running
	}

	
	
	
///////////////////////////////////////////MENU CLI ////////////////////////////////////////////////////////////////



	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Parks";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Quit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT };

	private static final String SUB_MENU_FEED_MONEY = "Feed Money";
	private static final String SUB_MENU_SELECT_PRODUCT = "Select Product";
	private static final String SUB_MENU_FINISH_TRANSACTION = "Finish Transaction";

	private static final String[] SUB_MENU_OPTIONS = { SUB_MENU_FEED_MONEY, SUB_MENU_SELECT_PRODUCT,
			SUB_MENU_FINISH_TRANSACTION };

	private static final String SUB_SUB_MENU_ONE_DOLLAR = "Add $1.00";
	private static final String SUB_SUB_MENU_TWO_DOLLARS = "Add $2.00";
	private static final String SUB_SUB_MENU_FIVE_DOLLARS = "Add $5.00";
	private static final String SUB_SUB_MENU_TEN_DOLLARS = "Add $10.00";
	private static final String	SUB_SUB_MENU_EXIT		= 	"EXIT";
	private static final String[] SUB_SUB_MENU_OPTIONS = { SUB_SUB_MENU_ONE_DOLLAR, 
															SUB_SUB_MENU_TWO_DOLLARS,
															SUB_SUB_MENU_FIVE_DOLLARS, 
															SUB_SUB_MENU_TEN_DOLLARS, 
															SUB_SUB_MENU_EXIT};

	private Menu campgroundMenu;
	public CampgroundCLI(DataSource datasource, Menu menu) {
		this.campgroundMenu = menu;
		
		
		// create your DAOs here
	}


	/**************************************************************************************************************************
	 * CampgroundCLI main processing loop
	 * 
	 * Display the main menu and process option chosen
	 ***************************************************************************************************************************/

	public void run() {
		//Vending.setup();   // NEED SETUP METHOD TO INITIALIZE 
		boolean shouldProcess = true; 

		while (shouldProcess) { 

		String choice = (String) campgroundMenu.getChoiceFromOptions(MAIN_MENU_OPTIONS, 3, false); 

			switch (choice) { 

			case MAIN_MENU_OPTION_DISPLAY_ITEMS:
				displayItems(); 
				break; 

			case MAIN_MENU_OPTION_PURCHASE:
				purchase(); 
				break; 

			case MAIN_MENU_OPTION_EXIT:
				endMethodProcessing();
				System.out.println("Thanks for visiting the National Park Campsite website. \"In all things of nature there is something of the marvelous.\" Aristotle");
				shouldProcess = false;
				break;
			}
		}
		return;
	}

	/**************************************************************************************************************************
	 * CampgroundCLI sub menu for purchase =
	 * 
	 * Display the Sub menu and process option chosen
	 ***************************************************************************************************************************/

	public void purchase() {

		boolean shouldProcess = true;
		boolean stillShopping = true;

		while (shouldProcess) {

			String choice = (String) campgroundMenu.getChoiceFromOptions(SUB_MENU_OPTIONS, 3, true); 
																									
			switch (choice) {

			case SUB_MENU_FEED_MONEY:
				money();
				break;

			case SUB_MENU_SELECT_PRODUCT:
				break;

			case SUB_MENU_FINISH_TRANSACTION:
				finalizeTransaction();
				shouldProcess = false;
				break;

			}
		}
		return;
	}

	/**************************************************************************************************************************
	 * CampgroundCLI Feed money loop
	 * 
	 * Display the sub menu and process option chosen
	 ***************************************************************************************************************************/

	public void money() {


		String choice = (String) campgroundMenu.getChoiceFromOptions(SUB_SUB_MENU_OPTIONS, 5, true); 

		switch (choice) {

		case SUB_SUB_MENU_ONE_DOLLAR:
			money();
			break;

		case SUB_SUB_MENU_TWO_DOLLARS:
			money();
			break;

		case SUB_SUB_MENU_FIVE_DOLLARS:
			money();
			break;

		case SUB_SUB_MENU_TEN_DOLLARS:
			money();
			break;

		case SUB_SUB_MENU_EXIT:
			break;
		}
		return;
	}

	/********************************************************************************************************
	 * Methods used to perform processing
	 ********************************************************************************************************/

	public static void displayItems() {
		//Vending.getItems();
	}

	public static boolean purchaseItems() {

		//////////////////////// TEXT PROMPTS FOR USER /////////////////////////////
		System.out.println("Which product would you like to purchase?");
		System.out.println("_________________________________________");
		
		displayItems(); 
		
		System.out.println("_________________________________________");
		System.out.println();
		//System.out.println("Current balance: $" + String.format("%.2f", Vending.getCurrentBalance()));
		System.out.println("Enter Slot ID: (A1, B3, etc)");
		System.out.println("To Return to menu enter \"EXIT\" ");

		String productChoice = getProductChoice();

		boolean result = true;

		if (productChoice.equals("EXIT")) {
			result = false;
		} else {
			try {
//				try {
//			//		Vending.products.get(productChoice).purchaseItem();
//				} catch (IOException e) {
//					System.out.println("I'm sorry you entered an invalid Slot ID");
//					System.out.println("Please try again.");
//				}
			} catch (NullPointerException e) {
				if (!productChoice.equals("EXIT")) {
					System.out.println("I'm sorry you entered an invalid Slot ID");
					System.out.println("Please try again.");
				}
			}
//			if (Vending.getCurrentBalance() < .75) {
//				System.out.println();
//				System.out.println("To continue shopping please Feed Money.");
//				System.out.println();
//				result = false;
//			}
		}
		return result;
	}

	public static void endMethodProcessing() {

	}

	public static void finalizeTransaction() {
//		Transaction finalizeTransaction = null;
//		try {
//			finalizeTransaction = new Transaction("FINISH TRANSACTION", 0.0);
//			finalizeTransaction.finishTransaction();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	
		
	}

	private static String getProductChoice() {
		Scanner theKeyboard = new Scanner(System.in);
		String userInput = theKeyboard.nextLine();
		String userInputParsed = userInput.trim().toUpperCase();
		return userInputParsed;
	}
}
