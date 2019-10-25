package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.park.Park;

public interface ReservationDAO {

// 	C.R.U.D (Create (Insert) - Read (Select) - Update - Delete)
	
	
	// Create a new Reservation
	public Reservation addReservations(String name, LocalDate fromDate, LocalDate toDate, int siteId); 
	
	//Return all reservations
	public List<Reservation> getAllReservations();
	//search Reservations by Dates
	public List<Reservation> getReservationsByDate(int siteId, LocalDate fromDate, LocalDate toDate);
	
	//search Reservations by Site
	public List<Reservation> getReservationsBySite(int siteId, int campId);
	
	
	//	Select a Reservation by it's ID (Primary Key)
	public Reservation getReservationById(int aReservationId);
	
	public List<Reservation> getReservationByName(String name);
	
	// search Reservations by max occupancy
	public List<Reservation> getResByMaxOcc(int occ);
	
	// Modify existing reservations
	public boolean updateReservation(int resID, LocalDate fromDate, LocalDate toDate);
	
	
	// Delete reservations
	public boolean delReservation(int resID);
	
	
	
}
