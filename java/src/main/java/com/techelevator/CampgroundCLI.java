package com.techelevator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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



	private static final String VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String SEARCH_RESERVATIONS = "Search for Reservations";
	private static final String RETURN_TO_MAIN_MENU = "Return to Previous Screen";
	private static final String[] SUB_MENU_OPTIONS = { VIEW_CAMPGROUNDS, SEARCH_RESERVATIONS,
														RETURN_TO_MAIN_MENU };

	private static final String SEARCH_FOR_AVAILABLE_RESERVATIONS = "Search for Available Reservations";
	private static final String	MENU_EXIT		= 					"Return to Previous Screen";
	private static final String[] RESERVATION_MENU_OPTIONS = { SEARCH_FOR_AVAILABLE_RESERVATIONS, 
															MENU_EXIT};

	
	
///////////////////////////////////////	Variable//////////////////////////////////////////////////
	private Menu campgroundMenu;
	private ParkDAO parkDAO;
	private CampgroundDAO campDAO;
	private ReservationDAO resDAO;
	private SiteDAO siteDAO;
	private String prevPark;
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
		while(true) {		
				Object lastChoice = displayParks()[displayParks().length-1];
				if(choice == lastChoice){
					endMethodProcessing();
				} else {
					displayParkDetails(choice);
					prevPark = choice;
					campgrounds(choice);
				}
		}
	}

	/**************************************************************************************************************************
	 * CampgroundCLI sub menu for purchase =
	 * 
	 * Display the Sub menu and process option chosen
	 ***************************************************************************************************************************/

	public void campgrounds(String park) {

			String choice = (String) campgroundMenu.getChoiceFromOptions(SUB_MENU_OPTIONS); 
			if(choice.equals(VIEW_CAMPGROUNDS)){
				System.out.println("Park Campgrounds");
				displayCampgrounds(displayCampgroundsByPark(park));
				
				
				
				Reservations();
			} else if(choice.equals(SEARCH_RESERVATIONS)) {
				//Reservations(); This needs to be modified to show all remove the campsite selection portion
			} else if(choice.equals(RETURN_TO_MAIN_MENU)) {
				run();
			}
		}

	/**************************************************************************************************************************
	 * CampgroundCLI loop
	 * 
	 * Display the sub menu and process option chosen
	 ***************************************************************************************************************************/

	public void Reservations() {

		String choice = (String) campgroundMenu.getChoiceFromOptions(RESERVATION_MENU_OPTIONS); 

		if(choice.equals(SEARCH_FOR_AVAILABLE_RESERVATIONS)){
			campSiteSearch();
		} else if(choice.equals(MENU_EXIT)) {
			return;
		}
	}
	
	

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
	
	
	
	public List<Campground> displayCampgroundsByPark(String parkName) {
		List<Park> parksDetails = parkDAO.getParkByName(parkName);
		int parkId = parksDetails.get(0).getPark_id();
		
		List<Campground> campgroundsByPark = campDAO.getCampgroundByPark(parkId); //<--- this is what i want for the viable search options
		 // <--- NOT SURE ABOUT THIS
		return campgroundsByPark;
	}
	
	public void displayCampgrounds(List<Campground> campgroundsByPark)	{

		System.out.printf(String.format("%-4s", ""));
		System.out.printf(String.format("%-15s", "Name")); 
		System.out.printf(String.format("%-20s", "Open"));
		System.out.printf(String.format("%-25s", "Close"));
		System.out.printf(String.format("%-30s", "Daily Fee"));
		System.out.println();

		if(campgroundsByPark.size() > 0) {
			for(Campground cur : campgroundsByPark) {
				System.out.printf(String.format("%-4s", cur.getCampground_id()));
				System.out.printf(String.format("%-15s", cur.getName()));
				System.out.printf(String.format( "%-20s", cur.getOpen_from_mm()));
				System.out.printf(String.format( "%-25s", cur.getOpen_to_mm()));
				System.out.printf(String.format( "%-30s", cur.getDaily_fee()));
				System.out.println();
			}
		} else {
			System.out.println("\n*** No results ***");
		}
		
	}

	
	private void campSiteSearch() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		int campgroundId = Integer.parseInt(getUserInput("Which campground(enter 0 to cancel)?"));
		// NEED TO LIMIT SELECTIONS TO THE CAMPGROUND OPTIONS AND 0 TO EXIT
		List<Campground> campgrounds = displayCampgroundsByPark(prevPark);
		
		if(campgroundId == 0) {
			return;
		} else if(campgroundId > campgrounds.size()) {
			System.out.println("INVALID SELECTION");
			return;
		} 
		
		String inputArrDate = getUserInput("What is the arrival date?__/__/____");
		// NEED TO THROW ERROR MESSAGE IF INPUT IS NOT VALID
		
		LocalDate arrDate = LocalDate.parse(inputArrDate, formatter);
		String inputDepartureDate = getUserInput("What is the departure date?__/__/____");
		// NEED TO THROW ERROR MESSAGE IF INPUT IS NOT VALID
		LocalDate depDate = LocalDate.parse(inputDepartureDate, formatter);
		
		List<Reservation> reservedSites = resDAO.getReservationsByDate(campgroundId, arrDate, depDate);
		displayAvailRes(reservedSites);
	}
	
	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
	
	
	
	public void displayAvailRes(List<Reservation> resSearch) {  
		
		
		List<Park> park = parkDAO.getParkByName(prevPark);
		int parkID = park.get(0).getPark_id();
		
		List<Campground> camp = campDAO.getCampgroundByPark(parkID);
		List<Site> sites = null;
		List<Integer> sitesByPark = new ArrayList<Integer> ();
		BigDecimal dailyFee = camp.get(0).getDaily_fee();
		
		for(Campground cur: camp) {
			sites = siteDAO.getSiteByCampground(cur.getCampground_id());
			for(Site curr: sites) {
				sitesByPark.add(curr.getSite_id());
			}
		}
		
		System.out.printf(String.format("%-8s", "Sites"));
		System.out.printf(String.format("%-14s", "Max Occ."));
		System.out.println("Fees");
		System.out.println();
		
		for(int i = 0; i < sites.size(); i++) {
			for(int j = 0; j < resSearch.size(); j++) {
				if(sitesByPark.get(i) != resSearch.get(j).getSite_id()) {
					System.out.println(sitesByPark.get(i) + " " + sites.get(i).getMax_occupancy() + " $" + dailyFee );
					System.out.println();
				}
			}
		}
		
		
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
























