package com.techelevator.park;

import java.math.BigDecimal;
import java.util.List;

public interface ParkDAO {

// 	C.R.U.D (Create (Insert) - Read (Select) - Update - Delete)
	
	//	Create Method - Add a row to the table
	public boolean addLocation(Park newPark);					//	Insert a row into the Park table from a Park Object

	// 	Read Method(s) - Select a row(s) from the table
	public Park getParkById(String aPark);						//	Select a Park by it's ID (Primary Key)
	
	public List<Park> getAllParks();							//	Return all the Park
	
	public List<Park> getParkByCampground(String campground);	//	Return all Park in a campground

	public BigDecimal getParkRate(Park aPark);					//	Return the rate for a Park	
	
	//	Update Method - Update (returns nothing)
	public void changeParkData(Park aPark);						//	Update a Park in the table using values in the location object

	//	Delete Method - Delete (returns nothing)
	public void deleteParkById(String aParkId);					//	Delete a Park by it's ID (Primary Key)
	
	public void deleteParkByCampground(String campground);		//	Delete all rows for a Park
	
	
	
	
	
}
