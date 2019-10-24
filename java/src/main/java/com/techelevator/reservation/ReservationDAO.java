package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

// 	C.R.U.D (Create (Insert) - Read (Select) - Update - Delete)
	
	
	// Create a new Reservation
	public boolean addReservations(Reservation newReservation); 
	
	//Return all reservations
	public List<Reservation> getAllReservations();
	//search Reservations by Dates
	public List<Reservation> getReservationsByDate(LocalDate fromDate, LocalDate toDate);
	
	//search Reservations by Site
	public List<Reservation> getReservationsBySite(Long SiteId);
	
	// search Reservations by max occupancy
	public List<Reservation> getResByMaxOcc(int occ);
	
	// Modify existing reservations
	public boolean updateReservation(Long resID, LocalDate fromDate, LocalDate toDate);
	
	
	// Delete reservations
	public boolean delReservation(Long resID);
	
	
	
}
