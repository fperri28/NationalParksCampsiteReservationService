package com.techelevator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.Menu;
import com.techelevator.campground.Campground;
import com.techelevator.campground.CampgroundDAO;
import com.techelevator.campground.JDBCCampgroundDAO;
import com.techelevator.park.JDBCParkDAO;
import com.techelevator.park.Park;
import com.techelevator.park.ParkDAO;
import com.techelevator.reservation.JDBCReservationDAO;
import com.techelevator.reservation.Reservation;
import com.techelevator.reservation.ReservationDAO;
import com.techelevator.site.JDBCSiteDAO;
import com.techelevator.site.Site;
import com.techelevator.site.SiteDAO;

public class CampgroundCLI {
	
///////////////////////////////////////////MENU CLI ////////////////////////////////////////////////////////////////



	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Parks";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Quit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT };

	private static final String VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String SEARCH_RESERVATIONS = "Search for Reservations";
	private static final String RETURN_TO_MAIN_MENU = "Return to Previous Screen";

	private static final String[] SUB_MENU_OPTIONS = { VIEW_CAMPGROUNDS, SEARCH_RESERVATIONS,
			RETURN_TO_MAIN_MENU };

	private static final String SUB_SUB_MENU_ONE_DOLLAR = "Add $1.00";
	private static final String SUB_SUB_MENU_TWO_DOLLARS = "Add $2.00";
	private static final String	SUB_SUB_MENU_EXIT		= 	"EXIT";
	private static final String[] SUB_SUB_MENU_OPTIONS = { SUB_SUB_MENU_ONE_DOLLAR, 
															SUB_SUB_MENU_TWO_DOLLARS,
															SUB_SUB_MENU_EXIT};

	
	
///////////////////////////////////////	Variable//////////////////////////////////////////////////
	private Menu campgroundMenu;
	private ParkDAO parkDAO;
	private CampgroundDAO campDAO;
	private ReservationDAO resDAO;
	private SiteDAO siteDAO;
	
	public static void main(String[] args) {
		CampgroundCLI application = new CampgroundCLI();
		application.run();
		
	}
	
	public CampgroundCLI() {
		this.campgroundMenu = new Menu(System.in, System.out);               		// Instantiate a menu for Campground to use

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		parkDAO = new JDBCParkDAO(dataSource);
		campDAO = new JDBCCampgroundDAO(dataSource);
		resDAO = new JDBCReservationDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		
	}


	/**************************************************************************************************************************
	 * CampgroundCLI main processing loop
	 * 
	 * Display the main menu and process option chosen
	 ***************************************************************************************************************************/

	public void run() {
		String choice = (String) campgroundMenu.getChoiceFromOptions(displayParks()); 
				
				Object lastChoice = displayParks()[displayParks().length-1];
				if(choice == lastChoice){
					endMethodProcessing();
				} else {
					displayParkDetails(choice);
					campgrounds(choice);
				}
	}

	/**************************************************************************************************************************
	 * CampgroundCLI sub menu for purchase =
	 * 
	 * Display the Sub menu and process option chosen
	 ***************************************************************************************************************************/

	public void campgrounds() {

		boolean shouldProcess = true;
		while (shouldProcess) {

			String choice = (String) campgroundMenu.getChoiceFromOptions(SUB_MENU_OPTIONS); 
																									
			switch (choice) {

			case VIEW_CAMPGROUNDS:
				
				break;

			case SEARCH_RESERVATIONS:
				break;

			case RETURN_TO_MAIN_MENU:
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

//	public void money() {
//
//
//		String choice = (String) campgroundMenu.getChoiceFromOptions(SUB_SUB_MENU_OPTIONS); 
//
//		switch (choice) {
//
//		case SUB_SUB_MENU_ONE_DOLLAR:
//			money();
//			break;
//
//		case SUB_SUB_MENU_TWO_DOLLARS:
//			money();
//			break;
//
//		case SUB_SUB_MENU_EXIT:
//			break;
//		}
//		return;
//	}

	/********************************************************************************************************
	 * Methods used to perform processing
	 ********************************************************************************************************/

	public Object[] displayParks() {
		List<Park> parks = parkDAO.getAllParks();
		List<String> parkNames = new ArrayList<String>();
		
		System.out.println();
		
		if(parks.size() > 0) {
			for(Park cur : parks) {
				parkNames.add(cur.getName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
		parkNames.add("Quit");
		return parkNames.toArray();
	}
	
	public Object[] displayCampgrounds(String parkId) {
		List<Campground> campgroundsByPark = campDAO.getCampgroundByPark(parkId);
		
		
		
		
		
		return ;
	}



	public static void endMethodProcessing() {
		System.out.println("Thanks for visiting the National Park Campsite website. \"In all things of nature there is something of the marvelous.\" Aristotle");
		System.exit(0);
	}
	
	public void displayParkDetails(String choice) {
		List<Park> parksDetails = parkDAO.getParkByName(choice);
		System.out.println();
		
		if(parksDetails.size() > 0) {
			for(Park cur : parksDetails) {
				System.out.println(cur.getName() + " National Park");
				System.out.printf(String.format( "%-17s", "Location: "));
				System.out.println(cur.getLocation());
				System.out.printf(String.format("%-17s", "Established: "));
				System.out.println(cur.getEstablish_date());
				System.out.printf(String.format("%-17s", "Area: "));
				System.out.println(cur.getArea() + " sq km");
				System.out.printf(String.format("%-17s", "Annual Visitors: " ));
				System.out.println(cur.getVisitors());
				System.out.println();
				System.out.println(cur.getDescription());
				
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
}
























