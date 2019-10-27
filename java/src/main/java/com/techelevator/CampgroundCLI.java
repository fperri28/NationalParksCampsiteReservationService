package com.techelevator;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
	
/////////////////////////////////////////// MENU CLI ////////////////////////////////////////////////////////////////

	private static final String VIEW_CAMPGROUNDS 					= "View Campgrounds";
	private static final String SEARCH_RESERVATIONS 				= "Search for Reservations";
	private static final String RETURN_TO_MAIN_MENU 				= "Return to Parks";
	private static final String[] SUB_MENU_OPTIONS 					= { VIEW_CAMPGROUNDS, 
																		SEARCH_RESERVATIONS,
																		RETURN_TO_MAIN_MENU };

	private static final String SEARCH_FOR_AVAILABLE_RESERVATIONS 			= "Search for Available Reservations";
//	private static final String SEARCH_FOR_AVAILABLE_RESERVATIONS_BY_PARK 	= "Search for Available Reservations in the entire Park";	
	private static final String SEARCH_FOR_BOOKED_RESERVATIONS 				= "View Booked Reservations";	
	private static final String	MENU_EXIT									= "Return to Previous Screen";
	private static final String[] RESERVATION_MENU_OPTIONS 					= { SEARCH_FOR_AVAILABLE_RESERVATIONS,
																				SEARCH_FOR_BOOKED_RESERVATIONS,
//																				SEARCH_FOR_AVAILABLE_RESERVATIONS_BY_PARK,
																				MENU_EXIT};
	
///////////////////////////////////////	Variables / Main / Constructor //////////////////////////////////////////////////
	
	private Menu 			campgroundMenu;
	private ParkDAO 		parkDAO;
	private CampgroundDAO 	campDAO;
	private ReservationDAO 	resDAO;
	private SiteDAO 		siteDAO;
	
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
		displayApplicationBanner();
		String choice = (String) campgroundMenu.getChoiceFromOptions(displayParks()); 
		while(true) {		
				Object lastChoice = displayParks()[displayParks().length-1];
				if(choice == lastChoice){
					endMethodProcessing();
				} else {
					displayParkDetails(choice);
					campgrounds(choice);
				}
		}
	}

	/**************************************************************************************************************************
	 * CampgroundCLI sub menu for Campgrounds and reservations search
	 * 
	 * Display the Sub menu and process option chosen
	 ***************************************************************************************************************************/

	public void campgrounds(String park) {

			String choice = (String) campgroundMenu.getChoiceFromOptions(SUB_MENU_OPTIONS); 
			if(choice.equals(VIEW_CAMPGROUNDS)){
				System.out.println("\n" + park + " National Park Campgrounds\n");
				int prevPark = parkDAO.getParkByName(park).getPark_id();
				displayCampgroundsByPark(prevPark);
				reservationsMenu(prevPark);
			} else if(choice.equals(SEARCH_RESERVATIONS)) {
				reservations();
			} else if(choice.equals(RETURN_TO_MAIN_MENU)) {
				run();
			}
		}

	/**************************************************************************************************************************
	 * CampgroundCLI new Reservation menu
	 * 
	 * Display the sub menu and process option chosen
	 ***************************************************************************************************************************/

	public void reservationsMenu(int prevPark) {
		String choice = (String) campgroundMenu.getChoiceFromOptions(RESERVATION_MENU_OPTIONS); 

		if(choice.equals(SEARCH_FOR_AVAILABLE_RESERVATIONS)){
			askForCampgrounId(prevPark);
		}
//		
//		BONUS under construction		
//		else if(choice.equals(SEARCH_FOR_AVAILABLE_RESERVATIONS_BY_PARK)) {
//			entireParkSearch(prevPark);
//		}
		else if(choice.equals(SEARCH_FOR_BOOKED_RESERVATIONS)) {
			displayNext30DaysOfReservations(prevPark);
		}
		else if(choice.equals(MENU_EXIT)) {
			return;
		}
		
	}
	
	/********************************************************************************************************
	 * Methods used to perform processing
	 ********************************************************************************************************/
	
	public void reservations() {
		Integer userReservationId = Integer.parseInt(getUserInput("\n\nPlease Enter Your Reservation ID: " +
																  "(enter 0 to cancel)"));
	
		Reservation reservation = resDAO.getReservationById(userReservationId);
	
		if(userReservationId.equals(0)) {
			return;
		} else if (reservation != null) {
			System.out.println();
			System.out.println("Congratulations " + reservation.getName() + ", you are booked.");
			System.out.println();
			System.out.println("Your reservation id is: " );			
			System.out.println(reservation.getReservation_id());
			System.out.println("Your site id is: " );
			System.out.println(reservation.getSite_id());
			System.out.println("Your arrival date is: ");
			System.out.println(reservation.getFrom_date());
			System.out.println("Your departure date is: ");
			System.out.println(reservation.getTo_date());
			System.out.println();

			return;
		} else {
			System.out.println("Invalid Reservation ID, please try again.");
			reservations();
			return;
		}
	}

	public Object[] displayParks() {
		List<Park> parks = parkDAO.getAllParks();
		List<String> parkNames = new ArrayList<String>();
		
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
	
	
	public void displayCampgroundsByPark(int prevPark) {
		List<Campground> campgroundsByPark = campDAO.getCampgroundByPark(prevPark); 
		displayCampgrounds(campgroundsByPark);
	}
	
	public void displayNext30DaysOfReservations(int prevPark) {
		List<Reservation> reservationsByPark = resDAO.getReservationsForNext30(LocalDate.now(), LocalDate.now().plusMonths(1), prevPark); 
		displayNext30Res(reservationsByPark);
	}

//	BONUS method, under construction - I cannot finish this due to selectReservation being coded into our date data collection
//	public void entireParkSearch(int prevPark) {
//		List<Campground> campgroundsList = campDAO.getCampgroundByPark(prevPark);		
//		Campground campground = campgroundsList.get(0);
//		int campgroundId = campground.getCampground_id();
//		campSiteSearch(prevPark, campgroundId);
//	}
	
	public void askForCampgrounId(int prevPark) {
		List<Campground> campgrounds = campDAO.getCampgroundByPark(prevPark);
		Set<Integer> idList = new HashSet<Integer> ();
		Integer userSelCampId = null;
	   
		/*
		//NumberFormatException
	    try {
            // intentional error
            String s = "FOOBAR";
            int i = Integer.parseInt(s);

            // this line of code will never be reached
            System.out.println("int value = " + i);
        }
        catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        */
	    
		
		
		
		try {
		
			userSelCampId = Integer.parseInt(getUserInput("\n\nWhich campground(enter 0 to cancel)?"));

			for(Campground cur: campgrounds) {
				idList.add(cur.getCampground_id());
			}
			
			// User campground validator
			if(userSelCampId == 0) {
				return;
			} else if(!idList.contains(userSelCampId)) {
				System.out.println("\nINVALID SELECTION");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Please enter valid input");
		}
		
		// Get user arrival date
		boolean success = false;
		
		do {
			try {
				arrDate = getUserInputDate("What is the arrival date? MM/DD/YYY");
				success = true;
			} catch (DateTimeParseException e){
				System.out.println("Please insert valid date");
			}
		}
		while(!success);
		
		// Get user departure date
		success = false;
		
		do {
			try {
				depDate = getUserInputDate("What is the departure date? MM/DD/YYY");
				success = true;
			} catch (DateTimeParseException e){
				System.out.println("Please insert valid date");
			}
		}
		while(!success);			
	 
		// Retrieve stay length and month values
	    Period intervalPeriod = Period.between(arrDate, depDate);
	    BigDecimal stayDays = new BigDecimal(intervalPeriod.getDays());
		
	    String arrMonth = String.valueOf(arrDate.getMonthValue());
	    String depMonth = String.valueOf(depDate.getMonthValue());
	    
	    // Validate dates 
	    if(arrDate.isBefore(depDate)) {
	    	availRes = siteDAO.getAvailableResBySite(campgroundId, prevPark, arrDate, depDate, arrMonth, depMonth);
	    } else {
	    	System.out.println("Invalid date selection. Departure date must be after arrival date");
	    	return;
	    }
	    
	    // Validate query results
		if(availRes.size() > 0 && availRes.size() < 5) {  // < ------ need to tie the campgroundId parameter to this if statement to confirm all campgrounds have same id
			displayAvailRes(availRes, stayDays);
// selectReservation is where I'm having my issue for the Bonue
			selectReservation(arrDate, depDate);
		} else if (availRes.size() > 0 && availRes.size() > 5) {

// If the List<site> is larger then 5 it shouldnt be from one campground. 
			
			displayAvailRes(availRes, stayDays);

		
		} else {
			
			System.out.println("\nNo available sites for these dates.");
			String inputAltDate = null;
			do {
				inputAltDate = getUserInput("\nWould you like to search an alternate dates? Y/N").toUpperCase();
				if (inputAltDate.contains("Y")) {
					campSiteSearch(prevPark, campgroundId);
				} else if(inputAltDate.contains("N")){
					return;
				}
			} while (!inputAltDate.equals("Y") && !inputAltDate.equals("N"));
		}
	}
	
	private LocalDate getUserInputDate(String message) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		String inputDate = getUserInput(message);
		LocalDate date = LocalDate.parse(inputDate, formatter);
		LocalDate today = LocalDate.now();
		Boolean validDate = false;
		do {
			if(date.isAfter(today)) {
				validDate = true;
			} else {
				System.out.println("Input date in past");
				inputDate = getUserInput(message);
				date = LocalDate.parse(inputDate, formatter);
			}
		} while(!validDate);
		return date;
	}
	
	
	private void selectReservation(LocalDate fromDate, LocalDate toDate) {
		Integer userSelSite = Integer.parseInt(getUserInput("\n\nWhich site should be reserved (enter 0 to cancel)?"));
		
		if(userSelSite == 0) {
			return;
		}
	
		String userName = getUserInput("\nWhat name should the reservation be made under?");
		Reservation newRes = resDAO.addReservations(userName, fromDate, toDate, userSelSite);
		
		System.out.println("\nCongratulations " + newRes.getName() + ", you are booked.");
		System.out.println("Your confirmation number/reservation id is: ");
		System.out.println(newRes.getReservation_id());
		System.out.println("Your arrival date is: ");
		System.out.println(newRes.getFrom_date());
		System.out.println("Your departure date is: ");
		System.out.println(newRes.getTo_date());
		System.out.println("\nThank you for booking with the National Park Data Base, we look forward to hosting you!");
		
		return;
	}
	
	public void displayNext30Res(List<Reservation> reservationsByPark) {
		System.out.println();
		System.out.printf(String.format("%-15s", "Reservation ID"));
		System.out.printf(String.format("%-15s", "Site ID")); 
		System.out.printf(String.format("%-32s", "Name"));
		System.out.printf(String.format("%-15s", "From Date"));
		System.out.printf(String.format("%-15s", "To Date"));
		System.out.printf(String.format("%-15s", "Date Created"));
		System.out.println();
		System.out.println("========================================================================================================");

		if(reservationsByPark.size() > 0) {

			for(Reservation res : reservationsByPark) {
							
				System.out.printf(String.format("%-15s", res.getReservation_id()));
				System.out.printf(String.format("%-15s", res.getSite_id()));
				System.out.printf(String.format("%-32s", res.getName()));
				System.out.printf(String.format("%-15s", res.getFrom_date()));
				System.out.printf(String.format("%-15s", res.getTo_date()));
				System.out.printf(String.format("%-15s", res.getCreate_date()));
				System.out.println();
			}
		} else {
			System.out.println("\n*** No results ***");
		}
		System.out.println("\n");
	}
	
	public void displayCampgrounds(List<Campground> campgroundsByPark)	{
		
		System.out.println();
		System.out.printf(String.format("%-6s", "ID"));
		System.out.printf(String.format("%-35s", "Campground Name")); 
		System.out.printf(String.format("%-13s", "Open"));
		System.out.printf(String.format("%-13s", "Close"));
		System.out.printf(String.format("%-13s", "Daily Fee"));
		System.out.println();
		System.out.println("============================================================================");

		if(campgroundsByPark.size() > 0) {

			for(Campground cur : campgroundsByPark) {
				
				// Convert months from number Strings to word Strings (i.e: 01 to January, 02 to February, etc)
				String openMonth = cur.getOpen_from_mm();
				String strOpenMonth = campDAO.stringMonth(openMonth);
				
				String closeMonth = cur.getOpen_to_mm();
				String strCloseMonth = campDAO.stringMonth(closeMonth);				
				
				System.out.printf(String.format("%-6s", cur.getCampground_id()));
				System.out.printf(String.format("%-35s", cur.getName()));
				System.out.printf(String.format("%-13s", strOpenMonth));
				System.out.printf(String.format("%-13s", strCloseMonth));
				System.out.printf(String.format("%-13s", "$" + cur.getDaily_fee().setScale(2)));
				System.out.println();
			}
			
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	public void displayAvailRes(List<Site> resSearch, BigDecimal duration) {  
		
		System.out.println();
		System.out.printf(String.format("%-8s", "Sites"));
		System.out.printf(String.format("%-10s", "Max Occ."));
		System.out.printf(String.format("%-15s", "Accessible?"));
		System.out.printf(String.format("%-10s", "RV Len."));
		System.out.printf(String.format("%-10s", "Utility"));
		System.out.printf(String.format("%-10s", "Cost"));
		System.out.println("\n============================================================");
		
		for(Site cur: resSearch) {
			
			System.out.printf(String.format("%-8s", cur.getSite_id()));
			System.out.printf(String.format("%-10s", cur.getMax_occupancy()));
			System.out.printf((String.format("%-15s", siteDAO.stringTrueFalseSwitch(cur.isAccessible())))); 
			System.out.printf((String.format("%-10s", siteDAO.stringRV(cur.getMax_rv_length())))); 
			System.out.printf((String.format("%-10s", siteDAO.stringTrueFalseSwitch(cur.isUtilities()))));
			System.out.printf((String.format("%-20s", "$" + campDAO.getCampgroundRate(cur.getCampground_id()).multiply(duration).setScale(2))));
			System.out.println();
		}
	}

	public void displayParkDetails(String choice) {
		List<Park> parksDetails = parkDAO.getParksByName(choice);
		
		if(parksDetails.size() > 0) {
			
			for(Park cur : parksDetails) {
				
				System.out.println(cur.getName() + " National Park");
				System.out.println("\n================================\n");
				System.out.printf(String.format( "%-17s", "Location: "));
				System.out.println(cur.getLocation());
				System.out.printf(String.format("%-17s", "Established: "));
				System.out.println(cur.getEstablish_date());
				System.out.printf(String.format("%-17s", "Area: "));
				System.out.println(cur.getArea() + " sq km");
				System.out.printf(String.format("%-17s", "Annual Visitors: " ));
				System.out.println(cur.getVisitors());
				System.out.println("\n================================\n");
				System.out.println(cur.getDescription());
				
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	public static void endMethodProcessing() {
		System.out.println("Thanks for visiting the National Park Campsite website." +
				"\n \"In all things of nature there is something of the marvelous.\" Aristotle");
		System.exit(0);
	}
	
	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
	
	private void displayApplicationBanner() {
		System.out.println("\n" + 
				"███╗   ██╗ █████╗ ████████╗██╗ ██████╗ ███╗   ██╗ █████╗ ██╗         ██████╗  █████╗ ██████╗ ██╗  ██╗███████╗\n" + 
				"████╗  ██║██╔══██╗╚══██╔══╝██║██╔═══██╗████╗  ██║██╔══██╗██║         ██╔══██╗██╔══██╗██╔══██╗██║ ██╔╝██╔════╝\n" + 
				"██╔██╗ ██║███████║   ██║   ██║██║   ██║██╔██╗ ██║███████║██║         ██████╔╝███████║██████╔╝█████╔╝ ███████╗\n" + 
				"██║╚██╗██║██╔══██║   ██║   ██║██║   ██║██║╚██╗██║██╔══██║██║         ██╔═══╝ ██╔══██║██╔══██╗██╔═██╗ ╚════██║\n" + 
				"██║ ╚████║██║  ██║   ██║   ██║╚██████╔╝██║ ╚████║██║  ██║███████╗    ██║     ██║  ██║██║  ██║██║  ██╗███████║\n" + 
				"╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝    ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝\n" + 
				"                                                                                                             \n" + 
				"██████╗  █████╗ ████████╗ █████╗     ██████╗  █████╗ ███████╗███████╗                                        \n" + 
				"██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗    ██╔══██╗██╔══██╗██╔════╝██╔════╝                                        \n" + 
				"██║  ██║███████║   ██║   ███████║    ██████╔╝███████║███████╗█████╗                                          \n" + 
				"██║  ██║██╔══██║   ██║   ██╔══██║    ██╔══██╗██╔══██║╚════██║██╔══╝                                          \n" + 
				"██████╔╝██║  ██║   ██║   ██║  ██║    ██████╔╝██║  ██║███████║███████╗                                        \n" + 
				"╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝╚══════╝                                        \n" + 
				"                                                                                                             \n" + 
				"\nWelcome to the National Parks Data Base." +
				"\nPlease choose the number of the park that you would like to know more about.");
	}
	
}







